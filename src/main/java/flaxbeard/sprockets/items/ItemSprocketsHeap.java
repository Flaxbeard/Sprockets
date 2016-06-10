package flaxbeard.sprockets.items;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.translation.I18n;

public class ItemSprocketsHeap extends ItemSprocketBase
{

	public ItemSprocketsHeap(String[] subnames)
	{
		super("oreHeap");
		this.subnames = subnames;
        this.setHasSubtypes(true);
	}
	
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list)
	{
		for (int i = 0; i < SprocketsItems.availableHeaps.size(); i++)
		{
			ItemStack s = new ItemStack(this, 1, SprocketsItems.availableHeaps.get(i));
			//s.setTagCompound(new NBTTagCompound());
			//s.getTagCompound().setString("oreName", SprocketsItems.oreNames.get(i));
			list.add(s);
		}
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack stack)
	{
		if (stack.hasTagCompound() && stack.getTagCompound().hasKey("oreName"))
		{
			return I18n.translateToLocal("item.sprockets.oreHeap.prefix.name") + 
					I18n.translateToLocal(stack.getTagCompound().getString("oreName") + ".name") +
					I18n.translateToLocal("item.sprockets.oreHeap.postfix.name");
		}
		else
		{
			if (SprocketsItems.availableHeaps.contains(stack.getMetadata()))
			{
				return I18n.translateToLocal("item.sprockets.oreHeap.prefix.name") + 
						I18n.translateToLocal(SprocketsItems.oreNames.get(stack.getMetadata()) + ".name") +
						I18n.translateToLocal("item.sprockets.oreHeap.postfix.name");
			}
		}
		return super.getItemStackDisplayName(stack);
	}

}
