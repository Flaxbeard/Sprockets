package flaxbeard.sprockets.common;

import flaxbeard.sprockets.api.network.MechanicalNetworkRegistry;
import flaxbeard.sprockets.blocks.SprocketsBlocks;
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
		SprocketsNetwork.init();
		MechanicalNetworkRegistry.initialize();
		SprocketsHandlers.init();
		
		SprocketsMultiparts.init();
	}
	
	public void postInit()
	{
		
	}
}
