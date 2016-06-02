package flaxbeard.sprockets.blocks;

import java.util.Random;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
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
import flaxbeard.sprockets.blocks.tiles.TileEntityWaterWheelComponent;

public class BlockWaterWheelPart extends BlockSprocketBase implements ITileEntityProvider
{	
	public BlockWaterWheelPart(String name, Material material, float hardness, float resistance)
	{
		super(material, name, false);
		this.setHardness(hardness);
		this.setResistance(resistance);
		this.setLightOpacity(0);
	}
	

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileEntityWaterWheelComponent();
    }
	
	@Override
	public int quantityDropped(Random random)
    {
        return 0;
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
		return EnumBlockRenderType.INVISIBLE;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
	{
	    return new ItemStack(Item.getItemFromBlock(SprocketsBlocks.waterwheel));
	}
}
