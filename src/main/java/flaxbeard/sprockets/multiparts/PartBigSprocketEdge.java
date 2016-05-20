package flaxbeard.sprockets.multiparts;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;

import mcmultipart.multipart.IMultipart;
import mcmultipart.multipart.IMultipartContainer;
import mcmultipart.multipart.ISlottedPart;
import mcmultipart.multipart.MultipartHelper;
import mcmultipart.multipart.PartSlot;
import mcmultipart.raytrace.PartMOP;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import flaxbeard.sprockets.api.IMechanicalConduit;
import flaxbeard.sprockets.lib.LibConstants;

public class PartBigSprocketEdge extends PartSprocketBase implements ISlottedPart, IMechanicalConduit
{
	
	public int facing = 0;
	private BlockPos parent;
	private int index = 0;
	
	private static final AxisAlignedBB[] BOUNDS;
	private static final AxisAlignedBB[] RENDER_BOUNDS;
	private static final HashSet<Vec3i> BLOCK_CIS_CONNECTIONS;
	private static final ArrayList<ArrayList<HashSet<Vec3i>>>  BLOCK_TRANS_CONNECTIONS;
	private static final ArrayList<ArrayList<HashSet<Tuple<Vec3i, PartSlot>>>> PART_CIS_CONNECTIONS;
	private static final ArrayList<ArrayList<HashSet<Tuple<Vec3i, PartSlot>>>> PART_TRANS_CONNECTIONS;
	private static final Vec3i HERE = new Vec3i(0, 0, 0);
	private static final Vec3i NORTH = EnumFacing.NORTH.getDirectionVec();
	private static final Vec3i SOUTH = EnumFacing.SOUTH.getDirectionVec();
	private static final Vec3i EAST = EnumFacing.EAST.getDirectionVec();
	private static final Vec3i WEST = EnumFacing.WEST.getDirectionVec();
	private static final Vec3i UP = EnumFacing.UP.getDirectionVec();
	private static final Vec3i DOWN = EnumFacing.DOWN.getDirectionVec();
	private static final ArrayList<ArrayList<EnumSet<PartSlot>>> MASK;
	private static final ArrayList<PartSlot> FACING;
	private int c = 0;
	
	static
	{
		
		BOUNDS = new AxisAlignedBB[6];
		RENDER_BOUNDS = new AxisAlignedBB[6];
		BLOCK_TRANS_CONNECTIONS = new ArrayList<ArrayList<HashSet<Vec3i>>>();
		BLOCK_CIS_CONNECTIONS = new HashSet<Vec3i>();
		PART_CIS_CONNECTIONS = new ArrayList<ArrayList<HashSet<Tuple<Vec3i, PartSlot>>>>();
		PART_TRANS_CONNECTIONS = new ArrayList<ArrayList<HashSet<Tuple<Vec3i, PartSlot>>>>();
		MASK = new ArrayList<ArrayList<EnumSet<PartSlot>>>();
		FACING = new ArrayList<PartSlot>();
		for (int i = 0; i < 4; i++)
		{
			BLOCK_TRANS_CONNECTIONS.add(new ArrayList<HashSet<Vec3i>>());
			PART_CIS_CONNECTIONS.add(new ArrayList<HashSet<Tuple<Vec3i, PartSlot>>>());
			PART_TRANS_CONNECTIONS.add(new ArrayList<HashSet<Tuple<Vec3i, PartSlot>>>());
			MASK.add(new ArrayList<EnumSet<PartSlot>>());
		}
		for (int side = 0; side < 6; side++)
		{
			int three = 0;
			int two = 2;
			int one = 1;
			int zero = 3;
			if (side == 0)
			{
				zero = 3;
				three = 0;
				two = 2;
				one = 1;
			}
			else if (side < 2)
			{
				three = 0;
				zero = 3;
				two = 1;
				one = 2;
			}
			else if (side > 3)
			{
				three = 1;
				zero = 2;
				two = 3;
				one = 0;
			}
			
			if (side == 3)
			{
				three = 0;
				zero = 3;
			}
			else if (side == 2)
			{
				three = 3;
				zero = 0;
			}
			else if (side == 4)
			{
				two = 0;
				one = 3;
			}
			BOUNDS[side] = SprocketsMultiparts.rotateAxis(side, 0F / 16F, 15F / 16F, 0F / 16F, 16F / 16F, 16F / 16F, 16F / 16F);
			RENDER_BOUNDS[side] = SprocketsMultiparts.rotateAxis(side, -16F / 16F, 15F / 16F, -16F / 16F, 32F / 16F, 16F / 16F, 32F / 16F);
			
			PART_CIS_CONNECTIONS.get(zero).add(SprocketsMultiparts.rotatePartFacing(side, 
					new Tuple(NORTH, PartSlot.UP)
					));
			PART_CIS_CONNECTIONS.get(one).add(SprocketsMultiparts.rotatePartFacing(side, 
					new Tuple(EAST, PartSlot.UP)
					));
			PART_CIS_CONNECTIONS.get(three).add(SprocketsMultiparts.rotatePartFacing(side, 
					new Tuple(SOUTH, PartSlot.UP)
					));
			PART_CIS_CONNECTIONS.get(two).add(SprocketsMultiparts.rotatePartFacing(side, 
					new Tuple(WEST, PartSlot.UP)
					));
			
			PART_TRANS_CONNECTIONS.get(zero).add(SprocketsMultiparts.rotatePartFacing(side, 
					new Tuple(SOUTH, PartSlot.UP),
					new Tuple(new Vec3i(0, 1, 1), PartSlot.NORTH),
					new Tuple(HERE, PartSlot.SOUTH)
					));
			PART_TRANS_CONNECTIONS.get(one).add(SprocketsMultiparts.rotatePartFacing(side, 
					new Tuple(WEST, PartSlot.UP),
					new Tuple(new Vec3i(-1, 1, 0), PartSlot.EAST),
					new Tuple(HERE, PartSlot.WEST)
					));
			PART_TRANS_CONNECTIONS.get(three).add(SprocketsMultiparts.rotatePartFacing(side, 
					new Tuple(NORTH, PartSlot.UP),
					new Tuple(new Vec3i(0, 1, -1), PartSlot.SOUTH),
					new Tuple(HERE, PartSlot.NORTH)
					));
			PART_TRANS_CONNECTIONS.get(two).add(SprocketsMultiparts.rotatePartFacing(side, 
					new Tuple(EAST, PartSlot.UP),
					new Tuple(new Vec3i(1, 1, 0), PartSlot.WEST),
					new Tuple(HERE, PartSlot.EAST)
					));
			
			BLOCK_TRANS_CONNECTIONS.get(zero).add(SprocketsMultiparts.rotateFacing(side, SOUTH));
			BLOCK_TRANS_CONNECTIONS.get(one).add(SprocketsMultiparts.rotateFacing(side, WEST));
			BLOCK_TRANS_CONNECTIONS.get(three).add(SprocketsMultiparts.rotateFacing(side, NORTH));
			BLOCK_TRANS_CONNECTIONS.get(two).add(SprocketsMultiparts.rotateFacing(side, EAST));
			
			MASK.get(zero).add(EnumSet.of(
					SprocketsMultiparts.rotatePartSlot(side, PartSlot.UP),
					SprocketsMultiparts.rotatePartSlot(side, PartSlot.EAST),
					SprocketsMultiparts.rotatePartSlot(side, PartSlot.WEST),
					SprocketsMultiparts.rotatePartSlot(side, PartSlot.NORTH)
					));
			MASK.get(one).add(EnumSet.of(
					SprocketsMultiparts.rotatePartSlot(side, PartSlot.UP),
					SprocketsMultiparts.rotatePartSlot(side, PartSlot.EAST),
					SprocketsMultiparts.rotatePartSlot(side, PartSlot.NORTH),
					SprocketsMultiparts.rotatePartSlot(side, PartSlot.SOUTH)
					));
			MASK.get(three).add(EnumSet.of(
					SprocketsMultiparts.rotatePartSlot(side, PartSlot.UP),
					SprocketsMultiparts.rotatePartSlot(side, PartSlot.EAST),
					SprocketsMultiparts.rotatePartSlot(side, PartSlot.WEST),
					SprocketsMultiparts.rotatePartSlot(side, PartSlot.SOUTH)
					));
			MASK.get(two).add(EnumSet.of(
					SprocketsMultiparts.rotatePartSlot(side, PartSlot.UP),
					SprocketsMultiparts.rotatePartSlot(side, PartSlot.WEST),
					SprocketsMultiparts.rotatePartSlot(side, PartSlot.NORTH),
					SprocketsMultiparts.rotatePartSlot(side, PartSlot.SOUTH)
					));
			FACING.add(SprocketsMultiparts.rotatePartSlot(side, PartSlot.UP));
		}		
		
		//BLOCK_CONNECTIONS = (ArrayList<EnumFacing>[]) temp.toArray();
		
	}
	
	@Override
	public boolean onActivated(EntityPlayer player, EnumHand hand,  ItemStack heldItem, PartMOP hit)
	{
		return super.onActivated(player, hand, heldItem, hit);
	}
	
	
	@Override
	public EnumSet<PartSlot> getSlotMask()
	{
		return MASK.get(index).get(facing).clone();
	}
	
	public void setSlot(int side)
	{
		this.facing = side;
	}

	@Override
	public void addSelectionBoxes(List<AxisAlignedBB> list)
	{
		list.add(new AxisAlignedBB(
				BOUNDS[facing].minX,
				BOUNDS[facing].minY,
				BOUNDS[facing].minZ,
				BOUNDS[facing].maxX,
				BOUNDS[facing].maxY,
				BOUNDS[facing].maxZ
				));
	}
	
	@Override
	public void addCollisionBoxes(AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity)
	{
		if (BOUNDS[facing].intersectsWith(mask))
		{
			list.add(BOUNDS[facing]);
		}
	}
	
	@Override
	public ItemStack getPickBlock(EntityPlayer player, PartMOP hit)
	{
		return new ItemStack(SprocketsMultiparts.bigSprocket, 1, damage);
	}

	@Override
	public float maxSpeed()
	{
		switch(damage)
		{
			case 0:
				return LibConstants.MAX_SPEED_WOODEN_SPROCKET;
			case 1:
				return LibConstants.MAX_SPEED_STONE_SPROCKET;
		}
		return LibConstants.MAX_SPEED_IRON_SPROCKET;
	}

	@Override
	public float minTorque()
	{
		switch(damage)
		{
			case 1:
				return LibConstants.MIN_TORQUE_STONE_SPROCKET;
			case 2:
				return LibConstants.MIN_TORQUE_IRON_SPROCKET;
		}
		return LibConstants.MIN_TORQUE_WOODEN_SPROCKET;
	}

	@Override
	public float maxTorque()
	{
		switch(damage)
		{
			case 0:
				return LibConstants.MAX_TORQUE_WOODEN_SPROCKET * 3;
			case 1:
				return LibConstants.MAX_TORQUE_STONE_SPROCKET * 3;
		}
		return LibConstants.MAX_TORQUE_IRON_SPROCKET * 3;
	}

	@Override
	public float sizeMultiplier()
	{
		return 3.0F;
	}

	@Override
	public HashSet<Tuple<Vec3i, PartSlot>> multipartCisConnections()
	{
		return PART_CIS_CONNECTIONS.get(index).get(facing);
	}

	@Override
	public HashSet<Tuple<Vec3i, PartSlot>> multipartTransConnections()
	{
		return PART_TRANS_CONNECTIONS.get(index).get(facing);
	}

	@Override
	public HashSet<Vec3i> cisConnections()
	{
		return BLOCK_CIS_CONNECTIONS;
	}

	@Override
	public HashSet<Vec3i> transConnections()
	{
		return BLOCK_TRANS_CONNECTIONS.get(index).get(facing);
	}
	
	@Override
	public boolean isNegativeDirection()
	{
		return facing % 2 == 0;
	}


	@Override
	public List<ItemStack> getDrops()
	{
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		drops.add(new ItemStack(SprocketsMultiparts.bigSprocket, 1, damage));
		return drops;
	}
	
	@Override
	public void setDamage(int damage)
	{
		this.damage = damage;
		switch(damage)
		{
			case 0:
				setMaterial(Material.WOOD);
				setHardness(LibConstants.MINE_TIME_WOODEN_SPROCKET * LibConstants.MINE_TIME_MULT_BIG_SPROCEKT);
				break;
			case 1:
				setMaterial(Material.ROCK);
				setHardness(LibConstants.MINE_TIME_STONE_SPROCKET * LibConstants.MINE_TIME_MULT_BIG_SPROCEKT);
				break;
			case 2:
				setMaterial(Material.IRON);
				setHardness(LibConstants.MINE_TIME_IRON_SPROCKET * LibConstants.MINE_TIME_MULT_BIG_SPROCEKT);
				break;
		}
	}
	
	
	
	
	public void setParent(BlockPos pos)
	{
		this.parent = pos;
	}
	
	public void setIndex(int index)
	{
		this.index = index;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		
		facing = nbt.getInteger("facing");
		int x = nbt.getInteger("x");
		int y = nbt.getInteger("y");
		int z = nbt.getInteger("z");
		int index = nbt.getInteger("index");
		this.setParent(new BlockPos(x, y, z));
		this.setSlot(facing);
		this.setIndex(index);
	}


	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt = super.writeToNBT(nbt);
		nbt.setInteger("facing", facing);
		nbt.setInteger("x", parent.getX());
		nbt.setInteger("y", parent.getY());
		nbt.setInteger("z", parent.getZ());
		nbt.setInteger("index", index);
		return nbt;
	}
	
	@Override
	public void writeUpdatePacket(PacketBuffer buf)
	{
		super.writeUpdatePacket(buf);
		buf.writeByte(facing);
		buf.writeInt(parent.getX());
		buf.writeInt(parent.getY());
		buf.writeInt(parent.getZ());
		buf.writeInt(index);
	
	}
	
	@Override
	public void readUpdatePacket(PacketBuffer buf)
	{
		super.readUpdatePacket(buf);
		facing = buf.readByte();
		int x = buf.readInt();
		int y = buf.readInt();
		int z = buf.readInt();
		int index = buf.readInt();
		this.setParent(new BlockPos(x, y, z));
		this.setSlot(facing);
		this.setIndex(index);
		
	}
	
	@Override
	public void onRemoved()
	{
		super.onRemoved();

		IMultipartContainer container = MultipartHelper.getPartContainer(getWorldMC(), getPosMC().subtract(parent));
		if (container != null)
		{
			IMultipart part = container.getPartInSlot(FACING.get(facing));
			if (part != null)
			{
				container.removePart(part);
			}
		}
	}

}
