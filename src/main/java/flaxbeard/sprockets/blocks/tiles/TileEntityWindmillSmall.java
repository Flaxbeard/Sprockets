package flaxbeard.sprockets.blocks.tiles;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.HashSet;

import mcmultipart.multipart.PartSlot;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Tuple;
import net.minecraft.util.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenBeach;
import net.minecraft.world.biome.BiomeGenDesert;
import net.minecraft.world.biome.BiomeGenHills;
import net.minecraft.world.biome.BiomeGenOcean;
import net.minecraft.world.biome.BiomeGenPlains;
import net.minecraft.world.biome.BiomeGenStoneBeach;
import flaxbeard.sprockets.api.IWrenchable;
import flaxbeard.sprockets.api.TileEntityMechanicalConduit;
import flaxbeard.sprockets.blocks.SprocketsBlocks;
import flaxbeard.sprockets.lib.LibConstants;
import flaxbeard.sprockets.multiparts.SprocketsMultiparts;

public class TileEntityWindmillSmall extends TileEntityMechanicalConduit implements IWrenchable
{
	private static final ArrayList<HashSet<Tuple<Vec3i, PartSlot>>> CIS;
	public int facing = -1;
	public byte canSpin = -1;
	public float speedMult = 1.0F;
	public byte connectedToTop = -1;
	public boolean directionFlipped = false;
	
	public TileEntityWindmillSmall()
	{
	}
	
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
		
		if (this.worldObj.getTotalWorldTime() % LibConstants.WINDMILL_UPDATE_TICKS == 0 || canSpin == -1)
		{
			checkSurroundings();
		}
		
		if (this.getNetwork() != null && canSpin == 1)
		{
			getNetwork().addSpeedFromBlock(this, 16f * speedMult * (directionFlipped ? -1 : 1), 1f * speedMult);
		}
		
		
		
		if (this.worldObj.isRemote && canSpin == 1 && this.worldObj.getTotalWorldTime() % 10 == 0 && getNetwork() != null && !getNetwork().isJammed())
		{
			Vec3i dir = EnumFacing.VALUES[facing].getDirectionVec();
			worldObj.spawnParticle(EnumParticleTypes.CLOUD, getPos().getX() + worldObj.rand.nextFloat(), getPos().getY() + worldObj.rand.nextFloat(), getPos().getZ() + worldObj.rand.nextFloat(), 
					-dir.getX() * speedMult * 0.1F, 0, -dir.getZ() * speedMult * 0.1F, 0);
		}
	}
	
	public boolean connectedToTop()
	{
		if (connectedToTop == -1)
		{
			connectedToTop = (byte) (SprocketsBlocks.windmill.getMetaFromState(worldObj.getBlockState(getPos())) >= 6 ? 1 : 0);
		}
		
		return connectedToTop == 1;
	}


	private void checkSurroundings()
	{
		canSpin = 1;
		speedMult = 1.0F;
		
		BiomeGenBase biome = worldObj.getBiomeGenForCoordsBody(getPos());
		if (biome instanceof BiomeGenOcean || biome instanceof BiomeGenBeach || biome instanceof BiomeGenStoneBeach)
		{
			speedMult *= 1.5F;
		}
		else if (biome instanceof BiomeGenHills || biome instanceof BiomeGenPlains || biome instanceof BiomeGenDesert)
		{
			speedMult *= 1.25F;
		}
		
		EnumFacing dir = EnumFacing.VALUES[facing];
		
		
		Vec3i directionVec = dir.getDirectionVec();
		if (!worldObj.isAirBlock(pos.add(directionVec)))
		{
			canSpin = 0;
		}
		
		//System.out.println(facing);
		
		for (int j = -1; j <= 1; j++)
		{
			for (int y = -1; y <= 1; y++)
			{
				if (j != 0 || y != 0)
				{
					BlockPos pos2 = pos.add(facing <= 3 ? j : 0, y, facing <= 3 ? 0 : j);
					BlockPos pos3 = pos2.add(directionVec);
					if (worldObj.getBlockState(pos3).getBlock().isFullBlock())
					{
						canSpin = 0;
					}
					if (worldObj.getBlockState(pos2).getBlock() == SprocketsBlocks.windmill)
					{
						canSpin = 0;
					}
				}
			}
		}
	}


	@Override
	public boolean isNegativeDirection()
	{
		return this.facing % 2 == 1;
	}

	@Override
	public HashSet<Tuple<Vec3i, PartSlot>> multipartCisConnections()
	{
		if (facing == -1 && worldObj != null && worldObj.getBlockState(getPos()) != null && worldObj.getBlockState(getPos()).getBlock() == SprocketsBlocks.windmill)
		{
			facing = SprocketsBlocks.windmill.getMetaFromState(worldObj.getBlockState(getPos())) % 6;
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
		this.canSpin = compound.getByte("canSpin");
		this.speedMult = compound.getFloat("speedMult");
		this.directionFlipped = compound.getBoolean("directionFlipped");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		compound.setByte("canSpin", canSpin);
		compound.setFloat("speedMult", speedMult);
		compound.setBoolean("directionFlipped", directionFlipped);
	}
	
	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
	{
		NBTTagCompound data = pkt.getNbtCompound();
		this.readFromNBT(data);
	}
	
	@Override
	public Packet getDescriptionPacket()
	{
		NBTTagCompound data = new NBTTagCompound();
		this.writeToNBT(data);
		return new S35PacketUpdateTileEntity(pos, 0, data);
	}

	
	
	@Override
	public boolean wrench(EntityPlayer player, World world, BlockPos pos, IBlockState state,
			EnumFacing side)
	{
		if (player.isSneaking())
		{
			this.directionFlipped = !directionFlipped;
		}
		return false;
	}

}
