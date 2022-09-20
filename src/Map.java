import java.util.HashMap;

public abstract class Map {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_BLUE = "\u001B[34m";

    public static void printMap(Player cur, Game game){
        char corn = '*';
        char vert = '|';
        char horz = '―';
        String focus = ANSI_BLUE+"P"+ANSI_RESET;
        String player = ANSI_RED+"P"+ANSI_RESET;
        String monster = ANSI_RED+"M"+ANSI_RESET;

        int x = game.limits.getMinx(),y = game.limits.getMiny();

        System.out.printf("\n%c",corn);
        for(int i=game.limits.getMinx();i<=game.limits.getMaxx()*2;i++){
            if(i%3==0) {
                System.out.print(horz);
            }else{
                System.out.print(" ");
            }
        }
        System.out.printf("%c\n",corn);

        for(int z=game.limits.getMiny();z<=game.limits.getMaxy();z++) {
            System.out.printf("%c", vert);
            for (int i = game.limits.getMinx(); i <= game.limits.getMaxx()*2; i++) {
                if(i%2==0) {
                    Position curPos = game.positions.get(GameUtility.makePosString(x, y).hashCode());
                    if (curPos != null) {
                        if (curPos.getCreature().toString().equals(cur.toString())) {
                            System.out.print(focus);
                        } else if (curPos.getCreature() instanceof Player) {
                            System.out.print(player);
                        } else {
                            System.out.print(monster);
                        }
                    } else {
                        System.out.print(" ");
                    }
                    x++;
                }else{
                    System.out.print(" ");
                }
            }
            System.out.printf("%c\n", vert);
            x=0;
            y++;
        }
        System.out.printf("%c",corn);
        for(int i=game.limits.getMinx();i<=game.limits.getMaxx()*2;i++){
            if(i%3==0) {
                System.out.print(horz);
            }else{
                System.out.print(" ");
            }
        }
        System.out.printf("%c\n",corn);
    }
}