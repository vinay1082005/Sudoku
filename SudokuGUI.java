import java.awt.*;
import java.util.Random;
import javax.swing.*;

public class SudokuGUI extends JFrame {
    private JTextField[][] cells = new JTextField[9][9];

    private int[][] board = new int[9][9];

    public SudokuGUI() {
        generatePuzzle();

        setTitle("Sudoku Game");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel gridPanel = new JPanel(new GridLayout(9, 9));
        Font font = new Font("Arial", Font.BOLD, 20);

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                JTextField cell = new JTextField();
                cell.setHorizontalAlignment(JTextField.CENTER);
                cell.setFont(font);

                if (board[i][j] != 0) {
                    cell.setText(String.valueOf(board[i][j]));
                    cell.setEditable(false);
                    cell.setBackground(Color.LIGHT_GRAY);
                }

                cells[i][j] = cell;
                gridPanel.add(cell);
            }
        }

        JButton solveButton = new JButton("Solve");
        solveButton.addActionListener(e -> solve());

        JButton resetButton = new JButton("New Game");
        resetButton.addActionListener(e -> {
            dispose();
            new SudokuGUI().setVisible(true);
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(solveButton);
        buttonPanel.add(resetButton);

        add(gridPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // 🔥 Generate Random Sudoku Puzzle
    private void generatePuzzle() {
        int[][] fullBoard = new int[9][9];
        solveSudoku(fullBoard);

        Random rand = new Random();

        // Copy full board
        for (int i = 0; i < 9; i++)
            System.arraycopy(fullBoard[i], 0, board[i], 0, 9);

        // Remove numbers randomly
        int cellsToRemove = 40; // difficulty control
        while (cellsToRemove > 0) {
            int r = rand.nextInt(9);
            int c = rand.nextInt(9);

            if (board[r][c] != 0) {
                board[r][c] = 0;
                cellsToRemove--;
            }
        }
    }

    private void solve() {
        int[][] tempBoard = getBoardFromUI();
        if (solveSudoku(tempBoard)) {
            updateUI(tempBoard);
        } else {
            JOptionPane.showMessageDialog(this, "No solution exists!");
        }
    }

    private int[][] getBoardFromUI() {
        int[][] temp = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                String text = cells[i][j].getText();
                temp[i][j] = text.isEmpty() ? 0 : Integer.parseInt(text);
            }
        }
        return temp;
    }

    private void updateUI(int[][] solved) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                cells[i][j].setText(String.valueOf(solved[i][j]));
            }
        }
    }

    private boolean isValid(int[][] board, int row, int col, int num) {
        for (int i = 0; i < 9; i++) {
            if (board[row][i] == num || board[i][col] == num)
                return false;
        }

        int startRow = row - row % 3;
        int startCol = col - col % 3;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[startRow + i][startCol + j] == num)
                    return false;
            }
        }
        return true;
    }

    private boolean solveSudoku(int[][] board) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (board[row][col] == 0) {
                    for (int num = 1; num <= 9; num++) {
                        if (isValid(board, row, col, num)) {
                            board[row][col] = num;
                            if (solveSudoku(board))
                                return true;
                            board[row][col] = 0;
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new SudokuGUI().setVisible(true);
        });
    }
}
