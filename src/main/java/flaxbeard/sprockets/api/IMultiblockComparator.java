package flaxbeard.sprockets.api;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public interface IMultiblockComparator
{
	public boolean isEqual(IBlockAccess iba, BlockPos pos);
}
