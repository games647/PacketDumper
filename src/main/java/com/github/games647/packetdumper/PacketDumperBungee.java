package com.github.games647.packetdumper;

import com.github.games647.packetdumper.hooks.bungee.BungeePacketListener;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

public class PacketDumperBungee extends Plugin {

    private PacketFormatter packetFormatter;

    @Override
    public void onEnable() {
        this.packetFormatter = new PacketFormatter(getLogger());

        if (getProxy().getPluginManager().getPlugin("BungeePacketListenerApi") != null) {
            BungeePacketListener.register(this);
        } else {
            getLogger().warning("No packet listener API found");
        }
    }

    public PacketFormatter getPacketFormatter() {
        return packetFormatter;
    }

    public CommonPlayer convertPlayer(ProxiedPlayer bungeePlayer) {
        return new CommonPlayer(bungeePlayer.getUniqueId(), bungeePlayer.getName(), bungeePlayer.getAddress());
    }
}
