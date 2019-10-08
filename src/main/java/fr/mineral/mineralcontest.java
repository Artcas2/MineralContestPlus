package fr.mineral;

import fr.mineral.Events.Mort;
import fr.mineral.Events.Spawn;
import fr.mineral.Exception.FullTeamException;
import fr.mineral.scoreboard.scoreboard;

import fr.mineral.Teams.Equipe;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.*;

public final class mineralcontest extends JavaPlugin implements CommandExecutor, Listener {

    // liste chainee de joueurs
    private Equipe teamJaune;
    private Equipe teamRouge;
    private Equipe teamBleu;
    private LinkedList<Equipe> teamList = new LinkedList<Equipe>();

    private Location positionSpawnArene;
    private Coffre coffre;
    private scoreboard scoreBoard;


    public static String prefix = ChatColor.BLUE + "[MINERALC] " + ChatColor.WHITE;
    public static String prefixErreur = ChatColor.BLUE + "[MINERALC] " + ChatColor.RED + "[ERREUR] " + ChatColor.WHITE;
    public static String prefixGlobal = ChatColor.BLUE + "[MINERALC] " + ChatColor.GREEN + "[GLOBAL] " + ChatColor.WHITE;
    public static String prefixPrive = ChatColor.BLUE + "[MINERALC] " + ChatColor.YELLOW + "[PRIVE] " + ChatColor.WHITE;


    // 60*60 car dans 60min il y a 60*60 sec
    public static int timeLeft = 60*60-1;
    public static int teamMaxPlayers = 1;
    private int gameStarted = 0;


    public static String ERROR_GAME_ALREADY_STARTED = "La partie à déjà commence";
    public static String ERROR_ALL_TEAM_NOT_FULL = "Au moins une équipe n'est pas complète. Il faut " + mineralcontest.teamMaxPlayers + " joueur(s) par équipe.";
    public static String ERROR_NOT_ENOUGHT_PLAYER = "Il n'y a pas assez de joueur connecté";
    public static String ERROR_PLAYER_NOT_IN_TEAM = "Vous devez être dans une équipe";
    public static String ERROR_ARENA_NOT_DEFINED = "Le spawn pour l'arène n'a pas encore été ajouté";
    public static String ERROR_GAME_NOT_STARTED = "La partie n'a pas encore démarrer.";
    public static String ERROR_CHEST_NOT_DEFINED = "La position du coffre n'a pas été défini";

    public static String GAME_SUCCESSFULLY_STARTED = "La partie vient de commencer";
    public static String GAME_STARTING = "La partie va démarrer";
    public static String GAME_WAITING_START = "En attente du démarrage de la partie";

    public static String ARENA_SPAWN_ADDED = "Le spawn pour l'arène a bien été ajouté";
    public static String ARENA_TELEPORTING = "Téléportation vers l'arène.";

    public static String RANDOMIZE_TEAM_BEGIN = "Vous allez etre attribué à une equipe de manière aléatoire.";
    public static String RANDOMIZE_TEAM_END = "Les équipes ont été crée !";

    public static String GLOBAL_CHEST_SPAWNED = "Le coffre est apparu dans l'arène !";
    public static String CHEST_DEFINED = "La position du coffre a bien été enregistrée";

    public static String GAME_STARTING_CHECKS = "Démarage des vérifications ...";

    public static mineralcontest plugin;

    // Constructeur, on initialise les variables
    public mineralcontest() {

        this.teamJaune = new Equipe("Jaune", ChatColor.YELLOW);
        this.teamRouge = new Equipe("Rouge", ChatColor.RED);
        this.teamBleu = new Equipe("Bleu", ChatColor.BLUE);

        this.coffre = new Coffre();

        mineralcontest.plugin = this;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("onEnable has beezn invoked!");
        Bukkit.getServer().getPluginManager().registerEvents(new Mort(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new Spawn(), this);

    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){

        if (cmd.getName().equalsIgnoreCase("setScoreBoard")){

            try {
                this.scoreBoard = new scoreboard();
                Bukkit.getServer().broadcastMessage("scoreBoard set");
            }catch (Exception e){

            }
        }

        if(cmd.getName().equalsIgnoreCase("join")){
            if(sender instanceof Player) {
                Player joueur = (Player) sender;
                if(args.length == 1) {
                    try {
                        this.addPlayerToTeam(joueur, args[0]);
                    }catch (Exception e) {
                        joueur.sendMessage(this.prefixErreur + e.getMessage());
                    }
                } else {
                    joueur.sendMessage(this.prefixErreur + "Utilisation de la commande: /join <team>");
                }
            }
            return true;

        }

        if(cmd.getName().equalsIgnoreCase("leave")){
            if(sender instanceof Player) {
                Player joueur = (Player) sender;
                try {
                    this.playerLeaveTeam(joueur);
                }catch (Exception e) {
                        joueur.sendMessage(this.prefixErreur + e.getMessage());
                }
            }
            return true;

        }

        if(cmd.getName().equalsIgnoreCase("start")){
            if(sender instanceof Player) {
                Player joueur = (Player) sender;
                try {
                    demarrerPartie();
                }catch (Exception e) {
                    joueur.sendMessage(this.prefixErreur + e.getMessage());
                }
            }
            return true;

        }

        if(cmd.getName().equalsIgnoreCase("randomizeTeam")){
            if(sender instanceof Player) {
                Player joueur = (Player) sender;
                try {
                    randomizeTeam();
                    this.teamList.add(teamBleu);
                    this.teamList.add(teamJaune);
                    this.teamList.add(teamRouge);

                    this.scoreBoard.setTeamScore(teamList);

                }catch (Exception e) {
                    joueur.sendMessage(this.prefixErreur + e.getMessage());
                }
            }

            return true;


        }

        if(cmd.getName().equalsIgnoreCase("setSpawn")){
            if(sender instanceof Player) {
                Player joueur = (Player) sender;
                try {
                    // TODO only person with certain permissions can do this cmd
                    Location loc = joueur.getLocation();
                    this.setAreneLocation(loc);

                }catch (Exception e) {
                    joueur.sendMessage(this.prefixErreur + e.getMessage());
                }
            }

            return true;
        }

        if(cmd.getName().equalsIgnoreCase("setCoffre")){
            if(sender instanceof Player) {
                Player joueur = (Player) sender;
                try {
                    // TODO only person with certain permissions can do this cmd
                    Location loc = joueur.getLocation();
                    this.coffre.setPosition(loc);
                    joueur.sendMessage(mineralcontest.prefixPrive + mineralcontest.CHEST_DEFINED);

                }catch (Exception e) {
                    joueur.sendMessage(this.prefixErreur + e.getMessage());
                }
            }

            return true;
        }

        if(cmd.getName().equalsIgnoreCase("spawnCoffre")){
            if(sender instanceof Player) {
                Player joueur = (Player) sender;
                try {
                    // TODO only person with certain permissions can do this cmd

                    this.coffre.spawn();
                    getServer().broadcastMessage(mineralcontest.prefixGlobal + mineralcontest.GLOBAL_CHEST_SPAWNED);
                }catch (Exception e) {
                    joueur.sendMessage(this.prefixErreur + e.getMessage());
                }
            }

            return true;
        }

        if(cmd.getName().equalsIgnoreCase("arene")){
            if(sender instanceof Player){
                Player joueur = (Player) sender;

                try{

                    if(this.gameStarted != 1) {
                        throw new Exception(this.ERROR_GAME_NOT_STARTED);
                    }

                    if(this.getPlayerTeam(joueur) == null)
                        throw new Exception(this.ERROR_PLAYER_NOT_IN_TEAM);


                    try {
                        Equipe team = getPlayerTeam(joueur);

                        for (Player P : team.getJoueurs()) {
                            P.teleport(this.positionSpawnArene);
                            P.sendMessage(this.prefixPrive + this.ARENA_TELEPORTING);
                        }
                    }catch(Exception e) {
                        joueur.sendMessage(this.prefixErreur + e.getMessage());
                    }


                }catch (Exception e){
                    joueur.sendMessage(this.prefixErreur + e.getMessage());
                }
            }

            return true;
        }

        if(cmd.getName().equalsIgnoreCase("teams")){
            if(sender instanceof Player){
                Player joueur = (Player) sender;

                try{
                    try {
                        getServer().broadcastMessage("================");
                        getServer().broadcastMessage(this.teamBleu.toString());
                        getServer().broadcastMessage(this.teamJaune.toString());
                        getServer().broadcastMessage(this.teamRouge.toString());
                        getServer().broadcastMessage("================");


                    }catch(Exception e) {
                        joueur.sendMessage(this.prefixErreur + e.getMessage());
                    }


                }catch (Exception e){
                    joueur.sendMessage(this.prefixErreur + e.getMessage());
                }
            }

            return true;
        }

        if(cmd.getName().equalsIgnoreCase("setHouse")){
            if(sender instanceof Player) {
                Player joueur = (Player) sender;
                try {
                    // TODO only person with certain permissions can do this cmd
                    Location loc = joueur.getLocation();

                    if(args.length == 1){
                        if(args[0].equals("rouge")){
                            teamRouge.setHouseLocation(loc);
                            for ( Player P : teamRouge.getJoueurs()) {
                                P.setBedSpawnLocation(teamRouge.getHouseLocation(), true);
                            }
                        }else if(args[0].equals("jaune")){
                            teamJaune.setHouseLocation(loc);
                            for ( Player P : teamJaune.getJoueurs()) {
                                P.setBedSpawnLocation(teamJaune.getHouseLocation(), true);
                            }
                        }else if(args[0].equals("bleu")){
                            teamBleu.setHouseLocation(loc);
                            for ( Player P : teamBleu.getJoueurs()) {
                                P.setBedSpawnLocation(teamBleu.getHouseLocation(), true);
                            }
                        }
                    }

                }catch (Exception e) {
                    joueur.sendMessage(this.prefixErreur + e.getMessage());
                }
            }

            return true;
        }

        return true;
    }


    public void setAreneLocation(Location areneLocation) {
        Bukkit.getServer().broadcastMessage(this.prefixGlobal + this.ARENA_SPAWN_ADDED);
        this.positionSpawnArene = areneLocation;
    }

    public Location getAreneLocation() throws Exception {
        if(this.positionSpawnArene == null)
            throw new Exception(this.ERROR_ARENA_NOT_DEFINED);
        return positionSpawnArene;
    }

    public void afficherEquipe() {
        getServer().broadcastMessage(this.prefixGlobal + this.teamBleu.toString());
        getServer().broadcastMessage(this.prefixGlobal + this.teamRouge.toString());
        getServer().broadcastMessage(this.prefixGlobal + this.teamJaune.toString());

    }

    private boolean demarrerPartie() throws Exception {

        if(this.gameStarted != 0) {
            throw new Exception(this.ERROR_GAME_ALREADY_STARTED);
        }

        getServer().broadcastMessage(this.prefixGlobal + this.GAME_STARTING);
        getServer().broadcastMessage("=============================");
        getServer().broadcastMessage(this.prefixGlobal + this.GAME_STARTING_CHECKS);
        // Pour démarrer la partie, il faut:
        // Tous les spawn maison défini
        // Spawn coffre arene définit
        // Spawn arene définit
        // Toutes les equipes soient pleine

        // SPAWN MAISON
        if(this.teamBleu.getHouseLocation() == null) {
            getServer().broadcastMessage(this.prefixGlobal + "[Verification] Spawn maison bleu: " + ChatColor.RED + "X");
            return false;
        }

        if(this.teamRouge.getHouseLocation() == null) {
            getServer().broadcastMessage(this.prefixGlobal + "[Verification] Spawn maison rouge: " + ChatColor.RED + "X");
            return false;
        }
        if(this.teamJaune.getHouseLocation() == null) {
            getServer().broadcastMessage(this.prefixGlobal + "[Verification] Spawn maison jaune: " + ChatColor.RED + "X");
            return false;
        }

        getServer().broadcastMessage(this.prefixGlobal + "[Verification] Spawn maison: " + ChatColor.GREEN + "OK");

        // SPAWN COFFRE ARENE
        if(this.coffre.getPosition() == null) {
            getServer().broadcastMessage(this.prefixGlobal + "[Verification] spawn coffre arene: " + ChatColor.RED + "X");
            return false;
        }
        getServer().broadcastMessage(this.prefixGlobal + "[Verification] Spawn coffre arene: " + ChatColor.GREEN + "OK");

        // SPAWN ARENE
        if(this.positionSpawnArene == null) {
            getServer().broadcastMessage(this.prefixGlobal + "[Verification] spawn arene: " + ChatColor.RED + "X");
            return false;
        }
        getServer().broadcastMessage(this.prefixGlobal + "[Verification] Spawn arene: " + ChatColor.GREEN + "OK");


        // EQUIPES PLEINE
        if(!this.teamRouge.isTeamFull()) {
            getServer().broadcastMessage(this.prefixGlobal + "[Verification] Equipe rouge pleine: " + ChatColor.RED + "X");
            return false;
        }
        if(!this.teamBleu.isTeamFull()) {
            getServer().broadcastMessage(this.prefixGlobal + "[Verification] Equipe bleu pleine: " + ChatColor.RED + "X");
            return false;
        }
        if(!this.teamJaune.isTeamFull()) {
            getServer().broadcastMessage(this.prefixGlobal + "[Verification] Equipe jaune pleine: " + ChatColor.RED + "X");
            return false;
        }
        getServer().broadcastMessage(this.prefixGlobal + "[Verification] Equipes pleines: " + ChatColor.GREEN + "OK");


        getServer().broadcastMessage(this.prefixGlobal + this.GAME_SUCCESSFULLY_STARTED);
        getServer().broadcastMessage("=============================");

        // On lance le timer
        new BukkitRunnable() {
            public void run() {

                if(timeLeft > 0) timeLeft--;
            }
        }.runTaskTimer(plugin, 0, 20);

        this.gameStarted = 1;
        return true;

    }

    // Créer les equipes aléatoirement
    public void randomizeTeam() throws Exception {
        if(this.gameStarted != 0) {
            throw new Exception(this.ERROR_GAME_ALREADY_STARTED);
        }

        if(mineralcontest.teamMaxPlayers*3 != getServer().getOnlinePlayers().size())
           throw new Exception(this.ERROR_NOT_ENOUGHT_PLAYER);

        ArrayList<String> team = new ArrayList<String>();


        getServer().broadcastMessage("================");


        getServer().broadcastMessage(this.prefixGlobal + this.RANDOMIZE_TEAM_BEGIN);
        for(int i = 0; i < mineralcontest.teamMaxPlayers; i++){
            team.add("jaune");
            team.add("rouge");
            team.add("bleu");
        }

        for(Player joueur : this.teamRouge.getJoueurs()) {
            this.teamRouge.removePlayer(joueur);
        }

        for(Player joueur : this.teamJaune.getJoueurs()) {
            this.teamJaune.removePlayer(joueur);
        }

        for(Player joueur : this.teamBleu.getJoueurs()) {
            this.teamBleu.removePlayer(joueur);
        }

        int random;
        Random r = new Random();
        String result;
        int indexJoueur = 0;
        Object[] joueurs = Bukkit.getServer().getOnlinePlayers().toArray();

        while(team.size() > 0 && indexJoueur != joueurs.length) {
            random = r.nextInt(team.size());
            result = team.get(random);

            if(result.equals("jaune")) {
                this.teamJaune.addPlayerToTeam((Player) joueurs[indexJoueur]);
                team.remove(random);
            }

            if(result.equals("rouge")) {
                this.teamRouge.addPlayerToTeam((Player) joueurs[indexJoueur]);
                team.remove(random);
            }

            if(result.equals("bleu")) {
                this.teamBleu.addPlayerToTeam((Player) joueurs[indexJoueur]);
                team.remove(random);
            }

            indexJoueur++;
        }

        getServer().broadcastMessage(this.prefixGlobal + this.RANDOMIZE_TEAM_END);
        getServer().broadcastMessage("================");

    }


    // Ajoute un joueur a une equipe
    private void addPlayerToTeam(Player p, String teamName) throws Exception {
        if(this.gameStarted != 0) {
            throw new Exception(this.ERROR_GAME_ALREADY_STARTED);
        }

        String[] equipes = {"rouge", "red", "bleu", "blue", "yellow", "jaune"};
        // On fait un foreach pour parcourir le tableau d'équipe
        for(String equipe : equipes) {
            // Le nom de l'équipe passé en commentaire existe
            if (teamName.toLowerCase().equalsIgnoreCase(equipe)) {
                switch (equipe) {
                    // On va vérifier si l'équipe est pleine ou non
                    // Si elle l'est, on retourne FALSE
                    // Sinon, on l'ajoute et on le supprime de son équipe initiale
                    case "red":
                    case "rouge":
                        if(this.teamRouge.isTeamFull()) {
                            throw new FullTeamException("rouge");
                        }
                        this.teamRouge.addPlayerToTeam(p);
                        break;

                    case "jaune":
                    case "yellow":
                        if(this.teamJaune.isTeamFull()) {
                            throw new FullTeamException("rouge");
                        }
                        this.teamJaune.addPlayerToTeam(p);
                        break;

                    case "blue":
                    case "bleu":
                        if(this.teamBleu.isTeamFull()) {
                            throw new FullTeamException("rouge");
                        }
                        this.teamBleu.addPlayerToTeam(p);
                        break;
                }
            }
        }
    }

    public void spawnCoffre() throws Exception {
        if(this.gameStarted != 1) {
            throw new Exception(this.ERROR_GAME_NOT_STARTED);
        }

        if(this.coffre.getPosition() == null)
            throw new Exception(this.ERROR_CHEST_NOT_DEFINED);



    }

    // Retourne vrai si le joueur est dans une equipe
    public boolean isPlayerInATeam(Player P) {
        return (this.teamBleu.isPlayerInTeam(P) || this.teamRouge.isPlayerInTeam(P) || this.teamJaune.isPlayerInTeam(P));
    }


    // Retourne l'equipe d'un joueur, NULL si sans team
    public Equipe getPlayerTeam(Player x) {
        if(this.teamBleu.isPlayerInTeam(x)) return this.teamBleu;
        if(this.teamRouge.isPlayerInTeam(x)) return this.teamRouge;
        if(this.teamJaune.isPlayerInTeam(x)) return this.teamJaune;
        return null;
    }


    // Retire un joueur de son equipe
    public boolean playerLeaveTeam(Player x) throws Exception {
        if(this.gameStarted != 0) {
            throw new Exception(this.ERROR_GAME_ALREADY_STARTED);
        }

        this.getPlayerTeam(x).removePlayer(x);
        return true;
    }

    public static String getTempsRestant() {
        int minutes, secondes;
        minutes = (timeLeft % 3600) / 60;
        secondes = (timeLeft % 60);
        return String.format("%02d:%02d", minutes, secondes);
    }

}
