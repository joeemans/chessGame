package backend;

import java.util.logging.Logger;

public abstract class Piece {

    String type;
    ChessAlphabet position;
    String color;
    boolean isWhite;

    public boolean isWhite() {
        return isWhite;
    }

    public Piece(String type, ChessAlphabet position, String color) {
        setType(type);
        setPosition(position);
        setColor(color);
        if (color.equals("white"))
            isWhite = true;
        else isWhite = false;
    }

    public String getColor() {
        return color;
    }

    private void setColor(String color) {
        this.color = color;
    }

    private void setType(String type) {
        this.type = type;
    }

    private void setPosition(ChessAlphabet position) {
        this.position = position;
    }

    public String getType() {
        return type;
    }

    public ChessAlphabet getPosition() {
        return position;
    }

    abstract boolean isValidMove(ChessGame board, ChessAlphabet start, ChessAlphabet end);

}
