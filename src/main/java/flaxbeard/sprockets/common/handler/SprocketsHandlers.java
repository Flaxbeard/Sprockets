package flaxbeard.sprockets.common.handler;

import net.minecraftforge.common.MinecraftForge;
import flaxbeard.sprockets.blocks.SprocketsBlocks;
import flaxbeard.sprockets.blocks.tiles.TileEntityStampMill;

public class SprocketsHandlers
{
	public static void init()
	{
		MinecraftForge.EVENT_BUS.register(new WrenchHandler());
		MinecraftForge.EVENT_BUS.register(new SprocketPlacementHandler());
		MinecraftForge.EVENT_BUS.register(SprocketsBlocks.stampMill);
	}
}
