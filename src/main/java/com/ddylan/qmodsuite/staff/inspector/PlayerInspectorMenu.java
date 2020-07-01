package com.ddylan.qmodsuite.staff.inspector;

import com.ddylan.library.menu.Button;
import com.ddylan.library.menu.Menu;
import com.ddylan.library.util.Color;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class PlayerInspectorMenu extends Menu {

    private final PlayerInspector playerInspector;

    @Override
    public String getTitle(Player player) {
        return Color.YELLOW + "Viewing: " + Color.RESET + playerInspector.getTarget().getDisplayName();
    }

    private boolean locked() {
        return false;
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        for (int i = 9; i < 36; i++) {
            buttons.put(buttons.size(), new DisplayButton(buttons.size()));
        }

        for (int i = 0; i < 9; i++) {
            buttons.put(buttons.size(), new DisplayButton(buttons.size()));
        }

        for (ItemStack itemStack : playerInspector.getTarget().getInventory().getArmorContents()) {
            buttons.put(buttons.size(), new DisplayButton(buttons.size()));
        }


        return buttons;
    }

    @Override
    public boolean isUpdateAfterClick() {
        return true;
    }

    @Override
    public boolean isAutoUpdate() {
        return true;
    }

    /**
     * The reason these numbers are so odd is to offset bukkit's 'natural' inventory slots.
     * We make it more reasonable for the 'viewer' or implementer.
     */
    @AllArgsConstructor
    @Getter
    private class DisplayButton extends Button {

        private final int slot;

        @Override
        public ItemStack getButtonItem(Player player) {
            if (slot <= 26) {
                return playerInspector.getTarget().getInventory().getItem(slot + 9);
            } else if (slot <= 35) {
                return playerInspector.getTarget().getInventory().getItem(slot - 27);
            } else if (slot <= 39) {
                return playerInspector.getTarget().getInventory().getItem(slot - 36);
            }
            return playerInspector.getTarget().getItemInHand();
        }

        @Override
        public boolean shouldUpdate(Player player, ClickType clickType) {
            return true;
        }

        @Override
        public boolean shouldCancel(Player player, ClickType clickType) {
            return locked();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack cursor, ItemStack current) {
            if (!locked()) {
                if (this.slot <= 26) {
                    player.getOpenInventory().setCursor(playerInspector.getTarget().getInventory().getItem(this.slot + 9));
                    playerInspector.getTarget().getInventory().setItem(this.slot + 9, current);
                } else if (this.slot <= 35) {
                    player.getOpenInventory().setCursor(playerInspector.getTarget().getInventory().getItem(this.slot - 27));
                    playerInspector.getTarget().getInventory().setItem(this.slot - 27, current);
                } else if (this.slot <= 39) {
                    player.getOpenInventory().setCursor(playerInspector.getTarget().getInventory().getItem(this.slot - 36));
                    playerInspector.getTarget().getInventory().setItem(this.slot - 36, current);
                }
            }
        }
    }

}
