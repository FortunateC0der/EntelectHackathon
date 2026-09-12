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
import java.util.logging.Level;
import java.util.logging.Logger;
public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
    static final int N = 50;
    static final int M = 50;
    static final int T = 500;
    static final int MAX_PLANTS_PER_TICK = 20;
    static final Map<Integer, String> SEASON_CHANGES = new TreeMap<>();
    static {
        SEASON_CHANGES.put(0, "Spring");
        SEASON_CHANGES.put(100, "Summer");
        SEASON_CHANGES.put(200, "Autumn");
        SEASON_CHANGES.put(300, "Winter");
        SEASON_CHANGES.put(400, "Spring");
    }
    static final int GRASS = 1;
    static final int ROSE_BUSH = 2;
    static final int DWARF_SUNFLOWER = 5;
    static final int LAVENDER = 6;
    static final int OAK_TREE = 12;
    public static void main(String[] args) {
        List<TickAction> actions = generateLevel1Strategy();
        writeJson("level1_solution.json", actions);
        int totalPlants = actions.stream().mapToInt(a -> a.plants.size()).sum();
        LOGGER.info("Level 1 solution written to level1_solution.json");
        LOGGER.info("Total planting actions scheduled: " + totalPlants);
    }
    static List<TickAction> generateLevel1Strategy() {
        List<TickAction> result = new ArrayList<>();
        Set<String> globalOccupied = new HashSet<>();
        for (int tick = 0; tick < T - 1; tick++) {
            List<PlantAction> plantsThisTick = new ArrayList<>();
            Set<String> usedThisTick = new HashSet<>();
            String season = getSeason(tick);
            boolean isWinter = "Winter".equals(season);
            int phase = tick / 100;
            if (phase == 0) {
                addPlants(plantsThisTick, usedThisTick, globalOccupied, OAK_TREE, 4, tick, 6);
                addPlants(plantsThisTick, usedThisTick, globalOccupied, GRASS, 3, tick, 1);
                addPlants(plantsThisTick, usedThisTick, globalOccupied, LAVENDER, 2, tick, 2);
                addPlants(plantsThisTick, usedThisTick, globalOccupied, DWARF_SUNFLOWER, 2, tick, 3);
                if (!isWinter) {
                    addPlants(plantsThisTick, usedThisTick, globalOccupied, ROSE_BUSH, 2, tick, 2);
                }
            } else if (phase == 1) {
                addPlants(plantsThisTick, usedThisTick, globalOccupied, OAK_TREE, 5, tick, 6);
                addPlants(plantsThisTick, usedThisTick, globalOccupied, GRASS, 2, tick, 1);
                addPlants(plantsThisTick, usedThisTick, globalOccupied, LAVENDER, 2, tick, 2);
                addPlants(plantsThisTick, usedThisTick, globalOccupied, DWARF_SUNFLOWER, 2, tick, 3);
                if (!isWinter) {
                    addPlants(plantsThisTick, usedThisTick, globalOccupied, ROSE_BUSH, 2, tick, 2);
                }
            } else if (phase == 2) {
                addPlants(plantsThisTick, usedThisTick, globalOccupied, OAK_TREE, 6, tick, 5);
                addPlants(plantsThisTick, usedThisTick, globalOccupied, GRASS, 2, tick, 1);
                addPlants(plantsThisTick, usedThisTick, globalOccupied, LAVENDER, 1, tick, 2);
                addPlants(plantsThisTick, usedThisTick, globalOccupied, DWARF_SUNFLOWER, 1, tick, 3);
                if (!isWinter) {
                    addPlants(plantsThisTick, usedThisTick, globalOccupied, ROSE_BUSH, 1, tick, 2);
                }
            } else {
                addPlants(plantsThisTick, usedThisTick, globalOccupied, OAK_TREE, 7, tick, 5);
                addPlants(plantsThisTick, usedThisTick, globalOccupied, GRASS, 1, tick, 1);
                addPlants(plantsThisTick, usedThisTick, globalOccupied, LAVENDER, 1, tick, 2);
                addPlants(plantsThisTick, usedThisTick, globalOccupied, DWARF_SUNFLOWER, 1, tick, 3);
                if (!isWinter) {
                    addPlants(plantsThisTick, usedThisTick, globalOccupied, ROSE_BUSH, 1, tick, 2);
                }
            }
            if (plantsThisTick.size() > MAX_PLANTS_PER_TICK) {
                plantsThisTick = new ArrayList<>(plantsThisTick.subList(0, MAX_PLANTS_PER_TICK));
            }
            for (PlantAction p : plantsThisTick) {
                globalOccupied.add(p.row + "," + p.col);
            }
            if (!plantsThisTick.isEmpty()) {
                result.add(new TickAction(tick, plantsThisTick));
            }
        }
        return result;
    }
    static void addPlants(List<PlantAction> plants, Set<String> usedThisTick, Set<String> globalOccupied,
                          int plantIndex, int count, int tick, int minSpacing) {
        Random random = new Random(tick * 31L + plantIndex * 17L + 991L);
        int attempts = 0;
        int maxAttempts = count * 250;
        while (count > 0 && attempts < maxAttempts) {
            attempts++;
            int row = random.nextInt(N);
            int col = random.nextInt(M);
            if (minSpacing > 1) {
                row = (row / minSpacing) * minSpacing + random.nextInt(minSpacing);
                col = (col / minSpacing) * minSpacing + random.nextInt(minSpacing);
                row = Math.min(Math.max(row, 0), N - 1);
                col = Math.min(Math.max(col, 0), M - 1);
            }
            String position = row + "," + col;
            if (usedThisTick.contains(position) || globalOccupied.contains(position)) {
                continue;
            }
            boolean tooClose = false;
            if (minSpacing > 1) {
                for (int dr = -minSpacing + 1; dr < minSpacing && !tooClose; dr++) {
                    for (int dc = -minSpacing + 1; dc < minSpacing && !tooClose; dc++) {
                        if (dr == 0 && dc == 0) continue;
                        int nr = row + dr;
                        int nc = col + dc;
                        if (nr >= 0 && nr < N && nc >= 0 && nc < M) {
                            String neighbor = nr + "," + nc;
                            if (globalOccupied.contains(neighbor) || usedThisTick.contains(neighbor)) {
                                tooClose = true;
                            }
                        }
                    }
                }
            }
            if (!tooClose) {
                usedThisTick.add(position);
                plants.add(new PlantAction(plantIndex, row, col));
                count--;
            }
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
        int plantIndex;
        int row;
        int col;
        PlantAction(int plantIndex, int row, int col) {
            this.plantIndex = plantIndex;
            this.row = row;
            this.col = col;
        }
    }
    static class TickAction {
        int tick;
        List<PlantAction> plants;
        TickAction(int tick, List<PlantAction> plants) {
            this.tick = tick;
            this.plants = plants;
        }
    }
    static void writeJson(String filename, List<TickAction> actions) {
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
                    writer.write("          \"plant_index\": " + plant.plantIndex + ",\n");
                    writer.write("          \"row\": " + plant.row + ",\n");
                    writer.write("          \"col\": " + plant.col + "\n");
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
            LOGGER.log(Level.SEVERE, "Failed to create solution file", e);
        }
    }
}