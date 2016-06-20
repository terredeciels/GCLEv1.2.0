package ggame;

import position.FenToGPosition;
import position.GCoups;
import position.GPosition;
import position.ICodage;

/*
    @TODO Thread
 */
public class GGame {

    public GCoups lastmove;
    public GPosition gPosition;

    public GGame() {
        gPosition = FenToGPosition.toGPosition(ICodage.FEN_INITIALE);
    }

    public void setGPositionMove(GPosition gPosition) {
        this.gPosition = gPosition;
    }

    public GPosition getGPositionMove() {
        return gPosition;
    }

    public void resetTo() {
        gPosition = FenToGPosition.toGPosition(ICodage.FEN_INITIALE);
    }
}
