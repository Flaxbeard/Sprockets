package flaxbeard.sprockets.blocks.tiles;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import mcmultipart.multipart.PartSlot;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenBeach;
import net.minecraft.world.biome.BiomeGenDesert;
import net.minecraft.world.biome.BiomeGenHills;
import net.minecraft.world.biome.BiomeGenOcean;
import net.minecraft.world.biome.BiomeGenPlains;
import net.minecraft.world.biome.BiomeGenStoneBeach;
import flaxbeard.sprockets.api.IMechanicalProducer;
import flaxbeard.sprockets.api.tool.IGyrometerable;
import flaxbeard.sprockets.api.tool.IWrenchable;
import flaxbeard.sprockets.blocks.SprocketsBlocks;
import flaxbeard.sprockets.lib.LibConstants;
import flaxbeard.sprockets.multiparts.SprocketsMultiparts;

public class TileEntityWindmillSmall extends TileEntitySprocketBase implements IWrenchable, IGyrometerable, IMechanicalProducer
{
	private static final ArrayList<HashSet<Tuple<Vec3i, PartSlot>>> CIS;
	private static final List<Set<Vec3i>> BLOCK_CIS;
	
	public int facing = -1;
	public byte canSpin = -1;
	private byte lastCanSpin = -1;
	public float speedMult = 1.0F;
	private float lastSpeedMult = 1.0F;
	public float blockedMult = 1.0F;
	private float lastBlockedMult = 1.0F;
	public byte connectedToTop = -1;
	public boolean directionFlipped = false;
	private static final Vec3i UP = EnumFacing.UP.getDirectionVec();
	
	static
	{
		CIS = new ArrayList<HashSet<Tuple<Vec3i, PartSlot>>>();
		BLOCK_CIS = new ArrayList<Set<Vec3i>>();
		for (int side = 0; side < 6; side++)
		{
			CIS.add(SprocketsMultiparts.rotatePartFacing(side, new Tuple(new Vec3i(0, 1, 0), PartSlot.DOWN)));
			BLOCK_CIS.add(SprocketsMultiparts.rotateFacing(side, UP));
		}
		
	}
	
	@Override
	public void update()
	{
		super.update();
		
		if (facing == -1 && worldObj != null && worldObj.getBlockState(getPosMC()) != null && worldObj.getBlockState(getPosMC()).getBlock() == SprocketsBlocks.windmillSmall)
		{
			facing = SprocketsBlocks.windmillSmall.getMetaFromState(worldObj.getBlockState(getPosMC())) % 6;
		}

		
		if ((this.worldObj.getTotalWorldTime() % LibConstants.WINDMILL_UPDATE_TICKS == 0 || canSpin == -1) && facing != -1)
		{
			checkSurroundings();
		}
		

		
		if (this.worldObj.isRemote && canSpin == 1 && this.worldObj.getTotalWorldTime() % 10 == 0 && getNetwork() != null && !getNetwork().isJammed() && facing != -1)
		{
			Vec3i dir = EnumFacing.VALUES[facing].getDirectionVec();
			worldObj.spawnParticle(EnumParticleTypes.CLOUD, getPosMC().getX() + worldObj.rand.nextFloat(), getPosMC().getY() + worldObj.rand.nextFloat(), getPosMC().getZ() + worldObj.rand.nextFloat(), 
					-dir.getX() * speedMult * 0.1F, 0, -dir.getZ() * speedMult * 0.1F, 0);
		}
	}
	
	public boolean connectedToTop()
	{
		if (connectedToTop == -1)
		{
			connectedToTop = (byte) (SprocketsBlocks.windmillSmall.getMetaFromState(worldObj.getBlockState(getPosMC())) >= 6 ? 1 : 0);
		}
		
		return connectedToTop == 1;
	}


	private void checkSurroundings()
	{
		lastCanSpin = canSpin;
		lastSpeedMult = speedMult;
		lastBlockedMult = blockedMult;
		canSpin = 1;
		speedMult = 1.0F;
		
		
		BiomeGenBase biome = worldObj.getBiomeGenForCoordsBody(getPosMC());
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
			canSpin = 2;
		}
		
		
		// Make sure there aren't blocks or windmills blocking the blade
		for (int w = -1; w <= 1; w++)
		{
			for (int y = -1; y <= 1; y++)
			{
				if (w != 0 || y != 0)
				{
					BlockPos pos2 = pos.add(facing <= 3 ? w : 0, y, facing <= 3 ? 0 : w);
					BlockPos pos3 = pos2.add(directionVec);
					
					IBlockState statePos3 = worldObj.getBlockState(pos3);
					
					// Check for obstructing blocks
					if (statePos3.getBlock().isFullBlock(statePos3))
					{
						canSpin = 2;
						return;
					}
					
					// Check for windmills
					if (w == 0 || y == 0)
					{
						if (worldObj.getBlockState(pos2).getBlock() == SprocketsBlocks.windmillSmall)
						{
							canSpin = 2;
							return;
						}
					}
				}
			}
		}
		
		// Check for blocks in front of the windmill
		int airBlocks = 0;
		int starts = 0;
		int finishes = 0;
		for (int w = -1; w <= 1; w++)
		{
			nonDepthLoop:
			for (int y = -1; y <= 1; y++)
			{
				for (int d = 2; d < 16; d++)
				{
					if ((w == 0 || y == 0))
					{
						
						BlockPos pos2 = pos.add(facing <= 3 ? w : (facing == 5 ? d : -d), y, facing <= 3 ? (facing == 3 ? d : -d) : w);					
						IBlockState statePos3 = worldObj.getBlockState(pos2);
						if (statePos3.getBlock() == Blocks.air)
						{
							airBlocks++;
						}
						else
						{
							if (statePos3.getBlock().isFullBlock(statePos3))
							{
								// A solid block will make all blocks behind it register as blocked as well
								continue nonDepthLoop;
							}
						}
						
					}
					
				}
			}
		}
		
		if (airBlocks > 65)
		{
			this.blockedMult = 1.0F;
		}
		else if (airBlocks > 60)
		{
			this.blockedMult = 0.8F;
		}
		else if (airBlocks > 50)
		{
			this.blockedMult = 0.5F;
		}
		else if (airBlocks > 40)
		{
			this.blockedMult = 0.2F;
		}
		else
		{
			this.canSpin = 3;
		}
		
		if (canSpin != lastCanSpin || blockedMult != lastBlockedMult || speedMult != lastSpeedMult)
		{
			this.getNetwork().updateNetworkSpeedAndTorque();
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
		if (facing == -1 && worldObj != null && worldObj.getBlockState(getPosMC()) != null && worldObj.getBlockState(getPosMC()).getBlock() == SprocketsBlocks.windmillSmall)
		{
			facing = SprocketsBlocks.windmillSmall.getMetaFromState(worldObj.getBlockState(getPosMC())) % 6;
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
		if (facing == -1 && worldObj != null && worldObj.getBlockState(getPosMC()) != null && worldObj.getBlockState(getPosMC()).getBlock() == SprocketsBlocks.windmillSmall)
		{
			facing = SprocketsBlocks.windmillSmall.getMetaFromState(worldObj.getBlockState(getPosMC())) % 6;
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
		this.canSpin = compound.getByte("canSpin");
		this.speedMult = compound.getFloat("speedMult");
		this.blockedMult = compound.getFloat("blockedMult");
		this.directionFlipped = compound.getBoolean("directionFlipped");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		compound.setByte("canSpin", canSpin);
		compound.setFloat("speedMult", speedMult);
		compound.setFloat("blockedMult", blockedMult);
		compound.setBoolean("directionFlipped", directionFlipped);
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		NBTTagCompound data = pkt.getNbtCompound();
		this.readFromNBT(data);
		if (getNetwork() != null)
		{
			getNetwork().updateNetworkSpeedAndTorque();
		}
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
			this.getNetwork().updateNetworkSpeedAndTorque();
		}
		return false;
	}

	@Override
	public void addInfo(List<ITextComponent> list)
	{
		if (canSpin == 2)
		{
			
			list.add(new TextComponentString(I18n.translateToLocal("sprockets.gyrometer.windmill.stuck")));
		}
		else if (canSpin == 3)
		{
			list.add(new TextComponentString(I18n.translateToLocal("sprockets.gyrometer.windmill.windBlockedFull")));
		}
		else if (canSpin == 1)
		{
			
			
			if (blockedMult == 0.2F)
			{
				list.add(new TextComponentString(I18n.translateToLocal("sprockets.gyrometer.windmill.windBlockedHigh")));
			}
			else if (blockedMult == 0.5F)
			{
				list.add(new TextComponentString(I18n.translateToLocal("sprockets.gyrometer.windmill.windBlockedMed")));
			}
			else if (blockedMult == 0.8F)
			{
				if (speedMult == 1.5F)
				{
					list.add(new TextComponentString(I18n.translateToLocal("sprockets.gyrometer.windmill.windBlockedLowSpeedHigh")));
				}
				else if (speedMult == 1.25F)
				{
					list.add(new TextComponentString(I18n.translateToLocal("sprockets.gyrometer.windmill.windBlockedLowSpeedMed")));
				}
				else
				{
					list.add(new TextComponentString(I18n.translateToLocal("sprockets.gyrometer.windmill.windBlockedLowSpeedLow")));
				}
			}
			else
			{
				if (speedMult == 1.5F)
				{
					list.add(new TextComponentString(I18n.translateToLocal("sprockets.gyrometer.windmill.windSpeedHigh")));
				}
				else if (speedMult == 1.25F)
				{
					list.add(new TextComponentString(I18n.translateToLocal("sprockets.gyrometer.windmill.windSpeedMed")));
				}
				else
				{
					list.add(new TextComponentString(I18n.translateToLocal("sprockets.gyrometer.windmill.windSpeedLow")));
				}
			}
		}
			
	}

	@Override
	public float torqueProduced()
	{
		return LibConstants.SMALL_WINDMILL_TORQUE * speedMult * blockedMult;
	}

	@Override
	public float speedProduced()
	{
		return LibConstants.SMALL_WINDMILL_SPEED * speedMult  * blockedMult * (directionFlipped ? -1 : 1);
	}

}
