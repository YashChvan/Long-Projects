
import java.util.ArrayList;

public class BruteCollinearPoints {
    ArrayList<LineSegment> lines;
    public BruteCollinearPoints(Point[] points)    // finds all line segments containing 4 points
    {
        for(int i=0 ; i<points.length -1 ; i++)
        {
            int index = i;
            for(int j=i+1 ; j<points.length ; j++)
            {
                if(points[index].compareTo(points[j]) >0)
                {
                    index = j;
                }
            }
            Point temp = points[index];
            points[index] = points[i];
            points[i] = temp;
        }

        for(int p=0 ; p<points.length-3 ; p++)
        {
            for(int q=p+1 ; q<points.length-2 ; q++)
            {
                for(int r=q+1 ; r<points.length-1 ; r++)
                {
                    if(points[p].slopeTo(points[q]) != points[p].slopeTo(points[r]))
                    {
                        continue;
                    }
                    for(int s=r+1 ; s<points.length ; s++)
                    {
                        if(points[p].slopeTo(points[s]) == points[p].slopeTo(points[r]))
                        {
                            lines.add(new LineSegment(points[p], points[s]));
                        }
                    }
                }
            }
        }
    }
    public int numberOfSegments()        // the number of line segments
    {
        return lines.size();
    }
    public LineSegment[] segments() // the line segments
    {
        LineSegment[] line = new LineSegment[lines.size()];
        for(int i=0 ; i<lines.size() ; i++)
        {
            line[i] = lines.get(i);
        }
        return line;
    }
}
