package flaxbeard.sprockets.multiparts;

import flaxbeard.sprockets.lib.LibConstants;
import mcmultipart.multipart.IRedstonePart.ISlottedRedstonePart;
import net.minecraft.util.EnumFacing;

public class PartRedstoneSprocket extends PartSprocket implements ISlottedRedstonePart
{
	private int cachedValue;
	private float cachedSpeed;
	
	@Override
	public boolean canConnectRedstone(EnumFacing side)
	{
		return true;
	}
	
	@Override
	public void update()
	{
		super.update();

		float speed = getNetwork().isJammed() ? 0 : Math.abs(getNetwork().getCachedSpeed());
		if (cachedSpeed != speed)
		{
			cachedSpeed = speed;
			cachedValue = (int) Math.min((15F * (cachedSpeed / (this.maxSpeed()))), 15);
			getWorldMC().notifyNeighborsOfStateChange(getPosMC(), getWorldMC().getBlockState(getPosMC()).getBlock());
		}
	}
	
	@Override
	protected void initialize()
	{
		super.initialize();
		cachedSpeed = getNetwork().isJammed() ? 0 : Math.abs(getNetwork().getCachedSpeed());
		cachedValue = (int) Math.min((15F * (cachedSpeed / (this.maxSpeed()))), 15);
	}

	@Override
	public int getWeakSignal(EnumFacing side)
	{
		if (!initialized)
		{
			initialize();
		}
		return cachedValue;
	}

	@Override
	public int getStrongSignal(EnumFacing side)
	{
		if (!initialized)
		{
			initialize();
		}
		return cachedValue;
	}

}
