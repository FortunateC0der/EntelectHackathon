package level4;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    static final int N = 200;
    static final int M = 300;
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
    static final int WHITEVEIL = 13;
    static final int EMBERROOT = 14;
    static final int MOONPETAL_LILY = 15;
    static final int WORLDTREE = 31;

    public static void main(String[] args) {
        List<TickAction> actions = generateLevel4Strategy();

        writeJson("level4_solution.json", actions);

        int total = actions.stream()
                .mapToInt(action -> action.plants.size())
                .sum();

        LOGGER.info("Level 4 solution written to level4_solution.json");
        LOGGER.info("Grid: " + N + " x " + M);
        LOGGER.info("Ticks: " + T);
        LOGGER.info("Total planting actions: " + total);
        LOGGER.info("Ticks with actions: " + actions.size());
    }

    static List<TickAction> generateLevel4Strategy() {
        List<TickAction> result = new ArrayList<>();

        for (int tick = 0; tick < T - 1; tick++) {
            List<PlantAction> plants = new ArrayList<>();
            Set<String> used = new HashSet<>();

            boolean isWinter = "Winter".equals(getSeason(tick));

            if (tick < 120) {
                add(plants, used, GRASS, 6, tick);
                add(plants, used, ROSE_BUSH, 4, tick, isWinter);
                add(plants, used, LAVENDER, 4, tick, isWinter);
                add(plants, used, DWARF_SUNFLOWER, 3, tick);
                add(plants, used, OAK_TREE, 2, tick);

            } else if (tick < 250) {
                add(plants, used, GRASS, 4, tick);
                add(plants, used, ROSE_BUSH, 3, tick, isWinter);
                add(plants, used, LAVENDER, 3, tick, isWinter);
                add(plants, used, DWARF_SUNFLOWER, 2, tick);
                add(plants, used, OAK_TREE, 1, tick);
                add(plants, used, BLUE_MOSS, 4, tick);
                add(plants, used, ORANGE_BLOSSOM, 3, tick, isWinter);

            } else if (tick < 420) {
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

            } else if (tick < 600) {
                add(plants, used, GRASS, 2, tick);
                add(plants, used, ROSE_BUSH, 2, tick, isWinter);
                add(plants, used, LAVENDER, 2, tick, isWinter);
                add(plants, used, BLUE_MOSS, 2, tick);
                add(plants, used, ORANGE_BLOSSOM, 1, tick, isWinter);
                add(plants, used, CRIMSON_VINE, 2, tick);
                add(plants, used, GLOWCAP_FUNGUS, 1, tick);
                add(plants, used, SILVER_FERN, 2, tick);
                add(plants, used, OAK_TREE, 1, tick);
                add(plants, used, PURPLE_CANOPY, 2, tick);
                add(plants, used, STONE_REED, 2, tick);
                add(plants, used, WHITEVEIL, 1, tick);

            } else {
                add(plants, used, GRASS, 2, tick);
                add(plants, used, ROSE_BUSH, 1, tick, isWinter);
                add(plants, used, LAVENDER, 1, tick, isWinter);
                add(plants, used, BLUE_MOSS, 1, tick);
                add(plants, used, CRIMSON_VINE, 1, tick);
                add(plants, used, SILVER_FERN, 1, tick);
                add(plants, used, OAK_TREE, 1, tick);
                add(plants, used, PURPLE_CANOPY, 1, tick);
                add(plants, used, STONE_REED, 1, tick);
                add(plants, used, WHITEVEIL, 1, tick);
                add(plants, used, EMBERROOT, 1, tick);
                add(plants, used, MOONPETAL_LILY, 1, tick);

                if (tick % 20 == 0) {
                    add(plants, used, WORLDTREE, 1, tick);
                }
            }

            if (plants.size() > MAX_PLANTS_PER_TICK) {
                plants = new ArrayList<>(
                        plants.subList(0, MAX_PLANTS_PER_TICK)
                );
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
        if (isWinter &&
                (plantIndex == ROSE_BUSH ||
                 plantIndex == ORANGE_BLOSSOM ||
                 plantIndex == LAVENDER)) {
            return;
        }

        Random random = new Random(
                tick * 37L + plantIndex * 17L + 98765L
        );

        int attempts = 0;
        int maxAttempts = Math.max(count * 150, 500);

        while (count > 0 && attempts < maxAttempts) {
            attempts++;

            int row = random.nextInt(N);
            int col = random.nextInt(M);

            if (plantIndex == OAK_TREE ||
                    plantIndex == PURPLE_CANOPY ||
                    plantIndex == WORLDTREE ||
                    plantIndex == SILVER_FERN) {

                row = (row / 8) * 8 + random.nextInt(6);
                col = (col / 8) * 8 + random.nextInt(6);

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

        for (Map.Entry<Integer, String> entry : SEASON_CHANGES.entrySet()) {
            if (tick >= entry.getKey()) {
                season = entry.getValue();
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

    static void writeJson(
            String filename,
            List<TickAction> actions
    ) {
        try (FileWriter writer = new FileWriter(filename)) {

            writer.write("{\n");
            writer.write("  \"actions\": [\n");

            for (int i = 0; i < actions.size(); i++) {
                TickAction tickAction = actions.get(i);

                writer.write("    {\n");
                writer.write("      \"tick\": " + tickAction.tick + ",\n");
                writer.write("      \"plants\": [\n");

                for (int j = 0; j < tickAction.plants.size(); j++) {
                    PlantAction plant = tickAction.plants.get(j);

                    writer.write("        {\n");
                    writer.write(
                            "          \"plant_index\": "
                                    + plant.plantIndex
                                    + ",\n"
                    );
                    writer.write(
                            "          \"row\": "
                                    + plant.row
                                    + ",\n"
                    );
                    writer.write(
                            "          \"col\": "
                                    + plant.col
                                    + "\n"
                    );
                    writer.write("        }");

                    if (j < tickAction.plants.size() - 1) {
                        writer.write(",");
                    }

                    writer.write("\n");
                }

                writer.write("      ]\n");
                writer.write("    }");

                if (i < actions.size() - 1) {
                    writer.write(",");
                }

                writer.write("\n");
            }

            writer.write("  ]\n");
            writer.write("}\n");

        } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Failed to write file", e);
        }
    }
}