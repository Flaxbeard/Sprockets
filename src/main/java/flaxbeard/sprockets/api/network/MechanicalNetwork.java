package flaxbeard.sprockets.api.network;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import flaxbeard.sprockets.api.IExcessTorqueConsumer;
import flaxbeard.sprockets.api.IMechanicalConduit;
import flaxbeard.sprockets.api.IMechanicalConsumer;
import flaxbeard.sprockets.api.IMechanicalProducer;

public class MechanicalNetwork
{
	private boolean jammed = false;
	private boolean powerJammed = false;
	
	public float rotation = 0;
	public float speed = 0;
	public int handSpin = 0;
	World world;
	
	private float minTorque = 0.0F;
	private BlockPos minTorqueLoc = null;
	private boolean torqueJammed = false;
	private boolean torqueCapped = false;
	private float maxTorque = 99999.0F;
	private boolean hasPower = false;
	
	public Set<IMechanicalConduit> conduits;
	private Map<IMechanicalConsumer, Float> consumers;
	private Map<IMechanicalProducer, Float> producers;
	private Map<IMechanicalProducer, Float> producerSpeed;
	private Map<IExcessTorqueConsumer, Float> torqueConsumers;
	public float consumerTorqueNeeded = 0.0F;
	public String id;
	
	private BlockPos jam;
	
	private float torque;

	private IMechanicalConduit parent;
	private float torqueSurplus;

	
	public MechanicalNetwork(String string, World world)
	{
		id = string;
		conduits = new HashSet<IMechanicalConduit>();
		consumers = new HashMap<IMechanicalConsumer, Float>();
		producers = new HashMap<IMechanicalProducer, Float>();
		producerSpeed = new HashMap<IMechanicalProducer, Float>();
		torqueConsumers = new HashMap<IExcessTorqueConsumer, Float>();
		this.world = world;
		parent = null;
		MechanicalNetworkRegistry.getInstance().register(id, this, world);
	}
	
	public IMechanicalConduit getParent()
	{
		if (parent == null)
		{
			parent = conduits.iterator().next();
		}
		return parent;
	}
	
	
	public void linkNetwork(IMechanicalConduit other, IMechanicalConduit inThis, boolean shouldDirectionFlip, boolean cis)
	{

		if (other.getNetwork() != null)
		{
			//MechanicalNetworkRegistry.getInstance().register(other.getNetwork().id, other.getNetwork(), world);
		}
		
	}
	
	
	public int size()
	{
		return conduits.size();
	}

	public void merge(MechanicalNetwork network)
	{
		IMechanicalConduit starting = getParent();
		this.addAllConduits(network.conduits, starting);
		network.resetAll();
	}
	
	public void updateNetworkSpeedAndTorque()
	{
		powerJammed = false;
		this.consumerTorqueNeeded = 0.0F;
		float excessTorqueNeeded = 0.0F;
		for (IMechanicalConsumer con : consumers.keySet())
		{
			if (con.torqueCost() == -1)
			{
				powerJammed = true;
				jam = con.getPosMC();
			}
			consumerTorqueNeeded += con.torqueCost() * con.getMultiplier();
		}
		
		for (IExcessTorqueConsumer con : torqueConsumers.keySet())
		{
			if (con.torqueCost() == -1)
			{
				powerJammed = true;
				jam = con.getPosMC();
			}
			excessTorqueNeeded += con.torqueCost() * con.getMultiplier();
		}
		
		this.torque = 0.0F;
		this.speed = 0.0F;
		hasPower = false;
		for (IMechanicalProducer prod : producers.keySet())
		{
			float prodSpeed = prod.speedProduced() * (prod.getState() ? -1 : 1) / prod.getMultiplier();
			
			if (prodSpeed != 0.0F)
			{
				if (speed == 0.0F)
				{
					speed += prodSpeed ;
					torque += prod.torqueProduced() * prod.getMultiplier();
					hasPower = true;
				}
				else if (speed == prodSpeed)
				{
					torque += prod.torqueProduced() * prod.getMultiplier();
				}
				else /*if (speed < 0 && prodSpeed > 0 || speed > 0 && prodSpeed < 0)*/
				{
					powerJammed = true;
					jam = prod.getPosMC();
				}
				/*else
				{
					if (Math.abs(prodSpeed) < Math.abs(speed))
					{
						speed = prodSpeed;
						torque += prod.torqueProduced() * prod.getMultiplier();
					}
				}*/
			}

		}
		
		if (this.torque > this.maxTorque)
		{
			this.torque = this.maxTorque;
			this.torqueCapped = true;
		}
		
		this.torqueSurplus = torque - consumerTorqueNeeded;
		float totalSurplus = torqueSurplus;
		this.torqueSurplus /= this.torqueConsumers.size();
		
		
		if ((consumerTorqueNeeded > 0 || excessTorqueNeeded > 0) && (consumerTorqueNeeded > torque || excessTorqueNeeded >= totalSurplus))
		{
			powerJammed = true;
			if (consumers.size() > 0)
			{
				jam = consumers.keySet().iterator().next().getPosMC();
			}
			else if (torqueConsumers.size() > 0)
			{
				jam = torqueConsumers.keySet().iterator().next().getPosMC();
			}
		}
				
	}



	public void addAllConduits(Collection<IMechanicalConduit> conduitsToAdd, IMechanicalConduit base)
	{
		for (IMechanicalConduit conduit : conduitsToAdd)
		{
			if (parent == null)
			{
				parent = conduit;
			}
			conduits.add(conduit);
			
			if (conduit instanceof IMechanicalConsumer)
			{
				IMechanicalConsumer consumer = (IMechanicalConsumer) conduit;
				consumers.put(consumer, consumer.torqueCost());
			}
			if (conduit instanceof IExcessTorqueConsumer)
			{
				IExcessTorqueConsumer consumer = (IExcessTorqueConsumer) conduit;
				torqueConsumers.put(consumer, 1f);
			}
			if (conduit instanceof IMechanicalProducer)
			{
				IMechanicalProducer producer = (IMechanicalProducer) conduit;
				producers.put(producer, producer.torqueProduced());
				producerSpeed.put(producer, producer.speedProduced());
			}
			conduit.setNetwork(this);
		}
		recalculateStates();

	}

	public void addConduit(IMechanicalConduit conduit)
	{
		conduits.add(conduit);
		if (parent == null)
		{
			parent = conduit;
		}
		
		if (conduit instanceof IMechanicalConsumer)
		{
			IMechanicalConsumer consumer = (IMechanicalConsumer) conduit;
			consumers.put(consumer, consumer.torqueCost());
		}
		if (conduit instanceof IMechanicalProducer)
		{
			IMechanicalProducer producer = (IMechanicalProducer) conduit;
			producers.put(producer, producer.torqueProduced());
			producerSpeed.put(producer, producer.speedProduced());
		}
		if (conduit instanceof IExcessTorqueConsumer)
		{
			IExcessTorqueConsumer consumer = (IExcessTorqueConsumer) conduit;
			torqueConsumers.put(consumer, 1f);
		}
		

		conduit.setNetwork(this);

		recalculateStates();
		
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
			Set<IMechanicalConduit> connected = MechanicalNetworkHelper.getConnectedConduits(conduit);

			for (IMechanicalConduit connectedConduit : connected)
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
			
			

			resetAll();
		}
	}
	
	public void resetAll()
	{
		this.conduits = new HashSet<IMechanicalConduit>();
		parent = null;
		consumers = new HashMap<IMechanicalConsumer, Float>();
		producers = new HashMap<IMechanicalProducer, Float>();
		producerSpeed = new HashMap<IMechanicalProducer, Float>();
		torqueConsumers = new HashMap<IExcessTorqueConsumer, Float>();
	}
	
	public boolean isJammed()
	{

		return jammed || torqueJammed || powerJammed;
	}
	
	public boolean isInternallyJammed()
	{
		return jammed;
	}


	public void recalculateStates()
	{
		recalculateStates(getParent());
		
	}

	private void recalculateStates(IMechanicalConduit next)
	{
		BlockPos result = MechanicalNetworkHelper.lock(next, conduits);
		jam = result;
		jammed = result != null;

		minTorque = 0.0F;
		maxTorque = 99999999.0F;
		
		
		for (IMechanicalConduit conduit : conduits)
		{
			float cMinTorque = conduit.minTorque() * conduit.getMultiplier();
			float cMaxTorque = conduit.maxTorque() * conduit.getMultiplier();
			if (cMinTorque > minTorque)
			{
				minTorque = cMinTorque;
				minTorqueLoc = conduit.getPosMC();
			}
			if (cMaxTorque <= maxTorque)
			{
				maxTorque = cMaxTorque;
			}
		}

		
	
		this.updateNetworkSpeedAndTorque();
		
		if (this.torque > this.maxTorque)
		{
			this.torque = this.maxTorque;
			this.torqueCapped = true;
		}
		else
		{
			this.torqueCapped = false;
		}

		

	}

	public boolean tick()
	{
		torqueJammed = (this.torque < this.minTorque);
		if (torqueJammed)
		{
			jam = minTorqueLoc;
		}
		
		
//
//		
//		if ((this.torque < this.minTorque || this.torque < this.consumerTorqueNeeded)&& !torqueJammed)
//		{
//			this.torqueJammed = true;
//		}
//		
//
//		
//		if (torqueJammed && (this.torque >= this.minTorque && this.torque >= this.consumerTorqueNeeded))
//		{
//			this.torqueJammed = false;
//		}
//
//
//		
		
		
		if (handSpin > 0)
		{
			handSpin--;
			//this.speed = 5;
			//this.addSpeed(LibConstants.CRANK_SPEED, LibConstants.CRANK_TORQUE);
			
		}
		else if (handSpin < 0)
		{
			handSpin++;
			//this.speed = -5;
			//this.addSpeed(-LibConstants.CRANK_SPEED, LibConstants.CRANK_TORQUE);
			
		}

		
		if (!isJammed() && !this.world.isRemote)
		{
			this.rotation += speed;
		}

		return conduits.size() > 0;
		
	}



	public void removeConduitTotal(IMechanicalConduit conduit, Set<IMechanicalConduit> neighbors)
	{
		if (conduits.contains(conduit))
		{
			Set<MechanicalNetwork> createdNetworks = new HashSet<MechanicalNetwork>();
			
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
				//network.rotation = rotation;
			}
			resetAll();
		}
	}



	public float getSpeed()
	{
		return isJammed() ? 0 : speed;
	}
	
	public float getCachedSpeed()
	{
		return getSpeed();
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

	public float getSpeedForConduit(IMechanicalConduit conduit)
	{
		return getSpeed() * conduit.getMultiplier();
	}
	
	public float getTorqueForConduit(IMechanicalConduit conduit)
	{
		return getTorque() / conduit.getMultiplier();
	}

	public void clientTick()
	{
		if (!isJammed() && this.world.isRemote)
		{
			this.rotation =  this.rotation + speed;
		}
		else
		{
	
			if (jam != null && world.isRemote && hasPower)
			{
				world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, 
						jam.getX() + world.rand.nextFloat(), 
						jam.getY() + world.rand.nextFloat(), 
						jam.getZ() + world.rand.nextFloat(), 
						0.0F, 
						0.0F, 
						0.0F, 
						new int[] { 5, 255, 255 });
			}
		}
	}

	public float getSurplusForConduit(IMechanicalConduit conduit)
	{
		return this.torqueSurplus / conduit.getMultiplier();
	}

}
