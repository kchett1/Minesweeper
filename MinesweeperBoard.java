package org.cis1200.minesweeper;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class MinesweeperBoard extends JPanel {
    // array stuff
    private final int gridSize = 10;
    private final int tileSize = 50;
    private final Tiles[][] board;
    // mines
    private final ArrayList<Tiles> mines = new ArrayList<>();
    private int numMines;
    private final JLabel status;
    private final Random randomize = new Random();
    // boolean state of tiles
    private int tilesClickedON = 0;
    private boolean gameOver = false;

    // Getters for testing
    public boolean getGameOver() {
        return gameOver;
    }

    public int getTilesClicked() {
        return tilesClickedON;
    }

    public int getMineCount() {
        return numMines;
    }

    public Tiles[][] getBoard() {
        return board;
    }

    public ArrayList<Tiles> getMines() {
        return mines;
    }

    public MinesweeperBoard(JLabel status, int numMines) {
        this.status = status;
        this.numMines = numMines;
        this.setLayout(new GridLayout(gridSize, gridSize));
        this.setPreferredSize((new Dimension(gridSize * tileSize, gridSize * tileSize)));
        board = new Tiles[gridSize][gridSize];
        createBoard();
        loadGameState();
        saveGameState();
    }

    private void createBoard() {
        for (int r = 0; r < gridSize; r++) {
            for (int c = 0; c < gridSize; c++) {
                Tiles square = new Tiles(r, c, this);
                board[r][c] = square;
                this.add(square);
            }
        }
        placeMine();
    }

    // randomly place the mines
    private void placeMine() {
        int minesRemaining = numMines - mines.size();
        while (minesRemaining > 0) {
            int r = randomize.nextInt(gridSize);
            int c = randomize.nextInt(gridSize);
            Tiles tile = board[r][c];
            if (!mines.contains(tile)) {
                mines.add(tile);
                minesRemaining -= 1;
            }
        }
    }

    public void mineIsThere(int r, int c) {
        // base case
        if (r < 0 || r >= gridSize || c < 0 || c >= gridSize) {
            return;
        }
        Tiles tile = board[r][c];

        // used swing doc for isEnabled to help check mine
        if (!tile.isEnabled() || gameOver) {
            return;
        }
        tile.setEnabled(false);
        tilesClickedON += 1;
        if (mines.contains(tile)) {
            revealMines();
        } else {
            int minesFound = numMinesNextTo(r, c);
            if (minesFound > 0) {
                tile.setText(String.valueOf(minesFound));
            } else {
                revealAdjacentCells(r, c);
            }
        }

        // check is my mines are all gone
        if (tilesClickedON == gridSize * gridSize - mines.size()) {
            gameOver = true;
            status.setText("Winner of Kieran's Game!");
        }
    }

    private void revealMines() {
        for (Tiles mine : mines) {
            mine.setText("o");
        }
        gameOver = true;
        status.setText("game over. hit reset tp try again.");
    }

    private int numMinesNextTo(int i, int j) {
        int count = 0;
        for (int r = -1; r <= 1; r++) {
            for (int c = -1; c <= 1; c++) {
                int r2 = i + r, c2 = j + c;
                if (r2 >= 0 && r2 < gridSize && c2 >= 0 && c2 < gridSize
                        && mines.contains(board[r2][c2])) {
                    count++;
                }
            }
        }
        return count;
    }

    private void revealAdjacentCells(int r, int c) {
        // base case
        if (r < 0 || r >= gridSize || c < 0 || c >= gridSize) {
            return;
        }

        // recursive call to mineisThere
        for (int r2 = -1; r2 <= 1; r2++) {
            for (int c2 = -1; c2 <= 1; c2++) {
                mineIsThere(r + r2, c + c2);
            }
        }
    }

    public void resetGame() {
        removeAll();
        mines.clear();
        tilesClickedON = 0;
        gameOver = false;
        status.setText("Kieran's Board");
        createBoard();
        placeMine();
        revalidate();
        repaint();
    }

    public void loadGameState() {
        File isSaved = new File("game_state.txt");
        if (!isSaved.exists()) {
            JOptionPane.showMessageDialog(this, "No games saved right now");
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(isSaved))) {
            String[] gridSizeLine = reader.readLine().split(" ");
            if (gridSizeLine.length != 2) {
                throw new IOException("incorrect grid error");
            }
            int rows = Integer.parseInt(gridSizeLine[0]);
            int cols = Integer.parseInt(gridSizeLine[1]);
            if (rows != gridSize || cols != gridSize) {
                throw new IOException("Grid size wrong.");
            }
            numMines = Integer.parseInt(reader.readLine());
            removeAll();
            mines.clear();
            tilesClickedON = 0;
            gameOver = false;
            this.setLayout(new GridLayout(rows, cols));

            for (int r = 0; r < rows; r++) {
                String[] rowState = reader.readLine().split(" ");
                if (rowState.length != cols) {
                    throw new IOException("Invalid row format.");
                }
                for (int c = 0; c < cols; c++) {
                    Tiles tile = new Tiles(r, c, this);
                    tile.loadState(rowState[c]);

                    if (tile.isMine()) {
                        mines.add(tile);
                    }
                    if (!tile.isEnabled()) {
                        tilesClickedON += 1;
                    }
                    board[r][c] = tile;
                    this.add(tile);
                }
            }

            status.setText("old game loaded");
            revalidate();
            repaint();
        } catch (IOException e) {
            resetGame();
        }
    }

    public void saveGameState() {
        // save game state in array format for easyier grid read
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("game_state.txt"))) {
            writer.write(gridSize + " " + gridSize);
            writer.newLine();
            writer.write(Integer.toString(numMines));

            // do same for tile
            for (int r = 0; r < gridSize; r++) {
                StringBuilder row = new StringBuilder();
                for (int c = 0; c < gridSize; c++) {
                    Tiles tile = board[r][c];
                    row.append(tile.saveState()).append(" ");
                }
                writer.write(row.toString().trim());
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
}
