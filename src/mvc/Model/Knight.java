
package mvc.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Knight.
 */
public class Knight extends Piece {

    /**
     * Instantiates a new Knight.
     *
     * @param owner the owner
     */
    public Knight(Player owner) {
        super(owner);
    }

    @Override
    public Move[] getMoves(Point startPoint) {
        int x = startPoint.getX(), y = startPoint.getY();

        List<Move> moves = new ArrayList<>();

        if (x - 1 >= 0 && y - 2 >= 0)
            moves.add(new Move(startPoint, new Point(x - 1, y - 2), Move.Direction.NONE));
        if (x + 1 < 8 && y - 2 >= 0)
            moves.add(new Move(startPoint, new Point(x + 1, y - 2), Move.Direction.NONE));
        if (x - 2 >= 0 && y - 1 >= 0)
            moves.add(new Move(startPoint, new Point(x - 2, y - 1), Move.Direction.NONE));
        if (x + 2 < 8 && y - 1 >= 0)
            moves.add(new Move(startPoint, new Point(x + 2, y - 1), Move.Direction.NONE));
        if (x - 2 >= 0 && y + 1 < 8)
            moves.add(new Move(startPoint, new Point(x - 2, y + 1), Move.Direction.NONE));
        if (x + 2 < 8 && y + 1 < 8)
            moves.add(new Move(startPoint, new Point(x + 2, y + 1), Move.Direction.NONE));
        if (x - 1 >= 0 && y + 2 < 8)
            moves.add(new Move(startPoint, new Point(x - 1, y + 2), Move.Direction.NONE));
        if (x + 1 < 8 && y + 2 < 8)
            moves.add(new Move(startPoint, new Point(x + 1, y + 2), Move.Direction.NONE));

        Move[] possibleMoves = new Move[moves.size()];
        possibleMoves = moves.toArray(possibleMoves);
        return possibleMoves;
    }

    @Override
    public String toString() {
        return "Knight";
    }

}
