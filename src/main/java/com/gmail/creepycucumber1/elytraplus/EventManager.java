package com.gmail.creepycucumber1.elytraplus;

import com.gmail.creepycucumber1.elytraplus.Items.AmethystElytra;
import com.gmail.creepycucumber1.elytraplus.Items.Elytra;
import com.gmail.creepycucumber1.elytraplus.Items.Fragment;
import com.gmail.creepycucumber1.elytraplus.Items.NetheriteElytra;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class EventManager implements Listener {

    private final ElytraPlus plugin;

    public EventManager(ElytraPlus instance) {
        this.plugin = instance;
    }

    private static Random rand = new Random();

    //on dragon death, drop 1-2 wing fragments
    @EventHandler
    public void onDragonDeath(EntityDeathEvent e) {
        if(e.getEntity() instanceof EnderDragon) {
            World world_the_end = plugin.getServer().getWorld("world_the_end");
            if (world_the_end != null) {
                int max = 4;
                int min = 2;
                int amt = rand.nextInt(max + 1 - min) + min;

                for(int i = 0; i < amt; i++) {
                    Item frag = world_the_end.dropItem(new Location(world_the_end, 0.5f, 100f, 0.5f), Fragment.fragment);
                    frag.setVelocity(new Vector(0, 0, 0));
                }

            } else {
                plugin.getLogger().info("Unable to locate world 'world_the_end' whilst attempting to handle Ender Dragon death; ignoring");
            }
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

    //throwable fireballs via essence
    @EventHandler
    public void onRightClickEssence(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if(e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (e.getItem() != null && e.getItem().getType().equals(Material.IRON_NUGGET) &&
                    e.getItem().hasItemMeta() && e.getItem().getItemMeta().hasCustomModelData() && e.getItem().getItemMeta().getCustomModelData() == 1) {
                p.launchProjectile(Fireball.class).setVelocity(p.getLocation().getDirection().multiply(2)
                        .add(new Vector((rand.nextInt(20 + 1 + 20) - 20) * 0.01,
                                (rand.nextInt(20 + 1 + 20) - 20) * 0.01,
                                (rand.nextInt(20 + 1 + 20) - 20) * 0.01)));
                e.getItem().setAmount(e.getItem().getAmount() - 1);
                p.updateInventory();
            }
        }
    }
    @EventHandler
    public void onProjectileHit(ProjectileHitEvent e) {
        if(e.getEntity().getShooter() instanceof Player && e.getEntityType().equals(EntityType.FIREBALL)) {

            try {
                Location l = e.getHitEntity().getLocation();
                List<Entity> entities = (List<Entity>) l.getWorld().getNearbyEntities(l, 5, 5, 5);
                for(Entity c : entities) {
                    if(c instanceof LivingEntity && !(c instanceof Player)) {
                        LivingEntity cl = (LivingEntity) c;
                        double health = cl.getHealth();
                        List<Double> oldHealth = new ArrayList<>();
                        oldHealth.add(health);
                        cl.damage(0.01, (Entity) e.getEntity().getShooter()); //check if source can damage entity by applying damage, comparing
                        if(cl.getHealth() != oldHealth.get(0)) { //if player can damage entity, apply velocity and heal entity
                            Vector velocity = c.getLocation().toVector().subtract(l.toVector()).add(new Vector(0, 0.8, 0)).normalize();
                            c.setVelocity(velocity.multiply(1.5));
                            if(!cl.isDead()) { cl.setHealth(cl.getHealth() + 0.01); }
                        }
                    }
                    if(c instanceof Player || c instanceof Item) {
                        Vector velocity = c.getLocation().toVector().subtract(l.toVector()).add(new Vector(0, 0.8, 0)).normalize();
                        c.setVelocity(velocity.multiply(1.5));
                    }
                }
                Particle.DustOptions dust = new Particle.DustOptions(Color.fromRGB(185, 90, 219), 3);
                l.getWorld().spawnParticle(Particle.REDSTONE, l.getX(), l.getY(), l.getZ(), 200, 2, 2, 2, dust);
                l.getWorld().playSound(l, Sound.ENTITY_ENDER_DRAGON_FLAP, 10, 1);

            } catch (Exception exception) {
                Location l = e.getHitBlock().getLocation();
                List<Entity> entities = (List<Entity>) l.getWorld().getNearbyEntities(l, 5, 5, 5);
                for(Entity c : entities) {
                    if(c instanceof LivingEntity && !(c instanceof Player)) {
                        LivingEntity cl = (LivingEntity) c;
                        double health = cl.getHealth();
                        List<Double> oldHealth = new ArrayList<>();
                        oldHealth.add(health);
                        cl.damage(0.01, (Entity) e.getEntity().getShooter()); //check if source can damage entity by applying damage, comparing
                        if(cl.getHealth() != oldHealth.get(0)) { //if player can damage entity, apply velocity and heal entity
                            Vector velocity = c.getLocation().toVector().subtract(l.toVector()).add(new Vector(0, 0.8, 0)).normalize();
                            c.setVelocity(velocity.multiply(1.5));
                            if(!cl.isDead()) { cl.setHealth(cl.getHealth() + 0.01); }
                        }
                    }
                    if(c instanceof Player || c instanceof Item) {
                        Vector velocity = c.getLocation().toVector().subtract(l.toVector()).add(new Vector(0, 0.8, 0)).normalize();
                        c.setVelocity(velocity.multiply(1.5));
                    }
                }
                Particle.DustOptions dust = new Particle.DustOptions(Color.fromRGB(185, 90, 219), 3);
                l.getWorld().spawnParticle(Particle.REDSTONE, l.getX(), l.getY(), l.getZ(), 200, 2, 2, 2, dust);
                l.getWorld().playSound(l, Sound.ENTITY_ENDER_DRAGON_FLAP, 10, 1);
            }
        }
    }
    @EventHandler
    public void onEntityExplode(EntityExplodeEvent e) {
        if(e.getEntity() instanceof Fireball && ((Fireball) e.getEntity()).getShooter() instanceof Player) {
            ((Fireball) e.getEntity()).setIsIncendiary(false);
            ((Fireball) e.getEntity()).setYield(0F);
            e.setCancelled(true);
        }
    }
}
