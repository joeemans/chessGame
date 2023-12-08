package backend;

public final class Queen extends Piece {

    public Queen(ChessAlphabet position, boolean color) {
        super(position, color);
        this.setType("Queen");
    }

    @Override
    boolean isValidMove(ChessGame board, ChessAlphabet start, ChessAlphabet end) {
        int row = start.getRank();
        int column = start.getFile();
        int Drow = end.getRank();
        int Dcolumn = end.getFile();
        //checking that queen movement is valid and that we have a piece at starting square, and that our path is clear
        if ((row == Drow || column == Dcolumn || Math.abs(row - Drow) == Math.abs(column - Dcolumn))
                && board.positions[row][column].isOccupied && board.isClearPath(start, end)) {
            //now we have finished checking the path, time to check if target square is empty
            if (!board.positions[Drow][Dcolumn].isOccupied) {
                return true;
            } //queen takes
            return (board.positions[row][column].piece.isWhite() && !board.positions[Drow][Dcolumn].piece.isWhite())
                    || (!board.positions[row][column].piece.isWhite() && board.positions[Drow][Dcolumn].piece.isWhite());
        }
        return false;
    }

    @Override
    protected Piece clone() throws CloneNotSupportedException {
        return PieceFactory.createQueen(this.getPosition(),this.isWhite());
    }

}
