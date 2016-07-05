package flaxbeard.sprockets.multiparts;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import mcmultipart.MCMultiPartMod;
import mcmultipart.multipart.IMultipart;
import mcmultipart.multipart.IMultipartContainer;
import mcmultipart.multipart.ISlottedPart;
import mcmultipart.multipart.MultipartHelper;
import mcmultipart.multipart.PartSlot;
import mcmultipart.raytrace.PartMOP;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import flaxbeard.sprockets.lib.LibConstants;

public class PartBelt extends PartSprocketBaseNoConduit implements ISlottedPart, ITickable
{
	public static final PropertyInteger DIR = PropertyInteger.create("dir", 0, 6);

	public int facing = 0;
	
	private static final ArrayList<EnumSet<PartSlot>> MASK;
	private static final AxisAlignedBB[] BOUNDS;
	private static final ArrayList<PartSlot> FACING;
	
	protected BlockPos parent;
	
	static
	{
		BOUNDS = new AxisAlignedBB[6];
		MASK = new ArrayList<EnumSet<PartSlot>>();
		FACING = new ArrayList<PartSlot>();
		for (int side = 0; side < 6; side++)
		{
			if (side % 2 == 0)
			{
				BOUNDS[side] = SprocketsMultiparts.rotateAxis(side, 0F / 16F, 7F / 16F, 5.5F / 16F, 16F / 16F, 9F / 16F, 10.5F / 16F);
			
				MASK.add(EnumSet.of(
						SprocketsMultiparts.rotatePartSlot(side, PartSlot.CENTER),
						SprocketsMultiparts.rotatePartSlot(side, PartSlot.EAST),
						SprocketsMultiparts.rotatePartSlot(side, PartSlot.WEST)
						));
			}
			else
			{
				BOUNDS[side] = SprocketsMultiparts.rotateAxis(side, 5.5F / 16F, 7F / 16F, 0F / 16F, 10.5F / 16F, 9F / 16F, 16F / 16F);

				MASK.add(EnumSet.of(
						SprocketsMultiparts.rotatePartSlot(side, PartSlot.CENTER),
						SprocketsMultiparts.rotatePartSlot(side, PartSlot.NORTH),
						SprocketsMultiparts.rotatePartSlot(side, PartSlot.SOUTH)
						));
			}
			
			FACING.add(SprocketsMultiparts.rotatePartSlot(side, PartSlot.UP));
		}				
	}
	
	public PartBelt()
	{
		setMaterial(Material.CLOTH);
		setHardness(LibConstants.MINE_TIME_BELT);
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
	public ItemStack getPickBlock(EntityPlayer player, PartMOP hit)
	{
		return new ItemStack(SprocketsMultiparts.belt, 1, damage);
	}
	

	@Override
	public List<ItemStack> getDrops()
	{
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		IMultipartContainer container = MultipartHelper.getPartContainer(getWorld(), parent);
		int size = 1;
		if (container != null)
		{
			IMultipart part = container.getPartInSlot(PartSlot.CENTER);
			if (part != null && part instanceof PartAxleBelt)
			{
				size = ((PartAxleBelt) part).size + 1;
			}
		}
		drops.add(new ItemStack(SprocketsMultiparts.belt, size, damage));
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

		IMultipartContainer container = MultipartHelper.getPartContainer(getWorld(), parent);
		if (container != null)
		{
			IMultipart part = container.getPartInSlot(PartSlot.CENTER);
			if (part != null && part instanceof PartAxleBelt && !((PartAxleBelt) part).isRemovingBelt)
			{
				((PartAxleBelt) part).removeBelt(parent);
			}
		}

	}
	
	@Override
	public IBlockState getActualState(IBlockState state)
	{
		return state.withProperty(DIR, facing);
	}
	
	@Override
	public BlockStateContainer createBlockState()
	{
		return new ExtendedBlockState(MCMultiPartMod.multipart, new IProperty[] {DIR}, new IUnlistedProperty[0] );
	}

	@Override
	public void update()
	{
		// TODO Auto-generated method stub
		
	}
}