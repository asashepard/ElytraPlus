package com.gmail.creepycucumber1.elytraplus;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.gmail.creepycucumber1.elytraplus.Items.*;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.*;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public class ElytraPlus extends JavaPlugin {

    private static ElytraPlus plugin;

    @Override
    public void onEnable() {
        plugin = this;
        PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvents(new EventManager(this), this);
        ModifyFlight();
        doParticles();

        //custom item initialization
        Essence.init();
        Fragment.init();
        AmethystElytra.init();
        NetheriteElytra.init();
        Elytra.init();

        //ProtocolLib
        ProtocolManager manager = ProtocolLibrary.getProtocolManager();
        manager.addPacketListener(new PacketAdapter(plugin, ListenerPriority.NORMAL, PacketType.Play.Client.AUTO_RECIPE) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                Player p = event.getPlayer();

                if(String.valueOf(packet.getStructures().read(0)).equalsIgnoreCase(
                        "InternalStructure[handle=minecraft:aelytra (MinecraftKey)]") ||
                        String.valueOf(packet.getStructures().read(0)).equalsIgnoreCase(
                                "InternalStructure[handle=minecraft:nelytra (MinecraftKey)]") ||
                        String.valueOf(packet.getStructures().read(0)).equalsIgnoreCase(
                                "InternalStructure[handle=minecraft:elytra (MinecraftKey)]") ||
                        String.valueOf(packet.getStructures().read(0)).equalsIgnoreCase(
                                "InternalStructure[handle=minecraft:fragment (MinecraftKey)]")) {

                    TextComponent message = new TextComponent(ChatColor.GREEN + "This is a custom recipe! Look to Discord for instructions.");
                    message.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://discord.hungermc.com"));
                    message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Visit the HungerMC Discord!")));

                    p.spigot().sendMessage(message);
                    event.setCancelled(true);
                }
            }
        });

        getLogger().info("ElytraPlus has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("ElytraPlus has been disabled!");
    }

    public void ModifyFlight() {

        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {

            @Override
            public void run() {

                ArrayList<Player> aflight = new ArrayList<>();
                ArrayList<Player> nflight = new ArrayList<>();

                for (Player p : plugin.getServer().getOnlinePlayers()) {
                    //AMETHYST
                    if (p.getInventory().getChestplate() != null && p.getInventory().getChestplate().getItemMeta().hasCustomModelData() &&
                            p.getInventory().getChestplate().getItemMeta().getCustomModelData() == 1) { //netherite
                        if (p.isGliding()) {
                            aflight.add(p);
                        }
                    }
                    //NETHERITE
                    if (p.getInventory().getChestplate() != null && p.getInventory().getChestplate().getItemMeta().hasCustomModelData() &&
                            p.getInventory().getChestplate().getItemMeta().getCustomModelData() == 2) { //netherite
                        if (p.isGliding()) {
                            nflight.add(p);
                        }
                    }

                    double multiplier = 1;

                    //adds velocity to amethyst fliers
                    if (!aflight.isEmpty()) {
                        for (Player ap : aflight) {
                            double pitch = ap.getLocation().getDirection().getY();
                            //requirements: facing down, going down but not too much, speed is slow enough and fast enough
                            if (pitch < 0) {

                                if (pitch > -70 && ap.getVelocity().getY() < 0 && ap.getVelocity().length() < 2.6 &&
                                        ap.getVelocity().length() > 0.5 && ap.getVelocity().length() < (Math.abs(pitch) + 0.5) * 2) { //flatter angles have lower max for multiplication

                                    multiplier = 1.01 + 0.085 * (Math.abs(pitch) * 0.5); }
                                ap.setVelocity(ap.getVelocity().multiply(multiplier));

                            } else if (ap.getVelocity().length() < 2.6) { //facing up and/or going up still small increase
                                ap.setVelocity(ap.getVelocity().multiply(1.00005 + 0.00025 * pitch));
                            }
                        }
                    }
                    //takes velocity from netherite fliers
                    if (!nflight.isEmpty()) {
                        for (Player np : nflight) {
                            double pitch = np.getLocation().getDirection().getY();
                            //requirements: speed is fast enough
                            if (np.getVelocity().length() > 0.6) {
                                multiplier = 0.96 + 0.05 * (Math.abs(pitch)); //faster when facing straight down, slower otherwise
                            }
                            np.setVelocity(np.getVelocity().multiply(multiplier).add(new Vector(0, -0.025, 0))); //constant downward pull
                        }
                    }
                }
            }
        }, 0, 2);
    }

    public static int part;

    public void doParticles() {

        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                for (Player p : plugin.getServer().getOnlinePlayers()) {

                    double deltax = p.getLocation().getDirection().getX() * 0.5;
                    double deltaz = p.getLocation().getDirection().getZ() * 0.5;
                    double deltay = 0;

                    if (p.isGliding()) {
                        part = 7;
                        deltay = 0.3;
                    } else {
                        part = 1;
                        deltay = 1;
                    }

                    //AMETHYST
                    if (p.getInventory().getChestplate() != null && p.getInventory().getChestplate().getItemMeta().hasCustomModelData() &&
                            p.getInventory().getChestplate().getItemMeta().getCustomModelData() == 1) { //amethyst

                        //add particle
                        Location l = p.getLocation();
                        l.setY(l.getY() + deltay);
                        //behind the player
                        l.setX(l.getX() - deltax);
                        l.setZ(l.getZ() - deltaz);

                        Particle.DustOptions dust = new Particle.DustOptions(Color.fromRGB(207, 160, 243), 1);
                        l.getWorld().spawnParticle(Particle.REDSTONE, l.getX(), l.getY(), l.getZ(), part, 0.2, 0.2, 0.2, dust);
                    }
                    //NETHERITE
                    else if (p.getInventory().getChestplate() != null && p.getInventory().getChestplate().getItemMeta().hasCustomModelData() &&
                            p.getInventory().getChestplate().getItemMeta().getCustomModelData() == 2) { //netherite

                        //add particle
                        Location l = p.getLocation();
                        l.setY(l.getY() + deltay);
                        //behind the player
                        l.setX(l.getX() - deltax);
                        l.setZ(l.getZ() - deltaz);

                        Particle.DustOptions dust = new Particle.DustOptions(Color.fromRGB(56, 53, 50), 1);
                        l.getWorld().spawnParticle(Particle.REDSTONE, l.getX(), l.getY(), l.getZ(), part, 0.2, 0.2, 0.2, dust);
                    }
                }
            }
        }, 0, 5);
    }
}
