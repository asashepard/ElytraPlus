package com.gmail.creepycucumber1.elytraplus.Items;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

public class Essence {

    public static ItemStack essence;

    public static void init() { createEssence(); }

    private static void createEssence() {
        ItemStack item = new ItemStack(Material.IRON_NUGGET);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW + "Dragon Essence");

        meta.setCustomModelData(1);

        item.setItemMeta(meta);
        essence = item;

        ShapedRecipe essenceR = new ShapedRecipe(NamespacedKey.minecraft("essence"), essence);
        essenceR.shape(" D ",
                       "DDD",
                       " D ");
        essenceR.setIngredient('D', Material.DRAGON_BREATH);
        try {
            Bukkit.getServer().addRecipe(essenceR);
        } catch (IllegalStateException ignored) { }
    }
}
