package flaxbeard.sprockets.api.tool;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author Flaxbeard
 *
 * Implement this interface if your block/te/multipart should have an effect when
 * the Wrench is used on it.
 */
public interface IWrenchable
{
	public boolean wrench(EntityPlayer player, World world, BlockPos pos, IBlockState state, EnumFacing side);
}
