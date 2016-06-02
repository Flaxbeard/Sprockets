package flaxbeard.sprockets.blocks;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.InventoryHelper;
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
import flaxbeard.sprockets.blocks.tiles.TileEntityMillstone;

public class BlockMillstone extends BlockSprocketBase implements ITileEntityProvider
{
	public static final PropertyBool MULTIBLOCK = PropertyBool.create("multiblock");

	public BlockMillstone(String name, Material material, float hardness, float resistance)
	{
		super(material, name);
		this.setHardness(hardness);
		this.setResistance(resistance);
		this.setLightOpacity(0);
		this.setDefaultState(this.blockState.getBaseState().withProperty(MULTIBLOCK, false));
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileEntityMillstone();
    }

	@SideOnly(Side.CLIENT)
	@Override
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
	{
	    return new ItemStack(Item.getItemFromBlock(SprocketsBlocks.millstone));
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.MODEL;
	}
	
	@Override
	public boolean isSolid()
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

		if (tileentity instanceof TileEntityMillstone && !worldIn.isRemote)
		{
    		TileEntityMillstone ms = (TileEntityMillstone) tileentity;
    		
    		for (int i = 0; i < ms.slots.getSlots(); i++)
    		{
    			ItemStack stack = ms.slots.getStackInSlot(i);
    			if (stack != null)
    			{
    				InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), stack);
    			}
    		}
		}
		super.breakBlock(worldIn, pos, state);

	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		boolean multi = false;
		if (meta > 0)
		{
			multi = true;
		}
	    return this.getDefaultState().withProperty(MULTIBLOCK, multi);
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		if (state.getBlock() == this)
		{
			return ((Boolean) state.getValue(MULTIBLOCK) ? 1 : 0);
		}
		else
		{
			return 0;
		}
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
	    return new BlockStateContainer(this, new IProperty[] {MULTIBLOCK});
	}


}
