package flaxbeard.sprockets.blocks.tiles;

import java.util.ArrayList;
import java.util.HashSet;

import mcmultipart.multipart.PartSlot;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import flaxbeard.sprockets.api.IWrenchable;
import flaxbeard.sprockets.blocks.BlockRedstoneEngine;
import flaxbeard.sprockets.blocks.SprocketsBlocks;
import flaxbeard.sprockets.lib.LibConstants;
import flaxbeard.sprockets.multiparts.SprocketsMultiparts;

public class TileEntityRedstoneEngine extends TileEntitySprocketBase implements IWrenchable
{
	private static final ArrayList<HashSet<Tuple<Vec3i, PartSlot>>> CIS;
	public int facing = -1;
	public boolean directionFlipped = false;
	public byte isOn = -1;
	
	static
	{
		CIS = new ArrayList<HashSet<Tuple<Vec3i, PartSlot>>>();
		for (int side = 0; side < 6; side++)
		{
			CIS.add(SprocketsMultiparts.rotatePartFacing(side, new Tuple(new Vec3i(0, 1, 0), PartSlot.DOWN)));
		}
		
	}
	
	@Override
	public void update()
	{
		super.update();

		if (this.getNetwork() != null && isOn())
		{
			getNetwork().addSpeedFromBlock(this, LibConstants.REDSTONE_ENGINE_SPEED * (directionFlipped ? -1 : 1), LibConstants.REDSTONE_ENGINE_TORQUE );
		}
		

	}
	
	public boolean isOn()
	{
		if (isOn == -1)
		{
			isOn = (byte) (SprocketsBlocks.redEngine.getMetaFromState(worldObj.getBlockState(getPosMC())) >= 6 ? 1 : 0);
		}
		
		return isOn == 1;
	}


	@Override
	public boolean isNegativeDirection()
	{
		return this.facing % 2 == 1;
	}

	@Override
	public HashSet<Tuple<Vec3i, PartSlot>> multipartCisConnections()
	{
		if (facing == -1 && worldObj != null && worldObj.getBlockState(getPosMC()) != null && worldObj.getBlockState(getPosMC()).getBlock() == SprocketsBlocks.redEngine)
		{
			facing = SprocketsBlocks.redEngine.getMetaFromState(worldObj.getBlockState(getPosMC())) % 6;
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
	public HashSet<Vec3i> cisConnections()
	{
		return new HashSet<Vec3i>();
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
		this.directionFlipped = compound.getBoolean("directionFlipped");
		this.isOn = compound.getByte("isOn");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		compound.setBoolean("directionFlipped", directionFlipped);
		compound.setByte("isOn", isOn);
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
	public boolean wrench(EntityPlayer player, World world, BlockPos pos, IBlockState state,
			EnumFacing side)
	{
		if (player.isSneaking())
		{
			this.directionFlipped = !directionFlipped;
			this.worldObj.setBlockState(pos, state.withProperty(BlockRedstoneEngine.REVERSED, directionFlipped), 2);
			worldObj.notifyBlockUpdate(pos, state, state.withProperty(BlockRedstoneEngine.REVERSED, directionFlipped), 2);
		}
		return false;
	}
}
