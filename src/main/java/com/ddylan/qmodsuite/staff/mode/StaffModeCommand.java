package com.ddylan.qmodsuite.staff.mode;

import com.ddylan.qmodsuite.qModSuite;
import com.ddylan.library.command.Command;
import org.bukkit.entity.Player;

public class StaffModeCommand {

    @Command(names = {"h", "hacker", "staffmode", "mm", "modmode"}, permission = "qmodsuite.staff")
    public static void hacker(Player player) {
        qModSuite.getInstance().getStaffModeHandler().toggle(player);
    }

}
