package com.gmail.creepycucumber1.elytraplus;

import com.gmail.creepycucumber1.elytraplus.Items.AmethystElytra;
import com.gmail.creepycucumber1.elytraplus.Items.Elytra;
import com.gmail.creepycucumber1.elytraplus.Items.Fragment;
import com.gmail.creepycucumber1.elytraplus.Items.NetheriteElytra;
import org.bukkit.*;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Objects;
import java.util.Random;

public class EventManager implements Listener {

    private final ElytraPlus plugin;

    public EventManager(ElytraPlus instance) {
        this.plugin = instance;
    }

    //on dragon death, drop 1-2 wing fragments
    private static Random rand = new Random();

    @EventHandler
    public void onDragonDeath(EntityDeathEvent e) {
        World world_the_end = plugin.getServer().getWorld("world_the_end");
        if (world_the_end != null) {
            int i = rand.nextInt(2); // 0-1

            Item frag = world_the_end.dropItem(new Location(world_the_end, 0.5f, 100f, 0.5f), new ItemStack(Fragment.fragment.getType(), i + 1));
            frag.setVelocity(new Vector(0, 0, 0));

        } else {
            plugin.getLogger().info("Unable to locate world 'world_the_end' whilst attempting to handle Ender Dragon death; ignoring");
        }
    }

    //decreases durability for amethyst elytra, increases for netherite elytra
    @EventHandler
    public void onDurabilityDecrease(PlayerItemDamageEvent e) {
        if (e.getItem().getItemMeta().hasCustomModelData() &&
                e.getItem().getItemMeta().getCustomModelData() == 1) { //amethyst
            if (e.getItem().getDurability() < (short) 429) {
                e.setDamage(2);
            }
        }
        else if (e.getItem().getItemMeta().hasCustomModelData() &&
                e.getItem().getItemMeta().getCustomModelData() == 2) { //netherite
            if (rand.nextInt(2) == 1) {
                e.setCancelled(true);
            }
        }
    }

    //more kinetic damage, less other damage (prot 4 equivalent) when using netherite elytra
    @EventHandler
    public void onKineticDamage(EntityDamageEvent e) {
        if(e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if(p.getInventory().getChestplate() != null && p.getInventory().getChestplate().getItemMeta().hasCustomModelData() &&
                    p.getInventory().getChestplate().getItemMeta().getCustomModelData() == 2) { //netherite
                if(e.getCause().equals(EntityDamageEvent.DamageCause.FLY_INTO_WALL) || e.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
                    e.setDamage(e.getDamage() * 1.6);
                } else {
                    e.setDamage(e.getDamage() * 0.84);
                }
            }
        }
    }

    //cancels combustion of netherite elytra
    @EventHandler
    public void onEntityCombust(EntityCombustEvent e) {
        if(e.getEntity() instanceof Item) {
            Item i = (Item) e.getEntity();
            if(i.getItemStack().getType().equals(Material.ELYTRA) && i.getItemStack().hasItemMeta() &&
                    i.getItemStack().getItemMeta().hasCustomModelData() && i.getItemStack().getItemMeta().getCustomModelData() == 2) { //netherite
                i.setInvulnerable(true);
            }
        }
    }
    @EventHandler
    public void onEntityDamageByLava(EntityDamageEvent e) {
        if(e.getEntity() instanceof Item) {
            if(e.getCause().equals(EntityDamageEvent.DamageCause.LAVA) || e.getCause().equals(EntityDamageEvent.DamageCause.FIRE_TICK)) {
                Item i = (Item) e.getEntity();
                if(i.getItemStack().getType().equals(Material.ELYTRA) && i.getItemStack().hasItemMeta() &&
                        i.getItemStack().getItemMeta().hasCustomModelData() && i.getItemStack().getItemMeta().getCustomModelData() == 2) { //netherite
                    i.setInvulnerable(true);
                    i.setFireTicks(0);
                }
            }
        }
    }


    //custom item verification in custom recipes (prevents nuggets from being viable ingredients)

    @EventHandler
    public void onPrepareFragmentCraft(PrepareItemCraftEvent e) {
        if (Objects.equals(e.getInventory().getResult(), Fragment.fragment)) {
            CraftingInventory inv = e.getInventory();
            ItemStack[] items = inv.getMatrix();

            //if any of the 6 side crafting slots aren't essence (CustomModelData 1): cancel
            if (!items[0].getItemMeta().hasCustomModelData() || items[0].getItemMeta().getCustomModelData() != 1
                    || !items[3].getItemMeta().hasCustomModelData() || items[0].getItemMeta().getCustomModelData() != 1
                    || !items[6].getItemMeta().hasCustomModelData() || items[0].getItemMeta().getCustomModelData() != 1
                    || !items[2].getItemMeta().hasCustomModelData() || items[0].getItemMeta().getCustomModelData() != 1
                    || !items[5].getItemMeta().hasCustomModelData() || items[0].getItemMeta().getCustomModelData() != 1
                    || !items[8].getItemMeta().hasCustomModelData() || items[0].getItemMeta().getCustomModelData() != 1) {
                e.getInventory().setResult(new ItemStack(Material.AIR));
            }
        }
    }
    @EventHandler
    public void onPrepareAnyElytraCraft(PrepareItemCraftEvent e) {
        if (Objects.equals(e.getInventory().getResult(), AmethystElytra.aelytra) ||
                Objects.equals(e.getInventory().getResult(), NetheriteElytra.nelytra) ||
                Objects.equals(e.getInventory().getResult(), Elytra.elytra)) {
            CraftingInventory inv = e.getInventory();
            ItemStack[] items = inv.getMatrix();

            //if any of the 6 side crafting slots aren't fragment (CustomModelData 2): cancel
            if (!items[0].getItemMeta().hasCustomModelData() || items[0].getItemMeta().getCustomModelData() != 2
                    || !items[3].getItemMeta().hasCustomModelData() || items[0].getItemMeta().getCustomModelData() != 2
                    || !items[6].getItemMeta().hasCustomModelData() || items[0].getItemMeta().getCustomModelData() != 2
                    || !items[2].getItemMeta().hasCustomModelData() || items[0].getItemMeta().getCustomModelData() != 2
                    || !items[5].getItemMeta().hasCustomModelData() || items[0].getItemMeta().getCustomModelData() != 2
                    || !items[8].getItemMeta().hasCustomModelData() || items[0].getItemMeta().getCustomModelData() != 2) {
                e.getInventory().setResult(new ItemStack(Material.AIR));
            }
        }
    }
}
