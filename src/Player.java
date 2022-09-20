import java.text.ParseException;
import java.time.LocalDate;

public class Player extends Creature{
    private final Weapon weapon;

    public Player(String name, int AC, int HP, int STR, int DEX, int CON, Weapon weapon){
        super.name = name;
        super.AC = AC;
        super.HP = HP;
        super.STR = STR;
        super.DEX = DEX;
        super.CON = CON;
        this.weapon = weapon;
        super.created = LocalDate.now();
    }

    public static Player loadFromCsv(String line) throws CsvReadException, ParseException {
        String[] temp = line.split("[,]");
        if(temp.length == 9) {
            try {
                int[] buff = new int[6];
                for (int i = 1, z = 0; i < 6; i++, z++) {
                    buff[z] = Integer.parseInt(temp[i]);
                }
                buff[5] = Integer.parseInt(temp[8]);

                GameUtility.validateName(temp[0]);

                Weapon w = new Weapon(temp[6], temp[7], buff[5]);
                return new Player(temp[0], buff[0], buff[1], buff[2], buff[3], buff[4], w);
            } catch (NumberFormatException e) {
                System.out.println(e.getMessage());
            }
        }
        throw new CsvReadException(line);
    }

    public void attack(Creature other){
        int temp = rollHit();
        System.out.printf("%s attacks %s with %s (%d to hit)...%s\n",name,other.getName(),weapon.getName(),other.getAC(),temp>=other.getAC()?"HITS!":"MISSES!");
        if(temp >= other.AC){
            other.takeDamage(Math.abs(weapon.rollDamage() + super.STR));
        }else{ System.out.printf("%s couldn't breach the armor of %s\n\n",name,other.getName()); }
    }

    public int rollHit(){
        return GameUtility.rollDice("d20")+Math.min((DEX-5),10)+weapon.getBonus();
    }

    public Weapon getWeapon(){
        return weapon;
    }

    @Override
    public String toString(){
        return String.format("%s%9s\t%s%5d\t%s%5d\t%s%2d\t%s%2d\t%s%2d\t%s%10s","Name:",super.name,"HP:",super.HP,"AC:",AC,"STR:",super.STR,"DEX:",super.DEX,"CON:",super.CON,"Weapon:",weapon.getName());
    }
}