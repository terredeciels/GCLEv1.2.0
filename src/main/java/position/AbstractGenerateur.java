package position;

import java.util.*;
import org.apache.commons.lang3.Range;
import static org.apache.commons.lang3.Range.between;
import static position.ICodage.TYPE_DE_COUPS.*;

public class AbstractGenerateur implements ICodage {

    protected List<GCoups> pseudoCoups;
    protected int[] etats;
    protected int couleur;
    protected boolean recherchePionAttaqueRoque = false;
    protected GPosition gp;
    protected Range<Integer> rang_final, rang_initial;

    public AbstractGenerateur() {
        this.recherchePionAttaqueRoque = true;
        range();
    }

    public AbstractGenerateur(GPosition gp, int couleur) {
        this.gp = gp;
        this.couleur = couleur;
        this.etats = gp.etats;
        range();

    }

    private void range() {
        rang_final = couleur == NOIR ? between(a1, h1) : between(a8, h8);
        rang_initial = couleur == NOIR ? between(98, 105) : between(38, 45);
    }

    protected boolean pieceQuiALeTrait(int caseO) {
        int couleurPiece = etats[caseO] < 0 ? BLANC : NOIR;
        return !(etats[caseO] == VIDE) && couleurPiece == couleur;
    }

    protected boolean pieceAdverse(int caseX) {
        return etats[caseX] != OUT && etats[caseX] * couleur < 0;
    }

    protected boolean pionDeCouleur(int s, int couleur) {
        int typeDePiece = etats[s] < 0 ? -etats[s] : etats[s];
        int couleurPiece = etats[s] < 0 ? BLANC : NOIR;
        return typeDePiece == PION && couleurPiece == couleur;
    }

    protected void ajouterCoups(int caseO, int caseX, TYPE_DE_COUPS type_de_coups) {
        if (type_de_coups != Null) {
            pseudoCoups.add(new GCoups(etats[caseO], caseO, caseX, etats[caseX], type_de_coups));
        }
    }

    protected void addPseudoCoupsPromotion(int caseO, int caseX, int pieceprise) {
        pseudoCoups.add(new GCoups(couleur * PION, caseO, caseX, pieceprise, Promotion, couleur * FOU));
        pseudoCoups.add(new GCoups(couleur * PION, caseO, caseX, pieceprise, Promotion, couleur * CAVALIER));
        pseudoCoups.add(new GCoups(couleur * PION, caseO, caseX, pieceprise, Promotion, couleur * DAME));
        pseudoCoups.add(new GCoups(couleur * PION, caseO, caseX, pieceprise, Promotion, couleur * TOUR));
    }

}
