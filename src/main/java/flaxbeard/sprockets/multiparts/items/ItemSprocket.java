package flaxbeard.sprockets.multiparts.items;


import java.util.List;

import mcmultipart.multipart.IMultipart;
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
import flaxbeard.sprockets.multiparts.PartSprocket;

public class ItemSprocket extends ItemSprocketMultipart
{
		
	public ItemSprocket()
	{
		super("sprocket");
		MultipartRegistry.registerPart(PartSprocket.class, Sprockets.MODID + ":" + name);
		setHasSubtypes(true);
		subnames = new String[] {"wooden", "stone", "iron"};
	}

	@Override
	public IMultipart createPart(World world, BlockPos pos, EnumFacing side,
			Vec3 hit, ItemStack stack, EntityPlayer player)
	{
		PartSprocket sprocket = new PartSprocket();
		sprocket.setSlot(side.getOpposite().ordinal());
		sprocket.markDirty();
		
		int damage = stack.getItemDamage();
		if (damage >= subnames.length)
		{
			damage = 0;
		}
		sprocket.setDamage(damage);
		
		return sprocket;
	}


}
