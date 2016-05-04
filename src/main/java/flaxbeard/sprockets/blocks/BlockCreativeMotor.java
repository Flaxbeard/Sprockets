package flaxbeard.sprockets.blocks;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
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
	
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
    {
		TileEntityCreativeMotor motor = (TileEntityCreativeMotor) worldIn.getTileEntity(pos);
		return false;
    }
	
	
	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}
	
	@Override
	public int getRenderType()
	{
		return 0;
	}
	
}
