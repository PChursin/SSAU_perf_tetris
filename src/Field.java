import java.util.Arrays;

/**
 * Created by Pavel Chursin on 06.10.2017.
 */
public class Field {
    private int[][] field;
    private int size, free;
    private int counter = 0;
    private static int MAX_TILE_SIZE = 4;
    private TileFactory tileFactory;

    public Field(int size, TileFactory tileFactory) {
        this.tileFactory = tileFactory;
        this.size = size;
        free = size*size;
        field = new int[size+MAX_TILE_SIZE][size+MAX_TILE_SIZE];
        for (int i = 0; i < size; i++)
            for (int j = size; j < size+MAX_TILE_SIZE; j++)
                field[i][j] = Integer.MAX_VALUE;
        for (int i = size; i < size+MAX_TILE_SIZE; i++)
            Arrays.fill(field[i], Integer.MAX_VALUE);
    }

    public Tile cutTile(int row, int col, int size) {
        boolean [][] cut = new boolean[size][size];
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                cut[i][j] = field[i+row][j+col] > 0;
        Tile out = tileFactory.createInstance(size);
        out.setCells(cut);
        return out;
    }

    public boolean applyTile(int row, int col, Tile tile) {
        Tile cut = cutTile(row, col, tile.getSize());
        if (cut.intersects(tile))
            return false;
        counter++;
        boolean [][] cells = tile.getCells();
        for (int i = 0; i < tile.getSize(); i++)
            for (int j = 0; j < tile.getSize(); j++)
                if (cells[i][j]) {
                    field[row + i][col + j] = counter;
                    free--;
                }
        return true;
    }

    public void removeTile(int row, int col, Tile tile) {
        boolean[][] cells = tile.getCells();
        for (int i = 0; i < tile.getSize(); i++)
            for (int j = 0; j < tile.getSize(); j++)
                if (cells[i][j]) {
                    field[row + i][col + j] = 0;
                    free++;
                }
        counter--;
    }

    public int getFree() {
        return free;
    }

    public int getSize() {
        return size;
    }

    public void printField() {
        int uncovered = 0;
        for (int i = 0; i < size; i++) {
            System.out.print("[");
            for (int j = 0; j < size; j++) {
                System.out.print(String.format("%4d", field[i][j]));
                if (field[i][j] == 0)
                    uncovered++;
            }
            System.out.println("]");
        }
        System.out.println("Total uncovered cells: "+uncovered);
    }
}
