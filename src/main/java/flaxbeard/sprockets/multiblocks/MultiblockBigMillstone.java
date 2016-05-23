package flaxbeard.sprockets.multiblocks;

import java.util.HashSet;
import java.util.Set;

import mcmultipart.multipart.PartSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3i;
import flaxbeard.sprockets.api.Multiblock;

public class MultiblockBigMillstone extends Multiblock
{
	//private static final int TOP;
	
	private static final HashSet<Tuple<Vec3i, PartSlot>> CISMP = new HashSet<Tuple<Vec3i, PartSlot>>();
	private static final HashSet<Vec3i> CIS = new HashSet<Vec3i>();
	
	static
	{
		CIS.add(new Vec3i(0, 1, 0));
		CIS.add(new Vec3i(0, -1, 0));
		CISMP.add(new Tuple(new Vec3i(0, 1, 0), PartSlot.DOWN));
		CISMP.add(new Tuple(new Vec3i(0, -1, 0), PartSlot.UP));
	}

	public MultiblockBigMillstone(String name, int xD, int height, int zD, int centerX, int centerY, int centerZ, Object... key)
	{
		super(name, xD, height, zD, centerX, centerY, centerZ, key);
		
	}
	
	public AxisAlignedBB getSpecialBounds(int index)
	{
		return new AxisAlignedBB(0, 0, 0, 1, 1, 1);
	}
	

	public Set<Tuple<Vec3i, PartSlot>> multipartCisConnections(int pos)
	{
		if (pos == 22)
		{
			return CISMP;
		}
		return new HashSet<Tuple<Vec3i, PartSlot>>();
	}

	public Set<Tuple<Vec3i, PartSlot>> multipartTransConnections(int pos)
	{
		return new HashSet<Tuple<Vec3i, PartSlot>>();
	}

	public Set<Vec3i> cisConnections(int pos)
	{
		if (pos == 22)
		{
			return CIS;
		}
		return new HashSet<Vec3i>();
	}

	public Set<Vec3i> transConnections(int pos)
	{
		return new HashSet<Vec3i>();
	}

}
