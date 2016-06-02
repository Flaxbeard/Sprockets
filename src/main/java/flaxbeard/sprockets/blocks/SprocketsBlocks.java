package flaxbeard.sprockets.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.registry.GameRegistry;
import flaxbeard.sprockets.Sprockets;
import flaxbeard.sprockets.blocks.tiles.TileEntityMillstone;
import flaxbeard.sprockets.blocks.tiles.TileEntityMultiblock;
import flaxbeard.sprockets.blocks.tiles.TileEntityPump;
import flaxbeard.sprockets.blocks.tiles.TileEntityRedstoneEngine;
import flaxbeard.sprockets.blocks.tiles.TileEntityStampMill;
import flaxbeard.sprockets.blocks.tiles.TileEntityTorsionSpring;
import flaxbeard.sprockets.blocks.tiles.TileEntityWaterWheel;
import flaxbeard.sprockets.blocks.tiles.TileEntityWaterWheelComponent;
import flaxbeard.sprockets.blocks.tiles.TileEntityWindmill;
import flaxbeard.sprockets.blocks.tiles.TileEntityWindmillSmall;

public class SprocketsBlocks
{
	public static BlockWindmillSmall windmillSmall;
	public static BlockWindmill windmill;
	public static BlockRedstoneEngine redEngine;
	public static BlockMillstone millstone;
	public static BlockTorsionSpring spring;
	public static BlockMultiblock mbBlock;
	public static BlockWaterWheel waterwheel;
	public static BlockWaterWheelPart waterwheelComponent;
	public static BlockStampMill stampMill;
	public static BlockPump pump;
	
	public static void preInit()
	{
		windmillSmall = new BlockWindmillSmall("windmillSmall", Material.WOOD, 1.0f, 1.0f);
		windmill = new BlockWindmill("windmill", Material.WOOD, 1.0f, 1.0f);
		redEngine = new BlockRedstoneEngine("redstoneEngine", Material.ROCK, 1.5F, 10.0F);
		millstone = new BlockMillstone("millstone", Material.ROCK, 1.5F, 10.0F);
		spring = new BlockTorsionSpring("spring", Material.ROCK, 1.5F, 10.0F);
		mbBlock = new BlockMultiblock();
		waterwheel = new BlockWaterWheel("waterwheel", Material.WOOD, 1.0f, 1.0f);
		waterwheelComponent = new BlockWaterWheelPart("waterwheelComponent", Material.WOOD, 1.0f, 1.0f);
		stampMill = new BlockStampMill("stampMill", Material.ROCK, 1.5F, 10.0F);
		pump = new BlockPump("pump", Material.ROCK);
		
		registerTileEntity(TileEntityWindmillSmall.class);
		registerTileEntity(TileEntityWindmill.class);
		registerTileEntity(TileEntityRedstoneEngine.class);
		registerTileEntity(TileEntityMillstone.class);
		registerTileEntity(TileEntityTorsionSpring.class);
		registerTileEntity(TileEntityMultiblock.class);
		registerTileEntity(TileEntityWaterWheel.class);
		registerTileEntity(TileEntityWaterWheelComponent.class);
		registerTileEntity(TileEntityStampMill.class);
		registerTileEntity(TileEntityPump.class);
	}

	
	private static void registerTileEntity(Class<? extends TileEntity> te)
	{
		String s = te.getSimpleName().substring("TileEntity".length());
		GameRegistry.registerTileEntity(te, Sprockets.MODID + ":" + s);
	}
}
