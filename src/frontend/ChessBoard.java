package frontend;

import backend.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import static javax.swing.SwingUtilities.isLeftMouseButton;

 class ChessBoard extends JFrame implements GameObserver {
    private final Tile[][] tiles = new Tile[8][8];
    private static Tile sourceTile;
    private static Tile destinationTile;
    private static ChessGame activeGame;
    private List <Square> possibleMoves;
    private final JPanel board;
    private boolean whiteTurn = true;
    private static ChessAlphabet checkedKingLocation;
    private static boolean flippingEnabled = true;
    private final JFrame parent;


    private void fillMenuBar(JMenuBar menuBar) {
        JMenu fileMenu = new JMenu("File");
        JMenu gameMenu = new JMenu("Game");
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Exit application
                System.exit(0);
            }
        });
        fileMenu.add(exitMenuItem);
        //Options for enabling and disabling board flipping
        JMenuItem enableFlippingItem = new JMenuItem("Enable Flipping");
        JMenuItem disableFlippingItem = new JMenuItem("Disable Flipping");
        JMenuItem undoMenu=new JMenuItem("Undo");

        // Add action listeners to handle item clicked
        enableFlippingItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Handle enable flipping action
                flippingEnabled = true;
            }
        });

        disableFlippingItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Handle disable flipping action
                flippingEnabled = false;
            }
        });
        undoMenu.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e1) {
                if (activeGame.undoMove()) {
                    for (int i = 0; i < 8; i++)
                        for (int j = 0; j < 8; j++) {
                            tiles[i][j].assignTileIcon();
                            tiles[i][j].repaint();
                            tiles[i][j].revalidate();
                        }
                    if (flippingEnabled)
                        flipBoard(board);
                    whiteTurn = !whiteTurn;
                }
                removeHighlightedMoves();
            }
        });
        gameMenu.add(enableFlippingItem);
        gameMenu.add(disableFlippingItem);
        gameMenu.add(undoMenu);
        menuBar.add(fileMenu);
        menuBar.add(gameMenu);
    }

    public ChessBoard(JFrame startMenu) {
        activeGame = ChessGame.getGameInstance();
        activeGame.addObserver(this);
        parent = startMenu;
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
        //then im going to create the board
        board = createBoardPanel();
        add(board);
       setVisible(true);
    }

    private void flipBoard(JPanel board){
        if (whiteTurn) {
            for (int i = 0; i < 8; i++)
                for (int j = 0; j < 8; j++) {
                    board.add(tiles[i][j]);
                    validate();
                }
        }else {
            for (int i = 7; i >= 0; i--)
                for (int j = 0; j < 8; j++) {
                    board.add(tiles[i][j]);
                    validate();
                }
        }
    }

    private void highlightPossibleMoves(){
        possibleMoves = activeGame.getAllValidMovesFromSquare(ChessAlphabet.getChessAlphabet(
                sourceTile.rank, sourceTile.file));
        for (Square square : possibleMoves){
            tiles[square.location.getRank()][square.location.getFile()].highlighted = true;
            tiles[square.location.getRank()][square.location.getFile()].repaint();
        }
    }

//    private void checkGameStatus(){
//        if (activeGame.isCheckmate(!whiteTurn)){
//            String player = whiteTurn ? "White" : "Black";
//            removeHighlightedMoves();
//            JOptionPane.showMessageDialog(null, player + " wins", "Checkmate",JOptionPane.INFORMATION_MESSAGE);
//            this.setVisible(false);
//            ChessGame.cleanup();
//            parent.setVisible(true);
//        }
//        else if (activeGame.isStalemate(!whiteTurn)){
//            removeHighlightedMoves();
//            JOptionPane.showMessageDialog(null, "A Stalemate has been reached.", "Stalemate",JOptionPane.INFORMATION_MESSAGE);
//            this.setVisible(false);
//            ChessGame.cleanup();
//            parent.setVisible(true);
//        }
//        else if (activeGame.drawByInsufficientMaterial())
//        {
//            removeHighlightedMoves();
//            JOptionPane.showMessageDialog(null, "Victory is now unachievable.", "Draw by insufficient material",JOptionPane.INFORMATION_MESSAGE);
//            this.setVisible(false);
//            ChessGame.cleanup();
//            parent.setVisible(true);
//        }
//    }

    private void removeHighlightedMoves() {
        for (Square square : possibleMoves){
            tiles[square.location.getRank()][square.location.getFile()].highlighted = false;
            tiles[square.location.getRank()][square.location.getFile()].repaint();
        }
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

     @Override
     public void check(ChessAlphabet position) {
         tiles[position.getRank()][position.getFile()].setBackground(Color.RED);
         tiles[position.getRank()][position.getFile()].revalidate();
     }

     @Override
     public void checkmate() {
         String player = whiteTurn ? "White" : "Black";
         removeHighlightedMoves();
         JOptionPane.showMessageDialog(null, player + " wins", "Checkmate",JOptionPane.INFORMATION_MESSAGE);
         this.setVisible(false);
         ChessGame.cleanup();
         parent.setVisible(true);
     }

     @Override
     public void stalemate() {
         removeHighlightedMoves();
         JOptionPane.showMessageDialog(null, "A Stalemate has been reached.", "Stalemate",JOptionPane.INFORMATION_MESSAGE);
         this.setVisible(false);
         ChessGame.cleanup();
         parent.setVisible(true);
     }

     @Override
     public void drawByInsufficientMaterial() {
         removeHighlightedMoves();
         JOptionPane.showMessageDialog(null, "Victory is now unachievable.", "Draw by insufficient material",JOptionPane.INFORMATION_MESSAGE);
         this.setVisible(false);
         ChessGame.cleanup();
         parent.setVisible(true);
     }

     @Override
     public void squaresChanged(ArrayList<ChessAlphabet> changedSquares) {
        for (ChessAlphabet changedsquare : changedSquares){
            tiles[changedsquare.getRank()][changedsquare.getFile()].assignTileIcon();
            tiles[changedsquare.getRank()][changedsquare.getFile()].repaint();
            tiles[changedsquare.getRank()][changedsquare.getFile()].revalidate();
        }
     }

     @Override
     public void removeCheck(ChessAlphabet position) {
         tiles[position.getRank()][position.getFile()].assignTileColor();
         tiles[position.getRank()][position.getFile()].repaint();
         tiles[position.getRank()][position.getFile()].revalidate();
     }

     private class Tile extends JPanel {
        private final int rank;
        private final int file;
        private Piece piece;
        private boolean highlighted;

        public Tile(int rank, int file) {
            this.rank = rank;
            this.file = file;
            setPreferredSize(new Dimension(75, 75));
            assignTileColor();
            assignTileIcon();
            validate();
            this.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (isLeftMouseButton(e)) {
                        if (sourceTile == null) {
                            // this means this is the first mouse click
                            sourceTile = Tile.this;
                            highlightPossibleMoves();
                        } else if (Tile.this == sourceTile) {
                            // same tile selected twice
                            sourceTile = null;
                            removeHighlightedMoves();
                        } else {
                            destinationTile = Tile.this;
                            //we are going to try the move
                            try {
                                Move attemptedMove = new Move(ChessAlphabet.getChessAlphabet(sourceTile.rank, sourceTile.file),
                                        ChessAlphabet.getChessAlphabet(destinationTile.rank,destinationTile.file),'0');
                                if (sourceTile.getPiece() instanceof Pawn &&
                                        activeGame.isValidMove(attemptedMove) &&
                                        ((sourceTile.getPiece().isWhite() && destinationTile.rank == 7)
                                        || (!sourceTile.getPiece().isWhite() && destinationTile.rank == 0))) {
                                    String[] options = {"Queen", "Bishop", "Rook", "Knight"};
                                    ImageIcon icon = new ImageIcon("PP.png");
                                    int choice = JOptionPane.showOptionDialog(null,
                                            "Choose a chess piece to promote to:", "Promotion",
                                            JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                                            icon, options, options[0]
                                    );
                                    if (choice >= 0 && choice < options.length) {
                                        attemptedMove.setPromotion(options[choice].charAt(0));
                                    }
                                }
                                Move.MoveExecutor.executeCommand(attemptedMove);
                                if(flippingEnabled) {
                                    flipBoard(board);
                                }
                                whiteTurn = !whiteTurn;
                            } catch (InvalidSyntaxException ex) {
                                //do nothing
                            } catch (InvalidMoveException ex) {
                                //do nothing
                            } finally {
                                sourceTile = null;
                                destinationTile = null;
                                removeHighlightedMoves();
                            }
                        }
                    }
                    else {
                        sourceTile = null;
                        destinationTile = null;
                        removeHighlightedMoves();
                    }
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (highlighted) {
                // Draws a gray circle
                int centerX = getWidth() / 2;
                int centerY = getHeight() / 2;
                int radius = 25;
                g.setColor(Color.gray);
                g.fillOval(centerX - radius, centerY - radius, 2 * radius, 2 * radius);
            }
        }

        private void assignTileIcon() {
            String pathName;
            this.removeAll();
            this.piece = activeGame.positions[rank][file].getPiece();
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
        }

        private void assignTileColor() {
            if ((rank + file) % 2 != 0)
                setBackground(new Color(217, 218, 199));
            else
                setBackground(new Color(111, 171, 88));
        }

        public Piece getPiece() {
            return piece;
        }

    }
}
