package com.exorath.lib.gameLevel;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


/**
 * Created by toonsev on 8/18/2017.
 */
public class Glow {

    public static void addGlow(ItemStack item)

    {
        ItemMeta meta = item.getItemMeta();
        meta.addEnchant(Enchantment.LURE, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
    }
}
