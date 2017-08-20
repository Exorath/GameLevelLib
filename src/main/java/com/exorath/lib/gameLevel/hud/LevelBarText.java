package com.exorath.lib.gameLevel.hud;

import com.exorath.exoHUD.HUDText;
import com.exorath.service.gamelevel.api.GameLevelServiceAPI;
import com.exorath.service.gamelevel.res.LevelFunction;
import com.exorath.service.gamelevel.res.LevelPlayer;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

/**
 * Created by toonsev on 8/20/2017.
 */
public class LevelBarText implements HUDText{
    private static final String BOX_ICON = "■";
    private GameLevelServiceAPI gameLevelServiceAPI;
    private String gameId;
    private Player player;
    private LevelFunction levelFunction;

    public LevelBarText(String gameId, GameLevelServiceAPI gameLevelServiceAPI, Player player) {
        this.gameId = gameId;
        this.gameLevelServiceAPI = gameLevelServiceAPI;
        this.player = player;
        this.levelFunction = gameLevelServiceAPI.getLevelFunction();
    }

    @Override
    public Observable<List<TextComponent>> getTextObservable() {
        return Observable.<List<TextComponent>>create(s -> {
            LevelPlayer levelPlayer = gameLevelServiceAPI.getPlayer(gameId, player.getUniqueId().toString());
            TextComponent openBracket = new TextComponent("[");
            openBracket.setColor(ChatColor.DARK_GRAY);
            TextComponent achievedLevels = new TextComponent();
            achievedLevels.setColor(ChatColor.AQUA);
            TextComponent toAchieve = new TextComponent();
            toAchieve.setColor(ChatColor.GRAY);
            TextComponent closedBracket = new TextComponent("]");
            closedBracket.setColor(ChatColor.DARK_GRAY);

            s.onNext(Arrays.asList(openBracket, achievedLevels, toAchieve, closedBracket));
        }).subscribeOn(Schedulers.io());
    }

    private String getAchievedLevels(LevelPlayer levelPlayer){
        int total = levelFunction.getXp(levelPlayer.getLvl() + 1);
        int req = (10*levelPlayer.getXp()) / total;
        StringBuilder msg = new StringBuilder(10);
        for(int i = 0; i < 10; i++){
            if(i >= req)
                return msg.toString();
            msg.append(BOX_ICON);
        }
        return msg.toString();
    }
    private String getToAchieveLevels(LevelPlayer levelPlayer){
        int total = levelFunction.getXp(levelPlayer.getLvl() + 1);
        int req = 10 - (10*levelPlayer.getXp()) / total;
        StringBuilder msg = new StringBuilder(10);
        for(int i = 0; i < 10; i++){
            if(i >= req)
                return msg.toString();
            msg.append(BOX_ICON);
        }
        return msg.toString();
    }
}
