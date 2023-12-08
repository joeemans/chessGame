package backend;

import java.util.ArrayList;

public interface GameObserver {
    void check(ChessAlphabet position);
    void checkmate();
    void stalemate();
    void drawByInsufficientMaterial();
    void squaresChanged(ArrayList<ChessAlphabet> changedSquares);
    void removeCheck(ChessAlphabet position);
}
