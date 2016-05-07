package flaxbeard.sprockets.api;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import flaxbeard.sprockets.api.network.MechanicalNetwork;
import flaxbeard.sprockets.api.network.MechanicalNetworkRegistry;

/**
 * @author Flaxbeard
 *
 * A base class for TileEntities that are mechanical conduits.
 * 
 */
public abstract class TileEntityMechanicalConduit extends TileEntity implements IMechanicalConduit, ITickable
{
	public boolean initialized = false;
	private boolean state = false;
	private MechanicalNetwork network = null;

	@Override
	public void update()
	{
		if (!initialized)
		{
			this.initialize();
		}
	}
	
	private void initialize()
	{
		if (MechanicalNetworkRegistry.getInstance().hasDimension(this.worldObj))
		{
			MechanicalNetworkRegistry.newOrJoin(this);
			this.initialized = true;
		}
		
	}

	@Override
	public float maxSpeed()
	{
		return 9999999F;
	}

	@Override
	public float minTorque()
	{
		return 0;
	}

	@Override
	public float maxTorque()
	{
		return 9999999F;
	}

	@Override
	public float sizeMultiplier()
	{
		return 1F;
	}

	@Override
	public void setState(boolean state)
	{
		this.state = state;
	}

	@Override
	public boolean getState()
	{
		return this.state;
	}

	@Override
	public MechanicalNetwork getNetwork()
	{
		return this.network;
	}

	@Override
	public void setNetwork(MechanicalNetwork network)
	{
		this.network = network;
	}
	
	@Override
	public void invalidate()
	{
		getNetwork().removeConduit(this);
		setNetwork(null);
		super.invalidate();
	}
	
	@Override
	public void remove()
	{
		this.getWorld().destroyBlock(getPos(), true);
	}

}
