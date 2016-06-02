package flaxbeard.sprockets.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import flaxbeard.sprockets.api.book.BookCategory;
import flaxbeard.sprockets.api.book.BookEntry;

/**
 * @author Flaxbeard
 * 
 * If you want to use this and it's not documented at this point please create an issue
 *
 */
public class SprocketsAPI
{
	private static final List<BookCategory> categories = new ArrayList<BookCategory>();
	private static final List<BookEntry> entries = new ArrayList<BookEntry>();
	
	private static final Map<ItemStack, ItemStack> 		millstoneRecipes 		= new HashMap<ItemStack, ItemStack>();
	private static final Map<IBlockState, ItemStack> 	stampMillRecipes 		= new HashMap<IBlockState, ItemStack>();
	private static final Map<IBlockState, IBlockState> 	stampMillRecipesBlock 	= new HashMap<IBlockState, IBlockState>();

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
	
	public static void addStampMillRecipe(IBlockState in, ItemStack out)
	{
		stampMillRecipes.put(in, out);
	}
	
	public static void addStampMillRecipe(IBlockState in, IBlockState out)
	{
		stampMillRecipesBlock.put(in, out);
	}
	
	private static boolean compareItemStacks(ItemStack stack1, ItemStack stack2)
	{
		return stack2.getItem() == stack1.getItem() && (stack2.getMetadata() == 32767 || stack2.getMetadata() == stack1.getMetadata());
	}
	
	private static boolean compareBlockStates(IBlockState state1, IBlockState state2)
	{
		return state1.equals(state2);
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
	
	public static ItemStack getStampMillRecipeItem(IBlockState state)
	{
		for (Entry<IBlockState, ItemStack> entry : stampMillRecipes.entrySet())
		{
			if (compareBlockStates(state, (IBlockState)entry.getKey()))
			{
				return (ItemStack) entry.getValue().copy();
			}
		}
		
		return null;
	}
	
	public static IBlockState getStampMillRecipeBlock(IBlockState state)
	{
		for (Entry<IBlockState, IBlockState> entry : stampMillRecipesBlock.entrySet())
		{
			if (compareBlockStates(state, (IBlockState)entry.getKey()))
			{
				return (IBlockState) entry.getValue();
			}
		}
		
		return null;
	}
}

