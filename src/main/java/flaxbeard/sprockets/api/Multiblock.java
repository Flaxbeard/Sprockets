package flaxbeard.sprockets.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mcmultipart.block.TileMultipartContainer;
import mcmultipart.multipart.IMultipart;
import mcmultipart.multipart.IMultipartContainer;
import mcmultipart.multipart.MultipartContainer;
import mcmultipart.multipart.MultipartHelper;
import mcmultipart.multipart.PartSlot;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import flaxbeard.sprockets.blocks.SprocketsBlocks;
import flaxbeard.sprockets.blocks.tiles.TileEntityMultiblock;

public class Multiblock
{
	public final Object[][][] mb;
	public final Object[] mbLong;

	public final int ySize;
	public final int xSize;
	public final int zSize;
	public final int centerX;
	public final int centerY;
	public final int centerZ;
	
	public final String name;
	
	private static Map<String, Multiblock> multiblocks = new HashMap<String, Multiblock>();
	
	public Multiblock(String name, int xD, int height, int zD, int centerX, int centerY, int centerZ, Object... key)
	{
		this.name = name;
		Character[][][] structure = new Character[height][zD][xD];
		
		System.out.println(xD + " " + height + " " + zD);
		ySize = height;
		zSize = zD;
		xSize = xD;
		
		this.centerX = centerX;
		this.centerY = centerY;
		this.centerZ = centerZ;
		
		for (int i = 0; i < height * zD; i++)
		{
			int y = i / zD;
			int z = i % zD;
			
			String value = (String) key[i];
			
			for (int x = 0; x < xD; x++)
			{
				structure[y][z][x] = value.charAt(x);
			}
		}
		
		if (structure.length == 0 || structure[0].length == 0 || structure[0][0].length == 0)
		{
			String ret = "Invalid multiblock: ";
			for (Object tmp : key)
			{
			    ret += tmp + ", ";
			}
			throw new RuntimeException(ret);
		}
		
		mb = new Object[height][zD][xD];
		mbLong = new Object[height * zD * xD];

		Map<Character, Object> itemMap = new HashMap<Character, Object>();
		
		int index = height * zD;
		for (; index < key.length; index += 2)
		{
			Character chr = (Character) key[index];
			Object in = key[index + 1];
			
			if (in instanceof ItemStack)
			{
				itemMap.put(chr, ((ItemStack)in).copy());
			}
			else if (in instanceof Item)
			{
				itemMap.put(chr, new ItemStack((Item)in));
			}
			else if (in instanceof IMultiblockComparator)
			{
				itemMap.put(chr, in);
			}
			else if (in instanceof Block)
			{
				if (in == Blocks.AIR)
				{
					itemMap.put(chr, "AIR");
				}
				else
				{
					itemMap.put(chr, new ItemStack((Block)in, 1, OreDictionary.WILDCARD_VALUE));
				}
			}
			else if (in instanceof String)
			{
				itemMap.put(chr, OreDictionary.getOres((String)in));
			}
			else
			{
				String ret = "Invalid multiblock: ";
				for (Object tmp : key)
				{
				    ret += tmp + ", ";
				}
				throw new RuntimeException(ret);
			}
		}
		
		
		for (int y = 0; y < structure.length; y++)
		{
			
			Character[][] layer = (Character[][]) structure[y];
			
			for (int z = 0; z < layer.length; z++)
			{
				Character[] row = layer[z];
				
				String s = "";
				
				for (int x = 0; x < row.length; x++)
				{
					Character value = row[x];
					
					Object val = null;
					
					if (value != ' ')
					{
						val = itemMap.get(value);
					}
		
					
					mb[y][z][x] = val;
					mbLong[x + z * xD + y * xD * zD] = val;
					s = s + value;
				}
				
				System.out.println(s);
			}
			System.out.println();
		}
		multiblocks.put(name, this);

		
	}
	
	public static Multiblock get(String str)
	{
		return multiblocks.get(str);
	}
	
	public String getTag()
	{
		return name;
	}
	
	public AxisAlignedBB getSpecialBounds(int index)
	{
		return null;
	}
	
	private boolean isEqual(Object value, IBlockAccess iba, BlockPos blockPos)
	{
		if (value == null)
		{
			return true;
		}
		
		if (value instanceof IMultiblockComparator)
		{
			return ((IMultiblockComparator) value).isEqual(iba, blockPos);
		}
		
		if (value.equals("AIR"))
		{
			return iba.isAirBlock(blockPos) || (MultipartHelper.getPartContainer(iba, blockPos) != null && MultipartHelper.getPartContainer(iba, blockPos).getParts().size() == 0);
		}
		
		
		IBlockState state = iba.getBlockState(blockPos);
		
		List<ItemStack> possibilities = new ArrayList<ItemStack>();
		
		if (value instanceof List)
		{
			possibilities = (List<ItemStack>) value;
		}
		else if (value instanceof ItemStack)
		{
			possibilities.add((ItemStack) value);
		}
		else
		{
			throw new RuntimeException("Non-itemstack value " + value.toString());
		}
		
		int meta = state.getBlock().getMetaFromState(state);

		for (ItemStack pos : possibilities)
		{
			int stackMeta = pos.getMetadata();
			if (!(stackMeta == OreDictionary.WILDCARD_VALUE || stackMeta == meta))
			{
				return false;
			}
			
			if (pos.getItem() == null)
			{
				throw new RuntimeException("Null item " + pos);
			}
			
			Block block = Block.getBlockFromItem(pos.getItem());
			
			if (block == null)
			{
				throw new RuntimeException("Null block " + pos.getItem());
			}
			
			return block == state.getBlock();
		}
		return false;
	}
	
	public boolean multiblockExists(World iba, BlockPos pos)
	{
		
		return 
				multiblockExists(iba, pos, false, false, false) || multiblockExists(iba, pos, true, false, false)
				|| multiblockExists(iba, pos, false, true, false) || multiblockExists(iba, pos, true, true, false)
				|| multiblockExists(iba, pos, false, false, true) || multiblockExists(iba, pos, true, false, true)
				|| multiblockExists(iba, pos, false, true, true) || multiblockExists(iba, pos, true, true, true);
	}
	
	public boolean multiblockExists(World iba, BlockPos pos, boolean swapXZ, boolean flipXo, boolean flipZo)
	{
		pos = pos.add(swapXZ ? -centerZ : -centerX, -centerY, swapXZ ? -centerX : -centerZ);

		boolean temp = flipXo;
		boolean flipX = swapXZ ? flipZo : flipXo;
		boolean flipZ = swapXZ ? temp : flipZo;
		for (int y = 0; y < ySize; y++)
		{
			for (int fz = 0; fz < zSize; fz++)
			{
				for (int fx = 0; fx < xSize; fx++)
				{
					int z = swapXZ ? fx : fz;
					int x = swapXZ ? fz : fx;
					
					if (!isEqual(mb[y][fz][fx], iba, pos.add(flipX ? -x : x, y, flipZ ? -z : z)))
					{

						return false;
					}
				}
			}
		}
		createMultiblock(iba, pos, swapXZ, flipXo, flipZo);
		return true;
	}
	
	public void destroyMultiblock(World world, BlockPos centerPos, boolean swapXZ, boolean flipXo, boolean flipZo)
	{
		BlockPos pos = centerPos.add(swapXZ ? -centerZ : -centerX, -centerY, swapXZ ? -centerX : -centerZ);

		
		boolean temp = flipXo;
		boolean flipX = swapXZ ? flipZo : flipXo;
		boolean flipZ = swapXZ ? temp : flipZo;
		

		for (int y = 0; y < ySize; y++)
		{
			for (int fz = 0; fz < zSize; fz++)
			{
				for (int fx = 0; fx < xSize; fx++)
				{
					int z = swapXZ ? fx : fz;
					int x = swapXZ ? fz : fx;

					if ((y != centerY || fz != centerZ || fx != centerX) && mb[y][fz][fx] != null)
					{
						BlockPos pos2 = pos.add(flipX ? -x : x, y, flipZ ? -z : z);

						TileEntity te = world.getTileEntity(pos2);
						if (te != null && te instanceof TileEntityMultiblock)
						{
							((TileEntityMultiblock) te).turnBack();
						}
					}
				}
			}
		}
	}

	private void createMultiblock(World world, BlockPos pos, boolean swapXZ, boolean flipXo, boolean flipZo)
	{
		boolean temp = flipXo;
		boolean flipX = swapXZ ? flipZo : flipXo;
		boolean flipZ = swapXZ ? temp : flipZo;
		
		int cZ = swapXZ ? centerX : centerZ;
		int cX = swapXZ ? centerZ : centerX;
		BlockPos centerPos = pos.add(cX, centerY, cZ);

		for (int y = 0; y < ySize; y++)
		{
			for (int fz = 0; fz < zSize; fz++)
			{
				for (int fx = 0; fx < xSize; fx++)
				{
					int z = swapXZ ? fx : fz;
					int x = swapXZ ? fz : fx;

					if ((y != centerY || fz != centerZ || fx != centerX) && mb[y][fz][fx] != null)
					{
				
						BlockPos pos2 = pos.add(flipX ? -x : x, y, flipZ ? -z : z);
						IBlockState state = world.getBlockState(pos2);
						List<ItemStack> drops = state.getBlock().getDrops(world, pos2, state, 0);
						
						IMultipartContainer mpc = MultipartHelper.getPartContainer(world, pos2);
						NBTTagCompound data = null;
						
						
						if (mpc != null && mpc instanceof TileMultipartContainer)
						{
							mpc = ((TileMultipartContainer) mpc).getPartContainer();	
						}

						if (mpc != null && mpc instanceof MultipartContainer)
						{
							data = ((MultipartContainer) mpc).writeToNBT(new NBTTagCompound());
						}
						if (mpc != null)
						{
							for (IMultipart part : mpc.getParts())
							{
								mpc.removePart(part);
							}
						}
						
						world.setBlockState(pos2, SprocketsBlocks.mbBlock.getDefaultState());
						((TileEntityMultiblock) world.getTileEntity(pos2)).init(this, state, x + z * zSize + y * zSize * ySize, centerPos, drops);
				
						if (mpc != null && mpc instanceof MultipartContainer)
						{
							((TileEntityMultiblock) world.getTileEntity(pos2)).setData(data);
						}
				

					}
				
				}
			}
		}
		setCenter(world, centerPos);
		((IMultiblockBrain) world.getTileEntity(centerPos)).addMultiblock(this, swapXZ, flipXo, flipZo);

	}
	
	public void setCenter(World world, BlockPos pos) {};

	public float getHardness()
	{
		return 1.0F;
	}

	public Set<Tuple<Vec3i, PartSlot>> multipartCisConnections(int pos, BlockPos center, World world)
	{
		return new HashSet<Tuple<Vec3i, PartSlot>>();
	}

	public Set<Tuple<Vec3i, PartSlot>> multipartTransConnections(int pos, BlockPos center, World world)
	{
		return new HashSet<Tuple<Vec3i, PartSlot>>();
	}

	public Set<Vec3i> cisConnections(int pos, BlockPos center, World world)
	{
		return new HashSet<Vec3i>();
	}

	public Set<Vec3i> transConnections(int pos, BlockPos center, World world)
	{
		return new HashSet<Vec3i>();
	}
	
	
	
}
