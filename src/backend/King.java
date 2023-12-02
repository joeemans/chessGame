package backend;

public class King extends Piece {

    private boolean hasMoved;//this is needed for the castle move

    public King(String type, ChessAlphabet position, String color) {
        super(type, position, color);

    }

    public void setHasMoved() {
        hasMoved = true;
    }

    public boolean canCastle(ChessGame board, ChessAlphabet start, ChessAlphabet end) {
        int row = start.getRank();
        int column = start.getFile();
        int Drow = end.getRank();
        int Dcolumn = end.getFile();
        int direction = Dcolumn > column ? 1 : -1;
        int Ccolumn = column + direction;
        if (Math.abs(column - Dcolumn) == 2 && Drow == row && !hasMoved) {
            if (direction == 1 && board.positions[Drow][Dcolumn + direction].piece instanceof Rook
                    && !(((Rook) board.positions[Drow][Dcolumn + direction].piece).getHasMoved())) {
                while (Ccolumn != Dcolumn) {
                    if (board.positions[row][Ccolumn].isOccupied
                            || board.isSquareUnderAttack(ChessAlphabet.getChessAlphabet(row, Ccolumn), board.positions[row][column].piece.isWhite)) {
                        return false;
                    }
                    Ccolumn += direction;
                }
                if (board.isSquareUnderAttack(start, board.positions[row][column].piece.isWhite)
                        || board.isSquareUnderAttack(end, board.positions[row][column].piece.isWhite)) {
                    return false;
                }
                return true;
            } else if (direction == -1 && board.positions[Drow][Dcolumn + 2 * direction].piece instanceof Rook
                    && !(((Rook) board.positions[Drow][Dcolumn + 2 * direction].piece).getHasMoved())) {
                while (Ccolumn != Dcolumn) {
                    if (board.positions[row][Ccolumn].isOccupied
                            || board.isSquareUnderAttack(ChessAlphabet.getChessAlphabet(row, Ccolumn), board.positions[row][column].piece.isWhite)) {
                        return false;
                    }
                    Ccolumn += direction;
                }
                if (board.isSquareUnderAttack(start, board.positions[row][column].piece.isWhite)
                        || board.isSquareUnderAttack(end, board.positions[row][column].piece.isWhite)) {
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    @Override
    boolean isValidMove(ChessGame board, ChessAlphabet start,
            ChessAlphabet end) {
        int row = start.getRank();
        int column = start.getFile();
        int Drow = end.getRank();
        int Dcolumn = end.getFile();
        //we are going to check for castle first
        if (Math.abs(column - Dcolumn) == 2 && Drow == row) {
            return canCastle(board, start, end);
        }
        //this is a very long if statement, we check for a correct king movement to a square that is not under threat from opponent
        if (Math.abs(row - Drow) <= 1 && Math.abs(column - Dcolumn) <= 1
                && !board.isSquareUnderAttack(end, board.positions[row][column].piece.isWhite)
                && (!board.positions[Drow][Dcolumn].isOccupied || (board.positions[row][column].piece.isWhite && !board.positions[Drow][Dcolumn].piece.isWhite)
                || (!board.positions[row][column].piece.isWhite && board.positions[Drow][Dcolumn].piece.isWhite))) {
            return true;
        }
        return false;
    }

}
