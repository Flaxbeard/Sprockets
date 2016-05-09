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
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import flaxbeard.sprockets.api.IMechanicalConduit;
import flaxbeard.sprockets.api.IWrenchable;
import flaxbeard.sprockets.api.network.MechanicalNetworkHelper;
import flaxbeard.sprockets.api.network.MechanicalNetworkRegistry;
import flaxbeard.sprockets.blocks.tiles.TileEntityRedstoneEngine;
import flaxbeard.sprockets.blocks.tiles.TileEntityWindmillSmall;

public class BlockRedstoneEngine extends BlockSprocketBase implements ITileEntityProvider, IWrenchable
{
	public static final PropertyDirection FACING = PropertyDirection.create("facing");
	public static final PropertyBool POWERED = PropertyBool.create("powered");
	
	public BlockRedstoneEngine(String name, Material material, float hardness, float resistance)
	{
		super(material, name);
		this.setHardness(hardness);
		this.setResistance(resistance);
		this.setLightOpacity(0);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(POWERED, false));
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileEntityRedstoneEngine();
    }
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.MODEL;
	}
	
	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	{
		return this.getDefaultState().withProperty(FACING, facing.getOpposite());
	}
	
	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
	{
		if (!worldIn.isRemote)
		{
			if (!worldIn.isBlockPowered(pos) && state.getValue(POWERED))
			{
				TileEntityRedstoneEngine te = (TileEntityRedstoneEngine) worldIn.getTileEntity(pos);
				boolean reversed = te.directionFlipped;
				worldIn.setBlockState(pos, state.withProperty(POWERED, false), 2);
				
				te = (TileEntityRedstoneEngine) worldIn.getTileEntity(pos);
				te.isOn = 0;
				te.directionFlipped = reversed;
				te.markDirty();
			}
			else if (worldIn.isBlockPowered(pos) && !state.getValue(POWERED)) 
			{
				TileEntityRedstoneEngine te = (TileEntityRedstoneEngine) worldIn.getTileEntity(pos);
				boolean reversed = te.directionFlipped;
				
				worldIn.setBlockState(pos, state.withProperty(POWERED, true), 2);
				
				te = (TileEntityRedstoneEngine) worldIn.getTileEntity(pos);
				te.isOn = 1;
				te.directionFlipped = reversed;
				te.markDirty();
			}
		}
	}
	
	@Override
	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
	{
		if (!worldIn.isRemote)
		{
			if (!worldIn.isBlockPowered(pos) && state.getValue(POWERED))
			{
				TileEntityRedstoneEngine te = (TileEntityRedstoneEngine) worldIn.getTileEntity(pos);
				boolean reversed = te.directionFlipped;
				worldIn.setBlockState(pos, state.withProperty(POWERED, false), 2);
				
				te = (TileEntityRedstoneEngine) worldIn.getTileEntity(pos);
				te.isOn = 0;
				te.directionFlipped = reversed;
				te.markDirty();
			}
			else if (worldIn.isBlockPowered(pos) && !state.getValue(POWERED)) 
			{
				TileEntityRedstoneEngine te = (TileEntityRedstoneEngine) worldIn.getTileEntity(pos);
				boolean reversed = te.directionFlipped;
				
				worldIn.setBlockState(pos, state.withProperty(POWERED, true), 2);
				
				te = (TileEntityRedstoneEngine) worldIn.getTileEntity(pos);
				te.isOn = 1;
				te.directionFlipped = reversed;
				te.markDirty();
			}
		}
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
	
	    return this.getDefaultState().withProperty(FACING, enumfacing).withProperty(POWERED, conTop);
	}
	
	/**
	 * Convert the BlockState into the correct metadata value
	 */
	public int getMetaFromState(IBlockState state)
	{
		if (state.getBlock() == this)
		{
			return ((EnumFacing)state.getValue(FACING)).getIndex() + ((Boolean) state.getValue(POWERED) ? 6 : 0);
		}
		else
		{
			return 0;
		}
	}
	
	protected BlockStateContainer createBlockState()
	{
	    return new BlockStateContainer(this, new IProperty[] {FACING, POWERED});
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
				TileEntityRedstoneEngine te = (TileEntityRedstoneEngine) world.getTileEntity(pos);

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
