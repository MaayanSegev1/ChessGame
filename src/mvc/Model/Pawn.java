
package mvc.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Pawn.
 */
public class Pawn extends Piece {


    /**
     * Instantiates a new Pawn.
     *
     * @param owner the owner
     */
    public Pawn(Player owner) {
        super(owner);
    }

    @Override
    public Move[] getMoves(Point p) {
        int x = p.getX(), y = p.getY();
        List<Move> moves = new ArrayList<>();
        if (owner.isWhite()) {
            if (x == 6) {
                Point[] t = {new Point(x - 1, y), new Point(x - 2, y)};
                moves.add(new Move(p, new Point(x - 2, y), t));
            }
            if (x - 1 >= 0) {
                Point[] t = {new Point(x - 1, y)};
                moves.add(new Move(p, new Point(x - 1, y), t));
            }
            if (x - 1 >= 0 && y - 1 >= 0) {
                Move m = new Move(p, new Point(x - 1, y - 1), Move.Direction.NONE);
                m.setEnemyNeeded(true);
                moves.add(m);
            }
            if (x - 1 >= 0 && y + 1 < 8) {
                Move m = new Move(p, new Point(x - 1, y + 1), Move.Direction.NONE);
                m.setEnemyNeeded(true);
                moves.add(m);
            }
        } else {
            if (x == 1) {
                Point[] t = {new Point(x + 1, y), new Point(x + 2, y)};
                moves.add(new Move(p, new Point(x + 2, y), t));
            }
            if (x + 1 < 8) {
                Point[] t = {new Point(x + 1, y)};
                moves.add(new Move(p, new Point(x + 1, y), t));
            }
            if (x + 1 < 8 && y - 1 >= 0) {
                Move m = new Move(p, new Point(x + 1, y - 1), Move.Direction.NONE);
                m.setEnemyNeeded(true);
                moves.add(m);
            }
            if (x + 1 < 8 && y + 1 < 8) {
                Move m = new Move(p, new Point(x + 1, y + 1), Move.Direction.NONE);
                m.setEnemyNeeded(true);
                moves.add(m);
            }
        }

        Move[] possibleMoves = new Move[moves.size()];
        possibleMoves = moves.toArray(possibleMoves);
        return possibleMoves;
    }

    @Override
    public String toString() {
        return "Pawn";
    }
}
