package flaxbeard.sprockets.blocks.tiles;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import mcmultipart.block.TileMultipartContainer;
import mcmultipart.multipart.IMultipartContainer;
import mcmultipart.multipart.MultipartContainer;
import mcmultipart.multipart.MultipartHelper;
import mcmultipart.multipart.PartSlot;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.common.capabilities.Capability;
import flaxbeard.sprockets.api.IMechanicalConduit;
import flaxbeard.sprockets.api.IMultiblockBrain;
import flaxbeard.sprockets.api.Multiblock;
import flaxbeard.sprockets.api.network.MechanicalNetworkHelper;

public class TileEntityMultiblock extends TileEntitySprocketBase
{
	
	private IBlockState block;
	private Multiblock multiblock;
	public int pos;
	private BlockPos center;
	public boolean isTurning = false;
	private NBTTagCompound data = null;
	private List<ItemStack> drops = new ArrayList<ItemStack>();
	public boolean swapXZ;
	public boolean flipX;
	public boolean flipZ;

	public void init(Multiblock mb, IBlockState bs, int pos, BlockPos centerPos, boolean swapXZ, boolean flipX,	boolean flipZ, List<ItemStack> drops)
	{
		this.multiblock = mb;
		this.block = bs;
		this.pos = pos;
		this.center = centerPos;
		this.drops = drops;
		this.swapXZ = swapXZ;
		this.flipX = flipX;
		this.flipZ = flipZ;
	}
	
	public List<ItemStack> getDrops()
	{
		return drops;
	}
	
	
	public IBlockState getBlockState()
	{
		return block;
	}
	
	public Multiblock getMultiblock()
	{
		return multiblock;
	}

	public int getIndex()
	{
		return pos;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		if (compound.hasKey("blockId"))
		{
			int blockId = compound.getInteger("blockId");
			block = Block.getBlockById(blockId).getStateFromMeta(compound.getInteger("metad"));
			pos = compound.getInteger("pos");

			multiblock =  Multiblock.get(compound.getString("multiblock"));
			center = new BlockPos(
					compound.getInteger("centerX"),
					compound.getInteger("centerY"),
					compound.getInteger("centerZ"));
			
			NBTTagList dropsTag = (NBTTagList) compound.getTag("drops");
			drops = new ArrayList<ItemStack>();
			for (int i = 0; i < dropsTag.tagCount(); i++)
			{
				drops.add(ItemStack.loadItemStackFromNBT(dropsTag.getCompoundTagAt(i)));
			}
			
			swapXZ = compound.getBoolean("swapXZ");
			flipX = compound.getBoolean("flipX");
			flipZ = compound.getBoolean("flipZ");
		}
		
		if (compound.hasKey("mpdata"))
		{
			data = compound.getCompoundTag("mpdata");
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		compound = super.writeToNBT(compound);
		if (block != null)
		{
			compound.setInteger("blockId", Block.getIdFromBlock(block.getBlock()));
			compound.setInteger("metad", block.getBlock().getMetaFromState(block));
			compound.setInteger("pos", pos);
			compound.setString("multiblock", multiblock.getTag());
			compound.setInteger("centerX", center.getX());
			compound.setInteger("centerY", center.getY());
			compound.setInteger("centerZ", center.getZ());
			compound.setBoolean("swapXZ", swapXZ);
			compound.setBoolean("flipX", flipX);
			compound.setBoolean("flipZ", flipZ);
			
			if (data != null)
			{
				compound.setTag("mpdata", data);
			}
			
			NBTTagList dropsTag = new NBTTagList();
			for (ItemStack item : drops)
			{
				dropsTag.appendTag(item.writeToNBT(new NBTTagCompound()));
			}
			
			compound.setTag("drops", dropsTag);
		}
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
		return new SPacketUpdateTileEntity(getPos(), 0, data);
	}
	
	@Override
	public void invalidate()
	{
		super.invalidate();
		
		if (!isTurning)
		{
			TileEntity te = worldObj.getTileEntity(center);
			if (te != null && te instanceof IMultiblockBrain)
			{
				((IMultiblockBrain) te).destroy();
			}
			
			//worldObj.destroyBlock(getPos(), true);
		}
	}

	public void turnBack()
	{
		isTurning = true;
		
		this.worldObj.setBlockState(getPos(), block);
		IMultipartContainer cont = MultipartHelper.getPartContainer(worldObj, getPos());
		
		if (cont != null && cont instanceof TileMultipartContainer)
		{
			cont = ((TileMultipartContainer) cont).getPartContainer();	
		}
		if (data != null && cont != null && cont instanceof MultipartContainer)
		{
			((MultipartContainer) cont).readFromNBT(data);
		}
		isTurning = false;
	}

	public void setData(NBTTagCompound data)
	{
		this.data = data;
	}


	@Override
	public Set<Tuple<Vec3i, PartSlot>> multipartCisConnections()
	{
		if (multiblock == null)
		{
			return new HashSet<Tuple<Vec3i, PartSlot>>();
		}
		return multiblock.multipartCisConnections(pos, center, worldObj, swapXZ, flipX,  flipZ);
	}


	@Override
	public Set<Tuple<Vec3i, PartSlot>> multipartTransConnections()
	{
		if (multiblock == null)
		{
			return new HashSet<Tuple<Vec3i, PartSlot>>();
		}
		return multiblock.multipartTransConnections(pos, center, worldObj, swapXZ, flipX,  flipZ);
	}


	@Override
	public Set<Vec3i> cisConnections()
	{
		if (multiblock == null)
		{
			return new HashSet<Vec3i>();
		}
		return multiblock.cisConnections(pos, center, worldObj, swapXZ, flipX,  flipZ);
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		if (center == null)
		{
			return super.hasCapability(capability, facing);
		}
		return ((IMultiblockBrain) worldObj.getTileEntity(center)).hasCapability(capability, facing, pos);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if (center == null)
		{
			return super.getCapability(capability, facing);
		}
		return ((IMultiblockBrain) worldObj.getTileEntity(center)).getCapability(capability, facing, pos);
	}


	@Override
	public Set<Vec3i> transConnections()
	{
		if (multiblock == null)
		{
			return new HashSet<Vec3i>();
		}
		return multiblock.transConnections(pos, center, worldObj, swapXZ, flipX,  flipZ);
	}


	@Override
	public boolean isNegativeDirection()
	{
		return false;
	}
	
	
	@Override
	public float getSpeed()
	{
		return ((IMechanicalConduit) worldObj.getTileEntity(center)).getSpeed();
	}

	@Override
	public float getTorque()
	{
		return ((IMechanicalConduit) worldObj.getTileEntity(center)).getTorque();
	}



}
