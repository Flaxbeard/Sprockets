package flaxbeard.sprockets.multiblocks;

import java.util.HashSet;
import java.util.Set;

import mcmultipart.multipart.PartSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
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
	
	@Override
	public AxisAlignedBB getSpecialBounds(int index, boolean swapXZ, boolean flipX, boolean flipZ)
	{
		if (index == 22)
		{
			return new AxisAlignedBB(5.5 / 16, 0, 5.5 / 16, 10.5 / 16, 1, 10.5 / 16);

		}
		if (index >= 9)
		{
			return null;
		}
		return new AxisAlignedBB(0, 0, 0, 1, 1, 1);
	}
	
	@Override
	public Set<Tuple<Vec3i, PartSlot>> multipartCisConnections(int pos, BlockPos center, World world, boolean swapXZ, boolean flipX, boolean flipZ)
	{
		if (pos == 22)
		{
			return CISMP;
		}
		return new HashSet<Tuple<Vec3i, PartSlot>>();
	}

	@Override
	public Set<Tuple<Vec3i, PartSlot>> multipartTransConnections(int pos, BlockPos center, World world, boolean swapXZ, boolean flipX, boolean flipZ)
	{
		return new HashSet<Tuple<Vec3i, PartSlot>>();
	}

	@Override
	public Set<Vec3i> cisConnections(int pos, BlockPos center, World world, boolean swapXZ, boolean flipX, boolean flipZ)
	{
		if (pos == 22)
		{
			return CIS;
		}
		return new HashSet<Vec3i>();
	}

	@Override
	public Set<Vec3i> transConnections(int pos, BlockPos center, World world, boolean swapXZ, boolean flipX, boolean flipZ)
	{
		return new HashSet<Vec3i>();
	}

}
