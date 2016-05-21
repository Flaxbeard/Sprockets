package flaxbeard.sprockets.blocks;

import java.util.Set;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import flaxbeard.sprockets.api.IMechanicalConduit;
import flaxbeard.sprockets.api.network.MechanicalNetworkHelper;
import flaxbeard.sprockets.api.network.MechanicalNetworkRegistry;
import flaxbeard.sprockets.api.tool.IWrenchable;
import flaxbeard.sprockets.blocks.tiles.TileEntityTorsionSpring;
import flaxbeard.sprockets.multiparts.SprocketsMultiparts;

public class BlockTorsionSpring extends BlockSprocketBase implements ITileEntityProvider, IWrenchable
{
	public static final PropertyDirection FACING = PropertyDirection.create("facing");
	
	private static final AxisAlignedBB[] BOUNDS;
	static
	{
		BOUNDS = new AxisAlignedBB[6];
		for (int side = 0; side < 6; side++)
		{
			BOUNDS[side] = SprocketsMultiparts.rotateAxis(side, 2F / 16F, 1F / 16F, 2F / 16F, 14F / 16F, 16F / 16F, 14F / 16F);
		}		
		
		
	}
	
	public BlockTorsionSpring(String name, Material material, float hardness, float resistance)
	{
		super(material, name);
		this.setHardness(hardness);
		this.setResistance(resistance);
		this.setLightOpacity(0);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return BOUNDS[((EnumFacing)state.getValue(FACING)).getIndex()];
    }
	

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileEntityTorsionSpring();
    }
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	{
		return this.getDefaultState().withProperty(FACING, facing.getOpposite());
	}

	public IBlockState getStateFromMeta(int meta)
	{
	    EnumFacing enumfacing = EnumFacing.getFront(meta);

	    return this.getDefaultState().withProperty(FACING, enumfacing);
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public boolean isSideSolid(IBlockState base, IBlockAccess worldIn, BlockPos pos, EnumFacing side)
    {
        return false;
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
			if (side != state.getValue(FACING))
			{
				TileEntityTorsionSpring te = (TileEntityTorsionSpring) world.getTileEntity(pos);

				Set<IMechanicalConduit> neighbors = MechanicalNetworkHelper.getConnectedConduits(te);
				te.getNetwork().removeConduitTotal(te, neighbors);
				te.setNetwork(null);
				float progress = te.progress;
				float signum = te.signum;
				MechanicalNetworkRegistry.newOrJoin(te);
				
				world.setBlockState(pos, state.withProperty(FACING, side));
				te = (TileEntityTorsionSpring) world.getTileEntity(pos);
				te.facing = side.ordinal();
				te.signum = signum;
				te.progress = progress;
			}
		}
		return false;
	}
}
