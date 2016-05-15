package flaxbeard.sprockets.items;

import flaxbeard.sprockets.Sprockets;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemSprocketBook extends ItemSprocketBase
{

	public ItemSprocketBook()
	{
		super("book");
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World world, EntityPlayer playerIn, EnumHand hand)
	{
		playerIn.openGui(Sprockets.INSTANCE, 0, world, 0, 0, 0);
		return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
	}
}
