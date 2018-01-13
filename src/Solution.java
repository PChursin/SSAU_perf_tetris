import java.util.*;

/**
 * Created by Pavel Chursin on 06.10.2017.
 */
public class Solution {

    public static final int TILE_SIZE = 4;
    public static final int FIELD_SIZE = 8;

    public static boolean[][] p1 = {
            {false,  true,  false, false},
            {true,   true,  true,  false},
            {false,  false, false, false},
            {false,  false, false, false}};

    public static boolean[][] p2 = {
            {true,   true,  true,  true},
            {false,  false, false, false},
            {false,  false, false, false},
            {false,  false, false, false}};

    public static boolean[][] p3 = {
            {true,   true,  true,  false},
            {false,  false, true, false},
            {false,  false, false, false},
            {false,  false, false, false}};

    public static boolean[][] p4 = {
            {true,   true,  true,  false},
            {true,   false, false, false},
            {false,  false, false, false},
            {false,  false, false, false}};

    public static boolean[][] p5 = {
            {true,   true,  false, false},
            {true,   true,  false, false},
            {false,  false, false, false},
            {false,  false, false, false}};

    public static boolean[][] p6 = {
            {true,   true,  false, false},
            {false,  true,  true,  false},
            {false,  false, false, false},
            {false,  false, false, false}};

    public static boolean[][] p7 = {
            {false,  true,  true, false},
            {true,   true,  false, false},
            {false,  false, false, false},
            {false,  false, false, false}};

    private ArrayList<Tile> allPositions;
    private Field field;
    private ArrayList<Integer> idxBase;
    private ArrayDeque<TurnData> turnsStack;
    private Random rand;
    TileFactory tileFactory;

    public Solution(long seed, TileFactory tileFactory) {
        rand = new Random(seed);
        this.tileFactory = tileFactory;
        boolean[][][] protos = {p1, p2, p3, p4, p5, p6, p7};
        allPositions = new ArrayList<>();
        for (boolean[][] p : protos) {
            Figure f = new Figure(TILE_SIZE, p, tileFactory);
            allPositions.addAll(f.getPositions());
        }

/*
        for (Tile t : allPositions) {
            t.printTile();
            System.out.println();
        }*/

        field = new Field(FIELD_SIZE, tileFactory);
        turnsStack = new ArrayDeque<>();
        idxBase = new ArrayList<>();
        for (int i = 0; i < allPositions.size(); i++)
            idxBase.add(i);
        ArrayList<Integer> indexes = new ArrayList<>();
        indexes.addAll(idxBase);

        int sRow = 0, sCol = 0;
        while (true) {
            fillFrom(sRow, sCol, indexes);
            if (field.getFree() == 0)
                break;
            TurnData lastTurn = turnsStack.pop();
            sRow = lastTurn.row;
            sCol = lastTurn.col;
            field.removeTile(sRow, sCol, allPositions.get(lastTurn.tileIdx));
            indexes = lastTurn.spare;
        }
        field.printField();
    }

    private void fillFrom(int sRow, int sCol, ArrayList<Integer> idx) {
        int fSize = field.getSize();
        for (int i = sRow; i < fSize; i++)
            for (int j = sCol; j < fSize; j++) {
                ArrayList<Integer> idxCopy = new ArrayList<>();
                idxCopy.addAll(idx);
                Collections.shuffle(idxCopy, rand);

                Tile cut = field.cutTile(i, j, TILE_SIZE);
                while (!idxCopy.isEmpty()) {
                    int tIdx = idxCopy.remove(0);
                    Tile fig = allPositions.get(tIdx);
                    if (!cut.intersects(fig)) {
                        field.applyTile(i, j, fig);
                        turnsStack.push(new TurnData(tIdx, i, j, idxCopy));
                        break;
                    }
                }
                idx = idxBase;
            }
    }

    public static void main(String[] args) {

        NaiveTileFactory naive = new NaiveTileFactory();
        BitsTileFactory bits = new BitsTileFactory();
        long seed = 500; //420 < 1 sec; 4 - 40 sec
        System.out.println("Naive:");
        int N = 5;
        double naiveTime = 0, bitsTime = 0;

        for (int i = 0; i < N; i++) {
            long t = (new Date()).getTime();
            Solution s = new Solution(seed, naive);
            double res = ((new Date()).getTime() - t) / 1000.0;
            naiveTime += res;
            System.out.println("took time: " + res + " sec");
            s = null;
        }
        System.out.println("Average naive: " + naiveTime/N + " sec");

        System.out.println("Bits:");
        for (int i = 0; i < N; i++) {
            long t = (new Date()).getTime();
            Solution s = new Solution(seed, bits);
            double res = ((new Date()).getTime() - t) / 1000.0;
            bitsTime += res;
            System.out.println("took time: " + res + " sec");
            s = null;
        }
        System.out.println("Average bits: " + bitsTime/N + " sec");
    }

    class TurnData {
        int tileIdx, row, col;
        ArrayList<Integer> spare;

        public TurnData(int tileIdx, int row, int col, ArrayList<Integer> spare) {
            this.tileIdx = tileIdx;
            this.row = row;
            this.col = col;
            this.spare = spare;
        }

        public boolean hasNext() {
            return !spare.isEmpty();
        }

        public int getNext() {
            return spare.remove(0);
        }
    }
}
