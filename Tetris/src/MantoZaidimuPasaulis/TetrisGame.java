package MantoZaidimuPasaulis;

import javax.swing.JFrame;

public class TetrisGame extends JFrame {
    public TetrisGame() {
        Board board = new Board();
        add(board);
        board.letsStart();
        setSize(200, 400);
        setTitle("Tetris");
    }

    public static void main(String[] args) {
        TetrisGame game = new TetrisGame();
        game.setLocationRelativeTo(null);
        game.setVisible(true);
    }
}
