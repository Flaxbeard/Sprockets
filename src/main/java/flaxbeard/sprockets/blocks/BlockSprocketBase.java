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
		this.setRegistryName(name);
		GameRegistry.register(this);
		ItemBlock ib = new ItemBlock(this);
		ib.setRegistryName(name);
		GameRegistry.register(ib);
		this.setUnlocalizedName(Sprockets.MODID + "." + name);
		this.setCreativeTab(Sprockets.creativeTab);
		this.name = name;
	}


	
}
