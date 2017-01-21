package com.github.games647.packetdumper.hooks.bukkit;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.github.games647.packetdumper.PacketDumperBukkit;

import org.bukkit.entity.Player;

/**
 * PacketListener using ProtocolLib in Bukkit
 */
public class ProtocolListener extends PacketAdapter {

    private final PacketDumperBukkit plugin;

    public ProtocolListener(PacketDumperBukkit plugin) {
        super(params().plugin(plugin));

        this.plugin = plugin;
    }

    @Override
    public void onPacketReceiving(PacketEvent packetEvent) {
        Player sender = packetEvent.getPlayer();
        PacketContainer packet = packetEvent.getPacket();
        plugin.getPacketFormatter().onIncoming(plugin.convertPlayer(sender), packet.getHandle());
    }

    @Override
    public void onPacketSending(PacketEvent packetEvent) {
        Player receiver = packetEvent.getPlayer();
        PacketContainer packet = packetEvent.getPacket();
        plugin.getPacketFormatter().onSent(plugin.convertPlayer(receiver), packet.getHandle());
    }

    public static void register(PacketDumperBukkit plugin) {
        ProtocolListener listener = new ProtocolListener(plugin);
        ProtocolLibrary.getProtocolManager().addPacketListener(listener);
    }
}
