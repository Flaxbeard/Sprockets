package flaxbeard.sprockets.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.item.ItemStack;
import flaxbeard.sprockets.api.book.BookCategory;
import flaxbeard.sprockets.api.book.BookEntry;

public class SprocketsAPI
{
	private static final List<BookCategory> categories = new ArrayList<BookCategory>();
	private static final List<BookEntry> entries = new ArrayList<BookEntry>();
	
	private static final Map<ItemStack, ItemStack> millstoneRecipes = new HashMap<ItemStack, ItemStack>();

	public static void addCategory(BookCategory category)
	{
		categories.add(category);
	}
	
	public static List<BookCategory> getCategories()
	{
		return categories;
	}
	
	public static void addEntry(BookEntry entry)
	{
		entries.add(entry);
		if (entry.getCategory() != null)
		{
			entry.getCategory().addEntry(entry);
		}
	}
	
	public static void addMillstoneRecipe(ItemStack in, ItemStack out)
	{
		millstoneRecipes.put(in, out);
	}
	
	private static boolean compareItemStacks(ItemStack stack1, ItemStack stack2)
	{
		return stack2.getItem() == stack1.getItem() && (stack2.getMetadata() == 32767 || stack2.getMetadata() == stack1.getMetadata());
	}
	
	public static ItemStack getMillstoneResult(ItemStack stack)
	{
		for (Entry<ItemStack, ItemStack> entry : millstoneRecipes.entrySet())
		{
			if (compareItemStacks(stack, (ItemStack)entry.getKey()))
			{
				return (ItemStack)entry.getValue().copy();
			}
		}
		
		return null;
	}
}

