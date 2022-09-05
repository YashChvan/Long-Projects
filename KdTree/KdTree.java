
import edu.princeton.cs.algs4.RectHV;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
    private Node root;
    private int size;

    public KdTree()                              
    {
        root = null;
        size = 0;
    }
    public boolean isEmpty()                    
    {
        return size == 0;
    }
    public int size()                        
    {
        return size;
    }
    public void insert(Point2D p)              
    {
        if(p == null) throw new IllegalArgumentException();

        if(root == null)
        {
            root = new Node(p, 0, null, null);
            size++;
            return;
        }
        if(!contains(p))
        {
            insert(root, p, 0, true);
            size++;
        }
    }
    public boolean contains(Point2D p)            
    {
        if(p == null) throw new IllegalArgumentException();

        return contains(root, p, true);
    }
    public void draw()                         
    {
        draw(root, new RectHV(0, 0, 1, 1));
    }
    public Iterable<Point2D> range(RectHV rect)             
    {
        if(rect == null) throw new IllegalArgumentException(); 
        List<Point2D> inrange = new ArrayList<>();
        range(root, inrange, rect);
        return inrange;

    }
    public Point2D nearest(Point2D p)             
    {
        if(p == null) throw new IllegalArgumentException();
        // if(root == null) throw new 
        return closest(root, p, root.point);
    }

    private class Node
    {
        final Point2D point;
        final int level;
        Node left;
        Node right;

        public Node(Point2D p, int level, Node l, Node r)
        {
            this.point = p;
            this.level =  level;
            this.left = l;
            this.right = r;
        }

    }

    private void insert(Node head, Point2D p, int level, boolean usex)
    {
        if(usex)
        {
            if(p.x() < head.point.x())
            {
                if(head.left == null)
                {
                    head.left = new Node(p, level+1, null, null);
                    return;
                }
                else
                {
                    insert(head.left, p, level+1,!usex );
                }
            }
            else
            {
                if(head.right == null)
                {
                    head.right = new Node(p, level+1, null, null);
                    return;
                }
                else
                {
                    insert(head.right, p, level+1,!usex );
                }
            }
        }
        else
        {
            if(p.y() < head.point.y())
            {
                if(head.left == null)
                {
                    head.left = new Node(p, level+1, null, null);
                    return;
                }
                else
                {
                    insert(head.left, p, level+1,!usex );
                }
            }
            else
            {
                if(head.right == null)
                {
                    head.right = new Node(p, level+1, null, null);
                    return;
                }
                else
                {
                    insert(head.right, p, level+1,!usex );
                }
            }
        }
    }

    private boolean contains(Node head, Point2D p, Boolean usex)
    {
        if(root == null)
        {
            return false;
        }
        if (head.point.equals(p))
        {
            return true;
        }
    
        if(usex)
        {
            if(p.x() < head.point.x())
            {
                if(head.left == null)
                {
                    return false;
                }
                else
                {
                    return contains(head.left, p, !usex );
                }
            }
            else
            {
                if(head.right == null)
                {
                    return false;
                }
                else
                {
                    return contains(head.right, p,!usex );
                }
            }
        }
        else
        {
            if(p.y() < head.point.y())
            {
                if(head.left == null)
                {
                    return false;
                }
                else
                {
                    return contains(head.left, p, !usex );
                }
            }
            else
            {
                if(head.right == null)
                {
                    return false;
                }
                else
                {
                    return contains(head.right, p, !usex );
                }
            }
        }
        
    }

    private void range(Node head, List<Point2D> l, RectHV rec)
    {
        if(head == null)
        {
            return;
        }

        if(rec.contains(head.point))
        {
            l.add(head.point);
        }

        if(head.level % 2 == 0) // we check for vertical line intersection
        {
            Point2D p = head.point;
            if(rec.xmin() <= p.x() && rec.xmax() >= p.x())
            {
                range(head.left, l, rec);
                range(head.right, l, rec);
            }
            else if(p.x() < rec.xmin())
            {
                range(head.right, l, rec);
            }
            else
            {
                range(head.left, l, rec);
            }
        }
        else
        {
            Point2D p = head.point;
            if(rec.ymin() <= p.y() && rec.ymax() >= p.y())
            {
                range(head.left, l, rec);
                range(head.right, l, rec);
            }
            else if(p.y() < rec.ymin())
            {
                range(head.right, l, rec);
            }
            else
            {
                range(head.left, l, rec);
            }
        }
    }

    private Point2D closest(Node head, Point2D p, Point2D min_pt)
    {
        if(head == null)
        {
            return min_pt;
        }

        if(head.level % 2 == 0)
        {
            if(head.point.x() <= p.x())
            {
                Point2D nptr = closest(head.right, p, head.point.distanceTo(p) < min_pt.distanceTo(p) ? head.point : min_pt);
                if(nptr.distanceTo(p) > Math.abs(head.point.x() - p.x()))
                {
                    Point2D newmin = head.point.distanceTo(p) < min_pt.distanceTo(p) ? head.point : min_pt;
                    Point2D nptl = closest(head.left, p,  nptr.distanceTo(p) < newmin.distanceTo(p) ? nptr : newmin);
                    return nptl.distanceTo(p) > nptr.distanceTo(p) ? nptr : nptl;
                }
                else
                {
                    return nptr;
                }
                
            }
            else
            {
                Point2D nptl = closest(head.left, p, head.point.distanceTo(p) < min_pt.distanceTo(p) ? head.point : min_pt);
                if(nptl.distanceTo(p) > Math.abs(head.point.x() - p.x()))
                {
                    Point2D newmin = head.point.distanceTo(p) < min_pt.distanceTo(p) ? head.point : min_pt;
                    Point2D nptr = closest(head.right, p, nptl.distanceTo(p) < newmin.distanceTo(p) ? nptl : newmin);
                    return nptl.distanceTo(p) > nptr.distanceTo(p) ? nptr : nptl;
                }
                else
                {
                    return nptl;
                }
            }
        }
        else
        {
            if(head.point.y() <= p.y())
            {
                Point2D nptr = closest(head.right, p, head.point.distanceTo(p) < min_pt.distanceTo(p) ? head.point : min_pt);
                if(nptr.distanceTo(p) > Math.abs(head.point.y() - p.y()))
                {
                    Point2D newmin = head.point.distanceTo(p) < min_pt.distanceTo(p) ? head.point : min_pt;
                    Point2D nptl = closest(head.left, p, nptr.distanceTo(p) < newmin.distanceTo(p) ? nptr : newmin);
                    return nptl.distanceTo(p) > nptr.distanceTo(p) ? nptr : nptl;
                }
                else
                {
                    return nptr;
                }
                
            }
            else
            {
                Point2D nptl = closest(head.left, p, head.point.distanceTo(p) < min_pt.distanceTo(p) ? head.point : min_pt);
                if(nptl.distanceTo(p) > Math.abs(head.point.y() - p.y()))
                {
                    Point2D newmin = head.point.distanceTo(p) < min_pt.distanceTo(p) ? head.point : min_pt;
                    Point2D nptr = closest(head.right, p, nptl.distanceTo(p) < newmin.distanceTo(p) ? nptl : newmin);
                    return nptl.distanceTo(p) > nptr.distanceTo(p) ? nptr : nptl;
                }
                else
                {
                    return nptl;
                }
            }
        }
    }

    private void draw(Node n, RectHV rec){
        if (n == null){
            return;
        }
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.02);
        n.point.draw();

        StdDraw.setPenRadius(0.001);
        if (n.level % 2 == 0){
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(n.point.x(), rec.ymin(), n.point.x(), rec.ymax());
            draw(n.left, new RectHV(rec.xmin(), rec.ymin(), n.point.x(), rec.ymax()));
            draw(n.right, new RectHV(n.point.x(), rec.ymin(), rec.xmax(), rec.ymax()));
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(rec.xmin(), n.point.y(), rec.xmax(), n.point.y());
            draw(n.left, new RectHV(rec.xmin(), rec.ymin(), rec.xmax(), n.point.y()));
            draw(n.right, new RectHV(rec.xmin(), n.point.y(), rec.xmax(), rec.ymax()));
        }
    }

    //testing site

    public static void main(String[] args) {
        KdTree kdTree = new KdTree();
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        while(n-- > 0)
        {
            double x,y;
            x = sc.nextDouble();
            y = sc.nextDouble();
            kdTree.insert(new Point2D(x, y));
        }
        System.out.println(kdTree.contains(new Point2D(0.4, 0.7)));
        for(Point2D p :  kdTree.range(new RectHV(0, 0, 0.5, 0.5)))
        {
            System.out.println(p.toString());
        }
        System.out.println(kdTree.nearest(new Point2D(0.417, 0.362)));
        // System.out.println(kdTree.nearest(new Point2D(0.684, 0.73)));

    }
}
