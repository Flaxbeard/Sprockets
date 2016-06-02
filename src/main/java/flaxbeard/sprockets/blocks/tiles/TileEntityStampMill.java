package flaxbeard.sprockets.blocks.tiles;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import mcmultipart.multipart.PartSlot;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemBlock;
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
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import flaxbeard.sprockets.api.IMechanicalConsumer;
import flaxbeard.sprockets.api.IMultiblockBrain;
import flaxbeard.sprockets.api.Multiblock;
import flaxbeard.sprockets.api.SprocketsAPI;
import flaxbeard.sprockets.api.network.MechanicalNetwork;
import flaxbeard.sprockets.blocks.BlockStampMill;
import flaxbeard.sprockets.blocks.SprocketsBlocks;
import flaxbeard.sprockets.common.SprocketsRecipes;
import flaxbeard.sprockets.common.util.ItemStackHandlerLimited;
import flaxbeard.sprockets.multiparts.SprocketsMultiparts;

public class TileEntityStampMill extends TileEntitySprocketBase implements IMechanicalConsumer, IMultiblockBrain
{
	public static final ArrayList<HashSet<Tuple<Vec3i, PartSlot>>> CIS;
	public static final List<Set<Vec3i>> BLOCK_CIS;

	public int facing = -1;
	public byte canSpin = -1;
	public int torque = 0;
	public int lastTorque = 0;
	public int ticksFalling = -1;
	public boolean doEffect = false;

	private static final Vec3i UP = EnumFacing.UP.getDirectionVec();
	private static final Vec3i DOWN = EnumFacing.DOWN.getDirectionVec();

	public final ItemStackHandler slots = new ItemStackHandlerLimited(1, 1);
	private float activeTicks;
	
	static
	{
		CIS = new ArrayList<HashSet<Tuple<Vec3i, PartSlot>>>();
		BLOCK_CIS = new ArrayList<Set<Vec3i>>();
		for (int side = 0; side < 6; side++)
		{
			CIS.add(SprocketsMultiparts.rotatePartFacing(side, new Tuple(new Vec3i(0, 1, 0), PartSlot.DOWN), new Tuple(new Vec3i(0, -1, 0), PartSlot.UP)));
			BLOCK_CIS.add(SprocketsMultiparts.rotateFacing(side, UP, DOWN));
		}
		
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing,
			int loc)
	{
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{
			return true;
		}
		return false;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing,
			int loc)
	{
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{

			return (T) slots;
			
		}
		return null;
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
			return (T) slots;
			
		}
		return super.getCapability(capability, facing);
	}
	
	@Override
	public void update()
	{
		super.update();


		if (facing == -1 && worldObj != null && worldObj.getBlockState(getPosMC()) != null && worldObj.getBlockState(getPosMC()).getBlock() == SprocketsBlocks.stampMill)
		{
			facing = SprocketsBlocks.stampMill.getMetaFromState(worldObj.getBlockState(getPosMC())) % 6;
		}
		
		boolean air = worldObj.isAirBlock(pos.add(0, -1, 0));
		
		if (!worldObj.isRemote && air && slots.getStackInSlot(0) != null && slots.getStackInSlot(0).stackSize > 0)
		{
			ItemStack stack = slots.getStackInSlot(0);
			
			if (stack.getItem() instanceof ItemBlock)
			{
				ItemBlock ib = (ItemBlock) stack.getItem();
				if (ib.canPlaceBlockOnSide(worldObj, pos.add(0, -1, 0), EnumFacing.DOWN, null, stack))
				{
					worldObj.setBlockState(pos.add(0, -1, 0), (ib.getBlock()).getStateFromMeta(ib.getMetadata(stack)), 2);
					slots.setStackInSlot(0, null);
					air = false;
				}
		
			}
		}
		
		if (this.getNetwork() != null && !worldObj.isRemote)
		{
			MechanicalNetwork network = this.getNetwork();

			if (!air && !network.isJammed())
			{
				activeTicks += Math.abs(network.getSpeedForConduit(this));
				
				if (activeTicks > 250)
				{
					IBlockState state = worldObj.getBlockState(pos.add(0, -1, 0));
					ItemStack s = SprocketsAPI.getStampMillRecipeItem(state);
					
					if (s != null)
					{
						InventoryHelper.spawnItemStack(worldObj, pos.getX(), pos.getY() - 1, pos.getZ(), s);
						worldObj.setBlockToAir(pos.add(0, -1, 0));
					}
					else
					{
						IBlockState state2 = SprocketsAPI.getStampMillRecipeBlock(state);
						
						if (state2 != null)
						{
							worldObj.setBlockState(pos, state2, 2);
						}
						else if (state.getBlockHardness(worldObj, pos.add(0, -1, 0)) >= 0 && state.getBlockHardness(worldObj,  pos.add(0, -1, 0)) < 25.0)
						{
							worldObj.destroyBlock(pos.add(0, -1, 0), true);
						}
					}
					activeTicks = 0;
				}
			}
			
			if (air)
			{
				activeTicks = 0;
			}
		}
		
		if (worldObj.isRemote && doEffect && !air)
		{
			IBlockState stat = worldObj.getBlockState(pos.add(0, -1, 0));
			worldObj.playSound(pos.getX() + .5F, pos.getY() + .5F, pos.getZ() + .5F, stat.getBlock().getSoundType().getBreakSound(), SoundCategory.BLOCKS, 0.7F, 0.5F + worldObj.rand.nextFloat() * .25F, false);

			for (int i = 0; i < 10; i++)
			{
				worldObj.spawnParticle(EnumParticleTypes.BLOCK_CRACK, pos.getX() + .5F, pos.getY(), pos.getZ()  + .5F, 0F, 0.1F, 0F, new int[] {Block.getStateId(stat)});
			}
			doEffect = false;

		}
		

	}
	
	


	@Override
	public boolean isNegativeDirection()
	{
		return true;
	}

	@Override
	public HashSet<Tuple<Vec3i, PartSlot>> multipartCisConnections()
	{
		if (facing == -1 && worldObj != null && worldObj.getBlockState(getPosMC()) != null && worldObj.getBlockState(getPosMC()).getBlock() == SprocketsBlocks.stampMill)
		{
			facing = SprocketsBlocks.stampMill.getMetaFromState(worldObj.getBlockState(getPosMC())) % 6;
		}

		
		if (facing == -1)
		{
			return new HashSet<Tuple<Vec3i, PartSlot>>();
		}
		return CIS.get(facing);
	}

	@Override
	public HashSet<Tuple<Vec3i, PartSlot>> multipartTransConnections()
	{
		return new HashSet<Tuple<Vec3i, PartSlot>>();
	}

	@Override
	public Set<Vec3i> cisConnections()
	{
		if (facing == -1 && worldObj != null && worldObj.getBlockState(getPosMC()) != null && worldObj.getBlockState(getPosMC()).getBlock() == SprocketsBlocks.stampMill)
		{
			facing = SprocketsBlocks.stampMill.getMetaFromState(worldObj.getBlockState(getPosMC())) % 6;
		}

		
		if (facing == -1)
		{
			return new HashSet<Vec3i>();
		}
		return BLOCK_CIS.get(facing);
	}

	@Override
	public HashSet<Vec3i> transConnections()
	{
		return new HashSet<Vec3i>();
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		
		slots.deserializeNBT(compound.getCompoundTag("inv"));

		swapXZ = compound.getBoolean("swapXZ");
		flipX = compound.getBoolean("flipX");
		flipZ = compound.getBoolean("flipZ");
		activeTicks = compound.getFloat("activeTicks");

	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		compound = super.writeToNBT(compound);
		NBTTagCompound item1 = new NBTTagCompound();

		compound.setBoolean("swapXZ", swapXZ);
		compound.setBoolean("flipX", flipX);
		compound.setBoolean("flipZ", flipZ);
		compound.setTag("inv", this.slots.serializeNBT());
		compound.setFloat("activeTicks", activeTicks);


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
	public void destroy()
	{
		if (!worldObj.isRemote)
		{
			for (int i = 0; i < slots.getSlots(); i++)
			{
				ItemStack stack = slots.getStackInSlot(i);
				if (stack != null)
				{
					InventoryHelper.spawnItemStack(worldObj, pos.getX(), pos.getY(), pos.getZ(), stack);
				}
			}
		}
		SprocketsRecipes.STAMPMILL.destroyMultiblock(worldObj, getPos(), swapXZ, flipX, flipZ);
		if (!worldObj.isRemote)
		{
			this.worldObj.setBlockState(getPos(), Blocks.IRON_BLOCK.getDefaultState(), 2);
		}
	}
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
	{
		return oldState.getBlock() != newState.getBlock();
	}

	@Override
	public float torqueCost()
	{
		return 0F;
	}


	
	private boolean swapXZ;
	private boolean flipX;
	private boolean flipZ;


	@Override
	public void addMultiblock(Multiblock mb, boolean swapXZ, boolean flipX,
			boolean flipZ)
	{
		this.swapXZ = swapXZ;
		this.flipX = flipX;
		this.flipZ = flipZ;
		this.worldObj.setBlockState(getPos(), worldObj.getBlockState(getPos()).withProperty(BlockStampMill.FACING, swapXZ ? EnumFacing.NORTH : EnumFacing.EAST), 2);
	}





	
	@Override
	public void invalidate()
	{
		//System.out.println(worldObj.isRemote);
		destroy();

		super.invalidate();
		
		System.out.println("INVALIDATE CENTER");

	}
	
}
