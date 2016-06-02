package flaxbeard.sprockets.blocks.tiles;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import flaxbeard.sprockets.blocks.SprocketsBlocks;

public class TileEntityPump extends TileEntity implements ITickable
{

	@Override
	public void update()
	{
		if (!worldObj.isRemote && worldObj.isAirBlock(pos.add(0, 1, 0)))
		{
			if (go())
			{
				worldObj.setBlockState(pos.add(0, 1, 0), Blocks.FLOWING_WATER.getDefaultState(), 1);
			}
		}
	}
	
	public boolean go()
	{
		Set<BlockPos> s = new HashSet<BlockPos>();
		return go(s);
	}
	
	public boolean go(Set<BlockPos> visited)
	{
		if (!visited.contains(pos))
		{
			visited.add(pos);
			BlockPos b = getBlock();
			if (b == null)
			{
				return false;
			}
			else
			{
				boolean canPull = true;
				BlockPos pumpPos = b.add(0, -1, 0);
				if (worldObj.getBlockState(pumpPos).getBlock() == SprocketsBlocks.pump)
				{
					if (!((TileEntityPump) worldObj.getTileEntity(pumpPos)).go(visited))
					{
						canPull = false;
					}
				}
				else
				{
					canPull = false;
				}
				
				if (!canPull)
				{
					worldObj.setBlockToAir(b);
					System.out.println("removing water at pos" + b);
				}
				return true;
			}
		}
		else
		{
			return false;
		}
	}
	
	public BlockPos getBlock()
	{
		BlockPos p = pos.add(0, -1, 0);
		IBlockState ibs = worldObj.getBlockState(p);
		
		if (ibs.getBlock() == SprocketsBlocks.pump)
		{
			return ((TileEntityPump) worldObj.getTileEntity(p)).getBlock();
		}
		
		int depth = getDepth(ibs);
		if (depth == 0)
		{
			return p;
		}
		else if (depth > 0)
		{
			int counter = 0;
			boolean newP = true;
			while (depth != 0 && counter < 32 && newP)
			{
				newP = false;
				for (int i = 1; i < 6; i++)
				{
					EnumFacing dir = EnumFacing.VALUES[i];
					BlockPos newPos = p.add(dir.getDirectionVec());
					IBlockState s = worldObj.getBlockState(newPos);
					int nD = getDepth(s);
					int sD = getDepth(worldObj.getBlockState(newPos.add(0, 1, 0)));
					
					if (nD >= 0 && sD >= 0)
					{
						depth = sD;
						p = newPos.add(0, 1, 0);
						newP = true;
					}
					else if (nD >= 0 && nD < depth)
					{
						depth = nD;
						p = newPos;
						newP = true;
					}
				}
				counter++;
			}
			if (depth == 0)
			{
				return p;
			}
		}
		return null;
	}
	
	private int getDepth(IBlockState state)
	{
		return state.getMaterial() == Material.WATER ? ((Integer)state.getValue(Blocks.FLOWING_WATER.LEVEL)).intValue() : -1;
	}

}
