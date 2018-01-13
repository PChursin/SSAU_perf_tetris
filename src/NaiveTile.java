import java.util.Arrays;

/**
 * Created by Pavel Chursin on 06.10.2017.
 */
public class NaiveTile implements Tile {
    private boolean[][] cells;
    private int size;

    public NaiveTile(int size) {
        this.size = size;
        cells = new boolean[size][size];
    }

    @Override
    public void setCells(boolean[][] inTile) {
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                cells[i][j] = inTile[i][j];
    }

    public int getSize() {
        return size;
    }

    public boolean[][] getCells() {
        return cells;
    }

    @Override
    public boolean isEqualTo(Tile o) {
        if (size == o.getSize()) {
            boolean[][] oCells = o.getCells();
            for (int i = 0; i < size; i++)
                for (int j = 0; j < size; j++)
                    if (cells[i][j] != oCells[i][j])
                        return false;
            return true;
        }
        return false;
    }

    @Override
    public boolean intersects(Tile o) {
        if (size == o.getSize()) {
            boolean[][] oCells = o.getCells();
            for (int i = 0; i < size; i++)
                for (int j = 0; j < size; j++)
                    if (cells[i][j] && oCells[i][j])
                        return true;
            return false;
        }
        throw new IllegalArgumentException("NaiveTile sizes are not equal!");
    }

    @Override
    public NaiveTile rotate() {
        boolean[][] nCells = new boolean[size][size];
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++) {
                nCells[i][j] = cells[j][size-1-i]; //(1,0)->(0,2); (1,1)->()
            }
        NaiveTile out = new NaiveTile(size);
        out.setCells(nCells);
        out.alignCells();
        return out;
    }

    public void printTile() {
        for (int i = 0; i < size; i++) {
            System.out.print("[");
            for (int j = 0; j < size; j++)
                System.out.print((cells[i][j] ? "☻" : "☺"));
            System.out.println("]");
        }
        System.out.println();
    }

    private void alignCells() {
        boolean[][] nCells = new boolean[size][size];
        int left = size, up = size;
        for (int i = 0; i < size; i++) {
            Arrays.fill(nCells[i], false);
            for (int j = 0; j < size; j++)
                if (cells[i][j]) {
                    if (i < up)
                        up = i;
                    if (j < left)
                        left = j;
                }
        }

        for (int i = up; i < size; i++)
            for (int j = left; j < size; j++)
                if (cells[i][j])
                    nCells[i-up][j-left] = cells[i][j];
        cells = nCells;
    }
}
