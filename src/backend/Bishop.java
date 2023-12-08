/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backend;

public final class Bishop extends Piece {

    Bishop(ChessAlphabet position, boolean color) {
        super(position, color);
        this.setType("Bishop");
    }

    @Override
    boolean isValidMove(ChessGame board, ChessAlphabet start, ChessAlphabet end) {
        int row = start.getRank();
        int column = start.getFile();
        int Drow = end.getRank();
        int Dcolumn = end.getFile();
        //checking for diagonal movement and if starting block is occupied so that we have a piece to move, and if we have a clear path
        if (Math.abs(Drow - row) == Math.abs(Dcolumn - column) && board.positions[row][column].isOccupied
                && board.isClearPath(start, end)) {
            //now the path is clear, we need to check if destination square is occupied
            if (!board.positions[Drow][Dcolumn].isOccupied) {
                return true;
            }
            //bishop takes
            if ((board.positions[row][column].piece.isWhite() && !board.positions[Drow][Dcolumn].piece.isWhite())
                    || (!board.positions[row][column].piece.isWhite() && board.positions[Drow][Dcolumn].piece.isWhite())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected Piece clone() throws CloneNotSupportedException {
        return PieceFactory.createBishop(this.getPosition(),this.isWhite());
    }

}
