package level2;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

public class Main {

    static final int N = 30;
    static final int M = 30;
    static final int T = 120;
    static final int MAX_PLANTS_PER_TICK = 20;

    static final Map<Integer, String> SEASON_CHANGES = new TreeMap<>();

    static {
        SEASON_CHANGES.put(0, "Spring");
        SEASON_CHANGES.put(30, "Summer");
        SEASON_CHANGES.put(60, "Autumn");
        SEASON_CHANGES.put(90, "Winter");
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
    static final int OAK_TREE = 12;

    public static void main(String[] args) {
        List<TickAction> actions = generateLevel2Strategy();

        writeJson("level2_solution.json", actions);

        int totalPlants = actions.stream()
                .mapToInt(action -> action.plants.size())
                .sum();

        System.out.println("Level 2 solution written to level2_solution.json");
        System.out.println("Total planting actions: " + totalPlants);
    }

    static List<TickAction> generateLevel2Strategy() {
        List<TickAction> result = new ArrayList<>();

        for (int tick = 0; tick < T - 1; tick++) {
            List<PlantAction> plants = new ArrayList<>();
            Set<String> usedPositions = new HashSet<>();

            String season = getSeason(tick);
            boolean isWinter = "Winter".equals(season);

            if (tick < 30) {
                add(plants, usedPositions, GRASS, 7, tick);
                add(plants, usedPositions, ROSE_BUSH, 5, tick, isWinter);
                add(plants, usedPositions, LAVENDER, 4, tick);
                add(plants, usedPositions, DWARF_SUNFLOWER, 3, tick);
                add(plants, usedPositions, OAK_TREE, 1, tick);

            } else if (tick < 55) {
                add(plants, usedPositions, GRASS, 3, tick);
                add(plants, usedPositions, ROSE_BUSH, 3, tick, isWinter);
                add(plants, usedPositions, LAVENDER, 3, tick);
                add(plants, usedPositions, DWARF_SUNFLOWER, 2, tick);
                add(plants, usedPositions, OAK_TREE, 1, tick);
                add(plants, usedPositions, BLUE_MOSS, 4, tick);
                add(plants, usedPositions, ORANGE_BLOSSOM, 3, tick, isWinter);

            } else if (tick < 85) {
                add(plants, usedPositions, GRASS, 2, tick);
                add(plants, usedPositions, ROSE_BUSH, 2, tick, isWinter);
                add(plants, usedPositions, LAVENDER, 2, tick);
                add(plants, usedPositions, DWARF_SUNFLOWER, 2, tick);
                add(plants, usedPositions, BLUE_MOSS, 2, tick);
                add(plants, usedPositions, ORANGE_BLOSSOM, 2, tick, isWinter);
                add(plants, usedPositions, OAK_TREE, 1, tick);
                add(plants, usedPositions, CRIMSON_VINE, 3, tick);
                add(plants, usedPositions, GLOWCAP_FUNGUS, 2, tick);
                add(plants, usedPositions, SILVER_FERN, 2, tick);

            } else {
                add(plants, usedPositions, GRASS, 2, tick);
                add(plants, usedPositions, ROSE_BUSH, 2, tick, isWinter);
                add(plants, usedPositions, LAVENDER, 2, tick);
                add(plants, usedPositions, DWARF_SUNFLOWER, 1, tick);
                add(plants, usedPositions, BLUE_MOSS, 2, tick);
                add(plants, usedPositions, ORANGE_BLOSSOM, 2, tick, isWinter);
                add(plants, usedPositions, CRIMSON_VINE, 2, tick);
                add(plants, usedPositions, GLOWCAP_FUNGUS, 1, tick);
                add(plants, usedPositions, SILVER_FERN, 2, tick);
                add(plants, usedPositions, OAK_TREE, 2, tick);
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
            Set<String> usedPositions,
            int plantIndex,
            int count,
            int tick
    ) {
        add(
                plants,
                usedPositions,
                plantIndex,
                count,
                tick,
                false
        );
    }

    static void add(
            List<PlantAction> plants,
            Set<String> usedPositions,
            int plantIndex,
            int count,
            int tick,
            boolean isWinter
    ) {
        if (isWinter && plantIndex == ROSE_BUSH) {
            return;
        }

        if (isWinter && plantIndex == ORANGE_BLOSSOM) {
            return;
        }

        Random random = new Random(
                tick * 37L + plantIndex * 17L
        );

        int attempts = 0;
        int maxAttempts = Math.max(count * 100, 100);

        while (count > 0 && attempts < maxAttempts) {
            attempts++;

            int row = random.nextInt(N);
            int col = random.nextInt(M);

            if (plantIndex == OAK_TREE
                    || plantIndex == DWARF_SUNFLOWER
                    || plantIndex == SILVER_FERN) {

                row = (row / 4) * 4 + random.nextInt(3);
                col = (col / 4) * 4 + random.nextInt(3);

                row = Math.min(row, N - 1);
                col = Math.min(col, M - 1);
            }

            if (row < 0 || row >= N || col < 0 || col >= M) {
                continue;
            }

            String position = row + "," + col;

            if (usedPositions.contains(position)) {
                continue;
            }

            usedPositions.add(position);

            plants.add(
                    new PlantAction(
                            plantIndex,
                            row,
                            col
                    )
            );

            count--;
        }
    }

    static String getSeason(int tick) {
        String season = "Spring";

        for (Map.Entry<Integer, String> entry
                : SEASON_CHANGES.entrySet()) {

            if (tick >= entry.getKey()) {
                season = entry.getValue();
            } else {
                break;
            }
        }

        return season;
    }

    static class PlantAction {

        int plantIndex;
        int row;
        int col;

        PlantAction(
                int plantIndex,
                int row,
                int col
        ) {
            this.plantIndex = plantIndex;
            this.row = row;
            this.col = col;
        }
    }

    static class TickAction {

        int tick;
        List<PlantAction> plants;

        TickAction(
                int tick,
                List<PlantAction> plants
        ) {
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
                writer.write(
                        "      \"tick\": "
                                + tickAction.tick
                                + ",\n"
                );

                writer.write("      \"plants\": [\n");

                for (int j = 0;
                     j < tickAction.plants.size();
                     j++) {

                    PlantAction plant =
                            tickAction.plants.get(j);

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
            System.err.println(
                    "Failed to write level2_solution.json: "
                            + e.getMessage()
            );
        }
    }
}