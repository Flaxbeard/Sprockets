package flaxbeard.sprockets.blocks.tiles;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import mcmultipart.multipart.PartSlot;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import flaxbeard.sprockets.api.IExcessTorqueConsumer;
import flaxbeard.sprockets.api.IMechanicalProducer;
import flaxbeard.sprockets.api.tool.IGyrometerable;
import flaxbeard.sprockets.blocks.SprocketsBlocks;
import flaxbeard.sprockets.lib.LibConstants;
import flaxbeard.sprockets.multiparts.SprocketsMultiparts;

public class TileEntityTorsionSpring extends TileEntitySprocketBase implements IMechanicalProducer, IExcessTorqueConsumer, IGyrometerable
{
	private static final ArrayList<HashSet<Tuple<Vec3i, PartSlot>>> CIS;
	private static final List<Set<Vec3i>> BLOCK_CIS;
	private static final Vec3i UP = EnumFacing.UP.getDirectionVec();
	
	private boolean powered;
	private boolean lastPowered;
	

	public int facing = -1;
	
	public float signum = 1;
	public float progress = 0;
	
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
	
	public float getMax()
	{
		return LibConstants.MAX_SPRING_STORAGE;
	}
	
	@Override
	public void update()
	{
		super.update();
		powered = getWorld().isBlockPowered(getPos());
		if (lastPowered != powered)
		{
			
			getNetwork().updateNetworkSpeedAndTorque();
			lastPowered = powered;
		}

		if (isReleasing())
		{
			if (!getNetwork().isJammed())
			{
				if (progress > 20)
				{
					progress -= 20;
					getNetwork().updateNetworkSpeedAndTorque();
				}
				else if (progress != 0)
				{
					progress = 0;
					getNetwork().updateNetworkSpeedAndTorque();
				}
			}
		}
		else
		{
			if (progress == 0)
			{
				signum = Math.signum((getState() ? 1 : -1) * getNetwork().getSpeedForConduit(this));
			}
			float power = Math.min(progress + Math.abs(getNetwork().getSpeedForConduit(this)), getNetwork().getSurplusForConduit(this) / LibConstants.SPRING_CONSTANT);

			if (power > getMax())
			{
				power = getMax();
			}
			if (power > progress && (signum == Math.signum((getState() ? 1 : -1) * getNetwork().getSpeedForConduit(this))))
			{
				progress = power;
				getNetwork().updateNetworkSpeedAndTorque();
				
			}
		}
		


	} 
	
	public boolean isPowered()
	{
		return powered;
	}
	
	public boolean isReleasing()
	{
		return isPowered();
	}


	@Override
	public boolean isNegativeDirection()
	{
		return this.facing % 2 == 1;
	}

	@Override
	public HashSet<Tuple<Vec3i, PartSlot>> multipartCisConnections()
	{
		if (facing == -1 && worldObj != null && worldObj.getBlockState(getPosMC()) != null && worldObj.getBlockState(getPosMC()).getBlock() == SprocketsBlocks.spring)
		{
			facing = SprocketsBlocks.spring.getMetaFromState(worldObj.getBlockState(getPosMC())) % 6;
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
		if (facing == -1 && worldObj != null && worldObj.getBlockState(getPosMC()) != null && worldObj.getBlockState(getPosMC()).getBlock() == SprocketsBlocks.spring)
		{
			facing = SprocketsBlocks.spring.getMetaFromState(worldObj.getBlockState(getPosMC())) % 6;
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
		powered = compound.getBoolean("powered");
		lastPowered = compound.getBoolean("lastPowered");
		progress = compound.getFloat("progress");
		signum = compound.getFloat("signum");
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		compound = super.writeToNBT(compound);
		compound.setBoolean("powered", powered);
		compound.setBoolean("lastPowered", lastPowered);
		compound.setFloat("progress", progress);
		compound.setFloat("signum", signum);

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
		data = this.writeToNBT(data);
		return new SPacketUpdateTileEntity(pos, 0, data);
	}
	


	@Override
	public float torqueProduced()
	{
		return isReleasing() && progress > 0 ? LibConstants.SPRING_CONSTANT * (progress - 5) : 0;
	}

	@Override
	public float speedProduced()
	{
		return isReleasing() && progress > 0 ? 20 * signum : 0;
	}

	@Override
	public float torqueCost()
	{
		return !isReleasing() ? (progress == getMax() ? -1F : progress * LibConstants.SPRING_CONSTANT ): 0;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInfo(List<ITextComponent> list)
	{
		int amount = Math.round(progress * 100.F / getMax());
		list.add(new TextComponentString(amount + I18n.format("sprockets.gyrometer.spring")));
	}
}
