package flaxbeard.sprockets.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.registry.GameRegistry;
import flaxbeard.sprockets.Sprockets;
import flaxbeard.sprockets.blocks.tiles.TileEntityCreativeMotor;
import flaxbeard.sprockets.blocks.tiles.TileEntityWindmillSmall;

public class SprocketsBlocks
{
	public static BlockCreativeMotor creativeMotor;
	public static BlockWindmillSmall windmill;
	
	public static void preInit()
	{
		creativeMotor = new BlockCreativeMotor("creativeMotor", Material.wood, 1.0f, 1.0f);
		windmill = new BlockWindmillSmall("windmill", Material.wood, 1.0f, 1.0f);
		registerTileEntity(TileEntityCreativeMotor.class);
		registerTileEntity(TileEntityWindmillSmall.class);
	}

	
	private static void registerTileEntity(Class<? extends TileEntity> te)
	{
		String s = te.getSimpleName().substring("TileEntity".length());
		GameRegistry.registerTileEntity(te, Sprockets.MODID + ":" + s);
	}
}
