/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backend;

/**
 *
 * @author youss
 */
public class Move implements MoveCommand {

    private ChessAlphabet start;
    private ChessAlphabet end;
    private char promotion;
    private boolean enPassant;
    private boolean castle;
    private final ChessGame instance = ChessGame.getGameInstance();


    public Move(ChessAlphabet start, ChessAlphabet end, char promotion) {
        this.start = start;
        this.end = end;
        this.setPromotion(promotion);
        if (instance.positions[start.getRank()][start.getFile()].isOccupied){
            Piece startPiece = instance.positions[start.getRank()][start.getFile()].getPiece();
                if (startPiece instanceof King && ((King) startPiece).canCastle(instance, start, end)) {
                    castle = true;
                }
                if (startPiece instanceof Pawn && ((Pawn) startPiece).isEnPassant(instance, start, end)) {
                    enPassant = true;
                }
        }
    }

    public ChessAlphabet getStart() {
        return start;
    }

    public ChessAlphabet getEnd() {
        return end;
    }

    public char getPromotion() {
        return promotion;
    }

    public boolean isEnPassant() {
        return enPassant;
    }

    public boolean isCastle() {
        return castle;
    }

    @Override
    public void executeMove() throws InvalidSyntaxException, InvalidMoveException {
        instance.validateMove(this);
    }

    public void setPromotion(char promotion) {
        this.promotion = promotion;
    }

    public static class MoveExecutor {
        public static void executeCommand(Move move) throws InvalidSyntaxException, InvalidMoveException {
            move.executeMove();
        }

    }
}
