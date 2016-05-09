package flaxbeard.sprockets.api.network;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;

import mcmultipart.multipart.IMultipart;
import mcmultipart.multipart.IMultipartContainer;
import mcmultipart.multipart.ISlottedPart;
import mcmultipart.multipart.MultipartHelper;
import mcmultipart.multipart.PartSlot;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import flaxbeard.sprockets.api.IMechanicalConduit;

public class MechanicalNetworkHelper
{

	public static BlockPos lock(IMechanicalConduit base, HashSet<IMechanicalConduit> conduits, MechanicalNetwork network)
	{
		return lock(base, new HashSet<IMechanicalConduit>(), new HashMap<IMechanicalConduit, Boolean>(), conduits, network);
	}
	
	private static BlockPos lock(IMechanicalConduit base, HashSet<IMechanicalConduit> visited, HashMap<IMechanicalConduit, Boolean> visitedMap, HashSet<IMechanicalConduit> networkConduits, MechanicalNetwork network)
	{
		BlockPos result = null;
		boolean first = false;
		if (!visited.contains(base))
		{
			visited.add(base);
			visitedMap.put(base, base.getState());
			first = true;
		}
		
		Tuple<HashMap<IMechanicalConduit, Boolean>, HashSet<IMechanicalConduit>> tup = getConnectedConduitsWithState(base, visitedMap.get(base), network);
		HashSet<IMechanicalConduit> connected = tup.getSecond();
		HashMap<IMechanicalConduit, Boolean> map = tup.getFirst();
		for (IMechanicalConduit connectedConduit : connected)
		{
			if (!visited.contains(connectedConduit))
			{
				if (networkConduits.contains(connectedConduit))
				{
					visited.add(connectedConduit);
					visitedMap.put(connectedConduit, map.get(connectedConduit));
					
					BlockPos subResult = lock(connectedConduit, visited, visitedMap, networkConduits, network);
					if (subResult != null)
					{
						result = subResult;
					}
				}
			}
			else
			{
				if (map.get(connectedConduit) != visitedMap.get(connectedConduit))
				{
					result = connectedConduit.getPosMC();
				}
			}
		}
		
		if (first)
		{
			for (IMechanicalConduit conduit : visited)
			{
				conduit.setState(visitedMap.get(conduit));
			}
		}
		return result;
	}
	
	/**
	 * Finds all connected IMechanicalConduits to this one. Calls the recursive version
	 * of this function with a new HashSet.
	 * 
	 * @param base The IMechanicalConduit to start crawling from
	 * @return A HashSet of all connected IMechanicalConduits
	 */
	public static HashSet<IMechanicalConduit> crawl(IMechanicalConduit base, IMechanicalConduit ignore)
	{
		return crawl(base, new HashSet<IMechanicalConduit>(), ignore);
	}
	
	
	/**
	 * Finds all connected IMechanicalConduits to this one. Recursive.
	 * 
	 * @param base The IMechanicalConduit to start crawling from
	 * @param visited A HashSet of already visited IMechanicalConduits
	 * @return A HashSet of all connected IMechanicalConduits
	 */
	private static HashSet<IMechanicalConduit> crawl(IMechanicalConduit base, HashSet<IMechanicalConduit> visited, IMechanicalConduit ignore)
	{
		
		if (!visited.contains(base))
		{
			visited.add(base);
		}
		
		HashSet<IMechanicalConduit> connected = getConnectedConduits(base);
		for (IMechanicalConduit connectedConduit : connected)
		{
			if (!visited.contains(connectedConduit) && !connectedConduit.equals(ignore) && base.sizeMultiplier() == connectedConduit.sizeMultiplier())
			{
				visited.add(connectedConduit);
				crawl(connectedConduit, visited, ignore);
			}
		}
		return visited;
	}
	
	/**
	 * Gets all IMechanicalConduits that are directly connected to this one.
	 * 
	 * @param base The IMechanicalConduit to start from
	 * @param ignore 
	 * @return A HashSet of all directly connected IMechanicalConduits
	 */
	public static HashSet<IMechanicalConduit> getConnectedConduits(IMechanicalConduit base)
	{
		HashSet<IMechanicalConduit> output = new HashSet<IMechanicalConduit>();

		BlockPos position = base.getPosMC();
		World world = base.getWorldMC();
		
		// Process all potential multipart cis-type connections
		outerLoop:
		for (Tuple<Vec3i, PartSlot> connection : base.multipartCisConnections())
		{
			Vec3i vector = connection.getFirst();
			PartSlot slot = connection.getSecond();
			
			BlockPos checkPos = position.add(vector);
			IMultipartContainer container = MultipartHelper.getPartContainer(world, checkPos);
			
			// If a container exists in the possible position...
			if (container != null)
			{
				IMultipart part = getPartInSlot(container, slot);
				
				// And it has an IMechanicalConduit in the right spot...
				if (part != null && part instanceof IMechanicalConduit)
				{
					IMechanicalConduit conduit = (IMechanicalConduit) part;
					
					// If this is a whole block (TE), check to make sure the conduit can
					// link with this
					if (base instanceof TileEntity)
					{
						if (willConnectCis(conduit, invertVector(vector)))
						{
							output.add(conduit);
							continue outerLoop;
						}
					}
					// If this is a multipart, check to see if the conduit can link
					else if (base instanceof ISlottedPart)
					{
						Object[] slots = getSlotsOccupied((ISlottedPart) base).toArray();
						for (Object checkSlot : slots)
						{
							if (willConnectCisMultipart(conduit, invertVector(vector), (PartSlot) checkSlot))
							{

								output.add(conduit);
								continue outerLoop;
							}
						}
						
					}
					else
					{
						throw new IllegalArgumentException("IMechanicalConduit that is not a TileEntity or IMultipart!");
					}
				}
			}
		}
		
		// Process all potential multipart trans-type connections
		outerLoop:
		for (Tuple<Vec3i, PartSlot> connection : base.multipartTransConnections())
		{
			Vec3i vector = connection.getFirst();
			PartSlot slot = connection.getSecond();
			
			BlockPos checkPos = position.add(vector);
			IMultipartContainer container = MultipartHelper.getPartContainer(world, checkPos);
			
			// If a container exists in the possible position...
			if (container != null)
			{
				IMultipart part = getPartInSlot(container, slot);
				
				// And it has an IMechanicalConduit in the right spot...
				if (part != null && part instanceof IMechanicalConduit)
				{
					IMechanicalConduit conduit = (IMechanicalConduit) part;
					
					// If this is a whole block (TE), check to make sure the conduit can
					// link with this
					if (base instanceof TileEntity)
					{
						if (willConnectTrans(conduit, invertVector(vector)))
						{
							
							output.add(conduit);
							continue outerLoop;
						}
					}
					// If this is a multipart, check to see if the conduit can link
					else if (base instanceof ISlottedPart)
					{
						
						Object[] slots = getSlotsOccupied((ISlottedPart) base).toArray();
						for (Object checkSlot : slots)
						{
							if (willConnectTransMultipart(conduit, invertVector(vector), (PartSlot) checkSlot))
							{
								output.add(conduit);
								continue outerLoop;
							}
						}
						
					}
					else
					{
						throw new IllegalArgumentException("IMechanicalConduit that is not a TileEntity or IMultipart!");
					}
				}
			}
		}
		
		// Process all potential full block cis-type connections
		outerLoop:
		for (Vec3i vector : base.cisConnections())
		{
			BlockPos checkPos = position.add(vector);
			TileEntity te = world.getTileEntity(checkPos);
			
			// If an IMechanicalConduit te exists in the possible position
			if (te != null && te instanceof IMechanicalConduit)
			{	
				IMechanicalConduit conduit = (IMechanicalConduit) te;
				
				// If this is a whole block (TE), check to make sure the conduit can
				// link with this
				if (base instanceof TileEntity)
				{
					if (willConnectCis(conduit, invertVector(vector)))
					{
						
						output.add(conduit);
						continue outerLoop;
					}
				}
				// If this is a multipart, check to see if the conduit can link
				else if (base instanceof ISlottedPart)
				{
					
					Object[] slots = getSlotsOccupied((ISlottedPart) base).toArray();
					
					for (Object checkSlot : slots)
					{
						if (willConnectCisMultipart(conduit, invertVector(vector), (PartSlot) checkSlot))
						{
							output.add(conduit);
							continue outerLoop;
						}
					}
					
				}
				else
				{
					throw new IllegalArgumentException("IMechanicalConduit that is not a TileEntity or IMultipart!");
				}
			}
		}
		
		// Process all potential full block trans-type connections
		outerLoop:
		for (Vec3i vector : base.transConnections())
		{
			BlockPos checkPos = position.add(vector);
			TileEntity te = world.getTileEntity(checkPos);
			
			// If an IMechanicalConduit te exists in the possible position
			if (te != null && te instanceof IMechanicalConduit)
			{	
				IMechanicalConduit conduit = (IMechanicalConduit) te;
				
				// If this is a whole block (TE), check to make sure the conduit can
				// link with this
				if (base instanceof TileEntity)
				{
					if (willConnectTrans(conduit, invertVector(vector)))
					{
						output.add(conduit);
						continue outerLoop;
					}
				}
				// If this is a multipart, check to see if the conduit can link
				else if (base instanceof ISlottedPart)
				{
					
					Object[] slots = getSlotsOccupied((ISlottedPart) base).toArray();
					
					for (Object checkSlot : slots)
					{
						if (willConnectTransMultipart(conduit, invertVector(vector), (PartSlot) checkSlot))
						{
							output.add(conduit);
							continue outerLoop;
						}
					}
					
				}
				else
				{
					throw new IllegalArgumentException("IMechanicalConduit that is not a TileEntity or IMultipart!");
				}
			}
		}
	
		
		return output;
	}
	
	
	
	

	
	/**
	 * Gets all IMechanicalConduits that are directly connected to this one.
	 * 
	 * @param base The IMechanicalConduit to start from
	 * @param network 
	 * @return A HashSet of all directly connected IMechanicalConduits
	 */
	public static Tuple<HashMap<IMechanicalConduit, Boolean>, HashSet<IMechanicalConduit>> getConnectedConduitsWithState(IMechanicalConduit base, boolean state, MechanicalNetwork network)
	{
		HashSet<IMechanicalConduit> output = new HashSet<IMechanicalConduit>();
		HashMap<IMechanicalConduit, Boolean> states = new HashMap<IMechanicalConduit, Boolean>();

		BlockPos position = base.getPosMC();
		World world = base.getWorldMC();
		
		// Process all potential multipart cis-type connections
		outerLoop:
		for (Tuple<Vec3i, PartSlot> connection : base.multipartCisConnections())
		{
			Vec3i vector = connection.getFirst();
			PartSlot slot = connection.getSecond();
			
			BlockPos checkPos = position.add(vector);
			IMultipartContainer container = MultipartHelper.getPartContainer(world, checkPos);
			
			// If a container exists in the possible position...
			if (container != null)
			{
				IMultipart part = getPartInSlot(container, slot);
				
				// And it has an IMechanicalConduit in the right spot...
				if (part != null && part instanceof IMechanicalConduit)
				{
					IMechanicalConduit conduit = (IMechanicalConduit) part;
					
					// If this is a whole block (TE), check to make sure the conduit can
					// link with this
					if (base instanceof TileEntity)
					{
						if (willConnectCis(conduit, invertVector(vector)))
						{
							if (network.contains(conduit))
							{
								output.add(conduit);
								if (conduit.isNegativeDirection() == base.isNegativeDirection())
								{
									states.put(conduit, state);
								}
								else
								{
									states.put(conduit, !state);
								}
							}
							else
							{
								network.linkNetwork(conduit, base, conduit.isNegativeDirection() != base.isNegativeDirection(), true);
							}
							
							continue outerLoop;
						}
					}
					// If this is a multipart, check to see if the conduit can link
					else if (base instanceof ISlottedPart)
					{
						Object[] slots = getSlotsOccupied((ISlottedPart) base).toArray();
						
						for (Object checkSlot : slots)
						{
							if (willConnectCisMultipart(conduit, invertVector(vector), (PartSlot) checkSlot))
							{
								if (network.contains(conduit))
								{
									output.add(conduit);
									if (conduit.isNegativeDirection() == base.isNegativeDirection())
									{
										states.put(conduit, state);
									}
									else
									{
										states.put(conduit, !state);
									}
								}
								else
								{
									network.linkNetwork(conduit, base, conduit.isNegativeDirection() != base.isNegativeDirection(), true);
								}
								
								continue outerLoop;
							}
						}
						
					}
					else
					{
						throw new IllegalArgumentException("IMechanicalConduit that is not a TileEntity or IMultipart!");
					}
				}
			}
		}
		
		// Process all potential multipart trans-type connections
		outerLoop:
		for (Tuple<Vec3i, PartSlot> connection : base.multipartTransConnections())
		{
			Vec3i vector = connection.getFirst();
			PartSlot slot = connection.getSecond();
			
			BlockPos checkPos = position.add(vector);
			IMultipartContainer container = MultipartHelper.getPartContainer(world, checkPos);
			
			// If a container exists in the possible position...
			if (container != null)
			{
				
				IMultipart part = getPartInSlot(container, slot);
				
				// And it has an IMechanicalConduit in the right spot...
				if (part != null && part instanceof IMechanicalConduit)
				{
					
					IMechanicalConduit conduit = (IMechanicalConduit) part;
					
					// If this is a whole block (TE), check to make sure the conduit can
					// link with this
					if (base instanceof TileEntity)
					{
						if (willConnectTrans(conduit, invertVector(vector)))
						{
							if (network.contains(conduit))
							{
								output.add(conduit);
								states.put(conduit, !state);
							}
							else
							{
								network.linkNetwork(conduit, base, true, false);
							}
							continue outerLoop;
						}
					}
					// If this is a multipart, check to see if the conduit can link
					else if (base instanceof ISlottedPart)
					{
						
						Object[] slots = getSlotsOccupied((ISlottedPart) base).toArray();
						
						for (Object checkSlot : slots)
						{
							if (willConnectTransMultipart(conduit, invertVector(vector), (PartSlot) checkSlot))
							{

								if (network.contains(conduit))
								{
									

									output.add(conduit);
									states.put(conduit, !state);
								}
								else
								{
									network.linkNetwork(conduit, base, true, false);
								}
								continue outerLoop;
							}
						}
						
					}
					else
					{
						throw new IllegalArgumentException("IMechanicalConduit that is not a TileEntity or IMultipart!");
					}
				}
			}
		}
		
		// Process all potential full block cis-type connections
		outerLoop:
		for (Vec3i vector : base.cisConnections())
		{
			BlockPos checkPos = position.add(vector);
			TileEntity te = world.getTileEntity(checkPos);
			
			// If an IMechanicalConduit te exists in the possible position
			if (te != null && te instanceof IMechanicalConduit)
			{	
				IMechanicalConduit conduit = (IMechanicalConduit) te;
				
				// If this is a whole block (TE), check to make sure the conduit can
				// link with this
				if (base instanceof TileEntity)
				{
					if (willConnectCis(conduit, invertVector(vector)))
					{
						if (network.contains(conduit))
						{
							output.add(conduit);
							if (conduit.isNegativeDirection() == base.isNegativeDirection())
							{
								states.put(conduit, state);
							}
							else
							{
								states.put(conduit, !state);
							}
						}
						else
						{
							network.linkNetwork(conduit, base, conduit.isNegativeDirection() != base.isNegativeDirection(), true);
						}
						
						continue outerLoop;
					}
				}
				// If this is a multipart, check to see if the conduit can link
				else if (base instanceof ISlottedPart)
				{
					
					Object[] slots = getSlotsOccupied((ISlottedPart) base).toArray();
					
					for (Object checkSlot : slots)
					{
						if (willConnectCisMultipart(conduit, invertVector(vector), (PartSlot) checkSlot))
						{
							if (network.contains(conduit))
							{
								output.add(conduit);
								if (conduit.isNegativeDirection() == base.isNegativeDirection())
								{
									states.put(conduit, state);
								}
								else
								{
									states.put(conduit, !state);
								}
							}
							else
							{
								network.linkNetwork(conduit, base, conduit.isNegativeDirection() != base.isNegativeDirection(), true);
							}
							continue outerLoop;
						}
					}
					
				}
				else
				{
					throw new IllegalArgumentException("IMechanicalConduit that is not a TileEntity or IMultipart!");
				}
			}
		}
		
		// Process all potential full block trans-type connections
		outerLoop:
		for (Vec3i vector : base.transConnections())
		{
			BlockPos checkPos = position.add(vector);
			TileEntity te = world.getTileEntity(checkPos);
			
			// If an IMechanicalConduit te exists in the possible position
			if (te != null && te instanceof IMechanicalConduit)
			{	
				IMechanicalConduit conduit = (IMechanicalConduit) te;
				
				// If this is a whole block (TE), check to make sure the conduit can
				// link with this
				if (base instanceof TileEntity)
				{
					if (willConnectTrans(conduit, invertVector(vector)))
					{
						if (network.contains(conduit))
						{
							output.add(conduit);
							states.put(conduit, !state);
						}
						else
						{
							network.linkNetwork(conduit, base, true, false);
						}
						continue outerLoop;
					}
				}
				// If this is a multipart, check to see if the conduit can link
				else if (base instanceof ISlottedPart)
				{
					
					Object[] slots = getSlotsOccupied((ISlottedPart) base).toArray();
					
					for (Object checkSlot : slots)
					{
						if (willConnectTransMultipart(conduit, invertVector(vector), (PartSlot) checkSlot))
						{
							if (network.contains(conduit))
							{
								output.add(conduit);
								states.put(conduit, !state);
							}
							else
							{
								network.linkNetwork(conduit, base, true, false);
							}
							continue outerLoop;
						}
					}
					
				}
				else
				{
					throw new IllegalArgumentException("IMechanicalConduit that is not a TileEntity or IMultipart!");
				}
			}
		}
	

		return new Tuple(states, output);
	}
	
	
	/**
	 * Checks whether a conduit will connect from a specific direction
	 * 
	 * @param conduit The conduit to test
	 * @param vector The direction to test from
	 * @return Whether the conduit will connect
	 */
	public static boolean willConnectCis(IMechanicalConduit conduit, Vec3i vector)
	{
		for (Vec3i potentialConnection : conduit.cisConnections())
		{
			if (potentialConnection.equals(vector))
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks whether a conduit will connect from a specific direction
	 * 
	 * @param conduit The conduit to test
	 * @param vector The direction to test from
	 * @return Whether the conduit will connect
	 */
	public static boolean willConnectTrans(IMechanicalConduit conduit, Vec3i vector)
	{
		for (Vec3i potentialConnection : conduit.transConnections())
		{
			if (potentialConnection.equals(vector))
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks whether a conduit will connect to a multipart from a specific direction
	 * 
	 * @param conduit The conduit to test
	 * @param vector The direction to test from
	 * @param slot The multipart slot to test for
	 * @return Whether the conduit will connect
	 */
	public static boolean willConnectCisMultipart(IMechanicalConduit conduit, Vec3i vector, PartSlot slot)
	{
		for (Tuple potentialConnection : conduit.multipartCisConnections())
		{
			if (potentialConnection.getFirst().equals(vector) && potentialConnection.getSecond().equals(slot))
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks whether a conduit will connect to a multipart from a specific direction
	 * 
	 * @param conduit The conduit to test
	 * @param vector The direction to test from
	 * @param slot The multipart slot to test for
	 * @return Whether the conduit will connect
	 */
	public static boolean willConnectTransMultipart(IMechanicalConduit conduit, Vec3i vector, PartSlot slot)
	{
		BlockPos position = conduit.getPosMC();
		for (Tuple potentialConnection : conduit.multipartTransConnections())
		{
			if (potentialConnection.getFirst().equals(vector) && potentialConnection.getSecond().equals(slot))
			{
				return true;
			}
		}
		return false;
	}
	
	
	private static Vec3i invertVector(Vec3i vec)
	{
		return new Vec3i(-vec.getX(), -vec.getY(), -vec.getZ());
	}
	
	public static EnumSet<PartSlot> getSlotsOccupied(ISlottedPart part)
	{

		if (part instanceof IConditionalPart)
		{
			return ((IConditionalPart) part).getOccupiedSlots();
		}
		else
		{
			return part.getSlotMask();
		}
	}
	
	public static IMultipart getPartInSlot(IMultipartContainer container, PartSlot slot)
	{
		IMultipart part = container.getPartInSlot(slot);
		if (part != null)
		{
			return part;
		}
		else
		{
			for (IMultipart potentialPart : container.getParts())
			{
				if (potentialPart instanceof IConditionalPart && ((IConditionalPart) potentialPart).getOccupiedSlots().contains(slot))
				{
					return potentialPart;
				}
			}
		}
		return null;
	}
}
