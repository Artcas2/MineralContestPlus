package fr.mineral.Teams;

import fr.mineral.Exception.FullTeamException;
import fr.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.LinkedList;

public class Equipe {
    private LinkedList<Player> joueurs;
    private String nomEquipe;
    private ChatColor couleur;
    private Location houseLocation;



    public Equipe(String nom, ChatColor c) {
        this.joueurs = new LinkedList<Player>();
        this.nomEquipe = nom;
        this.couleur = c;
    }


    // Retourne true si la team est pleine, false si non
    public boolean isTeamFull() {
        if(this.joueurs.size() >= mineralcontest.teamMaxPlayers)
            return true;
        return false;
    }

    public boolean addPlayerToTeam(Player p) throws Exception {
        if(!isTeamFull()) {
            this.joueurs.add(p);

            p.sendMessage(mineralcontest.prefix + "Bienvenue dans l'équipe" + this.couleur + " " + this.nomEquipe.toLowerCase());

            mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.prefixGlobal + "Le joueur " + ChatColor.GOLD + p.getDisplayName() + ChatColor.WHITE + " a rejoint l'équipe " + this.couleur + " " + this.nomEquipe.toLowerCase());
            return true;
        }
        throw new Exception("L'equipe est pleine.");
    }

    public boolean removePlayer(Player p) {
        if(isPlayerInTeam(p)) {
            this.joueurs.remove(p);
            p.sendMessage(mineralcontest.prefix + "Vous avez été retirer de l'équipe" + this.couleur + " " + this.nomEquipe.toLowerCase());
            return true;
        }
        return false;
    }

    public boolean isPlayerInTeam(Player p) {
        return this.joueurs.contains(p);
    }

    public LinkedList<Player> getJoueurs() {
        return this.joueurs;
    }

    public String toString() {
        String joueurs = "Equipe " + this.getCouleur() + this.nomEquipe + ChatColor.WHITE + ": ";
        for(int i = 0; i < this.joueurs.size(); i++) {
            joueurs += this.joueurs.get(i).getDisplayName() + " ";
        }
        return joueurs;
    }

    public String getNomEquipe() {
        return this.nomEquipe;
    }


    public ChatColor getCouleur() {
        return this.couleur;
    }

    public void setHouseLocation(Location houseLocation){
        Bukkit.getServer().broadcastMessage(mineralcontest.prefixGlobal + "Le spawn de l'équipe " +this.getCouleur() + this.getNomEquipe() + ChatColor.WHITE + "  pour réapparaitre a bien été ajouter");
        this.houseLocation = houseLocation;
    }

    public Location getHouseLocation() throws Exception {
        if(this.houseLocation == null)
            throw new Exception("Le spawn de l'équipe" +this.getCouleur() + this.getNomEquipe() + ChatColor.WHITE + " pour la réapparition n'a pas encore ete ajouter");
        return houseLocation;
    }




}
