
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {
    private final ArrayList<LineSegment> lines = new ArrayList<>();

    public FastCollinearPoints(Point[] points)     // finds all line segments containing 4 or more points
    {
        if (points == null)
            throw new IllegalArgumentException();
        
        checkNullEntries(points);
        checkDuplicateEntries(points);
        
        Point[] copy = points.clone();

        Arrays.sort(copy);

        for(int i=0 ; i<copy.length  ; i++)
        {
            Arrays.sort(copy);

            Arrays.sort(copy,copy[i].slopeOrder());

            // int index=1;
            int first = 1, last = 2;
            while(last < copy.length)
            {
                if(Double.compare(copy[0].slopeTo(copy[first]),copy[0].slopeTo(copy[last])) == 0)
                {
                    last++;
                }
                else
                {
                    if(last - first >= 3 && copy[0].compareTo(copy[first]) < 0)
                    {                     
                        lines.add(new LineSegment(copy[0], copy[last-1]));
                    }
                    first = last;
                }
            }
            if(last - first >= 3 && copy[0].compareTo(copy[first]) < 0)
            {                     
                lines.add(new LineSegment(copy[0], copy[last-1]));
            }
            
        }
    }
    public int numberOfSegments()        // the number of line segments
    {
        return lines.size();
    }
    public LineSegment[] segments()                // the line segments
    {
        return  lines.toArray(new LineSegment[lines.size()]);
    }

    private void checkNullEntries(Point[] points) {
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException();
            }
        }
    }

    private void checkDuplicateEntries(Point[] points) {
        for (int i = 0; i < points.length - 1; i++) {
            for (int j = i+1; j < points.length; j++) {
                if (points[i].compareTo(points[j]) == 0) {
                    throw new IllegalArgumentException();
                }
            }
        }
    }
    // testing site
    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
                StdOut.println(segment);
                segment.draw();
        }
        StdDraw.show();
    }
}