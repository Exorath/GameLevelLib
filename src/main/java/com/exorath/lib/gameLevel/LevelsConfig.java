package com.exorath.lib.gameLevel;

import java.util.HashMap;

/**
 * Created by toonsev on 8/17/2017.
 */
public class LevelsConfig {
    private HashMap<Integer, LevelHandler> rewardHandlersByLevel = new HashMap<>();
    private String gameTitle;
    private String gameId;

    public LevelsConfig(String gameTitle, String gameId) {
        this.gameTitle = gameTitle;
        this.gameId = gameId;
    }

    public HashMap<Integer, LevelHandler> getRewardHandlersByLevel() {
        return rewardHandlersByLevel;
    }

    public void addRewardHandler(int level, LevelHandler handler) {
        rewardHandlersByLevel.put(level, handler);
    }

    public String getGameId() {
        return gameId;
    }

    public String getGameTitle() {
        return gameTitle;
    }
}
