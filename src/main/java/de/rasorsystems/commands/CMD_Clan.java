package de.rasorsystems.commands;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.UUID;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;
import de.rasorsystems.main.Main;
import de.rasorsystems.main.clan.ClanAPI;
import de.rasorsystems.main.clan.MySQL;
import de.rasorsystems.main.clan.PlayerFetcher;
import de.rasorsystems.main.clan.Rankes;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class CMD_Clan extends Command {
    public CMD_Clan(String name) {
        super(name);
    }

    public void execute(CommandSender sender, String[] args) {
        ProxiedPlayer p = (ProxiedPlayer)sender;
        if (args.length == 0) {
            p.sendMessage( "/clan create <name> <prefix>");
            p.sendMessage( "/clan delete");
            p.sendMessage( "/clan info");
            p.sendMessage( "/clan promote <spieler>");
            p.sendMessage( "/clan info");
        } else if (args.length == 1) {
            Iterator var5;
            String s;
            if (args[0].equalsIgnoreCase("delete")) {
                if (ClanAPI.isInClan(p.getUniqueId())) {
                    if (ClanAPI.getClannrang(p.getUniqueId().toString()).equalsIgnoreCase("owner")) {
                        try {
                            var5 = ClanAPI.getClanMembers(ClanAPI.getClannameFromUser(p.getUniqueId())).iterator();

                            while(var5.hasNext()) {
                                s = (String)var5.next();
                                Main.getInstance().getProxy().getPlayer(s).sendMessage("§cDein Clan §e" + ClanAPI.getClannameFromUser(p.getUniqueId()) + " §cwurde aufgelöst!");
                            }

                            ClanAPI.removeClan(ClanAPI.getClannameFromUser(p.getUniqueId()));
                            p.sendMessage( "§aDein Clan wurde aufgelöst!");
                        } catch (SQLException var8) {
                            var8.printStackTrace();
                        }
                    } else {
                        p.sendMessage( "§cDu bist nicht der besitzer des Clanes!");
                    }
                } else {
                    p.sendMessage( "§cDu bist in keinem Clan!");
                }
            } else if (args[0].equalsIgnoreCase("info")) {
                if (ClanAPI.isInClan(p.getUniqueId())) {
                    p.sendMessage( "§8§m)-----(--((§8 [§4§l" + ClanAPI.getClannameFromUser(p.getUniqueId()) + "§8] §8§m))--)-----(\n");
                    try {
                        p.sendMessage( "§4Owner§7 des Clanes: " + PlayerFetcher.getName(UUID.fromString(ClanAPI.getClanOwnerUUID(ClanAPI.getClannameFromUser(p.getUniqueId())))));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    p.sendMessage( "\n§eAlle Member:\n");

                    try {
                        var5 = ClanAPI.getClanMembers(ClanAPI.getClannameFromUser(p.getUniqueId())).iterator();

                        while (var5.hasNext()) {
                            s = (String) var5.next();
                            p.sendMessage("§e- Name: " + PlayerFetcher.getName(UUID.fromString(s)) + " §7- §b" + ClanAPI.getClannrang(s));
                        }
                    }catch (ParseException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    p.sendMessage( "\n§8§m)-----(--((§8 [§4§l" + ClanAPI.getClannameFromUser(p.getUniqueId()) + "§8] §8§m))--)-----(");
                } else {
                    p.sendMessage( "§cDu bist in keinem Clan!");
                }
            }
        } else if (args.length == 2) {
            ProxiedPlayer t = Main.getInstance().getProxy().getPlayer(args[1]);
            if (args[0].equalsIgnoreCase("promote")) {
                if (t.isConnected()) {
                    if (t != p) {
                        if (t != null) {
                            if (ClanAPI.isInClan(p.getUniqueId())) {
                                if (ClanAPI.isInClan(t.getUniqueId())) {
                                    if (!ClanAPI.getClannrang(p.getUniqueId().toString()).equalsIgnoreCase("owner") && !ClanAPI.getClannrang(p.getUniqueId().toString()).equalsIgnoreCase("coowner") && !ClanAPI.getClannrang(p.getUniqueId().toString()).equalsIgnoreCase("mod")) {
                                        p.sendMessage( "§cKeine rechte");
                                    } else if (ClanAPI.getClannameFromUser(t.getUniqueId()).equals(ClanAPI.getClannameFromUser(p.getUniqueId()))) {
                                        if (ClanAPI.getClannrang(t.getUniqueId().toString()).equalsIgnoreCase("user")) {
                                            ClanAPI.setClanRang(ClanAPI.getClannameFromUser(p.getUniqueId()), t.getUniqueId(), Rankes.MOD);
                                            p.sendMessage( "§aDu hast den Spieler §b" + t.getName() + " §azum Clan §emoderator §abefördert!");
                                            t.sendMessage( "§aDu wurdest von §b" + p.getName() + " §azum Clan §emoderator §abefördert!");
                                        } else if (ClanAPI.getClannrang(t.getUniqueId().toString()).equalsIgnoreCase("mod")) {
                                            if (!ClanAPI.getClannrang(p.getUniqueId().toString()).equalsIgnoreCase("coowner") && !ClanAPI.getClannrang(p.getUniqueId().toString()).equalsIgnoreCase("owner")) {
                                                p.sendMessage( "§cKeine rechte");
                                            } else {
                                                ClanAPI.setClanRang(ClanAPI.getClannameFromUser(p.getUniqueId()), t.getUniqueId(), Rankes.COOWNER);
                                                p.sendMessage( "§aDu hast den Spieler §b" + t.getName() + " §azum Clan §eco-owner §abefördert!");
                                                t.sendMessage( "§aDu wurdest von §b" + p.getName() + " §azum Clan §eco-owner §abefördert!");
                                            }
                                        }
                                    } else {
                                        p.sendMessage( "§cDieser Spieler ist nicht in deinem Clan!");
                                    }
                                } else {
                                    p.sendMessage( "§cDiese Person ist nicht in deinem Clan!");
                                }
                            } else {
                                p.sendMessage( "§cDu bist in keinem Clan!");
                            }
                        } else {
                            p.sendMessage( "§cDieser Spieler ist nicht Online!");
                        }
                    } else {
                        p.sendMessage( "§cDu kannst nicht, dichselbst promoten!");
                    }
                } else {
                    p.sendMessage( "§cDieser Spieler ist nicht Online!");
                }
            } else if (args[0].equalsIgnoreCase("invite")) {
                if (t != p) {
                    if (t != null) {
                        if (t.isConnected()) {
                            if (ClanAPI.isInClan(p.getUniqueId())) {
                                if (!ClanAPI.isInClan(t.getUniqueId())) {
                                    if (!ClanAPI.getClannrang(p.getUniqueId().toString()).equalsIgnoreCase("owner") && !ClanAPI.getClannrang(p.getUniqueId().toString()).equalsIgnoreCase("coowner") && !ClanAPI.getClannrang(p.getUniqueId().toString()).equalsIgnoreCase("mod")) {
                                        p.sendMessage( "§ckeine rechte");
                                    } else if (!ClanAPI.isInvited(t.getUniqueId().toString(), ClanAPI.getClannameFromUser(p.getUniqueId()))) {
                                        ClanAPI.addclaninvite(p.getUniqueId(), ClanAPI.getClannameFromUser(p.getUniqueId()), t.getUniqueId());
                                        p.sendMessage( "§aDu hast den Spieler §b" + t.getName() + " §azu deinem Clan eingeladen");
                                        t.sendMessage( "§bDu wurdest vom " + ClanAPI.getClannameFromUser(p.getUniqueId()) + " §aeingeladen zu Joinen. \n§bTippe /clan join " + ClanAPI.getClannameFromUser(p.getUniqueId()) + " ein um zu joinen.");
                                    } else {
                                        p.sendMessage( "§cdu hast die person bereits invitet");
                                    }
                                } else {
                                    p.sendMessage( "§cDiese person ist bereits in einem clan!");
                                }
                            } else {
                                p.sendMessage( "§cDu bist in keinem Clan!");
                            }
                        } else {
                            p.sendMessage( "§cSpieler nicht online");
                        }
                    } else {
                        p.sendMessage( "§cDieser Spieler ist nicht Online!");
                    }
                }
            } else if (args[0].equalsIgnoreCase("join")) {
                if (!ClanAPI.isInClan(p.getUniqueId())) {
                    if (ClanAPI.getClannameFromUser(p.getUniqueId()) != args[1]) {
                        if (ClanAPI.isInvited(p.getUniqueId().toString(), args[1])) {
                            ClanAPI.addmembertoclan(p.getUniqueId(), args[1], Rankes.USER);
                            p.sendMessage( "§aDu bist dem Clan beigetreten!");
                            ClanAPI.removeclaninviteFromempfänger(t.getUniqueId());
                        } else {
                            p.sendMessage( "§cDu wurdest nicht von diesem Clan eingeladen!");
                        }
                    } else {
                        p.sendMessage( "§cDu bist bereits in diesem Clan!");
                    }
                } else {
                    p.sendMessage( "§cDu bist in keinem Clan!");
                }
            } else if (args[0].equalsIgnoreCase("kick")) {
                if (t != p) {
                    if (t != null) {
                        if (ClanAPI.isInClan(p.getUniqueId())) {
                            if (ClanAPI.isInClan(t.getUniqueId())) {
                                if (!ClanAPI.getClannrang(p.getUniqueId().toString()).equalsIgnoreCase("owner") && !ClanAPI.getClannrang(p.getUniqueId().toString()).equalsIgnoreCase("coowner") && !ClanAPI.getClannrang(p.getUniqueId().toString()).equalsIgnoreCase("mod")) {
                                    p.sendMessage( "§cKeine rechte");
                                } else if (ClanAPI.getClannameFromUser(t.getUniqueId()).equals(ClanAPI.getClannameFromUser(p.getUniqueId()))) {
                                    if (!ClanAPI.getClannrang(t.getUniqueId().toString()).equalsIgnoreCase("coowner")) {
                                        ClanAPI.removeclanmemberfromclan(t.getUniqueId().toString());
                                        p.sendMessage( "§aDu hast den Spieler §b" + t.getName() + " §aaus dem Clan geworfen.");
                                        t.sendMessage( "§aDu wurdest von §b" + p.getName() + " §aaus dem Clan geworfen.");
                                    } else if (ClanAPI.getClannrang(p.getUniqueId().toString()).equalsIgnoreCase("owner")) {
                                        ClanAPI.removeclanmemberfromclan(t.getUniqueId().toString());
                                        ClanAPI.removeclanmemberfromclan(t.getUniqueId().toString());
                                        p.sendMessage( "§aDu hast den Spieler §b" + t.getName() + " §aaus dem Clan geworfen.");
                                        t.sendMessage( "§aDu wurdest von §b" + p.getName() + " §aaus dem Clan geworfen.");
                                    } else {
                                        p.sendMessage( "§cDu darfst den CO-Owner nicht kicken!");
                                    }
                                } else {
                                    p.sendMessage( "§cDer Spieler ist nicht in deinem Clan!");
                                }
                            } else {
                                p.sendMessage( "§cDer Spieler ist nicht in deinem Clan!");
                            }
                        } else {
                            p.sendMessage( "§cDu bist in keinem Clan!");
                        }
                    } else {
                        p.sendMessage( "§cDieser Spieler ist nicht Online!");
                    }
                } else {
                    p.sendMessage( "§cDu kannst dich nicht selbst kicken");
                }
            }
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("create")) {
                if (!ClanAPI.isClanExists(args[1])) {
                    if (args[1].length() <= 8) {
                        if (args[2].length() <= 4) {
                            if (!ClanAPI.isInClan(p.getUniqueId())) {
                                ClanAPI.addClan(p.getUniqueId(), args[1], args[2]);
                                p.sendMessage("§aDu hast den Clan §e" + args[1] + " §aerfolgreich erstellt.");
                            } else {
                                p.sendMessage("§cDu bist bereits in einem Clan!");
                            }
                        } else {
                            p.sendMessage("§cDer Clanprefix darf nicht länger als 4 Zeichen sein!");
                        }
                    } else {
                        p.sendMessage("§cDer Clanname darf nicht länger als 8 Zeichen sein!");
                    }
                } else {
                    p.sendMessage("§cEs exestiert bereits ein Clan mit dem Namen!");
                }
            }
        }
    }
}
