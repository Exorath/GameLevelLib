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
public class LevelText implements HUDText {
    private GameLevelServiceAPI gameLevelServiceAPI;
    private String gameId;
    private Player player;
    private LevelFunction levelFunction;

    public LevelText(String gameId, GameLevelServiceAPI gameLevelServiceAPI, Player player) {
        this.gameId = gameId;
        this.gameLevelServiceAPI = gameLevelServiceAPI;
        this.player = player;
        this.levelFunction = gameLevelServiceAPI.getLevelFunction();
    }

    @Override
    public Observable<List<TextComponent>> getTextObservable() {
        return Observable.<List<TextComponent>>create(s -> {
            LevelPlayer levelPlayer = gameLevelServiceAPI.getPlayer(gameId, player.getUniqueId().toString());

            int reqPercentage = (100 * levelPlayer.getXp()) / levelFunction.getXp(levelPlayer.getLvl() + 1);
            TextComponent levelTxt = new TextComponent("Level");
            levelTxt.setColor(ChatColor.WHITE);
            TextComponent level = new TextComponent(" " + levelPlayer.getLvl());
            level.setColor(ChatColor.GREEN);
            TextComponent bracket = new TextComponent("(");
            bracket.setColor(ChatColor.GRAY);
            TextComponent xp = new TextComponent(" " + reqPercentage + "%");
            xp.setColor(ChatColor.AQUA);
            TextComponent bracketClosed = new TextComponent(")");
            bracketClosed.setColor(ChatColor.GRAY);
            s.onNext(Arrays.asList(levelTxt, level, bracket, xp, bracketClosed));
        }).subscribeOn(Schedulers.io());
    }

}
