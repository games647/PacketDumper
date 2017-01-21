package com.github.games647.packetdumper;

import com.google.common.collect.Sets;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.text.MessageFormat;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PacketFormatter {

    private final Logger logger;

    private boolean enabled;
    private Set<String> listeningPlayers = Sets.newHashSet();
    private Set<String> listeningPackets = Sets.newHashSet();
    private Set<PacketPhase> listeningPhase = Sets.newHashSet();

    public PacketFormatter(Logger logger) {
        this.logger = logger;
    }

    public void onIncoming(CommonPlayer sender, Object packet, PacketPhase phase) {
        if (isFiltered(sender, packet, phase)) {
            String packetName = packet.getClass().getSimpleName();
            String fields = ReflectionToStringBuilder.toString(packet, ToStringStyle.SHORT_PREFIX_STYLE);

            logger.log(Level.FINE, MessageFormat
                    .format("Incoming packet from {0}, Packet: {1}, Fields: {2}", sender, packetName, fields));
        }
    }

    public void onSent(CommonPlayer receiver, Object packet, PacketPhase phase) {
        if (isFiltered(receiver, packet, phase)) {
            String packetName = packet.getClass().getSimpleName();
            String fields = ReflectionToStringBuilder.toString(packet, ToStringStyle.SHORT_PREFIX_STYLE);

            logger.log(Level.FINE, MessageFormat
                    .format("Outgoing packet for {0}, Packet: {1}, Fields: {2}", receiver, packetName, fields));
        }
    }

    public String onCommand(String command, String[] args) {
        if (command.equals("packet-listener")) {
            return onListenerToggle(args);
        }

        if (args.length < 3) {
            return "Not enough arguments";
        }

        boolean add;
        if (args[0].equals("add")) {
            add = true;
        } else if (args[0].equals("remove")) {
            add = false;
        } else {
            return "Only add or remove is allowed for the first argument";
        }

        if (args[1].equals("player")) {
            return onPlayerChange(args, add);
        } else if (args[1].equals("phase")) {
            return onPhaseChange(args[2], add);
        } else if (args[1].equals("packet")) {
            return onPacketChange(args, add);
        }

        return "Unknown filter argument";
    }

    private String onPacketChange(String[] args, boolean add) {
        if (add) {
            listeningPackets.add(args[2]);
            return "Added packet";
        }

        listeningPackets.remove(args[2]);
        return "Removed packet";
    }

    private String onPhaseChange(String arg, boolean add) {
        PacketPhase packetPhase = PacketPhase.valueOf(arg);
        if (packetPhase == null) {
            return "Unknown packet phase";
        }

        if (add) {
            listeningPhase.add(packetPhase);
            return "Added phase filter";
        }

        listeningPhase.remove(packetPhase);
        return "Removed phase filter";
    }

    private String onPlayerChange(String[] args, boolean add) {
        if (add) {
            listeningPlayers.add(args[2]);
            return "Added player";
        }

        listeningPlayers.remove(args[2]);
        return "Removed player";
    }

    private boolean isFiltered(CommonPlayer player, Object packet, PacketPhase phase) {
        return enabled && logger.isLoggable(Level.FINE)
                && (listeningPlayers.isEmpty() || listeningPlayers.contains(player.getPlayerName()))
                && (listeningPackets.isEmpty() || listeningPackets.contains(packet.getClass().getSimpleName()))
                && (listeningPackets.isEmpty() || listeningPackets.contains(phase));
    }

    private String onListenerToggle(String[] args) {
        if (args[0].equals("start")) {
            enabled = true;
            return "Started logging";
        } else if (args[0].equals("stop")) {
            enabled = false;
            return "Stopped logging";
        }

        return "Unknown argument";
    }
}
