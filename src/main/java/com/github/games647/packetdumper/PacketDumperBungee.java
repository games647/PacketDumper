package com.github.games647.packetdumper;

import com.github.games647.packetdumper.hooks.bungee.BungeePacketListener;
import com.google.common.collect.Maps;

import gnu.trove.map.TObjectIntMap;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.protocol.DefinedPacket;
import net.md_5.bungee.protocol.Protocol;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.logging.Level;

public class PacketDumperBungee extends Plugin {

    private PacketFormatter packetFormatter;
    private final Map<Class<? extends DefinedPacket>, PacketPhase> protocolMap = Maps.newHashMap();

    @Override
    public void onEnable() {
        this.packetFormatter = new PacketFormatter(getLogger());

        if (getProxy().getPluginManager().getPlugin("BungeePacketListenerApi") != null) {
            BungeePacketListener.register(this);
        } else {
            getLogger().warning("No packet listener API found");
        }

        try {
            Field field = Protocol.DirectionData.class.getDeclaredField("packetMap");
            field.setAccessible(true);
            for (Protocol protocol : Protocol.values()) {
                TObjectIntMap<Class<DefinedPacket>> packetMap = (TObjectIntMap<Class<DefinedPacket>>) field.get(protocol.TO_CLIENT);
                PacketPhase phase = convert(protocol);
                for (Class<DefinedPacket> packet : packetMap.keySet()) {
                    protocolMap.put(packet, phase);
                }
            }
        } catch (Exception ex) {
            getLogger().log(Level.WARNING, "Cannot init packet phases", ex);
        }

        getProxy().getPluginManager().registerCommand(this
                , new Command("packet-listener", getDescription().getName().toLowerCase()) {
                    @Override
                    public void execute(CommandSender sender, String[] args) {
                        String legacyResponse = packetFormatter.onCommand("packet-listener", args);
                        sender.sendMessage(TextComponent.fromLegacyText(legacyResponse));
                    }
                });

        getProxy().getPluginManager().registerCommand(this
                , new Command("packet-filter", getDescription().getName().toLowerCase()) {
                    @Override
                    public void execute(CommandSender sender, String[] args) {
                        String legacyResponse = packetFormatter.onCommand("packet-filter", args);
                        sender.sendMessage(TextComponent.fromLegacyText(legacyResponse));
                    }
                });
    }

    public PacketFormatter getPacketFormatter() {
        return packetFormatter;
    }

    public Map<Class<? extends DefinedPacket>, PacketPhase> getProtocolMap() {
        return protocolMap;
    }

    public CommonPlayer convertPlayer(ProxiedPlayer bungeePlayer) {
        return new CommonPlayer(bungeePlayer.getUniqueId(), bungeePlayer.getName(), bungeePlayer.getAddress());
    }

    private PacketPhase convert(Protocol protocol) {
        switch (protocol) {
            case HANDSHAKE:
                return PacketPhase.HANDSHAKE;
            case STATUS:
                return PacketPhase.STATUS;
            case LOGIN:
                return PacketPhase.LOGIN;
            case GAME:
            default:
                return PacketPhase.PLAY;
        }
    }
}
