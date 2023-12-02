package backend;

public class Pawn extends Piece {

    private boolean hasMoved;//needed for en passant

    public Pawn(String type, ChessAlphabet position, String color) {
        super(type, position, color);

    }

    public void setHasMoved() {
        hasMoved = true;
    }

    public void promotePawn(ChessGame board, ChessAlphabet position, char newPiece) throws InvalidSyntaxException {
        String color = board.positions[position.getRank()][position.getFile()].piece.color;
        switch (newPiece) {
            case ('K'):
                board.positions[position.getRank()][position.getFile()].piece = new Knight("knight", position, color);
                break;
            case ('Q'):
                board.positions[position.getRank()][position.getFile()].piece = new Queen("queen", position, color);
                break;
            case ('B'):
                board.positions[position.getRank()][position.getFile()].piece = new Bishop("bishop", position, color);
                break;
            case ('R'):
                board.positions[position.getRank()][position.getFile()].piece = new Rook("rook", position, color);
                break;
            default:
                throw new InvalidSyntaxException("Invalid promotion piece.");
        }
    }

    public boolean isEnPassant(ChessGame board, ChessAlphabet start, ChessAlphabet end) {
        int row = start.getRank();
        int column = start.getFile();
        int Drow = end.getRank();
        int Dcolumn = end.getFile();
        if (Math.abs(row - Drow) == 1 && Math.abs(column - Dcolumn) == 1) {
            Move lastMove = board.moves.get(board.turn - 1);
            if (row == lastMove.end.getRank() && Math.abs(column - lastMove.end.getFile()) == 1) {
                if ((board.positions[row][column].piece.isWhite
                        && (!board.positions[lastMove.end.getRank()][lastMove.end.getFile()].piece.isWhite
                        && board.positions[lastMove.end.getRank()][lastMove.end.getFile()].piece instanceof Pawn))
                        || (!board.positions[row][column].piece.isWhite
                        && (board.positions[lastMove.end.getRank()][lastMove.end.getFile()].piece.isWhite
                        && board.positions[lastMove.end.getRank()][lastMove.end.getFile()].piece instanceof Pawn))) {
                    if (Dcolumn == lastMove.end.getFile()) {
                        if (Math.abs((lastMove.start.getRank()) - (lastMove.end.getRank())) == 2) {
                            return true;
                        }
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
        int direction = board.positions[row][column].piece.isWhite ? 1 : -1;

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
                if (board.positions[Drow][Dcolumn].piece.isWhite && !board.positions[row][column].piece.isWhite) {
                    return true;
                }
                if (!board.positions[Drow][Dcolumn].piece.isWhite && board.positions[row][column].piece.isWhite) {
                    return true;
                }
            }
            return isEnPassant(board, start, end);
        }
        return false;
    }
}
