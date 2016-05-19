package flaxbeard.sprockets.multiparts.items;

import java.util.List;

import mcmultipart.item.ItemMultiPart;
import mcmultipart.multipart.IMultipart;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import flaxbeard.sprockets.Sprockets;

public abstract class ItemSprocketMultipart extends ItemMultiPart
{
	public String name;
	public String[] subnames;
	
	public ItemSprocketMultipart(String name)
	{
		this.setRegistryName(name);
		GameRegistry.register(this);
		this.setUnlocalizedName(Sprockets.MODID + "." + name);
		this.setCreativeTab(Sprockets.creativeTab);
		this.name = name;
		subnames = new String[0];
	}
	
	private static final float EDGELOW = 3F / 16F;
	private static final float EDGEHIGH = 13F / 16F;
	
	public abstract boolean hasBoundingBox();
	
	public abstract AxisAlignedBB boundingBox(World world, BlockPos blockpos, EnumFacing hitFace, Vec3d hitVec);
	
	public static EnumFacing getFaceBasedOnPos(Vec3d hit, EnumFacing hitFace)
	{
		switch (hitFace)
		{
			case DOWN:
			case UP:
				if (hit.xCoord < EDGELOW && Math.abs(hit.xCoord) < Math.abs(hit.zCoord))
				{
					return EnumFacing.WEST;
				}
				else if (hit.xCoord > EDGEHIGH && Math.abs(hit.xCoord) > Math.abs(hit.zCoord))
				{
					return EnumFacing.EAST;
				}
				else if (hit.zCoord < EDGELOW && Math.abs(hit.zCoord) < Math.abs(hit.xCoord))
				{
					return EnumFacing.NORTH;
				}
				else if (hit.zCoord > EDGEHIGH && Math.abs(hit.zCoord) > Math.abs(hit.xCoord))
				{
					return EnumFacing.SOUTH;
				}
				break;
			case NORTH:
			case SOUTH:
				if (hit.xCoord < EDGELOW && Math.abs(hit.xCoord) < Math.abs(hit.yCoord))
				{
					return EnumFacing.WEST;
				}
				else if (hit.xCoord > EDGEHIGH && Math.abs(hit.xCoord) > Math.abs(hit.yCoord))
				{
					return EnumFacing.EAST;
				}
				else if (hit.yCoord < EDGELOW && Math.abs(hit.yCoord) < Math.abs(hit.xCoord))
				{
					return EnumFacing.DOWN;
				}
				else if (hit.yCoord > EDGEHIGH && Math.abs(hit.yCoord) > Math.abs(hit.xCoord))
				{
					return EnumFacing.UP;
				}
				break;
			case EAST:
			case WEST:
				if (hit.yCoord < EDGELOW && Math.abs(hit.yCoord) < Math.abs(hit.zCoord))
				{
					return EnumFacing.DOWN;
				}
				else if (hit.yCoord > EDGEHIGH && Math.abs(hit.yCoord) > Math.abs(hit.zCoord))
				{
					return EnumFacing.UP;
				}
				else if (hit.zCoord < EDGELOW && Math.abs(hit.zCoord) < Math.abs(hit.yCoord))
				{
					return EnumFacing.NORTH;
				}
				else if (hit.zCoord > EDGEHIGH && Math.abs(hit.zCoord) > Math.abs(hit.yCoord))
				{
					return EnumFacing.SOUTH;
				}
				break;
		}
		return hitFace;
	}
	
	
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list)
	{
		if (subnames.length == 0)
		{
			list.add(new ItemStack(this));
		}
		for (int i = 0; i < subnames.length; i++)
		{
			list.add(new ItemStack(this, 1, i));
		}
	}
	
	@Override
	public String getUnlocalizedName(ItemStack itemstack)
	{
		int damage = itemstack.getItemDamage();
		if (damage >= subnames.length)
		{
			return super.getUnlocalizedName();
		}
		return super.getUnlocalizedName(itemstack) + "." + subnames[damage];
	}


	
	@Override
	public abstract IMultipart createPart(World world, BlockPos pos, EnumFacing side, Vec3d hit, ItemStack stack, EntityPlayer player);
}
