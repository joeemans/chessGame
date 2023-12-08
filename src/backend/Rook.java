package backend;

public final class Rook extends Piece {

    private boolean hasMoved;//this is needed for the castle move

    public Rook(ChessAlphabet position, boolean color) {
        super(position, color);
        this.setType("Rook");
    }

    void setHasMoved() {
        hasMoved = true;
    }

    boolean getHasMoved() {
        return hasMoved;
    }

    @Override
    boolean isValidMove(ChessGame board, ChessAlphabet start, ChessAlphabet end) {
        int row = start.getRank();
        int column = start.getFile();
        int Drow = end.getRank();
        int Dcolumn = end.getFile();
        //checking for valid rook movement, and that we have a piece at starting square, and that our path is clear
        if ((row == Drow || column == Dcolumn) && board.positions[row][column].isOccupied
                && board.isClearPath(start, end)) {
            if (!board.positions[Drow][Dcolumn].isOccupied) {
                return true;
            }
            //rook takes
            return (board.positions[row][column].piece.isWhite() && !board.positions[Drow][Dcolumn].piece.isWhite())
                    || (!board.positions[row][column].piece.isWhite() && board.positions[Drow][Dcolumn].piece.isWhite());
        }
        return false;
    }

    @Override
    protected Piece clone() throws CloneNotSupportedException {
        Rook clone = (Rook) PieceFactory.createRook(this.getPosition() , this.isWhite());
        if(this.hasMoved)
            clone.setHasMoved();
        return clone;
    }
}
