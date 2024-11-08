package fr.synchroneyes.world_downloader.Items;

import fr.synchroneyes.world_downloader.Items.ItemInterface;
import fr.synchroneyes.world_downloader.MapInfo;
import fr.synchroneyes.world_downloader.WorldDownloader;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class MapDownloadItem extends ItemInterface {
    private String mapUrl = "";
    private String shortDescription = "";
    private String mapName = "";
    private String mapSizeDisplay = "";
    private String mapSize = "";
    private String mapFileName = "";

    public MapDownloadItem(String mapName, String mapUrl, String shortDescription, String mapSize, String mapSizeDisplay, String mapFileName) {
        this.mapName = mapName;
        this.mapUrl = mapUrl;
        this.shortDescription = shortDescription;
        this.mapSize = mapSize;
        this.mapSizeDisplay = mapSizeDisplay;
        this.mapFileName = mapFileName;
    }

    @Override
    public Material getItemMaterial() {
        return Material.PAPER;
    }

    @Override
    public String getNomInventaire() {
        return this.mapName;
    }

    @Override
    public String getDescriptionInventaire() {
        return this.shortDescription;
    }

    @Override
    public void performClick(Player joueur) {
        try {
            joueur.closeInventory();
            WorldDownloader.download(this, joueur);
        } catch (Exception e) {
            e.printStackTrace();
            joueur.sendMessage(e.getMessage());
        }
    }

    public static MapDownloadItem fromMapInfo(MapInfo mapInfo) {
        return new MapDownloadItem(mapInfo.map_name, mapInfo.map_url, mapInfo.map_description, mapInfo.map_size, mapInfo.map_size_display, mapInfo.map_file_name);
    }

    public String getMapUrl() {
        return this.mapUrl;
    }

    public String getShortDescription() {
        return this.shortDescription;
    }

    public String getMapName() {
        return this.mapName;
    }

    public String getMapSizeDisplay() {
        return this.mapSizeDisplay;
    }

    public String getMapSize() {
        return this.mapSize;
    }

    public String getMapFileName() {
        return this.mapFileName;
    }
}

