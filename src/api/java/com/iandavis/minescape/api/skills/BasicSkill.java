package com.iandavis.minescape.api.skills;

import com.iandavis.minescape.api.events.LevelUpEvent;
import com.iandavis.minescape.api.events.XPGainEvent;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

/**
 * Simple Implementation of the ISkill interface which provides all the basic functionality required.
 * For an example of a complete skill based off of this implementation, view the DiggingSkill class.
 */
public abstract class BasicSkill implements ISkill {
    protected int currentXP;

    //The calculation for these numbers = Math.floor((getLevel() + 300 * (2 ^ (getLevel() / 7)))/4).
    //Had no idea how to implement it though, so I just hardcoded the numbers into the existing array.
    protected static int[] xpLevels = new int[]{
            0, 83, 174, 276, 388, 512, 650, 801, 969, 1154,
            1358, 1584, 1833, 2107, 2411, 2746, 3115, 3523, 3973, 4470,
            5018, 5624, 6291, 7028, 7842, 8740, 9730, 10824, 12031, 13363,
            14833, 16456, 18247, 20224, 22406, 24815, 27473, 30408, 33648, 37224,
            41171, 45529, 50339, 55649, 61512, 67983, 75127, 83014, 91721, 101333,
            111945, 123660, 136594, 150872, 166636, 184040, 203254, 224466, 247886, 273742,
            302288, 333804, 368599, 407015, 449428, 496254, 547953, 605032, 668051, 737627,
            814445, 899257, 992895, 1096278, 1210421, 1336443, 1475581, 1629200, 1798808, 1986068,
            2192818, 2421087, 2673114, 2951373, 3258594, 3597792, 3972294, 4385776, 4842295, 5346332,
            5902831, 6517253, 7195629, 7944614, 8771558, 9684577, 10692629, 11805606, 13034431
    };

    public BasicSkill() {
        currentXP = 0;
    }

    @Override
    public void gainXP(int amount, EntityPlayer player) {
        int xpToNext = xpToNextLevel();
        int currentLevel = getLevel();

        this.currentXP += amount;

        if (amount < getMaxXP()) {
        	XPGainEvent xpEvent = new XPGainEvent(this, player, amount);
        	MinecraftForge.EVENT_BUS.post(xpEvent);
        }

        if (amount >= xpToNext && currentLevel < getMaxLevel()) {
            LevelUpEvent levelEvent = new LevelUpEvent(this, player);
            MinecraftForge.EVENT_BUS.post(levelEvent);
        }
    }

    @Override
    public void setXP(int xp) {
        currentXP = xp;
    }

    @Override
    public int getXP() {
        return currentXP;
    }

    @Override
    public int getLevel() {
        for (int level = 1; level < xpLevels.length; level++) {
            if (xpLevels[level] > currentXP) {
                return level;
            }
        }

        return getMaxLevel();
    }

    @Override
    public int xpForLevel(int level) {
        if (level >= getMaxLevel()) {
            return 0;
        }

        return xpLevels[level - 1];
    }

    @Override
    public int xpToNextLevel() {
        int currentXP = getXP();

        if (getLevel() >= getMaxLevel()) {
            return 0;
        }

        for (int level = 1; level < xpLevels.length; level++) {
            if (xpLevels[level] > currentXP) {
                return xpLevels[level] - currentXP;
            }
        }

        return 0;
    }

    @Override
    public int getMaxXP() {
    	return 200000000;
    }
    
    @Override
    public int getMaxLevel() {
        return 99;
    }
    
    @Override
    public void setLevel(int newLevel) {
        currentXP = xpLevels[newLevel - 1];
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound data = new NBTTagCompound();
        data.setInteger("xp", currentXP);
        return data;
    }

    @Override
    public void deserializeNBT(NBTTagCompound data) {
        currentXP = data.getInteger("xp");
    }

    @Override
    public void serializePacket(ByteBuf buf) {
        buf.writeInt(this.getXP());
    }

    @Override
    public void deserializePacket(ByteBuf buf) {
        currentXP = buf.readInt();
    }

    public static int[] getXpLevels() {
        return xpLevels;
    }
    
    //Stats
    
    protected int timer = 0;
    
    public int modifier = 0;
    
    protected int stat = getLevel() + modifier;
    
    protected int maxStat = getLevel() + modifier;
    
	@Override
	public int getCurrentStat() {

		for(int currentStat = stat; currentStat > getLevel(); currentStat--) {
			
			if(timer == 1200) {
				
				return currentStat;
			}
		}
		
		for(int currentStat = stat; currentStat < getLevel(); currentStat++) {
			
			if(timer == 1200) {
				
				return currentStat;
			}
		}
		
		return getLevel();
	}
	
	@Override
	public void setCurrentStat(int stat) {
		
		if(this.stat > maxStat) {
			
			stat = maxStat;
			
		}else if(this.stat < 0) {
			
			stat = 0;
		}
		
		stat = this.stat;
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
