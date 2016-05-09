package flaxbeard.sprockets.blocks;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;

import mcmultipart.multipart.PartSlot;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import flaxbeard.sprockets.api.IMechanicalConduit;
import flaxbeard.sprockets.api.IWrenchable;
import flaxbeard.sprockets.api.network.MechanicalNetworkHelper;
import flaxbeard.sprockets.api.network.MechanicalNetworkRegistry;
import flaxbeard.sprockets.blocks.tiles.TileEntityWindmill;
import flaxbeard.sprockets.multiparts.SprocketsMultiparts;

public class BlockWindmill extends BlockSprocketBase implements ITileEntityProvider, IWrenchable
{
	
	private static final AxisAlignedBB[] BOUNDS;
	static
	{
		BOUNDS = new AxisAlignedBB[6];
		for (int side = 0; side < 6; side++)
		{
			BOUNDS[side] = SprocketsMultiparts.rotateAxis(side, 5F / 16F, 4F / 16F, 5F / 16F, 11F / 16F, 16F / 16F, 11F / 16F);
		}		
		
		
	}
	
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	
	public BlockWindmill(String name, Material material, float hardness, float resistance)
	{
		super(material, name);
		this.setHardness(hardness);
		this.setResistance(resistance);
		this.setLightOpacity(0);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
	}
	
	public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side)
	{
		
		return canThisFit(worldIn, pos, side) && super.canPlaceBlockOnSide(worldIn, pos, side);
	}
	
	private boolean canThisFit(World worldIn, BlockPos pos, EnumFacing side)
	{
		int facing = side.ordinal();
		for (int w = -3; w <= 3; w++)
		{
			for (int y = -3; y <= 3; y++)
			{
				if (w != 0 || y != 0)
				{
					BlockPos pos2 = pos.add(facing <= 3 ? w : 0, y, facing <= 3 ? 0 : w);
					
					IBlockState statePos = worldIn.getBlockState(pos2);
					
					// Check for obstructing blocks
					if (!statePos.getBlock().isReplaceable(worldIn, pos2))
					{
						return false;
					}
				}
			}
		}
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileEntityWindmill();
    }
	
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public boolean isSideSolid(IBlockState base, IBlockAccess worldIn, BlockPos pos, EnumFacing side)
    {
        return side == EnumFacing.UP || side == EnumFacing.DOWN;
    }
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}
	
	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	{
		return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
	}
	
	@Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
		boolean ct = worldIn.getBlockState(pos.add(0, 1, 0)).getBlock() != Blocks.air;
    	worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);
    }

	@SideOnly(Side.CLIENT)
	public Item getItem(World worldIn, BlockPos pos)
	{
	    return Item.getItemFromBlock(SprocketsBlocks.windmill);
	}
	
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return BOUNDS[((EnumFacing)state.getValue(FACING)).getIndex()];
    }
	
	
	public IBlockState getStateFromMeta(int meta)
	{

	    EnumFacing enumfacing = EnumFacing.getFront(meta);
	
	    if (enumfacing.getAxis() == EnumFacing.Axis.Y)
	    {
	        enumfacing = EnumFacing.NORTH;
	    }
	
	    return this.getDefaultState().withProperty(FACING, enumfacing);
	}
	
	/**
	 * Convert the BlockState into the correct metadata value
	 */
	public int getMetaFromState(IBlockState state)
	{
		if (state.getBlock() == this)
		{
			return ((EnumFacing)state.getValue(FACING)).getIndex();
		}
		else
		{
			return 0;
		}
	}
	
	protected BlockStateContainer createBlockState()
	{
	    return new BlockStateContainer(this, new IProperty[] {FACING});
	}

	@Override
	public boolean wrench(EntityPlayer player, World world, BlockPos pos, IBlockState state,
			EnumFacing side)
	{
		side = side.getOpposite();
		if (!player.isSneaking())
		{
			if (side.ordinal() >= 2 && canThisFit(world, pos, side) && side != state.getValue(FACING))
			{
				TileEntityWindmill te = (TileEntityWindmill) world.getTileEntity(pos);

				HashSet<IMechanicalConduit> neighbors = MechanicalNetworkHelper.getConnectedConduits(te);
				te.getNetwork().removeConduitTotal(te, neighbors);
				te.setNetwork(null);
				MechanicalNetworkRegistry.newOrJoin(te);
				
				world.setBlockState(pos, state.withProperty(FACING, side), 2);
				te.facing = side.ordinal();
				
			}
		}
		return false;
	}
}
