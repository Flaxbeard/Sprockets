package flaxbeard.sprockets.common.handler;

import net.minecraftforge.common.MinecraftForge;

public class SprocketsHandlers
{
	public static void init()
	{
		MinecraftForge.EVENT_BUS.register(new WrenchHandler());
		MinecraftForge.EVENT_BUS.register(new SprocketPlacementHandler());
	}
}
