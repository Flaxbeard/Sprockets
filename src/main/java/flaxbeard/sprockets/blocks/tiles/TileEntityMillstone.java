package flaxbeard.sprockets.blocks.tiles;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import mcmultipart.multipart.PartSlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.ITextComponent;
import flaxbeard.sprockets.api.IMechanicalConsumer;
import flaxbeard.sprockets.api.SprocketsAPI;
import flaxbeard.sprockets.api.network.MechanicalNetwork;
import flaxbeard.sprockets.api.tool.IGyrometerable;
import flaxbeard.sprockets.lib.LibConstants;


public class TileEntityMillstone extends TileEntitySprocketBase implements IMechanicalConsumer, ISidedInventory, IGyrometerable
{
	private static final HashSet<Tuple<Vec3i, PartSlot>> CISMP = new HashSet<Tuple<Vec3i, PartSlot>>();
	private static final HashSet<Vec3i> CIS = new HashSet<Vec3i>();
	
	private ItemStack[] contents = new ItemStack[2];
	
	private boolean isActive = false;
	private boolean wasActive = false;
	private float activeTicks = 0;
	
	static
	{
		CIS.add(new Vec3i(0, 1, 0));
		CISMP.add(new Tuple(new Vec3i(0, 1, 0), PartSlot.DOWN));
	}
	
	@Override
	public void update()
	{
		super.update();
		if (worldObj.isRemote && isActive && this.contents[0] != null && getNetwork() != null && !getNetwork().isJammed())
		{
			for (int i = 0; i < 3; i++)
				worldObj.spawnParticle(EnumParticleTypes.ITEM_CRACK, 
						getPosMC().getX() + .5F, 
						getPosMC().getY() + .5F, 
						getPosMC().getZ() + .5F, 
						(getWorldMC().rand.nextFloat() -.5F) * 0.3F, 
						(getWorldMC().rand.nextFloat()) * 0.1F, 
						(getWorldMC().rand.nextFloat() -.5F) * 0.3F, 
						new int[] { Item.getIdFromItem(this.contents[0].getItem()) } );
			if (worldObj.getTotalWorldTime() % 60 == 0)
				worldObj.playSound(
						getPosMC().getX() + .5F, 
						getPosMC().getY() + .5F, 
						getPosMC().getZ() + .5F, 
						SoundEvents.weather_rain, SoundCategory.BLOCKS, 0.3f, 0.1f, false);
		}
		
		if (this.getNetwork() != null && !this.worldObj.isRemote)
		{
			isActive = this.contents[0] != null && isItemGrindable(this.contents[0]) && (contents[1] == null || (canFit(contents[1], SprocketsAPI.getMillstoneResult(this.contents[0]))));

			MechanicalNetwork network = this.getNetwork();
			
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
				ItemStack output = SprocketsAPI.getMillstoneResult(contents[0]);
				
				if (output != null && activeTicks >= 10 && (contents[1] == null || (canFit(contents[1], output))))
				{
					activeTicks = 0;
					this.decrStackSize(0, 1);
					
					
					if (contents[1] != null)
					{
						output.stackSize += contents[1].stackSize;
					}
					this.setInventorySlotContents(1, output);
				}
			}
			
			
		}
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
		return isActive ? LibConstants.MILLSTONE_TORQUE : 0.0F;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		this.contents[0] = ItemStack.loadItemStackFromNBT(compound.getCompoundTag("item1"));
		this.contents[1] = ItemStack.loadItemStackFromNBT(compound.getCompoundTag("item2"));
		
		isActive = compound.getBoolean("isActive");
		if (isActive != wasActive && this.getNetwork() != null)
		{
			this.getNetwork().updateNetworkSpeedAndTorque();
		}
		wasActive = isActive;
		//wasActive = compound.getBoolean("wasActive");
		activeTicks = compound.getFloat("activeTicks");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		NBTTagCompound item1 = new NBTTagCompound();
		
		if (this.contents[0] != null)
			this.contents[0].writeToNBT(item1);
		compound.setTag("item1", item1);
		
		NBTTagCompound item2 = new NBTTagCompound();
		if (this.contents[1] != null)
			this.contents[1].writeToNBT(item2);
		compound.setTag("item2", item2);
		
		compound.setBoolean("isActive", isActive);
		compound.setBoolean("wasActive", wasActive);
		compound.setFloat("activeTicks", activeTicks);
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		NBTTagCompound data = pkt.getNbtCompound();
		this.readFromNBT(data);
	}
	
	@Override
	public Packet getDescriptionPacket()
	{
		NBTTagCompound data = new NBTTagCompound();
		this.writeToNBT(data);
		return new SPacketUpdateTileEntity(pos, 0, data);
	}
	
	
	@Override
	public int getSizeInventory()
	{
		return 2;
	}

	@Override
	public ItemStack getStackInSlot(int index)
	{
		return contents[index];
	}

	@Override
	public ItemStack decrStackSize(int index, int count)
	{
		return ItemStackHelper.getAndSplit(contents, index, count);
	}

	@Override
	public ItemStack removeStackFromSlot(int index)
	{
		return ItemStackHelper.getAndRemove(contents, index);
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack)
	{
		contents[index] = stack;
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		return false;
	}

	@Override
	public void openInventory(EntityPlayer player)
	{
	}

	@Override
	public void closeInventory(EntityPlayer player)
	{		
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack)
	{
		if (index == 1)
		{
			return false;
		}
		return true;
	}

	@Override
	public int getField(int id)
	{
		return 0;
	}

	@Override
	public void setField(int id, int value)
	{		
	}

	@Override
	public int getFieldCount()
	{
		return 0;
	}

	@Override
	public void clear()
	{
		for (int i = 0; i < contents.length; i++)
		{
			contents[i] = null;
		}
	}

	@Override
	public String getName()
	{
		return "sprockets:container.millstone";
	}

	@Override
	public boolean hasCustomName()
	{
		return false;
	}

	@Override
	public ITextComponent getDisplayName()
	{
		return null;
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side)
	{
		switch (side)
		{
			case DOWN:
				return new int[] { 1 };
			case UP:
				return new int[0];
			default:
				return new int[] { 0, 1 };
		}
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn,
			EnumFacing direction)
	{
		return direction != EnumFacing.UP && this.isItemValidForSlot(index, itemStackIn) && direction != EnumFacing.DOWN && index == 0 && isItemGrindable(itemStackIn);
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack,
			EnumFacing direction)
	{
		return index == 1 && direction != EnumFacing.UP;
	}

	@Override
	public void addInfo(List<ITextComponent> list)
	{
		if (getNetwork() != null && getNetwork().getTorque() < this.torqueCost())
		{
			
		}
	}

}
