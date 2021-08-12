package com.gmail.creepycucumber1.elytraplus.Items;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NetheriteElytra {

    public static ItemStack nelytra;

    public static void init() { createnelytra(); }

    private static void createnelytra() {
        ItemStack item = new ItemStack(Material.ELYTRA);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_GRAY + "Netherite Elytra");
        List<String> lore = new ArrayList<String>();
        lore.add(ChatColor.WHITE + (ChatColor.ITALIC + "heavy and strong"));
        meta.setLore(lore);

        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "GENERIC_ARMOR", 6.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST);
        AttributeModifier modifier2 = new AttributeModifier(UUID.randomUUID(), "GENERIC_ARMOR_TOUGHNESS", 2.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST);
        AttributeModifier modifier3 = new AttributeModifier(UUID.randomUUID(), "GENERIC_KNOCKBACK_RESISTANCE", 0.1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR, modifier);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, modifier2);
        meta.addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, modifier3);

        meta.setCustomModelData(2);

        item.setItemMeta(meta);
        nelytra = item;

        ShapedRecipe nelytraR = new ShapedRecipe(NamespacedKey.minecraft("nelytra"), nelytra);
        nelytraR.shape("FNF",
                       "FNF",
                       "FNF");
        nelytraR.setIngredient('F', Fragment.fragment.getType()); nelytraR.setIngredient('N', Material.NETHERITE_INGOT);
        try {
            Bukkit.getServer().addRecipe(nelytraR);
        } catch (IllegalStateException ignored) { }
    }
}
