package com.iandavis.runecraft.events;

import com.iandavis.runecraft.skills.ISkill;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Event;

public class XPGainEvent extends Event {
    private ISkill skill;
    private EntityPlayer player;
    private int xpGained;

    public XPGainEvent() {

    }

    public XPGainEvent(ISkill skill, EntityPlayer player, int xpGained) {
        this.skill = skill;
        this.player = player;
        this.xpGained = xpGained;
    }

    public ISkill getSkill() {
        return skill;
    }

    public EntityPlayer getPlayer() {
        return player;
    }

    public int getXpGained() {
        return xpGained;
    }
}
