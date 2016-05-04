package flaxbeard.sprockets.common.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import flaxbeard.sprockets.Sprockets;

public class SprocketsNetwork
{
	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Sprockets.MODID);
	
	private static int id = 0;
	
	public static void init()
	{
	}
}
