package backend;

class ChessGameMemento {
    private int turn;
    private boolean gameHasEnded;
    public Square[][] savedBoardState=new Square[8][8];

    public ChessGameMemento(Square[][] savedBoardState,int turn,boolean gameHasEnded) {
        this.gameHasEnded=gameHasEnded;
        this.turn = turn;
        for(int i=0;i<8;i++)
            for (int j=0;j<8;j++)
            {
                try {
                    this.savedBoardState[i][j]= savedBoardState[i][j].clone();
                } catch (CloneNotSupportedException e) {
                    throw new RuntimeException(e);
                }
            }
    }

    public int getTurn() {
        return turn;
    }

    public boolean GameHasEnded() {
        return gameHasEnded;
    }

    public Square[][] getSavedBoardState() {
        return savedBoardState;
    }

}
