package com.gmail.creepycucumber1.elytraplus.Items;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class AmethystElytra {

    public static ItemStack aelytra;

    public static void init() { createaelytra(); }

    private static void createaelytra() {
        ItemStack item = new ItemStack(Material.ELYTRA);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_PURPLE + "Amethyst Elytra");
        List<String> lore = new ArrayList<String>();
        lore.add(ChatColor.WHITE + (ChatColor.ITALIC + "light and fast"));
        meta.setLore(lore);

        meta.setCustomModelData(1);

        item.setItemMeta(meta);
        aelytra = item;

        ShapedRecipe aelytraR = new ShapedRecipe(NamespacedKey.minecraft("aelytra"), aelytra);
        aelytraR.shape("FAF",
                       "F F",
                       "F F");
        aelytraR.setIngredient('F', Fragment.fragment.getType()); aelytraR.setIngredient('A', Material.AMETHYST_CLUSTER);
        try {
            Bukkit.getServer().addRecipe(aelytraR);
        } catch (IllegalStateException ignored) { }
    }
}
