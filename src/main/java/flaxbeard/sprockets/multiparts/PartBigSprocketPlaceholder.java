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
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import flaxbeard.sprockets.lib.LibConstants;

public class PartBigSprocketPlaceholder extends PartSprocketBaseNoConduit implements ISlottedPart
{
	public int facing = 0;
	
	private static final ArrayList<EnumSet<PartSlot>> MASK;
	private static final AxisAlignedBB[] BOUNDS;
	private static final AxisAlignedBB[] RENDER_BOUNDS;
	private static final ArrayList<PartSlot> FACING;
	
	private BlockPos parent;
	
	static
	{
		BOUNDS = new AxisAlignedBB[6];
		RENDER_BOUNDS = new AxisAlignedBB[6];
		MASK = new ArrayList<EnumSet<PartSlot>>();
		FACING = new ArrayList<PartSlot>();
		for (int side = 0; side < 6; side++)
		{
			BOUNDS[side] = SprocketsMultiparts.rotateAxis(side, 0F / 16F, 15F / 16F, 0F / 16F, 16F / 16F, 16F / 16F, 16F / 16F);
			RENDER_BOUNDS[side] = SprocketsMultiparts.rotateAxis(side, -16F / 16F, 15F / 16F, -16F / 16F, 32F / 16F, 16F / 16F, 32F / 16F);
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
	public ResourceLocation getModelPath()
	{
		return null;
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
				RENDER_BOUNDS[facing].minX - parent.getX(),
				RENDER_BOUNDS[facing].minY - parent.getY(),
				RENDER_BOUNDS[facing].minZ - parent.getZ(),
				RENDER_BOUNDS[facing].maxX - parent.getX(),
				RENDER_BOUNDS[facing].maxY - parent.getY(),
				RENDER_BOUNDS[facing].maxZ - parent.getZ()
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
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setInteger("facing", facing);
		nbt.setInteger("x", parent.getX());
		nbt.setInteger("y", parent.getY());
		nbt.setInteger("z", parent.getZ());
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
				setMaterial(Material.wood);
				setHardness(LibConstants.MINE_TIME_WOODEN_SPROCKET * LibConstants.MINE_TIME_MULT_BIG_SPROCEKT);
				break;
			case 1:
				setMaterial(Material.rock);
				setHardness(LibConstants.MINE_TIME_STONE_SPROCKET * LibConstants.MINE_TIME_MULT_BIG_SPROCEKT);
				break;
			case 2:
				setMaterial(Material.iron);
				setHardness(LibConstants.MINE_TIME_IRON_SPROCKET * LibConstants.MINE_TIME_MULT_BIG_SPROCEKT);
				break;
		}
	}
	
	@Override
	public void onRemoved()
	{
		IMultipartContainer container = MultipartHelper.getPartContainer(getWorld(), getPos().subtract(parent));
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
