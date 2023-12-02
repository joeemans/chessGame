package frontend;

import backend.*;

import javax.swing.*;
import java.awt.*;

public class ChessBoard extends JFrame {
    private final Tile[][] tiles = new Tile[8][8];
    private final Square[][] squares;
    private Tile sourceTile;
    private Tile destinationTile;


    private static void fillMenuBar(JMenuBar menuBar) {
        JMenu fileMenu = new JMenu("File");
        JMenu gameMenu = new JMenu("Game");
        menuBar.add(fileMenu);
        menuBar.add(gameMenu);
    }

    public ChessBoard() {
        ChessGame activeGame = new ChessGame();
        squares = activeGame.positions;
        //we start here by creating our game in the backend, then creating the JFrame for our game
        setTitle("Chess");
        setSize(new Dimension(800, 800));
        setResizable(false);
        setLocationRelativeTo(null);
        setIconImage(new ImageIcon("chessIcon.jpg").getImage());
        //now our JFrame is all set to go, I want to create here a menu bar personally, so that is exactly what I will do
        JMenuBar menuBar = new JMenuBar();
        fillMenuBar(menuBar);
        setJMenuBar(menuBar);
        JPanel board = createBoardPanel();
        add(board);
        setVisible(true);
    }

    private JPanel createBoardPanel() {
        //Time to create our board rn
        JPanel board = new JPanel(new GridLayout(8, 8));
        board.setPreferredSize(new Dimension(600, 600));
        for (int i = 7; i >= 0; i--)
            for (int j = 0; j < 8; j++) {
                final Tile tile = new Tile(i, j);
                tiles[i][j] = tile;
                board.add(tile);
            }
        return board;
    }

    private class Tile extends JPanel {
        private final int rank;
        private final int file;
        private final Piece piece;

        public Tile(int rank, int file) {
            this.rank = rank;
            this.file = file;
            this.piece = squares[rank][file].getPiece();
            setPreferredSize(new Dimension(75, 75));
//        this.setLayout(new FlowLayout(FlowLayout.CENTER));
            assignTileColor();
            assignTileIcon();
            validate();
        }

        protected void assignTileIcon() {
            String pathName;
            this.removeAll();
            if (piece == null) {
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
            JLabel image = new JLabel(new ImageIcon(pathName + ".png"));
            this.add(image);
//        image.setVerticalAlignment(SwingConstants.CENTER);
//        image.setHorizontalAlignment(SwingConstants.CENTER);
        }

        private void assignTileColor() {
            if ((rank + file) % 2 == 0)
                setBackground(new Color(217, 218, 199));
            else
                setBackground(new Color(111, 171, 88));
        }

        public Piece getPiece() {
            return piece;
        }

    }
}
