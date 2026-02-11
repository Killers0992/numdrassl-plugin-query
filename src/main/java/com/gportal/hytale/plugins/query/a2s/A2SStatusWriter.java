package com.gportal.hytale.plugins.query.a2s;

import com.gportal.a2s.PlayerInfo;
import com.gportal.a2s.QueryServer;
import com.gportal.a2s.ServerInfo;
import com.gportal.hytale.plugins.query.MainClass;
import com.gportal.hytale.plugins.query.Models.PluginConfig;
import me.internalizable.numdrassl.api.Numdrassl;
import org.slf4j.Logger;

import java.net.InetSocketAddress;

public class A2SStatusWriter {
    private final Logger logger;

    private QueryServer server;

    public A2SStatusWriter(Logger logger) {
        this.logger = logger;
    }

    public void start(String host, int port) {
        ServerInfo info = createServerInfo(host);
        server = new QueryServer(new InetSocketAddress(host, port), info);

        updateStatus();

        logger.info("A2S Query server started on {}:{}", host, port);
    }

    public void stop() {
        if (server != null) {
            server.shutdown();
            logger.info("A2S Query server stopped");
        }
    }

    private ServerInfo createServerInfo(String host) {
        return new ServerInfo(
                new InetSocketAddress(host, 28001),
                (byte) 17,
                PluginConfig.Singleton.ServerName,
                "proxy",
                "hytale",
                "Hytale",
                (short) 0,
                (byte) Numdrassl.getProxy().getPlayerCount(),
                (byte) 100,
                (byte) 0,
                'd',
                'l',
                false,
                false,
                Numdrassl.getProxy().getVersion(),
                (short) PluginConfig.Singleton.GameServerPort,
                null,
                null,
                null,
                null,
                null
        );
    }

    public void updateStatus() {
        if (server == null) return;

        // Update player count
        server.info.setPlayers((byte) Numdrassl.getProxy().getPlayerCount());

        // Update players list
        server.players.clear();

        byte id = 0;
        for (var player : Numdrassl.getProxy().getAllPlayers()){
            server.players.add(new PlayerInfo(id++, player.getUsername(), (short)0, 0));
        }
    }

    public QueryServer getServer() {
        return server;
    }
}
