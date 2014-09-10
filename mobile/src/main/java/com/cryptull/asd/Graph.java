package com.cryptull.asd;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Paco on 10/09/2014.
 */
public class Graph {

    /*public static void setG (){
        Utilities.dim = 5;
        Utilities.SolG = new ArrayList<Integer>();
        Utilities.SolG.add(1);
        Utilities.SolG.add(3);
        Utilities.SolG.add(4);
        Utilities.SolG.add(2);
        Utilities.SolG.add(5);
        Utilities.G = new boolean[][]{
                { false, true, true, true, true },
                { true, false, false, true, true },
                { true, false, false, true, false },
                { true, true, true, false, true },
                { true, true, false, true, false }
        };

    }*/

    public static boolean getRandomBoolean(Random random) {
        return random.nextBoolean();
    }

    public static void generateGraph (int nodes){
        Utilities.dim = nodes;
        Utilities.G = new boolean[nodes][nodes];
        Utilities.SolG = new ArrayList<Integer>();
        for (int i=0; i<nodes; i++){
            Utilities.SolG.add(i+1);
            Utilities.G[i][(i + 1)%nodes] = true;
            Utilities.G[(i + 1)%nodes][i] = true;
        }

        Random random = new Random();
        for(int i=0; i<nodes; i++){
            for (int j=0; j<nodes; j++){
                if (i != j){
                    if (Graph.getRandomBoolean(random)){
                        Utilities.G[i][j] = true;
                        Utilities.G[j][i] = true;
                    }
                }
            }
        }

        //Utilities.printGraph(Utilities.G);
    }
}
