package flaxbeard.sprockets.common.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import scala.util.Random;

public class SimpleWeightedRandom
{
	private int total = 0;
	private final Map<Integer, ItemStack> results = new HashMap<Integer, ItemStack>();
	private static final Random rand = new Random();
	
	public SimpleWeightedRandom(Object... items)
	{
		for (int index = 0; index < items.length; index += 2)
		{
			Object item = items[index];
			if (item instanceof Item)
			{
				item = new ItemStack((Item) item);
			}
			else if (item instanceof Block)
			{
				item = new ItemStack((Block) item);
			}
			else if (!(item instanceof ItemStack) && item != null)
			{
				throw new IllegalArgumentException("Invalid parameter for Weighted Random: " + item);
			}
			
			int value = (Integer) items[index + 1];
			
			results.put(total, (ItemStack) item);
			total += value;
		}
	}
	
	public ItemStack getDrop()
	{
		int random = rand.nextInt(total);
		
		ItemStack curr = null;
		ItemStack last = null;
		
		for (Entry<Integer, ItemStack> entry : results.entrySet())
		{
			curr = entry.getValue().copy();
			
			if (entry.getKey() >= random)
			{
				System.out.println(last);
				return last;
			}
			last = curr;
		}
		
		System.out.println(curr);
		return curr;
	}
}
