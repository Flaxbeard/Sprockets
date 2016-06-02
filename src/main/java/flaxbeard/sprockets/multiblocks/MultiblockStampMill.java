package flaxbeard.sprockets.multiblocks;

import java.util.HashSet;
import java.util.Set;

import mcmultipart.multipart.PartSlot;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import flaxbeard.sprockets.api.Multiblock;
import flaxbeard.sprockets.blocks.BlockStampMill;
import flaxbeard.sprockets.blocks.SprocketsBlocks;
import flaxbeard.sprockets.blocks.tiles.TileEntityStampMill;

public class MultiblockStampMill extends Multiblock
{
	private static final HashSet<Tuple<Vec3i, PartSlot>> CISMP = new HashSet<Tuple<Vec3i, PartSlot>>();
	private static final HashSet<Vec3i> CIS = new HashSet<Vec3i>();
	
	static
	{
		CIS.add(new Vec3i(0, 0, 0));
		CIS.add(new Vec3i(0, -1, 0));
		CISMP.add(new Tuple(new Vec3i(0, 0, 0), PartSlot.DOWN));
		CISMP.add(new Tuple(new Vec3i(0, 0, 0), PartSlot.UP));
	}
	
	public MultiblockStampMill(String name, int xD, int height, int zD,
			int centerX, int centerY, int centerZ, Object... key)
	{
		super(name, xD, height, zD, centerX, centerY, centerZ, key);
	}
	
	@Override
	public void setCenter(World world, BlockPos pos)
	{
		world.setBlockState(pos, SprocketsBlocks.stampMill.getDefaultState(), 2);
	}
	
	@Override
	public AxisAlignedBB getSpecialBounds(int index)
	{
		return new AxisAlignedBB(0, 0, 0, 1, 1, 1);
	}
	
	@Override
	public Set<Tuple<Vec3i, PartSlot>> multipartCisConnections(int pos, BlockPos center, World world)
	{
		if ((pos == 5 || pos == 3) && world.getBlockState(center).getBlock() == SprocketsBlocks.stampMill)
		{
			return TileEntityStampMill.CIS.get(world.getBlockState(center).getValue(BlockStampMill.FACING).ordinal());
		}
		return new HashSet<Tuple<Vec3i, PartSlot>>();
	}

	@Override
	public Set<Tuple<Vec3i, PartSlot>> multipartTransConnections(int pos, BlockPos center, World world)
	{
		return new HashSet<Tuple<Vec3i, PartSlot>>();
	}

	@Override
	public Set<Vec3i> cisConnections(int pos, BlockPos center, World world)
	{
		if ((pos == 5 || pos == 3) && world.getBlockState(center).getBlock() == SprocketsBlocks.stampMill)
		{
			return TileEntityStampMill.BLOCK_CIS.get(world.getBlockState(center).getValue(BlockStampMill.FACING).ordinal());
		}
		return new HashSet<Vec3i>();
	}

	@Override
	public Set<Vec3i> transConnections(int pos, BlockPos center, World world)
	{
		return new HashSet<Vec3i>();
	}
	


}
