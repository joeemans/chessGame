/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backend;

/**
 *
 * @author youss
 */
public class Move {

    ChessAlphabet start;
    ChessAlphabet end;
    char promotion;

    public Move(ChessAlphabet start, ChessAlphabet end) {
        this.start = start;
        this.end = end;
    }

    public Move(ChessAlphabet start, ChessAlphabet end, char promotion) {
        this.start = start;
        this.end = end;
        this.promotion = promotion;
    }
}
