
package mvc.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Bishop.
 */
public class Bishop extends Piece {

    /**
     * Instantiates a new Bishop.
     *
     * @param owner the owner
     */
    public Bishop(Player owner) {
        super(owner);
    }

    @Override
    public Move[] getMoves(Point p) {
        int x = p.getX(), y = p.getY();
        List<Move> moves = new ArrayList<>();

        for (int i = 1; i < 8; i++) {
            if (x - i >= 0 && y - i >= 0)
                moves.add(new Move(new Point(x, y), new Point(x - i, y - i), Move.Direction.LEFT_UP_DIAGONAL));
            if (x - i >= 0 && y + i < 8)
                moves.add(new Move(new Point(x, y), new Point(x - i, y + i), Move.Direction.RIGHT_UP_DIAGONAL));
            if (x + i < 8 && y - i >= 0)
                moves.add(new Move(new Point(x, y), new Point(x + i, y - i), Move.Direction.LEFT_DOWN_DIAGONAL));
            if (x + i < 8 && y + i < 8)
                moves.add(new Move(new Point(x, y), new Point(x + i, y + i), Move.Direction.RIGHT_DOWN_DIAGONAL));
        }

        Move[] possibleMoves = new Move[moves.size()];
        possibleMoves = moves.toArray(possibleMoves);
        return possibleMoves;
    }

    @Override
    public String toString() {
        return "Bishop";
    }


}
