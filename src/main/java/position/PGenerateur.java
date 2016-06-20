package position;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import static position.ICodage.PieceType.*;
import static position.ICodage.TYPE_DE_COUPS.*;

public class PGenerateur extends AbstractGenerateur {

    boolean estEnEchec;
    List<Integer> DIR_PIECE;
    int glissable;
    private int caseO;
    PieceType etat;

    public PGenerateur() {
        super();
    }

    public PGenerateur(GPosition g_position, int couleur) {
        super(g_position, couleur);
        pseudoCoups = new ArrayList<>();
    }

    public List<GCoups> pseudoCoups(GPosition gp, int couleur) {
        pseudoCoups = new ArrayList<>();
        this.gp = gp;
        this.etats = gp.etats;
        this.couleur = couleur;

        for (int s : CASES117) {
            if (pieceQuiALeTrait(s)) {
                etat = _PIECE_TYPE(etats[s] < 0 ? -etats[s] : etats[s]);

                caseO = s;
                pseudoCoups();
            }
        }
        return pseudoCoups;
    }

    public void pseudoCoups() {
    
        Iterator<Integer> it = etat.DIR_PIECE.iterator();
        switch (etat.glissant) {
            case non_glissant:
                if (etat == pion) {
                    pseudoCoups(recherchePionAttaqueRoque);
                } else {
                    while (it.hasNext()) {
                        int caseX = caseO + it.next();
                        if (etats[caseX] == VIDE) {
                            ajouterCoups(caseO, caseX, Deplacement);
                        } else if (pieceAdverse(caseX)) {
                            ajouterCoups(caseO, caseX, Prise);
                        }
                    }
                }
                break;
            case glissant:
                while (it.hasNext()) {
                    int direction = it.next();
                    int caseX = caseO + direction;
                    int etatX = etats[caseX];
                    while (etatX == VIDE) {
                        ajouterCoups(caseO, caseX, Deplacement);
                        caseX += direction;
                        etatX = etats[caseX];
                    }
                    if (pieceAdverse(caseX)) {
                        ajouterCoups(caseO, caseX, Prise);
                    }
                }
        }

    }

    public void pseudoCoups(boolean recherchePionAttaqueRoque) {
        int NordSudSelonCouleur = couleur == BLANC ? nord : sud;
        // avances du pion
        int caseX = caseO + NordSudSelonCouleur;
        if (etats[caseX] == VIDE) {
            if (rang_final.contains(caseX)) {
                addPseudoCoupsPromotion(caseO, caseX, 0);
            } else {
                pseudoCoups.add(new GCoups(couleur * PION, caseO, caseX, 0, Deplacement));
            }
            if (rang_initial.contains(caseO)) {
                caseX += NordSudSelonCouleur;
                if (etats[caseX] == VIDE) {
                    pseudoCoups.add(new GCoups(couleur * PION, caseO, caseX, 0, Deplacement));
                }
            }
        }
        // diagonales du pion (prise)
        if (!recherchePionAttaqueRoque) {
            diagonalePionPrise(caseO, NordSudSelonCouleur, est);
            diagonalePionPrise(caseO, NordSudSelonCouleur, ouest);
        } else {
            // diagonales du pion (attaque)
            diagonalePionAttaqueRoque(caseO, NordSudSelonCouleur, est);
            diagonalePionAttaqueRoque(caseO, NordSudSelonCouleur, ouest);
        }
    }

    public void diagonalePionAttaqueRoque(int caseO, int NordSudSelonCouleur, int estOuOuest) {
        int caseX = caseO + NordSudSelonCouleur + estOuOuest;
        if (etats[caseX] != OUT) {
            pseudoCoups.add(new GCoups(couleur * PION, caseO, caseX, etats[caseX], Attaque));
        }
    }

    public void diagonalePionPrise(int caseO, int NordSudSelonCouleur, int estOuOuest) {
        int caseX = caseO + NordSudSelonCouleur + estOuOuest;
        if (pieceAdverse(caseX)) {
            if (rang_final.contains(caseX)) {
                addPseudoCoupsPromotion(caseO, caseX, etats[caseX]);
            } else {
                pseudoCoups.add(new GCoups(couleur * PION, caseO, caseX, etats[caseX], Prise));
            }
        }
    }

}
