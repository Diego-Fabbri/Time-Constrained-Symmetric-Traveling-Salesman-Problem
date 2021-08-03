
package Utiity;

public class Data {

    public static double t0() {// value of t_o
        return 0;
    }
// V={0,1, 2, 3, 4,5,6,7}= ,

    public static int Problem_size() {

        return 8; // depot + nodes to visit =|V|=n
    }

    public static int speed() {
        // we assume that costs are distances, and time travels on arcs (i,j)in A are proportional due to a costant speed
// if costs are distances just set speed = 1

        return 1;
    }

    public static double[][] costs() {// matrix size 8x8
        // In this matrix triangle inequalities hold
        double[][] costs = {
            // 0   1     2      3     4     5     6     7     
            {0, 27.9, 54.6, 42.0, 56.5, 37.0, 30.9, 34.1,},// Node 0 depot
            {27.9, 0, 67.2, 25.6, 28.8, 48.4, 57.4, 21.6,},// Node 1
            {54.6, 67.2, 0, 60.5, 95.8, 18.8, 60.4, 52.1,},// Node 2
            {42.0, 25.6, 60.5, 0, 39.4, 43.1, 70.2, 12.2,},// Node 3
            {56.5, 28.8, 95.8, 39.4, 0, 77.2, 84.5, 44.4,},// Node 4
            {37.0, 48.4, 18.8, 43.1, 77.2, 0, 51.6, 34.0,},// Node 5
            {30.9, 57.4, 60.4, 70.2, 84.5, 51.6, 0, 59.3,},// Node 6
            {34.1, 21.6, 52.1, 12.2, 44.4, 34.0, 59.3, 0}, // Node 7
        };

        return costs;
    }

    public static double[][] travel_times(double[][] costs, double speed) {
        double[][] travel_times = new double[costs.length][costs[0].length];
        for (int i = 0; i < travel_times.length; i++) {
            for (int j = 0; j < travel_times[0].length; j++) {

                travel_times[i][j] = costs[i][j] / speed;
            }

        }

        return travel_times;
    }

    public static double[] Lower_Bounds() {
        double[] LB = {0, 0, 0, 0, 0, 0, 0, 0};
        return LB;
    }

    public static double[] Upper_Bounds() {
        double[] LB = {1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000};
        return LB;
    }

}
