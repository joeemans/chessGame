package backend;


public abstract class Piece implements Cloneable{

    private String type;
    private ChessAlphabet position;
    private final boolean isWhite;

    public boolean isWhite() {
        return isWhite;
    }

    public Piece(ChessAlphabet position, boolean isWhite) {
        setPosition(position);
        this.isWhite = isWhite;
    }

    public void setPosition(ChessAlphabet position) {
        this.position = position;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {this.type = type;}

    protected ChessAlphabet getPosition() {
        return position;
    }

    abstract boolean isValidMove(ChessGame board, ChessAlphabet start, ChessAlphabet end);

    @Override
    protected abstract Piece clone() throws CloneNotSupportedException;
}
