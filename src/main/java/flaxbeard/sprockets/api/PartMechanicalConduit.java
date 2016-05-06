package flaxbeard.sprockets.api;

import java.util.List;

import mcmultipart.multipart.Multipart;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import flaxbeard.sprockets.api.network.MechanicalNetwork;
import flaxbeard.sprockets.api.network.MechanicalNetworkRegistry;


/**
 * @author Flaxbeard
 *
 * A base class for Multiparts that are mechanical conduits.
 * 
 */
public abstract class PartMechanicalConduit extends Multipart implements IMechanicalConduit, ITickable
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
		if (MechanicalNetworkRegistry.getInstance().hasDimension(this.getWorld()))
		{
			MechanicalNetworkRegistry.newOrJoin(this);
			this.initialized = true;
		}
	}
	
	@Override
	public void onRemoved()
	{
		if (this.getNetwork() != null)
		{
			this.getNetwork().removeConduit(this);
		}
		
	}

	@Override
	public float maxSpeed()
	{
		return 0;
	}

	@Override
	public float minTorque()
	{
		return 0;
	}

	@Override
	public float maxTorque()
	{
		return 0;
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
	public void remove()
	{
		BlockPos pos = getPos();
		if (this.getWorld() != null)
		{
			if (!this.getWorld().isRemote)
			{
				List<ItemStack> drops = this.getDrops();
				for (ItemStack drop : drops)
				{
					EntityItem item = new EntityItem(getWorld(), pos.getX(), pos.getY(), pos.getZ(), drop);
					getWorld().spawnEntityInWorld(item);
				}
				this.getContainer().removePart(this);
				System.out.println("REMOVING");

			}
			
		}

		
	}
	

}
