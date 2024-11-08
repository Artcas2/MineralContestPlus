package fr.synchroneyes.mineral.Core;

import fr.synchroneyes.groups.Core.Groupe;
import fr.synchroneyes.mineral.Core.Arena.Coffre;
import fr.synchroneyes.mineral.Teams.Equipe;
import fr.synchroneyes.mineral.Translation.Lang;
import fr.synchroneyes.mineral.Utils.Door.AutomaticDoors;
import fr.synchroneyes.mineral.mineralcontest;
import java.util.LinkedHashMap;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.material.MaterialData;

public class House {
    private Equipe team;
    private AutomaticDoors doors;
    private LinkedHashMap<Block, MaterialData> blocks;
    private Coffre coffre;
    private Location spawnLocation;
    private String teamName;
    private ChatColor color;
    private Groupe groupe;

    public House(String nomEquipe, ChatColor couleur, Groupe g) {
        this.teamName = nomEquipe;
        this.color = couleur;
        this.team = new Equipe(this.teamName, this.color, g, this);
        this.doors = new AutomaticDoors(this.team, g);
        this.blocks = new LinkedHashMap();
        this.groupe = g;
    }

    public Coffre getCoffre() {
        return this.coffre;
    }

    public Equipe getTeam() {
        return this.team;
    }

    public void setCoffreEquipe(Location loc) {
        this.coffre = new Coffre();
        this.coffre.setPosition(loc);
        if (mineralcontest.debug) {
            mineralcontest.broadcastMessage(mineralcontest.prefixGlobal + Lang.translate(Lang.team_chest_added.toString(), this.team), this.groupe);
        }
    }

    public Location getCoffreEquipeLocation() throws Exception {
        if (this.coffre.getPosition() == null) {
            throw new Exception(Lang.translate(Lang.chest_not_defined.toString(), this.team));
        }
        return this.coffre.getPosition();
    }

    public void spawnCoffreEquipe() throws Exception {
        Location loc = this.coffre.getPosition();
        this.coffre.clear();
        loc.getBlock().setType(Material.CHEST);
        this.groupe.getGame().addAChest(loc.getBlock());
    }

    public void setHouseLocation(Location houseLocation) {
        if (mineralcontest.debug) {
            mineralcontest.broadcastMessage(mineralcontest.prefixGlobal + Lang.translate(Lang.team_house_location_added.toString(), this.team), this.groupe);
        }
        this.spawnLocation = houseLocation;
    }

    public Location getHouseLocation() {
        return this.spawnLocation;
    }

    public AutomaticDoors getPorte() {
        return this.doors;
    }

    public Groupe getGroupe() {
        return this.groupe;
    }
}

