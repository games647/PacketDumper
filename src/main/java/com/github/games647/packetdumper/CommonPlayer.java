package com.github.games647.packetdumper;

import java.net.SocketAddress;
import java.util.UUID;

public class CommonPlayer {

    private final UUID uuid;
    private final String playerName;
    private final SocketAddress address;

    public CommonPlayer(UUID uuid, String playerName, SocketAddress address) {
        this.uuid = uuid;
        this.playerName = playerName;
        this.address = address;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getPlayerName() {
        return playerName;
    }

    public SocketAddress getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return "CommonPlayer{" +
                "uuid=" + uuid +
                ", playerName='" + playerName + '\'' +
                ", address=" + address +
                '}';
    }
}
