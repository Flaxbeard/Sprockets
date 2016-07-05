package flaxbeard.sprockets.api;

import java.util.HashSet;
import java.util.Set;

import mcmultipart.multipart.PartSlot;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
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
	private float multiplier = 1;
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
	public void setMultiplier(float multiplier)
	{
		this.multiplier = multiplier;
	}

	@Override
	public float getMultiplier()
	{
		return this.multiplier;
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
		if (getNetwork() != null)
		{
			getNetwork().removeConduit(this);
			setNetwork(null);
		}
	
		super.invalidate();
	}
	
	@Override
	public void remove()
	{
		this.getWorldMC().destroyBlock(getPosMC(), true);
	}
	
	@Override
	public BlockPos getPosMC()
	{
		return getPos();
	}
	
	@Override
	public World getWorldMC()
	{
		return getWorld();
	}
	
	@Override
	public float getSpeed()
	{
		return getNetwork().getSpeedForConduit(this);
	}

	@Override
	public float getTorque()
	{
		return getNetwork().getTorqueForConduit(this);
	}
	
	private static final Set<Tuple<Vec3i, PartSlot>> emptyMultipart = new HashSet<Tuple<Vec3i, PartSlot>>();
	
	@Override
	public Set<Tuple<Vec3i, PartSlot>> multipartLinearConnections()
	{
		return emptyMultipart;
	}
	
	private static final Set<Vec3i> empty = new HashSet<Vec3i>();
	
	@Override
	public Set<Vec3i> linearConnections()
	{
		return empty;
	}
	
}
