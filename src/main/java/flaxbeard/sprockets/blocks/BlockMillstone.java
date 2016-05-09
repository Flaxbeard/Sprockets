package flaxbeard.sprockets.blocks;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import flaxbeard.sprockets.blocks.tiles.TileEntityMillstone;

public class BlockMillstone extends BlockSprocketBase implements ITileEntityProvider
{
	
	public BlockMillstone(String name, Material material, float hardness, float resistance)
	{
		super(material, name);
		this.setHardness(hardness);
		this.setResistance(resistance);
		this.setLightOpacity(0);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileEntityMillstone();
    }

	@SideOnly(Side.CLIENT)
	public Item getItem(World worldIn, BlockPos pos)
	{
	    return Item.getItemFromBlock(SprocketsBlocks.millstone);
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.MODEL;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public boolean isSideSolid(IBlockState base, IBlockAccess worldIn, BlockPos pos, EnumFacing side)
    {
        return side == EnumFacing.DOWN;
    }
	
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{ 
		TileEntity tileentity = worldIn.getTileEntity(pos);

		if (tileentity instanceof TileEntityMillstone)
		{
    		InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntityMillstone) tileentity);
			
		}
		super.breakBlock(worldIn, pos, state);

	}


}
