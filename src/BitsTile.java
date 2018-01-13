/**
 * Created by Pavel Chursin on 07.12.2017.
 */
public class BitsTile implements Tile {
    private int size;
    private long cells;
    BitsTile (int size) {
        this.size = size;
        cells = 0;
    }

    @Override
    public Tile rotate() {
        BitsTile out = new BitsTile(size);
        int outCounter = 0;
        int left = size, up = size;
        long tempCells = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if ((cells & (1 << (j * size + size - 1 - i))) != 0) {
                    tempCells |= 1 << outCounter;
                    if (i < up)
                        up = i;
                    if (j < left)
                        left = j;
                }
                outCounter++;
            }
        }

        //outCounter = 0;
        for (int i = up; i < size; i++) {
            for (int j = left; j < size; j++) {
                if ((cells & (1 << (j * size + size - 1 - i))) != 0)
                    out.cells |= 1 << ((i - up)*size + j - left);
                //outCounter++;
            }
        }

/*
        boolean[][] nCells = new boolean[size][size];

        for (int i = 0; i < size; i++) {
            Arrays.fill(nCells[i], false);
            for (int j = 0; j < size; j++)
                if (cells[i][j]) {

                }
        }

        for (int i = up; i < size; i++)
            for (int j = left; j < size; j++)
                if (cells[i][j])
                    nCells[i-up][j-left] = cells[i][j];
        cells = nCells;*/


        return out;
    }

    @Override
    public boolean intersects(Tile o) {
        return (((BitsTile)o).cells & cells) != 0;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public boolean[][] getCells() {
        boolean [][] out = new boolean[size][size];
        int counter = 0;
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++) {
                if ((cells & (1 << counter)) != 0)
                    out[i][j] = true;
                counter++;
            }
        return  out;
    }

    public void printTile() {
        boolean [][] boolCells = getCells();
        for (int i = 0; i < size; i++) {
            System.out.print("[");
            for (int j = 0; j < size; j++)
                System.out.print((boolCells[i][j] ? "☻" : "☺"));
            System.out.println("]");
        }
        System.out.println();
    }

    @Override
    public boolean isEqualTo(Tile o) {
        return ((BitsTile)o).cells == cells;
    }

    @Override
    public void setCells(boolean[][] cells) {
        int counter = 0;
        this.cells = 0;
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++) {
                if (cells[i][j])
                    this.cells |= 1 << counter;
                counter++;
            }
    }
}
