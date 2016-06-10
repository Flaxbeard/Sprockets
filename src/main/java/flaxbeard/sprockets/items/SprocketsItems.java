package flaxbeard.sprockets.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import flaxbeard.sprockets.api.SprocketsAPI;
import flaxbeard.sprockets.common.util.SimpleWeightedRandom;


public class SprocketsItems
{
	public static ItemWrench wrench;
	public static ItemCrank crank;
	public static ItemGyrometer gyrometer;
	public static ItemSprocketsResource resource;
	public static ItemSprocketsHeap heap;

	public static List<Integer> availableHeaps = new ArrayList<Integer>();
	public static Map<Integer, String> oreNames = new HashMap<Integer, String>();
	public static String[] ores = {
		"iron",
		"gold",
		"redstone",
		"diamond",
		"emerald",
		"lapis",
		"aluminium",
		"ardite",
		"cinnabar",
		"cobalt",
		"copper",
		"lead",
		"nickel",
		"osmium",
		"platinum",
		"plutonium",
		"silver",
		"tin",
		"uranium",
		"zinc"
	};
	//public static ItemSprocketBook book;
	
	public static void preInit()
	{
		wrench = new ItemWrench();
		crank = new ItemCrank();
		gyrometer = new ItemGyrometer();
		resource = new ItemSprocketsResource();
		
		
		String[] names = new String[ores.length];
		for (int i = 0; i < ores.length; i++)
		{
			String oreName = ores[i];
			names[i] = oreName;
		}
		
		heap = new ItemSprocketsHeap(names);

	//	book = new ItemSprocketBook();
	}

	public static void init()
	{

	}
	
	public static void postInit()
	{
		for (int i = 0; i < ores.length; i++)
		{
			String oreDictName = "ore" + ores[i].substring(0, 1).toUpperCase() + ores[i].substring(1);
			if (OreDictionary.doesOreNameExist(oreDictName))
			{
				
				ItemStack recipe = null;
				float xp = 0;
				List<ItemStack> stacks = OreDictionary.getOres(oreDictName);
				int j = 0;
				while (recipe == null && j < stacks.size())
				{
					ItemStack s = stacks.get(j);
					recipe = FurnaceRecipes.instance().getSmeltingResult(s);
					xp = FurnaceRecipes.instance().getSmeltingExperience(s);
					oreNames.put(i, s.getUnlocalizedName());
					j++;
				}
				
				if (recipe != null)
				{
					GameRegistry.addSmelting(new ItemStack(heap, 1, i), recipe, xp);
					
					availableHeaps.add(i);
					
					SprocketsAPI.addStampMillRecipe(oreDictName, 
						new SimpleWeightedRandom(
								new ItemStack(heap, 1, i), 20,
								new ItemStack(heap, 2, i), 5
								));
				}
			}
		}
	}
}
