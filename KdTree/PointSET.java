
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
 
public class PointSET {
    private final Set<Point2D> points;

    public PointSET()                               // construct an empty set of points 
    {
       points = new TreeSet<>();
    }
    public boolean isEmpty()                      // is the set empty? 
    {
        return points.isEmpty();
    }
    public int size()                         // number of points in the set 
    {
        return points.size();
    }
    public void insert(Point2D p)              // add the point to the set (if it is not already in the set)
    {
        if (p == null) throw new IllegalArgumentException();

        if(! points.contains(p))
        {
            points.add(p);
        }
    }
    public boolean contains(Point2D p)            // does the set contain point p? 
    {
        if (p == null) throw new IllegalArgumentException();

        return points.contains(p);
    }
    public void draw()                         // draw all points to standard draw 
    {
        for (Point2D p : points){
            p.draw();
        }
    }
    public Iterable<Point2D> range(RectHV rect)             // all points that are inside the rectangle (or on the boundary) 
    {
        if (rect == null) throw new IllegalArgumentException();

        LinkedList<Point2D> inrange = new LinkedList<>();
        for(Point2D p : points)
        {
            if(rect.contains(p))
            {
                inrange.add(p);
            }
        }
        return inrange;
    }
    public Point2D nearest(Point2D p)             // a nearest neighbor in the set to point p; null if the set is empty 
    {
        if(points.isEmpty()) return null;
        if (p == null) throw new IllegalArgumentException();

        double min_dis = Double.MAX_VALUE; 
        Point2D min_point = p;
        for(Point2D point : points)
        {
            if(point.distanceSquaredTo(p) < min_dis)
            {
                min_point = point;
                min_dis = point.distanceSquaredTo(p);
            }
        }
        return min_point;
    }
 
    // public static void main(String[] args)                  // unit testing of the methods (optional) 
 }