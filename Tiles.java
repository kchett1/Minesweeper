package org.cis1200.minesweeper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Tiles extends JButton {
    private final int row;
    private final int col;
    private final MinesweeperBoard board;
    private boolean flag = false;
    private boolean revealed = false;
    private boolean mine = false;

    public Tiles(int row, int col, MinesweeperBoard board) {
        this.row = row;
        this.col = col;
        this.board = board;
        setFont(new Font("Times New Roman", Font.BOLD, 30));
        setFocusable(false);
        setBackground(Color.BLACK);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (revealed) {
                    return;
                }

                if (SwingUtilities.isLeftMouseButton(e) && !flag) {
                    board.mineIsThere(row, col);
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    flagged();
                }
            }
        });
    }

    private void flagged() {
        if (revealed) {
            return;
        }

        flag = !flag;
        setText(flag ? "x" : "");
    }

    public void isRevealed(int adjacentMines) {
        if (revealed) {
            return;
        }

        revealed = true;
        setEnabled(false);
        setBackground(Color.WHITE);

        if (mine) {
            setText("o");
            setBackground(Color.RED);
        } else if (adjacentMines > 0) {
            setText(String.valueOf(adjacentMines));
        }
    }

    public String saveState() {
        return row + "," + col + "," + mine + "," + flag + "," + revealed;
    }

    public void loadState(String state) {
        String[] parts = state.split(",");
        mine = Boolean.parseBoolean(parts[2]);
        flag = Boolean.parseBoolean(parts[3]);
        revealed = Boolean.parseBoolean(parts[4]);

        if (flag) {
            setText("x");
            setBackground(Color.YELLOW);
        }

        if (revealed) {
            isRevealed(0);
        }
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean isMine() {
        return mine;
    }
}
