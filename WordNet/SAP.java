
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Digraph;

public class SAP {
    private int lengthvw;               // length of the shortest path between V and W
    private int ancestor;             // the nearest ancestor of V and W
    private Digraph copyG;            // save the copy of associated digraph
    private int[] distTov;            // distTov[v] = length of shortest V->v path
    private int[] distTow;            // distTow[v] = length of shortest W->v path
    private boolean[] markedv;        // markedv[v] = is there an V->v path?
    private boolean[] markedw;        // markedw[v] = is there an W->v path?
    private Stack<Integer> stackv;    // store changed auxiliary array1 entries
    private Stack<Integer> stackw;    // store changed auxiliary array1 entries

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) {
            throw new IllegalArgumentException("argument to SAP() is null");
        }
        copyG = new Digraph(G);
        distTov = new int[G.V()];
        distTow = new int[G.V()];
        markedv = new boolean[G.V()];
        markedw = new boolean[G.V()];
        stackv = new Stack<Integer>();
        stackw = new Stack<Integer>();
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        validateVertex(v);
        validateVertex(w);
        compute(v, w);
        return lengthvw;   
    }

    // a common ancestor of v and w that participates in a shortest ancestral path;
    // -1 if no such path
    public int ancestor(int v, int w) {
        validateVertex(v);
        validateVertex(w);    
        compute(v, w);
        return ancestor;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in
    // w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        validateVertices(v);
        validateVertices(w);
        compute(v, w);
        return lengthvw;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such
    // path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        validateVertices(v);
        validateVertices(w);
        compute(v, w);
        return ancestor;
    }

    // using two bfs lockstep from v and w to compute sap
    private void compute(int v, int w) { 
        lengthvw = -1;
        ancestor = -1;
        distTov[v] = 0;
        distTow[w] = 0;
        markedv[v] = true;
        markedw[w] = true;
        stackv.push(v);
        stackw.push(w);
        Queue<Integer> q1 = new Queue<Integer>();
        Queue<Integer> q2 = new Queue<Integer>();
        q1.enqueue(v);
        q2.enqueue(w); 
        bfs(q1, q2);
    }

    // using two bfs lockstep from sources v and sources w to compute sap
    private void compute(Iterable<Integer> v, Iterable<Integer> w) {
        lengthvw = -1;
        ancestor = -1;
        Queue<Integer> q1 = new Queue<Integer>();
        Queue<Integer> q2 = new Queue<Integer>();
        for (int v1 : v) {
            markedv[v1] = true;
            stackv.push(v1);
            distTov[v1] = 0;
            q1.enqueue(v1);
        }
        for (int w1 : w) {
            markedw[w1] = true;
            stackw.push(w1);
            distTow[w1] = 0;
            q2.enqueue(w1);
        }
        bfs(q1, q2);
    }

    // run two bfs alternating back and forth bewteen q1 and q2
    private void bfs(Queue<Integer> q1, Queue<Integer> q2) {
        while (!q1.isEmpty() || !q2.isEmpty()) {
            if (!q1.isEmpty()) {
                int v = q1.dequeue();
                if (markedw[v]) {
                    if (distTov[v] + distTow[v] < lengthvw || lengthvw == -1) {
                        ancestor = v;
                        lengthvw = distTov[v] + distTow[v];
                    }
                }
                // stop adding new vertex to queue if the distance exceeds the lengthvw
                if (distTov[v] < lengthvw || lengthvw == -1) {
                    for (int w : copyG.adj(v)) {
                        if (!markedv[w]) {
                            distTov[w] = distTov[v] + 1;
                            markedv[w] = true;
                            stackv.push(w);
                            q1.enqueue(w);

                            // StdOut.println("push " + w + " into q1");
                        }
                    }
                }
            }
            if (!q2.isEmpty()) {
                int v = q2.dequeue();
                if (markedv[v]) {
                    if (distTov[v] + distTow[v] < lengthvw || lengthvw == -1) {
                        ancestor = v;
                        lengthvw = distTov[v] + distTow[v];
                    }
                }
                // stop adding new vertex to queue if the distance exceeds the lengthvw
                if (distTow[v] < lengthvw || lengthvw == -1) {
                    for (int w : copyG.adj(v)) {
                        if (!markedw[w]) {
                            distTow[w] = distTow[v] + 1;
                            markedw[w] = true;
                            stackw.push(w);
                            q2.enqueue(w);

                            // StdOut.println("push " + w + " into q2");
                        }
                    }
                }
            }
        }
        init();    // reinitialize auxiliary array for next bfs
    }

    // init auxiliary array for bfs
    private void init() {
        while (!stackv.isEmpty()) {
            int v = stackv.pop();
            markedv[v] = false;
        }
        while (!stackw.isEmpty()) {
            int v = stackw.pop();
            markedw[v] = false;
        }
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertex(int v) {
        int V = markedv.length;
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertices(Iterable<Integer> vertices) {
        if (vertices == null) {
            throw new IllegalArgumentException("argument is null");
        }
        int V = markedv.length;
        for (int v : vertices) {
            if (v < 0 || v >= V) {
                throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
            }
        }
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}