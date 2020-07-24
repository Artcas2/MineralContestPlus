package fr.synchroneyes.mineral.Shop.Items.Permanent;

import fr.synchroneyes.groups.Core.Groupe;
import fr.synchroneyes.mineral.Shop.Items.Abstract.PermanentItem;
import fr.synchroneyes.mineral.Shop.ShopManager;
import fr.synchroneyes.mineral.Translation.Lang;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class EpeeDiamant extends PermanentItem {

    @Override
    public String getNomItem() {
        return Lang.shopitem_diamond_sword_title.toString();
    }

    @Override
    public String[] getDescriptionItem() {
        return new String[]{Lang.shopitem_diamond_sword_desc.toString()};
    }

    @Override
    public Material getItemMaterial() {
        return Material.DIAMOND_SWORD;
    }

    @Override
    public void onItemUse() {

        Groupe playerGroup = mineralcontest.getPlayerGroupe(joueur);

        if (playerGroup == null) return;


        // On supprime l'épée de base
        List<ItemStack> items_de_base = playerGroup.getPlayerBaseItem().getItems();
        for (ItemStack item : items_de_base)
            if (item != null && item.getType().toString().toLowerCase().contains("sword"))
                joueur.getInventory().remove(item);


        joueur.getInventory().addItem(new ItemStack(Material.DIAMOND_SWORD, 1));
    }


    @Override
    public int getPrice() {
        return ShopManager.getBonusPriceFromName("permanent_diamond_sword");
    }

}