package flaxbeard.sprockets.blocks.tiles;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import mcmultipart.multipart.PartSlot;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import flaxbeard.sprockets.api.IMechanicalConsumer;
import flaxbeard.sprockets.blocks.SprocketsBlocks;
import flaxbeard.sprockets.lib.LibConstants;
import flaxbeard.sprockets.multiparts.SprocketsMultiparts;

public class TileEntityBellows extends TileEntitySprocketBase implements IMechanicalConsumer
{
	private static final ArrayList<HashSet<Tuple<Vec3i, PartSlot>>> LINEAR;
	private static final List<Set<Vec3i>> BLOCK_LINEAR;
	
	public int facing = -1;
	public int numChilds = 0;
	boolean[] filled = new boolean[6];
	public byte isParent = -1;
	private int ticksExisted = 0;
	private static final Vec3i UP = EnumFacing.UP.getDirectionVec();
	
	static
	{
		LINEAR = new ArrayList<HashSet<Tuple<Vec3i, PartSlot>>>();
		BLOCK_LINEAR = new ArrayList<Set<Vec3i>>();
		for (int side = 0; side < 6; side++)
		{
			LINEAR.add(SprocketsMultiparts.rotatePartFacing(side, new Tuple(new Vec3i(0, 1, 0), PartSlot.DOWN)));
			BLOCK_LINEAR.add(SprocketsMultiparts.rotateFacing(side, UP));
		}
		
	}
	
	public void updateParentPlace()
	{
		BlockPos furn = pos.add(EnumFacing.VALUES[facing].getDirectionVec());

		for (int i = 0; i < facing; i++)
		{
			BlockPos otherHeaterPos = furn.subtract(EnumFacing.VALUES[i].getDirectionVec());
			TileEntity heat = worldObj.getTileEntity(otherHeaterPos);
			if (heat instanceof TileEntityBellows)
			{
				TileEntityBellows fH = (TileEntityBellows) heat;
				if (fH.isParent == 1)
				{
					fH.isParent = 0;
					this.isParent = 1;
					this.numChilds = fH.numChilds + 1;
					this.filled = fH.filled;
					this.filled[facing] = true;
					IBlockState state = worldObj.getBlockState(otherHeaterPos);
					worldObj.notifyBlockUpdate(otherHeaterPos, state, state, 2);
					return;
				}
			}
		}
		
		for (int i = facing + 1; i < 6; i++)
		{
			BlockPos otherHeaterPos = furn.subtract(EnumFacing.VALUES[i].getDirectionVec());
			TileEntity heat = worldObj.getTileEntity(otherHeaterPos);
			if (heat instanceof TileEntityBellows)
			{
				TileEntityBellows fH = (TileEntityBellows) heat;
				if (fH.isParent == 1)
				{
					this.isParent = 0;
					fH.numChilds++;
					fH.filled[facing] = true;
					IBlockState state = worldObj.getBlockState(otherHeaterPos);
					worldObj.notifyBlockUpdate(otherHeaterPos, state, state, 2);
					return;
				}
			}
		}
	}
	
	@Override
	public void update()
	{
		super.update();
		
		if (facing != -1)
		{
			BlockPos furn = pos.add(EnumFacing.VALUES[facing].getDirectionVec());
			TileEntity te = worldObj.getTileEntity(furn);
			if (isParent == -1 && !worldObj.isRemote)
			{
				isParent = 1;
				numChilds = 1;
				filled[facing] = true;
				updateParentPlace();
				IBlockState state = worldObj.getBlockState(pos);
				worldObj.notifyBlockUpdate(pos, state, state, 2);
			}
			if (te != null && te instanceof TileEntityFurnace)
			{
				if (isParent == 1)
				{
					float speed = 0;
					for (int i = 0; i < 6; i++)
					{
						if (filled[i])
						{
							BlockPos otherHeaterPos = furn.subtract(EnumFacing.VALUES[i].getDirectionVec());
							TileEntityBellows heat = (TileEntityBellows) worldObj.getTileEntity(otherHeaterPos);
							if (heat.getNetwork() != null)
							{
								if (!heat.getNetwork().isJammed())
								{
									speed += Math.abs(heat.getNetwork().getSpeedForConduit(heat));
								}
							}
						}
					}
					
					if (speed > 0)
					{
						int num = Math.max(1, (int) (30. / Math.sqrt(speed * 2)));
						TileEntityFurnace furnaceTE = (TileEntityFurnace) te;
						ticksExisted = (ticksExisted + 1) % num;
						
						if (!worldObj.isRemote)
						{
							if ((num == 1 || ticksExisted == 0) && furnaceTE.getField(2) > 0 && furnaceTE.getField(0) > 0 && furnaceTE.getField(2) < furnaceTE.getField(3) - 1)
							{
								furnaceTE.setField(2, furnaceTE.getField(2) + 1); // increase cook time
								furnaceTE.setField(0, furnaceTE.getField(0) - 1);
							}
						}
						else
						{
							if ((30. / Math.sqrt(speed * 2)) < 2 && Minecraft.getMinecraft().thePlayer.ticksExisted % 15 == 0)
							{
								worldObj.spawnParticle(EnumParticleTypes.LAVA, 
										furn.getX() + .5F,
										furn.getY() + .5F,
										furn.getZ() + .5F,
										.25F * (worldObj.rand.nextFloat() - .5F),
										.25F * (worldObj.rand.nextFloat() - .5F),
										.25F * (worldObj.rand.nextFloat() - .5F),
										new int[] { 5, 255, 255 });
								/*for (int i = 0; i < 6; i++)
								{
									if (filled[i])
									{
										BlockPos otherHeaterPos = furn.subtract(EnumFacing.VALUES[i].getDirectionVec());
										worldObj.spawnParticle(EnumParticleTypes.LAVA, 
												otherHeaterPos.getX() + .5F,
												otherHeaterPos.getY() + .5F,
												otherHeaterPos.getZ() + .5F,
												.25F * (worldObj.rand.nextFloat() - .5F),
												.25F * (worldObj.rand.nextFloat() - .5F),
												.25F * (worldObj.rand.nextFloat() - .5F),
												new int[] { 5, 255, 255 });
												
									}
								}*/
							}
						}
						
					}
				}
			}
		}
	}
	
	public void updateParentRemove()
	{
		BlockPos furn = pos.add(EnumFacing.VALUES[facing].getDirectionVec());

		if (isParent == 1)
		{
			for (int i = facing - 1; i >= 0; i--)
			{
				BlockPos otherHeaterPos = furn.subtract(EnumFacing.VALUES[i].getDirectionVec());
				TileEntity heat = worldObj.getTileEntity(otherHeaterPos);
				if (heat instanceof TileEntityBellows)
				{
					TileEntityBellows fH = (TileEntityBellows) heat;
					fH.isParent = 1;
					fH.numChilds = this.numChilds - 1;
					fH.filled = filled;
					fH.filled[facing] = false;
					IBlockState state = worldObj.getBlockState(otherHeaterPos);
					worldObj.notifyBlockUpdate(otherHeaterPos, state, state, 2);
					return;
				}
			}
		}
		else
		{
			for (int i = 5; i > facing; i--)
			{
				BlockPos otherHeaterPos = furn.subtract(EnumFacing.VALUES[i].getDirectionVec());
				TileEntity heat = worldObj.getTileEntity(otherHeaterPos);
				if (heat instanceof TileEntityBellows)
				{
					TileEntityBellows fH = (TileEntityBellows) heat;
					if (fH.isParent == 1)
					{
						fH.numChilds = fH.numChilds - 1;
						fH.filled[facing] = false;
						IBlockState state = worldObj.getBlockState(otherHeaterPos);
						worldObj.notifyBlockUpdate(otherHeaterPos, state, state, 2);
						return;
					}
	
				}
			}
		}
	}
	
	@Override
	public void invalidate()
	{
		updateParentRemove();
		super.invalidate();
	}
	



	@Override
	public boolean isNegativeDirection()
	{
		return this.facing % 2 == 1;
	}
	
	@Override
	public HashSet<Tuple<Vec3i, PartSlot>> multipartLinearConnections()
	{
		if (facing == -1 && worldObj != null && worldObj.getBlockState(getPosMC()) != null && worldObj.getBlockState(getPosMC()).getBlock() == SprocketsBlocks.bellows)
		{
			facing = SprocketsBlocks.bellows.getMetaFromState(worldObj.getBlockState(getPosMC()));
		}

		
		if (facing == -1)
		{
			return new HashSet<Tuple<Vec3i, PartSlot>>();
		}
	//	System.out.println(LINEAR.get(facing).iterator().next().getSecond());

		return LINEAR.get(facing);
	}

	@Override
	public HashSet<Tuple<Vec3i, PartSlot>> multipartCisConnections()
	{
		return new HashSet<Tuple<Vec3i, PartSlot>>();
	}

	@Override
	public HashSet<Tuple<Vec3i, PartSlot>> multipartTransConnections()
	{
		return new HashSet<Tuple<Vec3i, PartSlot>>();
	}

	@Override
	public Set<Vec3i> linearConnections()
	{
		if (facing == -1 && worldObj != null && worldObj.getBlockState(getPosMC()) != null && worldObj.getBlockState(getPosMC()).getBlock() == SprocketsBlocks.bellows)
		{
			facing = SprocketsBlocks.bellows.getMetaFromState(worldObj.getBlockState(getPosMC()));
		}

		
		if (facing == -1)
		{
			return new HashSet<Vec3i>();
		}

		return BLOCK_LINEAR.get(facing);
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
		isParent = compound.getByte("isParent");
		numChilds = compound.getInteger("numChilds");
		ticksExisted = compound.getInteger("ticksExisted");
		for (int i = 0; i < 6; i++)
		{
			filled[i] = compound.getBoolean("filled" + i);
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		compound = super.writeToNBT(compound);
		compound.setByte("isParent", isParent);
		compound.setInteger("numChilds", numChilds);
		compound.setInteger("ticksExisted", ticksExisted);

		for (int i = 0; i < 6; i++)
		{
			compound.setBoolean("filled" + i, filled[i]);
		}
		return compound;
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
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		NBTTagCompound data = new NBTTagCompound();
		this.writeToNBT(data);
		return new SPacketUpdateTileEntity(pos, 0, data);
	}


	@Override
	public float torqueCost()
	{
		return LibConstants.FRICTION_HEATER_TORQUE;
	}

}
