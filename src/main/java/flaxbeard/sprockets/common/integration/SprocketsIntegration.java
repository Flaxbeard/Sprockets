package flaxbeard.sprockets.common.integration;

import net.minecraftforge.fml.common.Loader;


public class SprocketsIntegration
{
	public static void postInit()
	{
		if (Loader.isModLoaded("Botania"))
		{
			BotaniaIntegration.postInit();
		}
	}
}
