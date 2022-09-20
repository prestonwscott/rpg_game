import java.text.ParseException;

public class Weapon {

    private String name = "";
    private String DiceType = "";
    private int Bonus = 0;

    public Weapon(String name, String DiceType, int Bonus){
        this.name = name;
        this.DiceType = DiceType;
        this.Bonus = Bonus;
    }

    public static Weapon loadFromCsv(String line) throws CsvReadException, ParseException {
        String[] temp = line.split("[,]");
        if(temp.length==3){
            try {
                int bonus = Integer.parseInt(temp[2]);
                GameUtility.validateName(temp[0]);

                return new Weapon(temp[0], temp[1], bonus);
            } catch (NumberFormatException e) {
                System.out.println(e.getMessage());
            }
        }
        throw new CsvReadException(line);
    }

    public int rollDamage(){
        return GameUtility.rollDice(DiceType) + Bonus;
    }

    public String getDiceType(){
        return DiceType;
    }

    public int getBonus(){
        return Bonus;
    }

    public String getName(){
        return name;
    }

    @Override
    public String toString(){
        return name + " (" + DiceType + "+" + Bonus + ")";
    }
}
