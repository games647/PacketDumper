package com.github.games647.packetdumper;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PacketFormatter {

    private final Logger logger;
    private boolean enabled;

    public PacketFormatter(Logger logger) {
        this.logger = logger;
    }

    public void onIncoming(CommonPlayer sender, Object packet) {
        if (enabled && logger.isLoggable(Level.FINE)) {
            String packetName = packet.getClass().getSimpleName();
            String fields = ReflectionToStringBuilder.toString(packet, ToStringStyle.SIMPLE_STYLE);

            logger.log(Level.FINE, MessageFormat
                    .format("Incoming packet from {0}, Packet: {1}, Fields: {2}", sender, packetName, fields));
        }
    }

    public void onSent(CommonPlayer receiver, Object packet) {
        if (enabled && logger.isLoggable(Level.FINE)) {
            String packetName = packet.getClass().getSimpleName();
            String fields = ReflectionToStringBuilder.toString(packet, ToStringStyle.SIMPLE_STYLE);

            logger.log(Level.FINE, MessageFormat
                    .format("Outgoing packet for {0}, Packet: {1}, Fields: {2}", receiver, packetName, fields));
        }
    }

    public String onCommand(String command, String[] args) {
        if (command.equals("packet-listener")) {
            if (args.length > 0) {
                if (args[0].equals("start")) {
                    enabled = true;
                    return "Started logging";
                } else if (args[0].equals("stop")) {
                    enabled = false;
                    return "Stopped logging";
                } else {
                    return "Unknown argument";
                }
            }

            return "Missing start/stop command";
        }

        return "";
    }
}
