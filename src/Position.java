public class Position {
    private int x,y,minx,miny,maxx,maxy;
    private Position limits;
    private Creature identifier;
    private boolean limit;

    public Position(int x, int y, Position limits, Creature identifier){
        this.x = x;
        this.y = y;
        this.limits = limits;
        this.identifier = identifier;
        limit = false;
    }

    public Position(int minx, int miny, int maxx, int maxy){
        this.minx = minx;
        this.miny = miny;
        this.maxx = maxx;
        this.maxy = maxy;
        limit = true;
    }

    public int getX(){ return x; }

    public int getY(){ return y; }

    public int getMinx(){
        return minx;
    }

    public int getMiny(){
        return miny;
    }

    public int getMaxx(){
        return maxx;
    }

    public int getMaxy(){
        return maxy;
    }

    public int incX(Game game){
        if(!limit && (x+1) <= limits.getMaxx()){
            Creature possible = isRight(game);
            if(possible == null) {
                x += 1;
                return 0;
            }else{
                return 2;
            }
        }
        return 1;
    }

    public int decX(Game game){
        if(!limit && (x-1) >= limits.getMinx()){
            Creature possible = isLeft(game);
            if(possible == null) {
                x -= 1;
                return 0;
            }else{
                return 2;
            }
        }
        return 1;
    }

    public int incY(Game game){
        if(!limit && (y+1) <= limits.getMaxy()){
            Creature possible = isDown(game);
            if(possible == null) {
                y += 1;
                return 0;
            }else{
                return 2;
            }
        }
        return 1;
    }

    public int decY(Game game){
        if(!limit && (y-1) >= limits.getMiny()){
            Creature possible = isUp(game);
            if(possible == null) {
                y -= 1;
                return 0;
            }else{
                return 2;
            }
        }
        return 1;
    }

    public Creature isUp(Game game){
        Position up = game.positions.get(GameUtility.makePosString(x,y-1).hashCode());
        if(up!=null){
            return up.getCreature();
        }
        return null;
    }

    public Creature isDown(Game game){
        Position down = game.positions.get(GameUtility.makePosString(x,y+1).hashCode());
        if(down!=null){
            return down.getCreature();
        }
        return null;
    }

    public Creature isRight(Game game){
        Position right = game.positions.get(GameUtility.makePosString(x+1,y).hashCode());
        if(right!=null){
            return right.getCreature();
        }
        return null;
    }

    public Creature isLeft(Game game){
        Position left = game.positions.get(GameUtility.makePosString(x-1,y).hashCode());
        if(left!=null){
            return left.getCreature();
        }
        return null;
    }

    public Creature[] inVicinity(Game game){
        Creature[] creatures = new Creature[0];
        Creature up = isUp(game);
        Creature down = isDown(game);
        Creature left = isLeft(game);
        Creature right = isRight(game);

        if(up!=null){
            Creature[] temp = new Creature[creatures.length+1];
            for(int i=0; i<creatures.length;i++){
                temp[i] = creatures[i];
            }
            temp[creatures.length] = up;
            creatures = temp;
        }
        if(down!=null){
            Creature[] temp = new Creature[creatures.length+1];
            for(int i=0; i<creatures.length;i++){
                temp[i] = creatures[i];
            }
            temp[creatures.length] = down;
            creatures = temp;
        }
        if(left!=null){
            Creature[] temp = new Creature[creatures.length+1];
            for(int i=0; i<creatures.length;i++){
                temp[i] = creatures[i];
            }
            temp[creatures.length] = left;
            creatures = temp;
        }
        if(right!=null){
            Creature[] temp = new Creature[creatures.length+1];
            for(int i=0; i<creatures.length;i++){
                temp[i] = creatures[i];
            }
            temp[creatures.length] = right;
            creatures = temp;
        }
        return creatures;
    }

    public Creature getCreature(){
        return identifier;
    }

    public int getCode(){
        return this.toString().hashCode();
    }

    @Override
    public String toString(){
        if(!limit){
            return GameUtility.makePosString(x,y);
        }else{
            return String.format("limit: ((%d,%d),(%d,%d))",minx,maxx,miny,maxy);
        }
    }
}
