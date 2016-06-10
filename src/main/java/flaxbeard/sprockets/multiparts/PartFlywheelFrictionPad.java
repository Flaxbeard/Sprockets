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
import flaxbeard.sprockets.api.IMechanicalConduit;
import flaxbeard.sprockets.items.SprocketsItems;
import flaxbeard.sprockets.lib.LibConstants;

public class PartFlywheelFrictionPad extends PartSprocketBase implements ISlottedPart, IMechanicalConduit
{
	
	public int facing = 0;
	
	private static final ArrayList<HashSet<Vec3i>> BLOCK_CIS_CONNECTIONS;
	private static final ArrayList<HashSet<Vec3i>> BLOCK_TRANS_CONNECTIONS;
	private static final ArrayList<HashSet<Tuple<Vec3i, PartSlot>>> PART_CIS_CONNECTIONS;
	private static final ArrayList<HashSet<Tuple<Vec3i, PartSlot>>> PART_TRANS_CONNECTIONS;
	private static final Vec3i HERE = new Vec3i(0, 0, 0);
	private static final Vec3i NORTH = EnumFacing.NORTH.getDirectionVec();
	private static final Vec3i SOUTH = EnumFacing.SOUTH.getDirectionVec();
	private static final Vec3i EAST = EnumFacing.EAST.getDirectionVec();
	private static final Vec3i WEST = EnumFacing.WEST.getDirectionVec();
	private static final Vec3i UP = EnumFacing.UP.getDirectionVec();
	private static final Vec3i DOWN = EnumFacing.DOWN.getDirectionVec();
	private static final ArrayList<EnumSet<PartSlot>> MASK;
	public static final ArrayList<PartSlot> SLOT_FROM_FACING;
	private int c = 0;
	
	static
	{
		BLOCK_CIS_CONNECTIONS = new ArrayList<HashSet<Vec3i>>();
		BLOCK_TRANS_CONNECTIONS = new ArrayList<HashSet<Vec3i>>();
		PART_CIS_CONNECTIONS = new ArrayList<HashSet<Tuple<Vec3i, PartSlot>>>();
		PART_TRANS_CONNECTIONS = new ArrayList<HashSet<Tuple<Vec3i, PartSlot>>>();
		MASK = new ArrayList<EnumSet<PartSlot>>();
		SLOT_FROM_FACING = new ArrayList<PartSlot>();
		for (int side = 0; side < 6; side++)
		{
			BLOCK_CIS_CONNECTIONS.add(SprocketsMultiparts.rotateFacing(side, UP));
			BLOCK_TRANS_CONNECTIONS.add(new HashSet<Vec3i>());
			PART_CIS_CONNECTIONS.add(SprocketsMultiparts.rotatePartFacing(side, new Tuple(UP, PartSlot.DOWN)));
			PART_TRANS_CONNECTIONS.add(new HashSet<Tuple<Vec3i, PartSlot>>());
			MASK.add(EnumSet.of(SprocketsMultiparts.rotatePartSlot(side, PartSlot.UP)));
			SLOT_FROM_FACING.add(SprocketsMultiparts.rotatePartSlot(side, PartSlot.UP));
		}		
		
		//BLOCK_CONNECTIONS = (ArrayList<EnumFacing>[]) temp.toArray();
		
	}
	
	@Override
	public void update()
	{
		IMultipartContainer container = MultipartHelper.getPartContainer(getWorld(), getPos());
		if (this.getNetwork() != null && container != null)
		{
			IMultipart part = container.getPartInSlot(PartSlot.CENTER);
			if (part != null && part instanceof PartFlywheel)
			{
				PartFlywheel wheel = (PartFlywheel) part;
				if (wheel.getNetwork() != null && Math.abs(wheel.speed) < Math.abs(this.getNetwork().getSpeedForConduit(this)))
				{
					wheel.speed = this.getNetwork().getSpeedForConduit(this) * (wheel.getState()!= this.getState() ? 1 : -1);

				}
				wheel.power += this.getNetwork().getSpeedForConduit(this) * this.getNetwork().getTorqueForConduit(this);

			}
		}
		super.update();
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
	}

	@Override
	public void addCollisionBoxes(AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity)
	{

	}
	
	@Override
	public ItemStack getPickBlock(EntityPlayer player, PartMOP hit)
	{
		return new ItemStack(SprocketsMultiparts.sprocket, 1, damage);
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
	public float sizeMultiplier()
	{
		return 1;
	}

	@Override
	public HashSet<Tuple<Vec3i, PartSlot>> multipartCisConnections()
	{
		return PART_CIS_CONNECTIONS.get(facing);
	}

	@Override
	public HashSet<Tuple<Vec3i, PartSlot>> multipartTransConnections()
	{
		return PART_TRANS_CONNECTIONS.get(facing);
	}

	@Override
	public HashSet<Vec3i> cisConnections()
	{
		return BLOCK_CIS_CONNECTIONS.get(facing);
	}

	@Override
	public HashSet<Vec3i> transConnections()
	{
		return BLOCK_TRANS_CONNECTIONS.get(facing);
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
		drops.add(new ItemStack(SprocketsMultiparts.sprocket, 1, damage));
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
				setHardness(LibConstants.MINE_TIME_WOODEN_SPROCKET);
				break;
			case 1:
				setMaterial(Material.ROCK);
				setHardness(LibConstants.MINE_TIME_STONE_SPROCKET);
				break;
			case 2:
				setMaterial(Material.IRON);
				setHardness(LibConstants.MINE_TIME_IRON_SPROCKET);
				break;
		}
	}
	
	@Override
	public void onRemoved()
	{
		super.onRemoved();

		IMultipartContainer container = MultipartHelper.getPartContainer(getWorld(), getPos());
		if (container != null)
		{
			IMultipart part = container.getPartInSlot(PartSlot.CENTER);
			if (part != null)
			{
				container.removePart(part);
			}
		}
	}

}
