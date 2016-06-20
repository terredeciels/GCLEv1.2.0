package position;

import static java.lang.System.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.chesspresso.position.Position;
import static position.ICodage.*;
import static position.ICodage.Roque.*;
import xboard.XBoardAdapter;

public class GPosition implements ICodage {

    int caseEP;
    Roque R;
    boolean[] roques;

    int trait;

    int[] etats;

    List<GCoups> coupsvalides;
    List<String> coupsvalides_lan;
    List<String> cp_coupsvalides_lan;
    boolean estEnEchec;
    String fen;
    Position position;
    public int _halfmoveCount;
    public int _fullmoveNumber;

    public GPosition() {
        etats = new int[NB_CELLULES];
        R = new Roque();
        roques = Roque.roques;

    }

    public boolean exec(GCoups gcoups, UndoGCoups ug) {
        arraycopy(etats, 0, ug.etats, 0, NB_CELLULES);
        ug.setKQkq(roques);
        ug.caseEP = caseEP;
        int caseO = gcoups.getCaseO();
        int caseX = gcoups.getCaseX();
        caseEP = -1;
        Roque.trait = trait;
        if (Math.abs(gcoups.getPiece()) == PION && Math.abs(caseX - caseO) == 24) {
            // avance de 2 cases
            caseEP = trait == NOIR ? caseX + 12 : caseX - 12;
        }
        switch (gcoups.getTypeDeCoups()) {
            case Deplacement:
                etats[caseX] = etats[caseO];
                etats[caseO] = VIDE;
                valideDroitRoque(gcoups);
                break;
            case Prise:
                etats[caseX] = etats[caseO];
                etats[caseO] = VIDE;
                //piece prise = tour
                valideDroitRoque(gcoups);
                break;
            case EnPassant:
                // caseX == caseEP
                etats[caseX] = etats[caseO];
                etats[caseO] = VIDE;
                if (trait == BLANC) {
                    etats[caseX + sud] = VIDE;
                } else if (trait == NOIR) {
                    etats[caseX + nord] = VIDE;
                }
                break;
            case Promotion:
                etats[caseX] = gcoups.getPiecePromotion();
                etats[caseO] = VIDE;
                break;
            case Roque:
                etats[caseX] = etats[caseO]; //ROI
                etats[caseO] = VIDE;
                etats[gcoups.getCaseXTour()] = etats[gcoups.getCaseOTour()]; //TOUR
                etats[gcoups.getCaseOTour()] = VIDE;
                unsetRoque();
                break;
            default:
                break;
        }
        trait = -trait;
        return true;
    }

    public void unexec(UndoGCoups ug) {
        arraycopy(ug.etats, 0, etats, 0, NB_CELLULES);
        arraycopy(ug.roques, 0, roques, 0, 4);
        caseEP = ug.caseEP;
        trait = -trait;
    }

    protected void valideDroitRoque(GCoups gcoups) {
        int caseO = gcoups.getCaseO();

        switch (gcoups.getPiece()) {
            //piece deplacee = tour ou roi
            case ROI:
                unsetRoque();
                break;
            case TOUR:
                if (caseO == caseTourH()) {
                    unsetK(trait);
                }
                if (caseO == caseTourA()) {
                    unsetQ(trait);
                }
                break;
            default:
                break;
        }
        // roi et tour à leurs places
        if (etats[caseTourA()] != trait * TOUR || etats[caseRoi()] != trait * ROI) {
            unsetQ(trait);
        }

        if (etats[caseTourH()] != trait * TOUR || etats[caseRoi()] != trait * ROI) {
            unsetK(trait);
        }
    }

    public boolean isInCheck(final int pCouleur) {
        getCoupsValides(pCouleur);
        return estEnEchec();
    }

    public boolean estEnEchec() {
        return estEnEchec;
    }

    public int getCaseEP() {
        return caseEP;
    }

    public List<GCoups> getCoupsValides() {
        Generateur generateur = new Generateur(this, trait);
        coupsvalides = generateur.getCoupsValides();
        estEnEchec = generateur.estEnEchec;
        coupsvalides_lan = new ArrayList<>();
        for (GCoups c : coupsvalides) {
            coupsvalides_lan.add(GCoups.getString(c));
        }
        Collections.sort(coupsvalides_lan);
        return coupsvalides;
    }

    public List<GCoups> getCoupsValides(int t) {
        int _trait = trait;
        if (t == BLANC) {//BLANC
            setTrait(BLANC);
            Generateur generateur = new Generateur(this, trait);
            coupsvalides = generateur.getCoupsValides();
            estEnEchec = generateur.estEnEchec;
            setTrait(_trait);
            return coupsvalides;
        } else {//NOIR
            setTrait(NOIR);
            Generateur generateur = new Generateur(this, trait);
            coupsvalides = generateur.getCoupsValides();
            estEnEchec = generateur.estEnEchec;
            setTrait(_trait);
            return coupsvalides;
        }
    }

    public List<String> getCoupsvalides_lan() {
        return coupsvalides_lan;
    }

    public int[] getEtats() {
        return etats;
    }

    public String getFen() {
        return fen;
    }

    public int getTrait() {
        return trait;
    }

    public String print() {
        String str = "";
        String e_str;
        String Clr_str;
        int rg = 0;
        for (int e : etats) {
            int piecetype = Math.abs(e);
//            Clr_str = piecetype == e ? "N" : "B";
            Clr_str = piecetype == e ? "B" : "N";
            switch (piecetype) {
                case PION:
                    e_str = "P";
                    break;
                case ROI:
                    e_str = "K";
                    break;
                case VIDE:
                    e_str = " ";
                    break;
                case OUT:
                    e_str = "X";
                    break;
                default:
                    e_str = STRING_PIECE[piecetype];
                    break;
            }
            Clr_str = "X".equals(e_str) ? "X" : Clr_str;
            Clr_str = " ".equals(e_str) ? "_" : Clr_str;
            str += e_str + Clr_str + " ";

            rg++;
            if (rg == 12) {
                str += '\n';
                rg = 0;
            }
        }
        return str;
    }

    public void setTrait(int trait) {
        this.trait = trait;
    }

    @Override
    public String toString() {
        return XBoardAdapter.DEBUG ? "CP_CoupsValides : "
                + '\n' + cp_coupsvalides_lan + '\n' + "G_CoupsValides : "
                + '\n' + coupsvalides_lan : "G_CoupsValides : "
                + '\n' + coupsvalides_lan;
        //        return coupsvalides_lan.toString();
    }

    public boolean hasRoques(int color) {
        int c = color == BLANC ? 0 : 2;
        return roques[0 + c] || roques[1 + c];
    }

    public boolean isGrandRoque(int color) {
        int c = color == BLANC ? 0 : 2;
        return roques[1 + c];
    }

    public boolean isPetitRoque(int color) {
        int c = color == BLANC ? 0 : 2;
        return roques[0 + c];
    }

    /**
     * Renvoi la valeur du compteur de demi-coups depuis la dernière prise ou le
     * dernier mouvement de pion.
     *
     * @return
     */
    public final int getHalfmoveCount() {

        return _halfmoveCount;
    }

    /**
     * Renvoi le numéro du coup.
     */
    public final int getFullmoveNumber() {

        return _fullmoveNumber;
    }

}
