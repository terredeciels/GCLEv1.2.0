package position;

import java.util.Objects;

public class GCoups implements ICodage {

     final int PAS_DE_PIECE = -1;
     final int piece;
     final int caseO;
     final int caseX;
     final int pieceprise;
     final TYPE_DE_COUPS type_de_coups;
     final int piecePromotion;
     final int caseOTour;
     final int caseXTour;

    public GCoups(int piece, int caseO, int caseX, int pieceprise, TYPE_DE_COUPS type_de_coups) {
        this.piece = piece;
        this.caseO = caseO;
        this.caseX = caseX;
        this.pieceprise = pieceprise;
        this.type_de_coups = type_de_coups;
        this.piecePromotion = PAS_DE_PIECE;
        this.caseOTour = PAS_DE_PIECE;
        this.caseXTour = PAS_DE_PIECE;
    }

    public GCoups(int piece, int caseO, int caseX, int pieceprise, TYPE_DE_COUPS type_de_coups, int piecePromotion) {
        this.piece = piece;
        this.caseO = caseO;
        this.caseX = caseX;
        this.pieceprise = pieceprise;
        this.type_de_coups = type_de_coups;
        this.piecePromotion = piecePromotion;
        this.caseOTour = PAS_DE_PIECE;
        this.caseXTour = PAS_DE_PIECE;
    }

    public GCoups(int piece, int caseO, int caseX, int caseOTour, int caseXTour, int pieceprise, TYPE_DE_COUPS type_de_coups) {
        this.piece = piece;
        this.caseO = caseO;
        this.caseX = caseX;
        this.caseOTour = caseOTour;
        this.caseXTour = caseXTour;
        this.pieceprise = pieceprise;
        this.type_de_coups = type_de_coups;
        this.piecePromotion = PAS_DE_PIECE;
    }

//    public GCoups(int caseO, int caseX) {
//        this.caseO = caseO;
//        this.caseX = caseX;
//        this.piece = PAS_DE_PIECE;
//        this.pieceprise = PAS_DE_PIECE;
//        this.type_de_coups = null;
//        this.piecePromotion = PAS_DE_PIECE;
//    }
    public static String getString(GCoups coups) {
        if (((coups.piece == ROI) && (coups.caseO == e1) && (coups.caseX == g1))
                || ((coups.piece == ROI) && (coups.caseO == e8) && (coups.caseX == g8))) {
            return "O-O";
        } else if (((coups.piece == ROI) && (coups.caseO == e1) && (coups.caseX == c1))
                || ((coups.piece == ROI) && (coups.caseO == e8) && (coups.caseX == c8))) {
            return "O-O-O";
        } else if (coups.type_de_coups == TYPE_DE_COUPS.EnPassant) {
            return STRING_CASES[INDICECASES[coups.getCaseO()]] + "x" + STRING_CASES[INDICECASES[coups.getCaseX()]];
        } else if (coups.type_de_coups == TYPE_DE_COUPS.Promotion) {
            if (coups.pieceprise != 0) {
                return STRING_CASES[INDICECASES[coups.getCaseO()]] + "x" + STRING_CASES[INDICECASES[coups.getCaseX()]]
                        + STRING_PIECE[Math.abs(coups.piecePromotion)];
            } else {
                return STRING_CASES[INDICECASES[coups.getCaseO()]] + "-" + STRING_CASES[INDICECASES[coups.getCaseX()]]
                        + STRING_PIECE[Math.abs(coups.piecePromotion)];
            }
        } else if (coups.type_de_coups == TYPE_DE_COUPS.Prise) {
            return STRING_CASES[INDICECASES[coups.getCaseO()]] + "x" + STRING_CASES[INDICECASES[coups.getCaseX()]];
        } else {
            return STRING_CASES[INDICECASES[coups.getCaseO()]] + "-" + STRING_CASES[INDICECASES[coups.getCaseX()]];
        }
    }

    public int getCaseO() {
        return caseO;
    }

    public int getCaseX() {
        return caseX;
    }

    public int getCaseOTour() {
        return caseOTour;
    }

    public int getCaseXTour() {
        return caseXTour;
    }

    public int getPiece() {
        return piece;
    }

    public int getPiecePrise() {
        return pieceprise;
    }

    public TYPE_DE_COUPS getTypeDeCoups() {
        return type_de_coups;
    }

    public int getPiecePromotion() {
        return piecePromotion;
    }

    @Override
    public boolean equals(final Object pObjet) {
        if (this == pObjet) {
            return true;
        }

        if (pObjet instanceof GCoups) {
            final GCoups m = (GCoups) pObjet;
            return (caseO == m.caseO) && (piece == m.piece) && (caseX == m.caseX)
                    && (pieceprise == m.pieceprise) && (piecePromotion == m.piecePromotion);
        }

        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + this.piece;
        hash = 79 * hash + this.caseO;
        hash = 79 * hash + this.caseX;
        hash = 79 * hash + this.pieceprise;
        hash = 79 * hash + Objects.hashCode(this.type_de_coups);
        hash = 79 * hash + this.piecePromotion;
        hash = 79 * hash + this.caseOTour;
        hash = 79 * hash + this.caseXTour;
        return hash;
    }

    @Override
    public String toString() {
        return getString(this);
    }

}
