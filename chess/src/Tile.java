import backend.*;

import javax.swing.*;
import java.awt.*;

public class Tile extends JPanel {
    private final int rank;
    private final int file;
    private final Piece piece;
    public Tile (int rank,int file){
        this.rank = rank;
        this.file = file;
        this.piece = Main.squares[rank][file].getPiece();
        setPreferredSize(new Dimension(75,75));
//        this.setLayout(new FlowLayout(FlowLayout.CENTER));
        assignTileColor();
        assignTileIcon();
        validate();
    }

    protected void assignTileIcon() {
        String pathName;
        this.removeAll();
        if (piece == null){
            return;
        }
        if (piece.isWhite())
            pathName = "W";
        else pathName = "B";
        if (piece instanceof Pawn)
            pathName += "P";
        else if (piece instanceof Rook)
            pathName += "R";
        else if (piece instanceof Queen)
            pathName += "Q";
        else if (piece instanceof King)
            pathName += "K";
        else if (piece instanceof Knight)
            pathName += "N";
        else if (piece instanceof Bishop)
            pathName += "B";
        JLabel image = new JLabel(new ImageIcon(pathName + ".gif"));
        this.add(image);
//        image.setVerticalAlignment(SwingConstants.CENTER);
//        image.setHorizontalAlignment(SwingConstants.CENTER);
        }

    private void assignTileColor(){
        if((rank+file) % 2 == 0)
            setBackground(new Color(217,218,199));
        else
            setBackground(new Color(111,171,88));
    }

    public Piece getPiece() {
        return piece;
    }

}
