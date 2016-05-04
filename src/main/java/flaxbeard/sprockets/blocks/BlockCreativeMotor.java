package flaxbeard.sprockets.blocks;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import flaxbeard.sprockets.blocks.tiles.TileEntityCreativeMotor;

public class BlockCreativeMotor extends BlockSprocketBase implements ITileEntityProvider
{
	
	public BlockCreativeMotor(String name, Material material, float hardness, float resistance)
	{
		super(material, name,  ItemBlock.class);
		this.setHardness(hardness);
		this.setResistance(resistance);
		this.setLightOpacity(0);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileEntityCreativeMotor();
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
    {
		TileEntityCreativeMotor motor = (TileEntityCreativeMotor) worldIn.getTileEntity(pos);
		return false;
    }
	
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
	
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.INVISIBLE;
	}
	
}
