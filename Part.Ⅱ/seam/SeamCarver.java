import edu.princeton.cs.algs4.Picture;

public class SeamCarver {

    private static final double MAX_ENERGY = 1000;
    private static final int GetRed = 0xFF0000;
    private static final int GetGreen = 0xFF00;
    private static final int GetBlue = 0xFF;
    private int width;
    private int height;
    private Picture picture;
    private double[][] grid;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException();
        }
        mutate(picture);
    }

    private int getSquDiffRGB(int c0, int r0, int c1, int r1) {
        int rgb0 = picture.getRGB(c0, r0);
        int rgb1 = picture.getRGB(c1, r1);
        int r = Math.abs(((rgb0 & GetRed) >> 16) - ((rgb1 & GetRed) >> 16));
        int g = Math.abs(((rgb0 & GetGreen) >> 8) - ((rgb1 & GetGreen) >> 8));
        int b = Math.abs((rgb0 & GetBlue) - (rgb1 & GetBlue));
        return r*r + g*g + b*b;
    }

    // current picture
    public Picture picture() {
        return picture;
    }

    // width of current picture
    public int width() {
        return width;
    }

    // height of current picture
    public int height() {
        return height;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            throw new IllegalArgumentException();
        }
        return grid[y][x];
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        if (height <= 1) {
            throw new IllegalArgumentException();
        }
        double[][] distTo = new double[width][height];
        int[][] offset = new int[width][height]; //similar to edgeTo
        for (int c = 1; c < width; c++) {
            for (int r = 0; r < height; r++) {
                double down = Double.POSITIVE_INFINITY;
                if (r > 0) {down = distTo[c-1][r-1];}
                double upper = Double.POSITIVE_INFINITY;
                if (r+1 < height) {upper = distTo[c-1][r+1];}
                int off = MIN(down, distTo[c-1][r], upper);
                distTo[c][r] = distTo[c-1][r+off] + grid[c][r];
                offset[c][r] = off;
            }
        }
        double min = Double.POSITIVE_INFINITY;
        int row = -1;
        for (int i = 0; i < height; i++) {
            if (distTo[width-1][i] < min) {
                min = distTo[width-1][i];
                row = i;
            }
        }
        int[] ans = new int[width];
        int tmp = row;
        for (int i = width-1; i >= 0; i--) {
            ans[i] = tmp;
            tmp += offset[i][tmp];
        }
        return ans;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        if (width <= 1) {
            throw new IllegalArgumentException();
        }
        double[][] distTo = new double[width][height];
        int[][] offset = new int[width][height]; //similar to edgeTo
        for (int r = 1; r < height; r++) {
            for (int c = 0; c < width; c++) {
                double left = Double.POSITIVE_INFINITY;
                if (c > 0) {left = distTo[c-1][r-1];}
                double right = Double.POSITIVE_INFINITY;
                if (c+1 < width) {right = distTo[c+1][r-1];}
                int off = MIN(left, distTo[c][r-1], right);
                distTo[c][r] = distTo[c+off][r-1] + grid[c][r];
                offset[c][r] = off;
            }
        }
        double min = Double.POSITIVE_INFINITY;
        int col = -1;
        for (int i = 0; i < width; i++) {
            if (distTo[i][height-1] < min) {
                min = distTo[i][height-1];
                col = i;
            }
        }
        int[] ans = new int[height];
        int tmp = col;
        for (int i = height-1; i >= 0; i--) {
            ans[i] = tmp;
            tmp += offset[tmp][i];
        }
        return ans;
    }

    private int MIN(double x, double y, double z) {
        if (x <= y && x <= z) {
            return -1;
        } else if (y <= z) {
            return 0;
        } else {
            return 1;
        }
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null || seam.length != width) {
            throw new IllegalArgumentException();
        }
        Picture pic = new Picture(width, height-1);
        for (int c = 0; c < width; c++) {
            for (int r = 0; r < seam[c]; r++) {
                pic.set(c, r, picture.get(c, r));
            }
            for (int r = seam[c]+1; r < height; r++) {
                pic.set(c, r-1, picture.get(c, r));
            }
        }
        mutate(pic);
        picture.show();
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null || seam.length != height) {
            throw new IllegalArgumentException();
        }
        Picture pic = new Picture(width-1, height);
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < seam[r]; c++) {
                pic.set(c, r, picture.get(c, r));
            }
            for (int c = seam[r]+1; c < width; c++) {
                pic.set(c-1, r, picture.get(c, r));
            }
        }
        mutate(pic);
        picture.show();
    }

    private void mutate(Picture picture) {
        this.picture = picture;
        width = picture.width();
        height = picture.height();
        grid = new double[width][height];
        // calculate energies of all nodes
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (i == 0 || i == width-1 || j == 0 || j == height-1) {
                    grid[i][j] = MAX_ENERGY;
                } else {
                    grid[i][j] = Math.sqrt(
                            getSquDiffRGB(i, j, i-1, j) +
                            getSquDiffRGB(i, j, i+1, j) +
                            getSquDiffRGB(i, j, i, j-1) +
                            getSquDiffRGB(i, j, i, j-1)
                    );
                }
            }
        }
    }

    //  unit testing (optional)
    public static void main(String[] args) {

    }

}