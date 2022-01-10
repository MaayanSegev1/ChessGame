
package mvc.Model;

/**
 * The type Model test.
 *
 */
public class ModelTest {
    /**
     * Main.
     *
     * @param args the args
     */
    public static void main(String[] args){
        Player p = new Player(true);
        Queen rook = new Queen(null);
        for(Move m :rook.getMoves(new Point(2, 2))){
            System.out.println(m);
        }
    }
}