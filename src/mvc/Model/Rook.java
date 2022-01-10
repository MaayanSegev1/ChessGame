
package mvc.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Rook.
 *
 */
public class Rook extends Piece{

    /**
     * Instantiates a new Rook.
     *
     * @param owner the owner
     */
    public Rook(Player owner) {
        super(owner);
    }

    @Override
    public Move[] getMoves(Point p){
        int x = p.getX(),y = p.getY();
        List<Move> moves = new ArrayList<>();
        
        for(int i = 1 ; i < 8 ; i++){
            if(x-i >=0)
                    moves.add(new Move(new Point(x, y),new Point(x-i, y),Move.Direction.UP));
            if(y-i>=0)
                    moves.add(new Move(new Point(x, y),new Point(x, y-i),Move.Direction.LEFT));
            if(y+i<8)
                    moves.add(new Move(new Point(x, y),new Point(x, y+i),Move.Direction.RIGHT));
            if(x+i <8)
                    moves.add(new Move(new Point(x, y),new Point(x+i, y),Move.Direction.DOWN));
        }

        Move[] possibleMoves = new Move[moves.size()];
        possibleMoves = moves.toArray(possibleMoves);
        return possibleMoves;
    }

    @Override
    public String toString() {
        return "Rook";
    }


}
