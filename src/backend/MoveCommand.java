package backend;

interface MoveCommand {
    void executeMove() throws InvalidSyntaxException, InvalidMoveException;
}
