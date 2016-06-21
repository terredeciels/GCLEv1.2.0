package position;

import static java.lang.System.*;
import java.util.List;
import org.chesspresso.position.Position;
import static position.ICodage.*;
import static position.ICodage.Roque.*;
import static position.ICodage.TYPE_DE_COUPS.EnPassant;

public class GPosition implements ICodage {

    int caseEP;
    Roque R;
    boolean[] roques;
    int trait;
    int[] etats;
    List<GCoups> coupsvalides;
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

    public void e(GPosition p, int co, int cx) {
        p.etats[co] = p.etats[cx];
    }

    public void e(GPosition p, int co) {
        p.etats[co] = VIDE;
    }

    int typePiece(int x) {
        return x < 0 ? -x : x;
    }

    int abs(int x) {
        return x < 0 ? -x : x;
    }

    public boolean exec(GCoups m, UndoGCoups ug) {
        arraycopy(etats, 0, ug.etats, 0, NB_CELLULES);
        ug.setKQkq(roques);
        ug.caseEP = caseEP;
        int O = m.caseO;
        int X = m.caseX;
        int p = m.piece;
        TYPE_DE_COUPS t = m.type_de_coups;
        caseEP = -1;
        Roque.trait = trait;

        if (typePiece(p) == PION && abs(X - O) == nord - sud) {
            // avance de 2 cases
            caseEP = trait == NOIR ? X + 12 : X - 12;
        }
        switch (t) {
            case Deplacement:
            case Prise:
                e(this, X, O);
                e(this, O);
                valideDroitRoque(m);
                break;
            case EnPassant:
                // X == caseEP
                e(this, X, O);
                e(this, O);
                if (t == EnPassant) {
                    e(this, X + nord * trait);
                }
                break;
            case Promotion:
                etats[X] = m.piecePromotion;
                e(this, O);
                break;
            case Roque:
                e(this, X, O);
                e(this, O);
                e(this, m.caseXTour, m.caseOTour);
                e(this, m.caseOTour);
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
                if (caseO == caseTourH(trait)) {
                    unsetK(trait);
                }
                if (caseO == caseTourA(trait)) {
                    unsetQ(trait);
                }
                break;
            default:
                break;
        }
        // roi et tour à leurs places
        if (etats[caseTourA(trait)] != trait * TOUR || etats[caseRoi(trait)] != trait * ROI) {
            unsetQ(trait);
        }

        if (etats[caseTourH(trait)] != trait * TOUR || etats[caseRoi(trait)] != trait * ROI) {
            unsetK(trait);
        }
    }

    public boolean isInCheck(final int pCouleur) {
        getCoupsValides(pCouleur);
        return estEnEchec;
    }

    public int getCaseEP() {
        return caseEP;
    }

    public List<GCoups> getCoupsValides() {
        Generateur generateur = new Generateur(this, trait);
        coupsvalides = generateur.getCoupsValides();
        estEnEchec = generateur.estEnEchec;
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

    public int[] getEtats() {
        return etats;
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

    public final int getHalfmoveCount() {
        return _halfmoveCount;
    }

    public final int getFullmoveNumber() {

        return _fullmoveNumber;
    }

}
