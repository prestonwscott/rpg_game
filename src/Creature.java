import java.time.LocalDate;

public abstract class Creature implements Comparable<Creature>{
    String name;
    LocalDate created;
    int HP,AC,STR,DEX,CON,pos,disarmed=0;

    public void takeDamage(int damage){
        if(damage>HP){
            System.out.printf("%s takes %d points of damage.\n",name,HP);
            HP = 0;
        }else {
            HP -= damage;
            System.out.printf("%s takes %d points of damage.\n", name, damage);
        }

    }

    public void genPos(Game game){
        int x = game.limits.getMaxx(), y = game.limits.getMaxy();
        while(game.positions.containsKey(String.format("(%d,%d)",x,y).hashCode())){
            x = GameUtility.intRand(game.limits.getMinx(),game.limits.getMaxx());
            y = GameUtility.intRand(game.limits.getMiny(),game.limits.getMaxy());
        }
        pos = GameUtility.makePosString(x,y).hashCode();
        game.positions.put(pos,new Position(x,y,game.limits,this));

    }

    public abstract void attack(Creature o);

    public String getName() {
        return name;
    }

    public int rollInit(){ return GameUtility.rollDice("d20") + DEX;}

    public int getPos(){ return pos; }

    public int getHP() {
        return HP;
    }

    public int getAC() {
        return AC;
    }

    public int getSTR() {
        return STR;
    }

    public int getDEX() {
        return DEX;
    }

    public int getCON() {
        return CON;
    }

    public void disarm(int dur){
        disarmed = dur;
    }

    public void setPos(int pos) { this.pos = pos; }

    public void setHP(int HP) { this.HP = HP; }

    @Override
    public boolean equals(Object o){
        return o!=null
                && getClass() == o.getClass()
                && name.equals(((Creature)o).getName());
    }

    public int compareTo(Creature o){
        return HP>o.getHP()?0:1;
    }
}