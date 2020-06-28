package com.ddylan.qmodsuite.staff.mode;

import com.ddylan.qmodsuite.qModSuite;
import com.ddylan.qmodsuite.staff.inspector.PlayerInspectorListener;
import com.ddylan.xlib.util.Color;
import com.ddylan.xlib.util.ItemBuilder;
import com.ddylan.xlib.visibility.VisibilityAction;
import com.lunarclient.bukkitapi.object.LCNotification;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.time.Duration;
import java.util.*;

public class StaffModeHandler {

    private final qModSuite modSuite;
    private final Set<UUID> staffSet;
    private final Set<UUID> visible;
    private final Map<UUID, ItemStack[]> lastSavedStaffInventories;
    private final Map<UUID, ItemStack[]> lastSavedStaffArmor;
    private final Map<UUID, GameMode> lastSavedGamemode;

    public StaffModeHandler(qModSuite modSuite) {
        this.modSuite = modSuite;
        staffSet = new HashSet<>();
        visible = new HashSet<>();
        lastSavedStaffInventories = new HashMap<>();
        lastSavedStaffArmor = new HashMap<>();
        lastSavedGamemode = new HashMap<>();

        modSuite.getServer().getPluginManager().registerEvents(new StaffModeListener(this), modSuite);
        modSuite.getServer().getPluginManager().registerEvents(new PlayerInspectorListener(this), modSuite);

        modSuite.getLibrary().getCommandHandler().registerClass(StaffModeCommand.class);

        modSuite.getLibrary().getVisibilityHandler().registerHandler("Staff-Mode", (paramPlayer1, paramPlayer2) -> {
            if (contains(paramPlayer1) && !paramPlayer2.hasPermission("qmodsuite.staff") && !visible.contains(paramPlayer1.getUniqueId())) {
                return VisibilityAction.HIDE;
            } else {
                return VisibilityAction.NEUTRAL;
            }
        });
    }

    public void onDisable() {
        for (UUID uuid : staffSet) {
            toggle(Bukkit.getPlayer(uuid));
        }
    }

    public boolean isVisible(Player player) {
        return visible.contains(player.getUniqueId());
    }

    public void toggleVisibility(Player player) {
        if (player == null) {
            return;
        }

        if (isVisible(player)) {
            visible.remove(player.getUniqueId());
        } else {
            visible.add(player.getUniqueId());
        }
        player.getInventory().setItem(8, getHotbar(player).get(8).build());
        player.updateInventory();
    }

    public void toggle(Player player) {
        if (player == null) {
            return;
        }

        player.sendMessage(Color.GOLD + "Mod Mode: " + (!contains(player) ? Color.GREEN + "Enabled":Color.RED + "Disabled"));
        modSuite.getLunarAPI().sendNotification(player, new LCNotification("You have toggled mod mode " + (!contains(player) ? "on":"off"), Duration.ofSeconds(5), LCNotification.Level.INFO));

        if (contains(player)) {
            remove(player);
            player.setGameMode(lastSavedGamemode.get(player.getUniqueId()));
            player.getInventory().setContents(lastSavedStaffInventories.get(player.getUniqueId()));
            player.getInventory().setArmorContents(lastSavedStaffArmor.get(player.getUniqueId()));
            for (Player online : Bukkit.getOnlinePlayers()) {
                modSuite.getLunarAPI().resetNametag(player, online);
            }

        } else {
            lastSavedGamemode.put(player.getUniqueId(), player.getGameMode());
            lastSavedStaffInventories.put(player.getUniqueId(), player.getInventory().getContents());
            lastSavedStaffArmor.put(player.getUniqueId(), player.getInventory().getArmorContents());
            player.setGameMode(GameMode.CREATIVE);
            player.getInventory().clear();
            getHotbar(player).forEach((integer, itemBuilder) -> player.getInventory().setItem(integer, itemBuilder.build()));
            add(player);
            for (Player staff : Bukkit.getOnlinePlayers()) {
                if (staff.hasPermission("qmodsuite.staff") && staffSet.contains(staff.getUniqueId())) {
                    for (Player online : Bukkit.getOnlinePlayers()) {
                        modSuite.getLunarAPI().overrideNametag(staff, Arrays.asList(Color.GRAY + "[Mod Mode]", staff.getDisplayName()), online);
                    }
                }
            }
        }

        player.updateInventory();
    }

    public ItemBuilder getVisibilityItem(Player player) {
        return (visible.contains(player.getUniqueId()) ? new ItemBuilder(Material.INK_SACK).name(Color.AQUA + "Become Invisible").durability(8):new ItemBuilder(Material.INK_SACK).name(Color.AQUA + "Become Visible").durability(10));
    }

    public ItemBuilder getInspector(Player player) {
        return new ItemBuilder(Material.BOOK).name(Color.AQUA + "Player Inspector");
    }

    private boolean remove(Player player) {
        return staffSet.remove(player.getUniqueId());
    }

    public boolean add(Player player) {
        return staffSet.add(player.getUniqueId());
    }

    public boolean contains(Player player) {
        return staffSet.contains(player.getUniqueId());
    }

    public Map<Integer, ItemBuilder> getHotbar(Player player) {
        Map<Integer, ItemBuilder> hotbar = new HashMap<>();
        hotbar.put(0, new ItemBuilder(Material.COMPASS).name(Color.AQUA + "Compass"));
        hotbar.put(1, getInspector(player));
        hotbar.put(2, new ItemBuilder(Material.WOOD_AXE).name(Color.AQUA + "* WorldEdit Wand *"));
        hotbar.put(3, new ItemBuilder(Material.CARPET).name(Color.AQUA + "Hide"));
        hotbar.put(7, new ItemBuilder(Material.SKULL_ITEM).name(Color.AQUA + "Online Staff"));
        hotbar.put(8, getVisibilityItem(player));
        return hotbar;
    }

    public boolean isHotbarItem(Player player, ItemStack itemStack) {
        for (ItemBuilder builder : getHotbar(player).values()) {
            if (itemStack != null) {
                if (itemStack.hasItemMeta()) {
                    if (builder.build().getItemMeta().equals(itemStack.getItemMeta())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
