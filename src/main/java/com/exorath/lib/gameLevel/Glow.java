package com.exorath.lib.gameLevel;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;

/**
 * Created by toonsev on 8/18/2017.
 */
public class Glow extends EnchantmentWrapper{

    private static Enchantment glow;

    public Glow( int id )
    {
        super(id);
    }

    @Override
    public boolean canEnchantItem(ItemStack item)
    {
        return true;
    }

    @Override
    public boolean conflictsWith(Enchantment other)
    {
        return false;
    }

    @Override
    public EnchantmentTarget getItemTarget()
    {
        return null;
    }

    @Override
    public int getMaxLevel()
    {
        return 10;
    }

    @Override
    public String getName()
    {
        return "Glow";
    }

    @Override
    public int getStartLevel()
    {
        return 1;
    }

    public static Enchantment getGlow()
    {
        if ( glow != null )
            return glow;

        try
        {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null , true);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        glow = new Glow(255);
        Enchantment.registerEnchantment(glow);
        return glow;
    }

    public static void addGlow(ItemStack item)
    {
        Enchantment glow = getGlow();

        item.addEnchantment(glow , 1);
    }
}
