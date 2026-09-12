package level3;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    static final int N = 150;
    static final int M = 150;
    static final int T = 800;
    static final int MAX_PLANTS_PER_TICK = 20;

    static final Map<Integer, String> SEASON_CHANGES = new TreeMap<>();

    static {
        SEASON_CHANGES.put(0, "Spring");
        SEASON_CHANGES.put(200, "Summer");
        SEASON_CHANGES.put(400, "Autumn");
        SEASON_CHANGES.put(600, "Winter");
    }

    static final int GRASS = 1;
    static final int ROSE_BUSH = 2;
    static final int BLUE_MOSS = 3;
    static final int CRIMSON_VINE = 4;
    static final int DWARF_SUNFLOWER = 5;
    static final int LAVENDER = 6;
    static final int ORANGE_BLOSSOM = 7;
    static final int SILVER_FERN = 8;
    static final int GLOWCAP_FUNGUS = 9;
    static final int PURPLE_CANOPY = 10;
    static final int STONE_REED = 11;
    static final int OAK_TREE = 12;

    public static void main(String[] args) {
        List<TickAction> actions = generateLevel3Strategy();
        writeJson("level3_solution.json", actions);

        int total = actions.stream().mapToInt(a -> a.plants.size()).sum();

        LOGGER.info("Level 3 solution written to level3_solution.json");
        LOGGER.info("Grid: " + N + " x " + M);
        LOGGER.info("Ticks: " + T);
        LOGGER.info("Total planting actions: " + total);
        LOGGER.info("Number of ticks with actions: " + actions.size());
    }

    static List<TickAction> generateLevel3Strategy() {
        List<TickAction> result = new ArrayList<>();

        for (int tick = 0; tick < T - 1; tick++) {
            List<PlantAction> plants = new ArrayList<>();
            Set<String> used = new HashSet<>();

            String season = getSeason(tick);
            boolean isWinter = "Winter".equals(season);

            if (tick < 150) {
                add(plants, used, GRASS, 6, tick);
                add(plants, used, ROSE_BUSH, 4, tick, isWinter);
                add(plants, used, LAVENDER, 4, tick, isWinter);
                add(plants, used, DWARF_SUNFLOWER, 3, tick);
                add(plants, used, OAK_TREE, 2, tick);
            } else if (tick < 300) {
                add(plants, used, GRASS, 4, tick);
                add(plants, used, ROSE_BUSH, 3, tick, isWinter);
                add(plants, used, LAVENDER, 3, tick, isWinter);
                add(plants, used, DWARF_SUNFLOWER, 2, tick);
                add(plants, used, OAK_TREE, 1, tick);
                add(plants, used, BLUE_MOSS, 4, tick);
                add(plants, used, ORANGE_BLOSSOM, 3, tick, isWinter);
            } else if (tick < 500) {
                add(plants, used, GRASS, 3, tick);
                add(plants, used, ROSE_BUSH, 2, tick, isWinter);
                add(plants, used, LAVENDER, 2, tick, isWinter);
                add(plants, used, DWARF_SUNFLOWER, 2, tick);
                add(plants, used, BLUE_MOSS, 2, tick);
                add(plants, used, ORANGE_BLOSSOM, 2, tick, isWinter);
                add(plants, used, OAK_TREE, 1, tick);
                add(plants, used, CRIMSON_VINE, 3, tick);
                add(plants, used, GLOWCAP_FUNGUS, 2, tick);
                add(plants, used, SILVER_FERN, 2, tick);
            } else {
                add(plants, used, GRASS, 2, tick);
                add(plants, used, ROSE_BUSH, 2, tick, isWinter);
                add(plants, used, LAVENDER, 2, tick, isWinter);
                add(plants, used, DWARF_SUNFLOWER, 1, tick);
                add(plants, used, BLUE_MOSS, 2, tick);
                add(plants, used, ORANGE_BLOSSOM, 1, tick, isWinter);
                add(plants, used, CRIMSON_VINE, 2, tick);
                add(plants, used, GLOWCAP_FUNGUS, 1, tick);
                add(plants, used, SILVER_FERN, 2, tick);
                add(plants, used, OAK_TREE, 1, tick);
                add(plants, used, PURPLE_CANOPY, 2, tick);
                add(plants, used, STONE_REED, 2, tick);
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

    static void add(
            List<PlantAction> plants,
            Set<String> used,
            int plantIndex,
            int count,
            int tick
    ) {
        add(plants, used, plantIndex, count, tick, false);
    }

    static void add(
            List<PlantAction> plants,
            Set<String> used,
            int plantIndex,
            int count,
            int tick,
            boolean isWinter
    ) {
        if (isWinter && (plantIndex == ROSE_BUSH
                || plantIndex == ORANGE_BLOSSOM
                || plantIndex == LAVENDER)) {
            return;
        }

        Random rnd = new Random(tick * 37L + plantIndex * 17L + 12345L);
        int attempts = 0;
        int maxAttempts = Math.max(count * 100, 300);

        while (count > 0 && attempts < maxAttempts) {
            attempts++;

            int row = rnd.nextInt(N);
            int col = rnd.nextInt(M);

            if (plantIndex == OAK_TREE ||
                plantIndex == DWARF_SUNFLOWER ||
                plantIndex == SILVER_FERN ||
                plantIndex == PURPLE_CANOPY) {

                row = (row / 6) * 6 + rnd.nextInt(5);
                col = (col / 6) * 6 + rnd.nextInt(5);

                row = Math.min(Math.max(row, 0), N - 1);
                col = Math.min(Math.max(col, 0), M - 1);
            }

            String key = row + "," + col;

            if (used.contains(key)) {
                continue;
            }

            used.add(key);
            plants.add(new PlantAction(plantIndex, row, col));
            count--;
        }
    }

    static String getSeason(int tick) {
        String season = "Spring";

        for (Map.Entry<Integer, String> e : SEASON_CHANGES.entrySet()) {
            if (tick >= e.getKey()) {
                season = e.getValue();
            } else {
                break;
            }
        }

        return season;
    }

    static class PlantAction {
        final int plantIndex;
        final int row;
        final int col;

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

    static void writeJson(String filename, List<TickAction> actions) {
        try (FileWriter w = new FileWriter(filename)) {
            w.write("{\n  \"actions\": [\n");

            for (int i = 0; i < actions.size(); i++) {
                TickAction ta = actions.get(i);

                w.write("    {\n");
                w.write("      \"tick\": " + ta.tick + ",\n");
                w.write("      \"plants\": [\n");

                for (int j = 0; j < ta.plants.size(); j++) {
                    PlantAction p = ta.plants.get(j);

                    w.write("        {\n");
                    w.write("          \"plant_index\": " + p.plantIndex + ",\n");
                    w.write("          \"row\": " + p.row + ",\n");
                    w.write("          \"col\": " + p.col + "\n");
                    w.write("        }");

                    if (j < ta.plants.size() - 1) {
                        w.write(",");
                    }

                    w.write("\n");
                }

                w.write("      ]\n    }");

                if (i < actions.size() - 1) {
                    w.write(",");
                }

                w.write("\n");
            }

            w.write("  ]\n}\n");

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to write file", e);
        }
    }
}