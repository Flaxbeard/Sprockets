package flaxbeard.sprockets.api.network;

import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.HashSet;

import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import flaxbeard.sprockets.api.IMechanicalConduit;
import flaxbeard.sprockets.lib.LibConstants;

public class MechanicalNetwork
{
	private class NetworkConnection
	{
		private boolean shouldDirecitonFlip;
		private boolean cis;
		private IMechanicalConduit other;
		private IMechanicalConduit inThis;
		
		private NetworkConnection(IMechanicalConduit other, IMechanicalConduit inThis, boolean shouldDirectionFlip, boolean cis)
		{
			this.other = other;
			this.inThis = inThis;
			this.shouldDirecitonFlip = shouldDirectionFlip;
			this.cis = cis;
		}
	}
	
	private class NetworkLink
	{
		private boolean jammed;
		private boolean cis;
		private boolean shouldDirectionFlip;
		
		private NetworkLink(boolean jammed, boolean cis, boolean shouldDirectionFlip)
		{
			this.jammed = jammed;
			this.cis = cis;
			this.shouldDirectionFlip = shouldDirectionFlip;
		}
	}
	
	private boolean networkJammed = false;

	private boolean jammed = false;
	private BlockPos jammedPoint = null;
	public float rotation = 0;
	public float speed = 0;
	public float cachedSpeed = 0;
	private int numSpeeds = 0;
	public int handSpin = 0;
	World world;
	
	private float minTorque = 0.0F;
	private boolean torqueJammed = false;
	private boolean torqueCapped = false;
	private float maxTorque = 99999.0F;
	
	public HashSet<IMechanicalConduit> conduits;
	public String id;
	
	private float size;
	
	private HashMap<String, NetworkLink> links = new  HashMap<String, NetworkLink>();
	private HashMap<String, ArrayList<NetworkConnection>> connections = new  HashMap<String, ArrayList<NetworkConnection>>();

	private int shouldRecalculateJams;

	private float torque;

	private boolean connJammed = false;



	
	public MechanicalNetwork(String string, float size, World world)
	{
		id = string;
		this.size = size;
		conduits = new HashSet<IMechanicalConduit>();
		this.world = world;
		MechanicalNetworkRegistry.getInstance().register(id, this, world);
	}
	
	
	public void linkNetwork(IMechanicalConduit other, IMechanicalConduit inThis, boolean shouldDirectionFlip, boolean cis)
	{

		if (other.getNetwork() != null)
		{
			//MechanicalNetworkRegistry.getInstance().register(other.getNetwork().id, other.getNetwork(), world);

			if (!this.connections.containsKey(other.getNetwork().id))
			{
				this.connections.put(other.getNetwork().id, new ArrayList<NetworkConnection>());
			}
			this.connections.get(other.getNetwork().id).add(new NetworkConnection(other, inThis, shouldDirectionFlip, cis));
		}
		
	}
	
	
	public float getSizeMultiplier()
	{
		return size;
	}
	
	public int size()
	{
		return conduits.size();
	}

	public void merge(MechanicalNetwork network)
	{
		IMechanicalConduit starting = conduits.iterator().next();
		this.addAllConduits(network.conduits, starting);
		network.conduits = new HashSet<IMechanicalConduit>();
	}
	
	public void addAllConduits(Collection<IMechanicalConduit> conduitsToAdd, IMechanicalConduit base)
	{
		this.conduits.addAll(conduitsToAdd);
		for (IMechanicalConduit conduit : conduitsToAdd)
		{
			conduit.setNetwork(this);
		}
		recalculateStates(base);
	}

	public void addConduit(IMechanicalConduit conduit)
	{
		conduits.add(conduit);
		if (conduits.size() == 1)
		{
			recalculateStates(conduit);
		}
		else
		{
			recalculateStates(conduits.iterator().next());
		}
		conduit.setNetwork(this);
	}
	
	public boolean contains(IMechanicalConduit conduit)
	{
		return conduits.contains(conduit);
	}
	
	public void removeConduit(IMechanicalConduit conduit)
	{
		if (conduits.contains(conduit))
		{
			HashSet<MechanicalNetwork> createdNetworks = new HashSet<MechanicalNetwork>();
			
			for (IMechanicalConduit connectedConduit : MechanicalNetworkHelper.getConnectedConduits(conduit))
			{
				if (this.contains(connectedConduit))
				{
					boolean newNetwork = true;
					for (MechanicalNetwork network : createdNetworks)
					{
						if (network.contains(connectedConduit))
						{
							newNetwork = false;
							break;
						}
					}
					
					if (newNetwork)
					{
						MechanicalNetwork network = MechanicalNetworkRegistry.getInstance().newNetworkFromConduit(connectedConduit, conduit);
						createdNetworks.add(network);
					}
				}
			}
			
			
			for (MechanicalNetwork network : createdNetworks)
			{
				network.rotation = rotation;
			}
			
			this.conduits = new HashSet<IMechanicalConduit>();
		}
	}
	
	public boolean isJammed()
	{

		return networkJammed || this.numSpeeds > 1;
	}
	

	
	public boolean recalculateJams(HashSet<String> history, boolean isJammed)
	{
		history.add(id);
		isJammed = isJammed || this.jammed || this.torqueJammed || this.connJammed;
		for (String key : links.keySet())
		{
			if (!history.contains(key))
			{
				isJammed = isJammed || links.get(key).jammed;
				
				//System.out.println("NET JAMMED? " + links.get(key).jammed);
				
				MechanicalNetwork network = MechanicalNetworkRegistry.getInstance().getNetwork(key, world);
				
				if (network != null)
				{
					isJammed = network.recalculateJams(history, isJammed) || isJammed;
				}
			}
		}
		
		return isJammed;
	}
	
	public void recalculateJams()
	{
		HashSet<String> history = new HashSet<String>();
		boolean isJammed = recalculateJams(history, false);
		
		//System.out.println(isJammed);
		//System.out.println(history);
		//System.out.println(links.size());
		for (String key : history)
		{
			MechanicalNetwork network = MechanicalNetworkRegistry.getInstance().getNetwork(key, world);
			
			if (network != null)
			{
				network.networkJammed = isJammed;
			}
		}
	}

	public void recalculateStates()
	{
		
		recalculateStates(conduits.iterator().next());
		
	}

	private void recalculateStates(IMechanicalConduit next)
	{
		links = new HashMap<String, NetworkLink>();
		connections = new HashMap<String, ArrayList<NetworkConnection>>();
		jammedPoint = MechanicalNetworkHelper.lock(next, conduits, this);
		jammed = jammedPoint != null;
		
		minTorque = 0.0F;
		maxTorque = 99999999.0F;
		
		
		for (IMechanicalConduit conduit : conduits)
		{
			if (conduit.minTorque() > minTorque)
			{
				minTorque = conduit.minTorque();
			}
			if (conduit.maxTorque() <= maxTorque)
			{
				maxTorque = conduit.maxTorque();
			}
		}

		for (String key : connections.keySet())
		{
			MechanicalNetwork network = MechanicalNetworkRegistry.getInstance().getNetwork(key, world);
			if (network != null)
			{
				ArrayList<NetworkConnection> conns = connections.get(key);
				
				boolean lastOpposite = false;
				boolean first = true;
				boolean inconsistantConnection = false;
				
				boolean lastCis = false;
				boolean lastShouldFlip = false;
				for (NetworkConnection l : conns)
				{
					if (first)
					{
						lastOpposite = l.inThis.getState() == l.other.getState();
						lastCis = l.cis;
						lastShouldFlip = l.shouldDirecitonFlip != lastOpposite;
						first = false;
					}
					else
					{
						boolean thisOpposite = l.inThis.getState() == l.other.getState();
						if (lastOpposite != thisOpposite || l.cis != lastCis || lastShouldFlip != (l.shouldDirecitonFlip != thisOpposite))
						{
							inconsistantConnection = true;
						}
						lastOpposite = thisOpposite;
						lastShouldFlip = (l.shouldDirecitonFlip != thisOpposite);
						lastCis = l.cis;
					}
				}
	
				NetworkLink link = new NetworkLink(inconsistantConnection, lastCis, lastShouldFlip);
				this.links.put(key, link);
				network.links.put(this.id, link);
			}
			else
			{
			}
		}

		for (String key : links.keySet())
		{
			MechanicalNetwork network = MechanicalNetworkRegistry.getInstance().getNetwork(key, world);
			if (network != null)
			{
				network.passSpeed(rotation, new HashSet<String>());
			}
		
		}
		this.shouldRecalculateJams = 2;

	}
	
	public void passSpeed(float i, HashSet<String> history)
	{
		history.add(this.id);
		if (!isJammed())
		{
			this.rotation = i;
		}
		for (String key : links.keySet())
		{
			MechanicalNetwork network = MechanicalNetworkRegistry.getInstance().getNetwork(key, world);
			if (!history.contains(key) && network != null)
			{
				network.passSpeed(i, history);
			}
		}
	}
	
	public void addSpeed(float speed, float torque, HashSet<String> history, HashMap<String, Boolean> historyFlip)
	{
		boolean neg = speed > 0;
		history.add(this.id);
		historyFlip.put(this.id, neg);

		if (numSpeeds == 0)
		{
			this.speed = speed;
			this.torque = torque;
			numSpeeds++;
		}
		else
		{
			if (speed == this.speed)
			{
				
				if (this.torque + torque > this.maxTorque)
				{
					float torque2 = this.maxTorque - this.torque;
					this.torque += torque;
					torque = torque2;
				}
				else
				{
					this.torque += torque;
				}
			}
			else
			{
				this.speed = 0;
				this.torque = 0;
				numSpeeds++;
			}
			
		}
			
			
		
		for (String key : links.keySet())
		{
			MechanicalNetwork network = MechanicalNetworkRegistry.getInstance().getNetwork(key, world);
			if (network != null)
			{
			
				if (!history.contains(key)) // TODO make it check even if it already has been passed to make sure it's the same direction
				{
					float speed2 = speed;
					if (!links.get(key).shouldDirectionFlip)
					{
						speed2 *= -1;
					}
					if (links.get(key).cis)
					{
						network.addSpeed(speed2, torque, history, historyFlip);
					}
					else
					{
						network.addSpeed(speed2 * (this.getSizeMultiplier() / network.getSizeMultiplier()),
								torque * (network.getSizeMultiplier() / this.getSizeMultiplier()),
								history, historyFlip);
					}
				}
				else
				{
					
					if (!links.get(key).shouldDirectionFlip)
					{
						neg = !neg;
					}
					
					boolean connJammed = neg != historyFlip.get(key);
					
					if (connJammed != this.connJammed)
					{
						this.connJammed = connJammed;
						this.recalculateJams();
					}
				}
			}
		}
	}

	public void addSpeed(float speed, float torque)
	{

		this.addSpeed(speed, torque, new HashSet<String>(), new HashMap<String, Boolean>());
	}

	public boolean tick()
	{
		
		
		
		if (this.numSpeeds <= 1 && !isJammed())
		{
			this.rotation += speed;
		}

		
		if (this.torque < this.minTorque && !torqueJammed)
		{
			this.torqueJammed = true;
			this.recalculateJams();
		}
		

		
		if (torqueJammed && (this.torque >= this.minTorque))
		{
			this.torqueJammed = false;
			this.recalculateJams();
		}
		
		if (this.torque > this.maxTorque)
		{
			this.torque = this.maxTorque;
			this.torqueCapped = true;
		}
		else
		{
			this.torqueCapped = false;
		}
		
		
		//System.out.println(this.maxTorquePos);
	
		this.cachedSpeed = speed;
		this.speed = 0;

		
		if (shouldRecalculateJams > 0)
		{
			this.shouldRecalculateJams--;
			this.recalculateJams();

		}

		
		if (conduits.size() <= 0)
		{
			for (String key : links.keySet())
			{
				MechanicalNetwork network = MechanicalNetworkRegistry.getInstance().getNetwork(key, world);
				if (network != null && network.links != null && network.links.containsKey(id))
				{
					network.links.remove(id);
				}
			}
		}
		
		try
		{
			for (IMechanicalConduit conduit : conduits)
			{
				if (conduit.getNetwork() != this)
				{
					//conduits.remove(conduit);
				}
			}
		}
		catch (ConcurrentModificationException e1)
		{
			
		}
		
		this.numSpeeds = 0;
		
		if (handSpin > 0)
		{
			handSpin--;
			
			this.addSpeed(LibConstants.CRANK_SPEED, LibConstants.CRANK_TORQUE);
			
		}
		else if (handSpin < 0)
		{
			handSpin++;
			
			this.addSpeed(-LibConstants.CRANK_SPEED, LibConstants.CRANK_TORQUE);
			
		}

		return conduits.size() > 0;
		
	}
	
	public void jamParticles()
	{
		if (conduits.size() > 0 && jammedPoint != null)
		{
			World world = conduits.iterator().next().getWorld();
			for (int i = 0; i < 3; i++)
				world.spawnParticle(EnumParticleTypes.VILLAGER_ANGRY, jammedPoint.getX() + world.rand.nextFloat(), jammedPoint.getY(), jammedPoint.getZ() + world.rand.nextFloat(), 0, 0, 0, 0);
		}
	}


	public void removeConduitTotal(IMechanicalConduit conduit, HashSet<IMechanicalConduit> neighbors)
	{
		if (conduits.contains(conduit))
		{
			HashSet<MechanicalNetwork> createdNetworks = new HashSet<MechanicalNetwork>();
			
			for (IMechanicalConduit connectedConduit : neighbors)
			{
				boolean newNetwork = true;
				for (MechanicalNetwork network : createdNetworks)
				{
					if (network.contains(connectedConduit))
					{
						newNetwork = false;
						break;
					}
				}
				
				if (newNetwork)
				{
					MechanicalNetwork network = MechanicalNetworkRegistry.getInstance().newNetworkFromConduit(connectedConduit, conduit);
					createdNetworks.add(network);
				}
			}
			
			
			for (MechanicalNetwork network : createdNetworks)
			{
				network.rotation = rotation;
			//	System.out.println(network.size());
			}
			this.conduits = new HashSet<IMechanicalConduit>();
		}
	}


	public void addSpeedFromBlock(
			IMechanicalConduit conduit, float f, float g)
	{
		this.addSpeed(f * (conduit.getState() ? 1.0F : -1.0F), g);
	}


	public float getSpeed()
	{
		return isJammed() ? 0 : speed;
	}
	
	public float getCachedSpeed()
	{
		return isJammed() ? 0 : cachedSpeed;
	}

	public float getMaxTorque()
	{
		return this.maxTorque;
	}
	
	public float getMinTorque()
	{
		return this.minTorque;
	}
	
	public boolean isTorqueCapped()
	{
		return this.torqueCapped;
	}
	
	public boolean isTorqueJammed()
	{
		return this.torqueJammed;
	}

	public float getTorque()
	{
		return torque;
	}


}
