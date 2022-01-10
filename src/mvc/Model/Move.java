
package mvc.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Move.
 */
public class Move {

    /**
     * The enum Direction.
     */
    public enum Direction {
        /**
         * Left up diagonal direction.
         */
        LEFT_UP_DIAGONAL,
        /**
         * Left down diagonal direction.
         */
        LEFT_DOWN_DIAGONAL,
        /**
         * Right up diagonal direction.
         */
        RIGHT_UP_DIAGONAL,
        /**
         * Right down diagonal direction.
         */
        RIGHT_DOWN_DIAGONAL,
        /**
         * Left direction.
         */
        LEFT,
        /**
         * Right direction.
         */
        RIGHT,
        /**
         * Up direction.
         */
        UP,
        /**
         * Down direction.
         */
        DOWN,
        /**
         * None direction.
         */
        NONE
    }

    ;
    private Point start;
    private Point destination;
    private Point[] intermediatePoints;
    private boolean enemyNeeded = false;

    /**
     * Instantiates a new Move.
     *
     * @param start       the start
     * @param destination the destination
     * @param d           the d
     */
    public Move(Point start, Point destination, Direction d) {
        this.start = start;
        this.destination = destination;
        if (d != Direction.NONE) {
            Point p;
            List<Point> inter = new ArrayList<>();
            switch (d) {
                case LEFT_UP_DIAGONAL:
                    p = new Point(start.getX() - 1, start.getY() - 1);
                    while (p.getX() != destination.getX() || p.getY() != destination.getY()) {
                        inter.add(p);
                        p = new Point(p.getX() - 1, p.getY() - 1);
                    }
                    break;
                case LEFT_DOWN_DIAGONAL:
                    p = new Point(start.getX() + 1, start.getY() - 1);
                    while (p.getX() != destination.getX() || p.getY() != destination.getY()) {
                        inter.add(p);
                        p = new Point(p.getX() + 1, p.getY() - 1);
                    }
                    break;
                case RIGHT_UP_DIAGONAL:
                    p = new Point(start.getX() - 1, start.getY() + 1);
                    while (p.getX() != destination.getX() || p.getY() != destination.getY()) {
                        inter.add(p);
                        p = new Point(p.getX() - 1, p.getY() + 1);
                    }
                    break;
                case RIGHT_DOWN_DIAGONAL:
                    p = new Point(start.getX() + 1, start.getY() + 1);
                    while (p.getX() != destination.getX() || p.getY() != destination.getY()) {
                        inter.add(p);
                        p = new Point(p.getX() + 1, p.getY() + 1);
                    }
                    break;
                case LEFT:
                    p = new Point(start.getX(), start.getY() - 1);
                    while (p.getX() != destination.getX() || p.getY() != destination.getY()) {
                        inter.add(p);
                        p = new Point(p.getX(), p.getY() - 1);
                    }
                    break;
                case RIGHT:
                    p = new Point(start.getX(), start.getY() + 1);
                    while (p.getX() != destination.getX() || p.getY() != destination.getY()) {
                        inter.add(p);
                        p = new Point(p.getX(), p.getY() + 1);
                    }
                    break;
                case UP:
                    p = new Point(start.getX() - 1, start.getY());
                    while (p.getX() != destination.getX() || p.getY() != destination.getY()) {
                        //System.out.println(p);
                        inter.add(p);
                        p = new Point(p.getX() - 1, p.getY());
                    }
                    break;
                case DOWN:
                    p = new Point(start.getX() + 1, start.getY());
                    while (p.getX() != destination.getX() || p.getY() != destination.getY()) {
                        inter.add(p);
                        p = new Point(p.getX() + 1, p.getY());
                    }
                    break;
            }

            Point[] intermediate = new Point[inter.size()];
            intermediatePoints = inter.toArray(intermediate);
        /*for(Point i : intermediatePoints)
                System.out.println(i);*/
        } else {
            //System.out.println("NONE");
            intermediatePoints = null;
        }
    }

    /**
     * Instantiates a new Move.
     *
     * @param start              the start
     * @param destination        the destination
     * @param intermediatePoints the intermediate points
     */
    public Move(Point start, Point destination, Point[] intermediatePoints) {
        this.start = start;
        this.destination = destination;
        this.intermediatePoints = intermediatePoints;
    }


    /**
     * Gets start.
     *
     * @return the start
     */
    public Point getStart() {
        return start;
    }

    /**
     * Get intermediate points point [ ].
     *
     * @return the point [ ]
     */
    public Point[] getIntermediatePoints() {
        return intermediatePoints;
    }

    /**
     * Gets destination.
     *
     * @return the destination
     */
    public Point getDestination() {
        return destination;
    }

    /**
     * Sets enemy needed.
     *
     * @param enemyNeeded the enemy needed
     */
    public void setEnemyNeeded(boolean enemyNeeded) {
        this.enemyNeeded = enemyNeeded;
    }

    /**
     * Is enemy needed boolean.
     *
     * @return the boolean
     */
    public boolean isEnemyNeeded() {
        return enemyNeeded;
    }

    @Override
    public String toString() {
        return "Move{" + "start=" + start + ", destination=" + destination + '}';
    }


}
