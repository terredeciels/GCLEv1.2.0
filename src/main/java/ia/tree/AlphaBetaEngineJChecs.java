package ia.tree;

import ia.IEval;
import ia.IIA;
import static ia.IIA.MAT_VALUE;
import java.util.List;
import position.GCoups;
import position.GPosition;
import position.UndoGCoups;

public class AlphaBetaEngineJChecs implements IIA {

    private final int depth;
    private final IEval f_eval;
    private final GPosition gp;

    public AlphaBetaEngineJChecs(GPosition gp, IEval f_eval, int depth) {
        this.f_eval = f_eval;
        this.gp = gp;
        this.depth = depth;
    }

    private int alphabeta(final GPosition gp, final int pProfondeur, final int pAlpha, final int pBeta) {

        final int trait = gp.getTrait();
        if (pProfondeur == 0) {
            return evaluate(gp, trait);
        }

        final List<GCoups> coups = getValidMoves(gp, trait);

        final int l = coups.size();
        if (l == 0) {
            return evaluate(gp, trait);
        }

        int res = MAT_VALUE - 1;
        int alpha = pAlpha;

        for (final GCoups mvt : coups) {
            UndoGCoups ug = new UndoGCoups();
            gp.exec(mvt, ug);
            final int note = -alphabeta(gp, pProfondeur - 1, -pBeta, -alpha);
            gp.unexec(ug);

            if (note > res) {
                res = note;
                if (res > alpha) {
                    alpha = res;
                    if (alpha > pBeta) {
                        return res;
                    }
                }
            }
        }

        return res;
    }

    @Override
    public GCoups search() {
        return searchMoveFor(gp, gp.getCoupsValides());
    }

    public GCoups searchMoveFor(final GPosition gp, final List<GCoups> pCoups) {

        final int l = pCoups.size();

        GCoups res = pCoups.get(0);
        int alpha = MAT_VALUE - 1;
        for (final GCoups mvt : pCoups) {

            UndoGCoups ug = new UndoGCoups();
            gp.exec(mvt, ug);
            final int note = -alphabeta(gp, depth - 1, MAT_VALUE, -alpha);
            gp.unexec(ug);
            if ((note > alpha)) {
                alpha = note;
                res = mvt;
            }
        }
        return res;
    }

    private int evaluate(GPosition gp, int trait) {
        return f_eval.evaluate(gp, trait);
    }

    private List<GCoups> getValidMoves(GPosition gp, int trait) {
        return gp.getCoupsValides(trait);
    }

}
