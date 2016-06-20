package position;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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

    private GPosition fPositionSimul(GCoups coups, int couleur) {
        GPosition positionSimul = new GPosition();
        System.arraycopy(etats, 0, positionSimul.etats, 0, NB_CELLULES);
        int caseO = coups.getCaseO();
        int caseX = coups.getCaseX();
        if (coups.getTypeDeCoups() == Deplacement
                || coups.getTypeDeCoups() == Prise) {
            positionSimul.etats[caseX] = positionSimul.etats[caseO];
            positionSimul.etats[caseO] = VIDE;
        } else if (coups.getTypeDeCoups() == EnPassant) {
            // caseX == caseEP
            positionSimul.etats[caseX] = positionSimul.etats[caseO];
            positionSimul.etats[caseO] = VIDE;
            if (couleur == BLANC) {
                positionSimul.etats[caseX + sud] = VIDE;
            } else if (couleur == NOIR) {
                positionSimul.etats[caseX + nord] = VIDE;
            }
        } else if (coups.getTypeDeCoups() == Promotion) {

            positionSimul.etats[caseX] = coups.getPiecePromotion();
            positionSimul.etats[caseO] = VIDE;

        }
        return positionSimul;
    }

    private int fCaseRoi(GPosition position, int couleur) {
        int[] pEtats = position.etats;
        int caseRoi = OUT;
        int etatO;
        int typeO;
        for (int caseO : CASES117) {
            etatO = pEtats[caseO];
            typeO = Math.abs(etatO);
            if ((typeO == ROI) && (etatO * couleur > 0)) {
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

        boolean possible;
        final GPosition pgPosition = gp;
        final int[] pgEtats = pgPosition.etats;
        if (couleur == BLANC) {
            if (pgPosition.roques[0]) {
                possible = ((pgEtats[f1] == VIDE)
                        && (pgEtats[h1] == -TOUR)
                        && (pgEtats[e1] == -ROI)
                        && (pgEtats[g1] == VIDE));
                possible &= !(attaqueRoque(e1, f1, g1, coupsAttaque));

                if (possible) {
                    pseudoCoups.add(new GCoups(ROI, e1, g1, h1, f1, 0, Roque));
                }
            }
            if (pgPosition.roques[1]) {
                possible = ((pgEtats[d1] == VIDE)
                        && (pgEtats[a1] == -TOUR)
                        && (pgEtats[e1] == -ROI)
                        && (pgEtats[c1] == VIDE)
                        && (pgEtats[b1] == VIDE));
                possible &= !(attaqueRoque(e1, d1, c1, coupsAttaque));

                if (possible) {
                    pseudoCoups.add(new GCoups(ROI, e1, c1, a1, d1, 0, Roque));
                }
            }
        } else {
            if (pgPosition.roques[2]) {
                possible = ((pgEtats[f8] == VIDE)
                        && (pgEtats[h8] == TOUR)
                        && (pgEtats[e8] == ROI)
                        && (pgEtats[g8] == VIDE));
                possible &= !(attaqueRoque(e8, f8, g8, coupsAttaque));

                if (possible) {
                    pseudoCoups.add(new GCoups(ROI, e8, g8, h8, f8, 0, Roque));
                }
            }
            if (pgPosition.roques[3]) {
                possible = ((pgEtats[d8] == VIDE)
                        && (pgEtats[a8] == TOUR)
                        && (pgEtats[e8] == ROI)
                        && (pgEtats[c8] == VIDE)
                        && (pgEtats[b8] == VIDE));
                possible &= !(attaqueRoque(e8, d8, c8, coupsAttaque));

                if (possible) {
                    pseudoCoups.add(new GCoups(ROI, e8, c8, a8, d8, 0, Roque));
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
