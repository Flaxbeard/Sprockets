package flaxbeard.sprockets.multiparts;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;

import mcmultipart.MCMultiPartMod;
import mcmultipart.multipart.IMultipartContainer;
import mcmultipart.multipart.MultipartHelper;
import mcmultipart.multipart.PartSlot;
import mcmultipart.raytrace.PartMOP;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

public class PartAxleBelt extends PartAxle
{
	public static final PropertyInteger DIR = PropertyInteger.create("dir", 0, 12);
	private static final ArrayList<EnumSet<PartSlot>> MASKS;
	private static final AxisAlignedBB[] BOUNDS;
	private static final AxisAlignedBB[] SELECTION_BOUNDS;

	//private static final AxisAlignedBB[] BOUNDS_TOP;
	//private static final AxisAlignedBB[] BOUNDS_BOTTOM;
	//private static final AxisAlignedBB[] BOUNDS_TOP_BOTTOM;
	
	static
	{
		MASKS = new ArrayList<EnumSet<PartSlot>>();
		MASKS.add(EnumSet.of(PartSlot.CENTER, PartSlot.EAST)); // 0 EAST
		MASKS.add(EnumSet.of(PartSlot.CENTER, PartSlot.SOUTH)); // 1 SOUTH
		MASKS.add(EnumSet.of(PartSlot.CENTER, PartSlot.EAST)); // 2 EAST
		MASKS.add(EnumSet.of(PartSlot.CENTER, PartSlot.UP)); // 3 UP
		MASKS.add(EnumSet.of(PartSlot.CENTER, PartSlot.UP)); // 4 UP
		MASKS.add(EnumSet.of(PartSlot.CENTER, PartSlot.SOUTH)); // 5 SOUTH
		MASKS.add(EnumSet.of(PartSlot.CENTER, PartSlot.WEST)); // 6 WEST
		MASKS.add(EnumSet.of(PartSlot.CENTER, PartSlot.NORTH)); // 7 NORTH
		MASKS.add(EnumSet.of(PartSlot.CENTER, PartSlot.WEST)); // 8 WEST
		MASKS.add(EnumSet.of(PartSlot.CENTER, PartSlot.DOWN)); // 9 DOWN
		MASKS.add(EnumSet.of(PartSlot.CENTER, PartSlot.DOWN)); // 10 DOWN
		MASKS.add(EnumSet.of(PartSlot.CENTER, PartSlot.NORTH)); // 11 NORTH

		BOUNDS = new AxisAlignedBB[12];
		
		BOUNDS[4] = new AxisAlignedBB(1.002F / 16F, 5.5F / 16F, 5.5F / 16F, 14.998F / 16F, 16F / 16F, 10.5F / 16F);
		BOUNDS[10] = new AxisAlignedBB(1.002F / 16F, 0F / 16F, 5.5F / 16F, 14.998F / 16F, 10.5F / 16F, 10.5F / 16F);
		
		BOUNDS[3] = new AxisAlignedBB(5.5F / 16F, 5.5F / 16F, 1.002F / 16F, 10.5F / 16F, 16F / 16F, 14.998F / 16F);
		BOUNDS[9] = new AxisAlignedBB(5.5F / 16F, 0F / 16F, 1.002F / 16F, 10.5F / 16F, 10.5F / 16F, 14.998F / 16F);

		BOUNDS[0] = new AxisAlignedBB(5.5F / 16F, 1.002F / 16F, 5.5F / 16F, 16F / 16F, 14.998F / 16F, 10.5F / 16F);
		BOUNDS[6] = new AxisAlignedBB(0F / 16F, 1.002F / 16F, 5.5F / 16F, 10.5F / 16F, 14.998F / 16F, 10.5F / 16F);

		BOUNDS[1] = new AxisAlignedBB(5.5F / 16F, 1.002F / 16F, 5.5F / 16F, 10.5F / 16F, 14.998F / 16F, 16F / 16F);
		BOUNDS[7] = new AxisAlignedBB(5.5F / 16F, 1.002F / 16F, 0F / 16F, 10.5F / 16F, 14.998F / 16F, 10.5F / 16F);

		BOUNDS[2] = new AxisAlignedBB(5.5F / 16F, 5.5F / 16F, 1.002F / 16F, 16F / 16F, 10.5F / 16F, 14.998F / 16F);
		BOUNDS[8] = new AxisAlignedBB(0F / 16F, 5.5F / 16F, 1.002F / 16F, 10.5F / 16F, 10.5F / 16F, 14.998F / 16F);

		BOUNDS[5] = new AxisAlignedBB(1.002F / 16F, 5.5F / 16F, 5.5F / 16F, 14.998F / 16F, 10.5F / 16F, 16F / 16F);
		BOUNDS[11] = new AxisAlignedBB(1.002F / 16F, 5.5F / 16F, 0F / 16F, 14.998F / 16F, 10.5F / 16F, 10.5F / 16F);
		
		SELECTION_BOUNDS = new AxisAlignedBB[12];
		
		SELECTION_BOUNDS[4] = new AxisAlignedBB(0.002F / 16F, 5.5F / 16F, 5.5F / 16F, 15.998F / 16F, 16F / 16F, 10.5F / 16F);
		SELECTION_BOUNDS[10] = new AxisAlignedBB(0.002F / 16F, 0F / 16F, 5.5F / 16F, 15.998F / 16F, 10.5F / 16F, 10.5F / 16F);
		
		SELECTION_BOUNDS[3] = new AxisAlignedBB(5.5F / 16F, 5.5F / 16F, 0.002F / 16F, 10.5F / 16F, 16F / 16F, 15.998F / 16F);
		SELECTION_BOUNDS[9] = new AxisAlignedBB(5.5F / 16F, 0F / 16F, 0.002F / 16F, 10.5F / 16F, 10.5F / 16F, 15.998F / 16F);

		SELECTION_BOUNDS[0] = new AxisAlignedBB(5.5F / 16F, 0.002F / 16F, 5.5F / 16F, 16F / 16F, 15.998F / 16F, 10.5F / 16F);
		SELECTION_BOUNDS[6] = new AxisAlignedBB(0F / 16F, 0.002F / 16F, 5.5F / 16F, 10.5F / 16F, 15.998F / 16F, 10.5F / 16F);

		SELECTION_BOUNDS[1] = new AxisAlignedBB(5.5F / 16F, 0.002F / 16F, 5.5F / 16F, 10.5F / 16F, 15.998F / 16F, 16F / 16F);
		SELECTION_BOUNDS[7] = new AxisAlignedBB(5.5F / 16F, 0.002F / 16F, 0F / 16F, 10.5F / 16F, 15.998F / 16F, 10.5F / 16F);

		SELECTION_BOUNDS[2] = new AxisAlignedBB(5.5F / 16F, 5.5F / 16F, 0.002F / 16F, 16F / 16F, 10.5F / 16F, 15.998F / 16F);
		SELECTION_BOUNDS[8] = new AxisAlignedBB(0F / 16F, 5.5F / 16F, 0.002F / 16F, 10.5F / 16F, 10.5F / 16F, 15.998F / 16F);

		SELECTION_BOUNDS[5] = new AxisAlignedBB(0.002F / 16F, 5.5F / 16F, 5.5F / 16F, 15.998F / 16F, 10.5F / 16F, 16F / 16F);
		SELECTION_BOUNDS[11] = new AxisAlignedBB(0.002F / 16F, 5.5F / 16F, 0F / 16F, 15.998F / 16F, 10.5F / 16F, 10.5F / 16F);

	}
	
	@Override
	public void addSelectionBoxes(List<AxisAlignedBB> list)
	{
		
		list.add(SELECTION_BOUNDS[hasBelt == 1 ? meta : meta + 6]);
	}
	
	@Override
	public void addCollisionBoxes(AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity)
	{
		super.addCollisionBoxes(mask, list, collidingEntity);

		AxisAlignedBB bounds = BOUNDS[hasBelt == 1 ? meta : meta + 6];
		if (bounds.intersectsWith(mask))
		{
			list.add(bounds);
		}
	}
	
	@Override
	public EnumSet<PartSlot> getSlotMask()
	{	
		if (hasBelt == 0)
		{
			return super.getSlotMask();
		}
		else
		{
			return MASKS.get(hasBelt == 1 ? meta : meta + 6).clone();
		}
	}
	
	@Override
	public List<ItemStack> getDrops()
	{
		List<ItemStack> drops = super.getDrops();
		
		int dropSize = 0;
		if (hasBelt == 1)
		{
			dropSize = size + 1;
		}
		else
		{
			IMultipartContainer c = MultipartHelper.getPartContainer(getWorld(), target);
			if (c != null && c.getPartInSlot(PartSlot.CENTER) != null && c.getPartInSlot(PartSlot.CENTER) instanceof PartAxleBelt)
			{
				dropSize = ((PartAxleBelt) c.getPartInSlot(PartSlot.CENTER)).size + 1;
			}
		}		
		drops.add(new ItemStack(SprocketsMultiparts.belt, dropSize, beltDamage));
		return drops;
	}
	
	
	@Override
	public boolean onActivated(EntityPlayer player, EnumHand hand, ItemStack heldItem, PartMOP hit)
	{
		System.out.println(hasBelt == 1 ? (int) meta : (int) meta + 6);
		return false;
	}
	
	@Override
	public IBlockState getActualState(IBlockState state)
	{
		return state.withProperty(DIR, hasBelt == 0 ? 12 : (hasBelt == 1 ? (int) meta : (int) meta + 6));
	}
	
	@Override
	public BlockStateContainer createBlockState()
	{
		return new ExtendedBlockState(MCMultiPartMod.multipart, new IProperty[] {DIR}, new IUnlistedProperty[0] );
	}
	
	public byte hasBelt = 0;
	protected int size;
	protected int axis;
	protected BlockPos target;
	protected byte meta = 0;
	public boolean isRemovingBelt = false;
	protected int beltDamage = 0;

	
	@Override
	public HashSet<Tuple<Vec3i, PartSlot>> multipartCisConnections()
	{
		if (hasBelt == 0)
		{
			return super.multipartCisConnections();
		}
		if (target == null)
		{
			System.out.println("R E " + getWorld().isRemote);
		}
		HashSet<Tuple<Vec3i, PartSlot>> list = (HashSet<Tuple<Vec3i, PartSlot>>) super.multipartCisConnections().clone();
		list.add(new Tuple(target.subtract(getPos()), PartSlot.CENTER));
		return list;
	}
	
	private static final ResourceLocation r = new ResourceLocation("sprockets:beltAxle");
	@Override
	public ResourceLocation getModelPath()
	{

		return super.getModelPath();
	}
	
	public void setBeltChild(BlockPos target, int meta, int beltDamage)
	{

		hasBelt = 2;
		this.target = target;
		
		this.meta = (byte) meta;
		this.beltDamage = beltDamage;
	}
	
	public void setBeltBrain(BlockPos target, int size, int axis, int meta, int beltDamage)
	{

		hasBelt = 1;
		this.size = size;
		this.axis = axis;
		this.target = target;
		this.meta = (byte) meta;
		this.beltDamage = beltDamage;
	}
	
	public void removeBelt(BlockPos pos)
	{
		isRemovingBelt = true;
		for (int n = 1; n < size; n++)
		{
			BlockPos remove = pos.add(axis == 0 ? n : 0,
					axis == 1 ? n : 0,
					axis == 2 ? n : 0);
			
			IMultipartContainer c = MultipartHelper.getPartContainer(getWorld(), remove);
			if (!getWorld().isRemote && c != null && c.getPartInSlot(PartSlot.CENTER) != null && c.getPartInSlot(PartSlot.CENTER) instanceof PartBelt)
			{
				PartBelt belt = (PartBelt) c.getPartInSlot(PartSlot.CENTER);

				c.removePart(belt);
				
			}
		}
		
		BlockPos remove = pos.add(axis == 0 ? size : 0,
				axis == 1 ? size : 0,
				axis == 2 ? size : 0);
		IMultipartContainer c = MultipartHelper.getPartContainer(getWorld(), remove);
		
		
		if (c != null && c.getPartInSlot(PartSlot.CENTER) != null && c.getPartInSlot(PartSlot.CENTER) instanceof PartAxle)
		{		
			if (!getWorld().isRemote)
			{
				PartAxle ax = ((PartAxle) c.getPartInSlot(PartSlot.CENTER));
	
				PartAxle newAxle = new PartAxle();
				newAxle.setFacing(ax.facing);
				newAxle.setDamage(ax.getDamage());
				c.removePart(ax);
				
				c.addPart(newAxle);
			}
		}

		if (!removing)
		{
			if (!getWorld().isRemote)
			{
				IMultipartContainer mc = MultipartHelper.getPartContainer(getWorld(), pos);
	
				PartAxle newAxle = new PartAxle();
				newAxle.setFacing(facing);
				newAxle.setDamage(getDamage());
				mc.removePart(this);
				mc.addPart(newAxle);
				return;
				
			}
		}
	
		
	}
	
	boolean removing = false;
	
	@Override
	public void onRemoved()
	{
		super.onRemoved();

		if (hasBelt == 1)
		{
			removing = true;
			removeBelt(getPos());

		}
		else if (hasBelt == 2)
		{
			IMultipartContainer c = MultipartHelper.getPartContainer(getWorld(), target);
			if (c != null && c.getPartInSlot(PartSlot.CENTER) != null && c.getPartInSlot(PartSlot.CENTER) instanceof PartAxleBelt)
			{
				((PartAxleBelt) c.getPartInSlot(PartSlot.CENTER)).removeBelt(target);
			}

		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		hasBelt = nbt.getByte("hasBelt");
		meta = nbt.getByte("meta");
		size = nbt.getInteger("size");
		axis = nbt.getInteger("dimension");
		beltDamage = nbt.getInteger("beltDamage");
		if (hasBelt != 0)
		{
			int x = nbt.getInteger("targetX");
			int y = nbt.getInteger("targetY");
			int z = nbt.getInteger("targetZ");
			target = new BlockPos(x, y, z);
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt = super.writeToNBT(nbt);
		nbt.setByte("hasBelt", hasBelt);
		nbt.setByte("meta", meta);

		nbt.setInteger("size", size);
		nbt.setInteger("dimension", axis);
		nbt.setInteger("beltDamage", beltDamage);
		if (hasBelt != 0)
		{
			nbt.setInteger("targetX", target.getX());
			nbt.setInteger("targetY", target.getY());
			nbt.setInteger("targetZ", target.getZ());

		}
		return nbt;
	}
	
	@Override
	public void writeUpdatePacket(PacketBuffer buf)
	{
		super.writeUpdatePacket(buf);
		buf.writeByte(hasBelt);
		buf.writeByte(meta);
		buf.writeInt(size);
		buf.writeInt(axis);
		buf.writeInt(beltDamage);
		if (hasBelt != 0)
		{
			buf.writeInt(target.getX());
			buf.writeInt(target.getY());
			buf.writeInt(target.getZ());
		}
	}
	

	@Override
	public void readUpdatePacket(PacketBuffer buf)
	{
		super.readUpdatePacket(buf);
		hasBelt = buf.readByte();
		meta = buf.readByte();
		size = buf.readInt();
		axis = buf.readInt();
		beltDamage = buf.readInt();
		if (hasBelt != 0)
		{
			int x = buf.readInt();
			int y = buf.readInt();
			int z = buf.readInt();
			target = new BlockPos(x, y, z);
		}
	}
}
