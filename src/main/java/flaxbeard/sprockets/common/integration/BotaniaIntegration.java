package flaxbeard.sprockets.common.integration;

import flaxbeard.sprockets.api.SprocketsAPI;
import net.minecraft.item.ItemStack;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibOreDict;

public class BotaniaIntegration
{

	public static void postInit()
	{
		for(int i = 0; i < 16; i++)
		{
			//SprocketsAPI.addMillstoneRecipe(new ItemStack(ModItems.petal, 1, i), new ItemStack(ModItems.dye, 1, i));
		}
	}

}
