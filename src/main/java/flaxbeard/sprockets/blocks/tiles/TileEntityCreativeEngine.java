package flaxbeard.sprockets.blocks.tiles;

import flaxbeard.sprockets.lib.LibConstants;
import net.minecraft.tileentity.TileEntity;

public class TileEntityCreativeEngine extends TileEntityRedstoneEngine
{
	@Override
	public float torqueProduced()
	{
		return isOn() ? LibConstants.CREATIVE_ENGINE_TORQUE : 0;
	}

	@Override
	public float speedProduced()
	{
		return isOn() ? LibConstants.CREATIVE_ENGINE_SPEED * (directionFlipped ? 1 : -1) : 0;
	}
}
