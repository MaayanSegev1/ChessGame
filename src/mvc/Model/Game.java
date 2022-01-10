
package mvc.Model;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observable;

/**
 * The type Game.
 */
public class Game extends Observable {
    /**
     * The constant WHITE.
     */
    public static final boolean WHITE = true;
    /**
     * The constant BLACK.
     */
    public static final boolean BLACK = false;

    private String state;
    private Player white;
    private Player black;
    private Board board;
    private Player activePlayer;

    /**
     * Instantiates a new Game.
     */
    public Game() {
        this.state = "normal";
        this.white = new Player(WHITE);
        this.black = new Player(BLACK);
        this.board = new Board();
        activePlayer = white;
        board.initBoard(white, black);
    }

    /**
     * Gets board.
     *
     * @return the board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Next player turn.
     */
    public void nextPlayerTurn() {
        if (activePlayer == white)
            activePlayer = black;
        else
            activePlayer = white;
        setChanged();
        notifyObservers();
        checkCheck(false);
        if (state == "check") {
            checkCheckmate();
        }
    }

    /**
     * Get active player from type Player.
     *
     * @return the player
     */
    public Player getActivePlayer() {
        return activePlayer;
    }

    /**
     * Get enemy player from type Player.
     *
     * @return the player
     */
    public Player getEnemyPlayer() {
        if (getActivePlayer().isWhite())
            return black;
        else
            return white;
    }

    /**
     * Play game boolean.
     *
     * @return the boolean
     */
    public boolean playGame() {
        boolean possibleMoves = true;
        while (possibleMoves) {

        }
        return false;
    }

    /**
     * End game.
     *
     * @param hasSurrendered the has surrendered
     */
    public void endGame(boolean hasSurrendered) {
        if (hasSurrendered)
            state = "surrender";
        else
            state = "checkmate";
        setChanged();
        notifyObservers();
    }

    /**
     * Gets state.
     *
     * @return the state
     */
    public String getState() {
        return state;
    }

    /**
     * Restart game.
     */
    public void restartGame() {
        this.state = "normal";
        activePlayer = white;
        board.initBoard(white, black);
        setChanged();
        notifyObservers();
    }

    /**
     * Get moves list.
     *
     * @param p the p
     * @return the list
     */
    public List<Entry<Move, Boolean>> getMoves(Point p) {
        List<Entry<Move, Boolean>> movesPossible = new ArrayList<>();
        Move[] moves = board.getMoves(p);
        for (Move m : moves) {
            if (checkIfMovePossible(m)) {
                movesPossible.add(new SimpleEntry<>(m, true));
            } else
                movesPossible.add(new SimpleEntry<>(m, false));
        }
        return movesPossible;
    }

    /**
     * Check .
     *
     * @param isATest the is a test
     */
    public void checkCheck(boolean isATest) {
        this.state = "normal";
        Point tempPoint;
        Piece tempPiece;
        Player enemyPlayer = getEnemyPlayer();
        int row = 0, column = 0;
        int x2, y2, y3;
        Point kingPosition = getBoard().getPiecePosition("King", getActivePlayer());
        if (!isATest) {
            //Cas où tous les coups mènent à l'échec
            int countNbPossibleMoves = 0;
            for (int i = 0; i < 64; i++) {
                tempPoint = new Point(row, column);
                tempPiece = getBoard().getPiece(tempPoint);
                if (tempPiece != null && tempPiece.getOwner() == getActivePlayer()) {
                    for (Entry<Move, Boolean> m : getMoves(tempPoint)) {
                        if (m.getValue() == true) {
                            countNbPossibleMoves++;
                        }
                    }
                }
                column++;
                if (column > 7) {
                    column = 0;
                    row++;
                }
            }
            if (countNbPossibleMoves == 0) {
                this.state = "checkmate";
                setChanged();
                notifyObservers();
                return;
            }
        }
        row = 0;
        column = 0;
        int x = kingPosition.getX();
        int y = kingPosition.getY();
        for (int i = 0; i < 64; i++) {
            tempPoint = new Point(row, column);
            tempPiece = getBoard().getPiece(tempPoint);
            if (tempPiece != null && tempPiece.getOwner() == enemyPlayer) {
                //Les pions agissent différemment que les autres pièces pour
                //capturer des pièces ennemies. Nous les traitons séparément.
                if (tempPiece.toString() != "Pawn") {
                    for (Move p : this.getBoard().getMoves(tempPoint)) {
                        x2 = p.getDestination().getX();
                        y2 = p.getDestination().getY();
                        if (x == x2 && y == y2) {
                            this.state = "check";
                        }
                    }
                } else {
                    if (enemyPlayer == black) {
                        x2 = row + 1;
                    } else {
                        x2 = row - 1;
                    }
                    y2 = column - 1;
                    y3 = column + 1;
                    if (x == x2 && y == y2) {
                        this.state = "check";
                    } else if (x == x2 && y == y3) {
                        this.state = "check";
                    }
                }
            }
            column++;
            if (column > 7) {
                column = 0;
                row++;
            }
        }
        if (!isATest)
            setChanged();
        notifyObservers();
    }

    /**
     * Check checkmate boolean.
     *
     * @return the boolean
     */
    public boolean checkCheckmate() {
        /**
         * Boolean array representing the region accessible by the king.
         * A playable square is represented by true.
         */
        boolean[][] gridKingMoves = new boolean[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                gridKingMoves[i][j] = false;
            }
        }
        //We know that the square where the king is located causes a failure
        gridKingMoves[1][1] = false;
        //Look where the king can move
        Point kingPosition = getBoard().getPiecePosition("King", getActivePlayer());
        int x = kingPosition.getX();
        int y = kingPosition.getY();
        int x2, bx, y2, by, y3;
        for (Entry<Move, Boolean> p : getMoves(kingPosition)) {
            x = kingPosition.getX();
            x2 = p.getKey().getDestination().getX();
            y = kingPosition.getY();
            y2 = p.getKey().getDestination().getY();
            bx = x2 - (x - 1);
            by = y2 - (y - 1);
            if (bx >= 0 && bx < 3 && by >= 0 && by < 3) {
                gridKingMoves[bx][by] = true;
            }
        }
        //Check for all the opposing pieces the blows that they do not allow the king
        int row = 0, column = 0;
        Piece tempPiece;
        Point tempPoint;
        Player enemyPlayer = getEnemyPlayer();
        for (int i = 0; i < 64; i++) {
            tempPoint = new Point(row, column);
            tempPiece = getBoard().getPiece(tempPoint);
            if (tempPiece != null && tempPiece.getOwner() == enemyPlayer) {
                //Pawns act differently than other pieces to capture enemy pieces.
                // We treat them separately.
                if (tempPiece.toString() != "Pawn") {
                    for (Move p : getBoard().getMoves(tempPoint)) {
                        x2 = p.getDestination().getX();
                        y2 = p.getDestination().getY();
                        bx = x2 - (x - 1);
                        by = y2 - (y - 1);
                        if (bx >= 0 && bx < 3 && by >= 0 && by < 3) {
                            gridKingMoves[bx][by] = false;
                        }
                    }
                } else {
                    if (enemyPlayer == black) {
                        x2 = row + 1;
                    } else {
                        x2 = row - 1;
                    }
                    y2 = column - 1;
                    y3 = column + 1;
                    bx = x2 - (x - 1);
                    by = y2 - (y - 1);
                    if (bx >= 0 && bx < 3 && by >= 0 && by < 3) {
                        gridKingMoves[bx][by] = false;
                    }
                    bx = x2 - (x - 1);
                    by = y3 - (y - 1);
                    if (bx >= 0 && bx < 3 && by >= 0 && by < 3) {
                        gridKingMoves[bx][by] = false;
                    }
                }
            }
            column++;
            if (column > 7) {
                column = 0;
                row++;
            }
        }
        boolean isCheckmate = true;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (gridKingMoves[i][j] == true)
                    isCheckmate = false;
            }
        }
        return isCheckmate;
    }

    /**
     * Move piece.
     *
     * @param m       the m
     * @param isATest the is a test
     */
    public void movePiece(Move m, boolean isATest) {
        getBoard().movePiece(m, isATest);
        checkCheck(isATest);
        if (!isATest) {
            this.state = "normal";
        }
    }

    /**
     * Check if move possible boolean.
     *
     * @param m the m
     * @return the boolean
     */
    public boolean checkIfMovePossible(Move m) {
        Board oldBoard = getBoard();
        board = (Board) getBoard().clone();
        String oldState = state;
        boolean isMovePossible;
        this.movePiece(m, true);
        if (state == "check" || state == "checkmate")
            isMovePossible = false;
        else
            isMovePossible = true;
        this.board = oldBoard;
        this.state = oldState;
        return isMovePossible;
    }
}
