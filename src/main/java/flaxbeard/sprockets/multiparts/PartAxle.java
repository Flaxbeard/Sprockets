package flaxbeard.sprockets.multiparts;

import java.util.HashSet;
import java.util.Set;

import mcmultipart.MCMultiPartMod;
import mcmultipart.multipart.IMultipartContainer;
import mcmultipart.multipart.MultipartHelper;
import mcmultipart.multipart.PartSlot;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import flaxbeard.sprockets.api.IMechanicalConduit;
import flaxbeard.sprockets.api.network.MechanicalNetworkHelper;
import flaxbeard.sprockets.api.network.MechanicalNetworkRegistry;


public class PartAxle extends PartAxleBase
{
	public static final PropertyInteger DIR = PropertyInteger.create("dir", 0, 12);

	@Override
	public IBlockState getExtendedState(IBlockState state)
	{
		return state.withProperty(DIR, hasBelt == 0 ? 12 : (hasBelt == 1 ? (int) meta : (int) meta + 6));
	}
	
	@Override
	public BlockStateContainer createBlockState()
	{
		return new ExtendedBlockState(MCMultiPartMod.multipart, new IProperty[] {DIR}, new IUnlistedProperty[0] );
	}
	
	public byte hasBelt = 0;
	private int size;
	private int axis;
	private BlockPos target;
	private byte meta = 0;
	
	@Override
	public HashSet<Tuple<Vec3i, PartSlot>> multipartCisConnections()
	{
		if (hasBelt == 0)
		{
			return super.multipartCisConnections();
		}
		HashSet<Tuple<Vec3i, PartSlot>> list = (HashSet<Tuple<Vec3i, PartSlot>>) super.multipartCisConnections().clone();
		list.add(new Tuple(target.subtract(getPos()), PartSlot.CENTER));
		return list;
	}
	
	private static final ResourceLocation r = new ResourceLocation("sprockets:beltAxle");
	@Override
	public ResourceLocation getModelPath()
	{

		return super.getModelPath();
	}
	
	public void setBeltChild(BlockPos target, int meta)
	{
		Set<IMechanicalConduit> neighbors = MechanicalNetworkHelper.getConnectedConduits(this);

		hasBelt = 2;
		this.target = target;
		
		this.getNetwork().removeConduitTotal(this, neighbors);
		this.setNetwork(null);
		this.meta = (byte) meta;
		MechanicalNetworkRegistry.newOrJoin(this);
	}
	
	public void setBeltBrain(BlockPos target, int size, int axis, int meta)
	{
		Set<IMechanicalConduit> neighbors = MechanicalNetworkHelper.getConnectedConduits(this);

		hasBelt = 1;
		this.size = size;
		this.axis = axis;
		this.target = target;
		this.meta = (byte) meta;
		
		this.getNetwork().removeConduitTotal(this, neighbors);
		this.setNetwork(null);
		MechanicalNetworkRegistry.newOrJoin(this);
	}
	
	public void removeBelt()
	{
		for (int n = 1; n < size; n++)
		{
			BlockPos remove = getPos().add(axis == 0 ? n : 0,
					axis == 1 ? n : 0,
					axis == 2 ? n : 0);
			
			IMultipartContainer c = MultipartHelper.getPartContainer(getWorld(), remove);
			if (c != null && c.getPartInSlot(PartSlot.CENTER) != null && c.getPartInSlot(PartSlot.CENTER) instanceof PartBelt)
			{
				c.removePart(c.getPartInSlot(PartSlot.CENTER));
			}
		}
		
		BlockPos remove = getPos().add(axis == 0 ? size : 0,
				axis == 1 ? size : 0,
				axis == 2 ? size : 0);
		IMultipartContainer c = MultipartHelper.getPartContainer(getWorld(), remove);
		if (c != null && c.getPartInSlot(PartSlot.CENTER) != null && c.getPartInSlot(PartSlot.CENTER) instanceof PartAxle)
		{
			PartAxle ax = ((PartAxle) c.getPartInSlot(PartSlot.CENTER));
			Set<IMechanicalConduit> neighbors = MechanicalNetworkHelper.getConnectedConduits(ax);
			ax.hasBelt = 0;
			this.getNetwork().removeConduitTotal(ax, neighbors);
			ax.setNetwork(null);
			MechanicalNetworkRegistry.newOrJoin(ax);
		}
		
		if (!removing)
		{
			Set<IMechanicalConduit> neighbors = MechanicalNetworkHelper.getConnectedConduits(this);
			hasBelt = 0;
			this.getNetwork().removeConduitTotal(this, neighbors);
			this.setNetwork(null);
			MechanicalNetworkRegistry.newOrJoin(this);
			System.out.println("Z");

		}
	}
	
	boolean removing = false;
	
	@Override
	public void onRemoved()
	{
		super.onRemoved();

		if (hasBelt == 1)
		{
			removing = true;
			removeBelt();
			System.out.println("A");

		}
		else if (hasBelt == 2)
		{
			IMultipartContainer c = MultipartHelper.getPartContainer(getWorld(), target);
			if (c != null && c.getPartInSlot(PartSlot.CENTER) != null && c.getPartInSlot(PartSlot.CENTER) instanceof PartAxle)
			{
				((PartAxle) c.getPartInSlot(PartSlot.CENTER)).removeBelt();
			}
			System.out.println("eA");

		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		hasBelt = nbt.getByte("hasBelt");
		meta = nbt.getByte("meta");
		size = nbt.getInteger("size");
		axis = nbt.getInteger("dimension");
		if (nbt.hasKey("targetX"))
		{
			int x = nbt.getInteger("targetX");
			int y = nbt.getInteger("targetY");
			int z = nbt.getInteger("targetZ");
			target = new BlockPos(x, y, z);
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt = super.writeToNBT(nbt);
		nbt.setByte("hasBelt", hasBelt);
		nbt.setByte("meta", meta);

		nbt.setInteger("size", size);
		nbt.setInteger("dimension", axis);
		if (target != null)
		{
			nbt.setInteger("targetX", target.getX());
			nbt.setInteger("targetY", target.getY());
			nbt.setInteger("targetZ", target.getZ());

		}
		return nbt;
	}
	
	@Override
	public void writeUpdatePacket(PacketBuffer buf)
	{
		super.writeUpdatePacket(buf);
		buf.writeByte(hasBelt);
		buf.writeByte(meta);
		buf.writeInt(size);
		buf.writeInt(axis);
	}
	

	@Override
	public void readUpdatePacket(PacketBuffer buf)
	{
		super.readUpdatePacket(buf);
		hasBelt = buf.readByte();
		meta = buf.readByte();
		size = buf.readInt();
		axis = buf.readInt();
	}
}
