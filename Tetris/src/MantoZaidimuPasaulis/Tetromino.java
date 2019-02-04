package MantoZaidimuPasaulis;


import java.util.Random;
import java.lang.Math;

public class Tetromino {

    private Pieces pieceShape;
    private int coords[][];
    private int[][][] shapesOnCords;

    public Tetromino() { //
        coords = new int[4][2]; //
        setShape(Pieces.EmptyShape);
    }
    public void setShape(Pieces shape) {
        shapesOnCords = new int[][][] {
                { { 0, 0 },   { 0, 0 },   { 0, 0 },   { 0, 0 } },
                { { 0, -1 },  { 0, 0 },   { -1, 0 },  { -1, 1 } },
                { { 0, -1 },  { 0, 0 },   { 1, 0 },   { 1, 1 } },
                { { 0, -1 },  { 0, 0 },   { 0, 1 },   { 0, 2 } },
                { { -1, 0 },  { 0, 0 },   { 1, 0 },   { 0, 1 } },
                { { 0, 0 },   { 1, 0 },   { 0, 1 },   { 1, 1 } },
                { { -1, -1 }, { 0, -1 },  { 0, 0 },   { 0, 1 } },
                { { 1, -1 },  { 0, -1 },  { 0, 0 },   { 0, 1 } }
        };
        for (int i = 0; i < 4 ; i++) {
            for (int j = 0; j < 2; j++) {
                coords[i][j] = shapesOnCords[shape.ordinal()][i][j];
            }
        }
        pieceShape = shape;

    }
    private void setX(int index, int x) { coords[index][0] = x; }
    private void setY(int index, int y) { coords[index][1] = y; }
    public int x(int index) { return coords[index][0]; }
    public int y(int index) { return coords[index][1]; }
    public Pieces getShape()  { return pieceShape; }

    public void setRandomShape()
    {
        Random r = new Random();
        int x = Math.abs(r.nextInt()) % 7 + 1;
        Pieces[] enumValues = Pieces.values();
        setShape(enumValues[x]);
    }

    public int minY()
    {
        int m = coords[0][1];
        for (int i=0; i < 4; i++) {
            m = Math.min(m, coords[i][1]);
        }
        return m;
    }

    public Tetromino changeRotation()
    {
        if (pieceShape == Pieces.SquareShape)
            return this;
        Tetromino result = new Tetromino();
        result.pieceShape = pieceShape;
        for (int i = 0; i < 4; ++i) {
            result.setX(i, -y(i));
            result.setY(i, x(i));
        }
        return result;
    }
}
