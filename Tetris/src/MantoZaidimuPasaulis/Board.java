package MantoZaidimuPasaulis;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements ActionListener {
    private int widthSize = 20;
    private int heightSize = 32;
    private Timer time;
    private boolean pieceFinishedToFall = false;
    private boolean pieceStartedToFall = false;
    private int removedLines = 0;
    private int xCord = 0;
    private int yCord = 0;
    private Tetromino fallingPiece;
    private Pieces[] board;

    public Board() {
        setFocusable(true);
        fallingPiece = new Tetromino();
        time = new Timer(400, this);
        time.start();
        board = new Pieces[widthSize * heightSize];
        addKeyListener(new TAdapter());
        clearBoard();
    }

    public void actionPerformed(ActionEvent e) {
        if (pieceFinishedToFall) {
            pieceFinishedToFall = false;
            drawNewPiece();
        } else {
            downFaster();
        }
    }

    int widthOfSquare() { return (int) getSize().getWidth() / widthSize; }
    int heightOfSquare() { return (int) getSize().getHeight() / heightSize; }
    Pieces shapeAt(int x, int y) { return board[(y * widthSize) + x]; }


    public void paint(Graphics graphics)
    {
        super.paint(graphics);
        Dimension size = getSize();
        int topOfBoard = (int) size.getHeight() - heightSize * heightOfSquare();

        for (int i = 0; i < heightSize; ++i) {
            for (int j = 0; j < widthSize; ++j) {
                Pieces shape = shapeAt(j, heightSize - i - 1);
                if (shape != Pieces.EmptyShape)
                    drawSingleSquare(graphics, 0 + j * widthOfSquare(),
                            topOfBoard + i * heightOfSquare());
            }
        }

        if (fallingPiece.getShape() != Pieces.EmptyShape) {
            for (int i = 0; i < 4; ++i) {
                int x = xCord + fallingPiece.x(i);
                int y = yCord - fallingPiece.y(i);
                drawSingleSquare(graphics, 0 + x * widthOfSquare(),
                        topOfBoard + (heightSize - y - 1) * heightOfSquare());
            }
        }
    }

    public void letsStart()
    {
        pieceStartedToFall = true;
        pieceFinishedToFall = false;
        removedLines = 0;
        clearBoard();
        drawNewPiece();
        time.start();
    }

    private void downFaster()
    {
        if (!changeDirection(xCord, yCord - 1,fallingPiece))
            pieceDropped();
    }

    private void clearBoard()
    {
        for (int i = 0; i < heightSize * widthSize; ++i)
            board[i] = Pieces.EmptyShape;
    }

    private void pieceDropped()
    {
        for (int i = 0; i < 4; ++i) {
            int x = xCord + fallingPiece.x(i);
            int y = yCord - fallingPiece.y(i);
            board[(y * widthSize) + x] = fallingPiece.getShape();
        }
        removeFullLines();
        if (!pieceFinishedToFall)
            drawNewPiece();
    }

    private void drawNewPiece()
    {
        fallingPiece.setRandomShape();
        xCord = widthSize / 2 + 1;
        yCord = heightSize - 1 + fallingPiece.minY();
        if (!changeDirection(xCord,yCord,fallingPiece)) {
            fallingPiece.setShape(Pieces.EmptyShape);
            time.stop();
            pieceStartedToFall = false;
        }
    }

    private boolean changeDirection(int newX, int newY, Tetromino newOne)
    {
        for (int i = 0; i < 4; ++i) {
            int x = newX + newOne.x(i);
            int y = newY - newOne.y(i);
            if (x < 0 || x >= widthSize || y < 0 || y >= heightSize)
                return false;
            if (shapeAt(x, y) != Pieces.EmptyShape)
                return false;
        }

        fallingPiece = newOne;
        xCord = newX;
        yCord = newY;
        repaint();
        return true;
    }

    private void removeFullLines()
    {
        int numFullLines = 0;
        for (int i = heightSize - 1; i >= 0; --i) {
            boolean lineIsFull = true;
            for (int j = 0; j < widthSize; ++j) {
                if (shapeAt(j, i)== Pieces.EmptyShape) {
                    lineIsFull = false;
                    break;
                }
            }

            if (lineIsFull) {
                ++numFullLines;
                for (int k = i; k < heightSize - 1; ++k) {
                    for (int j = 0; j < widthSize; ++j)
                        board[(k * widthSize) + j] = shapeAt(j, k + 1);
                }
            }
        }

        if (numFullLines > 0) {
            removedLines += numFullLines;
            pieceFinishedToFall = true;
            fallingPiece.setShape(Pieces.EmptyShape);
            repaint();
        }
    }

    private void drawSingleSquare(Graphics g, int x, int y)
    {
        g.fillRect(x + 1, y + 1, widthOfSquare() - 2, heightOfSquare() - 2);
        g.drawLine(x, y + heightOfSquare() - 1, x, y);
        g.drawLine(x, y, x + widthOfSquare() - 1, y);
        g.drawLine(x + 1, y + heightOfSquare() - 1,
                x + widthOfSquare() - 1, y + heightOfSquare() - 1);
        g.drawLine(x + widthOfSquare() - 1, y + heightOfSquare() - 1,
                x + widthOfSquare() - 1, y + 1);
    }

    class TAdapter extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            if(e.getKeyCode()==KeyEvent.VK_LEFT){
                changeDirection(xCord - 1, yCord,fallingPiece);
            }
            if(e.getKeyCode()==KeyEvent.VK_RIGHT){
                changeDirection(xCord + 1, yCord,fallingPiece);
            }
            if(e.getKeyCode()==KeyEvent.VK_SPACE){
                changeDirection(xCord, yCord,fallingPiece.changeRotation());
            }
            if(e.getKeyCode()==KeyEvent.VK_DOWN){
                downFaster();
            }
        }
    }
}
