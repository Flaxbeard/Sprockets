package flaxbeard.sprockets.multiparts;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import mcmultipart.multipart.PartSlot;
import mcmultipart.raytrace.PartMOP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3i;

public class PartScotchYoke extends PartAxleBase
{
	private static final ArrayList<HashSet<Tuple<Vec3i, PartSlot>>> PART_LINEAR_CONNECTIONS;
	private static final ArrayList<HashSet<Vec3i>> LINEAR_CONNECTIONS;
	private static final ArrayList<EnumSet<PartSlot>> MASK;
	
	static
	{
		PART_LINEAR_CONNECTIONS = new ArrayList<HashSet<Tuple<Vec3i, PartSlot>>>();
		LINEAR_CONNECTIONS = new ArrayList<HashSet<Vec3i>>();
		MASK = new ArrayList<EnumSet<PartSlot>>();

		for (int side = 0; side < 6; side++)
		{
			PART_LINEAR_CONNECTIONS.add(SprocketsMultiparts.rotatePartFacing(side, new Tuple(DOWN, PartSlot.CENTER)));
			LINEAR_CONNECTIONS.add(SprocketsMultiparts.rotateFacing(side, DOWN));
			MASK.add(EnumSet.of(PartSlot.CENTER, SprocketsMultiparts.rotatePartSlot(side, PartSlot.DOWN), SprocketsMultiparts.rotatePartSlot(side, PartSlot.UP)));
		}		
				
	}
	
	public int yokeFacing = 0;
	
	public void setYokeFacing(int side)
	{
		this.yokeFacing = side;
	}
	
	@Override
	public EnumSet<PartSlot> getSlotMask()
	{		
		return MASK.get(yokeFacing).clone();
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		
		yokeFacing = nbt.getInteger("yokeFacing");
		this.setFacing(facing);
	}


	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt = super.writeToNBT(nbt);
		nbt.setInteger("yokeFacing", yokeFacing);
		return nbt;
	}
	
	@Override
	public void writeUpdatePacket(PacketBuffer buf)
	{
		super.writeUpdatePacket(buf);
		buf.writeByte(yokeFacing);
	
	}
	
	@Override
	public void readUpdatePacket(PacketBuffer buf)
	{
		super.readUpdatePacket(buf);
		yokeFacing = buf.readByte();
	}
	
	@Override
	public ItemStack getPickBlock(EntityPlayer player, PartMOP hit)
	{
		return new ItemStack(SprocketsMultiparts.scotchYoke);
	}
	
	@Override
	public List<ItemStack> getDrops()
	{
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		drops.add(new ItemStack(SprocketsMultiparts.scotchYoke));
		drops.add(new ItemStack(SprocketsMultiparts.axle, 1, damage));
		return drops;
	}
	
	@Override
	public Set<Tuple<Vec3i, PartSlot>> multipartLinearConnections()
	{
		return PART_LINEAR_CONNECTIONS.get(yokeFacing);
	}
	
	@Override
	public Set<Vec3i> linearConnections()
	{
		//System.out.println(LINEAR_CONNECTIONS.get(yokeFacing).iterator().next());
		return LINEAR_CONNECTIONS.get(yokeFacing);
	}
	
	@Override
	public EnumSet<PartSlot> getOccupiedSlots()
	{
		EnumSet<PartSlot> ret = super.getOccupiedSlots().clone();
		ret.addAll(getSlotMask());
		return ret;
	}
	
}
