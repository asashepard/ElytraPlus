package com.gmail.creepycucumber1.elytraplus.Items;

import com.gmail.creepycucumber1.elytraplus.ElytraPlus;
import org.bukkit.*;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

public class Fragment implements Listener {

    public static ItemStack fragment;
    private final ElytraPlus plugin;

    public Fragment(ElytraPlus plugin) {
        this.plugin = plugin;
    }

    public static void init() { createFragment(); }

    private static void createFragment() {
        ItemStack item = new ItemStack(Material.IRON_NUGGET);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + "Wing Fragment");

        meta.setCustomModelData(2);

        item.setItemMeta(meta);
        fragment = item;

        ShapedRecipe fragmentR = new ShapedRecipe(NamespacedKey.minecraft("fragment"), fragment);
        fragmentR.shape("EME",
                        "EME",
                        "EME");
        fragmentR.setIngredient('E', Essence.essence.getType()); fragmentR.setIngredient('M', Material.PHANTOM_MEMBRANE);
        try {
            Bukkit.getServer().addRecipe(fragmentR);
        } catch (IllegalStateException ignored) { }
    }
}
