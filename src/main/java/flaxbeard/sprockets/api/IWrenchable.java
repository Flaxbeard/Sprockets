package flaxbeard.sprockets.api;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IWrenchable
{
	public boolean wrench(EntityPlayer player, World world, BlockPos pos, IBlockState state, EnumFacing side);
}
