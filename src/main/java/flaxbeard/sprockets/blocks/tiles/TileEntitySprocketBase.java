package flaxbeard.sprockets.blocks.tiles;

import net.minecraft.nbt.NBTTagCompound;
import flaxbeard.sprockets.api.TileEntityMechanicalConduit;

public abstract class TileEntitySprocketBase extends TileEntityMechanicalConduit
{
	//@Override
	//public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
	//{
		//return (oldState.getBlock() != newState.getBlock());
	//}
	
	@Override
	public NBTTagCompound getUpdateTag()
	{
		return writeToNBT(new NBTTagCompound());
	}
	
}
