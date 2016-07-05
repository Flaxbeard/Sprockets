package flaxbeard.sprockets.client;

import mcmultipart.client.multipart.MultipartRegistryClient;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import flaxbeard.sprockets.Sprockets;
import flaxbeard.sprockets.blocks.BlockSprocketBase;
import flaxbeard.sprockets.blocks.SprocketsBlocks;
import flaxbeard.sprockets.blocks.tiles.TileEntityBellows;
import flaxbeard.sprockets.blocks.tiles.TileEntityFrictionHeater;
import flaxbeard.sprockets.blocks.tiles.TileEntityMillstone;
import flaxbeard.sprockets.blocks.tiles.TileEntityStampMill;
import flaxbeard.sprockets.blocks.tiles.TileEntityTorsionSpring;
import flaxbeard.sprockets.blocks.tiles.TileEntityWaterWheel;
import flaxbeard.sprockets.blocks.tiles.TileEntityWindmill;
import flaxbeard.sprockets.blocks.tiles.TileEntityWindmillSmall;
import flaxbeard.sprockets.client.render.tile.RenderPartAxle;
import flaxbeard.sprockets.client.render.tile.RenderPartBigSprocket;
import flaxbeard.sprockets.client.render.tile.RenderPartClutch;
import flaxbeard.sprockets.client.render.tile.RenderPartFlywheel;
import flaxbeard.sprockets.client.render.tile.RenderPartFlywheelFrictionPad;
import flaxbeard.sprockets.client.render.tile.RenderPartScotchYoke;
import flaxbeard.sprockets.client.render.tile.RenderPartSprocket;
import flaxbeard.sprockets.client.render.tile.TileEntityBellowsRenderer;
import flaxbeard.sprockets.client.render.tile.TileEntityFrictionHeaterRenderer;
import flaxbeard.sprockets.client.render.tile.TileEntityMillstoneRenderer;
import flaxbeard.sprockets.client.render.tile.TileEntityStampMillRenderer;
import flaxbeard.sprockets.client.render.tile.TileEntityTorsionSpringRenderer;
import flaxbeard.sprockets.client.render.tile.TileEntityWaterWheelRenderer;
import flaxbeard.sprockets.client.render.tile.TileEntityWindmillRenderer;
import flaxbeard.sprockets.client.render.tile.TileEntityWindmillSmallRenderer;
import flaxbeard.sprockets.common.CommonProxy;
import flaxbeard.sprockets.items.ItemSprocketBase;
import flaxbeard.sprockets.items.SprocketsItems;
import flaxbeard.sprockets.multiparts.PartAxle;
import flaxbeard.sprockets.multiparts.PartAxleBelt;
import flaxbeard.sprockets.multiparts.PartAxleConveyorBelt;
import flaxbeard.sprockets.multiparts.PartBigSprocketCenter;
import flaxbeard.sprockets.multiparts.PartClutch;
import flaxbeard.sprockets.multiparts.PartFlywheel;
import flaxbeard.sprockets.multiparts.PartFlywheelFrictionPad;
import flaxbeard.sprockets.multiparts.PartLapisSprocket;
import flaxbeard.sprockets.multiparts.PartRedstoneSprocket;
import flaxbeard.sprockets.multiparts.PartScotchYoke;
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
		part = SprocketsMultiparts.redstoneSprocket;
		for (int i = 0; i < part.subnames.length; i++)
		{
			registerItemRendersPre(part, part.subnames[i]);
		}
		part = SprocketsMultiparts.lapisSprocket;
		for (int i = 0; i < part.subnames.length; i++)
		{
			registerItemRendersPre(part, part.subnames[i]);
		}
		
		part = SprocketsMultiparts.axle;
		for (int i = 0; i < part.subnames.length; i++)
		{
			registerItemRendersPre(part, part.subnames[i]);
		}

		part = SprocketsMultiparts.flywheel;
		for (int i = 0; i < part.subnames.length; i++)
		{
			registerItemRendersPre(part, part.subnames[i]);
		}
		
		
		part = SprocketsMultiparts.bigSprocket;
		for (int i = 0; i < part.subnames.length; i++)
		{
			registerItemRendersPre(part, part.subnames[i]);
		}
		
		ItemSprocketBase resource = SprocketsItems.resource;
		for (int i = 0; i < resource.subnames.length; i++)
		{
			registerItemRendersPre(resource, resource.subnames[i]);
		}
		
		resource = SprocketsItems.heap;
		for (int i = 0; i < resource.subnames.length; i++)
		{
			registerItemRendersPre(resource, "heap", resource.subnames[i]);
		}
		
		resource = SprocketsMultiparts.belt;
		for (int i = 0; i < resource.subnames.length; i++)
		{
			registerItemRendersPre(resource, resource.subnames[i]);
		}
		
		resource = SprocketsMultiparts.conveyorBelt;
		for (int i = 0; i < resource.subnames.length; i++)
		{
			registerItemRendersPre(resource, resource.subnames[i]);
		}
		
		
		registerRenders(SprocketsBlocks.redEngine);
		registerRenders(SprocketsBlocks.creativeEngine);
		registerRenders(SprocketsBlocks.millstone);
		registerRenders(SprocketsBlocks.spring);
		registerRenders(SprocketsBlocks.mbBlock);
		registerItemRenders(SprocketsMultiparts.scotchYoke);

		part = SprocketsMultiparts.sprocket;
		for (int i = 0; i < part.subnames.length; i++)
		{
			registerItemRenders(part, i, part.subnames[i]);
		}
		part = SprocketsMultiparts.axle;
		for (int i = 0; i < part.subnames.length; i++)
		{
			registerItemRenders(part, i, part.subnames[i]);
		}
		part = SprocketsMultiparts.flywheel;
		for (int i = 0; i < part.subnames.length; i++)
		{
			registerItemRenders(part, i, part.subnames[i]);
		}

		part = SprocketsMultiparts.redstoneSprocket;
		for (int i = 0; i < part.subnames.length; i++)
		{
			registerItemRenders(part, i, part.subnames[i]);
		}
		part = SprocketsMultiparts.lapisSprocket;
		for (int i = 0; i < part.subnames.length; i++)
		{
			registerItemRenders(part, i, part.subnames[i]);
		}
		part = SprocketsMultiparts.bigSprocket;
		for (int i = 0; i < part.subnames.length; i++)
		{
			registerItemRenders(part, i, part.subnames[i]);
		}
		
		resource = SprocketsItems.resource;
		for (int i = 0; i < resource.subnames.length; i++)
		{
			registerItemRenders(resource, i, resource.subnames[i]);
		}
		
		resource = SprocketsItems.heap;
		for (int i = 0; i < resource.subnames.length; i++)
		{
			registerItemRenders(resource, "heap", i, resource.subnames[i]);
		}
		
		resource = SprocketsMultiparts.belt;
		for (int i = 0; i < resource.subnames.length; i++)
		{
			registerItemRenders(resource, i, resource.subnames[i]);
		}
		resource = SprocketsMultiparts.conveyorBelt;
		for (int i = 0; i < resource.subnames.length; i++)
		{
			registerItemRenders(resource, i, resource.subnames[i]);
		}
		
		registerItemRenders(SprocketsMultiparts.clutch);

		registerItemRenders(SprocketsItems.wrench);
		registerItemRenders(SprocketsItems.crank);
		registerItemRenders(SprocketsItems.gyrometer);
	//	registerItemRenders(SprocketsItems.book);
		
		registerRenders(SprocketsBlocks.windmillSmall);
		registerRenders(SprocketsBlocks.frictionHeater);
		registerRenders(SprocketsBlocks.bellows);
		registerRenders(SprocketsBlocks.windmill);
		registerRenders(SprocketsBlocks.waterwheel);
		registerRenders(SprocketsBlocks.stampMill);
		registerRenders(SprocketsBlocks.waterwheelComponent);

	}


	@Override
	public void init()
	{
		super.init();

		MultipartRegistryClient.bindMultipartSpecialRenderer(PartSprocket.class, new RenderPartSprocket());
		MultipartRegistryClient.bindMultipartSpecialRenderer(PartRedstoneSprocket.class, new RenderPartSprocket());
		MultipartRegistryClient.bindMultipartSpecialRenderer(PartLapisSprocket.class, new RenderPartSprocket());
		MultipartRegistryClient.bindMultipartSpecialRenderer(PartBigSprocketCenter.class, new RenderPartBigSprocket());
		MultipartRegistryClient.bindMultipartSpecialRenderer(PartAxle.class, new RenderPartAxle());
		MultipartRegistryClient.bindMultipartSpecialRenderer(PartAxleBelt.class, new RenderPartAxle());
		MultipartRegistryClient.bindMultipartSpecialRenderer(PartAxleConveyorBelt.class, new RenderPartAxle());

		MultipartRegistryClient.bindMultipartSpecialRenderer(PartFlywheel.class, new RenderPartFlywheel());
		MultipartRegistryClient.bindMultipartSpecialRenderer(PartFlywheelFrictionPad.class, new RenderPartFlywheelFrictionPad());

		MultipartRegistryClient.bindMultipartSpecialRenderer(PartClutch.class, new RenderPartClutch());
		MultipartRegistryClient.bindMultipartSpecialRenderer(PartScotchYoke.class, new RenderPartScotchYoke());

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWindmillSmall.class, new TileEntityWindmillSmallRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWindmill.class, new TileEntityWindmillRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMillstone.class, new TileEntityMillstoneRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTorsionSpring.class, new TileEntityTorsionSpringRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWaterWheel.class, new TileEntityWaterWheelRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityStampMill.class, new TileEntityStampMillRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFrictionHeater.class, new TileEntityFrictionHeaterRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBellows.class, new TileEntityBellowsRenderer());

		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(SprocketsBlocks.windmillSmall), 0, TileEntityWindmillSmall.class);
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(SprocketsBlocks.windmill), 0, TileEntityWindmill.class);
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(SprocketsBlocks.spring), 0, TileEntityTorsionSpring.class);
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(SprocketsBlocks.waterwheel), 0, TileEntityWaterWheel.class);
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(SprocketsBlocks.stampMill), 0, TileEntityStampMill.class);
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(SprocketsBlocks.frictionHeater), 0, TileEntityFrictionHeater.class);
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(SprocketsBlocks.bellows), 0, TileEntityBellows.class);

	}
	
	
	@Override
	public void postInit()
	{
		super.postInit();
	}
	
	
	private void registerRenders(BlockSprocketBase block)
	{
		Item item = Item.getItemFromBlock(block);
		ModelLoader.setCustomModelResourceLocation(item, 
				0, new ModelResourceLocation(Sprockets.MODID + ":" + block.name, "inventory"));
	}
	
	private void registerItemRenders(ItemSprocketMultipart item, int meta, String mod)
	{
		ModelLoader.setCustomModelResourceLocation(item, 
				meta, new ModelResourceLocation(Sprockets.MODID + ":" + item.name + "_" + mod, "inventory"));
	}
	
	
	private void registerItemRendersPre(ItemSprocketBase resource, String folder, String mod)
	{
		ModelBakery.registerItemVariants(resource, new ResourceLocation(Sprockets.MODID + ":" + folder + "/" + resource.name + "_" + mod));
	}
	
	private void registerItemRendersPre(ItemSprocketMultipart item, String mod)
	{
		ModelBakery.registerItemVariants(item, new ResourceLocation(Sprockets.MODID + ":" + item.name + "_" + mod));
	}
	
	private void registerItemRenders(ItemSprocketMultipart item)
	{
		ModelLoader.setCustomModelResourceLocation(item, 
				0, new ModelResourceLocation(Sprockets.MODID + ":" + item.name, "inventory"));
	}
	
	private void registerItemRenders(ItemSprocketBase item, int meta, String mod)
	{
		ModelLoader.setCustomModelResourceLocation(item, 
				meta, new ModelResourceLocation(Sprockets.MODID + ":" + item.name + "_" + mod, "inventory"));
	}
	
	private void registerItemRenders(ItemSprocketBase item, String folder, int meta, String mod)
	{
		ModelLoader.setCustomModelResourceLocation(item, 
				meta, new ModelResourceLocation(Sprockets.MODID + ":" + folder + "/" + item.name + "_" + mod, "inventory"));
	}
	
	private void registerItemRendersPre(ItemSprocketBase item, String mod)
	{
		ModelBakery.registerItemVariants(item, new ResourceLocation(Sprockets.MODID + ":" + item.name + "_" + mod));
	}
	
	private void registerItemRenders(ItemSprocketBase item)
	{
		ModelLoader.setCustomModelResourceLocation(item, 
				0, new ModelResourceLocation(Sprockets.MODID + ":" + item.name, "inventory"));
	}
}
