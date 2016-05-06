package flaxbeard.sprockets.items;

import net.minecraft.block.material.Material;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import flaxbeard.sprockets.Sprockets;
import flaxbeard.sprockets.blocks.tiles.TileEntityCreativeMotor;
import flaxbeard.sprockets.blocks.tiles.TileEntityWindmillSmall;
import flaxbeard.sprockets.multiparts.SprocketsMultiparts;

public class SprocketsItems
{
	public static ItemWrench wrench;
	public static ItemCrank crank;
	public static ItemGyrometer gyrometer;
	public static ItemSprocketsResource resource;
	
	public static void preInit()
	{
		wrench = new ItemWrench();
		crank = new ItemCrank();
		gyrometer = new ItemGyrometer();
		resource = new ItemSprocketsResource();
	}

	public static void init()
	{

	}
	
}
