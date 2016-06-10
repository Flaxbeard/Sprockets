package flaxbeard.sprockets.common;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import flaxbeard.sprockets.Sprockets;

public class SprocketsSounds
{
	public static SoundEvent WOODEN_GEAR;
	
	public static void preInit()
	{
		ResourceLocation loc = new ResourceLocation(Sprockets.MODID, "woodGear");
		WOODEN_GEAR = GameRegistry.register(new SoundEvent(loc).setRegistryName(loc));
	}
}
