/**
 * Created by Pavel Chursin on 07.12.2017.
 */
public interface Tile {
    Tile rotate();
    boolean intersects(Tile o);
    int getSize();
    boolean[][] getCells();
    boolean isEqualTo(Tile o);
    void setCells(boolean[][] cells);
    void printTile();
}
