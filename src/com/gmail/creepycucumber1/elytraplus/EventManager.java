package com.gmail.creepycucumber1.elytraplus;

import com.gmail.creepycucumber1.elytraplus.Items.Fragment;
import org.bukkit.*;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

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

    //handles elytra deployment for boom elytra
    @EventHandler
    public static void onElytraDeploy(EntityToggleGlideEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (p.isSneaking() && !p.isGliding()) {
                if (p.getInventory().getChestplate() != null && p.getInventory().getChestplate().getItemMeta().hasCustomModelData() &&
                        p.getInventory().getChestplate().getItemMeta().getCustomModelData() == 3) { //boom
                    p.setVelocity(p.getLocation().getDirection().multiply(1.07));
                    Location l = p.getLocation().clone();
                    p.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, l, 10, 1, 1, 1);
                    p.getWorld().playSound(l, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, SoundCategory.MASTER, 3, 2);
                }
            }
        }
    }
}
