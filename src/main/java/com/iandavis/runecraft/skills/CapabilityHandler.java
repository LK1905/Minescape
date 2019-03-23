package com.iandavis.runecraft.skills;

import com.iandavis.runecraft.RunecraftMain;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Logger;

import static com.iandavis.runecraft.proxy.CommonProxy.logger;

public class CapabilityHandler {

    private static final ResourceLocation SKILLS = new ResourceLocation(RunecraftMain.MODID);

    public CapabilityHandler() {
    }

    @SubscribeEvent
    public void attachCapability(AttachCapabilitiesEvent<Entity> event) {
        if (!(event.getObject() instanceof EntityPlayer)) {
            return;
        }

        logger.info("Adding runecraft skill capability to player");
        event.addCapability(SKILLS, new SkillCapabilityProvider());
    }
}
