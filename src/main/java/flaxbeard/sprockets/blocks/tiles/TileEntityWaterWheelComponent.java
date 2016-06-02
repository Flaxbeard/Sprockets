package flaxbeard.sprockets.blocks.tiles;

import java.util.ArrayList;

import flaxbeard.sprockets.api.Multiblock;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class TileEntityWaterWheelComponent extends TileEntity
{
	private BlockPos center;

	public void setCenter(BlockPos center)
	{
		this.center = center;
	}
	
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);

		center = new BlockPos(
				compound.getInteger("centerX"),
				compound.getInteger("centerY"),
				compound.getInteger("centerZ"));
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		compound = super.writeToNBT(compound);

		if (center != null)
		{
			compound.setInteger("centerX", center.getX());
			compound.setInteger("centerY", center.getY());
			compound.setInteger("centerZ", center.getZ());
		}
		return compound;
	}
	
	@Override
	public void invalidate()
	{
		if (center != null)
		{
			TileEntity te = worldObj.getTileEntity(center);
			if (te != null && te instanceof TileEntityWaterWheel)
			{
				((TileEntityWaterWheel) te).destroy();
			}
		}
		super.invalidate();
	}
}
