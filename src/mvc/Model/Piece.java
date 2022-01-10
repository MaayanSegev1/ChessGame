
package mvc.Model;

/**
 * The type Piece.
 */
public abstract class Piece {
    protected boolean living = true;
    /**
     * The Owner.
     */
    protected Player owner;

    /**
     * Instantiates a new Piece.
     *
     * @param owner the owner
     */
    public Piece(Player owner) {
        this.owner = owner;
    }

    /**
     * Is living boolean.
     *
     * @return the boolean
     */
    public boolean isLiving() {
        return living;
    }

    /**
     * Sets living.
     *
     * @param living the living
     */
    public void setLiving(boolean living) {
        this.living = living;
    }

    /**
     * Gets owner.
     *
     * @return the owner
     */
    public Player getOwner() {
        return owner;
    }

    /**
     * Get moves move [ ].
     *
     * @param p the p
     * @return the move [ ]
     */
    public abstract Move[] getMoves(Point p);

    @Override
    public abstract String toString();
}
