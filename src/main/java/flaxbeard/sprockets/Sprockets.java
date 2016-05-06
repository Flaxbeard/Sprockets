package flaxbeard.sprockets;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import flaxbeard.sprockets.common.CommonProxy;
import flaxbeard.sprockets.lib.LibConstants;
import flaxbeard.sprockets.multiparts.SprocketsMultiparts;

@Mod(modid = Sprockets.MODID, version = Sprockets.VERSION, dependencies = "required-after:mcmultipart")
public class Sprockets
{
    public static final String MODID = "sprockets";
    public static final String VERSION = "@VERSION@";
        
    @SidedProxy(clientSide = "flaxbeard.sprockets.client.ClientProxy", serverSide = "flaxbeard.sprockets.common.CommonProxy")
    public static CommonProxy proxy;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    	LibConstants.loadConfig(event);
    	proxy.preInit();
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	proxy.init();
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
    	proxy.postInit();
    }
    
    public static CreativeTabs creativeTab = new CreativeTabs(MODID)
	{
		@Override
		public Item getTabIconItem()
		{
			return null;
		}
		@Override
		public ItemStack getIconItemStack()
		{
			return new ItemStack(SprocketsMultiparts.sprocket);
		}
	};
}
