import java.awt.*;
import java.util.Random;
import java.util.ArrayList;
import javax.swing.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.*;
import java.lang.Math;

public class Nothing extends JFrame {
    JLabel statusbar = new JLabel("");
    private class Board extends JPanel{

        private class MineCell extends JButton {
            int row, col, val = 0;
            boolean cov = true, mar = false;
            
            public MineCell(int r, int c){
                row = r;
                col = c;
                setFocusable(false);
                setMargin(new Insets(0, 0, 0, 0));
                setIcon(imgs[10]);
                setRolloverEnabled(false);
            }
        }

        private final int CELL_SIZE = 20;
    
        private final int N_MINES = 40;
        private final int N_ROWS = 16;
        private final int N_COLS = 16;
    
        private final int BOARD_WIDTH = N_COLS * CELL_SIZE;
        private final int BOARD_HEIGHT = N_ROWS * CELL_SIZE;

        private boolean inGame;
        private int minesLeft = N_MINES;
        private int cellsLeft = N_ROWS * N_COLS - N_MINES;
        private ImageIcon[] imgs;
    
        MineCell[][] allCells = new MineCell[N_ROWS][N_COLS];
    
        public Board() {
            setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
    
            imgs = new ImageIcon[13];

    
            for (int i = 0; i < 13; i++) {
    
                var path = "images/" + i + ".png";
                imgs[i] = new ImageIcon(path);
            }

            setLayout(new GridLayout(N_ROWS, N_COLS));
            
            for(int r = 0; r < N_ROWS; r++)
                for(int c = 0; c < N_COLS; c++){
                    MineCell cell = new MineCell(r, c);
                    allCells[r][c] = cell;
                    cell.addMouseListener(new MinesAdapter());
                    add(cell);
                }
            setMines();
            inGame = true;
            statusbar.setText(Integer.toString(minesLeft));
        }

        private void beginGame(){
            minesLeft = N_MINES;
            cellsLeft = N_ROWS * N_COLS - N_MINES;
            for(int r = 0; r < N_ROWS; r++)
                for(int c = 0; c < N_COLS; c++){
                    allCells[r][c].cov = true;
                    allCells[r][c].mar = false;
                    allCells[r][c].val = 0;
                    allCells[r][c].setIcon(imgs[10]);
                }
            setMines();
            inGame = true;
            statusbar.setText(Integer.toString(minesLeft));
        }
        
        private void setMines() {
            var random = new Random();

            int i = 0;

            while (i < N_MINES) {

                int curRow = Math.abs(random.nextInt()) % N_ROWS, curCol = Math.abs(random.nextInt()) % N_COLS;
                if (allCells[curRow][curCol].val != 9) {

                    allCells[curRow][curCol].val = 9;
                    i++;

                    if (curCol > 0) {
                        if (curRow > 0 && allCells[curRow - 1][curCol - 1].val != 9) allCells[curRow - 1][curCol - 1].val++;
                        if (allCells[curRow][curCol - 1].val != 9) allCells[curRow][curCol - 1].val++;
                        if (curRow < N_ROWS - 1 && allCells[curRow + 1][curCol - 1].val != 9) allCells[curRow + 1][curCol - 1].val++;
                    }

                    if (curRow > 0 && allCells[curRow - 1][curCol].val != 9) allCells[curRow - 1][curCol].val++;
                    if (curRow < N_ROWS - 1 && allCells[curRow + 1][curCol].val != 9) allCells[curRow + 1][curCol].val++;

                    if (curCol < N_COLS - 1) {
                        if (curRow > 0 && allCells[curRow - 1][curCol + 1].val != 9) allCells[curRow - 1][curCol + 1].val++;
                        if (allCells[curRow][curCol + 1].val != 9) allCells[curRow][curCol + 1].val++;
                        if (curRow < N_ROWS - 1 && allCells[curRow + 1][curCol + 1].val != 9) allCells[curRow + 1][curCol + 1].val++;
                    }

                }
            }
        }

        private class MinesAdapter extends MouseAdapter {

            @Override
            public void mousePressed(MouseEvent e) {
                MineCell cell = (MineCell) e.getSource();
                
                if(!inGame){
                    inGame = true;
                    beginGame();
                    return;
                }
                if (e.getButton() == MouseEvent.BUTTON3 && !cell.cov ) {
                    int numMarks = 0, numMines = 0;
                    int curRow = cell.row;
                    int curCol = cell.col;
                    if (curRow > 0 ) {
                        if (curCol > 0 && allCells[curRow - 1][curCol - 1].mar) {
                            numMarks++;
                            if(allCells[curRow - 1][curCol - 1].val == 9) numMines++;
                        }
                        if (allCells[curRow - 1][curCol].mar) {
                            numMarks++;
                            if(allCells[curRow - 1][curCol].val == 9) numMines++;
                        }
                        if (curCol < N_COLS-1 && allCells[curRow - 1][curCol + 1].mar) {
                            numMarks++;
                            if(allCells[curRow - 1][curCol + 1].val == 9) numMines++;
                        }
                    }

                    if (curCol > 0 && allCells[curRow][curCol - 1].mar) {
                        numMarks++;
                        if(allCells[curRow][curCol - 1].val == 9) numMines++;
                    }
                    if (curCol < N_COLS-1 && allCells[curRow][curCol+1].mar) {
                        numMarks++;
                        if(allCells[curRow][curCol + 1].val == 9) numMines++;
                    }

                    if (curRow < N_ROWS - 1) {
                        if (curCol > 0 && allCells[curRow + 1][curCol - 1].mar) {
                            numMarks++;
                            if(allCells[curRow + 1][curCol - 1].val == 9) numMines++;
                        }
                        if (allCells[curRow + 1][curCol].mar) {
                            numMarks++;
                            if(allCells[curRow + 1][curCol].val == 9) numMines++;
                        }
                        if (curCol < N_COLS-1 && allCells[curRow + 1][curCol + 1].mar) {
                            numMarks++;
                            if(allCells[curRow + 1][curCol + 1].val == 9) numMines++;
                        }
                    }

                    if(numMarks >= cell.val){
                        revealEmpty(curRow - 1, curCol - 1);
                        revealEmpty(curRow - 1, curCol);
                        revealEmpty(curRow - 1, curCol + 1);
                        revealEmpty(curRow, curCol - 1);
                        revealEmpty(curRow, curCol);
                        revealEmpty(curRow, curCol + 1);
                        revealEmpty(curRow + 1, curCol - 1);
                        revealEmpty(curRow + 1, curCol);
                        revealEmpty(curRow + 1, curCol + 1);
                        if(numMines < cell.val) {
                            revealMines();
                            inGame = false;
                            statusbar.setText("Game Over");
                            return;
                        }
                    }

                 }
                else if(e.getButton() == MouseEvent.BUTTON1 && cell.cov && !cell.mar){
                    if(cell.val == 9){
                        inGame = false;
                        statusbar.setText("Game Over");
                        revealMines();
                    }
                    else revealEmpty(cell.row, cell.col);
                }
                else if(e.getButton() == MouseEvent.BUTTON3 && cell.cov && cell.mar){
                    cell.mar = false;
                    cell.setIcon(imgs[10]);
                    minesLeft++;
                    statusbar.setText(Integer.toString(minesLeft));
                }
                else if(e.getButton() == MouseEvent.BUTTON3 && cell.cov && !cell.mar){
                    if(minesLeft != 0) {
                        cell.mar = true;
                        cell.setIcon(imgs[11]);
                        minesLeft--;
                        statusbar.setText(Integer.toString(minesLeft));
                    }
                    else statusbar.setText("No marks left");
                    
                }
                if(cellsLeft == 0){
                    inGame = false;
                    statusbar.setText("You Won");
                    revealMines();
                }
            }
        }

        void revealEmpty(int curRow, int curCol) {

            if (curRow < 0 || curRow >= N_ROWS || curCol < 0 || curCol >= N_COLS || !allCells[curRow][curCol].cov || allCells[curRow][curCol].mar) return;
            
            cellsLeft--;
            allCells[curRow][curCol].cov = false;
        
            allCells[curRow][curCol].setIcon(imgs[allCells[curRow][curCol].val]);
        
            if (allCells[curRow][curCol].val != 0) return;
        
            revealEmpty(curRow - 1, curCol - 1);
            revealEmpty(curRow - 1, curCol);
            revealEmpty(curRow - 1, curCol + 1);
            revealEmpty(curRow, curCol - 1);
            revealEmpty(curRow, curCol + 1);
            revealEmpty(curRow + 1, curCol - 1);
            revealEmpty(curRow + 1, curCol);
            revealEmpty(curRow + 1, curCol + 1);
        }

        public void revealMines(){
            for(int r = 0; r < N_ROWS; r++)
                for(int c = 0; c < N_COLS; c++)
                    if(!allCells[r][c].mar && allCells[r][c].val == 9) allCells[r][c].setIcon(imgs[9]);
                    else if(allCells[r][c].mar && allCells[r][c].val != 9) allCells[r][c].setIcon(imgs[12]);
        }
    }

    public Nothing(){
        add(new Board());
        add(statusbar, BorderLayout.SOUTH);
        setResizable(false);
        pack();
        
        setIconImage((new ImageIcon("images/wow.png")).getImage());
        setTitle("Minesweeper");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        Nothing ex = new Nothing();
    }
}