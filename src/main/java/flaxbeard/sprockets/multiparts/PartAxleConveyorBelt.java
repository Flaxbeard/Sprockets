package flaxbeard.sprockets.multiparts;

import java.util.ArrayList;
import java.util.List;

import mcmultipart.block.TileMultipartContainer;
import mcmultipart.multipart.IMultipartContainer;
import mcmultipart.multipart.MultipartHelper;
import mcmultipart.multipart.PartSlot;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;

public class PartAxleConveyorBelt extends PartAxleBelt
{	
	private class ConveyorBeltInventory implements IItemHandlerModifiable
	{
		private PartAxleConveyorBelt axle;
		
		private ConveyorBeltInventory(PartAxleConveyorBelt axle)
		{
			this.axle = axle;
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
				if (!axle.getWorld().isRemote)
				{
					BlockPos pos = axle.getPos();
					World world = axle.getWorld();
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

		BOUNDS = new AxisAlignedBB[12];
		
		BOUNDS[4] = new AxisAlignedBB(1.002F / 16F, 1F / 16F, 5.5F / 16F, 14.998F / 16F, 16F / 16F, 10.5F / 16F);
		BOUNDS[10] = new AxisAlignedBB(1.002F / 16F, 0F / 16F, 5.5F / 16F, 14.998F / 16F, 15F / 16F, 10.5F / 16F);
		
		BOUNDS[3] = new AxisAlignedBB(5.5F / 16F, 1F / 16F, 1.002F / 16F, 10.5F / 16F, 16F / 16F, 14.998F / 16F);
		BOUNDS[9] = new AxisAlignedBB(5.5F / 16F, 0F / 16F, 1.002F / 16F, 10.5F / 16F, 15F / 16F, 14.998F / 16F);

		BOUNDS[0] = new AxisAlignedBB(1F / 16F, 1.002F / 16F, 5.5F / 16F, 16F / 16F, 14.998F / 16F, 10.5F / 16F);
		BOUNDS[6] = new AxisAlignedBB(0F / 16F, 1.002F / 16F, 5.5F / 16F, 15F / 16F, 14.998F / 16F, 10.5F / 16F);

		BOUNDS[1] = new AxisAlignedBB(5.5F / 16F, 1.002F / 16F, 1F / 16F, 10.5F / 16F, 14.998F / 16F, 16F / 16F);
		BOUNDS[7] = new AxisAlignedBB(5.5F / 16F, 1.002F / 16F, 0F / 16F, 10.5F / 16F, 14.998F / 16F, 15F / 16F);

		BOUNDS[2] = new AxisAlignedBB(1F / 16F, 5.5F / 16F, 1.002F / 16F, 16F / 16F, 10.5F / 16F, 14.998F / 16F);
		BOUNDS[8] = new AxisAlignedBB(0F / 16F, 5.5F / 16F, 1.002F / 16F, 15F / 16F, 10.5F / 16F, 14.998F / 16F);

		BOUNDS[5] = new AxisAlignedBB(1.002F / 16F, 5.5F / 16F, 1F / 16F, 14.998F / 16F, 10.5F / 16F, 16F / 16F);
		BOUNDS[11] = new AxisAlignedBB(1.002F / 16F, 5.5F / 16F, 0F / 16F, 14.998F / 16F, 10.5F / 16F, 15F / 16F);
	}
	
	private ConveyorBeltInventory fakeInv = new ConveyorBeltInventory(this);

	
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
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && facing != EnumFacing.UP && meta > 1 && axis != 1)
		{
			return true;
		}
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && facing != EnumFacing.UP  && meta > 1 && axis != 1)
		{
			return (T) fakeInv;
		}
		return super.getCapability(capability, facing);
	}
	
	@Override
	public void update()
	{
		super.update();
		if (hasBelt == 1)
		{
			World world = getWorld();
			BlockPos pos = getPos();
			
			if (getNetwork() != null)
			{
				
				
				float absSpeed = getNetwork().getSpeedForConduit(this) * -1;
				if (this.getState())
				{
					absSpeed *= -1;
				}
				if (this.isNegativeDirection())
				{
					absSpeed *= -1;
				}
				
				int minorAxis = 0;
				switch (meta)
				{
					case 0:
						minorAxis = 1;
						absSpeed *= -1;
						break;
					case 1:
						minorAxis = 1;
						absSpeed *= -1;
						break;
					case 2:
						minorAxis = 2;
						absSpeed *= -1;
						break;
					case 5:
						minorAxis = 0;
						break;
					case 4:
						minorAxis = 0;
						absSpeed *= -1;
						break;
					case 3:
						minorAxis = 2;
						absSpeed *= -1;
						break;
				}
				
				float speed = absSpeed;
				
				if (minorAxis == 1) // "Wall" conveyors are less effective
				{
					speed *= 0.4F;
				}
				
				if (axis == 1)
				{
					speed *= 0.2F;
				}
				
				for (int neg = 0; neg < 2; neg++)
				{
					float upDownBottom = (neg == 0 ? 0.6F : 0.2F);
					float upDownTop = (neg == 0 ? 0.8F : 0.4F);
					AxisAlignedBB positive = new AxisAlignedBB(
							pos.getX() + 
								(axis == 0 ? 
									(1F / 16F)
								: minorAxis == 0 ?
									(1F / 16F)
								:
									upDownBottom), 
							pos.getY() + 
								(axis == 1 ? 
									(1F / 16F)
								: minorAxis == 1 ?
									(1F / 16F)
								:
									upDownBottom), 
							pos.getZ() + 
								(axis == 2 ? 
									(1F / 16F)
								: minorAxis == 2 ?
									(1F / 16F)
								:
									upDownBottom),
							pos.getX() + 
								(axis == 0 ? 
									(size + (15F / 16F))
								: minorAxis == 0 ?
									(15F / 16F)
								:
									upDownTop),
							pos.getY() + 
								(axis == 1 ? 
									(size + (15F / 16F))
								: minorAxis == 1 ?
									(15F / 16F)
								:
									upDownTop),
							pos.getZ() + 
								(axis == 2 ? 
									(size + (15F / 16F))
								: minorAxis == 2 ?
									(15F / 16F)
								:
									upDownTop));
					List<Entity> pentities = world.getEntitiesWithinAABB(Entity.class, positive);
					
					if (neg == 1)
					{
						speed *= -1;
					}
					
					for (Entity e : pentities)
					{
						switch (axis)
						{
							case 0:
								e.motionX = speed * 0.008F;
								break;
							case 1:
								e.motionY += speed * 0.006F;
								break;
							case 2:
								e.motionZ = speed * 0.008F;
								break;
						}
						
						
						if (e instanceof EntityItem && axis != 1)
						{
							switch (minorAxis)
							{
								case 0:
									if (e.posX > pos.getX() + 0.7F)
									{
										e.posX = pos.getX() + 0.7F;
									}
									
									if (e.posX < pos.getX() + 0.3F)
									{
										e.posX = pos.getX() + 0.3F;
									}
									//e.motionX = 0F;

									break;
								case 1:
								
									break;
								case 2:
									if (e.posZ > pos.getZ() + 0.7F)
									{
										e.posZ = pos.getZ() + 0.7F;
									}
									
									if (e.posZ < pos.getZ() + 0.3F)
									{
										e.posZ = pos.getZ() + 0.3F;
									}
									//e.motionZ = 0F;
									break;
							}
						}
					}
					
				}
				
				processInsert(world, pos, minorAxis, absSpeed);

			}
		}
	}

	private void processInsert(World world, BlockPos pos, int minorAxis, float absSpeed)
	{
		if (!world.isRemote && minorAxis != 1 && axis != 1)
		{
			if (absSpeed > 0)
			{
	
				TileEntity tileEntity = world.getTileEntity(target.add(axis == 0 ? 1 : 0, 0, axis == 0 ? 0 : 1));
				
				if (tileEntity == null || tileEntity instanceof TileMultipartContainer) return;
				
				EnumFacing f = axis == 0 ? EnumFacing.EAST : EnumFacing.SOUTH;
				if (!tileEntity.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, f)) return;
		        
				IItemHandler handler = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, f);
	
				
				AxisAlignedBB positive = new AxisAlignedBB(
						pos.getX() + 
							(axis == 0 ? 
								(size + (14F / 16F))
							: minorAxis == 0 ?
								(1F / 16F)
							:
								0.6F), 
						pos.getY() + 0.6F, 
						pos.getZ() + 
							(axis == 2 ? 
								(size + (14F / 16F))
							: minorAxis == 2 ?
								(1F / 16F)
							:
								0.6F),
						pos.getX() + 
							(axis == 0 ? 
								(size + (15F / 16F))
							: minorAxis == 0 ?
								(15F / 16F)
							:
								0.8F),
						pos.getY() + 0.8F,
						pos.getZ() + 
							(axis == 2 ? 
								(size + (15F / 16F))
							: minorAxis == 2 ?
								(15F / 16F)
							:
								0.8F));
				List<EntityItem> insertEntities = world.getEntitiesWithinAABB(EntityItem.class, positive);
				
				for (EntityItem item : insertEntities)
				{
					ItemStack stack = item.getEntityItem();
					ItemStack result = ItemHandlerHelper.insertItem(handler, stack, false);
					
					
					if (result == null || result.stackSize == 0)
					{
						item.setDead();
					}
					else if (!stack.areItemStacksEqual(stack, result))
					{
						item.setEntityItemStack(result);
					}
				}
			}
			else if (absSpeed < 0)
			{
				
				TileEntity tileEntity = world.getTileEntity(getPos().add(axis == 0 ? -1 : 0, 0, axis == 0 ? 0 : -1));
				
				if (tileEntity == null || tileEntity instanceof TileMultipartContainer) return;
				
				EnumFacing f = axis == 0 ? EnumFacing.WEST : EnumFacing.NORTH;
				if (!tileEntity.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, f)) return;
		        
				IItemHandler handler = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, f);
	
				
				AxisAlignedBB positive = new AxisAlignedBB(
						pos.getX() + 
							(axis == 0 ? 
								((1F / 16F))
							: minorAxis == 0 ?
								(1F / 16F)
							:
								0.6F), 
						pos.getY() + 0.6F, 
						pos.getZ() + 
							(axis == 2 ? 
								((1F / 16F))
							: minorAxis == 2 ?
								(1F / 16F)
							:
								0.6F),
						pos.getX() + 
							(axis == 0 ? 
								((2F / 16F))
							: minorAxis == 0 ?
								(15F / 16F)
							:
								0.8F),
						pos.getY() + 0.8F,
						pos.getZ() + 
							(axis == 2 ? 
								((2F / 16F))
							: minorAxis == 2 ?
								(15F / 16F)
							:
								0.8F));
				List<EntityItem> insertEntities = world.getEntitiesWithinAABB(EntityItem.class, positive);
				
				for (EntityItem item : insertEntities)
				{
					ItemStack stack = item.getEntityItem();
					ItemStack result = ItemHandlerHelper.insertItem(handler, stack, false);
					
					
					if (result == null || result.stackSize == 0)
					{
						item.setDead();
					}
					else if (!stack.areItemStacksEqual(stack, result))
					{
						item.setEntityItemStack(result);
					}
				}
				
				
			}
		}
	}
	
	@Override
	public List<ItemStack> getDrops()
	{
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		drops.add(new ItemStack(SprocketsMultiparts.axle, 1, damage));
		
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
		drops.add(new ItemStack(SprocketsMultiparts.conveyorBelt, dropSize, beltDamage));
		
		return drops;
	}
}
