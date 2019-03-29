package com.iandavis.runecraft.gui;

import com.iandavis.runecraft.proxy.ClientProxy;
import com.iandavis.runecraft.skills.ISkill;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SkillBarHUD extends Gui {
    private final ResourceLocation texture = new ResourceLocation("runecraft", "textures/gui/skills.png");

    public SkillBarHUD() {
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onOverlayRender(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) {
            return;
        }

        Minecraft mc = Minecraft.getMinecraft();

        ScaledResolution resolution = event.getResolution();
        int xPos = resolution.getScaledWidth() / 2 - 91;
        int yPos = resolution.getScaledHeight() - GuiIngameForge.left_height;
        GuiIngameForge.left_height += 16;

        ISkill activeSkill = ClientProxy.getActivelyTrainedSkill();

        if (activeSkill == null) {
            return;
        }

        if (!mc.playerController.shouldDrawHUD()) {
            return;
        }

        mc.getTextureManager().bindTexture(texture);

        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableLighting();
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();

        drawTexturedModalRect(xPos, yPos, 177, 0, 54, 6);

        int xpForNextLevel = activeSkill.getXP() + activeSkill.xpToNextLevel();
        int xpBarWidth = (int)(((float) activeSkill.getXP() / xpForNextLevel) * 48);

        drawTexturedModalRect(xPos + 3, yPos + 2, 180, 7, xpBarWidth, 3);
        String s = String.format("XP: %d / %d", activeSkill.getXP(), xpForNextLevel);

        yPos += 10;

        Color white = new Color(255, 255, 255, 255);
        mc.fontRenderer.drawString(s, xPos, yPos, white.getIntValue());

        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
    }
}