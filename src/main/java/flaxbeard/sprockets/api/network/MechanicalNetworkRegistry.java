package flaxbeard.sprockets.api.network;

import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import mcmultipart.event.PartEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent.Load;
import net.minecraftforge.event.world.WorldEvent.Unload;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import flaxbeard.sprockets.api.IMechanicalConduit;
import flaxbeard.sprockets.api.PartMechanicalConduit;

public class MechanicalNetworkRegistry
{
	
	private static MechanicalNetworkRegistry INSTANCE = new MechanicalNetworkRegistry();
	
	private Map<Integer, Map<String, MechanicalNetwork>> networks = new HashMap<Integer, Map<String, MechanicalNetwork>>();
	
	public static void initialize()
	{
		FMLCommonHandler.instance().bus().register(INSTANCE);
		MinecraftForge.EVENT_BUS.register(INSTANCE);
    }
	
	public static MechanicalNetworkRegistry getInstance()
	{
		return INSTANCE;
	}

	
	@SubscribeEvent
	public void onTick(TickEvent.ClientTickEvent e)
	{
		if (e.phase == TickEvent.Phase.START && !Minecraft.getMinecraft().isGamePaused())
		{

			
			try
			{
				for (Integer dimension : networks.keySet())
				{
					
					for (MechanicalNetwork network : networks.get(dimension).values())
					{
						network.clientTick();
					}
					
				}
			}
			catch (ConcurrentModificationException e1)
			{
				
			}
		}
	}
	
	@SubscribeEvent
	public void onTick(TickEvent.ServerTickEvent e)
	{
		if (e.phase == TickEvent.Phase.END)
		{
			
			
			Set<MechanicalNetwork> toRemove = new HashSet<MechanicalNetwork>();
			try
			{
				for (Integer dimension : networks.keySet())
				{
					
					for (MechanicalNetwork network : networks.get(dimension).values())
					{
						if (!network.tick())
						{
							toRemove.add(network);
						}
					}
					for (MechanicalNetwork remove : toRemove)
					{
						if (networks.get(dimension) != null)
						{
							networks.get(dimension).remove(remove.id);
						}
					}

					if (dimension == 0)
					{
						//System.out.println(networks.get(dimension).size());
					}
				}
			}
			catch (ConcurrentModificationException e1)
			{
				
			}
			//System.out.println(e.side);
		}
		
	}

	public static MechanicalNetwork newOrJoin(IMechanicalConduit conduit)
	{
		Set<IMechanicalConduit> connected = MechanicalNetworkHelper.getConnectedConduits(conduit);
		Set<MechanicalNetwork> networks = new HashSet<MechanicalNetwork>();
		MechanicalNetwork toReturn = null;
		int joined = 0;
		
		if (connected.size() > 0)
		{
			for (IMechanicalConduit conn : connected)
			{
				MechanicalNetwork network = conn.getNetwork();
				if (network != null)
				{
					networks.add(network);
				}
			}
			
			if (networks.size() > 0)
			{
				
				for (MechanicalNetwork network : networks)
				{
					if (toReturn == null)
					{
						toReturn = network;
						toReturn.addConduit(conduit);
					}
					else
					{
						toReturn.merge(network);
						joined++;
					}
				
				}
				
				if (joined > 0)
				{
					toReturn.recalculateStates();
				}
			}
		}
		
		if (toReturn == null)
		{
			toReturn = new MechanicalNetwork(UUID.randomUUID().toString(), conduit.getWorldMC());
			toReturn.addConduit(conduit);
			//toReturn.rotation = conduit.savedRotation()
		}
		
		
		return toReturn;
	}
	
	public void register(String id, MechanicalNetwork mechanicalNetwork, World dimension)
	{
		register(id, mechanicalNetwork, dimension.provider.getDimension());
	}

	public void register(String id, MechanicalNetwork mechanicalNetwork, int dimension)
	{
		networks.get(dimension).put(id, mechanicalNetwork);
	}
	
	public MechanicalNetwork getNetwork(String id, World dimension)
	{
		return getNetwork(id, dimension.provider.getDimension());
	}
	
	public MechanicalNetwork getNetwork(String id, int dimension)
	{
		if (networks.get(dimension) != null)
		{
			return networks.get(dimension).get(id);
		}
		return null;
	}

	public MechanicalNetwork newNetworkFromConduit(
			IMechanicalConduit conduit, IMechanicalConduit ignore)
	{
		MechanicalNetwork toReturn = new MechanicalNetwork(UUID.randomUUID().toString(), conduit.getWorldMC());
		Set<IMechanicalConduit> conduits = MechanicalNetworkHelper.crawl(conduit, ignore);
		toReturn.addAllConduits(conduits, conduits.iterator().next());

		return toReturn;
	}
	
	@SubscribeEvent
	public void loadWorld(Load loadEvent)
	{
		if (!hasDimension(loadEvent.getWorld()))
			this.networks.put(loadEvent.getWorld().provider.getDimension(), new HashMap<String, MechanicalNetwork>());
	}
	
	@SubscribeEvent
	public void unloadWorld(Unload unloadEvent)
	{
		this.networks.remove(unloadEvent.getWorld().provider.getDimension());
	}

	public boolean hasDimension(World worldObj)
	{
		return this.networks.containsKey(worldObj.provider.getDimension());
	}

}
