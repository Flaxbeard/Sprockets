package flaxbeard.sprockets.client;

import java.util.HashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

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
}
