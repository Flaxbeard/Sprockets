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
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import flaxbeard.sprockets.api.IMechanicalConduit;
import flaxbeard.sprockets.lib.LibConstants;

public class PartBigSprocketCenter extends PartSprocketBase implements ISlottedPart, IMechanicalConduit
{
	
	public int facing = 0;
	
	private static final AxisAlignedBB[] BOUNDS;
	public static final AxisAlignedBB[] RENDER_BOUNDS;
	private static final ArrayList<HashSet<Vec3i>> BLOCK_CIS_CONNECTIONS;
	private static final HashSet<Vec3i> BLOCK_TRANS_CONNECTIONS;
	private static final ArrayList<HashSet<Tuple<Vec3i, PartSlot>>> PART_CIS_CONNECTIONS;
	private static final HashSet<Tuple<Vec3i, PartSlot>> PART_TRANS_CONNECTIONS;
	private static final Vec3i HERE = new Vec3i(0, 0, 0);
	private static final Vec3i NORTH = EnumFacing.NORTH.getDirectionVec();
	private static final Vec3i SOUTH = EnumFacing.SOUTH.getDirectionVec();
	private static final Vec3i EAST = EnumFacing.EAST.getDirectionVec();
	private static final Vec3i WEST = EnumFacing.WEST.getDirectionVec();
	private static final Vec3i UP = EnumFacing.UP.getDirectionVec();
	private static final Vec3i DOWN = EnumFacing.DOWN.getDirectionVec();
	private static final ArrayList<EnumSet<PartSlot>> MASK;
	private static final ArrayList<PartSlot> FACING;
	private int c = 0;
	
	static
	{
		
		BOUNDS = new AxisAlignedBB[6];
		RENDER_BOUNDS = new AxisAlignedBB[6];
		BLOCK_CIS_CONNECTIONS = new ArrayList<HashSet<Vec3i>>();
		BLOCK_TRANS_CONNECTIONS = new HashSet<Vec3i>();
		PART_CIS_CONNECTIONS = new ArrayList<HashSet<Tuple<Vec3i, PartSlot>>>();
		PART_TRANS_CONNECTIONS = new HashSet<Tuple<Vec3i, PartSlot>>();
		MASK = new ArrayList<EnumSet<PartSlot>>();
		FACING = new ArrayList<PartSlot>();
		for (int side = 0; side < 6; side++)
		{
			BOUNDS[side] = SprocketsMultiparts.rotateAxis(side, 0F / 16F, 15F / 16F, 0F / 16F, 16F / 16F, 16F / 16F, 16F / 16F);
			RENDER_BOUNDS[side] = SprocketsMultiparts.rotateAxis(side, -16F / 16F, 15F / 16F, -16F / 16F, 32F / 16F, 16F / 16F, 32F / 16F);
			BLOCK_CIS_CONNECTIONS.add(SprocketsMultiparts.rotateFacing(side, UP));
			PART_CIS_CONNECTIONS.add(SprocketsMultiparts.rotatePartFacing(side, new Tuple(HERE, PartSlot.CENTER), new Tuple(UP, PartSlot.DOWN),
					new Tuple(WEST, PartSlot.UP),
					new Tuple(EAST, PartSlot.UP),
					new Tuple(SOUTH, PartSlot.UP),
					new Tuple(NORTH, PartSlot.UP)
					));
			MASK.add(EnumSet.of(
					SprocketsMultiparts.rotatePartSlot(side, PartSlot.UP),
					SprocketsMultiparts.rotatePartSlot(side, PartSlot.EAST),
					SprocketsMultiparts.rotatePartSlot(side, PartSlot.WEST),
					SprocketsMultiparts.rotatePartSlot(side, PartSlot.NORTH),
					SprocketsMultiparts.rotatePartSlot(side, PartSlot.SOUTH)
					));
			FACING.add(SprocketsMultiparts.rotatePartSlot(side, PartSlot.UP));
		}		
		
		//BLOCK_CONNECTIONS = (ArrayList<EnumFacing>[]) temp.toArray();
		
	}
	
	@Override
	public boolean onActivated(EntityPlayer player, EnumHand hand, ItemStack heldItem, PartMOP hit)
	{

		return false;
	}
	
	@Override
	public EnumSet<PartSlot> getSlotMask()
	{
		return MASK.get(facing).clone();
	}
	
	public void setSlot(int side)
	{
		this.facing = side;
	}

	@Override
	public void addSelectionBoxes(List<AxisAlignedBB> list)
	{
		list.add(BOUNDS[facing]);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public AxisAlignedBB getRenderBoundingBox()
	{
		return RENDER_BOUNDS[facing];
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
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		
		facing = nbt.getInteger("facing");
		this.setSlot(facing);
	}


	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt = super.writeToNBT(nbt);
		nbt.setInteger("facing", facing);
		return nbt;
	}
	
	@Override
	public void writeUpdatePacket(PacketBuffer buf)
	{
		super.writeUpdatePacket(buf);
		buf.writeByte(facing);
	
	}
	
	@Override
	public void readUpdatePacket(PacketBuffer buf)
	{
		super.readUpdatePacket(buf);
		facing = buf.readByte();
		this.setSlot(facing);
		
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
		return PART_CIS_CONNECTIONS.get(facing);
	}

	@Override
	public HashSet<Tuple<Vec3i, PartSlot>> multipartTransConnections()
	{
		return PART_TRANS_CONNECTIONS;
	}

	@Override
	public HashSet<Vec3i> cisConnections()
	{
		return BLOCK_CIS_CONNECTIONS.get(facing);
	}

	@Override
	public HashSet<Vec3i> transConnections()
	{
		return BLOCK_TRANS_CONNECTIONS;
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
	
	@Override
	public void onRemoved()
	{
		super.onRemoved();

		
		for (int i = -1; i <= 1; i++)
		{
			for (int j = -1; j <= 1; j++)
			{
				if (i != 0 || j != 0)
				{
					int x = 0;
					int y = i;
					int z = j;
					if (facing < 2)
					{
						x = i;
						y = 0;
						z = j;
					}
					else if (facing < 4)
					{
						x = i;
						y = j;
						z = 0;
					}
					
					IMultipartContainer container = MultipartHelper.getPartContainer(getWorldMC(), getPosMC().add(x, y, z));
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
		}
		
	}

}
