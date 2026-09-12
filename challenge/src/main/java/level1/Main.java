package level1;

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

    static final int N = 20;
    static final int M = 20;
    static final int T = 80;
    static final int MAX_PLANTS_PER_TICK = 20;

    static final Map<Integer, String> SEASON_CHANGES = new TreeMap<>();

    static {
        SEASON_CHANGES.put(0, "Spring");
        SEASON_CHANGES.put(20, "Summer");
        SEASON_CHANGES.put(40, "Autumn");
        SEASON_CHANGES.put(60, "Winter");
    }

    static final int GRASS = 1;
    static final int ROSE_BUSH = 2;
    static final int DWARF_SUNFLOWER = 5;
    static final int LAVENDER = 6;
    static final int OAK_TREE = 12;

    public static void main(String[] args) {
        List<TickAction> actions = generateLevel1Strategy();

        writeJson("level1_solution.json", actions);

        int totalPlants = actions.stream()
                .mapToInt(action -> action.plants.size())
                .sum();

        System.out.println("Level 1 solution written to level1_solution.json");
        System.out.println("Total planting actions scheduled: " + totalPlants);
    }

    static List<TickAction> generateLevel1Strategy() {
        List<TickAction> result = new ArrayList<>();

        for (int tick = 0; tick < T - 1; tick++) {
            List<PlantAction> plantsThisTick = new ArrayList<>();
            Set<String> usedPositions = new HashSet<>();

            String season = getSeason(tick);
            boolean isWinter = "Winter".equals(season);

            if (tick < 25) {
                addPlants(
                        plantsThisTick,
                        usedPositions,
                        GRASS,
                        6,
                        tick
                );

                addPlants(
                        plantsThisTick,
                        usedPositions,
                        LAVENDER,
                        4,
                        tick
                );

                addPlants(
                        plantsThisTick,
                        usedPositions,
                        DWARF_SUNFLOWER,
                        3,
                        tick
                );

                if (!isWinter) {
                    addPlants(
                            plantsThisTick,
                            usedPositions,
                            ROSE_BUSH,
                            3,
                            tick
                    );
                }

            } else if (tick < 50) {
                addPlants(
                        plantsThisTick,
                        usedPositions,
                        GRASS,
                        3,
                        tick
                );

                addPlants(
                        plantsThisTick,
                        usedPositions,
                        LAVENDER,
                        3,
                        tick
                );

                addPlants(
                        plantsThisTick,
                        usedPositions,
                        DWARF_SUNFLOWER,
                        3,
                        tick
                );

                if (!isWinter) {
                    addPlants(
                            plantsThisTick,
                            usedPositions,
                            ROSE_BUSH,
                            4,
                            tick
                    );
                }

                addPlants(
                        plantsThisTick,
                        usedPositions,
                        OAK_TREE,
                        2,
                        tick
                );

            } else {
                addPlants(
                        plantsThisTick,
                        usedPositions,
                        GRASS,
                        3,
                        tick
                );

                addPlants(
                        plantsThisTick,
                        usedPositions,
                        LAVENDER,
                        3,
                        tick
                );

                addPlants(
                        plantsThisTick,
                        usedPositions,
                        DWARF_SUNFLOWER,
                        3,
                        tick
                );

                if (!isWinter) {
                    addPlants(
                            plantsThisTick,
                            usedPositions,
                            ROSE_BUSH,
                            3,
                            tick
                    );
                }

                addPlants(
                        plantsThisTick,
                        usedPositions,
                        OAK_TREE,
                        2,
                        tick
                );
            }

            if (plantsThisTick.size() > MAX_PLANTS_PER_TICK) {
                plantsThisTick = new ArrayList<>(
                        plantsThisTick.subList(
                                0,
                                MAX_PLANTS_PER_TICK
                        )
                );
            }

            if (!plantsThisTick.isEmpty()) {
                result.add(
                        new TickAction(
                                tick,
                                plantsThisTick
                        )
                );
            }
        }

        return result;
    }

    static void addPlants(
            List<PlantAction> plants,
            Set<String> usedPositions,
            int plantIndex,
            int count,
            int tick
    ) {
        Random random = new Random(
                tick * 31L + plantIndex
        );

        int attempts = 0;
        int maxAttempts = count * 100;

        while (count > 0 && attempts < maxAttempts) {
            attempts++;

            int row = random.nextInt(N);
            int col = random.nextInt(M);

            if (plantIndex == DWARF_SUNFLOWER
                    || plantIndex == OAK_TREE) {

                row = (row / 3) * 3 + random.nextInt(3);
                col = (col / 3) * 3 + random.nextInt(3);

                row = Math.min(row, N - 1);
                col = Math.min(col, M - 1);
            }

            String position = row + "," + col;

            if (!usedPositions.contains(position)) {
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
                    "Failed to create solution file: "
                            + e.getMessage()
            );
        }
    }
}
