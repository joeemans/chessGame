package backend;

public enum ChessAlphabet {
    a1(0, 0), b1(0, 1), c1(0, 2), d1(0, 3), e1(0, 4), f1(0, 5), g1(0, 6), h1(0, 7),
    a2(1, 0), b2(1, 1), c2(1, 2), d2(1, 3), e2(1, 4), f2(1, 5), g2(1, 6), h2(1, 7),
    a3(2, 0), b3(2, 1), c3(2, 2), d3(2, 3), e3(2, 4), f3(2, 5), g3(2, 6), h3(2, 7),
    a4(3, 0), b4(3, 1), c4(3, 2), d4(3, 3), e4(3, 4), f4(3, 5), g4(3, 6), h4(3, 7),
    a5(4, 0), b5(4, 1), c5(4, 2), d5(4, 3), e5(4, 4), f5(4, 5), g5(4, 6), h5(4, 7),
    a6(5, 0), b6(5, 1), c6(5, 2), d6(5, 3), e6(5, 4), f6(5, 5), g6(5, 6), h6(5, 7),
    a7(6, 0), b7(6, 1), c7(6, 2), d7(6, 3), e7(6, 4), f7(6, 5), g7(6, 6), h7(6, 7),
    a8(7, 0), b8(7, 1), c8(7, 2), d8(7, 3), e8(7, 4), f8(7, 5), g8(7, 6), h8(7, 7);

    private final int rank;
    private final int file;

    ChessAlphabet(int rank, int file) {
        this.rank = rank;
        this.file = file;

    }

    public int getRank() {
        return rank;
    }

    public int getFile() {
        return file;
    }

    public static ChessAlphabet getChessAlphabet(int rank, int file) {
        return ChessAlphabet.values()[rank * 8 + file];
    }
}
