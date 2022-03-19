package de.rasorsystems.main;

import de.rasorsystems.commands.CMD_Clan;
import de.rasorsystems.main.clan.MySQL;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.CopyOption;
import java.nio.file.Files;

public class Main extends Plugin {

    private Configuration configuration;
    public static Main main;

    public Main() {
    }

    @Override
    public void onEnable() {
        main = this;
        this.saveDefaultConfig();

        try {
            this.configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(this.getDataFolder(), "config.yml"));
        } catch (IOException var2) {
            var2.printStackTrace();
        }
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new CMD_Clan("clan"));
        MySQL.connect();
    }

    @Override
    public void onDisable() {
        MySQL.close();
    }
    private void saveDefaultConfig() {
        if (!this.getDataFolder().exists()) {
            this.getDataFolder().mkdir();
        }

        File file = new File(this.getDataFolder(), "config.yml");
        if (!file.exists()) {
            try {
                InputStream in = this.getResourceAsStream("config.yml");
                Throwable var3 = null;

                try {
                    Files.copy(in, file.toPath(), new CopyOption[0]);
                } catch (Throwable var13) {
                    var3 = var13;
                    throw var13;
                } finally {
                    if (in != null) {
                        if (var3 != null) {
                            try {
                                in.close();
                            } catch (Throwable var12) {
                                var3.addSuppressed(var12);
                            }
                        } else {
                            in.close();
                        }
                    }

                }
            } catch (IOException var15) {
                var15.printStackTrace();
            }
        }

    }

    public Configuration getConfiguration() {
        return this.configuration;
    }

    public static Main getInstance() {
        return main;
    }
}
