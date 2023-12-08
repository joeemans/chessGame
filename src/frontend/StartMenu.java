package frontend;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.lang.System.exit;


public class StartMenu extends JFrame {
    private final JButton gameButton;
    private final JButton exitButton;
    private final JLabel background;

    public StartMenu(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 350);
        setLocationRelativeTo(null);
        setIconImage(new ImageIcon("chessIcon.jpg").getImage());
        ImageIcon image = new ImageIcon("startBackground.jpg");
        setTitle("Home Screen");
        background = new JLabel(image);
        gameButton = new JButton("New Game");
        exitButton = new JButton("Exit");
        gameButton.setBounds(200,100,100,50);
        exitButton.setBounds(200,200,100,50);
        gameButton.setBackground(Color.gray);
        exitButton.setBackground(Color.gray);
        background.add(gameButton);
        background.add(exitButton);
        background.setSize(500,350);
        //handling button clicked
        gameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ChessBoard chessBoard = new ChessBoard(StartMenu.this);
                setVisible(false);
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exit(-5);
            }
        });
        add(background);
        setVisible(true);
    }
}

