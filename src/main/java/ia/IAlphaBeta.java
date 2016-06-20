package ia;

import position.GPosition;

public interface IAlphaBeta {

    int alphabeta(final GPosition gPosition, final int pDepth, final int pAlpha, final int pBeta);
}
