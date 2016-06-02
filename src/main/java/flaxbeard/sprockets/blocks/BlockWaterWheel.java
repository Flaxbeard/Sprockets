package flaxbeard.sprockets.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import flaxbeard.sprockets.blocks.tiles.TileEntityWaterWheel;
import flaxbeard.sprockets.blocks.tiles.TileEntityWaterWheelComponent;
import flaxbeard.sprockets.lib.LibConstants;

public class BlockWaterWheel extends BlockSprocketBase implements ITileEntityProvider
{
	
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	
	public BlockWaterWheel(String name, Material material, float hardness, float resistance)
	{
		super(material, name);
		this.setHardness(hardness);
		this.setResistance(resistance);
		this.setLightOpacity(0);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
	}
	
	@Override
	public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side)
	{
		
		return canThisFit(worldIn, pos, side) && super.canPlaceBlockOnSide(worldIn, pos, side);
	}
	
	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn)
    {
		TileEntityWaterWheel te = (TileEntityWaterWheel) world.getTileEntity(pos);
		if (te != null)
		{
			TileEntityWaterWheel wheel = (TileEntityWaterWheel) te;
			int facing = state.getValue(FACING).ordinal();
			BlockPos posRight = pos.add(facing <= 3 ? 0 : 1, 0, facing <= 3 ? 1 : 0);
			BlockPos posLeft = pos.add(facing <= 3 ? 0 : -1, 0, facing <= 3 ? -1 : 0);
			
			boolean newLeft = world.getBlockState(posLeft).getBlock() == this;
			boolean newRight = world.getBlockState(posRight).getBlock() == this;
			boolean change = (newLeft != wheel.connLeft || newRight != wheel.connRight);
			
			if (change)
			{
				wheel.connLeft = newLeft;
				wheel.connRight = newRight;
				world.notifyBlockUpdate(pos, state, state, 2);
			}
		}
		
    }
	
	private boolean canThisFit(World worldIn, BlockPos pos, EnumFacing side)
	{
		int facing = side.ordinal();
		for (int w = -LibConstants.WATER_WHEEL_RAIDUS; w <= LibConstants.WATER_WHEEL_RAIDUS; w++)
		{
			for (int y = -LibConstants.WATER_WHEEL_RAIDUS; y <= LibConstants.WATER_WHEEL_RAIDUS; y++)
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
	public void onBlockAdded(World world, BlockPos pos, IBlockState state)
    {
		super.onBlockAdded(world, pos, state);
		int facing = state.getValue(FACING).ordinal();
		if (!world.isRemote)
		{
			for (int w = -LibConstants.WATER_WHEEL_RAIDUS; w <= LibConstants.WATER_WHEEL_RAIDUS; w++)
			{
				for (int y = -LibConstants.WATER_WHEEL_RAIDUS; y <= LibConstants.WATER_WHEEL_RAIDUS; y++)
				{
					if (y != 0 || w != 0)
					{
						BlockPos pos2 = pos.add(facing <= 3 ? w : 0, y, facing <= 3 ? 0 : w);
						world.setBlockState(pos2, SprocketsBlocks.waterwheelComponent.getDefaultState(), 2);
						((TileEntityWaterWheelComponent) world.getTileEntity(pos2)).setCenter(pos);
					}
				}
			}
		}
		BlockPos posRight = pos.add(facing <= 3 ? 0 : 1, 0, facing <= 3 ? 1 : 0);
		BlockPos posLeft = pos.add(facing <= 3 ? 0 : -1, 0, facing <= 3 ? -1 : 0);
		
		TileEntityWaterWheel wheel = (TileEntityWaterWheel) world.getTileEntity(pos);

		wheel.connLeft = world.getBlockState(posLeft).getBlock() == this;
		wheel.connRight = world.getBlockState(posRight).getBlock() == this;
		world.setBlockState(pos, state);
    }



	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileEntityWaterWheel();
    }
	
	
	@Override
	public boolean isSolid()
	{
		return false;
	}
	
	@Override
	public boolean isSideSolid(IBlockState base, IBlockAccess worldIn, BlockPos pos, EnumFacing side)
    {
        return false;
    }
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}
	
	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	{
		return this.getDefaultState().withProperty(FACING, facing);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
	{
	    return new ItemStack(Item.getItemFromBlock(SprocketsBlocks.waterwheel));
	}

	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{

	    EnumFacing enumfacing = EnumFacing.getFront(meta);
	
	    if (enumfacing.getAxis() == EnumFacing.Axis.Y)
	    {
	        enumfacing = EnumFacing.NORTH;
	    }
	
	    return this.getDefaultState().withProperty(FACING, enumfacing);
	}
	
	@Override
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
	
	@Override
	protected BlockStateContainer createBlockState()
	{
	    return new BlockStateContainer(this, new IProperty[] {FACING});
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{ 
		TileEntity tileentity = worldIn.getTileEntity(pos);

		if (tileentity instanceof TileEntityWaterWheel && !worldIn.isRemote)
		{
    		((TileEntityWaterWheel) tileentity).destroy();
		}
		super.breakBlock(worldIn, pos, state);

	}
}
