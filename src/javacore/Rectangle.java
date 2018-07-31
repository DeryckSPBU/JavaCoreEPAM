package javacore;

class Rectangle {
    class Coordinate {
        double x;
        double y;
        Coordinate(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    double area(Coordinate c1, Coordinate c2) {
        return area(c1.x, c1.y, c2.x, c2.y);
    }

    double area(double x1, double y1, double x2, double y2) {
        return Math.abs(x1 - x2) * Math.abs(y1 - y2);
    }

    double intersectionArea(double r1x1, double r1y1, double r1x2, double r1y2, double r2x1, double r2y1, double r2x2, double r2y2) {
        if ((r1x1 < r2x1 && r1x1 < r2x2 && r1x2 < r2x1 && r1x2 < r2x2)
                || (r1y1 < r2y1 && r1y1 < r2y2 && r1y2 < r2y1 && r1y2 < r2y2)) {
            return 0;
        }else {
            double x1 = Math.min(Math.max(r1x1, r1x2), Math.max(r2x1, r2x2));
            double x2 = Math.max(Math.min(r1x1, r1x2), Math.min(r2x1, r2x2));
            double y1 = Math.min(Math.max(r1y1, r1y2), Math.max(r2y1, r2y2));
            double y2 = Math.max(Math.min(r1y1, r1y2), Math.min(r2y1, r2y2));
            return area(x1, y1, x2, y2);
        }
    }

    double intersectionArea(Coordinate r1c1, Coordinate r1c2, Coordinate r2c1, Coordinate r2c2) {
        return intersectionArea(r1c1.x, r1c1.y, r1c2.x, r1c2.y, r2c1.x, r2c1.y, r2c2.x, r2c2.y);
    }

    public static void main(String[] args) {
        Rectangle rectangle = new Rectangle();
        System.out.println(rectangle.area(-1, 4 ,1 ,1));
        System.out.println(rectangle.intersectionArea(1,4, 2,8,2,5,9,11));
    }
}
