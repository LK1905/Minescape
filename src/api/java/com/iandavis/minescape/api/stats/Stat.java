package com.iandavis.minescape.api.stats;

import com.iandavis.minescape.api.skills.ISkill;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public abstract class Stat implements IStat{

	protected Entity entity;
	protected EntityPlayer player;
	protected ISkill skill;
	
	protected int timer = 0;
	
	public Stat(Entity entity, EntityPlayer player, ISkill skill) {
		
		this.entity = entity;
		this.player = player;
		this.skill = skill;
	}
	
	@Override
	public int getBaseStat() {
		
		if(entity == player) {
			return skill.getLevel();
		}
		
		return 0;
	};
	
	@Override
	public int getMinStat() {
		return 0;
	};
	
	@Override
	public int getMaxStat() {
		return getBaseStat() + getBoost();
	};
	
	@Override
	public int getCurrentStat() {
		
		if(getCurrentStat() >= getMaxStat()) {
			
			return getMaxStat();
		}
		
		return statUpdate();
	};
	
	@Override
	public void setStat(int stat) {
		stat = getCurrentStat();
	};
	
	@Override
	public int getBoost() {
		return 0;
	}
	
	public int statUpdate() {
		
		for(int currentStat = getCurrentStat(); currentStat > getBaseStat(); currentStat--) {
			
			if(timer == 1200) {
				
				return currentStat;
			}
		}
		
		for(int currentStat = getCurrentStat(); currentStat < getBaseStat(); currentStat++) {
			
			if(timer == 1200) {
				
				return currentStat;
			}
		}
		
		return getBaseStat();
	}
	
	@SubscribeEvent
	public void onPlayerTick(ClientTickEvent event) {
		
		if(timer < 1200) {
			
			timer++;
			
		}else {
			
			timer = 0;
		}
	}
}
