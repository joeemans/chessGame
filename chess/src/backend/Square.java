
package backend;

/**
 *
 * @author youss
 */
public class Square {
    boolean isOccupied;
     Piece piece;
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
    
}
