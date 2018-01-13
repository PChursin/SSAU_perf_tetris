import java.util.ArrayList;

/**
 * Created by Pavel Chursin on 06.10.2017.
 */
public class Figure {
    private int size;
    private ArrayList<Tile> positions;
    private TileFactory tileFactory;

    public Figure(int size, boolean[][] proto, TileFactory factory) {
        this.size = size;
        tileFactory = factory;
        fillPositions(proto);
    }

    public ArrayList<Tile> getPositions() {
        return positions;
    }

    private void fillPositions(boolean [][] proto) {
        positions = new ArrayList<>();
        Tile p = tileFactory.createInstance(size);
        p.setCells(proto);
        positions.add(p);
        for (int i = 0; i < 3; i++) {
            Tile next = positions.get(i).rotate();
            if (p.isEqualTo(next))
                return;
            positions.add(next);
        }
    }
}
