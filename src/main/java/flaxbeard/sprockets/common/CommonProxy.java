package flaxbeard.sprockets.common;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import flaxbeard.sprockets.Sprockets;
import flaxbeard.sprockets.api.network.MechanicalNetworkRegistry;
import flaxbeard.sprockets.blocks.SprocketsBlocks;
import flaxbeard.sprockets.book.BookData;
import flaxbeard.sprockets.common.handler.SprocketsGuiHandler;
import flaxbeard.sprockets.common.handler.SprocketsHandlers;
import flaxbeard.sprockets.common.network.SprocketsNetwork;
import flaxbeard.sprockets.items.SprocketsItems;
import flaxbeard.sprockets.multiparts.SprocketsMultiparts;

public class CommonProxy
{
	public void preInit()
	{
		SprocketsBlocks.preInit();
		SprocketsMultiparts.preInit();
		SprocketsItems.preInit();
	}
	
	public void init()
	{
		NetworkRegistry.INSTANCE.registerGuiHandler(Sprockets.INSTANCE, new SprocketsGuiHandler());
		
		SprocketsNetwork.init();
		MechanicalNetworkRegistry.initialize();
		SprocketsHandlers.init();
		
		SprocketsMultiparts.init();
		SprocketsItems.init();
		
		SprocketsRecipes.init();
	}
	
	public void postInit()
	{
		BookData.postInit();
	}
}
