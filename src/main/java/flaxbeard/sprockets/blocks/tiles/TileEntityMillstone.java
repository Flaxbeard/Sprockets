package flaxbeard.sprockets.blocks.tiles;

import java.util.HashSet;
import java.util.List;

import mcmultipart.multipart.PartSlot;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RangedWrapper;
import flaxbeard.sprockets.api.IMechanicalConsumer;
import flaxbeard.sprockets.api.IMultiblockBrain;
import flaxbeard.sprockets.api.Multiblock;
import flaxbeard.sprockets.api.SprocketsAPI;
import flaxbeard.sprockets.api.network.MechanicalNetwork;
import flaxbeard.sprockets.api.tool.IGyrometerable;
import flaxbeard.sprockets.blocks.BlockMillstone;
import flaxbeard.sprockets.blocks.SprocketsBlocks;
import flaxbeard.sprockets.common.SprocketsRecipes;
import flaxbeard.sprockets.lib.LibConstants;


public class TileEntityMillstone extends TileEntitySprocketBase implements IMechanicalConsumer, IGyrometerable, IMultiblockBrain
{
	private static final HashSet<Tuple<Vec3i, PartSlot>> CISMP = new HashSet<Tuple<Vec3i, PartSlot>>();
	private static final HashSet<Vec3i> CIS = new HashSet<Vec3i>();
		
	private boolean isActive = false;
	private boolean wasActive = false;
	private float activeTicks = 0;
	
	public final ItemStackHandler slots = new ItemStackHandler(2);
	private final RangedWrapper slotsSides = new RangedWrapper(slots, 0, 1);
	private final RangedWrapper slotsBottom = new RangedWrapper(slots, 1, 2);

	static
	{
		CIS.add(new Vec3i(0, 1, 0));
		CISMP.add(new Tuple(new Vec3i(0, 1, 0), PartSlot.DOWN));
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{
			return true;
		}
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{
			if (facing == EnumFacing.DOWN)
			{
				return (T) slotsBottom;
			}
			else
			{
				return (T) slotsSides;
			}
		}
		return super.getCapability(capability, facing);
	}
	
	@Override
	public void update()
	{
		super.update();
		
		if (isMultiblock)
		{
			if (worldObj.isRemote && isActive && getNetwork() != null && !getNetwork().isJammed() && Math.abs(getNetwork().getSpeedForConduit(this)) > 0)
			{
				for (int i = 0; i < 10; i++)
					worldObj.spawnParticle(EnumParticleTypes.ITEM_CRACK, 
							getPosMC().getX() + .5F + 2.5F * (getWorldMC().rand.nextFloat() -.5F), 
							getPosMC().getY() + .5F, 
							getPosMC().getZ() + .5F + 2.5F  * (getWorldMC().rand.nextFloat() -.5F), 
							(getWorldMC().rand.nextFloat() -.5F) * 0.3F, 
							0, 
							(getWorldMC().rand.nextFloat() -.5F) * 0.3F, 
							new int[] { Item.getIdFromItem(this.slots.getStackInSlot(0).getItem()) } );
			}
			
			if (worldObj.isRemote && getNetwork() != null && !getNetwork().isJammed() && Math.abs(getNetwork().getSpeedForConduit(this)) > 0)
			{
				if (worldObj.getTotalWorldTime() % 60 == 0)
					worldObj.playSound(
							getPosMC().getX() + .5F, 
							getPosMC().getY() + .5F, 
							getPosMC().getZ() + .5F, 
							SoundEvents.WEATHER_RAIN, SoundCategory.BLOCKS, 0.3f, 0.1f, false);
			}

		}
		else
		{
			if (worldObj.isRemote && isActive && getNetwork() != null && !getNetwork().isJammed() && Math.abs(getNetwork().getSpeedForConduit(this)) > 0)
			{
				for (int i = 0; i < 3; i++)
					worldObj.spawnParticle(EnumParticleTypes.ITEM_CRACK, 
							getPosMC().getX() + .5F, 
							getPosMC().getY() + .5F, 
							getPosMC().getZ() + .5F, 
							(getWorldMC().rand.nextFloat() -.5F) * 0.3F, 
							(getWorldMC().rand.nextFloat()) * 0.1F, 
							(getWorldMC().rand.nextFloat() -.5F) * 0.3F, 
							new int[] { Item.getIdFromItem(this.slots.getStackInSlot(0).getItem()) } );
				
				if (worldObj.getTotalWorldTime() % 60 == 0)
					worldObj.playSound(
							getPosMC().getX() + .5F, 
							getPosMC().getY() + .5F, 
							getPosMC().getZ() + .5F, 
							SoundEvents.WEATHER_RAIN, SoundCategory.BLOCKS, 0.3f, 0.1f, false);
			}
		}


		if (this.getNetwork() != null && !worldObj.isRemote)
		{
			MechanicalNetwork network = this.getNetwork();

			isActive = !isEmpty(slots.getStackInSlot(0)) && isItemGrindable(this.slots.getStackInSlot(0)) && (isEmpty(slots.getStackInSlot(1)) || (canFit(slots.getStackInSlot(1), SprocketsAPI.getMillstoneResult(this.slots.getStackInSlot(0)))));
			if (isActive != wasActive)
			{
				network.updateNetworkSpeedAndTorque();
				this.markDirty();
				worldObj.notifyBlockUpdate(getPosMC(), worldObj.getBlockState(getPosMC()), worldObj.getBlockState(getPosMC()), 2);
				wasActive = isActive;
			}

	
			if (!network.isJammed() && isActive && Math.abs(network.getSpeedForConduit(this)) > LibConstants.MILLSTONE_MIN_SPEED)
			{
				activeTicks += Math.abs(network.getSpeedForConduit(this));
				ItemStack output = SprocketsAPI.getMillstoneResult(slots.getStackInSlot(0));

				if (output != null && activeTicks >= (isMultiblock ? 20 : 200))
				{
					activeTicks = 0;
					
					ItemStack temp = slots.getStackInSlot(0).copy();
					temp.stackSize--;
					if (temp.stackSize == 0)
					{
						temp = null;
					}
					slots.setStackInSlot(0, temp);
					
					
					if (slots.getStackInSlot(1) != null)
					{
						output.stackSize += slots.getStackInSlot(1).stackSize;
					}
					slots.setStackInSlot(1, output);
				}
			}
			
			
			
		}
	}
	
	private boolean isEmpty(ItemStack stackInSlot)
	{
		return stackInSlot == null || stackInSlot.stackSize == 0;
	}

	private static boolean isItemGrindable(ItemStack stack)
	{
		return SprocketsAPI.getMillstoneResult(stack) != null && stack.stackSize > 0;
	}
	
	private static boolean canFit(ItemStack outputStack, ItemStack output)
	{
		if (outputStack.stackSize - output.stackSize > output.getMaxStackSize())
		{
			return false;
		}
		else
		{
			return outputStack.getItem().equals(output.getItem()) && outputStack.getItemDamage() == output.getItemDamage();
		}
	}
	
	@Override
	public HashSet<Tuple<Vec3i, PartSlot>> multipartCisConnections()
	{
		return CISMP;
	}

	@Override
	public HashSet<Tuple<Vec3i, PartSlot>> multipartTransConnections()
	{
		return new HashSet<Tuple<Vec3i, PartSlot>>();
	}

	@Override
	public HashSet<Vec3i> cisConnections()
	{
		return CIS;
	}

	@Override
	public HashSet<Vec3i> transConnections()
	{
		return new HashSet<Vec3i>();
	}

	@Override
	public boolean isNegativeDirection()
	{
		return false;
	}

	@Override
	public float torqueCost()
	{
		if (isMultiblock)
		{
			return LibConstants.MILLSTONE_TORQUE;
		}
		else
		{
			return isActive ? LibConstants.MILLSTONE_TORQUE : 0.0F;
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		
		slots.deserializeNBT(compound.getCompoundTag("inv"));
		
		isActive = compound.getBoolean("isActive");
		if (isActive != wasActive && this.getNetwork() != null)
		{
			this.getNetwork().updateNetworkSpeedAndTorque();
		}
		wasActive = isActive;
		//wasActive = compound.getBoolean("wasActive");
		activeTicks = compound.getFloat("activeTicks");
		
		isMultiblock = compound.getBoolean("isMultiblock");
		swapXZ = compound.getBoolean("swapXZ");
		flipX = compound.getBoolean("flipX");
		flipZ = compound.getBoolean("flipZ");

	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		compound = super.writeToNBT(compound);
		NBTTagCompound item1 = new NBTTagCompound();
		
		compound.setTag("inv", this.slots.serializeNBT());
		
		compound.setBoolean("isActive", isActive);
		compound.setBoolean("wasActive", wasActive);
		compound.setFloat("activeTicks", activeTicks);
		
		compound.setBoolean("isMultiblock", isMultiblock);
		compound.setBoolean("swapXZ", swapXZ);
		compound.setBoolean("flipX", flipX);
		compound.setBoolean("flipZ", flipZ);

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
	public void addInfo(List<ITextComponent> list)
	{
		if (getNetwork() != null && getNetwork().getTorque() < this.torqueCost())
		{
			
		}
	}
	
	private boolean swapXZ;
	private boolean flipX;
	private boolean flipZ;
	public boolean isMultiblock = false;

	@Override
	public void addMultiblock(Multiblock mb, boolean swapXZ, boolean flipX,
			boolean flipZ)
	{
		this.swapXZ = swapXZ;
		this.flipX = flipX;
		this.flipZ = flipZ;
		this.isMultiblock = true;
		this.worldObj.setBlockState(getPos(), worldObj.getBlockState(getPos()).withProperty(BlockMillstone.MULTIBLOCK, true), 2);
	}

	@Override
	public void invalidate()
	{
		super.invalidate();
		
		if (isMultiblock)
		{
			destroy();
		}
	}

	@Override
	public void destroy()
	{
		if (worldObj.getBlockState(getPos()).getBlock() == SprocketsBlocks.millstone)
		{
			this.worldObj.setBlockState(getPos(), worldObj.getBlockState(getPos()).withProperty(BlockMillstone.MULTIBLOCK, false), 2);
		}
		SprocketsRecipes.BIGMILLSTONE.destroyMultiblock(worldObj, getPos(), swapXZ, flipX, flipZ);
		isMultiblock = false;
	}
	
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
	{
		return oldState.getBlock() != newState.getBlock();
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing, int loc)
	{
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && (loc >= 0 && loc < 18))
		{
			return true;
		}
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing, int loc)
	{
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{
			if (facing == EnumFacing.DOWN && (loc >= 0 && loc < 9))
			{
				return (T) slotsBottom;
			}
			else if (loc >= 9 && loc < 18)
			{
				return (T) slotsSides;
			}
		}
		return super.getCapability(capability, facing);
	}
}
