package com.iandavis.minescape.api.stats;

public interface IStat {

	void setBaseStat(int stat);
	
	int getBaseStat();
	
	int getMaxStat();
	
	int getCurrentStat();
	
	void setCurrentStat(int stat);
	
	int getBoost();
	
	int statUpdate();
}
