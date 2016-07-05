package flaxbeard.sprockets.multiparts;

import java.util.ArrayList;
import java.util.List;

import flaxbeard.sprockets.lib.LibConstants;
import mcmultipart.multipart.IMultipart;
import mcmultipart.multipart.IMultipartContainer;
import mcmultipart.multipart.MultipartHelper;
import mcmultipart.multipart.PartSlot;
import mcmultipart.raytrace.PartMOP;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

public class PartConveyorBelt extends PartBelt
{
	private class ConveyorBeltInventory implements IItemHandlerModifiable
	{
		private PartConveyorBelt belt;
		
		private ConveyorBeltInventory(PartConveyorBelt belt)
		{
			this.belt = belt;
		}
		
		@Override
		public int getSlots()
		{
			return 1;
		}

		@Override
		public ItemStack getStackInSlot(int slot)
		{
			return null;
		}

		@Override
		public ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
		{
			if (!simulate)
			{
				if (!belt.getWorld().isRemote)
				{
					BlockPos pos = belt.getPos();
					World world = belt.getWorld();
					EntityItem ei = new EntityItem(world, pos.getX() + .5F, pos.getY() + .8F, pos.getZ() + .5F, stack.copy());
					ei.motionX = 0;
					ei.motionY = 0;
					ei.motionZ = 0;
					world.spawnEntityInWorld(ei);
					ei.motionX = 0;
					ei.motionY = 0;
					ei.motionZ = 0;
				}
			}
			return null;
		}

		@Override
		public ItemStack extractItem(int slot, int amount, boolean simulate)
		{
			return null;
		}

		@Override
		public void setStackInSlot(int slot, ItemStack stack)
		{
		}
		
	}
	
	private static final AxisAlignedBB[] BOUNDS;
		
	static
	{
		BOUNDS = new AxisAlignedBB[6];
		for (int side = 0; side < 6; side++)
		{
			if (side % 2 == 0)
			{
				BOUNDS[side] = SprocketsMultiparts.rotateAxis(side, 0F / 16F, 1F / 16F, 5.5F / 16F, 16F / 16F, 15F / 16F, 10.5F / 16F);
			
			}
			else
			{
				BOUNDS[side] = SprocketsMultiparts.rotateAxis(side, 5.5F / 16F, 1F / 16F, 0F / 16F, 10.5F / 16F, 15F / 16F, 16F / 16F);

			}
			
		}				
	}
	
	private ConveyorBeltInventory fakeInv = new ConveyorBeltInventory(this);
	
	public PartConveyorBelt()
	{
		setMaterial(Material.CLOTH);
		setHardness(LibConstants.MINE_TIME_BELT);
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && facing != EnumFacing.UP && (this.facing == 2 || this.facing == 5))
		{
			return true;
		}
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && facing != EnumFacing.UP  && (this.facing == 2 || this.facing == 5))
		{
			return (T) fakeInv;
		}
		return super.getCapability(capability, facing);
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
		return new ItemStack(SprocketsMultiparts.conveyorBelt, 1, damage);
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
		drops.add(new ItemStack(SprocketsMultiparts.conveyorBelt, size, damage));
		return drops;
	}
	
}