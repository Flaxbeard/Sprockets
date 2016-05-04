package flaxbeard.sprockets.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;
import flaxbeard.sprockets.Sprockets;

public class BlockSprocketBase extends Block
{
	public final String name;
	
	public BlockSprocketBase(Material material, String name)
	{
		super(material);
		GameRegistry.registerBlock(this, name);
		this.setUnlocalizedName(Sprockets.MODID + "." + name);
		this.setCreativeTab(Sprockets.creativeTab);
		this.name = name;
	}
	
	public BlockSprocketBase(Material material, String name, Class<? extends ItemBlock> ib)
	{
		super(material);
		GameRegistry.registerBlock(this, ib, name);
		this.setUnlocalizedName(Sprockets.MODID + "." + name);
		this.setCreativeTab(Sprockets.creativeTab);
		this.name = name;
	}


	
}
