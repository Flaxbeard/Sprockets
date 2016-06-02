package flaxbeard.sprockets.common.util;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class ItemStackHandlerLimited extends ItemStackHandler
{
	private final int limit;
	
	public ItemStackHandlerLimited(int size, int limit)
	{
		super(size); // me
		this.limit = limit;
	}
	
	@Override
	protected int getStackLimit(int slot, ItemStack stack)
    {
		return limit;
    }
}
