package flaxbeard.sprockets.blocks;

import flaxbeard.sprockets.blocks.tiles.TileEntityCreativeEngine;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockCreativeEngine extends BlockRedstoneEngine
{
	public BlockCreativeEngine(String name, Material material, float hardness, float resistance)
	{
		super(name, material, hardness, resistance);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileEntityCreativeEngine();
    }
}
