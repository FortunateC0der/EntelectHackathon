# Entelect Hackathon - Level 1

This project contains the Java solution for Level 1 of the Entelect Hackathon challenge.

The solution generates a planting strategy across multiple game ticks and writes the resulting actions to a JSON submission file.

## Requirements

Before running the project, make sure you have:

* Java Development Kit (JDK) 17 or higher
* Apache Maven
* Visual Studio Code
* Java Extension Pack for VS Code

You can verify Java and Maven from the VS Code terminal.

### Check Java

```bash
java -version
```

The project is configured for Java 17.

Expected output will look similar to:

```text
java version "17.x.x"
```

### Check Maven

```bash
mvn -version
```

You should see Maven information together with the Java version being used.

---

## Project Structure

The project should have the following structure:

```text
challenge/
│
├── pom.xml
├── README.md
│
└── src/
    └── main/
        └── java/
            └── level1/
                └── Main.java
```

The Java package is:

```java
package level1;
```

The main class is:

```text
level1.Main
```

---

## Maven Configuration

The project uses Java 17.

The `pom.xml` contains:

```xml
<properties>
    <maven.compiler.source>17</maven.compiler.source>
    <maven.compiler.target>17</maven.compiler.target>
</properties>
```

No external Java libraries are required by the solution.

---

## Running the Project

### 1. Open the project in VS Code

Open the `challenge` folder in Visual Studio Code.

The terminal should be opened from the project root:

```text
challenge/
```

You can confirm your current location with:

```bash
cd
```

---

### 2. Compile the project

Run:

```bash
mvn clean compile
```

Maven will:

1. Remove previous build files.
2. Compile the Java source code.
3. Place the compiled classes inside the `target` directory.

If everything is successful, Maven should display:

```text
BUILD SUCCESS
```

---

## If Maven Cannot Download a Plugin

If Maven reports an error similar to:

```text
maven-compiler-plugin failed to transfer
```

or:

```text
Read timed out
```

Maven may have cached a previous failed download.

Run:

```bash
mvn clean compile -U
```

The `-U` option forces Maven to check for updated dependencies and plugins.

Make sure you have an active internet connection.

If the problem continues, close VS Code and remove the cached compiler plugin from:

```text
C:\Users\DELL\.m2\repository\org\apache\maven\plugins\maven-compiler-plugin\
```

Then reopen VS Code and run:

```bash
mvn clean compile -U
```

---

## Running the Level 1 Solution

After the project successfully compiles, run:

```bash
java -cp target/classes level1.Main
```

The program should display something similar to:

```text
Level 1 solution written to level1_solution.json
Total planting actions scheduled:  ...
```

---

## Generated Output

The program creates:

```text
level1_solution.json
```

in the project root.

The project will then look like:

```text
challenge/
│
├── level1_solution.json
├── pom.xml
├── README.md
│
└── src/
    └── main/
        └── java/
            └── level1/
                └── Main.java
```

The generated JSON contains the planting actions for each game tick.

The general format is:

```json
{
  "actions": [
    {
      "tick": 0,
      "plants": [
        {
          "plant_index": 1,
          "row": 5,
          "col": 8
        }
      ]
    }
  ]
}
```

---

## Running Directly from VS Code

If the Java Extension Pack is installed, you can also open:

```text
src/main/java/level1/Main.java
```

and use the **Run** button above the `main` method.

However, using Maven first is recommended:

```bash
mvn clean compile
```

Then run:

```bash
java -cp target/classes level1.Main
```

This confirms that the project builds correctly using the same Maven project structure.

---

## Solution Overview

The Level 1 solution uses a predefined planting strategy.

The strategy considers:

* Grass
* Rose Bush
* Dwarf Sunflower
* Lavender
* Oak Tree
* Game ticks
* Seasons
* Maximum plants per tick
* Plant spacing

The solution divides the game into three stages:

### Early Game

The strategy focuses on establishing:

* Grass
* Lavender
* Dwarf Sunflower
* Rose Bush

### Mid Game

The strategy continues maintaining diversity while introducing:

* Oak Trees

### Late Game

The strategy attempts to maintain all five species while continuing to fill available positions.

---

## Plant Indices

The current solution uses the following plant indices:

| Plant           | Index |
| --------------- | ----: |
| Grass           |     1 |
| Rose Bush       |     2 |
| Dwarf Sunflower |     5 |
| Lavender        |     6 |
| Oak Tree        |    12 |

These values must match the plant definitions provided by the hackathon challenge.

---

## Game Configuration

The current solution is configured for:

```text
Grid height:              20
Grid width:               20
Total ticks:              80
Maximum plants per tick:  20
```

The configured seasons are:

```text
Tick 0  -> Spring
Tick 20 -> Summer
Tick 40 -> Autumn
Tick 60 -> Winter
```

If the official Level 1 configuration uses different values, update the constants in `Main.java`.

---

## Important Notes

The solution generates deterministic planting positions.

This means running the program multiple times with the same configuration produces the same planting strategy.

The solution also prevents multiple generated plants from using the same position within a single tick.

The generated solution should still be validated against the official hackathon game rules because the Java program cannot automatically guarantee that every generated action satisfies rules that are defined externally by the challenge engine.

---

## Quick Start

For a quick run, open the terminal in the `challenge` directory and execute:

```bash
mvn clean compile
```

Then:

```bash
java -cp target/classes level1.Main
```

If successful:

```text
BUILD SUCCESS
Level 1 solution written to level1_solution.json
```

The final submission file will be:

```text
level1_solution.json
```

## Troubleshooting

### `mvn` is not recognized

If Windows reports:

```text
'mvn' is not recognized as an internal or external command
```

Maven is either not installed or its `bin` directory is not included in the system `PATH`.

Install Maven and configure the `MAVEN_HOME`/`PATH` environment variables.

### `java` is not recognized

If Windows reports:

```text
'java' is not recognized as an internal or external command
```

Install JDK 17 and make sure Java is included in the system `PATH`.

### `BUILD FAILURE`

Run:

```bash
mvn clean compile -U
```

Then check the first error reported by Maven.

### cSpell reports `level1` or `hackathon` as an unknown word

This is a VS Code spelling warning and does not affect compilation.

You can safely ignore it.

---

## Author

Entelect Hackathon - Level 1

Java 17
Maven
Visual Studio Code
