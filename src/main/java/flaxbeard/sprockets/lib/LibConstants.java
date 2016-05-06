package flaxbeard.sprockets.lib;

import java.io.File;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class LibConstants
{
	// This is so I can easily edit things
	
	// These affect the mining time for Sprockets, Axles, and Big Sprockets
	public static final float MINE_TIME_WOODEN_SPROCKET 	= 0.2F;
	public static final float MINE_TIME_STONE_SPROCKET 		= 0.2F;
	public static final float MINE_TIME_IRON_SPROCKET 		= 0.3F;
	public static final float MINE_TIME_MULT_BIG_SPROCEKT	= 2.0F;
	
	// Multiplier that affects how fast gears/axles/other things appear to spin
	public static final float RENDER_ROTATION_SPEED_MULTIPLIER 	= 1.0F;
	
	// Minimum torque needed to spin various gear types
	public static final float MIN_TORQUE_WOODEN_SPROCKET	= 0.0F;
	public static final float MIN_TORQUE_STONE_SPROCKET		= 2.0F;
	public static final float MIN_TORQUE_IRON_SPROCKET		= 4.0F;
	
	// Maximum torque various gear types can handle
	public static final float MAX_TORQUE_WOODEN_SPROCKET	= 10.0F;
	public static final float MAX_TORQUE_STONE_SPROCKET		= 20.0F;
	public static final float MAX_TORQUE_IRON_SPROCKET		= 40.0F;
	
	// Ticks between each update for both windmills
	public static final int WINDMILL_UPDATE_TICKS = 140;
	
	// Power generation
	public static final float SMALL_WINDMILL_TORQUE 	= 1F;
	public static final float SMALL_WINDMILL_SPEED	 	= 16F;
	public static final float WINDMILL_TORQUE 			= 4F;
	public static final float WINDMILL_SPEED			= 8F;
	
	// Speed/torque/ticks of a hand crank
	public static final float CRANK_SPEED 	= 5.0F;
	public static final float CRANK_TORQUE 	= 1.0F;
	public static final int CRANK_TICKS		= 10;

	public static void loadConfig(FMLPreInitializationEvent event)
	{
		Configuration config = new Configuration(new File(event.getModConfigurationDirectory(), "FlaxbeardsSteamPower.cfg"));
		config.load();
	}
}
