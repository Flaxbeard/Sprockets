package flaxbeard.sprockets.blocks.tiles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mcmultipart.multipart.PartSlot;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import flaxbeard.sprockets.api.IMechanicalProducer;
import flaxbeard.sprockets.api.tool.IGyrometerable;
import flaxbeard.sprockets.blocks.SprocketsBlocks;
import flaxbeard.sprockets.lib.LibConstants;
import flaxbeard.sprockets.multiparts.SprocketsMultiparts;

public class TileEntityWaterWheel extends TileEntitySprocketBase implements IGyrometerable, IMechanicalProducer
{
	private static final ArrayList<HashSet<Tuple<Vec3i, PartSlot>>> CIS;
	private static final List<Set<Vec3i>> BLOCK_CIS;

	public int facing = -1;
	public byte canSpin = -1;
	public int torque = 0;
	public int lastTorque = 0;
	public boolean dir = false;
	public boolean lastDir = false;
	public boolean connLeft = false;
	public boolean connRight = false;
	private static final Vec3i UP = EnumFacing.UP.getDirectionVec();
	private static final Vec3i DOWN = EnumFacing.DOWN.getDirectionVec();

	public static Map<Integer, Set<BlockPos>> claimedWaters = new HashMap<Integer, Set<BlockPos>>();
	private Set<BlockPos> thisClaimed = new HashSet<BlockPos>();
	
	static
	{
		CIS = new ArrayList<HashSet<Tuple<Vec3i, PartSlot>>>();
		BLOCK_CIS = new ArrayList<Set<Vec3i>>();
		for (int side = 0; side < 6; side++)
		{
			CIS.add(SprocketsMultiparts.rotatePartFacing(side, new Tuple(new Vec3i(0, 1, 0), PartSlot.DOWN), new Tuple(new Vec3i(0, -1, 0), PartSlot.UP)));
			BLOCK_CIS.add(SprocketsMultiparts.rotateFacing(side, UP, DOWN));
		}
		
	}
	
	public boolean isClaimed(BlockPos water)
	{
		int dim = worldObj.provider.getDimension();
		if (claimedWaters.containsKey(dim))
		{
			return claimedWaters.get(dim).contains(water);
		}
		return false;
	}
	
	public void claimWater(BlockPos water)
	{
		int dim = worldObj.provider.getDimension();
		if (!claimedWaters.containsKey(dim))
		{
			claimedWaters.put(dim, new HashSet<BlockPos>());
		}
		
		claimedWaters.get(dim).add(water);
		thisClaimed.add(water);
	}
	
	public void clearWater()
	{
		int dim = worldObj.provider.getDimension();
		if (claimedWaters.containsKey(dim))
		{
			for (BlockPos water : thisClaimed)
			{
				claimedWaters.get(dim).remove(water);
			}
		}
	}
	
	@Override
	public void update()
	{
		super.update();
		
		if ((this.worldObj.getTotalWorldTime() % LibConstants.WINDMILL_UPDATE_TICKS == 0 || canSpin == -1) && facing != -1)
		{
			checkSurroundings();
		}

		if (facing == -1 && worldObj != null && worldObj.getBlockState(getPosMC()) != null && worldObj.getBlockState(getPosMC()).getBlock() == SprocketsBlocks.waterwheel)
		{
			facing = SprocketsBlocks.waterwheel.getMetaFromState(worldObj.getBlockState(getPosMC())) % 6;
		}

	}
	
	private int getDepth(IBlockState state)
	{
		return state.getMaterial() == Material.WATER ? ((Integer)state.getValue(Blocks.FLOWING_WATER.LEVEL)).intValue() : -1;
	}
	
	private void checkSurroundings()
	{
		clearWater();
		canSpin = 1;
		lastDir = dir;
		lastTorque = torque;
		
		BlockPos src = findSourceBlock();
		int num = 0;
		
		torque = 0;
		
		if (src != null)
		{
			int i = 0;
//			BlockPos res = null;
//			outerLoop:
//			while (src != null && i < 1000)
//			{
//				BlockPos best = null;
//				int bestVal = 0;
//				for (int direction = 1; direction < 6; direction++)
//				{
//					EnumFacing face = EnumFacing.VALUES[direction];
//					BlockPos n = src.add(face.getDirectionVec());
//					IBlockState nState = worldObj.getBlockState(n);
//					
//					int depth = getDepth(nState);
//					if (depth == 0 && getDepth(worldObj.getBlockState(n.add(0, 1, 0))) <= 0)
//					{
//						res = n;
//						break outerLoop;
//					}
//					else if (depth != -1)
//					{
//						System.out.println(best + " " + bestVal + " " + n + " " + depth);
//						if (depth > bestVal || n.getY() > best.getY())
//						{
//							bestVal = depth;
//							best = n;
//						}
//					}
//				}
//				src = best;
//				i++;
//			}
//			
//			System.out.println(res + " ");
//			
			BlockPos res = src;
			if (res != null)
			{
				num = 1;
				Set<BlockPos> toCheck = new HashSet<BlockPos>();
				Set<BlockPos> checked = new HashSet<BlockPos>();
				toCheck.add(res);
				checked.add(res);
				int k = 0;
				
				while (num < LibConstants.WATERWHEEL_NUM_WATER && k < 1000 && toCheck.size() > 0)
				{
					res = toCheck.iterator().next();
					toCheck.remove(res);
					for (int direction = 1; direction < 6; direction++)
					{
						EnumFacing face = EnumFacing.VALUES[direction];
						BlockPos n = res.add(face.getDirectionVec());
						IBlockState nState = worldObj.getBlockState(n);
						if (getDepth(nState) == 0 && !checked.contains(n))
						{
							toCheck.add(n);
							checked.add(n);
							
							if (!isClaimed(n))
							{
								num++;
								claimWater(n);
							}
						}
						else if (getDepth(nState) > 0 && !checked.contains(n))
						{
							toCheck.add(n);
							checked.add(n);
						}
					}
					k++;
				}
				
				torque = Math.min(LibConstants.WATERWHEEL_NUM_WATER, num);
				
			}
		}
		
		
		
		if (dir != lastDir || torque != lastTorque)
		{
			this.getNetwork().updateNetworkSpeedAndTorque();
		}
	}

	private BlockPos findSourceBlock()
	{
		for (int w = -LibConstants.WATER_WHEEL_RAIDUS; w <= LibConstants.WATER_WHEEL_RAIDUS; w++)
		{
			int y = LibConstants.WATER_WHEEL_RAIDUS + 1;
			BlockPos pos2 = pos.add(facing <= 3 ? w : 0, y, facing <= 3 ? 0 : w);
			if (getDepth(worldObj.getBlockState(pos2)) > 0)
			{
				Vec3d move = Blocks.FLOWING_WATER.modifyAcceleration(worldObj, pos2, null, new Vec3d(0, 0, 0));
				dir = (facing <= 3 ? (move.xCoord > 0) : (move.zCoord < 0));

				return pos2;
			}
		}
		
		for (int w = -LibConstants.WATER_WHEEL_RAIDUS; w <= LibConstants.WATER_WHEEL_RAIDUS; w++)
		{
			int y = -(LibConstants.WATER_WHEEL_RAIDUS + 1);
			BlockPos pos2 = pos.add(facing <= 3 ? w : 0, y, facing <= 3 ? 0 : w);
			
			if (getDepth(worldObj.getBlockState(pos2)) > 0)
			{
				Vec3d move = Blocks.FLOWING_WATER.modifyAcceleration(worldObj, pos2, null, new Vec3d(0, 0, 0));
				dir = (facing <= 3 ? (move.xCoord < 0) : (move.zCoord > 0));
				return pos2;
			}
		}
		
		for (int y = -LibConstants.WATER_WHEEL_RAIDUS; y <= LibConstants.WATER_WHEEL_RAIDUS; y++)
		{
			int w = LibConstants.WATER_WHEEL_RAIDUS + 1;
			BlockPos pos2 = pos.add(facing <= 3 ? w : 0, y, facing <= 3 ? 0 : w);
			if (getDepth(worldObj.getBlockState(pos2)) > 0)
			{
				dir = false ^ facing <= 3;
				return pos2;
			}
		}
		
		for (int y = -LibConstants.WATER_WHEEL_RAIDUS; y <= LibConstants.WATER_WHEEL_RAIDUS; y++)
		{
			int w = -(LibConstants.WATER_WHEEL_RAIDUS + 1);
			BlockPos pos2 = pos.add(facing <= 3 ? w : 0, y, facing <= 3 ? 0 : w);
			if (getDepth(worldObj.getBlockState(pos2)) > 0)
			{
				dir = true ^ facing <= 3;
				return pos2;
			}
		}
		
		return null;
	}
	

	@Override
	public boolean isNegativeDirection()
	{
		return true;
	}

	@Override
	public HashSet<Tuple<Vec3i, PartSlot>> multipartCisConnections()
	{
		if (facing == -1 && worldObj != null && worldObj.getBlockState(getPosMC()) != null && worldObj.getBlockState(getPosMC()).getBlock() == SprocketsBlocks.waterwheel)
		{
			facing = SprocketsBlocks.waterwheel.getMetaFromState(worldObj.getBlockState(getPosMC())) % 6;
		}

		
		if (facing == -1)
		{
			return new HashSet<Tuple<Vec3i, PartSlot>>();
		}
		return CIS.get(facing);
	}

	@Override
	public HashSet<Tuple<Vec3i, PartSlot>> multipartTransConnections()
	{
		return new HashSet<Tuple<Vec3i, PartSlot>>();
	}

	@Override
	public Set<Vec3i> cisConnections()
	{
		if (facing == -1 && worldObj != null && worldObj.getBlockState(getPosMC()) != null && worldObj.getBlockState(getPosMC()).getBlock() == SprocketsBlocks.waterwheel)
		{
			facing = SprocketsBlocks.waterwheel.getMetaFromState(worldObj.getBlockState(getPosMC())) % 6;
		}

		
		if (facing == -1)
		{
			return new HashSet<Vec3i>();
		}
		return BLOCK_CIS.get(facing);
	}

	@Override
	public HashSet<Vec3i> transConnections()
	{
		return new HashSet<Vec3i>();
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		this.canSpin = compound.getByte("canSpin");
		this.torque = compound.getInteger("torque");
		this.dir = compound.getBoolean("dir");
		this.connLeft = compound.getBoolean("connLeft");
		this.connRight = compound.getBoolean("connRight");
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		compound = super.writeToNBT(compound);
		compound.setByte("canSpin", canSpin);
		compound.setInteger("torque", torque);
		compound.setBoolean("dir", dir);
		compound.setBoolean("connLeft", connLeft);
		compound.setBoolean("connRight", connRight);
		return compound;
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		NBTTagCompound data = pkt.getNbtCompound();
		this.readFromNBT(data);
	}
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		NBTTagCompound data = new NBTTagCompound();
		this.writeToNBT(data);
		return new SPacketUpdateTileEntity(pos, 0, data);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInfo(List<ITextComponent> list)
	{
	
			
	}

	@Override
	public float torqueProduced()
	{
		return (torque * 1F / LibConstants.WATERWHEEL_NUM_WATER) * LibConstants.WATERWHEEL_TORQUE;
	}

	@Override
	public float speedProduced()
	{
		return torque > 0 ? (dir ? -LibConstants.WATERWHEEL_SPEED : LibConstants.WATERWHEEL_SPEED) : 0F;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
    public AxisAlignedBB getRenderBoundingBox()
    {
		return new AxisAlignedBB(
				pos.add(facing <= 3 ? -3 : 0, -3, facing <= 3 ? 0 : -3),
				pos.add(facing <= 3 ? 4 : 0, 4, facing <= 3 ? 0 : 4));
    }
	
	public void destroy()
	{
		clearWater();
		for (int w = -LibConstants.WATER_WHEEL_RAIDUS; w <= LibConstants.WATER_WHEEL_RAIDUS; w++)
		{
			for (int y = -LibConstants.WATER_WHEEL_RAIDUS; y <= LibConstants.WATER_WHEEL_RAIDUS; y++)
			{
				BlockPos pos2 = pos.add(facing <= 3 ? w : 0, y, facing <= 3 ? 0 : w);
				if (worldObj.getBlockState(pos2).getBlock() == SprocketsBlocks.waterwheelComponent)
				{
					worldObj.setBlockToAir(pos2);
				}
			}
		}
		
		if (worldObj.getBlockState(pos).getBlock() == SprocketsBlocks.waterwheel)
		{
			worldObj.destroyBlock(getPos(), true);
		}
		
	}
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
	{
		return oldState.getBlock() != newState.getBlock();
	}

}
