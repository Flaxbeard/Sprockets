package flaxbeard.sprockets.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.common.registry.GameRegistry;
import flaxbeard.sprockets.Sprockets;

public abstract class BlockSprocketBase extends Block
{
	public final String name;
	
	public BlockSprocketBase(Material material, String name)
	{
		this(material, name, true, true);
	}
	
	public BlockSprocketBase(Material material, String name, boolean creativeTab)
	{
		this(material, name, creativeTab, true);
	}


	public BlockSprocketBase(Material material, String name, boolean creativeTab, boolean itemBlock)
	{
		super(material);
		this.setRegistryName(name);
		GameRegistry.register(this);
		if (itemBlock)
		{
			ItemBlock ib = new ItemBlock(this);
			ib.setRegistryName(name);
			GameRegistry.register(ib);
		}

		this.setUnlocalizedName(Sprockets.MODID + "." + name);
		if (creativeTab)
		{
			this.setCreativeTab(Sprockets.creativeTab);
		}
		this.name = name;
	}

	public abstract boolean isSolid();
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return isSolid();
	}

	@Override
	public boolean isFullCube(IBlockState state)
	{
		return isSolid();
	}
	
	
	@Override
	public boolean isBlockNormalCube(IBlockState state)
	{
		return isSolid();
	}
	
	@Override
	public boolean isVisuallyOpaque()
	{
		return isSolid();
	}
}
