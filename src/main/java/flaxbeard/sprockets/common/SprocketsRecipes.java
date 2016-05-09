package flaxbeard.sprockets.common;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import flaxbeard.sprockets.blocks.SprocketsBlocks;
import flaxbeard.sprockets.items.SprocketsItems;
import flaxbeard.sprockets.multiparts.SprocketsMultiparts;

public class SprocketsRecipes
{
	public static void init()
	{
		// Sprockets
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(SprocketsMultiparts.sprocket, 4, 0),
				" I ",
				"ISI",
				" I ",
				Character.valueOf('I'), "plankWood", Character.valueOf('S'), "stickWood"
				));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(SprocketsMultiparts.sprocket, 4, 1),
				" I ",
				"ISI",
				" I ",
				Character.valueOf('I'), new ItemStack(Blocks.hardened_clay), Character.valueOf('S'), "stickWood"
				));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(SprocketsMultiparts.sprocket, 4, 2),
				" I ",
				"ISI",
				" I ",
				Character.valueOf('I'), "ingotIron", Character.valueOf('S'), "stickWood"
				));
		
		// Redstone Sprockets
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(SprocketsMultiparts.redstoneSprocket, 1, 0),
				new ItemStack(SprocketsMultiparts.sprocket, 1, 0), "dustRedstone"
				));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(SprocketsMultiparts.redstoneSprocket, 1, 1),
				new ItemStack(SprocketsMultiparts.sprocket, 1, 1), "dustRedstone"
				));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(SprocketsMultiparts.redstoneSprocket, 1, 2),
				new ItemStack(SprocketsMultiparts.sprocket, 1, 2), "dustRedstone"
				));
		
		// Lapis Sprockets
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(SprocketsMultiparts.lapisSprocket, 1, 0),
				new ItemStack(SprocketsMultiparts.sprocket, 1, 0), "gemLapis"
				));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(SprocketsMultiparts.lapisSprocket, 1, 1),
				new ItemStack(SprocketsMultiparts.sprocket, 1, 1), "gemLapis"
				));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(SprocketsMultiparts.lapisSprocket, 1, 2),
				new ItemStack(SprocketsMultiparts.sprocket, 1, 2), "gemLapis"
				));
		
		// Big sprockets
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(SprocketsMultiparts.bigSprocket, 1, 0),
				"SS",
				"SS",
				Character.valueOf('S'), new ItemStack(SprocketsMultiparts.sprocket, 1, 0)
				));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(SprocketsMultiparts.bigSprocket, 1, 1),
				"SS",
				"SS",
				Character.valueOf('S'), new ItemStack(SprocketsMultiparts.sprocket, 1, 1)
				));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(SprocketsMultiparts.bigSprocket, 1, 2),
				"SS",
				"SS",
				Character.valueOf('S'), new ItemStack(SprocketsMultiparts.sprocket, 1, 2)
				));
		
		// Axles
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(SprocketsMultiparts.axle, 4, 0),
				"I",
				"I",
				"I",
				Character.valueOf('I'), "plankWood"
				));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(SprocketsMultiparts.axle, 4, 1),
				"I",
				"I",
				"I",
				Character.valueOf('I'), new ItemStack(Blocks.hardened_clay)
				));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(SprocketsMultiparts.axle, 4, 2),
				"I",
				"I",
				"I",
				Character.valueOf('I'), "ingotIron"
				));
		
		// Windmill Blade
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(SprocketsItems.resource, 1, 0),
				"SW",
				"SW",
				"I ",
				Character.valueOf('W'), new ItemStack(Blocks.wool), Character.valueOf('S'), "stickWood", Character.valueOf('I'), "ingotIron"
				));
		
		// Small Windmill
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(SprocketsBlocks.windmillSmall),
				" I ",
				"IAI",
				"SIS",
				Character.valueOf('I'), "ingotIron", Character.valueOf('S'), "stone", Character.valueOf('A'), new ItemStack(SprocketsMultiparts.axle, 1, 0)
				));
		
		// Windmill
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(SprocketsBlocks.windmill),
				" B ",
				"BAB",
				" B ",
				Character.valueOf('B'), new ItemStack(SprocketsItems.resource, 1, 0), Character.valueOf('A'), new ItemStack(SprocketsMultiparts.axle, 1, 0)
				));
		
		// Redstone Engine
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(SprocketsBlocks.redEngine),
				"SAS",
				"SRS",
				"SSS",
				Character.valueOf('S'), "stone", Character.valueOf('R'), "blockRedstone", Character.valueOf('A'), new ItemStack(SprocketsMultiparts.axle, 1, 1)
				));

		// Millstone
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(SprocketsBlocks.millstone),
				" A ",
				"SAS",
				"SSS",
				Character.valueOf('S'), "stoneDiorite", Character.valueOf('A'), new ItemStack(SprocketsMultiparts.axle, 1, 0)
				));
		
		// Gyrometer
		GameRegistry.addRecipe(new ItemStack(SprocketsItems.gyrometer),
				"G",
				"G",
				"C",
				Character.valueOf('G'), new ItemStack(SprocketsMultiparts.sprocket, 1, 0), Character.valueOf('C'), new ItemStack(Items.compass)
				);
		
		// Crank
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(SprocketsItems.crank),
				" SS",
				"SS ",
				Character.valueOf('S'), "stickWood"
				));
		
		// Wrench
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(SprocketsItems.wrench),
				"III",
				" I ",
				"I  ",
				Character.valueOf('I'), "ingotIron"
				));
	}
}
