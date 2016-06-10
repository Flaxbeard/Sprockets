package flaxbeard.sprockets.multiparts;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import mcmultipart.microblock.IMicroblock.IFaceMicroblock;
import mcmultipart.multipart.IMultipart;
import mcmultipart.multipart.IMultipartContainer;
import mcmultipart.multipart.ISlottedPart;
import mcmultipart.multipart.PartSlot;
import mcmultipart.raytrace.PartMOP;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3i;
import flaxbeard.sprockets.api.IMechanicalConduit;
import flaxbeard.sprockets.api.network.IConditionalPart;
import flaxbeard.sprockets.api.network.MechanicalNetworkHelper;
import flaxbeard.sprockets.api.network.MechanicalNetworkRegistry;
import flaxbeard.sprockets.lib.LibConstants;

public abstract class PartAxleBase extends PartSprocketBase implements ISlottedPart, IMechanicalConduit, IConditionalPart
{
	
	public int facing = 0;
	private static final EnumSet<PartSlot> MASK  = EnumSet.of(PartSlot.CENTER);
	
	public static final ArrayList<PartSlot> TOP_SLOT;
	public static final ArrayList<PartSlot> BOTTOM_SLOT;
	private static final ArrayList<EnumSet<PartSlot>> TOP;
	private static final ArrayList<EnumSet<PartSlot>> BOTTOM;
	private static final ArrayList<EnumSet<PartSlot>> TOP_BOTTOM;
	
	private static final AxisAlignedBB[] BOUNDS;
	private static final AxisAlignedBB[] BOUNDS_TOP;
	private static final AxisAlignedBB[] BOUNDS_BOTTOM;
	private static final AxisAlignedBB[] BOUNDS_TOP_BOTTOM;
	
	
	private static final ArrayList<HashSet<Vec3i>> BLOCK_CIS_CONNECTIONS;
	private static final ArrayList<HashSet<Vec3i>> BLOCK_TRANS_CONNECTIONS;
	private static final ArrayList<HashSet<Tuple<Vec3i, PartSlot>>> PART_CIS_CONNECTIONS;
	private static final ArrayList<HashSet<Tuple<Vec3i, PartSlot>>> PART_CIS_CONNECTIONS_TOP;
	private static final ArrayList<HashSet<Tuple<Vec3i, PartSlot>>> PART_CIS_CONNECTIONS_BOTTOM;
	private static final ArrayList<HashSet<Tuple<Vec3i, PartSlot>>> PART_CIS_CONNECTIONS_TOP_BOTTOM;
	private static final ArrayList<HashSet<Tuple<Vec3i, PartSlot>>> PART_TRANS_CONNECTIONS;
	protected static final Vec3i HERE = new Vec3i(0, 0, 0);
	protected static final Vec3i NORTH = EnumFacing.NORTH.getDirectionVec();
	protected static final Vec3i SOUTH = EnumFacing.SOUTH.getDirectionVec();
	protected static final Vec3i EAST = EnumFacing.EAST.getDirectionVec();
	protected static final Vec3i WEST = EnumFacing.WEST.getDirectionVec();
	protected static final Vec3i UP = EnumFacing.UP.getDirectionVec();
	protected static final Vec3i DOWN = EnumFacing.DOWN.getDirectionVec();
	
	
	private int c = 0;
	public float rotation = 0;
	public boolean top = true;
	public boolean bottom = true;
	
	static
	{
		BOUNDS = new AxisAlignedBB[6];
		BOUNDS_TOP = new AxisAlignedBB[6];
		BOUNDS_BOTTOM = new AxisAlignedBB[6];
		BOUNDS_TOP_BOTTOM = new AxisAlignedBB[6];
		BLOCK_CIS_CONNECTIONS = new ArrayList<HashSet<Vec3i>>();
		BLOCK_TRANS_CONNECTIONS = new ArrayList<HashSet<Vec3i>>();
		PART_CIS_CONNECTIONS = new ArrayList<HashSet<Tuple<Vec3i, PartSlot>>>();
		PART_CIS_CONNECTIONS_TOP = new ArrayList<HashSet<Tuple<Vec3i, PartSlot>>>();
		PART_CIS_CONNECTIONS_BOTTOM = new ArrayList<HashSet<Tuple<Vec3i, PartSlot>>>();
		PART_CIS_CONNECTIONS_TOP_BOTTOM = new ArrayList<HashSet<Tuple<Vec3i, PartSlot>>>();
		PART_TRANS_CONNECTIONS = new ArrayList<HashSet<Tuple<Vec3i, PartSlot>>>();
		
		TOP_SLOT = new ArrayList<PartSlot>();
		BOTTOM_SLOT = new ArrayList<PartSlot>();
		
		BOTTOM = new ArrayList<EnumSet<PartSlot>>();
		TOP = new ArrayList<EnumSet<PartSlot>>();
		TOP_BOTTOM = new ArrayList<EnumSet<PartSlot>>();
		
		for (int side = 0; side < 6; side++)
		{
			BOUNDS[side] = SprocketsMultiparts.rotateAxis(side, 5.5F / 16F, 1.002F / 16F, 5.5F / 16F, 10.5F / 16F, 14.998F / 16F, 10.5F / 16F);
			BOUNDS_TOP[side] = SprocketsMultiparts.rotateAxis(side, 5.5F / 16F, 0F / 16F, 5.5F / 16F, 10.5F / 16F, 14.998F / 16F, 10.5F / 16F);
			BOUNDS_BOTTOM[side] = SprocketsMultiparts.rotateAxis(side, 5.5F / 16F, 1.002F / 16F, 5.5F / 16F, 10.5F / 16F, 16F / 16F, 10.5F / 16F);
			BOUNDS_TOP_BOTTOM[side] = SprocketsMultiparts.rotateAxis(side, 5.5F / 16F, 0F / 16F, 5.5F / 16F, 10.5F / 16F, 16F / 16F, 10.5F / 16F);
			
			BLOCK_CIS_CONNECTIONS.add(SprocketsMultiparts.rotateFacing(side, UP, DOWN));
			
			PART_CIS_CONNECTIONS.add(SprocketsMultiparts.rotatePartFacing(side, new Tuple(HERE, PartSlot.UP), new Tuple(HERE, PartSlot.DOWN)));
			PART_CIS_CONNECTIONS_TOP.add(SprocketsMultiparts.rotatePartFacing(side, new Tuple(HERE, PartSlot.UP), new Tuple(HERE, PartSlot.DOWN), new Tuple(DOWN, PartSlot.UP)));
			PART_CIS_CONNECTIONS_BOTTOM.add(SprocketsMultiparts.rotatePartFacing(side, new Tuple(HERE, PartSlot.UP), new Tuple(HERE, PartSlot.DOWN), new Tuple(UP, PartSlot.DOWN)));
			PART_CIS_CONNECTIONS_TOP_BOTTOM.add(SprocketsMultiparts.rotatePartFacing(side, new Tuple(HERE, PartSlot.UP), new Tuple(HERE, PartSlot.DOWN), new Tuple(DOWN, PartSlot.UP), new Tuple(UP, PartSlot.DOWN)));
			
			PART_TRANS_CONNECTIONS.add(new HashSet<Tuple<Vec3i, PartSlot>>());
			BLOCK_TRANS_CONNECTIONS.add(new HashSet<Vec3i>());
			
			TOP_SLOT.add(SprocketsMultiparts.rotatePartSlot(side, PartSlot.DOWN));			
			BOTTOM_SLOT.add(SprocketsMultiparts.rotatePartSlot(side, PartSlot.UP));	
			
			TOP.add(EnumSet.of(TOP_SLOT.get(side), PartSlot.CENTER));
			BOTTOM.add(EnumSet.of(BOTTOM_SLOT.get(side), PartSlot.CENTER));
			TOP_BOTTOM.add(EnumSet.of(TOP_SLOT.get(side), BOTTOM_SLOT.get(side), PartSlot.CENTER));
		}		
		
		//BLOCK_CONNECTIONS = (ArrayList<EnumFacing>[]) temp.toArray();
		
	}
	
	protected void updateTopBottom()
	{
		IMultipartContainer contain = this.getContainer();
		if (contain != null)
		{
			Set<IMechanicalConduit> neighbors = MechanicalNetworkHelper.getConnectedConduits(this);
			boolean lastTop = top;
			boolean lastBottom = bottom;
			IMultipart topPart = contain.getPartInSlot(TOP_SLOT.get(facing));
			top = topPart == null || (topPart instanceof IFaceMicroblock && ((IFaceMicroblock) topPart).isFaceHollow());
			
			IMultipart bottomPart = contain.getPartInSlot(BOTTOM_SLOT.get(facing));
			bottom = bottomPart == null || (bottomPart instanceof IFaceMicroblock && ((IFaceMicroblock) bottomPart).isFaceHollow());

			
			if ((top != lastTop || bottom != lastBottom) )
			{
				Set<IMechanicalConduit> neighbors2 = MechanicalNetworkHelper.getConnectedConduits(this);
				
				for (IMechanicalConduit neighbor : neighbors2)
				{
					if (!neighbors.contains(neighbor))
					{
						this.getNetwork().conduits.remove(this);
						this.setNetwork(null);
						MechanicalNetworkRegistry.newOrJoin(this);
						return;
					}
				}
				
				for (IMechanicalConduit neighbor : neighbors)
				{
					if (!neighbors2.contains(neighbor))
					{
						this.getNetwork().removeConduitTotal(this, neighbors);
						this.setNetwork(null);
						MechanicalNetworkRegistry.newOrJoin(this);
						return;
						//this.getNetwork().conduits.remove(this);
						//this.setNetwork(null);
						//this.initialized = false;
						//return;
					}
				}
				//markDirty();
			}
		}
	}
	
	
	@Override
	public void onPartChanged(IMultipart part)
	{
		super.onPartChanged(part);
		if (initialized)
		{
			updateTopBottom();
		}
		
	}

	@Override
	public EnumSet<PartSlot> getSlotMask()
	{		
		return MASK.clone();
	}
	
	public void setFacing(int side)
	{
		this.facing = side;
	}


	@Override
	public void addSelectionBoxes(List<AxisAlignedBB> list)
	{
		if (top)
		{
			if (bottom)
			{
				list.add(BOUNDS_TOP_BOTTOM[facing]);
			}
			else
			{
				list.add(BOUNDS_TOP[facing]);
			}
		}
		else if (bottom)
		{
			list.add(BOUNDS_BOTTOM[facing]);
		}
		list.add(BOUNDS[facing]);
	}

	@Override
	public void addCollisionBoxes(AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity)
	{
		if (BOUNDS[facing].intersectsWith(mask))
		{
			list.add(BOUNDS[facing]);
		}
	}
	
	@Override
	public ItemStack getPickBlock(EntityPlayer player, PartMOP hit)
	{
		return new ItemStack(SprocketsMultiparts.axle, 1, damage);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		
		facing = nbt.getInteger("facing");
		top = nbt.getBoolean("top");
		bottom = nbt.getBoolean("bottom");
		this.setFacing(facing);
	}


	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt = super.writeToNBT(nbt);
		nbt.setInteger("facing", facing);
		nbt.setBoolean("top", top);
		nbt.setBoolean("bottom", bottom);
		return nbt;
	}
	
	@Override
	public void writeUpdatePacket(PacketBuffer buf)
	{
		super.writeUpdatePacket(buf);
		buf.writeByte(facing);
		buf.writeBoolean(top);
		buf.writeBoolean(bottom);
	
	}
	
	@Override
	public void readUpdatePacket(PacketBuffer buf)
	{
		super.readUpdatePacket(buf);
		facing = buf.readByte();
		top = buf.readBoolean();
		bottom = buf.readBoolean();
		this.setFacing(facing);
	}

	@Override
	public float maxSpeed()
	{
		switch(damage)
		{
			case 0:
				return LibConstants.MAX_SPEED_WOODEN_SPROCKET;
			case 1:
				return LibConstants.MAX_SPEED_STONE_SPROCKET;
		}
		return LibConstants.MAX_SPEED_IRON_SPROCKET;
	}

	@Override
	public float minTorque()
	{
		switch(damage)
		{
			case 1:
				return LibConstants.MIN_TORQUE_STONE_SPROCKET;
			case 2:
				return LibConstants.MIN_TORQUE_IRON_SPROCKET;
		}
		return LibConstants.MIN_TORQUE_WOODEN_SPROCKET;
	}

	@Override
	public float maxTorque()
	{
		switch(damage)
		{
			case 0:
				return LibConstants.MAX_TORQUE_WOODEN_SPROCKET;
			case 1:
				return LibConstants.MAX_TORQUE_STONE_SPROCKET;
		}
		return LibConstants.MAX_TORQUE_IRON_SPROCKET;
	}


	@Override
	public float sizeMultiplier()
	{
		return 1;
	}

	@Override
	public HashSet<Tuple<Vec3i, PartSlot>> multipartCisConnections()
	{
		if (top)
		{
			if (bottom)
			{
				return PART_CIS_CONNECTIONS_TOP_BOTTOM.get(facing);
			}
			else
			{
				return PART_CIS_CONNECTIONS_TOP.get(facing);
			}
		}
		else if (bottom)
		{
			return PART_CIS_CONNECTIONS_BOTTOM.get(facing);
		}
		return PART_CIS_CONNECTIONS.get(facing);
	}

	@Override
	public HashSet<Tuple<Vec3i, PartSlot>> multipartTransConnections()
	{
		return PART_TRANS_CONNECTIONS.get(facing);
	}

	@Override
	public HashSet<Vec3i> cisConnections()
	{
		return BLOCK_CIS_CONNECTIONS.get(facing);
	}

	@Override
	public HashSet<Vec3i> transConnections()
	{
		return BLOCK_TRANS_CONNECTIONS.get(facing);
	}


	@Override
	public boolean isNegativeDirection()
	{
		return facing % 2 == 0;
	}


	@Override
	public EnumSet<PartSlot> getOccupiedSlots()
	{
		if (top)
		{
			if (bottom)
			{
				

				return TOP_BOTTOM.get(facing);
			}
			else
			{
				return TOP.get(facing);
			}
		}
		else if (bottom)
		{
			return BOTTOM.get(facing);
		}
		return EnumSet.of(PartSlot.CENTER);
	}


	@Override
	public List<ItemStack> getDrops()
	{
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		drops.add(new ItemStack(SprocketsMultiparts.axle, 1, damage));
		return drops;
	}
	
	@Override
	public void setDamage(int damage)
	{
		this.damage = damage;
		switch(damage)
		{
			case 0:
				setMaterial(Material.WOOD);
				setHardness(LibConstants.MINE_TIME_WOODEN_SPROCKET);
				break;
			case 1:
				setMaterial(Material.ROCK);
				setHardness(LibConstants.MINE_TIME_STONE_SPROCKET);
				break;
			case 2:
				setMaterial(Material.IRON);
				setHardness(LibConstants.MINE_TIME_IRON_SPROCKET);
				break;
		}
	}

}
