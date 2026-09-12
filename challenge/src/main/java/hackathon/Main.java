package hackathon;

import java.util.*;
import java.io.FileWriter;
import java.io.IOException;

public class Main {

    static final int N = 20;
    static final int M = 20;
    static final int T = 80;
    static final int MAX_PLANTS_PER_TICK = 20;

    static final int GRASS = 1;
    static final int ROSE_RUSH = 2;
    static final int DWARF_SUNFLOWER = 3;
    static final int LAVENDER = 6;
    static final int OAK_TREE = 12;

public static void main(String[] args) {
        List<TickAction> actions = generateLevel1Strategy();

        // Write the JSON submission file
        writeJson("level1_solution.json", actions);

        System.out.println("Level 1 solution written to level1_solution.json");
        System.out.println("Total planting actions scheduled: " + 
            actions.stream().mapToInt(a -> a.plants.size()).sum());
    }
}