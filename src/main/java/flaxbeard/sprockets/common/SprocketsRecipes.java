package flaxbeard.sprockets.common;

import mcmultipart.multipart.PartSlot;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import flaxbeard.sprockets.api.SprocketsAPI;
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
				Character.valueOf('I'), "stone", Character.valueOf('S'), "stickWood"
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
				Character.valueOf('I'), "stone"
				));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(SprocketsMultiparts.axle, 4, 2),
				"I",
				"I",
				"I",
				Character.valueOf('I'), "ingotIron"
				));
		
		// Clutches
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(SprocketsMultiparts.clutch, 1, 0),
				"A",
				"L",
				"A",
				Character.valueOf('A'), new ItemStack(SprocketsMultiparts.axle, 1, 0), Character.valueOf('L'), new ItemStack(Blocks.LEVER)
				));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(SprocketsMultiparts.clutch, 1, 1),
				"A",
				"L",
				"A",
				Character.valueOf('A'), new ItemStack(SprocketsMultiparts.axle, 1, 1), Character.valueOf('L'), new ItemStack(Blocks.LEVER)
				));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(SprocketsMultiparts.clutch, 1, 2),
				"A",
				"L",
				"A",
				Character.valueOf('A'), new ItemStack(SprocketsMultiparts.axle, 1, 2), Character.valueOf('L'), new ItemStack(Blocks.LEVER)
				));
				
		
		// Windmill Blade
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(SprocketsItems.resource, 1, 0),
				"SW",
				"SW",
				"I ",
				Character.valueOf('W'), new ItemStack(Blocks.WOOL), Character.valueOf('S'), "stickWood", Character.valueOf('I'), "ingotIron"
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
		
		// Torsion Spring
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(SprocketsBlocks.spring),
				"III",
				"IAI",
				"III",
				Character.valueOf('I'), "ingotIron", Character.valueOf('A'), new ItemStack(SprocketsMultiparts.axle, 1, 1)
				));
		
		// Gyrometer
		GameRegistry.addRecipe(new ItemStack(SprocketsItems.gyrometer),
				"G",
				"G",
				"C",
				Character.valueOf('G'), new ItemStack(SprocketsMultiparts.sprocket, 1, 0), Character.valueOf('C'), new ItemStack(Items.COMPASS)
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
		

		SprocketsAPI.addMillstoneRecipe(new ItemStack(Items.BONE), new ItemStack(Items.DYE, 3, 15));
		SprocketsAPI.addMillstoneRecipe(new ItemStack(Blocks.YELLOW_FLOWER), new ItemStack(Items.DYE, 2, 11));
		SprocketsAPI.addMillstoneRecipe(new ItemStack(Blocks.RED_FLOWER, 1, 0), new ItemStack(Items.DYE, 2, 1));
		SprocketsAPI.addMillstoneRecipe(new ItemStack(Blocks.RED_FLOWER, 1, 1), new ItemStack(Items.DYE, 2, 12));
		SprocketsAPI.addMillstoneRecipe(new ItemStack(Blocks.RED_FLOWER, 1, 2), new ItemStack(Items.DYE, 2, 13));
		SprocketsAPI.addMillstoneRecipe(new ItemStack(Blocks.RED_FLOWER, 1, 3), new ItemStack(Items.DYE, 2, 7));
		SprocketsAPI.addMillstoneRecipe(new ItemStack(Blocks.RED_FLOWER, 1, 4), new ItemStack(Items.DYE, 2, 1));
		SprocketsAPI.addMillstoneRecipe(new ItemStack(Blocks.RED_FLOWER, 1, 5), new ItemStack(Items.DYE, 2, 14));
		SprocketsAPI.addMillstoneRecipe(new ItemStack(Blocks.RED_FLOWER, 1, 6), new ItemStack(Items.DYE, 2, 7));
		SprocketsAPI.addMillstoneRecipe(new ItemStack(Blocks.RED_FLOWER, 1, 7), new ItemStack(Items.DYE, 2, 9));
		SprocketsAPI.addMillstoneRecipe(new ItemStack(Blocks.RED_FLOWER, 1, 8), new ItemStack(Items.DYE, 2, 7));

		SprocketsAPI.addMillstoneRecipe(new ItemStack(Blocks.DOUBLE_PLANT, 1, 0), new ItemStack(Items.DYE, 3, 11));
		SprocketsAPI.addMillstoneRecipe(new ItemStack(Blocks.DOUBLE_PLANT, 1, 1), new ItemStack(Items.DYE, 3, 13));
		SprocketsAPI.addMillstoneRecipe(new ItemStack(Blocks.DOUBLE_PLANT, 1, 4), new ItemStack(Items.DYE, 3, 1));
		SprocketsAPI.addMillstoneRecipe(new ItemStack(Blocks.DOUBLE_PLANT, 1, 5), new ItemStack(Items.DYE, 3, 9));
		
		SprocketsAPI.addMillstoneRecipe(new ItemStack(Items.REEDS, 1, 0), new ItemStack(Items.SUGAR, 3, 0));

	}
}
