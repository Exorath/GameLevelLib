package com.exorath.lib.gameLevel;

import com.exorath.exomenus.InventoryMenu;
import com.exorath.exomenus.MenuItem;
import com.exorath.exomenus.Size;
import com.exorath.service.gamelevel.res.LevelPlayer;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

/**
 * Created by toonsev on 8/17/2017.
 */
public class LevelsMenu {
    private InventoryMenu menu;
    private LevelPlayer levelPlayer;

    public LevelsMenu(LevelPlayer levelPlayer, String gameTitle) {
        this.levelPlayer = levelPlayer;
        String title = ChatColor.DARK_GRAY + "Lvl " + levelPlayer.getLvl() + " on " + gameTitle;
        this.menu = new InventoryMenu(title, Size.SIX_LINE, new MenuItem[Size.SIX_LINE.getslots()], null);
    }

    public InventoryMenu getMenu() {
        return menu;
    }

    public MenuItem addLevel(int level, LevelHandler levelHandler) {
        MenuItem menuItem = getLevelItem(levelPlayer, level, levelHandler);
        menu.setItem(level + 8, menuItem);
        return menuItem;
    }

    private MenuItem getLevelItem(LevelPlayer levelPlayer, int level, LevelHandler levelHandler) {
        boolean hasLvl = levelPlayer.getLvl() >= level;
        boolean consumed = levelPlayer.getConsumable().contains(level);

        String title = getTitle(levelHandler, hasLvl, consumed, level);
        title = title.substring(0, 32);//make sure we don't have overflow
        Material material = getMaterial(levelHandler, hasLvl, consumed);
        String[] lore = getLore(levelHandler, hasLvl, consumed);
        return new MenuItem(title, new ItemStack(material), lore);

    }

    private static String getTitle(LevelHandler levelHandler, boolean hasLvl, boolean consumed, int level) {
        if (hasLvl) {
            return ChatColor.RED + "Level " + level;
        } else {
            if (levelHandler == null || levelHandler.getRewards().isEmpty()) {
                return ChatColor.GREEN + "Level " + level;
            } else if (consumed) {
                return ChatColor.LIGHT_PURPLE + "Level " + level + ChatColor.GRAY + " (Right Click for " + ChatColor.AQUA + "Reward" + ChatColor.GRAY + ")";
            } else {
                return ChatColor.GREEN + "Level " + level + ChatColor.GRAY + " (Reward Already Received)";
            }
        }
    }

    private static String[] getLore(LevelHandler levelHandler, boolean hasLvl, boolean consumed) {
        ArrayList<String> lore = new ArrayList<>();
        if (levelHandler == null || levelHandler.getRewards().isEmpty()) {
            lore.add("");
            lore.add(ChatColor.DARK_GRAY + "No reward for this level.");
        } else {
            lore.add("");
            for (String reward : levelHandler.getRewards())
                lore.add(ChatColor.GRAY + "+ " + reward);
            lore.add("");
            if (hasLvl) {
                if (consumed)
                    lore.add(ChatColor.RED + "You've already received a reward.");
                else
                    lore.add(ChatColor.GREEN + "Right click to receive reward.");
            } else
                lore.add(ChatColor.RED + "Level up to receive reward.");
        }
        return lore.toArray(new String[lore.size()]);
    }

    private static Material getMaterial(LevelHandler levelHandler, boolean hasLvl, boolean consumed) {
        if (levelHandler == null) {
            return Material.STORAGE_MINECART;
        } else {
            Material empty;
            Material full;
            switch (levelHandler.getItemType()) {
                case 1:
                    empty = Material.COAL_BLOCK;
                    full = Material.GOLD_BLOCK;
                    break;
                default:
                    empty = Material.MINECART;
                    full = Material.STORAGE_MINECART;
                    break;
            }
            return consumed && hasLvl ? empty : full;
        }
    }
}
