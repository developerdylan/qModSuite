package com.ddylan.qmodsuite.staff.inspector;

import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
public class PlayerInspector {

    private final Player target, viewer;

    public PlayerInspector(Player target, Player viewer) {
        this.target = target;
        this.viewer = viewer;
    }

    //TODO logs maybe? unlock function? idk

}
