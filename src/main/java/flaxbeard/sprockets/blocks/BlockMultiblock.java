package flaxbeard.sprockets.blocks;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import flaxbeard.sprockets.api.Multiblock;
import flaxbeard.sprockets.blocks.tiles.TileEntityMultiblock;

public class BlockMultiblock extends BlockSprocketBase implements ITileEntityProvider
{
	
	public BlockMultiblock()
	{
		super(Material.ROCK, "multiblock", false);
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		System.out.println(((TileEntityMultiblock) worldIn.getTileEntity(pos)).pos);
		return super.onBlockActivated(worldIn, pos, state, playerIn, hand, heldItem, side, hitX, hitY, hitZ);
	}
	

	@Override
	public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos)
	{
		Multiblock mb = getMultiblock(worldIn, pos);;
		if (mb != null)
		{
			return mb.getHardness();
		}
		return super.getBlockHardness(blockState, worldIn, pos);
	}

	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos)
	{
		Multiblock mb = getMultiblock(worldIn, pos);
		int index = getIndex(worldIn, pos);
		
		
		if (mb != null)
		{
			AxisAlignedBB special = mb.getSpecialBounds(index);
			if (special != null)
			{
				return special;
			}
		}
		IBlockState stat = getState(worldIn, pos);
		if (stat != null && stat.getBlock() != this)
		{
			return stat.getCollisionBoundingBox(worldIn, pos);
		}
		return super.getCollisionBoundingBox(blockState, worldIn, pos);
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		Multiblock mb = getMultiblock(source, pos);
		int index = getIndex(source, pos);
		if (mb != null)
		{
			AxisAlignedBB special = mb.getSpecialBounds(index);
			if (special != null)
			{
				return special;
			}
		}
		IBlockState stat = getState(source, pos);
		if (stat != null && stat.getBlock() != this)
		{
			return stat.getBoundingBox(source, pos);
		}
		return super.getBoundingBox(state, source, pos);
	}
	
	public int getIndex(IBlockAccess world, BlockPos pos)
	{
		TileEntity te = world.getTileEntity(pos);
		if (te != null && te instanceof TileEntityMultiblock)
		{
			return ((TileEntityMultiblock) te).getIndex();
		}
		return 0;
	}
	
	public IBlockState getState(IBlockAccess world, BlockPos pos)
	{
		TileEntity te = world.getTileEntity(pos);
		if (te != null && te instanceof TileEntityMultiblock)
		{
			IBlockState state = ((TileEntityMultiblock) te).getBlockState();
			if (state != null)
			{
				return state;
			}
		}
		return null;
	}
	
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos)
	{
		TileEntity te = world.getTileEntity(pos);
		if (te != null && te instanceof TileEntityMultiblock)
		{
			TileEntityMultiblock mb = ((TileEntityMultiblock) te);
			
			if (!mb.isTurning)
			{
				return mb.getDrops();
			}
		}
		return new ArrayList<ItemStack>();
	}
	
	
	public Multiblock getMultiblock(IBlockAccess world, BlockPos pos)
	{
		TileEntity te = world.getTileEntity(pos);
		if (te != null && te instanceof TileEntityMultiblock)
		{
			return ((TileEntityMultiblock) te).getMultiblock();
		}
		return null;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileEntityMultiblock();
	}

	@SideOnly(Side.CLIENT)
	@Override
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
	{
		return getState(worldIn, pos).getBlock().getItem(worldIn, pos, getState(worldIn, pos));
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.INVISIBLE;
	}



	@Override
	public boolean isSolid()
	{
		return false;
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{ 
		for (ItemStack stack : getDrops(worldIn, pos))
		{
			InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), stack);
		}
		super.breakBlock(worldIn, pos, state);
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
    {
		return new ArrayList<ItemStack>();
    }


}
