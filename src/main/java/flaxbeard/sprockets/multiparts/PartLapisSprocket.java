package flaxbeard.sprockets.multiparts;

import mcmultipart.multipart.IRedstonePart.ISlottedRedstonePart;
import net.minecraft.util.EnumFacing;

public class PartLapisSprocket extends PartSprocket implements ISlottedRedstonePart
{
	private int cachedValue;
	private float cachedTorque;
	
	@Override
	public boolean canConnectRedstone(EnumFacing side)
	{
		return true;
	}
	
	@Override
	public void update()
	{
		super.update();
		
		if (initialized)
		{
			float torque = getNetwork().isJammed() ? 0 : getNetwork().getTorque();
			if (cachedTorque != torque)
			{
				cachedTorque = torque;
				cachedValue = (int) (15F * (cachedTorque / this.maxTorque()));
				getWorldMC().notifyNeighborsOfStateChange(getPosMC(), getWorldMC().getBlockState(getPosMC()).getBlock());
			}
		}

	}
	
	@Override
	protected void initialize()
	{
		super.initialize();
		cachedTorque = getNetwork().getTorque();
		cachedValue = (int) (15F * (cachedTorque / this.maxTorque()));
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
