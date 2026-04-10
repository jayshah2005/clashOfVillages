package src.Network.Server;

import src.Network.Packet;
import src.PlayerAccount.Buildings.Building;
import src.PlayerAccount.Player;
import src.PlayerAccount.Resources;
import src.PlayerAccount.Village;
import src.PlayerAccount.VillageObject;
import src.Utility.AttackResolver;
import src.Utility.ChallengeAdapter;
import src.Utility.InputChecker;
import src.Utility.Position;
import src.enums.AttackResult;
import src.enums.Buildings;
import src.enums.Fighters;
import src.enums.View;
import src.exceptions.NoPlayerFoundException;
import java.util.concurrent.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class ClientHandler implements Runnable {


    Socket socket;
    ObjectInputStream in;
    ObjectOutputStream out;
    Server server;
    View currentView;


    public ClientHandler(Socket socket, Server server) throws IOException {
        this.socket = socket;
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
        this.server = server;

    }

    @Override
    public void run() {

        try {
            Player p = getPlayer(out, in);
            if (p == null) return;
            System.out.println("Player " + p.getName() + " created/loaded and ready to go");


            String inp = "";
            String output;
            Packet packet;
            boolean allowed;

            while(!inp.equals("quit")){

                try{
                    packet =  (Packet) in.readObject();
                } catch (IOException e) {
                    System.out.println("Player " + p.getName() + ": Error receiving packet.");
                    try{
                        this.socket.close();
                        return;
                    } catch (IOException e1) {
                        throw new RuntimeException("Error closing socket: " + e1);
                    }
                }

                inp = packet.getMessage().toLowerCase();
                this.currentView = packet.getCurrentView();

                allowed = this.isInputVerifiedAndAuthorzied(inp, p);

                try{
                    out.writeObject(new Packet(allowed));
                }
                catch (IOException e1) {throw new RuntimeException("Error sending packet (not) allowing user action.");}

                if(!allowed) continue;

                output = this.processInput(p, inp);

                try{
                    out.reset();
                    out.writeObject(new Packet(output, currentView, new Object[]{p}));
                }
                catch (IOException e1) {throw new RuntimeException("Error sending packet about output of user action.");}

                System.out.println("Player " + p.getName() + " received: " + inp);
            }

        } catch (ClassNotFoundException e){
            throw new RuntimeException("Wrong class: " + e);
        }
    }

    /**
     * given an input the input will be checked to see if its valid and authorized based on the current view.
     * so if you are in the village view you can select shop, upgrade, train, attack or quit
     * these are all of the valid inputs at that current view.
     *
     * @param p player inp is being processed for
     * @param inp the inp being processed
     * @return a string indicating what happened
     */
    public String processInput(Player p, String inp) {

        // This should never happen so thus if it does, we probably need to restart the game
        String err = "Unable to process input. Please restart the game by quiting (type: 'quit')";
        String inpCased = inp.toLowerCase();

        if(inpCased.equals("quit")) return null;

        switch(this.currentView){
            case VILLAGE -> {
                return handleVillageInput(p, inpCased);
            }
            case SHOP -> {
                return handleShopInput(p, inpCased);
            }
            case UPGRADE -> {
                return handleUpgradeInput(p, inpCased);
            }
            case TRAIN -> handleTrainInput(p, inpCased);
            case ATTACK -> this.currentView = View.VILLAGE;
            case TEST -> {
                return handleTestInput(p, inpCased);
            }
            default -> {return err;}

        }

        return null;
    }

    /**
     * Test input will prompt the player if they want to generate an army to test their village or if they want to
     * generate a vilalge and test their army.
     * @param p
     * @param inp
     * @return
     */
    private String handleTestInput(Player p, String inp){

        switch (inp) {
            case "army" -> {
                AttackResult result = testGeneratedArmy(p);
                if (result == AttackResult.SUCCESS) {
                    return "Your village LOST the defense test.";
                } else {
                    return "Your village SUCCESSFULLY defended against the generated army.";
                }
            }

            case "village" -> {
                return testVillage(p);
            }

            case "back" -> {
                this.currentView = View.VILLAGE;
                return null;
            }
            default -> {
                return "Invalid test option.";
            }
        }
    }

    /**
     * Test player army against randomly generated villages of similar defence scores
     * @param attacker
     * @return
     */
    public String testVillage(Player attacker) {

        int totalTests = 100;

        //thread pool, 10 at once
        ExecutorService executor = Executors.newFixedThreadPool(10);
        List<Future<AttackResult>> futures = new ArrayList<>();

        //submit tasks
        for (int i = 0; i < totalTests; i++) {
            futures.add(executor.submit(() -> testGeneratedVillage(attacker)));
        }

        int wins = 0;

        //collect results
        for (Future<AttackResult> f : futures) {
            try {
                if (f.get() == AttackResult.SUCCESS) {
                    wins++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //close
        executor.shutdown();

        double successRate = (wins * 100.0) / totalTests;

        return "Army Attack Success Rate: " + successRate + "% (" + wins + "/" + totalTests + ")";
    }

    /**
     * attack generated village
     * @param attacker
     * @return
     */
    public AttackResult testGeneratedVillage(Player attacker) {
        Player defender = generateTestVillage(attacker);
        AttackResolver resolver = new ChallengeAdapter();

        return resolver.resolveAttack(attacker, defender);
    }

    /**
     * create test village
     * @param attacker
     * @return
     */
    public Player generateTestVillage(Player attacker) {
        Player defender = new Player("AI_Defender");

        defender.getVillage().setResources(new Resources(1000, 1000, 1000));

        Position hallPos = getRandomFreePosition(defender.getVillage());
        if (hallPos != null) {
            defender.placeTownHall(hallPos);
        }

        float attackStrength = getAttackScore(attacker);

        int towers = Math.max(1, (int)(attackStrength / 50));
        int cannons = Math.max(1, (int)(attackStrength / 100));
        int farms = Math.max(1, (int)(attackStrength / 80));
        int mines = Math.max(1, (int)(attackStrength / 80));

        placeGeneratedBuildings(defender, Buildings.ARCHERTOWER, towers);
        placeGeneratedBuildings(defender, Buildings.CANNON, cannons);
        placeGeneratedBuildings(defender, Buildings.FARM, farms);
        placeGeneratedBuildings(defender, Buildings.GOLDMINE, mines);
        placeGeneratedBuildings(defender, Buildings.IRONMINE, mines);
        placeGeneratedBuildings(defender, Buildings.LUMBERMILL, mines);

        return defender;
    }

    /**
     * place buildings (in available spaces) for test village
     * @param defender
     * @param buildingType
     * @param amount
     */
    private void placeGeneratedBuildings(Player defender, Buildings buildingType, int amount) {
        for (int i = 0; i < amount; i++) {
            Position pos = getRandomFreePosition(defender.getVillage());

            if (pos == null) {
                break;
            }

            defender.getVillage().purchaseBuilding(buildingType, pos);
        }
    }

    public float getAttackScore(Player player) {

        AtomicReference<Float> attackScore = new AtomicReference<>(0f);

        player.fighters.forEach((f, amount) -> attackScore.updateAndGet(v ->
                v + f.getAttackScore() * amount
                        + (amount > 1 ? amount * 0.5f : 0f)
        ));

        return attackScore.get();
    }

    /**
     * gets free position on map for randomly generated test village
     * @param village
     * @return
     */
    private Position getRandomFreePosition(Village village) {
        Random rand = new Random();
        int width = village.getMap().getGrid().length;
        int height = village.getMap().getGrid()[0].length;

        for (int i = 0; i < 100; i++) {
            int x = rand.nextInt(width);
            int y = rand.nextInt(height);

            if (village.getMap().getGrid()[x][y] == null) {
                return new Position(x, y);
            }
        }

        return null;
    }

    public Map<Fighters, Integer> generateArmy(Village village) {
        Map<Fighters, Integer> army = new HashMap<>();

        double defense = village.getDefenceCapacity();

        int soldiers = Math.max(1, (int)(defense * 0.4 / 100));
        int archers  = Math.max(1, (int)(defense * 0.3 / 80));
        int knights  = Math.max(1, (int)(defense * 0.2 / 200));
        int catapults = Math.max(1, (int)(defense * 0.1 / 300));

        army.put(Fighters.SOLDIER, soldiers);
        army.put(Fighters.ARCHER, archers);
        army.put(Fighters.KNIGHT, knights);
        army.put(Fighters.CATAPULT, catapults);

        return army;
    }

    public AttackResult testGeneratedArmy(Player defender) {
        Map<Fighters, Integer> generatedArmy = generateArmy(defender.getVillage());

        Player attacker = new Player("AI_Test");
        attacker.fighters = generatedArmy;

        AttackResolver resolver = new ChallengeAdapter();

        return resolver.resolveAttack(attacker, defender);
    }

    /**
     * upgrade inputs checks if a valid number is entered, and if you have the resources to build the structure
     * upgrade will list all of your buildings, this checks if you selected a building within the list and if you can do
     * the upgrade
     *
     * @param p curr player
     * @param inp the input being procesesd
     * @return a string indicating the outcome of an event
     */
    private String handleUpgradeInput(Player p, String inp) {

        if(inp.equals("back")) {
            this.currentView = View.VILLAGE;
            return null;
        }

        int index;

        try{
            index = Integer.parseInt(inp);
        }catch(Exception e){
            return "Invalid building selection.";
        }

        List<VillageObject> buildings = p.village.getVillageObjects();

        if(index < 1 || index > buildings.size()){
            return "Invalid building number.";
        }

        VillageObject obj = buildings.get(index - 1);

        if(!(obj instanceof Building)){
            return "Cannot upgrade this object.";
        }

        boolean success = p.village.upgradeBuilding((Building)obj);

        if(success){
            return "Building upgraded successfully!";
        }

        return "Upgrade failed.";
    }

    /**
     * handles all the inputs for training units, and creates the unit if valid input is provided
     * @param inp
     * @return
     */
    private String handleTrainInput(Player p, String inp) {

        if(inp.equals("back")) {
            this.currentView = View.VILLAGE;
            return null;
        }

        Fighters fighter = Fighters.valueOf(inp.toUpperCase());
        Resources cost = fighter.getFighterCost();

        if(cost == null) {
            throw new NullPointerException("Fighter cost is null.");
        }

        p.village.resources.subtract(cost);
        p.createUnit(fighter);

        return fighter + " created successfully!";
    }

    /**
     * village is the main menu, the valid inputs are all of the other game states the player can set.
     * shop, attack, upgrade, train and gather. once a view is selected we switch the current view
     * @param inp
     * @return
     */
    private String handleVillageInput(Player p, String inp){
        switch (inp) {
            case "shop":
                this.currentView = View.SHOP;
                return null;
            case "attack":
                this.currentView = View.ATTACK;
                //return this.facilitateAttack(p);
                return this.facilitateAttackWithAdapter(p);
            case "upgrade":
                this.currentView = View.UPGRADE;
                return null;
            case "train":
                this.currentView = View.TRAIN;
                return null;
            case "test":
                this.currentView= View.TEST;
            case "gather":
                p.village.gatherResources();
                return null;
            default:
                // This should not happen if we already validate inputs beforehand
                // Try throwing an error
                return "Please enter a proper input";
        }
    }

    /**
     * handles shop inputs, first it checks if you selected a valid building. then it verifies if your coordinates to
     * place the building are valid
     * @param inp
     * @return
     */
    private String handleShopInput(Player p, String inp){

        // exit shop
        if(inp.equals("back")){
            this.currentView = View.VILLAGE;
            return null;
        }

        Buildings building;
        Packet packet;

        try{
            building = Buildings.valueOf(inp.toUpperCase());
        }catch(Exception e){
            return "invalid shop selection";
        }

        try{
            out.writeObject(new Packet("build"));
        } catch (IOException e) {
            throw new RuntimeException("Error sending input information to player" + e);
        }

        try{packet = (Packet) in.readObject();}
        catch (IOException | ClassNotFoundException e) {throw new RuntimeException(e);}

        Object[] arr = packet.getPayload();

        int x = Integer.parseInt(arr[0].toString());
        int y = Integer.parseInt(arr[1].toString());

        Position pos = new Position(x,y);

        boolean success = p.village.purchaseBuilding(building,pos);

        if(success){
            this.currentView = View.VILLAGE;
            return "Building was placed";
        }

        return "Could not place building";
    }

    /**
     * Facilitate attack handles the attacking logic
     * This method calls the adapater class to determine if an attack is successful or not between 2 play
     * @param p
     * @return
     */
    public String facilitateAttackWithAdapter(Player p){

        Player potentialTarget;
        Set<Player> notEligible = new HashSet<>();
        notEligible.add(p);

        // attack using the Challenge Adapter class
        AttackResolver resolver = new ChallengeAdapter();
        String inp;
        Packet packet;

        while(true){
            potentialTarget = server.findRandomPlayerToAttack(notEligible);

            try {
                if(potentialTarget == null) out.writeObject(new Packet("attack", false));
                out.writeObject(new Packet("attack", true, new Object[]{potentialTarget}));
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }

            try{
                packet = (Packet) in.readObject();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Wrong input from server when getting input in attack screen: " + e);
            }

            inp = packet.getMessage().toLowerCase();

            if(inp.equals("y")){

                AttackResult result = resolver.resolveAttack(p, potentialTarget);

                if (result == AttackResult.SUCCESS) {

                    Resources loot = potentialTarget.getVillage().getResources().clone();
                    loot.multiply(0.3);

                    potentialTarget.getVillage().getResources().subtract(loot);
                    p.getVillage().getResources().add(loot);

                    try {
                        out.writeObject(new Packet(new Object[]{1.0, loot}));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    try {
                        out.writeObject(new Packet(new Object[]{1.0, new Resources()}));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                this.processInput(p, "back");
                break;
            }

            if(inp.equals("next")) continue;

            if(inp.equals("n")){
                this.processInput(p, "home");
                break;
            }
        }

        return null;
    }

    /**
     * authorize player inputs by checking if that input command exists at that current view
     * @param inp user input
     * @param p player we are verifying the inp for
     * @return a boolean value indicating the valididty of an input
     */
    private boolean isInputVerifiedAndAuthorzied(String inp, Player p) {
        InputChecker ic = new InputChecker();

        if(inp.equals("quit")) return true;

        try{
            if(!ic.isInputValid(inp, this.currentView)) {
                System.out.println("Not a valid input");
                return false;
            }

            if(!ic.isInputAllowed(inp, this.currentView, p)){
                System.out.println("You are not allowed to perform this action");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();    // This is server logging so server knows the error happened
            return false;
        }

        return true;
    }

    /**
     * determines which player account is loaded, if there are no players the user can create a player.
     * If we create a player, we also need to place the initial townhall
     * Once the player is loaded/created, we lock it so others cannot load this account.
     * @return the player that is loaded
     */
    public Player getPlayer(ObjectOutputStream out, ObjectInputStream in) {
        Packet packet;
        String name;
        Player p;

        try{
            // Tell the client if there are any saved players
            out.writeBoolean(!server.players.isEmpty());
            out.writeObject(new Packet(getPlayerNames()));  // send a list of saved player names to client for them to select
        } catch (Exception e) {
            throw new RuntimeException("Error sending player information when loading: " + e);
        }

        try{
            packet = (Packet) in.readObject();
        } catch (Exception e) {
            System.out.println("Sudden connection closed when getting player name: " + e);

            try{
                this.socket.close();
            } catch (IOException e1) {
                throw new RuntimeException("Error closing socket: " + e);
            }

            return null;
        }

        name = packet.getMessage();

        if(server.players.stream().map(Player::getName).anyMatch(name::equals)){
            p = server.players.stream().filter(player -> player.name.equals(name)).findFirst().get();
            server.players.remove(p);
        } else {
            p = new Player(name);
        }

        server.activePlayers.add(p);

        try {
            if(p.getVillage().getVillageObjects().isEmpty()){
                out.writeObject(new Packet("create"));

                Position pos;
                boolean placed;

                do{
                    packet = (Packet) in.readObject();
                    pos = (Position) packet.getPayload()[0];
                    placed = p.placeTownHall(pos);
                    out.writeBoolean(placed);
                }while (!placed);

            } else {
                out.writeObject(new Packet("load"));
            }

            out.writeObject(new Packet(new Object[]{p}));

        } catch (IOException | ClassNotFoundException e) {
            throw new  RuntimeException(e);
        }



        return p;
    }

    /**
     * Get all player names that we have loaded
     * @return get all player names
     */
    private String[] getPlayerNames(){
        return server.players.stream().map(Player::getName).toArray(String[]::new);
    }

}
