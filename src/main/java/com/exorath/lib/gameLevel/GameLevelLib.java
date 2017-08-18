package com.exorath.lib.gameLevel;

import com.exorath.commons.AntiSpam;
import com.exorath.exomenus.MenuItem;
import com.exorath.service.gamelevel.api.GameLevelServiceAPI;
import com.exorath.service.gamelevel.res.LevelFunction;
import com.exorath.service.gamelevel.res.LevelPlayer;
import com.exorath.service.gamelevel.res.Success;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;

import java.util.List;


/**
 * Configuration file decides where the item goes, if clicked a menu opens with all levels (for now 36)
 * Created by toonsev on 8/17/2017.
 */
public class GameLevelLib implements Listener {
    private GameLevelServiceAPI gameLevelServiceAPI;
    private Plugin plugin;
    private LevelsConfig levelsConfig;
    private Integer levelSlot;
    private LevelFunction levelFunction;

    public GameLevelLib(GameLevelServiceAPI gameLevelServiceAPI, Plugin plugin, LevelsConfig levelsConfig, Integer levelSlot) {
        this.gameLevelServiceAPI = gameLevelServiceAPI;
        this.plugin = plugin;
        this.levelsConfig = levelsConfig;
        this.levelSlot = levelSlot;
        this.levelFunction = gameLevelServiceAPI.getLevelFunction();
    }

    public void openInventory(Player player) {
        if (AntiSpam.isSpamming(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "Don't spam click.");
            return;
        }
        AntiSpam.setSpamming(plugin, player.getUniqueId());

        final String playerId = player.getUniqueId().toString();
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            final LevelPlayer levelPlayer = gameLevelServiceAPI.getPlayer(levelsConfig.getGameId(), playerId);
            plugin.getServer().getScheduler().runTask(plugin, () -> {
                LevelsMenu levelsMenu = getInventory(levelPlayer);
                levelsMenu.getMenu().open(player);
            });
        });
    }

    private LevelsMenu getInventory(final LevelPlayer levelPlayer) {
        LevelsMenu levelsMenu = new LevelsMenu(levelPlayer, levelsConfig.getGameTitle(), levelFunction);
        for (int i = 1; i <= 36; i++) {
            final LevelHandler handler = levelsConfig.getRewardHandlersByLevel().get(i);
            MenuItem menuItem = levelsMenu.addLevel(i, handler);
            final int lvl = i;
            menuItem.getClickObservable().subscribe((event) -> {
                onClick(event, levelPlayer, lvl, handler);
            });
        }
        return levelsMenu;
    }

    private void onClick(InventoryClickEvent event, LevelPlayer levelPlayer, int level, LevelHandler levelHandler) {
        if (AntiSpam.isSpamming(event.getWhoClicked().getUniqueId()))
            return;
        AntiSpam.setSpamming(plugin, event.getWhoClicked().getUniqueId());
        if (levelPlayer.getLvl() < level) {
            event.getWhoClicked().sendMessage(ChatColor.RED + "You are not lvl " + level + " yet.");
        } else if (levelHandler != null && !levelHandler.getRewards().isEmpty()) {
            if (levelPlayer.getConsumable() != null && levelPlayer.getConsumable().contains(level)) {
                event.getWhoClicked().closeInventory();
                event.getWhoClicked().sendMessage(ChatColor.GREEN + "Unlocking rewards...");
                unlockRewards((Player) event.getWhoClicked(), level, levelHandler);
            }
        }
    }

    private void unlockRewards(final Player player, final int lvl, final LevelHandler levelHandler) {
        final String gameId = levelsConfig.getGameId();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            final Success success = gameLevelServiceAPI.consumeLevel(gameId, player.getUniqueId().toString(), lvl);
            Bukkit.getScheduler().runTask(plugin, () -> {
                if (success.isSuccess()) {
                    levelHandler.handleReward(player);
                    player.sendMessage(ChatColor.GREEN + "Unlocked!");
                } else
                    player.sendMessage(ChatColor.RED + "Error(code " + success.getCode() + "): " + success.getError());
            });

        });
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (levelSlot != null)
            event.getPlayer().getInventory().setItem(levelSlot, getHotbarItem(event.getPlayer()));
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (levelSlot == null)
            return;
        if (event.getPlayer().getInventory().getHeldItemSlot() != levelSlot)
            return;
        event.setCancelled(true);
        openInventory(event.getPlayer());
    }

    private ItemStack getHotbarItem(Player player) {
        ItemStack is = new ItemStack(Material.SKULL_ITEM);
        SkullMeta meta = (SkullMeta) Bukkit.getItemFactory().getItemMeta(Material.SKULL_ITEM);
        meta.setOwner(player.getName());
        meta.setDisplayName(ChatColor.GREEN + "Your Account " + ChatColor.GRAY + "(Right Click)");
        is.setItemMeta(meta);
        return is;
    }

}
