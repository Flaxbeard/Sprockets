package flaxbeard.sprockets.lib;

import java.io.File;

import flaxbeard.sprockets.Sprockets;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class LibConstants
{
	// This is so I can easily edit things
	
	// These affect the mining time for Sprockets, Axles, and Big Sprockets
	public static float MINE_TIME_WOODEN_SPROCKET 	= 0.2F;
	public static float MINE_TIME_STONE_SPROCKET 	= 0.2F;
	public static float MINE_TIME_IRON_SPROCKET 	= 0.3F;
	public static float MINE_TIME_MULT_BIG_SPROCEKT	= 2.0F;
	
	// Multiplier that affects how fast gears/axles/other things appear to spin
	public static float RENDER_ROTATION_SPEED_MULTIPLIER = 1.0F;
	
	// Minimum torque needed to spin various gear types
	public static float MIN_TORQUE_WOODEN_SPROCKET	= 0.0F;
	public static float MIN_TORQUE_STONE_SPROCKET	= 3.0F;
	public static float MIN_TORQUE_IRON_SPROCKET	= 6.0F;
	
	// Maximum torque various gear types can handle
	public static float MAX_TORQUE_WOODEN_SPROCKET	= 20.0F;
	public static float MAX_TORQUE_STONE_SPROCKET	= 40.0F;
	public static float MAX_TORQUE_IRON_SPROCKET	= 60.0F;
	
	// Maximum speed various gear types can handle
	public static float MAX_SPEED_WOODEN_SPROCKET	= 40.0F;
	public static float MAX_SPEED_STONE_SPROCKET	= 20.0F;
	public static float MAX_SPEED_IRON_SPROCKET		= 10.0F;
	
	// Ticks between each update for both windmills
	public static int WINDMILL_UPDATE_TICKS = 140;
	
	// Power generation
	public static float SMALL_WINDMILL_TORQUE 	= 1F;
	public static float SMALL_WINDMILL_SPEED 	= 16F;
	public static float WINDMILL_TORQUE 		= 4F;
	public static float WINDMILL_SPEED			= 8F;
	public static float WATERWHEEL_TORQUE 		= 10F;
	public static float WATERWHEEL_SPEED		= 5F;
	public static float REDSTONE_ENGINE_TORQUE	= 0.5F;
	public static float REDSTONE_ENGINE_SPEED 	= 4F;
	public static float CREATIVE_ENGINE_TORQUE	= 100F;
	public static float CREATIVE_ENGINE_SPEED 	= 16F;
	
	// Power consumption
	public static float MILLSTONE_TORQUE		= 30F;
	public static float BIG_MILLSTONE_TORQUE	= 90F;
	public static float MILLSTONE_MIN_SPEED		= 0.75F;
	public static float STAMP_MILL_TORQUE 		= 15F;
	
	// Water wheel radius
	public static final int WATER_WHEEL_RAIDUS	= 1;
	
	// Speed/torque/ticks of a hand crank
	public static float CRANK_SPEED 	= 5.0F;
	public static float CRANK_TORQUE 	= 1.0F;
	public static final int CRANK_TICKS	= 10;
	
	// Maximum speed for Redstone gear displays
	public static float MAX_SPEED_REDGEAR = 60.F;
	
	// How many "charge units" before torque needed increases
	public static float SPRING_CONSTANT = .002F;
	
	// Maximum torque a spring can hold
	public static float MAX_SPRING_STORAGE = 20.F / SPRING_CONSTANT;
	
	// How many water source blocks fully powers a single water wheel
	public static int WATERWHEEL_NUM_WATER = 100;
	
	// Processing times
	public static float STAMP_MILL_ROTATION_NEEDED = 2500F;
	public static float MILLSTONE_ROTATION_NEEDED = 200F;
	public static float MILLSTONE_BIG_ROTATION_NEEDED = 50F;

	public static void loadConfig(FMLPreInitializationEvent event)
	{
		Configuration config = new Configuration(new File(event.getModConfigurationDirectory(), Sprockets.MODID + ".cfg"));
		config.load();
		MIN_TORQUE_WOODEN_SPROCKET = config.getFloat("Wooden sprocket minimum torque", "Transfer", MIN_TORQUE_WOODEN_SPROCKET, 0F, 999F, "");
		MIN_TORQUE_STONE_SPROCKET = config.getFloat("Stone sprocket minimum torque", "Transfer", MIN_TORQUE_STONE_SPROCKET, 0F, 999F, "");
		MIN_TORQUE_IRON_SPROCKET = config.getFloat("Iron sprocket minimum torque", "Transfer", MIN_TORQUE_IRON_SPROCKET, 0F, 999F, "");
		
		MAX_TORQUE_WOODEN_SPROCKET = config.getFloat("Wooden sprocket maximum torque", "Transfer", MAX_TORQUE_WOODEN_SPROCKET, 0F, 999F, "");
		MAX_TORQUE_STONE_SPROCKET = config.getFloat("Stone sprocket maximum torque", "Transfer", MAX_TORQUE_STONE_SPROCKET, 0F, 999F, "");
		MAX_TORQUE_IRON_SPROCKET = config.getFloat("Iron sprocket maximum torque", "Transfer", MAX_TORQUE_IRON_SPROCKET, 0F, 999F, "");
		
		WINDMILL_UPDATE_TICKS = config.getInt("Ticks between each update of windmill speed", "Performance", WINDMILL_UPDATE_TICKS, -1, 9999, "");

		SMALL_WINDMILL_TORQUE = config.getFloat("Small windmill torque output", "Production", SMALL_WINDMILL_TORQUE, 0F, 999F, "");
		WINDMILL_TORQUE = config.getFloat("Large windmill torque output", "Production", WINDMILL_TORQUE, 0F, 999F, "");
		REDSTONE_ENGINE_TORQUE = config.getFloat("Redstone engine torque output", "Production", REDSTONE_ENGINE_TORQUE, 0F, 999F, "");
		CREATIVE_ENGINE_TORQUE = config.getFloat("Creative engine torque output", "Production", CREATIVE_ENGINE_TORQUE, 0F, 999F, "");
		WATERWHEEL_TORQUE = config.getFloat("Water wheel torque output", "Production", WATERWHEEL_TORQUE, 0F, 999F, "");

		SMALL_WINDMILL_SPEED = config.getFloat("Small windmill speed output", "Production", SMALL_WINDMILL_SPEED, 0F, 999F, "");
		WINDMILL_SPEED = config.getFloat("Large windmill speed output", "Production", WINDMILL_SPEED, 0F, 999F, "");
		REDSTONE_ENGINE_SPEED = config.getFloat("Redstone engine speed output", "Production", REDSTONE_ENGINE_SPEED, 0F, 999F, "");
		CREATIVE_ENGINE_SPEED = config.getFloat("Creative engine speed output", "Production", CREATIVE_ENGINE_SPEED, 0F, 999F, "");
		WATERWHEEL_SPEED = config.getFloat("Water wheel speed output", "Production", WATERWHEEL_SPEED, 0F, 999F, "");
		
		WATERWHEEL_NUM_WATER = config.getInt("Water wheel blocks", "Production", WATERWHEEL_NUM_WATER, 0, 999, "How many source blocks needed to fully power a water wheel");

		SPRING_CONSTANT = config.getFloat("Spring constant", "Storage", SPRING_CONSTANT, 0F, 1F, "The closer this number is to 0, the further the spring can be wound");
		MAX_SPRING_STORAGE = config.getFloat("Spring maximum torque", "Storage", 20F, 0F, 999F, "How high a network torque needs to be to fully finish turning a spring") / SPRING_CONSTANT;
		
		STAMP_MILL_ROTATION_NEEDED = config.getFloat("Stamp mill rotation needed", "Consumption", STAMP_MILL_ROTATION_NEEDED, 0F, 9999F, "How much rotation must accumulate to finish one operation on the stamp mill");
		MILLSTONE_ROTATION_NEEDED = config.getFloat("Millstone rotation needed", "Consumption", MILLSTONE_ROTATION_NEEDED, 0F, 9999F, "How much rotation must accumulate to finish one operation on the millstone");
		MILLSTONE_BIG_ROTATION_NEEDED = config.getFloat("Big millstone rotation needed", "Consumption", MILLSTONE_BIG_ROTATION_NEEDED, 0F, 9999F, "How much rotation must accumulate to finish one operation on the big millstone");
		
		MILLSTONE_TORQUE = config.getFloat("Millstone torque requirement", "Consumption", MILLSTONE_TORQUE, 0F, 999F, "");
		BIG_MILLSTONE_TORQUE = config.getFloat("Big millstone torque requirement", "Consumption", BIG_MILLSTONE_TORQUE, 0F, 999F, "");
		STAMP_MILL_TORQUE = config.getFloat("Stamp mill torque requirement", "Consumption", STAMP_MILL_TORQUE, 0F, 999F, "");
		MILLSTONE_MIN_SPEED = config.getFloat("Millstone minimum speed", "Consumption", MILLSTONE_MIN_SPEED, 0F, 999F, "");


		config.save();
	}
}
