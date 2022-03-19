package de.rasorsystems.main.clan;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.rasorsystems.main.Main;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.*;
import java.util.UUID;

public class MySQL {
        public static String username = Main.getInstance().getConfiguration().getString("user");
        public static String password = Main.getInstance().getConfiguration().getString("passwort");
        public static String database = Main.getInstance().getConfiguration().getString("database");
        public static String host = Main.getInstance().getConfiguration().getString("host");
        public static String port = Main.getInstance().getConfiguration().getString("port");
        public static Connection con;

        public MySQL() {
        }

        public static void connect() {
            if (!isConnected()) {
                try {
                    con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?useJDBCCompliantTimezoneShift=true&&serverTimezone=UTC&&useUnicode=true&autoReconnect=true", username, password);
                    Main.getInstance().getProxy().getConsole().sendMessage("§aVerbindung zur Datenbank aufgebaut!");
                    update("CREATE TABLE IF NOT EXISTS clans(clanname  VARCHAR(100), owner VARCHAR(100), prefix VARCHAR(100), coins VARCHAR(100))");
                    update("CREATE TABLE IF NOT EXISTS clanmembers(UUID VARCHAR(100), clanname  VARCHAR(100), rang VARCHAR(100))");
                    update("CREATE TABLE IF NOT EXISTS claninvites(UUIDsender VARCHAR(100), UUIDsend  VARCHAR(100), clan VARCHAR(100))");
                } catch (SQLException var1) {
                    Main.getInstance().getProxy().getConsole().sendMessage("§cVerbindung zur Datenbank konnte nicht aufgebaut werden, ueberpruefe bitte die Zugangsdaten ob sie korrekt eingegeben wurden und ob die Datenbank hochgefahren ist!");
                }
            }

        }

        public static void close() {
            if (isConnected()) {
                try {
                    con.close();
                    Main.getInstance().getProxy().getConsole().sendMessage("§cMySQL Verbindung geschlossen!");
                } catch (SQLException var1) {
                    var1.printStackTrace();
                }
            }

        }

        public static boolean isConnected() {
            return con != null;
        }

        public static void update(String qry) {
            if (isConnected()) {
                try {
                    con.createStatement().executeUpdate(qry);
                } catch (SQLException var2) {
                    var2.printStackTrace();
                }
            } else if (!isConnected()) {
                connect();
            }

        }

        public static ResultSet getResult(String qry) {
            if (isConnected()) {
                try {
                    return con.createStatement().executeQuery(qry);
                } catch (SQLException var2) {
                    var2.printStackTrace();
                    return null;
                }
            } else {
                if (!isConnected()) {
                    connect();
                }

                return null;
            }
        }

        public static PreparedStatement getStatement(String sql) {
            if (con != null) {
                try {
                    return con.prepareStatement(sql);
                } catch (SQLException var2) {
                    var2.printStackTrace();
                }
            }

            return null;
        }

        public static boolean isRegistered(ProxiedPlayer p) {
            try {
                PreparedStatement ps = getStatement("SELECT * FROM Playerdata WHERE UUID= ?");
                ps.setString(1, p.getUniqueId().toString());
                ResultSet rs = ps.executeQuery();
                boolean Playerdata = rs.next();
                rs.close();
                rs.close();
                return Playerdata;
            } catch (Exception var4) {
                var4.printStackTrace();
                return false;
            }
        }

    public static String loadUUID(final String playerName) {
        try {
            final URLConnection urlConnection = new URL("https://api.mojang.com/users/profiles/minecraft/" + playerName).openConnection();
            urlConnection.connect();
            final JsonParser jsonParser = new JsonParser();
            final JsonElement jsonElement = jsonParser.parse((Reader)new InputStreamReader((InputStream)urlConnection.getContent()));
            final JsonObject jsonObject = jsonElement.getAsJsonObject();
            System.out.println(getUniqueIdFromString(jsonObject.get("id").getAsString()));
            return jsonObject.get("id").getAsString().replaceFirst("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5");
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (IOException e2) {
            e2.printStackTrace();
        }
        catch (Exception ex) {}
        return null;
    }

    private static UUID getUniqueIdFromString(final String uuid) {
        try {
            return UUID.fromString(uuid.replaceFirst("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5"));
        }
        catch (Exception e) {
            return null;
        }
    }


    }
