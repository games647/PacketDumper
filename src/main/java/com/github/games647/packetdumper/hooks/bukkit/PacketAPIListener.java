package com.github.games647.packetdumper.hooks.bukkit;

import com.github.games647.packetdumper.PacketDumperBukkit;

import org.bukkit.entity.Player;
import org.inventivetalent.packetlistener.PacketListenerAPI;
import org.inventivetalent.packetlistener.handler.PacketHandler;
import org.inventivetalent.packetlistener.handler.ReceivedPacket;
import org.inventivetalent.packetlistener.handler.SentPacket;

/**
 * PacketListener using PacketListenerAPI in Bukkit
 */
public class PacketAPIListener extends PacketHandler {

    private final PacketDumperBukkit plugin;

    public PacketAPIListener(PacketDumperBukkit plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onSend(SentPacket sentPacket) {
        Player receiver = sentPacket.getPlayer();
        Object packet = sentPacket.getPacket();
        plugin.getPacketFormatter().onSent(plugin.convertPlayer(receiver), packet, null);
    }

    @Override
    public void onReceive(ReceivedPacket receivedPacket) {
        Player sender = receivedPacket.getPlayer();
        Object packet = receivedPacket.getPacket();
        plugin.getPacketFormatter().onSent(plugin.convertPlayer(sender), packet, null);
    }

    public static void register(PacketDumperBukkit plugin) {
        PacketAPIListener handler = new PacketAPIListener(plugin);
        PacketListenerAPI.addPacketHandler(handler);
    }
}
