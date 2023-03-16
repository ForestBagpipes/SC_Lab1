/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package P2.turtle;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;

public class TurtleSoup {

    /**
     * Draw a square.
     *
     * @param turtle     the turtle context
     * @param sideLength length of each side
     */
    //根据边长画一个正方形
    public static void drawSquare(P2.turtle.Turtle turtle, int sideLength) {
        for (int i = 0; i < 4; i++) {
            turtle.forward(sideLength);
            turtle.turn(90);
        }
    }

    /**
     * Determine inside angles of a regular polygon.
     * <p>
     * There is a simple formula for calculating the inside angles of a polygon;
     * you should derive it and use it here.
     *
     * @param sides number of sides, where sides must be > 2
     * @return angle in degrees, where 0 <= angle < 360
     */
    //已知边数，返回多边形每个角的度数
    public static double calculateRegularPolygonAngle(int sides) {
        return (double) 180.0 * (1.0 - 2.0 / sides);
    }

    /**
     * Determine number of sides given the size of interior angles of a regular polygon.
     * <p>
     * There is a simple formula for this; you should derive it and use it here.
     * Make sure you *properly round* the answer before you return it (see java.lang.Math).
     * HINT: it is easier if you think about the exterior angles.
     *
     * @param angle size of interior angles in degrees, where 0 < angle < 180
     * @return the integer number of sides
     */
    //已知多边形的一个内角度数，求多边形的边数
    public static int calculatePolygonSidesFromAngle(double angle) {
        return (int) Math.round(360 / ((double) 180.0 - angle)); //调用函数进行四舍五入
    }

    /**
     * Given the number of sides, draw a regular polygon.
     * <p>
     * (0,0) is the lower-left corner of the polygon; use only right-hand turns to draw.
     *
     * @param turtle     the turtle context
     * @param sides      number of sides of the polygon to draw
     * @param sideLength length of each side
     */
    //根据给出的边数和边长，从（0,0）出发画一个正多边形
    public static void drawRegularPolygon(P2.turtle.Turtle turtle, int sides, int sideLength) {
        for (int i = 0; i < sides; i++) {
            turtle.forward(sideLength);
            double ang = calculateRegularPolygonAngle(sides);
            turtle.turn(180.0 - ang);
        }
    }

    /**
     * Given the current direction, current location, and a target location, calculate the Bearing
     * towards the target point.
     * <p>
     * The return value is the angle input to turn() that would point the turtle in the direction of
     * the target point (targetX,targetY), given that the turtle is already at the point
     * (currentX,currentY) and is facing at angle currentBearing. The angle must be expressed in
     * degrees, where 0 <= angle < 360.
     * <p>
     * HINT: look at http://en.wikipedia.org/wiki/Atan2 and Java's math libraries
     *
     * @param currentBearing current direction as clockwise from north
     * @param currentX       current location x-coordinate
     * @param currentY       current location y-coordinate
     * @param targetX        target point x-coordinate
     * @param targetY        target point y-coordinate
     * @return adjustment to Bearing (right turn amount) to get to target point,
     * must be 0 <= angle < 360
     */
    public static double calculateBearingToPoint(double currentBearing, int currentX, int currentY,
                                                 int targetX, int targetY) {
        double x = targetX - currentX;  //相对横坐标长度
        double y = targetY - currentY;  //相对纵坐标长度
        double len = Math.sqrt(x * x + y * y); //求出当前点和目标点之间的距离
        //相同点，则不用旋转角度
        if (len == 0) {
            return 0;
        }
        double bearingAngle = 180.0 * Math.atan2(y, x) / Math.PI; //求出两点形成的直线与水平正半轴之间的夹角
        if (bearingAngle >= -180.0 && bearingAngle <= 90.0) {
            bearingAngle = 90.0 - bearingAngle;
        } else {
            bearingAngle = 450.0 - bearingAngle;
        }
        bearingAngle = bearingAngle - currentBearing;
        if (bearingAngle < 0)//度数区间必须在0到360直接，因此小于0的度数要加360以此满足需求
        {
            bearingAngle = bearingAngle + 360;
        }
        return bearingAngle;
    }

    /**
     * Given a sequence of points, calculate the Bearing adjustments needed to get from each point
     * to the next.
     * <p>
     * Assumes that the turtle starts at the first point given, facing up (i.e. 0 degrees).
     * For each subsequent point, assumes that the turtle is still facing in the direction it was
     * facing when it moved to the previous point.
     * You should use calculateBearingToPoint() to implement this function.
     *
     * @param xCoords list of x-coordinates (must be same length as yCoords)
     * @param yCoords list of y-coordinates (must be same length as xCoords)
     * @return list of Bearing adjustments between points, of size 0 if (# of points) == 0,
     * otherwise of size (# of points) - 1
     */
    //根据得到的一系列x，y坐标，算出每次的偏转角并保存到集合中返回
    public static List<Double> calculateBearings(List<Integer> xCoords, List<Integer> yCoords) {
        List<Double> ang = new ArrayList<Double>();
        int currentX = xCoords.get(0);
        int currentY = yCoords.get(0);
        int targetX = 0;
        int targetY = 0;
        double bearingAngle = 0;
        for (int i = 1; i < xCoords.size(); i++) {
            targetX = xCoords.get(i);
            targetY = yCoords.get(i);
            bearingAngle = calculateBearingToPoint(bearingAngle, currentX, currentY, targetX, targetY);
            ang.add(bearingAngle);
            currentX = targetX;
            currentY = targetY;
        }
        return ang;
    }

    /**
     * Given a set of points, compute the convex hull, the smallest convex set that contains all the points
     * in a set of input points. The gift-wrapping algorithm is one simple approach to this problem, and
     * there are other algorithms too.
     *
     * @param points a set of points with xCoords and yCoords. It might be empty, contain only 1 point, two points or more.
     * @return minimal subset of the input points that form the vertices of the perimeter of the convex hull
     */
    //凸包问题
    public static Set<Point> convexHull(Set<Point> points) {
        //当点个数小于等于三个，大于等于0个的时候，该点集即凸包
        if (points.size() <= 3 && points.size() >= 0) {
            return points;
        }
        Set<Point> resultPoints = new HashSet<Point>();//保存凸包上的点集
        Point p = new Point(Double.MAX_VALUE, Double.MAX_VALUE); //用于保存左下角的点
        for (Point i : points) {
            if (i.x() < p.x() || (i.x() == p.x()) && i.y() < p.y()) {
                p = i;
            }
        }
        Point curPoint = p; //保存当前点
        Point minPoint = null;//保存右旋角最小的点
        double x1 = 0.0, y1 = 1.0; //y轴正方向向量
        do {
            resultPoints.add(curPoint);
            Double minAngle = Double.MAX_VALUE, x2 = 0.0, y2 = 0.0;
            for (Point i : points) {
                if ((!resultPoints.contains(i) || i == p) ) {
                    double x3 = i.x() - curPoint.x(), y3 = i.y() - curPoint.y(); //当前凸包点到点i的向量坐标
                    double angle = Math.acos((x1 * x3 + y1 * y3) / (Math.sqrt(x1 * x1 + y1 * y1) * Math.sqrt(x3 * x3 + y3 * y3)));//用向量积的方式求夹角
                    //当右旋角度相同时，选择距离较远的点
                    if (angle < minAngle || (angle == minAngle && x3 * x3 + y3 * y3 > Math.pow(i.x() - minPoint.x(), 2) + Math.pow(i.y() - minPoint.y(), 2))) {
                        minPoint = i;
                        minAngle = angle;
                        x2 = x3;
                        y2 = y3;
                    }
                }
            }
            x1 = x2;
            y1 = y2;
            curPoint = minPoint;
        } while (curPoint != p); //结束条件为当前的点和最初的点p共用同一个地址
        return resultPoints;
    }

    /**
     * Draw your personal, custom art.
     * <p>
     * Many interesting images can be drawn using the simple implementation of a turtle.  For this
     * function, draw something interesting; the complexity can be as little or as much as you want.
     *
     * @param turtle the turtle context
     */
    public static void drawPersonalArt(P2.turtle.Turtle turtle) {
        int len = 10;
        for (int i = 0; i < 50; i++) {
            if(i%2==0){
                turtle.color(PenColor.BLUE);
            }
            else {
                turtle.color(PenColor.RED);
            }
            for (int j = 0; j < 5; j++) {
                drawRegularPolygon(turtle,5,len);
            }
            len += 5;
            turtle.turn(360.0/50.0);
        }
    }

    /**
     * Main method.
     * <p>
     * This is the method that runs when you run "java TurtleSoup".
     *
     * @param args unused
     */
    public static void main(String args[]) {
        P2.turtle.DrawableTurtle turtle = new P2.turtle.DrawableTurtle();
        P2.turtle.DrawableTurtle turtle1 = new P2.turtle.DrawableTurtle();
        P2.turtle.DrawableTurtle turtle2 = new P2.turtle.DrawableTurtle();

        drawPersonalArt(turtle);
        // draw the window
        turtle.draw();
        drawSquare(turtle1,100);
        turtle1.draw();
        drawRegularPolygon(turtle2,6,100);
        turtle2.draw();

    }

}
