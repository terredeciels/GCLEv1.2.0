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

    /**
     * attention: -couleur
     */
    private void ajouterRoques() {
        List<GCoups> coupsAttaque = new PGenerateur().pseudoCoups(gp, -couleur);
        int[] e = gp.etats;
        for (Integer type = 0; type < 4; type++) {
            int _c0 = o_o[type][0];
            int _c1 = o_o[type][1];
            int _c2 = o_o[type][2];
            int _c3 = o_o[type][3];
            int e_c4 = type == 1 || type == 3 ? e[o_o[type][4]] : VIDE;

            if (gp.roques[type]) {
                if ((e[_c0] == couleur * ROI && e[_c2] == couleur * TOUR
                        && e[_c3] == VIDE && e[_c1] == VIDE && e_c4 == VIDE)
                        && !(attaqueRoque(_c0, _c3, _c1, coupsAttaque))) {
                    pseudoCoups.add(new GCoups(ROI, _c0, _c1, _c2, _c3, 0, Roque));
                }
            }
        }
    }

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
            pionEstOuest(caseEP, est);
            pionEstOuest(caseEP, ouest);
        }
    }

    private void pionEstOuest(int caseEP, int estouest) {
        int caseEstOuest = caseEP + couleur * nord + estouest;
        if (pionDeCouleur(caseEstOuest, couleur)) {
            pseudoCoups.add(new GCoups(couleur * PION, caseEstOuest, caseEP, 0, EnPassant));
        }
    }
}
