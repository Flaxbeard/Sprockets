package flaxbeard.sprockets.api;

import java.util.HashSet;

import mcmultipart.multipart.PartSlot;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Tuple;
import net.minecraft.util.Vec3i;
import net.minecraft.world.World;
import flaxbeard.sprockets.api.network.MechanicalNetwork;


/**
 * Implement this class in a TileEntity or IMultipart if it can receive
 * or output mechanical energy
 */
public interface IMechanicalConduit
{
	/**
	 * All of the relative multipart positions this conduit can connect to in a cis connection
	 * 
	 * @return An iterable of tuples, representing the different possible sub-parts this conduit
	 * 			can connect to. The first element is null if the sub-part is in the same block,
	 * 			otherwise it's the direction of the connecting block. The second element is the
	 * 			slot of the object this conduit can connect to.
	 */
	public HashSet<Tuple<Vec3i, PartSlot>> multipartCisConnections();
	
	/**
	 * All of the relative multipart positions this conduit can connect to in a trans connection
	 * 
	 * @return An iterable of tuples, representing the different possible sub-parts this conduit
	 * 			can connect to. The first element is null if the sub-part is in the same block,
	 * 			otherwise it's the direction of the connecting block. The second element is the
	 * 			slot of the object this conduit can connect to.
	 */
	public HashSet<Tuple<Vec3i, PartSlot>> multipartTransConnections();
	
	/**
	 * All of the full blocks this conduit can connect to in a cis connection
	 * 
	 * @return An iterable representing the different directions this conduit can connect.
	 */
	public HashSet<Vec3i> cisConnections();
	
	/**
	 * All of the full blocks this conduit can connect to in a trans connection
	 * 
	 * @return An iterable representing the different directions this conduit can connect.
	 */
	public HashSet<Vec3i> transConnections();

	/**
	 * The maximum speed this conduit can rotate at.
	 * 
	 * @return The conduit's maximum speed.
	 */
	public float maxSpeed();

	/**
	 * The minimum torque this conduit can rotate at.
	 * 
	 * @return The conduit's minimum torque.
	 */
	public float minTorque();
	
	/**
	 * The maximum torque this conduit can rotate at.
	 * 
	 * @return The conduit's maximum torque.
	 */
	public float maxTorque();
	
	/**
	 * The speed/torque modifier for this conduit. A normal gear is 1.0. Conduits are linked
	 * in networks of identically sized conduits, so keep this in mind if adding nonstandard
	 * sizes.
	 * 
	 * @return The size multiplier of the conduit.
	 */
	public float sizeMultiplier();
	
	public World getWorld();
	
	public BlockPos getPos();
	
	public void setState(boolean state);
	
	public boolean getState();
	
	public MechanicalNetwork getNetwork();
	
	public void setNetwork(MechanicalNetwork network);
	
	public boolean isNegativeDirection();

	public void remove();
}
