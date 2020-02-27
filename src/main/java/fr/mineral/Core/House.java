package fr.mineral.Core;

import fr.mineral.Core.Arena.Coffre;
import fr.mineral.Teams.Equipe;
import fr.mineral.Translation.Lang;
import fr.mineral.Utils.Door.AutomaticDoors;
import fr.mineral.Utils.SaveableBlock;
import fr.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.material.MaterialData;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

public class House {
    private Equipe team;
    private AutomaticDoors doors;


    private LinkedList<SaveableBlock> blocks;
    private Coffre coffre;
    private Location spawnLocation;
    private String teamName;
    private ChatColor color;


    public House(String nomEquipe, ChatColor couleur) {
        this.teamName = nomEquipe;
        this.color = couleur;
        this.team = new Equipe(this.teamName, this.color);
        this.doors = new AutomaticDoors(team);
        this.blocks = new LinkedList<>();
    }

    /*
            Used to save house blocks
     */

    public Equipe getTeam() {
        return this.team;
    }

    public void addBlock(Location location) throws Exception{
        MaterialData materialData;
        Block block = location.getBlock();

        materialData = block.getState().getData();
        this.blocks.add(new SaveableBlock(block));
        mineralcontest.log.info(mineralcontest.prefix + ChatColor.GOLD + "Block " + block.getType().toString() + " successfully added");
    }


    public void removeBlock(Location location) throws Exception {
        MaterialData materialData;
        Block block = location.getBlock();

        if(block.getType().equals(Material.AIR)) {
            throw new Exception("Impossible d'ajouter de l'air comme block");
        }
        materialData = block.getState().getData();
        /* For each "value<block, materialdata> in saved blocks*/
        for(SaveableBlock saveableBlock : blocks)
            if(saveableBlock.getLocation().equals(location)) {
                blocks.remove(saveableBlock);
                return;
        }
    }


    public LinkedList<SaveableBlock> getBlocks() {
        return blocks;
    }

    /*
    -------------------------------------------
     */


    public void setCoffreEquipe(Location loc) {
        this.coffre = new Coffre();
        this.coffre.setPosition(loc);
        mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.prefixGlobal + Lang.translate(Lang.team_chest_added.toString(), team));

    }

    public Location getCoffreEquipeLocation() throws Exception {
        if(this.coffre.getPosition() == null)
            throw new Exception(Lang.translate(Lang.chest_not_defined.toString(), team));
        return coffre.getPosition();
    }

    public void spawnCoffreEquipe() throws Exception {
        Location loc = coffre.getPosition();
        Block block = loc.getBlock();
        loc.getBlock().setType(Material.CHEST);
    }

    public void setHouseLocation(Location houseLocation){
        Bukkit.getServer().broadcastMessage(mineralcontest.prefixGlobal + Lang.translate(Lang.team_house_location_added.toString(), team));
        this.spawnLocation = houseLocation;
    }

    public Location getHouseLocation() {
        if(this.spawnLocation == null)
            Bukkit.broadcastMessage(mineralcontest.prefixGlobal + Lang.translate(Lang.team_house_location_not_added.toString(), team));
        return spawnLocation;
    }




    public AutomaticDoors getPorte() {
        return doors;
    }
}