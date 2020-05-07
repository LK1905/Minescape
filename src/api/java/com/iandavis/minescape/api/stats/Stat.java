package com.iandavis.minescape.api.stats;

import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public abstract class Stat implements IStat{

	protected EntityLivingBase entity;
	protected int timer = 0;
	protected int stat;
	
	public Stat(EntityLivingBase entity) {
		
		this.entity = entity;
		this.stat = 1;
	}
	
	@Override
	public void setBaseStat(int stat) {
		this.stat = stat;
	}
	
	@Override
	public int getBaseStat() {
		
		return stat;
	};
	
	@Override
	public int getMaxStat() {
		
		return getBaseStat() + getBoost();
	}
	
	@Override
	public int getCurrentStat() {
		
		if(getCurrentStat() >= getMaxStat()) {
			
			return getMaxStat();
			
		}else if(getCurrentStat() <= 0) {
			
			return 0;
		}
		
		return statUpdate();
	}
	
	@Override
	public void setCurrentStat(int stat) {
		stat = getCurrentStat();
	}
	
	@Override
	public int getBoost() {
		return 0;
	}
	
	@Override
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
