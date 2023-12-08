package backend;

public final class Pawn extends Piece implements Cloneable{

    private boolean hasMoved;//needed for en passant

    public Pawn(ChessAlphabet position, boolean color) {
        super(position, color);
        this.setType("Pawn");
    }

    void setHasMoved() {
        hasMoved = true;
    }

    void promotePawn(ChessGame board, ChessAlphabet position, char newPiece) throws InvalidSyntaxException {
        boolean color = board.positions[position.getRank()][position.getFile()].piece.isWhite();
        switch (newPiece) {
            case ('K'):
                board.positions[position.getRank()][position.getFile()].piece = PieceFactory.createKnight(position, color);
                break;
            case ('Q'):
                board.positions[position.getRank()][position.getFile()].piece = PieceFactory.createQueen(position, color);
                break;
            case ('B'):
                board.positions[position.getRank()][position.getFile()].piece = PieceFactory.createBishop(position, color);
                break;
            case ('R'):
                board.positions[position.getRank()][position.getFile()].piece = PieceFactory.createRook(position, color);
                break;
            default:
                throw new InvalidSyntaxException("Invalid promotion piece");
        }
    }

    public boolean isEnPassant(ChessGame board, ChessAlphabet start, ChessAlphabet end) {
        int row = start.getRank();
        int column = start.getFile();
        int Drow = end.getRank();
        int Dcolumn = end.getFile();
        int rowDirection = this.isWhite() ? 1 : -1;
        if (row + rowDirection == Drow && Math.abs(column - Dcolumn) == 1 && board.turn >1) {
            Move lastMove = board.moves.get(board.turn - 1);
            if (row == lastMove.getEnd().getRank() && Math.abs(column - lastMove.getEnd().getFile()) == 1) {
                if ((board.positions[row][column].piece.isWhite()
                        && (!board.positions[lastMove.getEnd().getRank()][lastMove.getEnd().getFile()].piece.isWhite()
                        && board.positions[lastMove.getEnd().getRank()][lastMove.getEnd().getFile()].piece instanceof Pawn))
                        || (!board.positions[row][column].piece.isWhite()
                        && (board.positions[lastMove.getEnd().getRank()][lastMove.getEnd().getFile()].piece.isWhite()
                        && board.positions[lastMove.getEnd().getRank()][lastMove.getEnd().getFile()].piece instanceof Pawn))) {
                    if (Dcolumn == lastMove.getEnd().getFile()) {
                        return Math.abs((lastMove.getStart().getRank()) - (lastMove.getEnd().getRank())) == 2;
                    }
                }
            }
        }
        return false;
    }

    @Override
    boolean isValidMove(ChessGame board, ChessAlphabet start, ChessAlphabet end) {
        int row = start.getRank();
        int column = start.getFile();
        int Drow = end.getRank();
        int Dcolumn = end.getFile();
        int direction = board.positions[row][column].piece.isWhite() ? 1 : -1;

        //checking first starting block is occupied so that we have a piece to move
        if (board.positions[row][column].isOccupied) {
            //pawn moving upwards
            if (column == Dcolumn && Drow == row + direction && !board.positions[Drow][Dcolumn].isOccupied) {
                return true;
            }
            //pawn moving upwards by 2 as first move
            if (column == Dcolumn && Drow == row + 2 * direction && !board.positions[Drow][Dcolumn].isOccupied
                    && !board.positions[row + direction][column].isOccupied & !hasMoved) {
                return true;
            }
            //pawn captures
            if (Math.abs(column - Dcolumn) == 1 && row + direction == Drow && board.positions[Drow][Dcolumn].isOccupied) {
                if (board.positions[Drow][Dcolumn].piece.isWhite() && !board.positions[row][column].piece.isWhite()) {
                    return true;
                }
                if (!board.positions[Drow][Dcolumn].piece.isWhite() && board.positions[row][column].piece.isWhite()) {
                    return true;
                }
            }
            return isEnPassant(board, start, end);
        }
        return false;
    }

    @Override
     protected Piece clone() throws CloneNotSupportedException {
        Pawn clone = (Pawn) PieceFactory.createPawn(this.getPosition(),this.isWhite());
        if (this.hasMoved)
            clone.setHasMoved();
        return clone;
    }
}
