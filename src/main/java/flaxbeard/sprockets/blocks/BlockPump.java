package flaxbeard.sprockets.blocks;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import flaxbeard.sprockets.blocks.tiles.TileEntityPump;

public class BlockPump extends BlockSprocketBase implements ITileEntityProvider
{

	public BlockPump(String name, Material material)
	{
		super(material, name);
	}

	@Override
	public boolean isSolid()
	{		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileEntityPump();
	}

}
