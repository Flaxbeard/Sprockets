package flaxbeard.sprockets.blocks;

import java.util.HashSet;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import flaxbeard.sprockets.api.IMechanicalConduit;
import flaxbeard.sprockets.api.IWrenchable;
import flaxbeard.sprockets.api.network.MechanicalNetworkHelper;
import flaxbeard.sprockets.api.network.MechanicalNetworkRegistry;
import flaxbeard.sprockets.blocks.tiles.TileEntityWindmillSmall;

public class BlockWindmillSmall extends BlockSprocketBase implements ITileEntityProvider, IWrenchable
{
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	public static final PropertyBool CONNECT_TOP = PropertyBool.create("connecttop");
	
	public BlockWindmillSmall(String name, Material material, float hardness, float resistance)
	{
		super(material, name);
		this.setHardness(hardness);
		this.setResistance(resistance);
		this.setLightOpacity(0);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(CONNECT_TOP, false));
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileEntityWindmillSmall();
	}
	
	@Override
	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
    {
		TileEntityWindmillSmall te = (TileEntityWindmillSmall) worldIn.getTileEntity(pos);
		if (te != null)
		{
			boolean ct = worldIn.getBlockState(pos.add(0, 1, 0)).getBlock() != Blocks.air;
			boolean flipped = te.directionFlipped;
			worldIn.setBlockState(pos, state.withProperty(CONNECT_TOP, ct));
			//System.out.println(te.directionFlipped);
			
			
			te = (TileEntityWindmillSmall) worldIn.getTileEntity(pos);
			te.connectedToTop = (byte) (ct ? 1 : 0);

			te.markDirty();
			// TODO test
			//te.directionFlipped = flipped;
			//worldIn.markBlockForUpdate(pos);
		}
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
	    return Item.getItemFromBlock(SprocketsBlocks.windmillSmall);
	}
	
	
	public IBlockState getStateFromMeta(int meta)
	{
		boolean conTop = false;
		if (meta >= 6)
		{
			conTop = true;
			meta -= 6;
		}
	    EnumFacing enumfacing = EnumFacing.getFront(meta);
	
	    if (enumfacing.getAxis() == EnumFacing.Axis.Y)
	    {
	        enumfacing = EnumFacing.NORTH;
	    }
	
	    return this.getDefaultState().withProperty(FACING, enumfacing).withProperty(CONNECT_TOP, conTop);
	}
	
	/**
	 * Convert the BlockState into the correct metadata value
	 */
	public int getMetaFromState(IBlockState state)
	{
		if (state.getBlock() == this)
		{
			return ((EnumFacing)state.getValue(FACING)).getIndex() + ((Boolean) state.getValue(CONNECT_TOP) ? 6 : 0);
		}
		else
		{
			return 0;
		}
	}
	
	protected BlockStateContainer createBlockState()
	{
	    return new BlockStateContainer(this, new IProperty[] {FACING, CONNECT_TOP});
	}

	@Override
	public boolean wrench(EntityPlayer player, World world, BlockPos pos, IBlockState state,
			EnumFacing side)
	{
		if (!player.isSneaking())
		{
			if (side.ordinal() >= 2 && side != state.getValue(FACING))
			{
				TileEntityWindmillSmall te = (TileEntityWindmillSmall) world.getTileEntity(pos);

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
