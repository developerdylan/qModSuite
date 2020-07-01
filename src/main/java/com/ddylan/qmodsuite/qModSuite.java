package com.ddylan.qmodsuite;

import com.ddylan.qmodsuite.staff.mode.StaffModeHandler;
import com.ddylan.library.LibraryPlugin;
import com.lunarclient.bukkitapi.LunarClientAPI;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class qModSuite extends JavaPlugin {

    @Getter private static qModSuite instance;

    private StaffModeHandler staffModeHandler;

    private LunarClientAPI lunarAPI;
    private LibraryPlugin LibraryPlugin;

    @Override
    public void onEnable() {
        instance = this;

        lunarAPI = (LunarClientAPI) getServer().getPluginManager().getPlugin("LunarClient-API");
        LibraryPlugin = (LibraryPlugin) getServer().getPluginManager().getPlugin("Library");

        staffModeHandler = new StaffModeHandler(instance);
    }

    @Override
    public void onDisable() {
        staffModeHandler.onDisable();
        instance = null;
    }
}
