
import java.util.Scanner;
import java.lang.IllegalArgumentException;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;


public class Percolation {
    
    private int top ,bottom , n;
    private WeightedQuickUnionUF board;
    private boolean sitestatus[][];
    private int noofopensites;
    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n)
    {
        if(n<=0) throw new IllegalArgumentException();

        board = new WeightedQuickUnionUF(n*n +2);
        this.n = n;
        top = n*n;
        bottom = n*n + 1;
        sitestatus = new boolean[n][n];
        noofopensites = 0;
    }
    // opens the site (row, col) if it is not open already
    public void open(int row, int col)
    {
        if(row < 1 || col < 1) throw new IllegalArgumentException();

        int r = row-1 , c = col-1;
        if(sitestatus[r][c])
        {
            return;
        }
        sitestatus[r][c] = true;
        noofopensites++;
        int cur = (r) * n + (c);
        if(r == 0)
        {
            board.union(top, cur);
        }
        
        if(r == n-1)
        {
            board.union(bottom, cur);
        }

        if(r+1<n && sitestatus[r+1][c])
        {
            board.union((r+1)*n + c, cur);
        }

        if(r-1 >=0 && sitestatus[r-1][c])
        {
            board.union((r-1)*n + c , cur);
        }

        if(c+1<n && sitestatus[r][c+1])
        {
            board.union(r*n + c+1, cur);
        }

        if(c-1 >=0 && sitestatus[r][c-1])
        {
            board.union(r*n + c-1 , cur);
        }

    }
    // is the site (row, col) open?
    public boolean isOpen(int row, int col)
    {
        if(row < 1 || col < 1) throw new IllegalArgumentException();

        return sitestatus[row-1][col-1];
    }
    // is the site (row, col) full?
    public boolean isFull(int row, int col)
    {
        if(row < 1 || col < 1) throw new IllegalArgumentException();

        return board.find((row-1)*n + col-1) == board.find(top);
    }
    // returns the number of open sites
    public int numberOfOpenSites()
    {
        return noofopensites;
    }
    // does the system percolate?
    public boolean percolates()
    {
        return board.find(top) == board.find(bottom);
    }

    // testing unit
    public static void main(String args[])
    {
        int n;
        Scanner sc = new Scanner(System.in);
        n = sc.nextInt();
        Percolation percolation =new Percolation(n);
        while( ! percolation.percolates())
        {
            int r,c;
            r = sc.nextInt();
            c = sc.nextInt();
            percolation.open(r,c);
        }
        System.out.println("System percolates");
        sc.close();
    }
}
