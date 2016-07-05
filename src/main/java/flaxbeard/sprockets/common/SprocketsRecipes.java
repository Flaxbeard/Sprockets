package flaxbeard.sprockets.common;

import mcmultipart.block.TileMultipartContainer;
import mcmultipart.multipart.IMultipart;
import mcmultipart.multipart.IMultipartContainer;
import mcmultipart.multipart.MultipartContainer;
import mcmultipart.multipart.MultipartHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import flaxbeard.sprockets.api.IMultiblockComparator;
import flaxbeard.sprockets.api.Multiblock;
import flaxbeard.sprockets.api.SprocketsAPI;
import flaxbeard.sprockets.blocks.SprocketsBlocks;
import flaxbeard.sprockets.items.SprocketsItems;
import flaxbeard.sprockets.multiblocks.MultiblockBigMillstone;
import flaxbeard.sprockets.multiblocks.MultiblockStampMill;
import flaxbeard.sprockets.multiparts.PartAxle;
import flaxbeard.sprockets.multiparts.PartSprocketBase;
import flaxbeard.sprockets.multiparts.SprocketsMultiparts;

public class SprocketsRecipes
{
	
	private static class SprocketMultipartComparison<T extends PartSprocketBase> implements IMultiblockComparator
	{
		private int damage;
		private Class<T> partClass;
		
		private SprocketMultipartComparison(Class<T> partClass, int damage)
		{
			this.partClass = partClass;
			this.damage = damage;
		}
		
		@Override
		public boolean isEqual(IBlockAccess iba, BlockPos pos)
		{
			boolean result = false;
			IMultipartContainer cont = MultipartHelper.getPartContainer(iba, pos);
			
			if (cont != null && cont instanceof TileMultipartContainer)
			{
				cont = ((TileMultipartContainer) cont).getPartContainer();	
			}
			if (cont != null && cont instanceof MultipartContainer)
			{
				for (IMultipart part : cont.getParts())
				{
					if (partClass == part.getClass())
					{
						if (((PartSprocketBase) part).getDamage() == damage)
						{
							result = true;
						}
						else
						{
							return false;
						}
					}
					else
					{
						return false;
					}
				}
			}
			else
			{
				//System.out.println(cont);
			}
			
			return result;
		}
		
	}
	
	public static Multiblock BIGMILLSTONE;
	public static Multiblock STAMPMILL;

	public static void init()
	{
		
		BIGMILLSTONE = new MultiblockBigMillstone(
				"bigMillstone",
				3, 3, 3,
				1, 1, 1,
				
				"QQQ",
				"QQQ",
				"QQQ",
				
				"AAA",
				"QMQ",
				"AAA",
				
				"AAA",
				"APA",
				"AAA",
				
				Character.valueOf('M'), SprocketsBlocks.millstone,
				Character.valueOf('Q'), Blocks.QUARTZ_BLOCK,
				Character.valueOf('P'), new SprocketMultipartComparison(PartAxle.class, 0),
				Character.valueOf('A'), Blocks.AIR);
		
		STAMPMILL = new MultiblockStampMill(
				"stampMill",
				3, 3, 1,
				1, 1, 0,
				
				"F F",
				
				"FIF",
				
				"FPF",
				
				Character.valueOf('F'), Blocks.OAK_FENCE,
				Character.valueOf('I'), Blocks.IRON_BLOCK,
				Character.valueOf('P'), Blocks.PISTON);
		
		
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
		
		// Belts
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(SprocketsMultiparts.belt, 32, 0),
				"LLL",
				"L L",
				"LLL",
				Character.valueOf('L'), "leather"
				));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(SprocketsMultiparts.conveyorBelt, 2, 0),
				" B ",
				"B B",
				" B ",
				Character.valueOf('B'), new ItemStack(SprocketsMultiparts.belt, 1, 0)
				));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(SprocketsMultiparts.belt, 2, 0),
				new ItemStack(SprocketsMultiparts.conveyorBelt, 1, 0)
				));
		
		// Clutch
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(SprocketsMultiparts.clutch, 2, 0),
				"A",
				"L",
				"A",
				Character.valueOf('A'), new ItemStack(Blocks.STONE_SLAB, 1, 0), Character.valueOf('L'), new ItemStack(Blocks.LEVER)
				));
		
		// Scotch Yoke
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(SprocketsMultiparts.scotchYoke, 2, 0),
				"III",
				"I I",
				" I ",
				Character.valueOf('I'), "ingotIron"
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
		
		// Water Wheel Part
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(SprocketsItems.resource, 1, 1),
				"PPP",
				" P ",
				" A ",
				Character.valueOf('P'), "plankWood", Character.valueOf('A'), new ItemStack(SprocketsMultiparts.axle, 1, 0)
				));

		// Water Wheel
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(SprocketsBlocks.waterwheel),
				"CCC",
				"CAC",
				"CCC",
				Character.valueOf('C'), new ItemStack(SprocketsItems.resource, 1, 1), Character.valueOf('A'), new ItemStack(SprocketsMultiparts.axle, 1, 0)
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
		
		// Bellows
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(SprocketsBlocks.bellows),
				"PPP",
				"L L",
				"PPP",
				Character.valueOf('P'), "plankWood", Character.valueOf('L'), "leather"
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
		
		// Friction Heater
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(SprocketsBlocks.frictionHeater),
				"A",
				"L",
				"N",
				Character.valueOf('A'), new ItemStack(SprocketsMultiparts.axle, 1, 0), Character.valueOf('L'), "leather", Character.valueOf('N'), "netherrack"
				));
	

		SprocketsAPI.addMillstoneRecipe(new ItemStack(Items.BONE), new ItemStack(Items.DYE, 4, 15));
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
