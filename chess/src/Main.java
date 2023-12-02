import backend.ChessGame;
import backend.Square;
import javax.swing.*;
import java.awt.*;

public class Main {
    static Tile[][] tiles = new Tile [8][8];
     static Square[][] squares;

    public static void main(String[] args) {
        ChessGame activeGame = new ChessGame();
        squares = activeGame.positions;
        //we start here by creating our JFrame for the main game, this is an alternative method to extending JFrame by a subclass
        JFrame chessScreen = new JFrame("Chess");
        chessScreen.setSize(new Dimension(800,800));
        chessScreen.setResizable(false);
        chessScreen.setLocationRelativeTo(null);
        chessScreen.setIconImage(new ImageIcon("chessIcon.jpg").getImage());
        //now our JFrame is all set to go, I want to create here a menu bar personally, so that is exactly what I will do
        JMenuBar menuBar = new JMenuBar();
        fillMenuBar(menuBar);
        chessScreen.setJMenuBar(menuBar);
        JPanel board = createBoardPanel();
        chessScreen.add(board);
        chessScreen.setVisible(true);
    }



    private static JPanel  createBoardPanel() {
        //Time to create our board rn
        JPanel board = new JPanel(new GridLayout(8,8));
        board.setPreferredSize(new Dimension(600,600));
        for (int i =7 ; i>=0 ; i--)
            for (int j=0 ; j<8 ; j++) {
                final Tile tile = new Tile(i, j);
                tiles[i][j] = tile;
                board.add(tile);
            }
        return board;
    }

    private static void fillMenuBar(JMenuBar menuBar) {
        JMenu fileMenu = new JMenu("File");
        JMenu gameMenu = new JMenu("Game");
        menuBar.add(fileMenu);
        menuBar.add(gameMenu);
    }
}