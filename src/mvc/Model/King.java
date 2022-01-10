
package mvc.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * The type King.
 */
public class King extends Piece {

    /**
     * Instantiates a new King.
     *
     * @param owner the owner
     */
    public King(Player owner) {
        super(owner);
    }

    @Override
    public Move[] getMoves(Point p) {
        int x = p.getX(), y = p.getY();
        List<Move> moves = new ArrayList<>();
        for (int i : new Integer[]{-1, 0, 1}) {
            for (int j : new Integer[]{-1, 0, 1}) {
                if (i + x < 8 && i + x >= 0 && j + y < 8 && j + y >= 0 && (i != 0 || j != 0))
                    moves.add(new Move(new Point(x, y), new Point(x + i, y + j), Move.Direction.NONE));
            }
        }
        Move[] possibleMoves = new Move[moves.size()];
        possibleMoves = moves.toArray(possibleMoves);
        return possibleMoves;
    }

    @Override
    public String toString() {
        return "King";
    }

}
