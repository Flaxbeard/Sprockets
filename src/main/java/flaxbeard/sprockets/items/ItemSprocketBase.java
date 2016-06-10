package flaxbeard.sprockets.items;

import java.util.List;

import flaxbeard.sprockets.Sprockets;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemSprocketBase extends Item
{
	public final String name;
	public String[] subnames;

	
	public ItemSprocketBase(String name)
	{
		this.setRegistryName(name);
		GameRegistry.register(this);
		this.setUnlocalizedName(Sprockets.MODID + "." + name);
		this.setCreativeTab(Sprockets.creativeTab);
		this.name = name;
		subnames = new String[0];
        this.setMaxDamage(0);

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

}
