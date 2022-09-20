import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    static Scanner uinp = new Scanner(System.in);
    public static ArrayList<Weapon> weapons = new ArrayList<>();
    public static ArrayList<Creature> creatures = new ArrayList();

    public static void main(String[] args) {
        GameUtility.loadWeapons();

        mainMenu();
        uinp.close();
    }

    private static void mainMenu() {
        int choice;
        choice = GameUtility.intPrompt(uinp,"1. Start Game\n2. Create Character\n3. Load Character\n4. Save Character\n5. Quit",">",1,5);

        switch (choice) {
            case 1:
                //change map size here in argument
                Game game = new Game(0,25,0,25);

                GameUtility.resetHealth();
                game.play();
                GameUtility.clearMonsters();
                mainMenu();
                break;
            case 2:
                GameUtility.createCharacter(uinp);
                mainMenu();
                break;
            case 3:
                GameUtility.loadPlayers(uinp);
                mainMenu();
                break;
            case 4:
                GameUtility.savePlayers(uinp);
                mainMenu();
                break;
        }
    }
}