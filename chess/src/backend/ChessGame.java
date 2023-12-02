package backend;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChessGame {

    public Square[][] positions = new Square[8][8];
    int turn = 0;
    King wk;
    King bk;
    protected ArrayList<Move> moves = new ArrayList<>();
    private boolean gameEnded = false;

    public ChessGame() {
        initializeGame();
    }

    private void initializeGame() {
        King whiteKing = new King("King", ChessAlphabet.e1, "white");
        positions[ChessAlphabet.e1.getRank()][ChessAlphabet.e1.getFile()] = new Square(whiteKing);
        wk = whiteKing;
        King BlackKing = new King("King", ChessAlphabet.e8, "black");
        positions[ChessAlphabet.e8.getRank()][ChessAlphabet.e8.getFile()] = new Square(BlackKing);
        bk = BlackKing;
        Rook whiteRook1 = new Rook("Rook", ChessAlphabet.a1, "white");
        positions[ChessAlphabet.a1.getRank()][ChessAlphabet.a1.getFile()] = new Square(whiteRook1);
        Rook whiteRook2 = new Rook("Rook", ChessAlphabet.h1, "white");
        positions[ChessAlphabet.h1.getRank()][ChessAlphabet.h1.getFile()] = new Square(whiteRook2);
        Rook blackRook1 = new Rook("Rook", ChessAlphabet.a8, "black");
        positions[ChessAlphabet.a8.getRank()][ChessAlphabet.a8.getFile()] = new Square(blackRook1);
        Rook blackRook2 = new Rook("Rook", ChessAlphabet.h8, "black");
        positions[ChessAlphabet.h8.getRank()][ChessAlphabet.h8.getFile()] = new Square(blackRook2);
        Bishop whiteBishop1 = new Bishop("Bishop", ChessAlphabet.c1, "white");
        positions[ChessAlphabet.c1.getRank()][ChessAlphabet.c1.getFile()] = new Square(whiteBishop1);
        Bishop whiteBishop2 = new Bishop("Bishop", ChessAlphabet.f1, "white");
        positions[ChessAlphabet.f1.getRank()][ChessAlphabet.f1.getFile()] = new Square(whiteBishop2);
        Bishop blackBishop1 = new Bishop("Bishop", ChessAlphabet.c8, "black");
        positions[ChessAlphabet.c8.getRank()][ChessAlphabet.c8.getFile()] = new Square(blackBishop1);
        Bishop blackBishop2 = new Bishop("Bishop", ChessAlphabet.f8, "black");
        positions[ChessAlphabet.f8.getRank()][ChessAlphabet.f8.getFile()] = new Square(blackBishop2);
        Knight whiteKnight1 = new Knight("Knight", ChessAlphabet.b1, "white");
        positions[ChessAlphabet.b1.getRank()][ChessAlphabet.b1.getFile()] = new Square(whiteKnight1);
        Knight whiteKnight2 = new Knight("Knight", ChessAlphabet.g1, "white");
        positions[ChessAlphabet.g1.getRank()][ChessAlphabet.g1.getFile()] = new Square(whiteKnight2);
        Knight blackKnight1 = new Knight("Knight", ChessAlphabet.b8, "black");
        positions[ChessAlphabet.b8.getRank()][ChessAlphabet.b8.getFile()] = new Square(blackKnight1);
        Knight blackKnight2 = new Knight("Knight", ChessAlphabet.g8, "black");
        positions[ChessAlphabet.g8.getRank()][ChessAlphabet.g8.getFile()] = new Square(blackKnight2);
        Queen whiteQueen = new Queen("Queen", ChessAlphabet.d1, "white");
        positions[ChessAlphabet.d1.getRank()][ChessAlphabet.d1.getFile()] = new Square(whiteQueen);
        Queen blackQueen = new Queen("Queen", ChessAlphabet.d8, "black");
        positions[ChessAlphabet.d8.getRank()][ChessAlphabet.d8.getFile()] = new Square(blackQueen);

        for (int i = 0; i < 8; i++) {
            positions[1][i] = new Square(new Pawn("Pawn", ChessAlphabet.getChessAlphabet(1, i), "white"));
            positions[6][i] = new Square(new Pawn("Pawn", ChessAlphabet.getChessAlphabet(6, i), "black"));

        }
        for (int i = 2; i < 6; i++) {
            for (int j = 0; j < 8; j++) {
                positions[i][j] = new Square();
            }
        }
    }

    public void loadGame(String filename) throws InvalidSyntaxException, InvalidMoveException {
        ChessGameReader(filename);
        String color;
        for (Move move : moves) {
            if (gameEnded) {
                System.out.println("Game already ended");
                return;
            }
            System.out.print(move.start);
              System.out.println(move.end);
            validateMove(move.start, move.end, move.promotion);
             printBoard();
             System.out.println();
            if (isCheckmate(turn % 2 == 0)) {
                gameEnded = true;
                if (turn % 2 == 0) {
                    color = "Black";
                } else {
                    color = "White";
                }
                System.out.println(color + " Won");
                continue;
            }
            if (isStalemate(turn % 2 == 0)) {
                gameEnded = true;
                System.out.println("Stalemate");
                continue;
            }
            if (drawByInsufficientMaterial()) {
                gameEnded = true;
                System.out.println("Insufficient Material");
                continue;
            }
            if (isSquareUnderAttack(wk.position, true)) {
                System.out.println("White in check");
            }
            if (isSquareUnderAttack(bk.position, false)) {
                System.out.println("Black in check");
            }
        }
    }

    public boolean isSquareUnderAttack(ChessAlphabet square, boolean isWhite) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (this.positions[i][j].piece != null && this.positions[i][j].piece.isWhite != isWhite
                        && this.positions[i][j].piece.isValidMove(this, ChessAlphabet.getChessAlphabet(i, j), square)) {
                    //pawn doesn't take moving forward
                    if (this.positions[i][j].piece instanceof Pawn && j == square.getFile()) {
                        return false;
                    } else {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void validateMove(ChessAlphabet start, ChessAlphabet end, char promotion) throws InvalidSyntaxException {
        int row = start.getRank();
        int column = start.getFile();
        int Drow = end.getRank();
        int Dcolumn = end.getFile();
        Piece startPiece = positions[row][column].piece;
        Piece endPiece = positions[Drow][Dcolumn].piece;
        if (!isValidMove(start, end)) {
            System.out.println("Invalid move");
            return;
        } else {
            if (startPiece instanceof King && ((King) startPiece).canCastle(this, start, end)) {
                System.out.println("Castle");
            }
            if (startPiece instanceof Pawn && ((Pawn) startPiece).isEnPassant(this, start, end)) {
                System.out.println("Enpassant");
            }
            movePiece(start, end);
        }
        if (startPiece instanceof Pawn && ((startPiece.isWhite && end.getRank() == 7) || (!startPiece.isWhite && end.getRank() == 0))) {
            ((Pawn) startPiece).promotePawn(this, end, promotion);
        }
        if (startPiece instanceof Pawn) {
            ((Pawn) startPiece).setHasMoved();
        } else if (startPiece instanceof Rook) {
            ((Rook) startPiece).setHasMoved();
        }
        if (startPiece instanceof King) {
            ((King) startPiece).setHasMoved();
        }
        turn += 1;
        if (endPiece != null) {
            System.out.println("Captured " + endPiece.type);
        }
    }

    public boolean isValidMove(ChessAlphabet start, ChessAlphabet end) {
        int row = start.getRank();
        int column = start.getFile();
        int Drow = end.getRank();
        int Dcolumn = end.getFile();
        int Cdirection = Dcolumn > column ? 1 : -1;
        Piece temp = null;
        boolean enPassantFlag = false;
        boolean castleFlag = false;
        //check we have a piece to move
        if (!positions[row][column].isOccupied) {
            return false;
        }
        Piece startPiece = positions[row][column].piece;
        ChessAlphabet position = startPiece.position;
        Piece endPiece = positions[Drow][Dcolumn].piece;
        //check is white turn and piece to move is white
        if ((turn % 2 == 0 && !startPiece.isWhite) || (turn % 2 != 0 && startPiece.isWhite)) {
            return false;
        }
        //checking for enPassant to be able to reverse it
        if (startPiece instanceof Pawn && ((Pawn) startPiece).isEnPassant(this, start, end)) {
            enPassantFlag = true;
            temp = positions[row][column + Cdirection].piece;
        }
        //checking for castle to be able to reverse it
        if (startPiece instanceof King && ((King) startPiece).canCastle(this, start, end)) {
            castleFlag = true;
            if (Cdirection == 1) {
                temp = positions[Drow][Dcolumn + Cdirection].piece;
            } else {
                temp = positions[Drow][Dcolumn + 2 * Cdirection].piece;
            }
        }
        //to avoid infinite loop caused by kings being in range of each other
        if (startPiece instanceof King) {
            if ((startPiece).isWhite && Math.abs(Dcolumn - bk.position.getFile()) <= 1 && Math.abs(Drow - bk.position.getRank()) <= 1) {
                return false;
            } else if (!((startPiece).isWhite) && Math.abs(Dcolumn - wk.position.getFile()) <= 1 && Math.abs(Drow - wk.position.getRank()) <= 1) {
                return false;
            }
        }
        //check if move is valid
        if (!startPiece.isValidMove(this, start, end)) {
            return false;
        } else {
            movePiece(start, end);
        }
        //check if after move there is a check
        if ((turn % 2 == 0 && isSquareUnderAttack(wk.position, true))
                || (turn % 2 == 1 && isSquareUnderAttack(bk.position, false))) {
            positions[row][column].piece = startPiece;
            positions[row][column].isOccupied = true;
            startPiece.position = position;
            if (endPiece == null) {
                positions[Drow][Dcolumn].clearSquare();
            } else {
                positions[Drow][Dcolumn].piece = endPiece;
            }
            return false;
        }
        positions[row][column].piece = startPiece;
        positions[row][column].isOccupied = true;
        if (endPiece == null) {
            positions[Drow][Dcolumn].clearSquare();
        } else {
            positions[Drow][Dcolumn].piece = endPiece;
        }
        if (enPassantFlag) {
            positions[row][column + Cdirection].piece = temp;
            positions[row][column + Cdirection].isOccupied = true;
        }
        if (castleFlag) {
            if (Cdirection == 1) {
                positions[Drow][Dcolumn + Cdirection].isOccupied = true;
                positions[Drow][Dcolumn + Cdirection].piece = temp;
                positions[Drow][Dcolumn + Cdirection].piece.position = ChessAlphabet.getChessAlphabet(Drow, Dcolumn + Cdirection);
                positions[Drow][Dcolumn - Cdirection].clearSquare();
            } else {
                positions[Drow][Dcolumn + 2 * Cdirection].isOccupied = true;
                positions[Drow][Dcolumn + 2 * Cdirection].piece = temp;
                positions[Drow][Dcolumn + 2 * Cdirection].piece.position = ChessAlphabet.getChessAlphabet(Drow, Dcolumn + 2 * Cdirection);
                positions[Drow][Dcolumn - Cdirection].clearSquare();
            }
        }
        startPiece.position = position;
        return true;
    }

    public void movePiece(ChessAlphabet start, ChessAlphabet end) {
        int row = start.getRank();
        int column = start.getFile();
        int Drow = end.getRank();
        int Dcolumn = end.getFile();
        int Cdirection = Dcolumn > column ? 1 : -1;

        Piece piece = positions[start.getRank()][start.getFile()].piece;

        //en Passant check
        if (piece instanceof Pawn && ((Pawn) piece).isEnPassant(this, start, end)) {
            positions[row][column + Cdirection].clearSquare();
        }
        //castle check
        if (piece instanceof King && ((King) piece).canCastle(this, start, end)) {
            if (Cdirection == 1) {
                positions[Drow][Dcolumn - Cdirection].piece = positions[Drow][Dcolumn + Cdirection].piece;
                positions[Drow][Dcolumn - Cdirection].isOccupied = true;
                positions[Drow][Dcolumn + Cdirection].clearSquare();
                positions[Drow][Dcolumn - Cdirection].piece.position = ChessAlphabet.getChessAlphabet(Drow, Dcolumn - Cdirection);
            } else {
                positions[Drow][Dcolumn - Cdirection].piece = positions[Drow][Dcolumn + 2 * Cdirection].piece;
                positions[Drow][Dcolumn - Cdirection].isOccupied = true;
                positions[Drow][Dcolumn + 2 * Cdirection].clearSquare();
                positions[Drow][Dcolumn - Cdirection].piece.position = ChessAlphabet.getChessAlphabet(Drow, Dcolumn - Cdirection);
            }
        }
        positions[Drow][Dcolumn].piece = piece;
        positions[Drow][Dcolumn].isOccupied = true;
        positions[row][column].clearSquare();
        piece.position = ChessAlphabet.getChessAlphabet(Drow, Dcolumn);
    }

    public List<Square> getAllValidMovesFromSquare(ChessAlphabet square) {
        List<Square> validMoves = new ArrayList<>();
        Piece piece = positions[square.getRank()][square.getFile()].piece;
        if ((turn % 2 == 0 && !piece.isWhite) || (turn % 2 != 0 && piece.isWhite)) {
            return validMoves;
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (isValidMove(square, ChessAlphabet.getChessAlphabet(i, j))) {
                    validMoves.add(positions[i][j]);
                }
            }
        }
        return validMoves;
    }

    public void ChessGameReader(String filename) throws InvalidSyntaxException {
        String temp;
        String[] str;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            while ((temp = reader.readLine()) != null) {
                if (temp.equals("")) {
                    continue;
                }
                str = temp.split(",");
                char startFile = str[0].charAt(0);
                char startRank = str[0].charAt(1);
                char endFile = str[1].charAt(0);
                char endRank = str[1].charAt(1);
                int rankS = Character.getNumericValue(startRank) - 1;
                int fileS = startFile - 'a';
                int rankE = Character.getNumericValue(endRank) - 1;
                int fileE = endFile - 'a';
                if (rankS > 7 || rankS < 0 || fileS > 7 || fileS < 0 || rankE > 7 || rankE < 0 || fileE > 7 || fileE < 0) {
                    throw new InvalidSyntaxException("Unrecognized move input");
                }
                switch (str.length) {
                    case 2 ->
                        moves.add(new Move(ChessAlphabet.values()[rankS * 8 + fileS], ChessAlphabet.values()[rankE * 8 + fileE]));
                    case 3 ->
                        moves.add(new Move(ChessAlphabet.values()[rankS * 8 + fileS], ChessAlphabet.values()[rankE * 8 + fileE], str[2].charAt(0)));
                    default ->
                        throw new InvalidSyntaxException("Unrecognized move input");
                }
            }
            reader.close();
        } catch (FileNotFoundException e1) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("File not found");
        }
    }

    private boolean isStalemate(boolean isWhite) {
        if ((getAllValidMovesFromSquare(wk.position).isEmpty() && !isSquareUnderAttack(wk.position, true))
                || ((getAllValidMovesFromSquare(bk.position).isEmpty()) && !isSquareUnderAttack(bk.position, false))) {
            return !isValidMoveForPlayer(isWhite);
        }
        return false;
    }

    public boolean isCheckmate(boolean isWhite) {
        if ((getAllValidMovesFromSquare(wk.position).isEmpty() && isSquareUnderAttack(wk.position, true))
                || (getAllValidMovesFromSquare(bk.position).isEmpty() && isSquareUnderAttack(bk.position, false))) {
            return !isValidMoveForPlayer(isWhite);
        }
        return false;
    }

    private boolean isValidMoveForPlayer(boolean isWhite) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = positions[i][j].piece;
                if (piece != null && piece.isWhite == isWhite) {
                    for (int Drow = 0; Drow < 8; Drow++) {
                        for (int Dcol = 0; Dcol < 8; Dcol++) {
                            if (isValidMove(ChessAlphabet.getChessAlphabet(i, j), ChessAlphabet.getChessAlphabet(Drow, Dcol))) {
                                // Found a legal move, not checkmate
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean drawByInsufficientMaterial() {
        int whiteLightPieceCount = 0, blackLightPieceCount = 0;
        //Light pieces are the bishop and the knight
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (positions[i][j].piece != null && positions[i][j].piece.isWhite) {
                    if (positions[i][j].piece instanceof Knight || positions[i][j].piece instanceof Bishop) {
                        whiteLightPieceCount++;
                    } else if (!(positions[i][j].piece instanceof King)) {
                        return false;
                    }
                }
                if (positions[i][j].piece != null && !positions[i][j].piece.isWhite) {
                    if (positions[i][j].piece instanceof Knight || positions[i][j].piece instanceof Bishop) {
                        blackLightPieceCount++;
                    } else if (!(positions[i][j].piece instanceof King)) {
                        return false;
                    }
                }
            }
        }
        return whiteLightPieceCount <= 1 && blackLightPieceCount <= 1;
    }

    private void printBoard() {
        for (int i = 7; i >= 0; i--) {
            for (int j = 0; j < 8; j++) {
                if (positions[i][j] == null || !positions[i][j].isOccupied) {
                    System.out.print("[ ]"); // indicate empty square
                } else {
                    char pieceSymbol = 0;
                    try {
                        pieceSymbol = getPieceSymbol(positions[i][j].piece);
                    } catch (InvalidSyntaxException ex) {
                        Logger.getLogger(ChessGame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    System.out.print("[" + pieceSymbol + "]");
                }
            }
            System.out.println(); // New line after each row
        }
    }

    private char getPieceSymbol(Piece piece) throws InvalidSyntaxException {
        // Returns a character representing the piece
        if (piece instanceof Pawn) {
            return piece.isWhite ? 'P' : 'p';
        } else if (piece instanceof Knight) {
            return piece.isWhite ? 'N' : 'n';
        } else if (piece instanceof Bishop) {
            return piece.isWhite ? 'B' : 'b';
        } else if (piece instanceof Rook) {
            return piece.isWhite ? 'R' : 'r';
        } else if (piece instanceof Queen) {
            return piece.isWhite ? 'Q' : 'q';
        } else if (piece instanceof King) {
            return piece.isWhite ? 'K' : 'k';
        }

        //Unrecognized syntax
        throw new InvalidSyntaxException("Invalid Syntax for pieces received in getPieceSymbol method");
    }

    public boolean isClearPath(ChessAlphabet start, ChessAlphabet end) {
        int row = start.getRank();
        int column = start.getFile();
        int Drow = end.getRank();
        int Dcolumn = end.getFile();
        int rowDirection = Drow > row ? 1 : (Drow < row ? -1 : 0);
        int columnDirection = Dcolumn > column ? 1 : (Dcolumn < column ? -1 : 0);
        int Crow = row + rowDirection;
        int Ccolumn = column + columnDirection;
        //iterating over the squares in the path to check if they are occupied
        while (Crow != Drow || Ccolumn != Dcolumn) {
            if (positions[Crow][Ccolumn].isOccupied) {
                return false;
            }
            Crow += rowDirection;
            Ccolumn += columnDirection;
        }
        return true;
    }
}
