package com.iandavis.minescape.api.stats;

public interface IStat {

	int getBaseStat();
	
	int getMinStat();
	
	int getMaxStat();
	
	int getCurrentStat();
	
	void setStat(int stat);
	
	int getBoost();
}
