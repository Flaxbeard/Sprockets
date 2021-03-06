package flaxbeard.sprockets.blocks.tiles;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import mcmultipart.multipart.PartSlot;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import flaxbeard.sprockets.api.IMechanicalProducer;
import flaxbeard.sprockets.api.tool.IGyrometerable;
import flaxbeard.sprockets.api.tool.IWrenchable;
import flaxbeard.sprockets.blocks.SprocketsBlocks;
import flaxbeard.sprockets.lib.LibConstants;
import flaxbeard.sprockets.multiparts.SprocketsMultiparts;

public class TileEntityWindmill extends TileEntitySprocketBase implements IWrenchable, IGyrometerable, IMechanicalProducer
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
		
		if (facing == -1 && worldObj != null && worldObj.getBlockState(getPosMC()) != null && worldObj.getBlockState(getPosMC()).getBlock() == SprocketsBlocks.windmill)
		{
			facing = SprocketsBlocks.windmill.getMetaFromState(worldObj.getBlockState(getPosMC())) % 6;
		}


		if ((this.worldObj.getTotalWorldTime() % LibConstants.WINDMILL_UPDATE_TICKS == 0 || canSpin == -1) && facing != -1)
		{
			checkSurroundings();
		}
		
		
		
		if (this.worldObj.isRemote && canSpin == 1 && getNetwork() != null && facing != -1)
		{
			
			Vec3i dir = EnumFacing.VALUES[facing].getDirectionVec();
			worldObj.spawnParticle(EnumParticleTypes.CLOUD, 
					getPosMC().getX() + (worldObj.rand.nextFloat() - .5F) * (facing <= 3 ? 6F : 0F) + .5F, 
					getPosMC().getY() + (worldObj.rand.nextFloat() - .5F) * 6F + .5F, 
					getPosMC().getZ() + (worldObj.rand.nextFloat() - .5F) * (facing <= 3 ? 0F : 6F) + .5F, 
					-dir.getX() * speedMult * 0.1F, 0, -dir.getZ() * speedMult * 0.1F, 0);
		}
	}
	
	@SideOnly(Side.CLIENT)
	@Override
    public AxisAlignedBB getRenderBoundingBox()
    {
		return new AxisAlignedBB(
				pos.add(facing <= 3 ? -3 : 0, -3, facing <= 3 ? 0 : -3),
				pos.add(facing <= 3 ? 4 : 0, 4, facing <= 3 ? 0 : 4));
    }

	private void checkSurroundings()
	{
		lastCanSpin = canSpin;
		lastSpeedMult = speedMult;
		lastBlockedMult = blockedMult;
		canSpin = 1;
		speedMult = 1.0F;
		
		Biome biome = worldObj.getBiomeForCoordsBody(getPosMC());
		if (biome == Biomes.OCEAN|| biome == Biomes.BEACH || biome == Biomes.STONE_BEACH)
		{
			speedMult *= 1.5F;
		}
		else if (biome == Biomes.PLAINS || biome == Biomes.DESERT || biome == Biomes.DESERT_HILLS || biome == Biomes.SAVANNA || biome == Biomes.SAVANNA_PLATEAU)
		{
			speedMult *= 1.25F;
		}
		
		EnumFacing dir = EnumFacing.VALUES[facing];
		
		
		Vec3i directionVec = dir.getDirectionVec();

		
		for (int w = -6; w <= 6; w++)
		{
			for (int y = -6; y <=6; y++)
			{
				for (int d = -6; d <=6; d++)
				{
					if (w != 0 || y != 0 || d != 0)
					{
						BlockPos pos2 = pos.add(facing <= 3 ? w : d, y, facing <= 3 ? d : w);
	
						// Check for windmills
						if (Math.abs(w) + Math.abs(y) < 10)
						{
							if (worldObj.getBlockState(pos2).getBlock() == SprocketsBlocks.windmill)
							{
								TileEntityWindmill otherWindmill = ((TileEntityWindmill) worldObj.getTileEntity(pos2));
								if (d == 0 || (facing <= 3 && otherWindmill.facing > 3 || facing > 3 && otherWindmill.facing <= 3))
								{
									((TileEntityWindmill) worldObj.getTileEntity(pos2)).canSpin = 2;
									((TileEntityWindmill) worldObj.getTileEntity(pos2)).getNetwork().updateNetworkSpeedAndTorque();
									canSpin = 2;
									return;
								}
							}
						}
					}
				}
			}
		}
		
		
		// Make sure there aren't blocks or windmills blocking the blade
		for (int w = -3; w <= 3; w++)
		{
			for (int y = -3; y <= 3; y++)
			{
				if (w != 0 || y != 0)
				{
					BlockPos pos2 = pos.add(facing <= 3 ? w : 0, y, facing <= 3 ? 0 : w);
					
					IBlockState statePos = worldObj.getBlockState(pos2);
					
					// Check for obstructing blocks
					if (!statePos.getBlock().isReplaceable(worldObj, pos2))
					{
						canSpin = 2;
						return;
					}
				}
			}
		}
		
		// Check for blocks in front of the windmill
		int airBlocks = 0;
		int starts = 0;
		int finishes = 0;
		for (int w = -3; w <= 3; w++)
		{
			int minY = -3;
			int maxY = 3;
			
			// Check a diamond shaped area
			if (w == -3 || w == 3)
			{
				minY = 0;
				maxY = 0;
			}
			else if (w == -2 || w == 2)
			{
				minY = -1;
				maxY = 1;
			}
			else if (w == -1 || w == 1)
			{
				minY = -2;
				maxY = 2;
			}
			
			nonDepthLoop:
			for (int y = minY; y <= maxY; y++)
			{
				for (int d = 1; d < 24; d++)
				{
					
					BlockPos pos2 = pos.add(facing <= 3 ? w : (facing == 5 ? d : -d), y, facing <= 3 ? (facing == 3 ? d : -d) : w);					
					IBlockState statePos3 = worldObj.getBlockState(pos2);
					if (statePos3.getBlock() == Blocks.AIR)
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
				
		if (airBlocks > 500)
		{
			this.blockedMult = 1.0F;
		}
		else if (airBlocks > 465)
		{
			this.blockedMult = 0.8F;
		}
		else if (airBlocks > 385)
		{
			this.blockedMult = 0.5F;
		}
		else if (airBlocks > 310)
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
		if (facing == -1 && worldObj != null && worldObj.getBlockState(getPosMC()) != null && worldObj.getBlockState(getPosMC()).getBlock() == SprocketsBlocks.windmill)
		{
			facing = SprocketsBlocks.windmill.getMetaFromState(worldObj.getBlockState(getPosMC())) % 6;
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
		if (facing == -1 && worldObj != null && worldObj.getBlockState(getPosMC()) != null && worldObj.getBlockState(getPosMC()).getBlock() == SprocketsBlocks.windmill)
		{
			facing = SprocketsBlocks.windmill.getMetaFromState(worldObj.getBlockState(getPosMC())) % 6;
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
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		compound = super.writeToNBT(compound);
		compound.setByte("canSpin", canSpin);
		compound.setFloat("speedMult", speedMult);
		compound.setFloat("blockedMult", blockedMult);
		compound.setBoolean("directionFlipped", directionFlipped);
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
	@SideOnly(Side.CLIENT)
	public void addInfo(List<ITextComponent> list)
	{
		if (canSpin == 2)
		{
			
			list.add(new TextComponentString(I18n.format("sprockets.gyrometer.windmill.stuck")));
		}
		else if (canSpin == 3)
		{
			list.add(new TextComponentString(I18n.format("sprockets.gyrometer.windmill.windBlockedFull")));
		}
		else if (canSpin == 1)
		{
			
			
			if (blockedMult == 0.2F)
			{
				list.add(new TextComponentString(I18n.format("sprockets.gyrometer.windmill.windBlockedHigh")));
			}
			else if (blockedMult == 0.5F)
			{
				list.add(new TextComponentString(I18n.format("sprockets.gyrometer.windmill.windBlockedMed")));
			}
			else if (blockedMult == 0.8F)
			{
				if (speedMult == 1.5F)
				{
					list.add(new TextComponentString(I18n.format("sprockets.gyrometer.windmill.windBlockedLowSpeedHigh")));
				}
				else if (speedMult == 1.25F)
				{
					list.add(new TextComponentString(I18n.format("sprockets.gyrometer.windmill.windBlockedLowSpeedMed")));
				}
				else
				{
					list.add(new TextComponentString(I18n.format("sprockets.gyrometer.windmill.windBlockedLowSpeedLow")));
				}
			}
			else
			{
				if (speedMult == 1.5F)
				{
					list.add(new TextComponentString(I18n.format("sprockets.gyrometer.windmill.windSpeedHigh")));
				}
				else if (speedMult == 1.25F)
				{
					list.add(new TextComponentString(I18n.format("sprockets.gyrometer.windmill.windSpeedMed")));
				}
				else
				{
					list.add(new TextComponentString(I18n.format("sprockets.gyrometer.windmill.windSpeedLow")));
				}
			}
		}
			
	}

	@Override
	public float torqueProduced()
	{
		return canSpin == 1 ? LibConstants.WINDMILL_TORQUE * speedMult * blockedMult : 0;
	}

	@Override
	public float speedProduced()
	{
		return canSpin == 1 ? LibConstants.WINDMILL_SPEED * speedMult  * blockedMult * (directionFlipped ? -1 : 1) : 0;
	}
}
