import java.util.HashMap;

public class Game {
    private int turn=0,moves=5,dlength=2;
    private boolean escape = false;
    public final Position limits;
    public HashMap<Integer,Position> positions = new HashMap<>();

    public Game(int minx,int maxx,int miny, int maxy){
         limits = new Position(minx,miny,maxx,maxy);
    }

    public void play(){
        if(Main.creatures.size() >= 2) {
            int sel = GameUtility.intPrompt(Main.uinp, "1. Play with Random Monsters\n2. Play with Players Only (PvP)\n3. Back", ">", 1, 3);

            if (sel == 1) {
                GameUtility.loadMonsters();
            }

            for (Creature creature : Main.creatures) {
                creature.genPos(this);

            }
            takeTurn();
        }else{
            System.out.println("You must have at least 2 Players to play!");
        }
    }

    public void cycleTurn(){
        if(Main.creatures.size()-1 == turn){
            turn=0;
        }else {
            turn++;
        }
        moves=5;
        takeTurn();
    }

    public void takeTurn() {
        if(Main.creatures.get(turn) instanceof Player){
            if(Main.creatures.get(turn).getHP() > 0) {
                Player cur = (Player) Main.creatures.get(turn);

                while (moves != 0 && !escape) {
                    Map.printMap(cur, this);
                    int sel = GameUtility.stringCmd(Main.uinp, String.format("\tIt is now %s's turn! Enter a sequence of (WASD) characters or at a time, (P) to skip your turn, or (Q) to quit to main menu, %d moves left\n", cur.getName(), moves), ">", cur, moves, this);

                    switch (sel) {
                        case 1:
                            cycleTurn();
                            break;
                        case 2:
                            escape = true;
                            return;
                    }
                    if (sel > 10 && sel < 20) {
                        moves -= sel - 10;
                    }
                    if (sel > 20) {
                        moves -= sel - 20;
                        combat(cur);
                    }
                }
                if (!escape) {
                    cycleTurn();
                }
            }else{
                int HP = 0;
                for(Creature creature : Main.creatures){
                    if(creature instanceof Player){
                        HP += creature.getHP();
                    }
                }
                if(HP>0){
                    cycleTurn();
                }else{
                    System.out.println("Game Over!");
                    escape = true;
                }
            }
        }else{
            //monsterAI
            /*
            if(!escape) {
                Creature now = Main.creatures.get(turn);

                Creature[] creatures = new Creature[0];
                for (var creature : Main.creatures) {
                    Creature[] temp = new Creature[creatures.length + 1];
                    for (int i = 0; i < creatures.length; i++) {
                        temp[i] = creatures[i];
                    }
                    temp[creatures.length] = creature;
                    creatures = temp;
                }

                Position near = GameUtility.nearestPlayer(GameUtility.getPlayers(creatures), now, this);
                Position mine = positions.get(now.getPos());

                int targetX = near.getX(), targetY = near.getY(), myX, myY;
                while (moves != 0) {
                    myX = mine.getX();
                    myY = mine.getY();

                    if (myX != targetX && myY != targetY) {
                        int sel = GameUtility.intRand(1, 2);
                        if (sel == 1) {
                            if (targetX > myX) {
                                mine.incX(this);
                            } else {
                                mine.decX(this);
                            }
                        } else {
                            if (targetY > myY) {
                                mine.incY(this);
                            } else {
                                mine.decY(this);
                            }
                        }
                    } else {
                        if (myX == targetX && myY != targetY) {
                            if (targetY > myY) {
                                mine.incY(this);
                            } else {
                                mine.decY(this);
                            }
                        }
                        if (myX != targetX && myY == targetY) {
                            if (targetX > myX) {
                                mine.incX(this);
                            } else {
                                mine.decX(this);
                            }
                        }
                    }
                    moves--;
                }

                positions.put(mine.getCode(), mine);
                positions.remove(now.getPos());
                now.setPos(mine.getCode());

            }
            */
            cycleTurn();
        }
    }

    private void combat(Player cur){
        Map.printMap(cur,this);
        Creature[] others = positions.get(cur.getPos()).inVicinity(this);
        Creature[] all = new Creature[others.length+1];
        for(int i=0;i<=others.length;i++){
            if(i==0){
                all[i] = cur;
            }else {
                all[i] = others[i-1];
            }
        }

        Monster[] monsters = GameUtility.getMonsters(all);
        Player[] players = GameUtility.getPlayers(all);

        InitiativeUtility init = new InitiativeUtility(all);
        Integer[] indices = init.rollInitiative();

        if(monsters.length == 0){
            //PVP
            int prov = GameUtility.intPrompt(Main.uinp, "Provoke?", "1. Yes\n2. No\n>", 1, 2);
            if(prov == 1) {
                while(cur.getHP() > 0) {
                    for (int i = 0; i < all.length; i++) {
                        Player now = (Player) all[indices[i]];
                        if (now.disarmed == 0) {
                            if (now.equals(cur)) {
                                int sel, choice = GameUtility.intPrompt(Main.uinp, "It's your turn, what will you do?", "1. Attack\n2. Try To Disarm\n3. Move Away\n>", 1, 3);
                                String options = "";
                                Creature opp;

                                switch (choice) {
                                    case 1:
                                        if(others.length>1) {
                                            for (int z = 0; z < others.length; z++) {
                                                options = options.concat(String.format("\n%d. Attack %s\n", z + 1, others[z].getName()));
                                            }
                                            sel = GameUtility.intPrompt(Main.uinp, options, ">", 1, all.length - 1);
                                            opp = others[sel - 1];
                                        }else{
                                            opp = others[0];
                                        }
                                        cur.attack(opp);
                                        break;
                                    case 2:
                                        if(others.length>1) {
                                            for (int z = 0; z < others.length; z++) {
                                                options = options.concat(String.format("\n%d. Disarm %s\n", z + 1, others[z].getName()));
                                            }
                                            sel = GameUtility.intPrompt(Main.uinp, options, ">", 1, all.length - 1);
                                            opp = others[sel - 1];
                                        }else {
                                            opp = others[0];
                                        }
                                        if (cur.rollInit() + cur.getSTR() > opp.rollInit() + opp.getSTR()) {
                                            opp.disarm(dlength);
                                            System.out.println(cur.getName() + " has successfully disarmed " + opp.getName());
                                        } else {
                                            System.out.println(cur.getName() + " failed to disarm " + opp.getName());
                                        }

                                        break;
                                    case 3:
                                        moves = 5;
                                        return;
                                }
                            } else {
                                //if not current player
                                int choice = GameUtility.intRand(1, 2);
                                Creature opp = all[0];

                                if (choice == 1) {
                                    //attack opp
                                    now.attack(opp);
                                } else {
                                    //disarm opp
                                    if (now.rollInit() + now.getSTR() > opp.rollInit() + opp.getSTR()) {
                                        opp.disarm(dlength);
                                        System.out.println(now.getName() + " has successfully disarmed " + opp.getName());
                                    } else {
                                        System.out.println(now.getName() + " failed to disarm " + opp.getName());
                                    }
                                }
                            }
                        } else {
                            System.out.println(now.getName() + " is disarmed for " + now.disarmed + " more turns.\n");
                            now.disarmed--;
                        }
                    }
                }
            }else{
                return;
            }
        }else{
            //monsters only
            String message = "You encounter ";
            if (monsters.length > 1) {
                for (int i = 0; i < monsters.length; i++) {
                    if (i + 1 == monsters.length) {
                        message = message.concat("and " + monsters[i].toString() + "!");
                    } else {
                        message = message.concat(monsters[i].toString() + ", ");
                    }
                }
            }else{
                message = message.concat(monsters[0].toString());
            }
            System.out.println(message);

            int playHP,monsHP;
            do {
                for (int i = 0; i < all.length && all[indices[i]].getHP() > 0; i++) {
                    Creature temp = all[indices[i]];
                    if (temp.disarmed == 0) {
                        Monster curmon;
                        Player curplay;
                        if (temp instanceof Monster) {
                            //monster's turn
                            curmon = (Monster) temp;
                            Player opp = players[GameUtility.intRand(0, players.length - 1)];
                            int choice = GameUtility.intRand(1, 2);

                            if (choice == 1) {
                                //attack
                                curmon.attack(opp);
                            } else {
                                //disarm
                                if (curmon.rollInit() + curmon.getSTR() > opp.rollInit() + opp.getSTR()) {
                                    opp.disarm(dlength);
                                    System.out.println(curmon.getName() + " disarmed " + opp.getName());
                                } else {
                                    System.out.println(curmon.getName() + " failed to disarm " + opp.getName());
                                }
                            }
                        } else {
                            //player's turn
                            curplay = (Player) temp;
                            if (curplay == cur) {
                                //your turn
                                int sel, choice = GameUtility.intPrompt(Main.uinp, "It's your turn, what will you do?", "1. Attack\n2. Try To Disarm\n>", 1, 2);
                                String options = "";
                                Monster opp;

                                switch (choice) {
                                    case 1:
                                        if (monsters.length > 1) {
                                            for (int z = 0; z < monsters.length; z++) {
                                                options = options.concat(String.format("\n%d. Attack %s\n", z + 1, monsters[z].getName()));
                                            }
                                            sel = GameUtility.intPrompt(Main.uinp, options, ">", 1, monsters.length);
                                            opp = monsters[sel - 1];
                                        } else {
                                            opp = monsters[0];
                                        }
                                        cur.attack(opp);
                                        break;
                                    case 2:
                                        if (monsters.length > 1) {
                                            for (int z = 0; z < monsters.length; z++) {
                                                options = options.concat(String.format("\n%d. Disarm %s\n", z + 1, monsters[z].getName()));
                                            }
                                            sel = GameUtility.intPrompt(Main.uinp, options, ">", 1, monsters.length);
                                            opp = monsters[sel - 1];
                                        } else {
                                            opp = monsters[0];
                                        }
                                        if (cur.rollInit() + cur.getSTR() > opp.rollInit() + opp.getSTR()) {
                                            opp.disarm(dlength);
                                            System.out.println(cur.getName() + " has successfully disarmed " + opp.getName());
                                        } else {
                                            System.out.println(cur.getName() + " failed to disarm " + opp.getName());
                                        }
                                        break;
                                }
                            } else {
                                //other player's turn
                                int choice = GameUtility.intRand(1, 2);
                                int sel = GameUtility.intRand(0, monsters.length - 1);
                                Monster opp = monsters[sel];

                                if (choice == 1) {
                                    //attack opp
                                    curplay.attack(opp);
                                } else {
                                    //disarm opp
                                    if (curplay.rollInit() + curplay.getSTR() > opp.rollInit() + opp.getSTR()) {
                                        opp.disarm(dlength);
                                        System.out.println(curplay.getName() + " has successfully disarmed " + opp.getName());
                                    } else {
                                        System.out.println(curplay.getName() + " failed to disarm " + opp.getName());
                                    }
                                }
                            }
                        }
                    } else {
                        System.out.println(temp.getName() + " is disarmed for " + temp.disarmed + " more turn(s).\n");
                        temp.disarmed--;
                    }
                }

                //health update
                playHP=0;
                monsHP=0;
                for(Creature creature : all) {
                    if (creature.getHP() == 0) {
                        if (creature instanceof Player && ((Player) creature).equals(cur)) {
                            moves = 0;
                        }
                        if (all.length > 2) {
                            Creature[] temp_all = new Creature[all.length - 1];
                            for (int z = 0, a = 0; z < all.length; z++) {
                                if (!creature.equals(all[z])) {
                                    temp_all[a] = all[z];
                                    a++;
                                }
                            }
                            all = temp_all;
                            players = GameUtility.getPlayers(all);
                            monsters = GameUtility.getMonsters(all);

                            init = new InitiativeUtility(all);
                            indices = init.rollInitiative();
                        }
                        positions.remove(creature.getPos());
                    } else {
                        if (creature instanceof Player) {
                            playHP += creature.getHP();
                        } else {
                            monsHP += creature.getHP();
                        }
                    }
                }
            }while(playHP != 0 && monsHP != 0);
        }
    }
}