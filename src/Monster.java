import java.text.ParseException;
import java.time.LocalDate;

public class Monster extends Creature{
    int cr;
    MonsterType type;

    private enum MonsterType{
        HUMANOID,
        FIEND,
        DRAGON;
    }

    public Monster(String name,MonsterType type,int HP, int AC, int STR, int DEX, int CON){
        super.name = name;
        super.HP = HP;
        super.AC = AC;
        super.STR = STR;
        super.DEX = DEX;
        super.CON = CON;
        this.cr = (int)(Math.random() * 5);
        super.created = LocalDate.now();
        this.type = type;
    }

    public static Monster loadFromCsv(String line) throws CsvReadException, ParseException {
        String[] temp = line.split("[,]");
        if(temp.length == 7) {
            try {
                int[] buff = new int[5];
                for (int i = 2, z = 0; i < 7; i++, z++) {
                    buff[z] = Integer.parseInt(temp[i]);
                }

                return new Monster(temp[0], MonsterType.valueOf(temp[1]), buff[0], buff[1], buff[2], buff[3], buff[4]);
            } catch (NumberFormatException e) {
                System.out.println(e.getMessage());
            }
        }
        throw new CsvReadException(line);
    }

    public void attack(Creature other){
        int temp = rollHit();
        System.out.printf("%s attacks %s (%d to hit)...%s\n",name,other.getName(),other.getAC(),temp>=other.getAC()?"HITS!":"MISSES!");
        if(temp>=other.getAC()){
            other.takeDamage(Math.max(GameUtility.rollDice("d6")+super.STR,1));
        }else{ System.out.printf("%s couldn't breach the armor of %s\n",name,other.getName()); }
    }

    private int rollHit(){
        return GameUtility.rollDice("d20")+Math.min((DEX-5),10);
    }

    @Override

    public String toString(){
        return type + " " + super.getName();
    }
}