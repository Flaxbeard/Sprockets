package flaxbeard.sprockets.multiparts;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import mcmultipart.multipart.ISlottedPart;
import mcmultipart.multipart.PartSlot;
import mcmultipart.raytrace.PartMOP;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.Vec3i;
import flaxbeard.sprockets.api.IMechanicalConduit;
import flaxbeard.sprockets.api.network.MechanicalNetworkHelper;
import flaxbeard.sprockets.api.network.MechanicalNetworkRegistry;

public class PartClutch extends PartAxleBase
{
	public IMechanicalConduit upConduit = null;
	public IMechanicalConduit downConduit = null;
	
	public boolean engaged = true;
	public boolean lastEngaged = true;
	public int shouldUpdate = -1;
	
	private static final List<Vec3i> DOWN_DIR;
	private static final List<PartSlot> UP_SLOT;


	static
	{
		DOWN_DIR = new ArrayList<Vec3i>();
		UP_SLOT = new ArrayList<PartSlot>();

		for (int side = 0; side < 6; side++)
		{
			DOWN_DIR.add(SprocketsMultiparts.rotateFacing(side, DOWN).iterator().next());
			UP_SLOT.add(SprocketsMultiparts.rotatePartSlot(side, PartSlot.DOWN));
		}
		
	}
	
	@Override
	protected void updateTopBottom()
	{
		super.updateTopBottom();
		shouldUpdate = 1;
		this.sendUpdatePacket();

	}

	private void updateNets()
	{
		if (getWorld() != null)
		{
			boolean temp = engaged;
			engaged = true;
			Set<IMechanicalConduit> neighbors = MechanicalNetworkHelper.getConnectedConduits(this);
			engaged = temp;
			
			downConduit = null;
			upConduit = null;
			for (IMechanicalConduit neighbor : neighbors)
			{
		
				if (neighbor != null && neighbor.getNetwork() != null && neighbor != this)
				{
					if (
							(top && neighbor.getPosMC().equals(getPos().add(DOWN_DIR.get(facing))))
							|| (!top && neighbor.getPosMC().equals(getPos()) && neighbor instanceof ISlottedPart && ((ISlottedPart) neighbor).getSlotMask().iterator().next().equals(UP_SLOT.get(facing)))
							)
					{
						downConduit = neighbor;
					}
					else
					{
						upConduit = neighbor;
					}
				}
			}
		}
		
	}
	
	@Override
	public ItemStack getPickBlock(EntityPlayer player, PartMOP hit)
	{
		return new ItemStack(SprocketsMultiparts.clutch, 1, damage); // TODO
	}
	
	@Override
	public List<ItemStack> getDrops()
	{
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		drops.add(new ItemStack(SprocketsMultiparts.clutch, 1, damage));
		return drops;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		this.engaged = nbt.getBoolean("engaged");
		this.lastEngaged = nbt.getBoolean("lastEngaged");
	}


	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt = super.writeToNBT(nbt);
		nbt.setBoolean("engaged", engaged);
		nbt.setBoolean("lastEngaged", lastEngaged);
		return nbt;
	}
	
	@Override
	public void writeUpdatePacket(PacketBuffer buf)
	{
		super.writeUpdatePacket(buf);
	}
	
	@Override
	public void readUpdatePacket(PacketBuffer buf)
	{
		super.readUpdatePacket(buf);
		shouldUpdate = 1;
	}

	
	@Override
	public void onNeighborBlockChange(Block block) {
		shouldUpdate = 1;
		this.sendUpdatePacket();
	}
	
	@Override
	public HashSet<Tuple<Vec3i, PartSlot>> multipartCisConnections()
	{
		if (!engaged)
		{
			return new HashSet<Tuple<Vec3i, PartSlot>>();
		}
		return super.multipartCisConnections();
	}


	@Override
	public HashSet<Vec3i> cisConnections()
	{
		if (!engaged)
		{
			return new HashSet<Vec3i>();
		}
		return super.cisConnections();
	}
	
	@Override
	public void update()
	{
		if (shouldUpdate > 0)
		{
			shouldUpdate--;
		}
		else if (shouldUpdate == 0)
		{
			shouldUpdate--;
			updateNets();
		}
		
		super.update();
		if (!getWorld().isBlockPowered(getPos()) != lastEngaged)
		{
			Set<IMechanicalConduit> neighbors = MechanicalNetworkHelper.getConnectedConduits(this);
	
			engaged = !getWorld().isBlockPowered(getPos());
			this.getNetwork().removeConduitTotal(this, neighbors);
			
			this.setNetwork(null);
			MechanicalNetworkRegistry.newOrJoin(this);
			lastEngaged = engaged;
			
		}
	}
	
}
