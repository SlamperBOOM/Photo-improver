package model;

public class DotPoint {
    private final int x;
    private final int y;

    public DotPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public DotPoint(double x, double y){
        this.x = (int)Math.round(x);
        this.y = (int)Math.round(y);
    }

    public double getDistance(DotPoint otherPoint){
        double difX = this.x - otherPoint.x;
        double difY = this.y - otherPoint.y;

        return Math.sqrt(difX*difX+difY*difY);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public DotPoint subtract(DotPoint point){
        return new DotPoint(x-point.x, y-point.y);
    }

    public DotPoint operate(double[][] A){
        return new DotPoint(A[0][0]*x+A[0][1]*y, A[1][0]*x+A[1][1]*y);
    }

    @Override
    public String toString() {
        return "DotPoint{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
