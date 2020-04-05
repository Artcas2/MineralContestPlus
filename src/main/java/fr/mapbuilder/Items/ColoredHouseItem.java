package fr.mapbuilder.Items;

import fr.mapbuilder.Blocks.BlocksDataColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ColoredHouseItem {

    private ItemStack item;
    private BlocksDataColor blockColor;
    public static String itemPrefix = "Maison ";

    public ColoredHouseItem(BlocksDataColor houseColor) {
        this.item = houseColor.toItemStack();
        this.blockColor = houseColor;
        ItemMeta itemMeta = this.item.getItemMeta();
        itemMeta.setDisplayName(itemPrefix + houseColor.color);
        this.item.setItemMeta(itemMeta);
    }

    public void giveItemToPlayer(Player p) {
        p.getInventory().addItem(item);
    }

}
