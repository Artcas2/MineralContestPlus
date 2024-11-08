package fr.synchroneyes.data_storage;

import fr.synchroneyes.data_storage.DatabaseTablesName;
import fr.synchroneyes.file_manager.FileList;
import fr.synchroneyes.mineral.mineralcontest;
import java.io.File;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class DatabaseInitialisation {
    public static void createDatabase() throws Exception {
        boolean sqlInstallationRequired = false;
        File SQLFile = new File(mineralcontest.plugin.getDataFolder(), FileList.MySQL_database_schema.toString());
        if (!SQLFile.exists()) {
            throw new Exception("SQL Schema file doesnt exists: " + FileList.MySQL_database_schema.toString());
        }
        Connection connection = mineralcontest.plugin.getConnexion_database();
        if (connection == null) {
            return;
        }
        for (DatabaseTablesName table : DatabaseTablesName.values()) {
            PreparedStatement show_table = connection.prepareStatement("SHOW TABLES LIKE ?");
            show_table.setString(1, table.toString());
            show_table.execute();
            ResultSet resultSet = show_table.getResultSet();
            if (resultSet.next()) continue;
            sqlInstallationRequired = true;
            break;
        }
        if (!sqlInstallationRequired) {
            return;
        }
        StringBuilder sqlFileContent = new StringBuilder();
        List<String> lignes_fichiers = Files.readAllLines(SQLFile.toPath());
        for (String ligne : lignes_fichiers) {
            if (ligne.length() <= 1) continue;
            mineralcontest.plugin.getConnexion_database().createStatement().execute(ligne);
        }
        Bukkit.getConsoleSender().sendMessage(mineralcontest.prefix + ChatColor.GREEN + "SQL tables created!");
    }
}

