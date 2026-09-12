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
import java.util.logging.Level;
import java.util.logging.Logger;
public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
    static final int N = 70;
    static final int M = 100;
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
        int total = actions.stream().mapToInt(a -> a.plants.size()).sum();
        LOGGER.info("Level 2 solution written to level2_solution.json");
        LOGGER.info("Total planting actions: " + total);
        LOGGER.info("Number of ticks with actions: " + actions.size());
    }
    static List<TickAction> generateLevel2Strategy() {
        List<TickAction> result = new ArrayList<>();
        Set<String> globalOccupied = new HashSet<>();
        for (int tick = 0; tick < T - 1; tick++) {
            List<PlantAction> plants = new ArrayList<>();
            Set<String> usedThisTick = new HashSet<>();
            String season = getSeason(tick);
            boolean isWinter = "Winter".equals(season);
            int phase = tick / 100;
            if (phase == 0) {
                add(plants, usedThisTick, globalOccupied, OAK_TREE, 6, tick, 6);
                add(plants, usedThisTick, globalOccupied, GRASS, 3, tick, 1);
                add(plants, usedThisTick, globalOccupied, LAVENDER, 2, tick, 2, isWinter);
                add(plants, usedThisTick, globalOccupied, DWARF_SUNFLOWER, 2, tick, 3);
                if (!isWinter) {
                    add(plants, usedThisTick, globalOccupied, ROSE_BUSH, 2, tick, 2);
                }
            } else if (phase == 1) {
                add(plants, usedThisTick, globalOccupied, OAK_TREE, 7, tick, 5);
                add(plants, usedThisTick, globalOccupied, GRASS, 2, tick, 1);
                add(plants, usedThisTick, globalOccupied, LAVENDER, 2, tick, 2, isWinter);
                add(plants, usedThisTick, globalOccupied, DWARF_SUNFLOWER, 2, tick, 3);
                if (!isWinter) {
                    add(plants, usedThisTick, globalOccupied, ROSE_BUSH, 1, tick, 2);
                }
                add(plants, usedThisTick, globalOccupied, BLUE_MOSS, 2, tick, 2);
            } else if (phase == 2) {
                add(plants, usedThisTick, globalOccupied, OAK_TREE, 8, tick, 5);
                add(plants, usedThisTick, globalOccupied, GRASS, 1, tick, 1);
                add(plants, usedThisTick, globalOccupied, LAVENDER, 1, tick, 2, isWinter);
                add(plants, usedThisTick, globalOccupied, DWARF_SUNFLOWER, 1, tick, 3);
                if (!isWinter) {
                    add(plants, usedThisTick, globalOccupied, ROSE_BUSH, 1, tick, 2);
                }
                add(plants, usedThisTick, globalOccupied, BLUE_MOSS, 2, tick, 2);
                add(plants, usedThisTick, globalOccupied, CRIMSON_VINE, 1, tick, 2);
            } else {
                add(plants, usedThisTick, globalOccupied, OAK_TREE, 9, tick, 4);
                add(plants, usedThisTick, globalOccupied, GRASS, 1, tick, 1);
                add(plants, usedThisTick, globalOccupied, LAVENDER, 1, tick, 2, isWinter);
                add(plants, usedThisTick, globalOccupied, DWARF_SUNFLOWER, 1, tick, 3);
                if (!isWinter) {
                    add(plants, usedThisTick, globalOccupied, ROSE_BUSH, 1, tick, 2);
                }
                add(plants, usedThisTick, globalOccupied, BLUE_MOSS, 1, tick, 2);
                add(plants, usedThisTick, globalOccupied, CRIMSON_VINE, 1, tick, 2);
                add(plants, usedThisTick, globalOccupied, ORANGE_BLOSSOM, 1, tick, 2, isWinter);
                add(plants, usedThisTick, globalOccupied, SILVER_FERN, 1, tick, 4);
                add(plants, usedThisTick, globalOccupied, GLOWCAP_FUNGUS, 1, tick, 3);
            }
            if (plants.size() > MAX_PLANTS_PER_TICK) {
                plants = new ArrayList<>(plants.subList(0, MAX_PLANTS_PER_TICK));
            }
            for (PlantAction p : plants) {
                globalOccupied.add(p.row + "," + p.col);
            }
            if (!plants.isEmpty()) {
                result.add(new TickAction(tick, plants));
            }
        }
        return result;
    }
    static void add(List<PlantAction> plants, Set<String> usedThisTick, Set<String> globalOccupied,
                    int plantIndex, int count, int tick, int minSpacing) {
        add(plants, usedThisTick, globalOccupied, plantIndex, count, tick, minSpacing, false);
    }
    static void add(List<PlantAction> plants, Set<String> usedThisTick, Set<String> globalOccupied,
                    int plantIndex, int count, int tick, int minSpacing, boolean isWinter) {
        if (isWinter) {
            if (plantIndex == ROSE_BUSH || plantIndex == ORANGE_BLOSSOM || plantIndex == LAVENDER) {
                return;
            }
        }
        Random rnd = new Random(tick * 37L + plantIndex * 17L + 54321L);
        int attempts = 0;
        int maxAttempts = Math.max(count * 300, 400);
        while (count > 0 && attempts < maxAttempts) {
            attempts++;
            int row = rnd.nextInt(N);
            int col = rnd.nextInt(M);
            if (minSpacing > 1) {
                row = (row / minSpacing) * minSpacing + rnd.nextInt(minSpacing);
                col = (col / minSpacing) * minSpacing + rnd.nextInt(minSpacing);
                row = Math.min(Math.max(row, 0), N - 1);
                col = Math.min(Math.max(col, 0), M - 1);
            }
            String key = row + "," + col;
            if (usedThisTick.contains(key) || globalOccupied.contains(key)) continue;
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
                usedThisTick.add(key);
                plants.add(new PlantAction(plantIndex, row, col));
                count--;
            }
        }
    }
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
            LOGGER.log(Level.SEVERE, "Failed to write file", e);
        }
    }
}