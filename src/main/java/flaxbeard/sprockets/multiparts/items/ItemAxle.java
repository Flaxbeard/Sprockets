package flaxbeard.sprockets.multiparts.items;


import java.util.List;

import mcmultipart.multipart.IMultipart;
import mcmultipart.multipart.IMultipartContainer;
import mcmultipart.multipart.MultipartHelper;
import mcmultipart.multipart.MultipartRegistry;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import flaxbeard.sprockets.Sprockets;
import flaxbeard.sprockets.multiparts.PartAxle;
import flaxbeard.sprockets.multiparts.PartSprocket;

public class ItemAxle extends ItemSprocketMultipart
{
		
	public ItemAxle()
	{
		super("axle");
		MultipartRegistry.registerPart(PartAxle.class, Sprockets.MODID + ":" + name);
		setHasSubtypes(true);
		subnames = new String[] {"wooden", "stone", "iron"};
	}
	
	@Override
	public IMultipart createPart(World world, BlockPos pos, EnumFacing side,
			Vec3 hit, ItemStack stack, EntityPlayer player)
	{
		PartAxle axle = new PartAxle();
		axle.setFacing(side.ordinal());
		
		IMultipartContainer contain = MultipartHelper.getPartContainer(world, pos);
		if (contain != null)
		{
			axle.top = contain.getPartInSlot(PartAxle.TOP_SLOT.get(side.ordinal())) == null;
			axle.bottom = contain.getPartInSlot(PartAxle.BOTTOM_SLOT.get(side.ordinal())) == null;
		}
		
		int damage = stack.getItemDamage();
		if (damage >= subnames.length)
		{
			damage = 0;
		}
		axle.setDamage(damage);
		
		axle.markDirty();
		return axle;
	}

}
