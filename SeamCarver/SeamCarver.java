
import java.util.Arrays;

import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

public class SeamCarver {
    private Picture pic;
    private double[][] energyAt;
    private int[][] xAt;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        this.pic = picture;
    }

    // current picture
    public Picture picture() {
        return pic;
    }

    // width of current picture
    public int width() {
        return pic.width();
    }

    // height of current picture
    public int height() {
        return pic.height();
    }

    /// energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x >= width()) {
            throw new IndexOutOfBoundsException();
        }

        if (y < 0 || y >= height()) {
            throw new IndexOutOfBoundsException();
        }

        if (x == 0 || x == width() - 1
            || y == 0 || y == height() - 1) {
            return 195075;
        } else {
            return xGradientSquared(x, y) + yGradientSquared(x, y);
        }
    }

    private int xGradientSquared(int x, int y) {
        int deltared = Math.abs(pic.get(x - 1, y).getRed()- pic.get(x + 1, y).getRed());
        int deltagreen = Math.abs(pic.get(x - 1, y).getGreen()- pic.get(x + 1, y).getGreen());
        int deltablue = Math.abs(pic.get(x - 1, y).getBlue()- pic.get(x + 1, y).getBlue());

        return deltared * deltared + deltagreen * deltagreen + deltablue * deltablue;
    }

    private int yGradientSquared(int x, int y) {
        int deltared = Math.abs(pic.get(x, y - 1).getRed()- pic.get(x, y + 1).getRed());
        int deltagreen = Math.abs(pic.get(x, y - 1).getGreen()- pic.get(x, y + 1).getGreen());
        int deltablue = Math.abs(pic.get(x, y - 1).getBlue()- pic.get(x, y + 1).getBlue());

        return deltared * deltared + deltagreen * deltagreen + deltablue * deltablue;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        // Transpose picture.
        Picture original = pic;
        Picture transpose = new Picture(original.height(), original.width());

        for (int w = 0; w < transpose.width(); w++) {
            for (int h = 0; h < transpose.height(); h++) {
                transpose.set(w, h, original.get(h, w));
            }
        }

        this.pic = transpose;

        // call findVerticalSeam
        int[] seam = findVerticalSeam();

        // Transpose back.
        this.pic = original;

        return seam;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        energyAt = new double[width()][height()];
        xAt = new int[width()][height()];

        for (int x = 0; x < width(); x++) {
            for (int y = 0; y < height(); y++) {
                energyAt[x][y] = Double.POSITIVE_INFINITY;
            }
        }

        for (int x = 0; x < width(); x++) {
            energyAt[x][0] = 195075;
        }

        for (int y = 0; y < height() - 1; y++) {
            for (int x = 0; x < width(); x++) {
                if (x > 0) {
                    relax(x, y, x - 1, y + 1);
                }

                relax(x, y, x, y + 1);

                if (x < width() - 1) {
                    relax(x, y, x + 1, y + 1);
                }
            }
        }

        // find minimum energy path
        double minEnergy = Double.POSITIVE_INFINITY;
        int minEnergyX = -1;
        for (int w = 0; w < width(); w++) {
            if (energyAt[w][height() - 1] < minEnergy) {
                minEnergyX = w;
                minEnergy = energyAt[w][height() - 1];
            }
        }
        assert minEnergyX != -1;

        int[] seam = new int[height()];
        seam[height() - 1] = minEnergyX;
        int prevX = xAt[minEnergyX][height() - 1];

        for (int h = height() - 2; h >= 0; h--) {
            seam[h] = prevX;
            prevX = xAt[prevX][h];
        }

        return seam;
    }

    private void relax(int x1, int y1, int x2, int y2) {
        if (energyAt[x2][y2] > energyAt[x1][y1] + energy(x2, y2)) {
            energyAt[x2][y2] = energyAt[x1][y1] + energy(x2, y2);
            xAt[x2][y2] = x1;
        }
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        // Transpose picture.
        Picture original = pic;
        Picture transpose = new Picture(original.height(), original.width());

        for (int w = 0; w < transpose.width(); w++) {
            for (int h = 0; h < transpose.height(); h++) {
                transpose.set(w, h, original.get(h, w));
            }
        }

        this.pic = transpose;
        transpose = null;
        original = null;

        // call removeVerticalSeam
        removeVerticalSeam(seam);

        // Transpose back.
        original = pic;
        transpose = new Picture(original.height(), original.width());

        for (int w = 0; w < transpose.width(); w++) {
            for (int h = 0; h < transpose.height(); h++) {
                transpose.set(w, h, original.get(h, w));
            }
        }

        this.pic = transpose;
        transpose = null;
        original = null;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null) {
            throw new NullPointerException();
        }

        if (seam.length != height()) {
            throw new IllegalArgumentException();
        }

        Picture original = this.pic;
        Picture carved = new Picture(original.width() - 1, original.height());

        for (int h = 0; h < carved.height(); h++) {
            for (int w = 0; w < seam[h]; w++) {
                carved.set(w, h, original.get(w, h));
            }
            for (int w = seam[h]; w < carved.width(); w++) {
                carved.set(w, h, original.get(w + 1, h));
            }
        }

        this.pic = carved;
    }

    public static void main(String[] args) {
        Picture picture;
        SeamCarver seamCarver;
        int[] seam;

        picture = new Picture("seamCarving/4x6.png");
        // picture = new Picture("seamCarving/6x5.png");

        seamCarver = new SeamCarver(picture);
        // StdOut.println(seamCarver.energy(0, 0));  // should be 195075.0
        // StdOut.println(seamCarver.energy(1, 1));  // should be 56334.0
        for (int i = 0; i < seamCarver.width(); i++) {
            for (int j = 0; j < seamCarver.height(); j++) {
                StdOut.println("energy(" + i + "," + j + "): "
                               + seamCarver.energy(i, j));
            }
        }

        // should be 6
        StdOut.println(seamCarver.width());
        // should be 5
        StdOut.println(seamCarver.height());

        // should be 5
        seam = seamCarver.findVerticalSeam();
        StdOut.println(seam.length);
        StdOut.println(Arrays.toString(seam));
        seamCarver.removeVerticalSeam(seam);

        for (int i = 0; i < seamCarver.width(); i++) {
            for (int j = 0; j < seamCarver.height(); j++) {
                StdOut.println("energy(" + i + "," + j + "): "
                               + seamCarver.energy(i, j));
            }
        }

        seam = seamCarver.findHorizontalSeam();
        StdOut.println(Arrays.toString(seam));
        StdOut.println(seam.length);
        seamCarver.removeHorizontalSeam(seam);
    }
}
