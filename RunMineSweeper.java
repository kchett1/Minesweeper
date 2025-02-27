package org.cis1200.minesweeper;

import javax.swing.*;
import java.awt.*;

public class RunMineSweeper implements Runnable {
    @Override
    public void run() {
        // from tic tac toe logic
        final JFrame frame = new JFrame("MineSweeper");
        frame.setLocation(300, 300);
        final JPanel statusPanel = new JPanel();
        frame.add(statusPanel, BorderLayout.SOUTH);
        final JLabel status = new JLabel();
        statusPanel.add(status);
        int numMines = wordsForNumMines();
        final MinesweeperBoard board = new MinesweeperBoard(status, numMines);
        frame.add(board, BorderLayout.CENTER);
        final JPanel controlPanel = new JPanel();
        frame.add(controlPanel, BorderLayout.NORTH);
        final JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> board.resetGame());
        controlPanel.add(resetButton);

        // Instructions button
        final JButton instructionsButton = new JButton("Instructions");
        instructionsButton.addActionListener(e -> showInstructions());
        controlPanel.add(instructionsButton);

        // save button
        final JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            board.saveGameState();
            JOptionPane.showMessageDialog(
                    null,
                    "saved the game. Come back!",
                    "Save Game",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });
        controlPanel.add(saveButton);

        final JButton loadButton = new JButton("Load");
        loadButton.addActionListener(e -> {
            board.loadGameState();
            JOptionPane.showMessageDialog(
                    null,
                    "game loaded",
                    "Load Game",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });
        controlPanel.add(loadButton);

        // from tic tac toe board
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        board.resetGame();
    }

    private int wordsForNumMines() {
        // from docuscript of Javaswing
        String enterMines = JOptionPane.showInputDialog(
                null, "Enter the num of mines:",
                "Kieran's Minesweeper", JOptionPane.QUESTION_MESSAGE
        );
        try {
            return Math.max(1, Integer.parseInt(enterMines));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "invalid input, so 10 mines");
            return 5;
        }
    }

    // Instructions button
    private void showInstructions() {
        JOptionPane.showMessageDialog(
                null,
                "Minesweeper Instructions:\n" +
                        "- Left-click to unveil tile.\n" +
                        "- Right-click to flag mines.\n" +
                        "- Dont click mines or you lose\n" +
                        "- Clear all safe spaces to win\n" +
                        "- Use Save to keep progress.\n" +
                        "- Use the Load to find old games.",
                "Instructions",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}
