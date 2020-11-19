package com.company;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Board
{
    private final int[][] tiles;
    private final int n;
    private int posZero;

    //0 if initial, 1 if 0 came from down to up, 2 if right to left, 3 if up to down, 4 if left to right
    private int moveType;

    public Board(int[][] tiles)
    {
        this.tiles = tiles;
        this.n = tiles.length;
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if (tiles[i][j] == 0)
                    posZero = i * n + j;
        moveType = 0;
    }
    public int getMoveType()
    {
        return moveType;
    }
    private void changeMoveType(int moveType)
    {
        this.moveType = moveType;
    }
    public String toString()
    {
        String s = n + "\n";
        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < n; j++)
                s += tiles[i][j] + "\t";
            s += "\n";
        }
        return s;
    }
    public int dimension()
    {
        return n;
    }
    public int hamming()
    {
        int c = 0, count = 0;
        final int N = n * n;
        for (; c < N; c++)
        {
            int row = c / n;
            int col = c % n;
            if (tiles[row][col] == 0)
                continue;
            if (tiles[row][col] != c + 1)
                count++;
        }
        return count;
    }
    public int manhattan()
    {
        int c = 0, count = 0;
        final int N = n * n;
        for (; c < N; c++)
        {
            int row = c / n;
            int col = c % n;
            if (tiles[row][col] == 0)
                continue;
            if (tiles[row][col] != c + 1)
            {
                int val = tiles[row][col] - 1;
                int rowdash = val / n;
                int coldash = val % n;
                count += Math.abs(row - rowdash) + Math.abs(col - coldash);
            }
        }
        return count;
    }
    public boolean isGoal()
    {
        return this.hamming() == 0;
    }
    public boolean equals(Object y)
    {
        if (this == y)
            return true;
        if (y == null)
            return false;
        if (this.getClass() != y.getClass())
            return false;

        Board that = (Board) y;
        if (this.n != that.n)
            return false;
        int N = this.n * this.n;
        for (int i = 0; i < N; i++)
            if (this.tiles[i] != that.tiles[i])
                return false;
        return true;
    }
    private Board createCopy()
    {
        int[][] newTiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                newTiles[i][j] = this.tiles[i][j];
        Board board = new Board(newTiles);
        board.posZero = this.posZero;
        board.changeMoveType(0);
        return board;
    }

    public Iterable<Board> neighbors()
    {
        return new BoardIterable(this);
    }
    private class BoardIterable implements Iterable<Board>
    {
        private final Board obj;
        public BoardIterable(Board obj)
        {
            this.obj = obj;
        }
        public Iterator<Board> iterator()
        {
            return new BoardIterator(obj);
        }
        private class BoardIterator implements Iterator<Board>
        {
            private int current;
            private int neiCount;
            private final Board[] iterBoard;

            public BoardIterator(Board obj)
            {
                int row = posZero / n;
                int col = posZero % n;
                neiCount = 4;
                if (row == 0)
                    neiCount--;
                if (col == 0)
                    neiCount--;
                if (row == n - 1)
                    neiCount--;
                if (col == n - 1)
                    neiCount--;
                iterBoard = new Board[neiCount];
                current = 0;
                makeIterBoard(obj);
            }
            private void makeClone(Board obj)
            {
                for (int k = 0; k < neiCount; k++)
                {
                    iterBoard[k] = obj.createCopy();
                }
            }
            private void makeIterBoard(Board obj)
            {
                makeClone(obj);
                int row = posZero / n;
                int col = posZero % n;
                int i = 0;
                if (row != 0)
                {
                    int temp = iterBoard[i].tiles[row][col];
                    iterBoard[i].tiles[row][col] = iterBoard[i].tiles[row - 1][col];
                    iterBoard[i].tiles[row - 1][col] = temp;
                    iterBoard[i].changeMoveType(1);
                    iterBoard[i].posZero = (row - 1) * n + col;
                    i++;
                }
                if (col != 0)
                {
                    int temp = iterBoard[i].tiles[row][col];
                    iterBoard[i].tiles[row][col] = iterBoard[i].tiles[row][col - 1];
                    iterBoard[i].tiles[row][col - 1] = temp;
                    iterBoard[i].changeMoveType(2);
                    iterBoard[i].posZero = (row) * n + col - 1;
                    i++;
                }
                if (row != n - 1)
                {
                    int temp = iterBoard[i].tiles[row][col];
                    iterBoard[i].tiles[row][col] = iterBoard[i].tiles[row + 1][col];
                    iterBoard[i].tiles[row + 1][col] = temp;
                    iterBoard[i].changeMoveType(3);
                    iterBoard[i].posZero = (row + 1) * n + col;
                    i++;
                }
                if (col != n - 1)
                {
                    int temp = iterBoard[i].tiles[row][col];
                    iterBoard[i].tiles[row][col] = iterBoard[i].tiles[row][col + 1];
                    iterBoard[i].tiles[row][col + 1] = temp;
                    iterBoard[i].changeMoveType(4);
                    iterBoard[i].posZero = (row) * n + col + 1;
                    i++;
                }
            }
            public boolean hasNext()
            {
                return current < neiCount;
            }
            public void remove()
            {
                throw new UnsupportedOperationException("UnsupportedOperationException");
            }
            public Board next()
            {
                if (iterBoard[current] == null)
                    throw new NoSuchElementException("ReachedEnd");
                Board board = iterBoard[current];
                current++;
                return board;
            }
        }
    }

    public Board twin()
    {
        Board board = this.createCopy();
        if (tiles[0][0] != 0 && tiles[0][1] != 0)
        {
            int temp = board.tiles[0][0];
            board.tiles[0][0] = board.tiles[0][1];
            board.tiles[0][1] = temp;
        }
        else
        {
            int temp = board.tiles[1][0];
            board.tiles[1][0] = board.tiles[1][1];
            board.tiles[1][1] = temp;
        }
        return board;
    }


    public static void main(String[] args)
    {
        int n = 3;
        //int[][] tiles = {{8, 1, 3}, {4, 0, 2}, {7, 6, 5}};
        int[][] tiles = {{1, 2, 3}, {4, 5, 6}, {0, 7, 8}};
        Board b = new Board(tiles);
        System.out.println(b.toString());
        //System.out.println(b.hamming());
        //System.out.println(b.manhattan());
        Iterable<Board> ble= b.neighbors();
        Iterator<Board> it = ble.iterator();
        while (it.hasNext())
        {
            Board board = it.next();
            System.out.println(board.toString());
        }
        //System.out.println(b.twin().toString());
        //System.out.println(b.toString());
    }
}
