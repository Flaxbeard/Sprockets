package flaxbeard.sprockets.items;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.registry.GameRegistry;
import flaxbeard.sprockets.Sprockets;
import flaxbeard.sprockets.blocks.tiles.TileEntityCreativeMotor;
import flaxbeard.sprockets.blocks.tiles.TileEntityWindmillSmall;

public class SprocketsItems
{
	public static ItemWrench wrench;
	public static ItemCrank crank;
	
	public static void preInit()
	{
		wrench = new ItemWrench();
		crank = new ItemCrank();
	}
	
}
