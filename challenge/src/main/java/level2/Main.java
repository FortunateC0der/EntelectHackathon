package level2;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {
    // Constants
    static final int N = 30;
    static final int M = 30;
    static final int T = 120;
    static final int MAX_PLANTS_PER_TICK = 20;

    // Season changes
    static final Map<Integer, String> SEASON_CHANGES = new TreeMap<>();
    static {
        SEASON_CHANGES.put(0,  "Spring");
        SEASON_CHANGES.put(30, "Summer");
        SEASON_CHANGES.put(60, "Autumn");
        SEASON_CHANGES.put(90, "Winter");
    }

    // Plant indices
    static final int GRASS            = 1;
    static final int ROSE_BUSH        = 2;
    static final int BLUE_MOSS        = 3;
    static final int CRIMSON_VINE     = 4;
    static final int DWARF_SUNFLOWER  = 5;
    static final int LAVENDER         = 6;
    static final int ORANGE_BLOSSOM   = 7;
    static final int SILVER_FERN      = 8;
    static final int GLOWCAP_FUNGUS   = 9;
    static final int OAK_TREE         = 12;

    // Main method 
    public static void main(String[] args) {
        List<TickAction> actions = generateLevel2Strategy();
        writeJson("level2_solution.json", actions);

        int total = actions.stream().mapToInt(a -> a.plants.size()).sum();
        System.out.println("Level 2 solution written to level2_solution.json");
        System.out.println("Total planting actions: " + total);
        System.out.println("Number of ticks with actions: " + actions.size());
    }

    // Generate a planting strategy for Level 2
    static List<TickAction> generateLevel2Strategy() {
        List<TickAction> result = new ArrayList<>();

        for (int tick = 0; tick < T - 1; tick++) {
            List<PlantAction> plants = new ArrayList<>();
            Set<String> used = new HashSet<>(); 
            String season = getSeason(tick);
            boolean isWinter = "Winter".equals(season);

            if (tick < 30) {
                add(plants, used, GRASS,           7, tick);
                add(plants, used, ROSE_BUSH,       5, tick, isWinter);
                add(plants, used, LAVENDER,        5, tick, isWinter);
                add(plants, used, DWARF_SUNFLOWER, 3, tick);
                add(plants, used, OAK_TREE,        1, tick);
            }

            else if (tick < 55) {
                add(plants, used, GRASS,           3, tick);
                add(plants, used, ROSE_BUSH,       3, tick, isWinter);
                add(plants, used, LAVENDER,        3, tick, isWinter);
                add(plants, used, DWARF_SUNFLOWER, 2, tick);
                add(plants, used, OAK_TREE,        1, tick);

                add(plants, used, BLUE_MOSS,       4, tick);
                add(plants, used, ORANGE_BLOSSOM,  3, tick, isWinter);
            }

            else if (tick < 85) {
                add(plants, used, GRASS,           2, tick);
                add(plants, used, ROSE_BUSH,       2, tick, isWinter);
                add(plants, used, LAVENDER,        2, tick, isWinter);
                add(plants, used, DWARF_SUNFLOWER, 2, tick);
                add(plants, used, BLUE_MOSS,       2, tick);
                add(plants, used, ORANGE_BLOSSOM,  2, tick, isWinter);
                add(plants, used, OAK_TREE,        1, tick);

                add(plants, used, CRIMSON_VINE,    3, tick);
                add(plants, used, GLOWCAP_FUNGUS,  2, tick);
                add(plants, used, SILVER_FERN,     2, tick);
            }

            else {
                add(plants, used, GRASS,           2, tick);
                add(plants, used, ROSE_BUSH,       2, tick, isWinter);
                add(plants, used, LAVENDER,        2, tick, isWinter);
                add(plants, used, DWARF_SUNFLOWER, 1, tick);
                add(plants, used, BLUE_MOSS,       2, tick);
                add(plants, used, ORANGE_BLOSSOM,  2, tick, isWinter);
                add(plants, used, CRIMSON_VINE,    2, tick);
                add(plants, used, GLOWCAP_FUNGUS,  1, tick);
                add(plants, used, SILVER_FERN,     2, tick);
                add(plants, used, OAK_TREE,        2, tick);
            }

            if (plants.size() > MAX_PLANTS_PER_TICK) {
                plants = new ArrayList<>(plants.subList(0, MAX_PLANTS_PER_TICK));
            }

            if (!plants.isEmpty()) {
                result.add(new TickAction(tick, plants));
            }
        }
        return result;
    }

    // Add planting actions to the list, ensuring no duplicates and respecting season constraints
    static void add(List<PlantAction> plants, Set<String> used,
                    int plantIndex, int count, int tick) {
        add(plants, used, plantIndex, count, tick, false);
    }

    static void add(List<PlantAction> plants, Set<String> used,
                    int plantIndex, int count, int tick, boolean isWinter) {
        if (isWinter) {
            if (plantIndex == ROSE_BUSH || plantIndex == ORANGE_BLOSSOM || plantIndex == LAVENDER) {
                return;
            }
        }

        Random rnd = new Random(tick * 37L + plantIndex * 17L + 12345L);
        int attempts = 0;
        int maxAttempts = Math.max(count * 80, 150);

        while (count > 0 && attempts < maxAttempts) {
            attempts++;

            int row = rnd.nextInt(N);
            int col = rnd.nextInt(M);

            if (plantIndex == OAK_TREE || plantIndex == DWARF_SUNFLOWER || plantIndex == SILVER_FERN) {
                row = (row / 4) * 4 + rnd.nextInt(3);
                col = (col / 4) * 4 + rnd.nextInt(3);
                row = Math.min(Math.max(row, 0), N - 1);
                col = Math.min(Math.max(col, 0), M - 1);
            }

            String key = row + "," + col;
            if (used.contains(key)) continue;

            used.add(key);
            plants.add(new PlantAction(plantIndex, row, col));
            count--;
        }
    }

    // Determine the season based on the current tick
    static String getSeason(int tick) {
        String season = "Spring";
        for (Map.Entry<Integer, String> e : SEASON_CHANGES.entrySet()) {
            if (tick >= e.getKey()) season = e.getValue();
            else break;
        }
        return season;
    }

    static class PlantAction {
        final int plantIndex, row, col;
        PlantAction(int plantIndex, int row, int col) {
            this.plantIndex = plantIndex;
            this.row = row;
            this.col = col;
        }
    }

    static class TickAction {
        final int tick;
        final List<PlantAction> plants;
        TickAction(int tick, List<PlantAction> plants) {
            this.tick = tick;
            this.plants = plants;
        }
    }

    // Write the planting actions to a JSON file
    static void writeJson(String filename, List<TickAction> actions) {
        try (FileWriter w = new FileWriter(filename)) {
            w.write("{\n  \"actions\": [\n");
            for (int i = 0; i < actions.size(); i++) {
                TickAction ta = actions.get(i);
                w.write("    {\n      \"tick\": " + ta.tick + ",\n      \"plants\": [\n");
                for (int j = 0; j < ta.plants.size(); j++) {
                    PlantAction p = ta.plants.get(j);
                    w.write("        {\n");
                    w.write("          \"plant_index\": " + p.plantIndex + ",\n");
                    w.write("          \"row\": " + p.row + ",\n");
                    w.write("          \"col\": " + p.col + "\n");
                    w.write("        }");
                    if (j < ta.plants.size() - 1) w.write(",");
                    w.write("\n");
                }
                w.write("      ]\n    }");
                if (i < actions.size() - 1) w.write(",");
                w.write("\n");
            }
            w.write("  ]\n}\n");
        } catch (IOException e) {
            System.err.println("Failed to write file: " + e.getMessage());
        }
    }
}