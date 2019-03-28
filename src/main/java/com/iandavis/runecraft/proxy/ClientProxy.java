package com.iandavis.runecraft.proxy;

import com.iandavis.runecraft.gui.MenuInterfaceOverride;
import com.iandavis.runecraft.network.*;
import com.iandavis.runecraft.skills.ISkillCapability;
import com.iandavis.runecraft.skills.SkillCapability;
import com.iandavis.runecraft.skills.SkillCapabilityProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.Logger;

public class ClientProxy implements Proxy {
    private static Logger logger;
    private static ISkillCapability skillCapability = null;

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();

    }

    @Override
    public void init(FMLInitializationEvent event) {
        LevelUpMessage.registerClientSide();
        XPGainMessage.registerClientSide();
        StatsRequestMessage.registerClientSide();
        StatsResponseMessage.registerClientSide();

        StatsResponseHandler.registerSingleShotListener(ClientProxy::loadSkillCapability);

        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {

    }

    @Override
    public void serverStarting(FMLServerStartingEvent event) {
    }

    @Override
    public EntityPlayer getPlayerEntityFromContext(MessageContext parContext) {
        return null;
    }

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
            CommonProxy.networkWrapper.sendTo(
                    new StatsResponseMessage(event.player.getCapability(SkillCapabilityProvider.skill, null)),
                    (EntityPlayerMP) event.player);
        }
    }

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        if (event.getGui() instanceof GuiInventory) {
            if (Minecraft.getMinecraft() == null) {
                logger.warn("mc was null!");
                return;
            } else if (Minecraft.getMinecraft().player == null) {
                logger.warn("mc.player was null!");
                return;
            }

            event.setGui(new MenuInterfaceOverride(Minecraft.getMinecraft().player));
        } else if (event.getGui() instanceof GuiContainerCreative) {

        }
    }

    private static void loadSkillCapability(StatsResponseMessage message, MessageContext context) {
        if (message == null || context == null) {
            return;
        }

        skillCapability = message.getSkillCapability();
    }

    public static void updateSkillCapability() {
        StatsResponseHandler.registerSingleShotListener(ClientProxy::loadSkillCapability);
        CommonProxy.networkWrapper.sendToServer(new StatsRequestMessage());
    }

    public static ISkillCapability getSkillCapability() {
        return skillCapability;
    }
}
