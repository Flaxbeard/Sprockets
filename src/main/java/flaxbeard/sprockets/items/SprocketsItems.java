package flaxbeard.sprockets.items;


public class SprocketsItems
{
	public static ItemWrench wrench;
	public static ItemCrank crank;
	public static ItemGyrometer gyrometer;
	public static ItemSprocketsResource resource;
	//public static ItemSprocketBook book;
	
	public static void preInit()
	{
		wrench = new ItemWrench();
		crank = new ItemCrank();
		gyrometer = new ItemGyrometer();
		resource = new ItemSprocketsResource();
	//	book = new ItemSprocketBook();
	}

	public static void init()
	{

	}
	
}
