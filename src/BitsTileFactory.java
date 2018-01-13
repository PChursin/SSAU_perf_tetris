/**
 * Created by Pavel Chursin on 07.12.2017.
 */
public class BitsTileFactory implements TileFactory {
    @Override
    public Tile createInstance(int size) {
        return new BitsTile(size);
    }
}
