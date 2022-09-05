
import java.lang.IllegalArgumentException;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private int numberoftrials;
    private double resultoftrial[];

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials)
    {
        if(n<1 || trials<1) throw new IllegalArgumentException();
        
        numberoftrials = trials;
        resultoftrial = new double[trials];
        for(int t =0 ; t < trials ; t++)
        {
            Percolation p = new Percolation(n);
            while(!p.percolates())
            {
                int x = StdRandom.uniform(1, n+1);
                int y = StdRandom.uniform(1, n+1);
                p.open(x, y);
            }
            resultoftrial[t] = (double)(numberoftrials) / (n*n);
        }

    }

    // sample mean of percolation threshold
    public double mean()
    {
        return StdStats.mean(resultoftrial);
    }

    // sample standard deviation of percolation threshold
    public double stddev()
    {
        return StdStats.stddev(resultoftrial);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo()
    {
        return mean() - ((1.96 * stddev()) / Math.sqrt(numberoftrials));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi()
    {
        return mean() + ((1.96 * stddev()) / Math.sqrt(numberoftrials));
    }

    public static void main(String[] args) {
        int gridSize = 2;
        int trialCount = 100000;
        if (args.length >= 2) {
            gridSize = Integer.parseInt(args[0]);
            trialCount = Integer.parseInt(args[1]);
        }
        PercolationStats ps = new PercolationStats(gridSize, trialCount);

        String confidence = ps.confidenceLo() + ", " + ps.confidenceHi();
        StdOut.println("mean                    = " + ps.mean());
        StdOut.println("stddev                  = " + ps.stddev());
        StdOut.println("95% confidence interval = " + confidence);
    }
}
