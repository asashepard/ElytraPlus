package com.gmail.creepycucumber1.elytraplus.Items;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

public class Elytra {

    public static ItemStack elytra;

    public static void init() { createelytra(); }

    private static void createelytra() {
        ItemStack item = new ItemStack(Material.ELYTRA);
        elytra = item;

        ShapedRecipe elytraR = new ShapedRecipe(NamespacedKey.minecraft("elytra"), elytra);
        elytraR.shape("FPF",
                      "F F",
                      "F F");
        elytraR.setIngredient('F', Fragment.fragment.getType()); elytraR.setIngredient('P', Material.POPPED_CHORUS_FRUIT);
        try {
            Bukkit.getServer().addRecipe(elytraR);
        } catch (IllegalStateException ignored) { }
    }
}
