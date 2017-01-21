package com.github.games647.packetdumper.hooks.bungee;

import com.github.games647.packetdumper.PacketDumperBungee;
import com.github.games647.packetdumper.PacketPhase;

import de.inventivegames.packetlistener.handler.PacketHandler;
import de.inventivegames.packetlistener.handler.ReceivedPacket;
import de.inventivegames.packetlistener.handler.SentPacket;

import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * PacketListener using bungeepacketlistenerapi for Bungee
 */
public class BungeePacketListener extends PacketHandler {

    private final PacketDumperBungee plugin;

    public BungeePacketListener(PacketDumperBungee plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onSend(SentPacket sentPacket) {
        ProxiedPlayer receiver = sentPacket.getPlayer();
        Object packet = sentPacket.getPacket();
        PacketPhase phase = plugin.getProtocolMap().get(packet.getClass());
        plugin.getPacketFormatter().onSent(plugin.convertPlayer(receiver), packet, phase);
    }

    @Override
    public void onReceive(ReceivedPacket receivedPacket) {
        ProxiedPlayer sender = receivedPacket.getPlayer();
        Object packet = receivedPacket.getPacket();
        PacketPhase phase = plugin.getProtocolMap().get(packet.getClass());
        plugin.getPacketFormatter().onSent(plugin.convertPlayer(sender), packet, phase);
    }

    public static void register(PacketDumperBungee plugin) {
        PacketHandler.addHandler(new BungeePacketListener(plugin));
    }
}
