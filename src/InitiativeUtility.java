import java.util.Arrays;

public class InitiativeUtility {
    private Creature[] creatures;
    private Integer[] indices;

    public InitiativeUtility(Creature[] creatures) {
        this.creatures = creatures;
    }

    public Integer[] rollInitiative() {
        Integer[] rolls = new Integer[creatures.length];
        for (int i = 0; i < creatures.length; i++) {
            rolls[i] = creatures[i].rollInit();
        }

        ArrayIndexComparator c = new ArrayIndexComparator(rolls);
        this.indices = c.createIndexArray();
        Arrays.sort(this.indices, c);

        return indices;
    }
}