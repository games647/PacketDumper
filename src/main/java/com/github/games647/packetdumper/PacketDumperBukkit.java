package com.github.games647.packetdumper;

import com.github.games647.packetdumper.hooks.bukkit.PacketAPIListener;
import com.github.games647.packetdumper.hooks.bukkit.ProtocolListener;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class PacketDumperBukkit extends JavaPlugin {

    private PacketFormatter packetFormatter;

    @Override
    public void onEnable() {
        this.packetFormatter = new PacketFormatter(getLogger());

        if (getServer().getPluginManager().isPluginEnabled("ProtocolLib")) {
            ProtocolListener.register(this);
        } else if (getServer().getPluginManager().isPluginEnabled("PacketListenerAPI")) {
            PacketAPIListener.register(this);
        } else {
            getLogger().warning("No packet listener API found");
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage(packetFormatter.onCommand(command.getName(), args));
        return true;
    }

    public PacketFormatter getPacketFormatter() {
        return packetFormatter;
    }

    public CommonPlayer convertPlayer(Player bukkitPlayer) {
        return new CommonPlayer(bukkitPlayer.getUniqueId(), bukkitPlayer.getName(), bukkitPlayer.getAddress());
    }
}
