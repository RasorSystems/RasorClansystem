package de.rasorsystems.main.clan;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class ClanAPI {

        public static void addClan(UUID p, String clan, String prefix) {
            try {
                PreparedStatement pr = MySQL.getStatement("INSERT INTO clans (owner, clanname, prefix, coins) VALUES (?, ?, ?, ?)");
                pr.setString(1, p.toString());
                pr.setString(2, clan);
                pr.setString(3, prefix);
                pr.setLong(4, 0L);
                pr.executeUpdate();
                pr.close();
            } catch (SQLException var4) {
                var4.printStackTrace();
            }

            addmembertoclan(p, clan, Rankes.OWNER);
        }

        public static void removeClan(String clan) throws SQLException {
            try {
                PreparedStatement pr = MySQL.getStatement("DELETE FROM clans WHERE clanname = ?");
                pr.setString(1, clan);
                pr.executeUpdate();
                pr.close();
            } catch (Exception var3) {
            }

            Iterator var4 = getClanMembers(clan).iterator();

            while(var4.hasNext()) {
                String member = (String)var4.next();
                removeclanmemberfromclan(member);
            }

        }

        public static void changeClan(String clanname, String name, String prefix, UUID ownerUUID) {
            try {
                PreparedStatement ps = MySQL.getStatement("UPDATE clans SET clanname= ? AND owner= ? AND prefix = ? WHERE UUID= ?");
                ps.setString(1, clanname);
                ps.setString(2, ownerUUID.toString());
                ps.setString(3, prefix);
                ps.executeUpdate();
                ps.close();
            } catch (Exception var5) {
                var5.printStackTrace();
            }

        }

        public static String getClanprefix(String clanname) {
            try {
                PreparedStatement ps = MySQL.getStatement("SELECT * FROM clans WHERE clanname= ?");
                ps.setString(1, clanname);
                ResultSet rs = ps.executeQuery();
                rs.next();
                String name = rs.getString("prefix");
                rs.close();
                ps.close();
                return name;
            } catch (Exception var4) {
                var4.printStackTrace();
                return "Clanlos";
            }
        }

        public static String getClanOwnerUUID(String clanname) {
            try {
                PreparedStatement ps = MySQL.getStatement("SELECT * FROM clans WHERE clanname= ?");
                ps.setString(1, clanname);
                ResultSet rs = ps.executeQuery();
                rs.next();
                String points = rs.getString("owner");
                rs.close();
                ps.close();
                return points;
            } catch (Exception var4) {
                var4.printStackTrace();
                return null;
            }
        }

        public static long getClannameFromOwner(UUID owner) {
            try {
                PreparedStatement ps = MySQL.getStatement("SELECT * FROM clans WHERE owner= ?");
                ps.setString(1, owner.toString());
                ResultSet rs = ps.executeQuery();
                rs.next();
                long points = rs.getLong("clanname");
                rs.close();
                ps.close();
                return points;
            } catch (Exception var5) {
                var5.printStackTrace();
                return -1L;
            }
        }

        public static String getClannameFromUser(UUID user) {
            try {
                PreparedStatement ps = MySQL.getStatement("SELECT * FROM clanmembers WHERE UUID= ?");
                ps.setString(1, user.toString());
                ResultSet rs = ps.executeQuery();
                rs.next();
                String points = rs.getString("clanname");
                rs.close();
                ps.close();
                return points;
            } catch (Exception var4) {
                var4.printStackTrace();
                return null;
            }
        }

        public static boolean isClanExists(String clanname) {
            try {
                PreparedStatement ps = MySQL.getStatement("SELECT * FROM clans WHERE clanname= ?");
                ps.setString(1, clanname);
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

        public static boolean isInClan(UUID p) {
            try {
                PreparedStatement ps = MySQL.getStatement("SELECT * FROM clanmembers WHERE UUID= ?");
                ps.setString(1, p.toString());
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

        public static void addmembertoclan(UUID p, String clanname, Rankes rang) {
            try {
                PreparedStatement pr = MySQL.getStatement("INSERT INTO clanmembers (UUID, clanname, rang) VALUES (?, ?, ?)");
                pr.setString(1, p.toString());
                pr.setString(2, clanname);
                if (rang.equals(Rankes.OWNER)) {
                    pr.setString(3, "owner");
                } else if (rang.equals(Rankes.MOD)) {
                    pr.setString(3, "mod");
                } else if (rang.equals(Rankes.USER)) {
                    pr.setString(3, "user");
                }

                pr.executeUpdate();
                pr.close();
            } catch (SQLException var4) {
                var4.printStackTrace();
            }

        }

        public static double getcoins(String clanname) {
            try {
                PreparedStatement ps = MySQL.getStatement("SELECT * FROM clans WHERE clanname= ?");
                ps.setString(1, clanname);
                ResultSet rs = ps.executeQuery();
                rs.next();
                double points = rs.getDouble("coins");
                rs.close();
                ps.close();
                return points;
            } catch (Exception var5) {
                var5.printStackTrace();
                return -1.0D;
            }
        }


        public static void addclaninvite(UUID p, String clanname, UUID t) {
            try {
                PreparedStatement pr = MySQL.getStatement("INSERT INTO claninvites (UUIDsender, UUIDsend, clan) VALUES (?, ?, ?)");
                pr.setString(1, p.toString());
                pr.setString(2, t.toString());
                pr.setString(3, clanname);
                pr.executeUpdate();
                pr.close();
            } catch (SQLException var4) {
                var4.printStackTrace();
            }

        }

        public static void removeclaninviteFromempfänger(UUID uuid) {
            try {
                PreparedStatement pr = MySQL.getStatement("DELETE FROM claninvites WHERE UUIDsend = ?");
                pr.setString(1, uuid.toString());
                pr.executeUpdate();
                pr.close();
            } catch (Exception var2) {
            }

        }

        public static List<String> getClanInvitesbyclan(UUID send) throws SQLException {
            List<String> list = new ArrayList();
            PreparedStatement ps = MySQL.getStatement("SELECT * FROM claninvites WHERE UUIDsend= ?");
            ps.setString(1, send.toString());
            ResultSet rs = ps.executeQuery();

            try {
                while(rs.next()) {
                    list.add(rs.getString("clan"));
                }
            } catch (SQLException var5) {
                var5.printStackTrace();
            }

            return list;
        }

        public static void removeclanmemberfromclan(String uuid) {
            try {
                PreparedStatement pr = MySQL.getStatement("DELETE FROM clanmembers WHERE UUID = ?");
                pr.setString(1, uuid);
                pr.executeUpdate();
                pr.close();
            } catch (Exception var2) {
            }

        }

        public static List<String> getClanMembers(String clanname) throws SQLException {
            List<String> list = new ArrayList();
            PreparedStatement ps = MySQL.getStatement("SELECT * FROM clanmembers WHERE clanname= ?");
            ps.setString(1, clanname);
            ResultSet rs = ps.executeQuery();

            try {
                while(rs.next()) {
                    list.add(rs.getString("UUID"));
                }
            } catch (SQLException var5) {
                var5.printStackTrace();
            }

            return list;
        }

        public static void setClanRang(String clanname, UUID user, Rankes enums) {
            try {
                PreparedStatement ps = MySQL.getStatement("UPDATE clanmembers SET rang= ? WHERE UUID= ?");
                if (enums.equals(Rankes.COOWNER)) {
                    ps.setString(1, "COowner");
                } else if (enums.equals(Rankes.MOD)) {
                    ps.setString(1, "mod");
                } else if (enums.equals(Rankes.USER)) {
                    ps.setString(1, "user");
                } else if (enums.equals(Rankes.OWNER)) {
                    ps.setString(1, "owner");
                }

                ps.setString(2, user.toString());
                ps.executeUpdate();
                ps.close();
            } catch (Exception var4) {
                var4.printStackTrace();
            }

        }

        public static String getClannrang(String user) {
            try {
                PreparedStatement ps = MySQL.getStatement("SELECT * FROM clanmembers WHERE UUID= ?");
                ps.setString(1, user.toString());
                ResultSet rs = ps.executeQuery();
                rs.next();
                String points = rs.getString("rang");
                rs.close();
                ps.close();
                return points;
            } catch (Exception var4) {
                var4.printStackTrace();
                return null;
            }
        }

        public static boolean isInvited(String uuid, String clan) {
            try {
                PreparedStatement ps = MySQL.getStatement("SELECT * FROM claninvites WHERE UUIDsend= ? AND clan = ? ");
                ps.setString(1, uuid);
                ps.setString(2, clan);
                ResultSet rs = ps.executeQuery();
                boolean Playerdata = rs.next();
                rs.close();
                rs.close();
                return Playerdata;
            } catch (Exception var5) {
                var5.printStackTrace();
                return false;
            }
        }
    }

