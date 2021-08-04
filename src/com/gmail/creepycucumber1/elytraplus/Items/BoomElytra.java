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

public class BoomElytra {

    public static ItemStack belytra;

    public static void init() { createbelytra(); }

    private static void createbelytra() {
        ItemStack item = new ItemStack(Material.ELYTRA);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_GREEN + "Boom Elytra");
        List<String> lore = new ArrayList<String>();
        lore.add(ChatColor.WHITE + (ChatColor.ITALIC + "quick and explosive"));
        meta.setLore(lore);

        meta.setCustomModelData(3);

        item.setItemMeta(meta);
        belytra = item;

        ShapedRecipe belytraR = new ShapedRecipe(NamespacedKey.minecraft("belytra"), belytra);
        belytraR.shape("FCF",
                       "F F",
                       "F F");
        belytraR.setIngredient('F', Fragment.fragment.getType()); belytraR.setIngredient('C', Material.END_CRYSTAL);
        try {
            Bukkit.getServer().addRecipe(belytraR);
        } catch (IllegalStateException ignored) { }
    }
}
