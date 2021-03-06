package flaxbeard.sprockets.multiparts;

import java.util.ArrayList;
import java.util.EnumSet;
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
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import flaxbeard.sprockets.lib.LibConstants;

public class PartBigSprocketPlaceholder extends PartSprocketBaseNoConduit implements ISlottedPart, ITickable
{
	public int facing = 0;
	
	private static final ArrayList<EnumSet<PartSlot>> MASK;
	private static final AxisAlignedBB[] BOUNDS;
	private static final ArrayList<PartSlot> FACING;
	
	private BlockPos parent;
	
	static
	{
		BOUNDS = new AxisAlignedBB[6];
		MASK = new ArrayList<EnumSet<PartSlot>>();
		FACING = new ArrayList<PartSlot>();
		for (int side = 0; side < 6; side++)
		{
			BOUNDS[side] = SprocketsMultiparts.rotateAxis(side, 0F / 16F, 15F / 16F, 0F / 16F, 16F / 16F, 16F / 16F, 16F / 16F);
			MASK.add(EnumSet.of(
					SprocketsMultiparts.rotatePartSlot(side, PartSlot.UP),
					SprocketsMultiparts.rotatePartSlot(side, PartSlot.EAST),
					SprocketsMultiparts.rotatePartSlot(side, PartSlot.WEST),
					SprocketsMultiparts.rotatePartSlot(side, PartSlot.NORTH),
					SprocketsMultiparts.rotatePartSlot(side, PartSlot.SOUTH)
					));
			FACING.add(SprocketsMultiparts.rotatePartSlot(side, PartSlot.UP));
		}				
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
	
	public void setParent(BlockPos pos)
	{
		this.parent = pos;
	}

	@Override
	public void addSelectionBoxes(List<AxisAlignedBB> list)
	{
		list.add(new AxisAlignedBB(
				BOUNDS[facing].minX,
				BOUNDS[facing].minY ,
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
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		
		facing = nbt.getInteger("facing");
		int x = nbt.getInteger("x");
		int y = nbt.getInteger("y");
		int z = nbt.getInteger("z");
		this.setParent(new BlockPos(x, y, z));
		this.setSlot(facing);
	}


	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt = super.writeToNBT(nbt);
		nbt.setInteger("facing", facing);
		nbt.setInteger("x", parent.getX());
		nbt.setInteger("y", parent.getY());
		nbt.setInteger("z", parent.getZ());
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
	
	}
	
	@Override
	public void readUpdatePacket(PacketBuffer buf)
	{
		super.readUpdatePacket(buf);
		facing = buf.readByte();
		int x = buf.readInt();
		int y = buf.readInt();
		int z = buf.readInt();
		this.setParent(new BlockPos(x, y, z));
		this.setSlot(facing);
		
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

		IMultipartContainer container = MultipartHelper.getPartContainer(getWorld(), getPos().subtract(parent));
		if (container != null)
		{
			IMultipart part = container.getPartInSlot(FACING.get(facing));
			if (part != null)
			{
				container.removePart(part);
			}
		}
		IMultipartContainer here = MultipartHelper.getPartContainer(getWorld(), getPos());

	}

	@Override
	public void update()
	{
		// TODO Auto-generated method stub
		
	}
}
