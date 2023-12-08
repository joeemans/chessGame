package backend;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ChessGame {

    public Square[][] positions = new Square[8][8];
    public int turn = 0;
    private King whiteKing;
    private King blackKing;
    ArrayList<Move> moves = new ArrayList<>();
    private boolean gameEnded = false;
    private static ChessGame gameInstance;
    private final Stack <ChessGameMemento> gameStates = new Stack<>();
    private final ArrayList <GameObserver> gameObservers = new ArrayList<>();
    private ChessAlphabet checkedKingLocation;

    //here we applied the singleton design pattern to ensure that all times, only one instance of the game exists
    public static ChessGame getGameInstance(){
        if (gameInstance == null) {
            synchronized (ChessGame.class) {
                if (gameInstance == null) {
                    gameInstance = new ChessGame();
                }
            }
        }
        return gameInstance;
    }

    private ChessGame() {
        initializeGame();
    }

    public static synchronized void cleanup() {
        if (gameInstance != null)
        {
            gameInstance = null;
        }
    }

    public void addObserver (GameObserver observer){
        gameObservers.add(observer);
    }
    public void removeObserver (GameObserver observer){
        gameObservers.remove(observer);
    }
    private void notifyCheck (ChessAlphabet position){
        for (GameObserver observer : gameObservers){
            observer.check(position);
        }
        checkedKingLocation = position;
    }
    private void notifyRemoveCheck (ChessAlphabet position){
        for (GameObserver observer : gameObservers){
            observer.removeCheck(position);
        }
        checkedKingLocation = null;
    }
    private void notifyCheckmate(){
        for (GameObserver observer : gameObservers){
            observer.checkmate();
        }
    }

    private void notifyStalemate(){
        for (GameObserver observer : gameObservers){
            observer.stalemate();
        }
    }

    private void notifyDrawByInsufficientMaterial(){
        for (GameObserver observer : gameObservers){
            observer.drawByInsufficientMaterial();
        }
    }
    private void notifySquaresChanged(ArrayList<ChessAlphabet> changedSquares){
        for (GameObserver observer : gameObservers){
            observer.squaresChanged(changedSquares);
        }
    }
    private void initializeGame() {
        whiteKing = (King) PieceFactory.createKing(ChessAlphabet.e1,true);
        positions[ChessAlphabet.e1.getRank()][ChessAlphabet.e1.getFile()] = new Square(whiteKing);
        blackKing = (King) PieceFactory.createKing(ChessAlphabet.e8,false);
        positions[ChessAlphabet.e8.getRank()][ChessAlphabet.e8.getFile()] = new Square(blackKing);
        positions[ChessAlphabet.a1.getRank()][ChessAlphabet.a1.getFile()] = new Square(PieceFactory.createRook(ChessAlphabet.a1,true));
        positions[ChessAlphabet.h1.getRank()][ChessAlphabet.h1.getFile()] = new Square(PieceFactory.createRook(ChessAlphabet.h1,true));
        positions[ChessAlphabet.a8.getRank()][ChessAlphabet.a8.getFile()] = new Square(PieceFactory.createRook(ChessAlphabet.a8,false));
        positions[ChessAlphabet.h8.getRank()][ChessAlphabet.h8.getFile()] = new Square(PieceFactory.createRook(ChessAlphabet.h8,false));
        positions[ChessAlphabet.c1.getRank()][ChessAlphabet.c1.getFile()] = new Square(PieceFactory.createBishop(ChessAlphabet.c1,true));
        positions[ChessAlphabet.f1.getRank()][ChessAlphabet.f1.getFile()] = new Square(PieceFactory.createBishop(ChessAlphabet.f1,true));
        positions[ChessAlphabet.c8.getRank()][ChessAlphabet.c8.getFile()] = new Square(PieceFactory.createBishop(ChessAlphabet.c8,false));
        positions[ChessAlphabet.f8.getRank()][ChessAlphabet.f8.getFile()] = new Square(PieceFactory.createBishop(ChessAlphabet.f8,false));
        positions[ChessAlphabet.b1.getRank()][ChessAlphabet.b1.getFile()] = new Square(PieceFactory.createKnight(ChessAlphabet.b1,true));
        positions[ChessAlphabet.g1.getRank()][ChessAlphabet.g1.getFile()] = new Square(PieceFactory.createKnight(ChessAlphabet.g1,true));
        positions[ChessAlphabet.b8.getRank()][ChessAlphabet.b8.getFile()] = new Square(PieceFactory.createKnight(ChessAlphabet.b8,false));
        positions[ChessAlphabet.g8.getRank()][ChessAlphabet.g8.getFile()] = new Square(PieceFactory.createKnight(ChessAlphabet.g8,false));
        positions[ChessAlphabet.d1.getRank()][ChessAlphabet.d1.getFile()] = new Square(PieceFactory.createQueen(ChessAlphabet.d1,true));
        positions[ChessAlphabet.d8.getRank()][ChessAlphabet.d8.getFile()] = new Square(PieceFactory.createQueen(ChessAlphabet.d8,false));

        for (int i = 0; i < 8; i++) {
            positions[1][i] = new Square(PieceFactory.createPawn(ChessAlphabet.getChessAlphabet(1, i),true));
            positions[6][i] = new Square(PieceFactory.createPawn(ChessAlphabet.getChessAlphabet(6, i),false));
        }
        for (int i = 0; i <8; i++) {
            for (int j = 0; j < 8; j++) {
                if (positions[i][j] == null)
                   positions[i][j] = new Square();
                positions[i][j].location = ChessAlphabet.getChessAlphabet(i,j);
            }
        }
        saveState();
    }

    private void saveState(){
        gameStates.push(new ChessGameMemento(positions,turn,gameEnded));
    }

    private void retrieveFromMemento(ChessGameMemento memento){
        this.turn = memento.getTurn();
        this.gameEnded = memento.GameHasEnded();
        for (int i=0 ; i<8 ; i++)
            for (int j=0; j<8 ; j++){
                try {
                    positions[i][j] = memento.getSavedBoardState()[i][j].clone();
                } catch (CloneNotSupportedException e) {
                    throw new RuntimeException(e);
                }
            }
        blackKing = findKing(false);
        whiteKing = findKing(true);
        kingInCheck();
        moves.remove(moves.size()-1);
    }

    private King findKing(Boolean isWhite) {
        for (int i=0; i<8 ; i++)
            for (int j=0 ; j<8 ; j++){
                if (positions[i][j].piece != null && positions[i][j].piece instanceof King
                        && isWhite == positions[i][j].piece.isWhite()){
                    return (King) positions[i][j].piece;
                }
            }
        return null;
    }

    public boolean undoMove(){
        if (gameStates.size()>1){
            gameStates.pop();
            retrieveFromMemento(gameStates.peek());
            return true;
        }
        return false;
    }

    private void kingInCheck(){
        if (checkedKingLocation != null){
            notifyRemoveCheck(checkedKingLocation);
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = positions[i][j].piece;
                if (piece != null && piece.isValidMove(this,ChessAlphabet.getChessAlphabet(i,j),whiteKing.getPosition())) {
                    notifyCheck(whiteKing.getPosition());
                    return;
                }
                else if (piece != null && piece.isValidMove(this,ChessAlphabet.getChessAlphabet(i,j),blackKing.getPosition())){
                    notifyCheck(blackKing.getPosition());
                    return;
                }
            }
        }
    }

//    public void loadGame(String filename) throws InvalidSyntaxException, InvalidMoveException {
//        ChessGameReader(filename);
//        String color;
//        for (Move move : moves) {
//            if (gameEnded) {
//                System.out.println("Game already ended");
//                return;
//            }
//            System.out.print(move.start);
//              System.out.println(move.end);
//            validateMove(move.start, move.end, move.promotion);
////             printBoard();
//             System.out.println();
//            if (isCheckmate(turn % 2 == 0)) {
//                gameEnded = true;
//                if (turn % 2 == 0) {
//                    color = "Black";
//                } else {
//                    color = "White";
//                }
//                System.out.println(color + " Won");
//                continue;
//            }
//            if (isStalemate(turn % 2 == 0)) {
//                gameEnded = true;
//                System.out.println("Stalemate");
//                continue;
//            }
//            if (drawByInsufficientMaterial()) {
//                gameEnded = true;
//                System.out.println("Insufficient Material");
//                continue;
//            }
//            if (isSquareUnderAttack(whiteKing.getPosition(), true)) {
//                System.out.println("White in check");
//            }
//            if (isSquareUnderAttack(blackKing.getPosition(), false)) {
//                System.out.println("Black in check");
//            }
//        }
//    }

    protected boolean isSquareUnderAttack(ChessAlphabet square, boolean isWhite) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (this.positions[i][j].piece != null && this.positions[i][j].piece.isWhite() != isWhite
                        && this.positions[i][j].piece.isValidMove(this, ChessAlphabet.getChessAlphabet(i, j), square)) {
                    //pawn doesn't take moving forward
                    return !(this.positions[i][j].piece instanceof Pawn) || j != square.getFile();
                }
            }
        }
        return false;
    }

    public void validateMove(Move move) throws InvalidMoveException, InvalidSyntaxException {
        ChessAlphabet start = move.getStart();
        ChessAlphabet end = move.getEnd();
        int row = start.getRank();
        int column = start.getFile();
        int Drow = end.getRank();
        int Dcolumn = end.getFile();
        int Cdirection = Dcolumn > column ? 1 : -1;
        Piece startPiece = positions[row][column].piece;
        ArrayList <ChessAlphabet> changedSquares = new ArrayList<>();
        changedSquares.add(start);
        changedSquares.add(end);
        if (!isValidMove(move)) {
            throw new InvalidMoveException("Invalid Move");
        } else {
            movePiece(start, end);
        }
        if (startPiece instanceof Pawn && ((startPiece.isWhite() && end.getRank() == 7) || (!startPiece.isWhite() && end.getRank() == 0))) {
            char promotion = move.getPromotion();
            ((Pawn) startPiece).promotePawn(this, end, promotion);
        }
        if(move.isCastle()){
            ChessAlphabet rookSquare;
            if(Dcolumn>column) {
                rookSquare = positions[Drow][Dcolumn + Cdirection].location;
            }
            else {
                rookSquare = positions[Drow][Dcolumn + 2 * Cdirection].location;
            }
            changedSquares.add(rookSquare);
            changedSquares.add(positions[Drow][Dcolumn-Cdirection].location);
        }
        if (move.isEnPassant()){
            changedSquares.add(positions[row][Dcolumn].location);
        }
        if (startPiece instanceof Pawn) {
            ((Pawn) startPiece).setHasMoved();
        } else if (startPiece instanceof Rook) {
            ((Rook) startPiece).setHasMoved();
        }
        if (startPiece instanceof King) {
            ((King) startPiece).setHasMoved();
        }
        moves.add(move);
        notifySquaresChanged(changedSquares);
        turn += 1;
        saveState();
        if(isCheckmate(turn % 2 ==0)){
            notifyCheckmate();
            return;
        }
        if(isStalemate(turn % 2 ==0)){
            notifyStalemate();
            return;
        }
        if(drawByInsufficientMaterial()){
            notifyDrawByInsufficientMaterial();
            return;
        }
        kingInCheck();
    }

    public boolean isValidMove(Move move) {
        ChessAlphabet start = move.getStart();
        ChessAlphabet end = move.getEnd();
        int row = start.getRank();
        int column = start.getFile();
        int Drow = end.getRank();
        int Dcolumn = end.getFile();
        int Cdirection = Dcolumn > column ? 1 : -1;
        Piece temp = null;
        //check we have a piece to move
        if (!positions[row][column].isOccupied) {
            return false;
        }
        Piece startPiece = positions[row][column].piece;
        ChessAlphabet position = startPiece.getPosition();
        Piece endPiece = positions[Drow][Dcolumn].piece;
        //check is white turn and piece to move is white
        if ((turn % 2 == 0 && !startPiece.isWhite()) || (turn % 2 != 0 && startPiece.isWhite())) {
            return false;
        }
        //checking for enPassant to be able to reverse it
        if (move.isEnPassant()) {
            temp = positions[row][column + Cdirection].piece;
        }
        //checking for castle to be able to reverse it
        if (move.isCastle()) {
            if (Cdirection == 1) {
                temp = positions[Drow][Dcolumn + Cdirection].piece;
            } else {
                temp = positions[Drow][Dcolumn + 2 * Cdirection].piece;
            }
        }
        //to avoid infinite loop caused by kings being in range of each other
        if (startPiece instanceof King) {
            if ((startPiece).isWhite() && Math.abs(Dcolumn - blackKing.getPosition().getFile()) <= 1 && Math.abs(Drow - blackKing.getPosition().getRank()) <= 1) {
                return false;
            } else if (!((startPiece).isWhite()) && Math.abs(Dcolumn - whiteKing.getPosition().getFile()) <= 1 && Math.abs(Drow - whiteKing.getPosition().getRank()) <= 1) {
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
        if ((turn % 2 == 0 && isSquareUnderAttack(whiteKing.getPosition(), true))
                || (turn % 2 == 1 && isSquareUnderAttack(blackKing.getPosition(), false))) {
            positions[row][column].piece = startPiece;
            positions[row][column].isOccupied = true;
            startPiece.setPosition(position);
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
        if (move.isEnPassant()) {
            positions[row][column + Cdirection].piece = temp;
            positions[row][column + Cdirection].isOccupied = true;
        }
        if (move.isCastle()) {
            if (Cdirection == 1) {
                positions[Drow][Dcolumn + Cdirection].isOccupied = true;
                positions[Drow][Dcolumn + Cdirection].piece = temp;
                positions[Drow][Dcolumn + Cdirection].piece.setPosition(ChessAlphabet.getChessAlphabet(Drow, Dcolumn + Cdirection));
                positions[Drow][Dcolumn - Cdirection].clearSquare();
            } else {
                positions[Drow][Dcolumn + 2 * Cdirection].isOccupied = true;
                positions[Drow][Dcolumn + 2 * Cdirection].piece = temp;
                positions[Drow][Dcolumn + 2 * Cdirection].piece.setPosition(ChessAlphabet.getChessAlphabet(Drow, Dcolumn + 2 * Cdirection));
                positions[Drow][Dcolumn - Cdirection].clearSquare();
            }
        }
        startPiece.setPosition(position);
        return true;
    }

    private void movePiece(ChessAlphabet start, ChessAlphabet end) {
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
                positions[Drow][Dcolumn - Cdirection].piece.setPosition(ChessAlphabet.getChessAlphabet(Drow, Dcolumn - Cdirection));
            } else {
                positions[Drow][Dcolumn - Cdirection].piece = positions[Drow][Dcolumn + 2 * Cdirection].piece;
                positions[Drow][Dcolumn - Cdirection].isOccupied = true;
                positions[Drow][Dcolumn + 2 * Cdirection].clearSquare();
                positions[Drow][Dcolumn - Cdirection].piece.setPosition(ChessAlphabet.getChessAlphabet(Drow, Dcolumn - Cdirection));
            }
        }
        positions[Drow][Dcolumn].piece = piece;
        positions[Drow][Dcolumn].isOccupied = true;
        positions[row][column].clearSquare();
        piece.setPosition(ChessAlphabet.getChessAlphabet(Drow, Dcolumn));
    }

    public List<Square> getAllValidMovesFromSquare(ChessAlphabet square) {
        List<Square> validMoves = new ArrayList<>();
        Piece piece = positions[square.getRank()][square.getFile()].piece;
        if (piece == null)
            return validMoves;
        if ((turn % 2 == 0 && !piece.isWhite()) || (turn % 2 != 0 && piece.isWhite())) {
            return validMoves;
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (isValidMove(new Move(square, ChessAlphabet.getChessAlphabet(i, j) , '0'))) {
                    validMoves.add(positions[i][j]);
                }
            }
        }
        return validMoves;
    }

//    public void ChessGameReader(String filename) throws InvalidSyntaxException {
//        String temp;
//        String[] str;
//        try {
//            BufferedReader reader = new BufferedReader(new FileReader(filename));
//            while ((temp = reader.readLine()) != null) {
//                if (temp.isEmpty()) {
//                    continue;
//                }
//                str = temp.split(",");
//                char startFile = str[0].charAt(0);
//                char startRank = str[0].charAt(1);
//                char endFile = str[1].charAt(0);
//                char endRank = str[1].charAt(1);
//                int rankS = Character.getNumericValue(startRank) - 1;
//                int fileS = startFile - 'a';
//                int rankE = Character.getNumericValue(endRank) - 1;
//                int fileE = endFile - 'a';
//                if (rankS > 7 || rankS < 0 || fileS > 7 || fileS < 0 || rankE > 7 || rankE < 0 || fileE > 7 || fileE < 0) {
//                    throw new InvalidSyntaxException("Unrecognized move input");
//                }
//                switch (str.length) {
//                    case 2 ->
//                        moves.add(new Move(ChessAlphabet.values()[rankS * 8 + fileS], ChessAlphabet.values()[rankE * 8 + fileE],'0'));
//                    case 3 ->
//                        moves.add(new Move(ChessAlphabet.values()[rankS * 8 + fileS], ChessAlphabet.values()[rankE * 8 + fileE], str[2].charAt(0)));
//                    default ->
//                        throw new InvalidSyntaxException("Unrecognized move input");
//                }
//            }
//            reader.close();
//        } catch (FileNotFoundException e1) {
//            System.out.println("File not found");
//        } catch (IOException e) {
//            System.out.println("File not found");
//        }
//    }

    public boolean isStalemate(boolean isWhite) {
        if ((getAllValidMovesFromSquare(whiteKing.getPosition()).isEmpty() && !isSquareUnderAttack(whiteKing.getPosition(), true))
                || ((getAllValidMovesFromSquare(blackKing.getPosition()).isEmpty()) && !isSquareUnderAttack(blackKing.getPosition(), false))) {
            return !isValidMoveForPlayer(isWhite);
        }
        return false;
    }

    public boolean isCheckmate(boolean isWhite) {
        if ((getAllValidMovesFromSquare(whiteKing.getPosition()).isEmpty() && isSquareUnderAttack(whiteKing.getPosition(), true))
                || (getAllValidMovesFromSquare(blackKing.getPosition()).isEmpty() && isSquareUnderAttack(blackKing.getPosition(), false))) {
            return !isValidMoveForPlayer(isWhite);
        }
        return false;
    }

    private boolean isValidMoveForPlayer(boolean isWhite) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = positions[i][j].piece;
                if (piece != null && piece.isWhite() == isWhite) {
                    for (int Drow = 0; Drow < 8; Drow++) {
                        for (int Dcol = 0; Dcol < 8; Dcol++) {
                            if (isValidMove(new Move(ChessAlphabet.getChessAlphabet(i, j), ChessAlphabet.getChessAlphabet(Drow, Dcol),'0'))) {
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
                if (positions[i][j].piece != null && positions[i][j].piece.isWhite()) {
                    if (positions[i][j].piece instanceof Knight || positions[i][j].piece instanceof Bishop) {
                        whiteLightPieceCount++;
                    } else if (!(positions[i][j].piece instanceof King)) {
                        return false;
                    }
                }
                if (positions[i][j].piece != null && !positions[i][j].piece.isWhite()) {
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

//    private void printBoard() {
//        for (int i = 7; i >= 0; i--) {
//            for (int j = 0; j < 8; j++) {
//                if (positions[i][j] == null || !positions[i][j].isOccupied) {
//                    System.out.print("[ ]"); // indicate empty square
//                } else {
//                    char pieceSymbol = 0;
//                    try {
//                        pieceSymbol = getPieceSymbol(positions[i][j].piece);
//                    } catch (InvalidSyntaxException ex) {
//                        Logger.getLogger(ChessGame.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                    System.out.print("[" + pieceSymbol + "]");
//                }
//            }
//            System.out.println(); // New line after each row
//        }
//    }

//    private char getPieceSymbol(Piece piece) throws InvalidSyntaxException {
//        // Returns a character representing the piece
//        if (piece instanceof Pawn) {
//            return piece.isWhite ? 'P' : 'p';
//        } else if (piece instanceof Knight) {
//            return piece.isWhite ? 'N' : 'n';
//        } else if (piece instanceof Bishop) {
//            return piece.isWhite ? 'B' : 'b';
//        } else if (piece instanceof Rook) {
//            return piece.isWhite ? 'R' : 'r';
//        } else if (piece instanceof Queen) {
//            return piece.isWhite ? 'Q' : 'q';
//        } else if (piece instanceof King) {
//            return piece.isWhite ? 'K' : 'k';
//        }
//
//        //Unrecognized syntax
//        throw new InvalidSyntaxException("Invalid Syntax for pieces received in getPieceSymbol method");
//    }

    protected boolean isClearPath(ChessAlphabet start, ChessAlphabet end) {
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
