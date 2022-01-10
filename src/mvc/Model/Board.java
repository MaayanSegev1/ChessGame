
package mvc.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * The type Board.
 */
public class Board extends Observable implements Cloneable {
    private Piece[][] pieces;

    /**
     * Instantiates a new Board.
     */
    public Board() {
        this.pieces = new Piece[8][8];
    }

    /**
     * Init board.
     *
     * @param white the white
     * @param black the black
     */
    public void initBoard(Player white, Player black) {
        /* 
            Appeler constructeur des differences pieces pieces[i][j] = new King(white);
        */
        pieces[0][0] = new Rook(black);
        pieces[0][1] = new Knight(black);
        pieces[0][2] = new Bishop(black);
        pieces[0][3] = new Queen(black);
        pieces[0][4] = new King(black);
        pieces[0][5] = new Bishop(black);
        pieces[0][6] = new Knight(black);
        pieces[0][7] = new Rook(black);
        for (int i = 0; i < 8; i++) {
            pieces[1][i] = new Pawn(black);
        }
        for (int i = 2; i < 7; i++) {
            for (int j = 0; j < 8; j++) {
                pieces[i][j] = null;
            }
        }

        pieces[7][0] = new Rook(white);
        pieces[7][1] = new Knight(white);
        pieces[7][2] = new Bishop(white);
        pieces[7][3] = new Queen(white);
        pieces[7][4] = new King(white);
        pieces[7][5] = new Bishop(white);
        pieces[7][6] = new Knight(white);
        pieces[7][7] = new Rook(white);
        for (int i = 0; i < 8; i++) {
            pieces[6][i] = new Pawn(white);
        }
        setChanged();
        notifyObservers();
    }

    /**
     * Get piece from type Piece.
     *
     * @param p the p
     * @return the piece
     */
    public Piece getPiece(Point p) {
        int x = p.getX(), y = p.getY();

        return pieces[x][y];
    }

    /**
     * Get piece position point.
     *
     * @param piece the piece
     * @param owner the owner
     * @return the point
     */
    public Point getPiecePosition(String piece, Player owner) {
        int row = 0, column = 0;
        Point tempPoint;
        Piece tempPiece;
        for (int i = 0; i < 64; i++) {
            tempPoint = new Point(row, column);
            tempPiece = getPiece(tempPoint);
            if (tempPiece != null
                    && tempPiece.getOwner() == owner
                    && tempPiece.toString() == piece) {
                return tempPoint;
            }
            column++;
            if (column > 7) {
                column = 0;
                row++;
            }
        }
        return null;
    }

    /**
     * Get moves move [ ].
     *
     * @param p the p
     * @return the move [ ]
     */
    public Move[] getMoves(Point p) {
        int x = p.getX(), y = p.getY();

        List<Move> moves = new ArrayList<>();
        for (Move m : this.pieces[x][y].getMoves(p)) {
            boolean isPossible = true;
            if (m.isEnemyNeeded() && pieces[m.getDestination().getX()][m.getDestination().getY()] == null) {
                isPossible = false;
            }
            if (m.getIntermediatePoints() != null) {
                for (Point i : m.getIntermediatePoints()) {
                    if (pieces[i.getX()][i.getY()] != null) {
                        isPossible = false;
                    }
                }
            }
            if (isPossible) {
                if (pieces[m.getDestination().getX()][m.getDestination().getY()] == null) {
                    moves.add(m);
                } else {
                    if (pieces[m.getDestination().getX()][m.getDestination().getY()].owner != pieces[x][y].owner) {
                        moves.add(m);
                    }
                }
            }
        }
        Move[] allMoves = new Move[moves.size()];
        allMoves = moves.toArray(allMoves);
        return allMoves;
    }

    /**
     * Move piece.
     *
     * @param m       the m
     * @param isATest the is a test
     */
    public void movePiece(Move m, boolean isATest) {
        this.pieces[m.getDestination().getX()][m.getDestination().getY()] = this.pieces[m.getStart().getX()][m.getStart().getY()];
        this.pieces[m.getStart().getX()][m.getStart().getY()] = null;
        if (!isATest) {
            this.setChanged();
            this.notifyObservers();
        }
    }

    @Override
    public Object clone() {
        Board clonedBoard = new Board();
        Point tempPoint;
        Piece tempPiece;
        Player tempPlayer;
        int row = 0, column = 0;
        for (int i = 0; i < 64; i++) {
            tempPoint = new Point(row, column);
            tempPiece = getPiece(tempPoint);
            if (tempPiece != null) {
                tempPlayer = tempPiece.getOwner();
                switch (tempPiece.toString()) {
                    case "Pawn":
                        clonedBoard.pieces[row][column] = new Pawn(tempPlayer);
                        break;
                    case "King":
                        clonedBoard.pieces[row][column] = new King(tempPlayer);
                        break;
                    case "Queen":
                        clonedBoard.pieces[row][column] = new Queen(tempPlayer);
                        break;
                    case "Knight":
                        clonedBoard.pieces[row][column] = new Knight(tempPlayer);
                        break;
                    case "Rook":
                        clonedBoard.pieces[row][column] = new Rook(tempPlayer);
                        break;
                    case "Bishop":
                        clonedBoard.pieces[row][column] = new Bishop(tempPlayer);
                        break;
                }
            }
            column++;
            if (column > 7) {
                column = 0;
                row++;
            }
        }
        return clonedBoard;
    }
}
