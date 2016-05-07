package flaxbeard.sprockets.multiparts;

import java.util.HashSet;

import mcmultipart.multipart.PartSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.oredict.OreDictionary;
import flaxbeard.sprockets.multiparts.items.ItemAxle;
import flaxbeard.sprockets.multiparts.items.ItemBigSprocket;
import flaxbeard.sprockets.multiparts.items.ItemLapisSprocket;
import flaxbeard.sprockets.multiparts.items.ItemRedstoneSprocket;
import flaxbeard.sprockets.multiparts.items.ItemSprocket;
import flaxbeard.sprockets.multiparts.items.ItemSprocketMultipart;

public class SprocketsMultiparts
{
	public static ItemSprocketMultipart sprocket;
	public static ItemSprocketMultipart redstoneSprocket;
	public static ItemSprocketMultipart axle;
	public static ItemSprocketMultipart bigSprocket;
	public static ItemSprocketMultipart lapisSprocket;
	
	public static void preInit()
	{
		sprocket = new ItemSprocket();
		redstoneSprocket = new ItemRedstoneSprocket();
		lapisSprocket = new ItemLapisSprocket();
		
		axle = new ItemAxle();
		
		bigSprocket = new ItemBigSprocket();
	}
	
	
	public static void init()
	{
		OreDictionary.registerOre("gearWood", new ItemStack(bigSprocket, 1, 0));
		OreDictionary.registerOre("gearStone", new ItemStack(bigSprocket, 1, 1));
		OreDictionary.registerOre("gearIron", new ItemStack(bigSprocket, 1, 2));
	}
	
	
	/**
	 * Helper function to create an AxisAlginedBB based on rotation. The input is assumed to be the 0 (up) state.
	 */
	public static AxisAlignedBB rotateAxis(int axis, float x0, float y0, float z0, float x1, float y1, float z1)
	{
		switch(axis)
		{
			case(0):
			default:
				return new AxisAlignedBB(x0, y0, z0, x1, y1, z1);
			case(1):
				return new AxisAlignedBB(x0, 1F - y1, z0, x1, 1F - y0, z1);
			case(2):
				return new AxisAlignedBB(x0, z0, y0, x1, z1, y1);
			case(3):
				return new AxisAlignedBB(x0, z1, 1F - y0, x1, z0, 1F - y1);
			case(4):
				return new AxisAlignedBB(y0, x0, z0, y1, x1, z1);
			case(5):
				return new AxisAlignedBB(1F - y1, x0, z0, 1F - y0, x1, z1);
		}
	}
	
	/**
	 * Helper function to create a list of EnumFacing and PartSlot based on rotation. The input directions 
	 * are assumed to be for the 0 (up) state.
	 */
	public static HashSet<Tuple<Vec3i, PartSlot>> rotatePartFacing(
			int axis, Tuple<Vec3i, PartSlot>... directions)
	{
		HashSet<Tuple<Vec3i, PartSlot>> array = new HashSet<Tuple<Vec3i, PartSlot>>();
		for (Tuple<Vec3i, PartSlot> direction : directions)
		{
			switch(axis)
			{
				case(0):
					array.add(direction);
					break;
				case(1):
					array.add(new Tuple(x180(direction.getFirst()), rotatePartSlot(axis, direction.getSecond())));
					break;
				case(5):
					array.add(new Tuple(x90(direction.getFirst()), rotatePartSlot(axis, direction.getSecond())));
					break;
				case(3):
					array.add(new Tuple(z90(direction.getFirst()), rotatePartSlot(axis, direction.getSecond())));
					break;
				case(2):
					array.add(new Tuple(z90(z90(z90(direction.getFirst()))), rotatePartSlot(axis, direction.getSecond())));
					break;
				case(4):

					array.add(new Tuple(x90(x180(direction.getFirst())), rotatePartSlot(axis, direction.getSecond())));
					break;
				default:
					array.add(direction);
					


			}
		}
		return array;

	}
	
	public static PartSlot rotatePartSlot(int axis, PartSlot direction)
	{
		if (direction.f3 == null)
		{
			
			if (direction.f2 == null)
			{
				return PartSlot.getFaceSlot(rotateFacing(axis, direction.f1));
			}
			
			return PartSlot.getEdgeSlot(rotateFacing(axis, direction.f1), rotateFacing(axis, direction.f2));

		}
		return PartSlot.getCornerSlot(rotateFacing(axis, direction.f1), rotateFacing(axis, direction.f2), rotateFacing(axis, direction.f3));
	}
	
	public static EnumFacing rotateFacing(int axis, EnumFacing direction)
	{
		switch(axis)
		{
			case(0):
				return direction;
			case(1):
				return x180(direction);
			case(5):
				return x90(direction);
			case(3):
				return z90(direction);
			case(2):
				return z90(z90(z90(direction)));
			case(4):
				return x90(x180(direction));
			default:
				return direction;

		}
	}

	/**
	 * Helper function to create a list of EnumFacing based on rotation. The input directions are assumed to be 
	 * for the 0 (up) state.
	 */
	public static HashSet<Vec3i> rotateFacing(int axis, Vec3i... directions)
	{
		HashSet<Vec3i> array = new HashSet<Vec3i>();
		
		for (Vec3i direction : directions)
		{
			switch(axis)
			{
				case(0):
					array.add(direction);
					break;
				case(1):
					array.add(x180(direction));
					break;
				case(5):
					array.add(x90(direction));
					break;
				case(3):
					array.add(z90(direction));
					break;
				case(2):
					array.add(z90(z90(z90(direction))));
					break;
				case(4):
					array.add(x90(x180(direction)));
					break;
				default:
					array.add(direction);
	
			}
		}
		return array;
	}

	
	private static EnumFacing x180(EnumFacing direction)
	{
		if (direction == null)
		{
			return null;
		}
		switch(direction)
		{
			case UP:
				return EnumFacing.DOWN;
			case DOWN:
				return EnumFacing.UP;
			case WEST:
				return EnumFacing.EAST;
			case EAST:
				return EnumFacing.WEST;
			default:
				return direction;
		}
	}
	
	private static EnumFacing z90(EnumFacing direction)
	{
		if (direction == null)
		{
			return null;
		}
		switch(direction)
		{
			case UP:
				return EnumFacing.NORTH;
			case NORTH:
				return EnumFacing.DOWN;
			case DOWN:
				return EnumFacing.SOUTH;
			case SOUTH:
				return EnumFacing.UP;
			default:
				return direction;
		}
	}
	
	private static EnumFacing x90(EnumFacing direction)
	{
		if (direction == null)
		{
			return null;
		}
		switch(direction)
		{
			case UP:
				return EnumFacing.WEST;
			case WEST:
				return EnumFacing.DOWN;
			case DOWN:
				return EnumFacing.EAST;
			case EAST:
				return EnumFacing.UP;
			default:
				return direction;
		}
	}
	

	
	private static Vec3i x180(Vec3i direction)
	{
		return new Vec3i(-direction.getX(), -direction.getY(), direction.getZ());
	}
	
	private static Vec3i x90(Vec3i direction)
	{
		return new Vec3i(-direction.getY(), direction.getX(), direction.getZ());
	}
	
	private static Vec3i z90(Vec3i direction)
	{
		return new Vec3i(direction.getX(), direction.getZ(), -direction.getY());
	}
	
}
