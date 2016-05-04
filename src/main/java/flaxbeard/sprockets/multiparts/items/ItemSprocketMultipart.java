package flaxbeard.sprockets.multiparts.items;

import java.util.List;

import mcmultipart.item.ItemMultiPart;
import mcmultipart.multipart.IMultipart;
import mcmultipart.multipart.MultipartRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import flaxbeard.sprockets.Sprockets;
import flaxbeard.sprockets.multiparts.PartSprocketBase;

public abstract class ItemSprocketMultipart extends ItemMultiPart
{
	public String name;
	public String[] subnames;
	
	public ItemSprocketMultipart(String name)
	{
		this.setRegistryName(name);
		GameRegistry.registerItem(this);
		this.setUnlocalizedName(Sprockets.MODID + "." + name);
		this.setCreativeTab(Sprockets.creativeTab);
		this.name = name;
		subnames = new String[0];
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
	public abstract IMultipart createPart(World world, BlockPos pos, EnumFacing side, Vec3 hit, ItemStack stack, EntityPlayer player);
}
