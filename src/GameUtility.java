import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.ParseException;
import java.util.Scanner;
import java.io.IOException;

public abstract class GameUtility {
    public static String makePosString(int x, int y){
        return String.format("(%d,%d)",x,y);
    }

    public static int rollDice(String in){
        if(in.length() == 0){
            return 0;
        }else{
            int num = 1, sides = 0;
            if (in.matches("\\d*[d]\\d+")) {
                String[] token = in.split("d");
                if(token[0].length() != 0){
                    num = Integer.parseInt(token[0]);
                }
                if(token[1].length() != 0){
                    sides = Integer.parseInt(token[1]);
                }

                int v = 0;
                for(int i=0;i<num;i++){
                    v += intRand(1,sides);
                }
                return v;
            } else {
                return 0;
            }
        }
    }

    public static void loadWeapons(){
        File folder = new File("saved/weapons");
        File[] files = folder.listFiles();
        FileReader finp;
        Scanner line;

        for(File file : files) {
            if(file.isFile()) {
                try {
                    finp = new FileReader("saved/weapons/" + file.getName());
                    line = new Scanner(finp);

                    try {
                        do {
                            Weapon temp = Weapon.loadFromCsv(line.nextLine());
                            Main.weapons.add(temp);
                        } while (line.hasNext());
                    } catch (CsvReadException | ParseException e) {
                        System.out.println(e.getMessage());
                    } finally {
                        try {
                            finp.close();
                            line.close();
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                } catch (FileNotFoundException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public static void savePlayers(Scanner in){
        Player[] players = new Player[0];

        for(Creature creature : Main.creatures){
            if(creature instanceof Player){
                Player[] temp = new Player[players.length+1];
                for(int i=0;i< players.length;i++){
                    temp[i] = players[i];
                }
                temp[players.length] = (Player)creature;
                players = temp;
            }
        }

        if(Main.creatures.size() != 0) {
            String options = "Save a character:\n";
            for (int i = 1; i <= Main.creatures.size(); i++) {
                if (i < Main.creatures.size()) {
                    options = options.concat(String.format("%d. %s\n", i, players[i - 1]));
                } else {
                    options = options.concat(String.format("%d. %s\n%d. %s", i, players[i - 1], i + 1, "Go back"));
                }
            }
            int sel = intPrompt(in, options, ">", 1, players.length + 1);

            if (sel != players.length + 1) {

                Player chosen = players[sel - 1];
                try {
                    FileWriter out = new FileWriter("saved/players/" + chosen.getName() + ".csv", false);
                    out.write(String.format("%s,%d,%d,%d,%d,%d,%s,%s,%d", chosen.getName(), chosen.getAC(), chosen.getHP(), chosen.getSTR(), chosen.getDEX(), chosen.getCON(), chosen.getWeapon().getName(), chosen.getWeapon().getDiceType(), chosen.getWeapon().getBonus()));
                    out.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }else{
            System.out.println("There are no players to save!");
        }
    }

    public static void loadPlayers(Scanner in){
        File folder = new File("saved/players");
        File[] files = folder.listFiles();
        Player[] players = new Player[0];
        FileReader finp;
        Scanner line;

        for(File file : files) {
            if(file.isFile()) {
                try {
                    finp = new FileReader("saved/players/" + file.getName());
                    line = new Scanner(finp);

                    try {
                        Player[] ptemp = new Player[players.length+1];

                        for(int i=0;i<players.length;i++){
                            ptemp[i] = players[i];
                        }

                        Player temp = Player.loadFromCsv(line.nextLine());
                        ptemp[players.length] = temp;
                        players = ptemp;
                    } catch (CsvReadException | ParseException e) {
                    } finally {
                        try {
                            finp.close();
                            line.close();
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                } catch (FileNotFoundException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        if(players.length!=0) {
            String options = "Load a character:\n";
            for (int i = 1; i <= players.length; i++) {
                if (i < players.length) {
                    options = options.concat(String.format("%d. %s\n", i, players[i - 1]));
                } else {
                    options = options.concat(String.format("%d. %s\n%d. %s", i, players[i - 1],i+1,"Go back"));
                }
            }
            int sel = intPrompt(in, options, ">", 1,players.length+1);

            if(sel != players.length+1) {
                for(Creature creature : Main.creatures){
                    if(creature.equals(players[sel-1])){
                        System.out.println("Player already exists!");
                        return;
                    }
                }
                Main.creatures.add(players[sel-1]);
            }
        }
    }

    public static void clearMonsters(){
        for(int i=0;i<Main.creatures.size();i++){
            if(Main.creatures.get(i) instanceof Monster){
                Main.creatures.remove(i);
                i=0;
            }
        }
    }

    public static void loadMonsters(){
        File folder = new File("saved/monsters");
        File[] files = folder.listFiles();
        FileReader finp;
        Scanner line;

        for(File file : files) {
            if(file.isFile()) {
                try {
                    finp = new FileReader("saved/monsters/" + file.getName());
                    line = new Scanner(finp);

                    try {
                        do {
                            Monster temp = Monster.loadFromCsv(line.nextLine());
                            Main.creatures.add(temp);
                        } while (line.hasNext());
                    } catch (CsvReadException | ParseException e) {
                        System.out.println(e.getMessage());
                    } finally {
                        try {
                            finp.close();
                            line.close();
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                } catch (FileNotFoundException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public static void validateName(String name) throws ParseException {
        if(name.matches("[A-Z]*[a-z]*")){
            if(!name.matches("[A-Z]+[a-z]*")){
                throw new ParseException("Name must start with a capital letter",0);
            }
            if(name.length() > 24){
                throw new ParseException("Name must not exceed 24 characters",name.length());
            }
        }else{
            throw new ParseException("Name must not contain any special characters or numbers",0);
        }
    }

    public static int intRand(int min, int max){
        return (int)((Math.random() * max)+min);
    }

    public static int intPrompt(Scanner input, String options, String query, int min, int max) {
        int sel;
        if (options.length() != 0){
            System.out.printf("\n%s\n", options);
        }
        do{
            System.out.printf("%s ", query);
            try{
                sel = input.nextInt();
            }catch(Exception notInt){
                sel = -1;
                System.out.print("\nPlease enter an integer value\n");
            }
            input.nextLine();
        }while(sel < min || sel > max);

        return sel;
    }

    public static int stringCmd(Scanner in, String msg, String emb, Player cur, int moves, Game game){
        int result;
        Position now = game.positions.get(cur.getPos());

        System.out.println(msg);
        do {
            System.out.printf("%s ",emb);
            String input = in.nextLine().trim();
            if(input.length() <= moves && input.length()!=0) {
                if (input.matches("[wasdWASD]{1,5}")) {
                    for (int i = 0; i < input.length(); i++) {
                        if (input.substring(i, i + 1).equalsIgnoreCase("w")) {
                            result = now.decY(game);
                            switch(result){
                                case 0:
                                    //update HashMap ;(
                                    game.positions.put(now.getCode(),now);
                                    game.positions.remove(cur.getPos());
                                    cur.setPos(now.getCode());
                                    break;
                                case 1:
                                    return 10 + i;
                                case 2:
                                    return 20 + i;
                            }
                        }
                        if (input.substring(i, i + 1).equalsIgnoreCase("a")) {
                            result = now.decX(game);
                            switch(result){
                                case 0:
                                    game.positions.put(now.getCode(),now);
                                    game.positions.remove(cur.getPos());
                                    cur.setPos(now.getCode());
                                    break;
                                case 1:
                                    return 10 + i;
                                case 2:
                                    return 20 + i;
                            }
                        }
                        if (input.substring(i, i + 1).equalsIgnoreCase("s")) {
                            result = now.incY(game);
                            switch(result){
                                case 0:
                                    game.positions.put(now.getCode(),now);
                                    game.positions.remove(cur.getPos());
                                    cur.setPos(now.getCode());
                                    break;
                                case 1:
                                    return 10 + i;
                                case 2:
                                    return 20 + i;
                            }
                        }
                        if (input.substring(i, i + 1).equalsIgnoreCase("d")) {
                            result = now.incX(game);
                            switch(result){
                                case 0:
                                    game.positions.put(now.getCode(),now);
                                    game.positions.remove(cur.getPos());
                                    cur.setPos(now.getCode());
                                    break;
                                case 1:
                                    return 10 + i;
                                case 2:
                                    return 20 + i;
                            }
                        }
                    }

                    if(game.positions.get(cur.getPos()).inVicinity(game).length == 0){
                        return 10 + input.length();
                    }else{
                        return 20 + input.length();
                    }
                }
            }
            if(input.length()>moves){
                System.out.println("You do not have enough moves for this!");
            }
            if (input.matches("[pP]")) {
                return 1;
            }
            if (input.matches("[qQ]")) {
                return 2;
            }
            System.out.print("\nPlease input a correct sequence or command\n");
        }while(true);
    }

    public static Player[] getPlayers(Creature[] creatures){
        Player[] players = new Player[0];
        for(Creature creature : creatures){
            if(creature instanceof Player){
                Player[] temp = new Player[players.length+1];
                for(int i=0;i<players.length;i++){
                    temp[i] = players[i];
                }
                temp[players.length] = (Player)creature;
                players = temp;
            }
        }
        return players;
    }

    public static Monster[] getMonsters(Creature[] creatures){
        Monster[] monsters = new Monster[0];
        for(Creature creature : creatures){
            if(creature instanceof Monster){
                Monster[] temp = new Monster[monsters.length+1];
                for(int i=0;i<monsters.length;i++){
                    temp[i] = monsters[i];
                }
                temp[monsters.length] = (Monster)creature;
                monsters = temp;
            }
        }
        return monsters;
    }

    public static Position nearestPlayer(Player[] players, Creature cur, Game game){
        Position thisPos = game.positions.get(cur.getPos());
        int thisX=thisPos.getX(),thisY=thisPos.getY(),minPos=0,min=Integer.MAX_VALUE,temp;

        for(Player player : players){
            temp = Math.abs(game.positions.get(player.getPos()).getX()-thisX);
            temp += Math.abs(game.positions.get(player.getPos()).getY()-thisY);

            if(temp<min){
                min = temp;
                minPos = player.getPos();
            }
        }

        return game.positions.get(minPos);
    }

    public static void resetHealth(){
        for(Creature creature : Main.creatures){
            if(creature instanceof Player){
                creature.setHP(1);
            }
        }
    }

    public static void createCharacter(Scanner uinp) {
        int choice, modifier = 10, str = -5, dex = -5, con = -5, vn = 1;
        String name;
        do {
            System.out.print("\nEnter Character Name: ");
            name = uinp.nextLine();

            try{
                GameUtility.validateName(name);
                vn = 0;
            }catch(ParseException e){
                System.out.printf("\n%s",e.getMessage());
            }
        }while(vn==1);

        choice = GameUtility.intPrompt(uinp, "1. Manual Stats\n2. Random Stats", "Manual or Random Stats?", 1, 2);
        if (choice == 1) {
            do {
                choice = GameUtility.intPrompt(uinp, String.format("STR: %d\nDEX: %d\nCON: %d\nRemaining: %d\n\n1. Add STR\n2. Add DEX\n3. Add CON\n4. Reset\n5. Finish", str + 5, dex + 5, con + 5, modifier), ">", 1, 5);
                switch (choice) {
                    case 1 -> {
                        str = modifier > 0 ? str + 1 : str;
                        modifier = modifier != 0 ? modifier - 1 : 0;
                    }
                    case 2 -> {
                        dex = modifier > 0 ? dex + 1 : dex;
                        modifier = modifier != 0 ? modifier - 1 : 0;
                    }
                    case 3 -> {
                        con = modifier > 0 ? con + 1 : con;
                        modifier = modifier != 0 ? modifier - 1 : 0;
                    }
                    case 4 -> {
                        str = -5;
                        dex = -5;
                        con = -5;
                        modifier = 10;
                    }
                }
            } while (choice != 5);
        } else {
            while (modifier != 0) {
                choice = GameUtility.intRand(1, 10);
                choice = Math.min(choice, modifier);
                modifier -= choice;

                switch (GameUtility.intRand(1, 3)) {
                    case 1 -> str += choice;
                    case 2 -> dex += choice;
                    case 3 -> con += choice;
                }
            }
        }

        for(int i=0;i< Main.weapons.size();i++){
            System.out.printf("\n%d. %s, Damage: %s, Bonus To-Hit: %d",i+1,Main.weapons.get(i).getName(),Main.weapons.get(i).getDiceType(),Main.weapons.get(i).getBonus());
        }

        choice = GameUtility.intPrompt(uinp,"","\nSelect weapon:",1,Main.weapons.size());

        str = Math.max(str, 0);
        dex = Math.max(dex, 0);
        con = Math.max(con, 0);

        Player temp = new Player(name,15+dex,50+con,str,dex,con,Main.weapons.get(choice-1));
        System.out.printf("~==========================~\n%s",temp);

        choice = GameUtility.intPrompt(uinp,"1. Confirm\n2. Deny","Add new character to player list?",1,2);
        if(choice == 1){
            Main.creatures.add(temp);
            System.out.print("\nPlayer added successfully!\n");
        }
    }
}