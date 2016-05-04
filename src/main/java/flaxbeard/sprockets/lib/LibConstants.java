package flaxbeard.sprockets.lib;

public class LibConstants
{
	// This is so I can easily edit things (and eventually for when a config is implemented)
	
	// These affect the mining time for Sprockets, Axles, and Big Sprockets
	public static final float MINE_TIME_WOODEN_SPROCKET 	= 0.2F;
	public static final float MINE_TIME_STONE_SPROCKET 		= 0.2F;
	public static final float MINE_TIME_IRON_SPROCKET 		= 0.3F;
	public static final float MINE_TIME_MULT_BIG_SPROCEKT	= 2.0F;
	
	// Multiplier that affects how fast gears/axles/other things appear to spin
	public static final float RENDER_ROTATION_SPEED_MULTIPLIER 	= 1.0F;
	
	public static final float MIN_TORQUE_WOODEN_SPROCKET	= 0.0F;
	public static final float MIN_TORQUE_STONE_SPROCKET		= 2.0F;
	public static final float MIN_TORQUE_IRON_SPROCKET		= 4.0F;
	
	public static final float MAX_TORQUE_WOODEN_SPROCKET	= 10.0F;
	public static final float MAX_TORQUE_STONE_SPROCKET		= 20.0F;
	public static final float MAX_TORQUE_IRON_SPROCKET		= 40.0F;
	
	public static final int WINDMILL_UPDATE_TICKS = 140;
	
	public static final float CRANK_SPEED 	= 5.0F;
	public static final float CRANK_TORQUE 	= 1.0F;
	public static final int CRANK_TICKS		= 10;
}
