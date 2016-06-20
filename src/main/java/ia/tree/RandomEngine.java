package ia.tree;

import ia.IIA;
import java.util.List;
import java.util.Random;
import position.GCoups;
import position.GPosition;

public class RandomEngine implements IIA {

    static final Random RANDOMIZER = new Random();
    private final GPosition gp;

    public RandomEngine(GPosition gp) {
        this.gp = gp;
//        setMoveSorter(new StaticMoveSorter());
    }

    protected GCoups searchMoveFor(final GPosition pEtat, final List<GCoups> pCoups) {
        final int l = pCoups.size();
        assert (l != 0);
//        addHalfmove(l);
        final GCoups res = pCoups.get(RANDOMIZER.nextInt(l));
        return res;
    }

    @Override
    public GCoups search() {
        return searchMoveFor(gp, gp.getCoupsValides());
    }
}
