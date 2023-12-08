package backend;

public final class Knight extends Piece {

    public Knight(ChessAlphabet position, boolean color) {
        super(position, color);
        this.setType("Knight");
    }

    @Override
    boolean isValidMove(ChessGame board, ChessAlphabet start, ChessAlphabet end) {
        int rowS = start.getRank();
        int colS = start.getFile();
        int rowE = end.getRank();
        int colE = end.getFile();
        //checking is starting block is occupied (so that we have a piece to move, and the distance is equal to knight path
        if (board.positions[rowS][colS].isOccupied && ((Math.abs(rowS - rowE) == 2 && Math.abs(colS - colE) == 1)
                || (Math.abs(rowS - rowE) == 1 && Math.abs(colS - colE) == 2))) {
            //knight takes
            if (board.positions[rowE][colE].isOccupied) {
                if (((board.positions[rowS][colS].piece.isWhite()) && !((board.positions[rowE][colE].piece.isWhite())))
                        || (!(board.positions[rowS][colS].piece.isWhite()) && (board.positions[rowE][colE].piece.isWhite()))) {
                    return true;
                }
            }
            //in this case knight jumps straight to destination
            else return true;
        }
        return false;
    }

    @Override
    protected Piece clone() throws CloneNotSupportedException {
        return PieceFactory.createKnight(this.getPosition() , this.isWhite());
    }
}
