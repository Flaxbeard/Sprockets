package flaxbeard.sprockets.items;


public class SprocketsItems
{
	public static ItemWrench wrench;
	public static ItemCrank crank;
	public static ItemGyrometer gyrometer;
	public static ItemSprocketsResource resource;
	
	public static void preInit()
	{
		wrench = new ItemWrench();
		crank = new ItemCrank();
		gyrometer = new ItemGyrometer();
		resource = new ItemSprocketsResource();
	}

	public static void init()
	{

	}
	
}
