package hackathon;

public class Main {
    public static void main(String[] args) {
        Set <String> plantToUnlockSpecies = new HashSet<>();
        plantToUnlockSpecies.add("Grass");
        plantToUnlockSpecies.add("Rose Bush");
        plantToUnlockSpecies.add("Lavender");
        plantToUnlockSpecies.add("Dwarf Sunflower");
        plantToUnlockSpecies.add("Oak Tree");
    }

    class Plant {
        String name;
        int timeToMature;
        double rateOfSpread;
        double survivalRate;

        Plant(String name, int timeToMature, double rateOfSpread) {
            this.name = name;
            this.timeToMature = timeToMature;
            this.rateOfSpread = rateOfSpread;
        }
    }
}