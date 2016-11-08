import java.util.*;
import java.io.*;

public class TravelAlgorithm {
  //diamond code the data in
  //assign each location to a unique number
  final private HashMap<String, Integer> DESTINATIONS = new HashMap<String, Integer>();
  static {
    DESTINATIONS.put("Marina Bay Sands", 0);
    DESTINATIONS.put("Singapore Flyer", 1);
    DESTINATIONS.put("Vivo City", 2);
    DESTINATIONS.put("Resorts World Sentosa", 3);
    DESTINATIONS.put("Buddha Tooth Relic Temple", 4);
    DESTINATIONS.put("Zoo", 5);
  }
  final private int mNoOfDestinations = DESTINATIONS.size();

  //arrays of cost and time taken
  //outer most array index: 1 is public transport, 2 is taxi, 3 is foot
  //second layer of array determines the starting point (from)
  //third layer of array determines the end point (to)
  final private double[][][] COST = new double[3][mNoOfDestinations][mNoOfDestinations];
  static {
    //public transport
    //from MBS
    COST[0][0][1] = 0.83;
    COST[0][0][2] = 1.18;
    COST[0][0][3] = 4.03;
    COST[0][0][4] = 0.88;
    COST[0][0][5] = 1.96;
    //from Spore Flyer
    COST[0][1][0] = 0.83;
    COST[0][1][2] = 1.26;
    COST[0][1][3] = 4.03;
    COST[0][1][4] = 0.98;
    COST[0][1][5] = 1.89;
    //from Vivo
    COST[0][2][0] = 1.18;
    COST[0][2][1] = 1.26;
    COST[0][2][3] = 2.00;
    COST[0][2][4] = 0.98;
    COST[0][2][5] = 1.99;
    //from Sentosa
    COST[0][3][0] = 1.18;
    COST[0][3][1] = 1.26;
    COST[0][3][2] = 0.00;
    COST[0][3][4] = 0.98;
    COST[0][3][5] = 1.99;
    //from Temple
    COST[0][4][0] = 0.88;
    COST[0][4][1] = 0.98;
    COST[0][4][2] = 0.98;
    COST[0][4][3] = 3.98;
    COST[0][4][5] = 1.91;
    //from Zoo
    COST[0][5][0] = 1.88;
    COST[0][5][1] = 1.96;
    COST[0][5][2] = 2.11;
    COST[0][5][3] = 4.99;
    COST[0][5][4] = 1.91;

    //taxi
    //from MBS
    COST[1][0][1] = 3.22;
    COST[1][0][2] = 6.96;
    COST[1][0][3] = 8.50;
    COST[1][0][4] = 4.98;
    COST[1][0][5] = 18.40;
    //from Spore Flyer
    COST[1][1][0] = 4.32;
    COST[1][1][2] = 7.84;
    COST[1][1][3] = 9.38;
    COST[1][1][4] = 4.76;
    COST[1][1][5] = 18.18;
    //from Vivo
    COST[1][2][0] = 8.30;
    COST[1][2][1] = 7.96;
    COST[1][2][3] = 4.54;
    COST[1][2][4] = 6.42;
    COST[1][2][5] = 22.58;
    //from Sentosa
    COST[1][3][0] = 8.74;
    COST[1][3][1] = 8.40;
    COST[1][3][2] = 3.22;
    COST[1][3][4] = 6.64;
    COST[1][3][5] = 22.80;
    //from Temple
    COST[1][4][0] = 5.32;
    COST[1][4][1] = 4.76;
    COST[1][4][2] = 4.98;
    COST[1][4][3] = 6.52;
    COST[1][4][5] = 18.40;
    //from Zoo
    COST[1][5][0] = 22.48;
    COST[1][5][1] = 19.40;
    COST[1][5][2] = 21.48;
    COST[1][5][3] = 23.68;
    COST[1][5][4] = 21.60;

    //on foot all free so leave to default 0
  }

  //same structure as cost
  final private int[][][] TIME = new int[3][mNoOfDestinations][mNoOfDestinations];
  static {
    //public
    TIME[0][0][1] = 17;
    TIME[0][0][2] = 26;
    TIME[0][0][3] = 35;
    TIME[0][0][4] = 19;
    TIME[0][0][5] = 84;

    TIME[0][1][0] = 17;
    TIME[0][1][2] = 31;
    TIME[0][1][3] = 38;
    TIME[0][1][4] = 24;
    TIME[0][1][5] = 85;

    TIME[0][2][0] = 24;
    TIME[0][2][1] = 29;
    TIME[0][2][3] = 10;
    TIME[0][2][4] = 18;
    TIME[0][2][5] = 85;

    TIME[0][3][0] = 33;
    TIME[0][3][1] = 38;
    TIME[0][3][2] = 10;
    TIME[0][3][4] = 27;
    TIME[0][3][5] = 92;

    TIME[0][4][0] = 18;
    TIME[0][4][1] = 23;
    TIME[0][4][2] = 19;
    TIME[0][4][3] = 28;
    TIME[0][4][5] = 83;

    TIME[0][5][0] = 86;
    TIME[0][5][1] = 87;
    TIME[0][5][2] = 86;
    TIME[0][5][3] = 96;
    TIME[0][5][4] = 84;

    //taxi
    TIME[1][0][1] = 3;
    TIME[1][0][2] = 14;
    TIME[1][0][3] = 19;
    TIME[1][0][4] = 8;
    TIME[1][0][5] = 30;

    TIME[1][1][0] = 6;
    TIME[1][1][2] = 13;
    TIME[1][1][3] = 18;
    TIME[1][1][4] = 8;
    TIME[1][1][5] = 29;

    TIME[1][2][0] = 12;
    TIME[1][2][1] = 14;
    TIME[1][2][3] = 9;
    TIME[1][2][4] = 11;
    TIME[1][2][5] = 31;

    TIME[1][3][0] = 13;
    TIME[1][3][1] = 14;
    TIME[1][3][2] = 4;
    TIME[1][3][4] = 12;
    TIME[1][3][5] = 32;

    TIME[1][4][0] = 7;
    TIME[1][4][1] = 8;
    TIME[1][4][2] = 9;
    TIME[1][4][3] = 14;
    TIME[1][4][5] = 30;

    TIME[1][5][0] = 32;
    TIME[1][5][1] = 29;
    TIME[1][5][2] = 32;
    TIME[1][5][3] = 36;
    TIME[1][5][4] = 30;

    //by foot
    TIME[2][0][1] = 14;
    TIME[2][0][2] = 69;
    TIME[2][0][3] = 76;
    TIME[2][0][4] = 28;
    TIME[2][0][5] = 269;

    TIME[2][1][0] = 14;
    TIME[2][1][2] = 81;
    TIME[2][1][3] = 88;
    TIME[2][1][4] = 39;
    TIME[2][1][5] = 264;

    TIME[2][2][0] = 69;
    TIME[2][2][1] = 81;
    TIME[2][2][3] = 12;
    TIME[2][2][4] = 47;
    TIME[2][2][5] = 270;

    TIME[2][3][0] = 76;
    TIME[2][3][1] = 88;
    TIME[2][3][2] = 12;
    TIME[2][3][4] = 55;
    TIME[2][3][5] = 285;

    TIME[2][4][0] = 28;
    TIME[2][4][1] = 39;
    TIME[2][4][2] = 47;
    TIME[2][4][3] = 55;
    TIME[2][4][5] = 264;

    TIME[2][5][0] = 269;
    TIME[2][5][1] = 264;
    TIME[2][5][2] = 270;
    TIME[2][5][3] = 285;
    TIME[2][5][4] = 264;
  }

  public static void main(String[] args) {

  }
}
