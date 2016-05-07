package flaxbeard.sprockets.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.registry.GameRegistry;
import flaxbeard.sprockets.Sprockets;
import flaxbeard.sprockets.blocks.tiles.TileEntityCreativeMotor;
import flaxbeard.sprockets.blocks.tiles.TileEntityRedstoneEngine;
import flaxbeard.sprockets.blocks.tiles.TileEntityWindmill;
import flaxbeard.sprockets.blocks.tiles.TileEntityWindmillSmall;

public class SprocketsBlocks
{
	public static BlockCreativeMotor creativeMotor;
	public static BlockWindmillSmall windmillSmall;
	public static BlockWindmill windmill;
	public static BlockRedstoneEngine redEngine;
	
	public static void preInit()
	{
		creativeMotor = new BlockCreativeMotor("creativeMotor", Material.wood, 1.0f, 1.0f);
		windmillSmall = new BlockWindmillSmall("windmillSmall", Material.wood, 1.0f, 1.0f);
		windmill = new BlockWindmill("windmill", Material.wood, 1.0f, 1.0f);
		redEngine = new BlockRedstoneEngine("redstoneEngine", Material.rock, 1.5F, 10.0F);
		registerTileEntity(TileEntityCreativeMotor.class);
		registerTileEntity(TileEntityWindmillSmall.class);
		registerTileEntity(TileEntityWindmill.class);
		registerTileEntity(TileEntityRedstoneEngine.class);
	}

	
	private static void registerTileEntity(Class<? extends TileEntity> te)
	{
		String s = te.getSimpleName().substring("TileEntity".length());
		GameRegistry.registerTileEntity(te, Sprockets.MODID + ":" + s);
	}
}
