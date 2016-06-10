package flaxbeard.sprockets.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import flaxbeard.sprockets.api.book.BookCategory;
import flaxbeard.sprockets.api.book.BookEntry;
import flaxbeard.sprockets.common.util.SimpleWeightedRandom;

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
	
	private static final Map<ItemStack, ItemStack> 				millstoneRecipes 		= new HashMap<ItemStack, ItemStack>();
	private static final Map<Object, ItemStack> 			stampMillRecipes 		= new HashMap<Object, ItemStack>();
	private static final Map<Object, SimpleWeightedRandom> stampMillRecipesRand 	= new HashMap<Object, SimpleWeightedRandom>();
	private static final Map<Object, IBlockState> 			stampMillRecipesBlock 	= new HashMap<Object, IBlockState>();

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
	
	public static void addStampMillRecipe(Object in, ItemStack out)
	{
		stampMillRecipes.put(in, out);
	}
	
	public static void addStampMillRecipe(Object in, SimpleWeightedRandom out)
	{
		stampMillRecipesRand.put(in, out);
	}
	
	public static void addStampMillRecipe(Object in, IBlockState out)
	{
		stampMillRecipesBlock.put(in, out);
	}
	
	private static boolean compareItemStacks(ItemStack stack1, ItemStack stack2)
	{
		return stack2.getItem() == stack1.getItem() && (stack2.getMetadata() == 32767 || stack2.getMetadata() == stack1.getMetadata());
	}
	
	private static boolean compareBlockStates(IBlockState state1, Object state2)
	{
		if (state2 instanceof String)
		{
			ItemStack stack = new ItemStack(state1.getBlock());
			if (stack.getItem() == null)
			{
				return false;
			}
			for (int id : OreDictionary.getOreIDs(stack))
			{
				if (id == OreDictionary.getOreID((String) state2))
				{
					return true;
				}
			}
			
			return false;
		}
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
	
	public static ItemStack getStampMillRecipeRand(IBlockState state)
	{
		for (Entry<Object, SimpleWeightedRandom> entry : stampMillRecipesRand.entrySet())
		{
			if (compareBlockStates(state, entry.getKey()))
			{
				return (ItemStack) entry.getValue().getDrop();
			}
		}
		
		return null;
	}
	
	public static ItemStack getStampMillRecipeItem(IBlockState state)
	{
		for (Entry<Object, ItemStack> entry : stampMillRecipes.entrySet())
		{
			if (compareBlockStates(state, entry.getKey()))
			{
				return (ItemStack) entry.getValue().copy();
			}
		}
		
		return null;
	}
	
	public static IBlockState getStampMillRecipeBlock(IBlockState state)
	{
		for (Entry<Object, IBlockState> entry : stampMillRecipesBlock.entrySet())
		{
			if (compareBlockStates(state, entry.getKey()))
			{
				return (IBlockState) entry.getValue();
			}
		}
		
		return null;
	}
}

