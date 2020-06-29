package fr.synchroneyes.mineral.Events;

import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.Settings.GameSettings;
import fr.synchroneyes.mineral.Statistics.Class.KillStat;
import fr.synchroneyes.mineral.Teams.Equipe;
import fr.synchroneyes.mineral.Translation.Lang;
import fr.synchroneyes.mineral.Utils.Player.PlayerUtils;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;



public class EntityDamage implements Listener {

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        World worldEvent = event.getEntity().getWorld();
        if (mineralcontest.isAMineralContestWorld(worldEvent)) {
            Game partie = mineralcontest.getWorldGame(worldEvent);

            if (event.getEntity() instanceof Player && mineralcontest.isInMineralContestHub((Player) event.getEntity())) {
                event.setCancelled(true);
                return;
            }


            if (partie == null) {
                event.setCancelled(true);
                return;
            }

            if (!partie.isGameStarted() || partie.isGamePaused()) {
                event.setCancelled(true);
                return;
            }

            if (event.getEntity() instanceof Player && partie.isGameStarted()) {
                Player victime = (Player) event.getEntity();


                if (victime.getHealth() - event.getFinalDamage() < 0) {
                    PlayerUtils.setMaxHealth(victime);
                    event.setCancelled(true);
                    PlayerUtils.killPlayer(victime);


                    if (event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)) {
                        EntityDamageByEntityEvent event1 = (EntityDamageByEntityEvent) event;
                        if (event1.getDamager() instanceof Player) {
                            registerKill(victime, (Player) event1.getDamager());
                        }
                    } else if (event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE) {
                        EntityDamageByEntityEvent event2 = (EntityDamageByEntityEvent) event;
                        if (event2.getDamager() instanceof Arrow) {
                            Arrow fleche = (Arrow) event2.getDamager();
                            if (fleche.getShooter() instanceof Player) {
                                Player attaquant_f = (Player) fleche.getShooter();
                                registerKill(victime, attaquant_f);
                            }
                        }
                    } else {
                        mineralcontest.broadcastMessage(mineralcontest.prefixGlobal + Lang.translate(Lang.player_died.toString(), victime), partie.groupe);
                    }
                }
            }
        }

    }


    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) throws Exception {

        World worldEvent = event.getEntity().getWorld();
        if (mineralcontest.isAMineralContestWorld(worldEvent)) {
            Game partie = mineralcontest.getWorldGame(worldEvent);
            if (partie != null && (!partie.isGameStarted() || partie.isGamePaused())) {
                event.setCancelled(true);
                return;
            }
            if (event.getEntity() instanceof Player && partie != null && partie.isGameStarted()) {
                Player victime = (Player) event.getEntity();
                Player attaquant = null;
                // We will check If they are both on the same team
                if (event.getDamager() instanceof Player) {
                    attaquant = (Player) event.getDamager();
                } else if (event.getDamager() instanceof Arrow) {
                    Arrow arrow = (Arrow) event.getDamager();
                    if (arrow.getShooter() instanceof Player) {
                        attaquant = (Player) arrow.getShooter();
                    }
                }

                GameSettings settings = partie.groupe.getParametresPartie();


                // Do the check
                if (attaquant != null) {
                    Equipe attaquantTeam = partie.getPlayerTeam(attaquant);
                    Equipe victimeTeam = partie.getPlayerTeam(victime);


                    int enable_friendly_fire = settings.getCVAR("mp_enable_friendly_fire").getValeurNumerique();
                    if ((attaquantTeam != null || victimeTeam != null) && attaquantTeam.equals(victimeTeam) && enable_friendly_fire == 0) {
                        event.setCancelled(true);
                        return;
                    }
                }

                if (event.getDamager() instanceof Player && partie.isReferee((Player) event.getDamager())) {
                    event.setCancelled(true);
                    return;
                }

                if (partie.isReferee(victime)) {
                    event.setCancelled(true);
                    return;
                }

                if (partie.getArene().getDeathZone().isPlayerDead(victime)) {
                    event.setCancelled(true);
                    return;
                }

                if (partie.getArene().getCoffre().openingPlayer != null && partie.getArene().getCoffre().openingPlayer.equals(victime))
                    partie.getArene().getCoffre().close();

                // Si une entité meurt d'un coup/explosion/...
                if (victime.getHealth() - event.getFinalDamage() < 0) {
                    victime.setHealth(20D);
                    event.setCancelled(true);

                    if (event.getDamager() instanceof Arrow) {
                        Arrow fleche = (Arrow) event.getDamager();
                        if (fleche.getShooter() instanceof Player) {
                            Player attaquant_f = (Player) fleche.getShooter();
                            Bukkit.broadcastMessage(attaquant_f.getDisplayName() + " a attacké " + victime.getDisplayName());
                            registerKill(victime, attaquant_f);
                        }
                    }

                    // Si c'est un joueur qui a tué notre victime
                    if (event.getDamager() instanceof Player) {
                        registerKill(victime, (Player) event.getDamager());
                    }


                    PlayerUtils.killPlayer(victime);

                }

            }
        }
    }

    private void registerKill(Player dead, Player attacker) {
        if (mineralcontest.getPlayerGame(dead) == null) return;
        mineralcontest.broadcastMessage(mineralcontest.prefixGlobal + Lang.translate(Lang.player_killed.toString(), dead, attacker), mineralcontest.getPlayerGame(dead).groupe);

        Game partie = mineralcontest.getPlayerGame(attacker);
        if (partie != null && partie.isGameStarted()) {
            partie.getStatsManager().register(KillStat.class, attacker, dead);
        }

        mineralcontest.getPlayerGame(dead).killCounter++;
    }
}