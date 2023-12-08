package backend;

//this is the factory design pattern. Its main purpose is separating the process of creating objects from the rest of the program,
//which generally allows for better control over it. However, its necessity is rather disputed here since we can only create a
//        set number of pieces
abstract class PieceFactory {
    static Piece createPawn(ChessAlphabet position, boolean colour) {
        return new Pawn(position, colour);
    }

    static Piece createBishop(ChessAlphabet position, boolean colour) {
        return new Bishop(position, colour);
    }

    static Piece createKnight(ChessAlphabet position, boolean colour) {
        return new Knight(position, colour);
    }

    static Piece createKing(ChessAlphabet position, boolean colour) {
        return new King(position, colour);
    }

    static Piece createQueen(ChessAlphabet position, boolean colour) {
        return new Queen(position, colour);
    }

    static Piece createRook(ChessAlphabet position, boolean colour) {
        return new Rook(position, colour);
    }
}