package com.exorath.lib.gameLevel;

import org.bukkit.entity.Player;

import java.util.List;

/**
 * Created by toonsev on 8/17/2017.
 */
public interface LevelHandler {

    List<String> getRewards();

    void handleReward(Player player);

    /**
     * 0=minecart, 1=gold, coal
     * @return
     */
    int getItemType();
}
