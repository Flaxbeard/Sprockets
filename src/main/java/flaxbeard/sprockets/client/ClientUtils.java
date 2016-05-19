package flaxbeard.sprockets.client;

import java.util.HashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import flaxbeard.sprockets.api.IMechanicalConduit;
import flaxbeard.sprockets.api.network.MechanicalNetwork;
import flaxbeard.sprockets.lib.LibConstants;

public class ClientUtils
{
	private static HashMap<String, ResourceLocation> textures = new HashMap<String, ResourceLocation>();
	

	public static void bindTexture(String string)
	{
		if (!textures.containsKey(string))
		{
			textures.put(string, new ResourceLocation(string));
			System.out.println("[Sprockets] Registering new ResourceLocation: " + string);
		}
		Minecraft.getMinecraft().getTextureManager().bindTexture(textures.get(string));
	}
	
	public static float getRotation(IMechanicalConduit conduit, float partialTicks)
	{
		MechanicalNetwork network = conduit.getNetwork();
		return getRotation(network.getSpeed(), partialTicks, conduit.getMultiplier(), network.rotation);
	//	return (network.rotation + (partialTicks * network.getSpeed())) * conduit.getMultiplier() * LibConstants.RENDER_ROTATION_SPEED_MULTIPLIER;
	}
	
	public static float getRotation(float speed, float partialTicks, float multiplier, float rotation)
	{
		return (rotation + (partialTicks * speed)) * multiplier * LibConstants.RENDER_ROTATION_SPEED_MULTIPLIER;
	}
}
