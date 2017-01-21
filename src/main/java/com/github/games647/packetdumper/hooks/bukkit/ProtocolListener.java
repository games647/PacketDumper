package com.github.games647.packetdumper.hooks.bukkit;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.github.games647.packetdumper.PacketDumperBukkit;

import com.github.games647.packetdumper.PacketPhase;
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
        PacketType.Protocol protocol = packetEvent.getPacketType().getProtocol();
        plugin.getPacketFormatter().onIncoming(plugin.convertPlayer(sender), packet.getHandle(), convert(protocol));
    }

    @Override
    public void onPacketSending(PacketEvent packetEvent) {
        Player receiver = packetEvent.getPlayer();
        PacketContainer packet = packetEvent.getPacket();
        PacketType.Protocol protocol = packetEvent.getPacketType().getProtocol();
        plugin.getPacketFormatter().onSent(plugin.convertPlayer(receiver), packet.getHandle(), convert(protocol));
    }

    public static void register(PacketDumperBukkit plugin) {
        ProtocolListener listener = new ProtocolListener(plugin);
        ProtocolLibrary.getProtocolManager().addPacketListener(listener);
    }

    private PacketPhase convert(PacketType.Protocol protocol) {
        switch (protocol) {
            case HANDSHAKING:
                return PacketPhase.HANDSHAKE;
            case STATUS:
                return PacketPhase.STATUS;
            case LOGIN:
                return PacketPhase.LOGIN;
            case PLAY:
            case LEGACY:
            default:
                return PacketPhase.PLAY;
        }
    }
}
