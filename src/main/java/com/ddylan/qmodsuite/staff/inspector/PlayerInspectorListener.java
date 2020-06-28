package com.ddylan.qmodsuite.staff.inspector;

import com.ddylan.qmodsuite.staff.mode.StaffModeHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class PlayerInspectorListener implements Listener {

    private final StaffModeHandler handler;

    public PlayerInspectorListener(StaffModeHandler handler) {
        this.handler = handler;
    }

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        if (event.getRightClicked() instanceof Player) {
            Player clicked = (Player) event.getRightClicked();
            if (handler.contains(player)) {
                if (player.getItemInHand() != null) {
                    if (player.getItemInHand().hasItemMeta()) {
                        if (player.getItemInHand().getItemMeta().equals(handler.getInspector(player).build().getItemMeta())) {
                            new PlayerInspectorMenu(new PlayerInspector(clicked, player)).openMenu(player);
                        }
                    }
                }
            }
        }
    }

}
