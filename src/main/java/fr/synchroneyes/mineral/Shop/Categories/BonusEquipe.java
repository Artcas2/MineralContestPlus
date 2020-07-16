package fr.synchroneyes.mineral.Shop.Categories;

import fr.synchroneyes.mineral.Shop.Categories.Abstract.Category;
import fr.synchroneyes.mineral.Shop.NPCs.BonusSeller;
import org.bukkit.Material;

public class BonusEquipe extends Category {

    public BonusEquipe(BonusSeller npc) {
        super(npc);
    }

    @Override
    public String getNomCategorie() {
        return "Bonus équipe";
    }

    @Override
    public Material getItemMaterial() {
        return Material.ENDER_CHEST;
    }

    @Override
    public String[] getDescription() {
        return new String[0];
    }
}
