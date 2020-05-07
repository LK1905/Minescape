package com.iandavis.minescape.stats;

import com.iandavis.minescape.api.skills.ISkill;
import com.iandavis.minescape.api.stats.Stat;
import com.iandavis.minescape.api.utils.CapabilityUtils;
import com.iandavis.minescape.capability.skill.CapabilitySkills;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class AttackStat extends Stat{

	EntityLivingBase entity;
	EntityPlayer player = Minecraft.getMinecraft().player;
	
	ISkill skill = CapabilityUtils.getCapability(player, CapabilitySkills.getSkillCapability(), null).getSkill("Attack");
	
	public AttackStat(EntityLivingBase entity) {
		super(entity);
		this.entity = entity;
	}
	
	@Override
	public int getBaseStat() {
		
		if(entity == player) {
			
			return skill.getLevel();
		}
		
		return 1;
	}

}
