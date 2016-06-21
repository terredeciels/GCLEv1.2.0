package position;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import static position.ICodage.Roque.*;
import static position.ICodage.TYPE_DE_COUPS.*;

public class Generateur extends PGenerateur {

    public Generateur(GPosition gp, int couleur) {
        super(gp, couleur);
        this.couleur = gp.trait;
    }

    public List<GCoups> getCoupsValides() {
        pseudoCoups(gp, couleur);
        ajouterRoques();
        ajouterCoupsEP();
        pseudoCoups.removeAll(coupsEnEchec());
        return pseudoCoups;
    }

    private List<GCoups> coupsEnEchec() {
        List<GCoups> aRetirer = new ArrayList<>();
        int caseRoiCouleur;
        Iterator<GCoups> it = pseudoCoups.iterator();
        while (it.hasNext()) {
            GCoups coups = it.next();
            GPosition positionSimul = fPositionSimul(coups, couleur);
            caseRoiCouleur = fCaseRoi(positionSimul, couleur);

            List<GCoups> pseudoCoupsPosSimul = new PGenerateur().pseudoCoups(positionSimul, -couleur);

            estEnEchec = fEstEnEchec(pseudoCoupsPosSimul, caseRoiCouleur);
            if (estEnEchec) {
                aRetirer.add(coups);
            }
        }
        return aRetirer;
    }

    public void e(GPosition p, int co, int cx) {
        p.etats[co] = p.etats[cx];
    }

    public void e(GPosition p, int co) {
        p.etats[co] = VIDE;
    }

    private GPosition fPositionSimul(GCoups m, int couleur) {
        GPosition p = new GPosition();
        System.arraycopy(etats, 0, p.etats, 0, NB_CELLULES);
        int O = m.caseO;
        int X = m.caseX;
        TYPE_DE_COUPS t = m.getTypeDeCoups();
        int piecePromotion = m.getPiecePromotion();
        switch (t) {
            case Deplacement:
            case Prise:
            case EnPassant:
                e(p, X, O);
                e(p, O);
                if (t == EnPassant) {
                    e(p, X + nord * couleur);
                }
                break;
            case Promotion:
                p.etats[X] = piecePromotion;
                e(p, O);
        }
        return p;
    }

    private int fCaseRoi(GPosition position, int couleur) {
//        int[] pEtats = position.etats;
        int caseRoi = OUT;
        int etatO;
        int typeO;

        for (int caseO : CASES117) {
            etatO = position.etats[caseO];
            typeO = Math.abs(etatO);
            if (typeO == ROI && etatO * couleur > 0) {
                caseRoi = caseO;
                break;
            }
        }
        return caseRoi;
    }

    private boolean fEstEnEchec(List<GCoups> pseudoCoupsPosSimul, int caseRoi) {
        boolean isCheck = false;
        Iterator<GCoups> it = pseudoCoupsPosSimul.iterator();
        while (it.hasNext()) {
            if (it.next().getCaseX() == caseRoi) {
                isCheck = true;
                break;
            }
        }
        return isCheck;
    }

    public void ajouterRoques() {
        List<GCoups> coupsAttaque = new PGenerateur().pseudoCoups(gp, -couleur);
        for (Integer rq = 0; rq < 4; rq++) {
            possibleRoque(couleur, rq, gp, gp.etats, coupsAttaque);
        }
    }

    public void possibleRoque(int color, int t_roque, GPosition gp, int[] pgEtats, List<GCoups> coupsAttaque) {
        boolean possible;
        Integer _c0 = roques_cases[t_roque][0];
        Integer _c1 = roques_cases[t_roque][1];
        Integer _c2 = roques_cases[t_roque][2];
        Integer _c3 = roques_cases[t_roque][3];
        Integer pgEtats_c4 = t_roque == 1 || t_roque == 3 ? pgEtats[roques_cases[t_roque][4]] : VIDE;
        if (gp.roques[t_roque]) {
            possible = ((pgEtats[_c0] == color * ROI)
                    && (pgEtats[_c2] == color * TOUR)
                    && (pgEtats[_c3] == VIDE)
                    && (pgEtats[_c1] == VIDE)
                    && (pgEtats_c4 == VIDE));
            possible &= !(attaqueRoque(_c0, _c3, _c1, coupsAttaque));
            if (possible) {
                pseudoCoups.add(new GCoups(ROI, _c0, _c1, _c2, _c3, 0, Roque));
//                addMove(fCoups, _c0, _c1, _c2, _c3);
            }
        }
    }

    /**
     * attention: -couleur
     */
//    private void ajouterRoques() {
//
//        List<GCoups> coupsAttaque = new PGenerateur().pseudoCoups(gp, -couleur);
//
//        boolean possible;
//        final GPosition pgPosition = gp;
//        final int[] pgEtats = pgPosition.etats;
//        if (couleur == BLANC) {
//            if (pgPosition.roques[0]) {
//                possible = ((pgEtats[f1] == VIDE)
//                        && (pgEtats[h1] == -TOUR)
//                        && (pgEtats[e1] == -ROI)
//                        && (pgEtats[g1] == VIDE));
//                possible &= !(attaqueRoque(e1, f1, g1, coupsAttaque));
//
//                if (possible) {
//                    pseudoCoups.add(new GCoups(ROI, e1, g1, h1, f1, 0, Roque));
//                }
//            }
//            if (pgPosition.roques[1]) {
//                possible = ((pgEtats[d1] == VIDE)
//                        && (pgEtats[a1] == -TOUR)
//                        && (pgEtats[e1] == -ROI)
//                        && (pgEtats[c1] == VIDE)
//                        && (pgEtats[b1] == VIDE));
//                possible &= !(attaqueRoque(e1, d1, c1, coupsAttaque));
//
//                if (possible) {
//                    pseudoCoups.add(new GCoups(ROI, e1, c1, a1, d1, 0, Roque));
//                }
//            }
//        } else {
//            if (pgPosition.roques[2]) {
//                possible = ((pgEtats[f8] == VIDE)
//                        && (pgEtats[h8] == TOUR)
//                        && (pgEtats[e8] == ROI)
//                        && (pgEtats[g8] == VIDE));
//                possible &= !(attaqueRoque(e8, f8, g8, coupsAttaque));
//
//                if (possible) {
//                    pseudoCoups.add(new GCoups(ROI, e8, g8, h8, f8, 0, Roque));
//                }
//            }
//            if (pgPosition.roques[3]) {
//                possible = ((pgEtats[d8] == VIDE)
//                        && (pgEtats[a8] == TOUR)
//                        && (pgEtats[e8] == ROI)
//                        && (pgEtats[c8] == VIDE)
//                        && (pgEtats[b8] == VIDE));
//                possible &= !(attaqueRoque(e8, d8, c8, coupsAttaque));
//
//                if (possible) {
//                    pseudoCoups.add(new GCoups(ROI, e8, c8, a8, d8, 0, Roque));
//                }
//            }
//        }
//    }
    private boolean attaqueRoque(int E1ouE8, int F1ouF8, int G1ouG8, List<GCoups> coupsAttaque) {
        boolean attaque = false;
        int caseX;
        for (GCoups coups : coupsAttaque) {
            caseX = coups.getCaseX();
            if ((caseX == E1ouE8) || (caseX == F1ouF8) || (caseX == G1ouG8)) {
                attaque = true;
                break;
            }
        }
        return attaque;
    }

    private void ajouterCoupsEP() {
        final int caseEP = gp.caseEP;
        // prise en passant (avant recherche d'échecs)
        if (caseEP != PAS_DE_CASE) {
            if (couleur == BLANC) {
                int caseX = caseEP + sudest;
                if (pionBlanc(caseX)) {
                    pseudoCoups.add(new GCoups(couleur * PION, caseX, caseEP, 0, EnPassant));
                }
                caseX = caseEP + sudouest;
                if (pionBlanc(caseX)) {
                    pseudoCoups.add(new GCoups(couleur * PION, caseX, caseEP, 0, EnPassant));
                }
            } else {
                int caseX = caseEP + nordest;
                if (pionNoir(caseX)) {
                    pseudoCoups.add(new GCoups(couleur * PION, caseX, caseEP, 0, EnPassant));
                }
                caseX = caseEP + nordouest;
                if (pionNoir(caseX)) {
                    pseudoCoups.add(new GCoups(couleur * PION, caseX, caseEP, 0, EnPassant));
                }
            }
        }
    }

}
