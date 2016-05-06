package flaxbeard.sprockets.client;

import mcmultipart.client.multipart.MultipartRegistryClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import flaxbeard.sprockets.Sprockets;
import flaxbeard.sprockets.blocks.BlockSprocketBase;
import flaxbeard.sprockets.blocks.SprocketsBlocks;
import flaxbeard.sprockets.blocks.tiles.TileEntityWindmill;
import flaxbeard.sprockets.blocks.tiles.TileEntityWindmillSmall;
import flaxbeard.sprockets.client.render.tile.RenderPartAxle;
import flaxbeard.sprockets.client.render.tile.RenderPartBigSprocket;
import flaxbeard.sprockets.client.render.tile.RenderPartSprocket;
import flaxbeard.sprockets.client.render.tile.TileEntityWindmillRenderer;
import flaxbeard.sprockets.client.render.tile.TileEntityWindmillSmallRenderer;
import flaxbeard.sprockets.common.CommonProxy;
import flaxbeard.sprockets.items.ItemSprocketBase;
import flaxbeard.sprockets.items.SprocketsItems;
import flaxbeard.sprockets.multiparts.PartAxle;
import flaxbeard.sprockets.multiparts.PartBigSprocketCenter;
import flaxbeard.sprockets.multiparts.PartSprocket;
import flaxbeard.sprockets.multiparts.SprocketsMultiparts;
import flaxbeard.sprockets.multiparts.items.ItemSprocketMultipart;

public class ClientProxy extends CommonProxy
{
	
	@Override
	public void preInit()
	{
		super.preInit();
		
		ItemSprocketMultipart part = SprocketsMultiparts.sprocket;
		for (int i = 0; i < part.subnames.length; i++)
		{
			registerItemRendersPre(part, part.subnames[i]);
		}
		part = SprocketsMultiparts.axle;
		for (int i = 0; i < part.subnames.length; i++)
		{
			registerItemRendersPre(part, part.subnames[i]);
		}
		
		part = SprocketsMultiparts.bigSprocket;
		for (int i = 0; i < part.subnames.length; i++)
		{
			registerItemRendersPre(part, part.subnames[i]);
		}
	}
	
	@Override
	public void init()
	{
		super.init();
		registerRenders(SprocketsBlocks.creativeMotor);
		
		
		MultipartRegistryClient.bindMultipartSpecialRenderer(PartSprocket.class, new RenderPartSprocket());
		MultipartRegistryClient.bindMultipartSpecialRenderer(PartBigSprocketCenter.class, new RenderPartBigSprocket());
		MultipartRegistryClient.bindMultipartSpecialRenderer(PartAxle.class, new RenderPartAxle());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWindmillSmall.class, new TileEntityWindmillSmallRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWindmill.class, new TileEntityWindmillRenderer());
		
		
		ItemSprocketMultipart part = SprocketsMultiparts.sprocket;
		for (int i = 0; i < part.subnames.length; i++)
		{
			registerItemRenders(part, i, part.subnames[i]);
		}
		part = SprocketsMultiparts.axle;
		for (int i = 0; i < part.subnames.length; i++)
		{
			registerItemRenders(part, i, part.subnames[i]);
		}
		part = SprocketsMultiparts.bigSprocket;
		for (int i = 0; i < part.subnames.length; i++)
		{
			registerItemRenders(part, i, part.subnames[i]);
		}
		
		registerItemRenders(SprocketsItems.wrench);
		registerItemRenders(SprocketsItems.crank);
		registerItemRenders(SprocketsItems.gyrometer);
		
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(SprocketsBlocks.windmillSmall), 0, TileEntityWindmillSmall.class);
		registerRenders(SprocketsBlocks.windmillSmall);
		
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(SprocketsBlocks.windmill), 0, TileEntityWindmill.class);
		registerRenders(SprocketsBlocks.windmill);
	}
	
	@Override
	public void postInit()
	{
		super.postInit();
	}
	
	
	private void registerRenders(BlockSprocketBase block)
	{
		Item item = Item.getItemFromBlock(block);
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 
				0, new ModelResourceLocation(Sprockets.MODID + ":" + block.name, "inventory"));
	}
	
	private void registerItemRenders(ItemSprocketMultipart item, int meta, String mod)
	{
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 
				meta, new ModelResourceLocation(Sprockets.MODID + ":" + item.name + "_" + mod, "inventory"));
	}
	
	private void registerItemRendersPre(ItemSprocketMultipart item, String mod)
	{
		ModelBakery.registerItemVariants(item, new ResourceLocation(Sprockets.MODID + ":" + item.name + "_" + mod));
	}
	
	private void registerItemRenders(ItemSprocketMultipart item)
	{
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 
				0, new ModelResourceLocation(Sprockets.MODID + ":" + item.name, "inventory"));
	}
	
	private void registerItemRenders(ItemSprocketBase item, int meta, String mod)
	{
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 
				meta, new ModelResourceLocation(Sprockets.MODID + ":" + item.name + "_" + mod, "inventory"));
	}
	
	private void registerItemRendersPre(ItemSprocketBase item, String mod)
	{
		ModelBakery.registerItemVariants(item, new ResourceLocation(Sprockets.MODID + ":" + item.name + "_" + mod));
	}
	
	private void registerItemRenders(ItemSprocketBase item)
	{
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 
				0, new ModelResourceLocation(Sprockets.MODID + ":" + item.name, "inventory"));
	}
}
