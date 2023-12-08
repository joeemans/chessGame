
package backend;

/**
 *
 * @author youss
 */
public class Square implements Cloneable {
    boolean isOccupied;
     Piece piece;
     public ChessAlphabet location;
    public Piece getPiece() {
        return piece;
    }

    public Square(Piece piece){
        this.piece = piece;
        isOccupied = true;       
    }
    
    public Square(){
        piece=null;
        isOccupied=false;
    }
    
    public void clearSquare(){
        this.isOccupied = false;
        piece=null;
    }

    @Override
    protected Square clone() throws CloneNotSupportedException {
        Square clone;
        if (this.getPiece() !=null) {
            clone = new Square(this.piece.clone());
            clone.isOccupied = isOccupied;
        } else {
            clone = new Square();
        }
        clone.location = this.location;
        return clone;
    }
}
