# PacketDumper

## Description

This plugin listens to Minecraft packets and logs them with their content to the console. It tries to serialize
every packet to a human readable format. Furthermore it filters packets if you want to only listen to specific packets
or only packet to/from a specific player.

## Features

* Listens to incoming and outgoing packets
* Display packet contents
* Display sender and receiver information for that player

## ToDo

* Filtering packet ids
* Filter packet phase
* Filter packet receiver/sender
* Try to deobfuscate fields if possible

## Commands

    none at the moment

## Supported Plugins/Platforms

* ProtocolLib (Bukkit)
* PacketListenerAPI (Bukkit)
* BungeePacketListenerAPI (Bungee)