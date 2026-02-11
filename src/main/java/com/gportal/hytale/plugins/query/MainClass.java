package com.gportal.hytale.plugins.query;

import com.gportal.hytale.plugins.query.Models.PluginConfig;
import com.gportal.hytale.plugins.query.a2s.A2SQueryManager;
import me.internalizable.numdrassl.api.event.Subscribe;
import me.internalizable.numdrassl.api.event.proxy.ProxyInitializeEvent;
import me.internalizable.numdrassl.api.plugin.DataDirectory;
import me.internalizable.numdrassl.api.plugin.Inject;
import me.internalizable.numdrassl.api.plugin.Plugin;
import org.slf4j.Logger;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

@Plugin(
        id = "dev.killers0992.numdrassl-plugin-query",
        name = "NumdrasslPluginQuery",
        version = "1.0.0",
        authors = {"Killers0992"},
        description = "HytalePluginQuery ported to Numdrassl proxy"
)
public class MainClass {

    public static final String Default_Query_Host = "0.0.0.0";
    public static final int Default_Query_Port = 27015;
    public static final int Default_GameServer_Port = 5520;
    public static final String Default_Server_Name = "Proxy server";

    @Inject
    public Logger logger;

    @Inject
    @DataDirectory
    private Path dataDirectory;

    public Yaml yaml;

    @Subscribe
    public void onProxyInitialize(ProxyInitializeEvent event) {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setPrettyFlow(true);
        options.setIndent(2);
        yaml = new Yaml(options);

        LoadConfig();

        A2SQueryManager a2sQueryManager = new A2SQueryManager(this, logger);

        a2sQueryManager.start();
    }

    public void LoadConfig(){
        Path configPath = dataDirectory.resolve("config.yml");

        CreateDefault(configPath);

        try (Reader reader = Files.newBufferedReader(configPath)) {

            Map<String, Object> data = yaml.load(reader);

            PluginConfig.Singleton = new PluginConfig();

            PluginConfig.Singleton.QueryHost = (String) data.getOrDefault("query_host", Default_Query_Host);
            PluginConfig.Singleton.QueryPort = (int) data.getOrDefault("query_port", Default_Query_Port);
            PluginConfig.Singleton.GameServerPort = (int) data.getOrDefault("gameserver_port", Default_GameServer_Port);
            PluginConfig.Singleton.ServerName = (String) data.getOrDefault("server_name", Default_Server_Name);

            logger.debug("Loaded config");
        } catch (IOException e) {
            logger.error("Failed to load config file: {}", configPath, e);
        }
    }

    private void CreateDefault(Path configPath){
        if (!Files.exists(configPath)) {
            Map<String, Object> defaultConfig = new LinkedHashMap<>();

            defaultConfig.put("query_host", Default_Query_Host);
            defaultConfig.put("query_port", Default_Query_Port);
            defaultConfig.put("gameserver_port", Default_GameServer_Port);
            defaultConfig.put("server_name", Default_Server_Name);

            try (Writer writer = Files.newBufferedWriter(configPath)) {
                yaml.dump(defaultConfig, writer);
            } catch (IOException e) {
                logger.error("Failed to write default config: {}", configPath, e);
            }

            logger.info("Created default config file");
        }
    }
}
