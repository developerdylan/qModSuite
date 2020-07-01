package com.ddylan.qmodsuite.staff.mode;

import com.ddylan.library.util.Color;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class StaffModeListener implements Listener {

    private final StaffModeHandler staffModeHandler;

    public StaffModeListener(StaffModeHandler staffModeHandler) {
        this.staffModeHandler = staffModeHandler;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (staffModeHandler.contains(player)) {
            if (staffModeHandler.isHotbarItem(player, event.getCursor()) || staffModeHandler.isHotbarItem(player, event.getCurrentItem())) {
                event.setCancelled(true);
                event.setResult(Event.Result.DENY);
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction().name().contains("RIGHT")) {
            if (staffModeHandler.contains(event.getPlayer())) {
                if (staffModeHandler.isHotbarItem(player, player.getItemInHand())) {
                    event.setCancelled(true);
                    if (event.getPlayer().getItemInHand().getItemMeta().equals(staffModeHandler.getHotbar(player).get(8).build().getItemMeta())) {
                        staffModeHandler.toggleVisibility(player);
                    }
                    player.updateInventory();
                }
            }
        }
    }

    @EventHandler
    public void onProjectile(ProjectileLaunchEvent event) {
        if (event.getEntity().getShooter() instanceof Player) {
            Player player = (Player) event.getEntity().getShooter();
            if (staffModeHandler.contains(player)) {
                event.setCancelled(true);
                event.getEntity().remove();
                player.sendMessage(Color.RED + Color.BOLD + "You cannot do this in mod mode!");
                player.updateInventory();
            }
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();

        if (staffModeHandler.contains(player)) {
            if (!staffModeHandler.isVisible(player) || staffModeHandler.isHotbarItem(player, event.getItemDrop().getItemStack())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();

        if (staffModeHandler.contains(player)) {
            if (!staffModeHandler.isVisible(player) || staffModeHandler.isHotbarItem(player, event.getItem().getItemStack())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (staffModeHandler.contains(player)) {
            if (player.hasMetadata("build")) {
                if (player.getMetadata("build").get(0).asBoolean()) {
                    return;
                }
            }

            if (!staffModeHandler.isVisible(player)) {
                event.setCancelled(true);
                player.sendMessage(Color.RED + Color.BOLD + "You cannot do this in mod mode!");
                player.updateInventory();
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (staffModeHandler.contains(player)) {
            if (player.hasMetadata("build")) {
                if (player.getMetadata("build").get(0).asBoolean()) {
                    return;
                }
            }

            if (!staffModeHandler.isVisible(player)) {
                event.setCancelled(true);
                player.sendMessage(Color.RED + Color.BOLD + "You cannot do this in mod mode!");
                player.updateInventory();
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            if (staffModeHandler.contains(player)) {
                event.setCancelled(true);
                player.sendMessage(Color.RED + Color.BOLD + "You cannot do this in mod mode!");
                player.updateInventory();
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onQuit(PlayerQuitEvent event) {
        if (staffModeHandler.contains(event.getPlayer())) {
            staffModeHandler.toggle(event.getPlayer());
        }
    }

}
