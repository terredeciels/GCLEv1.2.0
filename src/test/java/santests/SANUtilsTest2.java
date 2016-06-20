package santests;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;
import org.junit.Test;

import position.FenToGPosition;
import position.GCoups;
import position.GPosition;
import position.ICodage;
import static position.ICodage.TYPE_DE_COUPS.Deplacement;

import sanutils.SANException;
import static position.ICodage.TYPE_DE_COUPS.Prise;
import static position.ICodage.TYPE_DE_COUPS.Promotion;
import position.UndoGCoups;
import static sanutils.SANUtils.toMove;
import static sanutils.SANUtils.toSAN;

public final class SANUtilsTest2 implements ICodage {

    public SANUtilsTest2() {

    }

    @Test
    public void testToMove() {
//        GPosition gp = FenToGPosition.toGPosition(ICodage.FEN_INITIALE);
//        String f = "8/2P2p2/5B1k/7N/6P1/p7/P3KP1P/8 w - - 1 43";
//        GPosition gp = FenToGPosition.toGPosition(f);
//
//        try {
////
//            UndoGCoups ug = new UndoGCoups();
//            GCoups mvt = toMove(gp, "c8Q");
//            GCoups gCoups = new GCoups(BLANC * PION, c7, c8, 0, Promotion, BLANC * DAME);
//            assertEquals(gCoups, mvt);
//            gp.exec(mvt, ug);
//
//        } catch (final SANException e) {
//            fail(e.toString());
//        }
        String f = "r4rk1/2p1bppp/p1q2n2/7b/1p6/1BPP1N1P/PP3PP1/R1BQR1K1 w - - 0 16";
        GPosition gp = FenToGPosition.toGPosition(f);
        try {
//            GCoups mvt = toMove(etat, "e3");
//            GCoups jcoups = new GCoups(BLANC * PION, e2, e3, 0, Deplacement);
//            assertEquals(jcoups, mvt);
//
//            UndoGCoups ug = new UndoGCoups();
//            etat.exec(mvt, ug);
//
//            mvt = toMove(etat, "Na6");
//            assertEquals(new GCoups(NOIR * CAVALIER, b8, a6, 0, Deplacement), mvt);
//            etat.exec(mvt, ug);
            gp.getCoupsValides();
            System.out.println(gp);
            GCoups mvt = toMove(gp, "Rxe7");
            GCoups gCoups = new GCoups(BLANC * TOUR, e1, e7, NOIR * FOU, Prise);
            assertEquals(gCoups, mvt);
//            gp.exec(mvt, ug);

        } catch (final SANException e) {
            fail(e.toString());
        }
    }

    @Test
    public void testToSAN() {

        GPosition gp = FenToGPosition.toGPosition("r1bq1rk1/pppp2pN/2n2n2/4pp2/P7/RP6/2PPPPPP/2BQKB1R b K - 0 7");
        GCoups mvt = new GCoups(NOIR * ROI, g8, h7, BLANC * CAVALIER, Prise);
        assertEquals("Kxh7", toSAN(gp, mvt));

    }
}
