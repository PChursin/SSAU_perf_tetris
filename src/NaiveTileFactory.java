/**
 * Created by Pavel Chursin on 07.12.2017.
 */
public class NaiveTileFactory implements TileFactory {
    @Override
    public Tile createInstance(int size) {
        return new NaiveTile(size);
    }
}
