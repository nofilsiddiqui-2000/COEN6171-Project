import java.util.*;

public class RobotSimulation {
    private int[][] floor;
    private int x, y;
    private boolean penDown;
    private enum Direction { NORTH, SOUTH, EAST, WEST }
    private Direction facing;
    private List<String> history;

    // Constructor
    public RobotSimulation(int n) {
        // If you also want to prevent zero/negative at the constructor level:
        if (n <= 0) {
            throw new IllegalArgumentException("Floor size must be greater than 0.");
        }
        floor = new int[n][n];
        x = 0;
        y = 0;
        penDown = false;
        facing = Direction.NORTH;
        history = new ArrayList<>();
    }

    public int getX() { 
        return x; 
    }

    public int getY() {
        return y;
    }

    public void penUp() {
        penDown = false;
        history.add("U");
    }

    public void penDown() {
        penDown = true;
        history.add("D");
    }

    public boolean isPenDown() { 
        return penDown; 
    }

    public String getFacing() { 
        return facing.name(); 
    }

    public void turnRight() {
        switch (facing) {
            case NORTH: facing = Direction.EAST; break;
            case EAST:  facing = Direction.SOUTH; break;
            case SOUTH: facing = Direction.WEST;  break;
            case WEST:  facing = Direction.NORTH; break;
        }
        history.add("R");
    }

    public void turnLeft() {
        switch (facing) {
            case NORTH: facing = Direction.WEST;  break;
            case WEST:  facing = Direction.SOUTH; break;
            case SOUTH: facing = Direction.EAST;  break;
            case EAST:  facing = Direction.NORTH; break;
        }
        history.add("L");
    }

    public void move(int steps) {
        for (int i = 0; i < steps; i++) {
            int newX = x, newY = y;
            switch (facing) {
                case NORTH: newY++; break;
                case SOUTH: newY--; break;
                case EAST:  newX++; break;
                case WEST:  newX--; break;
            }
            // Check boundaries
            if (newX >= 0 && newX < floor.length && newY >= 0 && newY < floor.length) {
                x = newX;
                y = newY;
                if (penDown) {
                    floor[y][x] = 1;
                }
            }
        }
        history.add("M " + steps);
    }

    public void printFloor() {
        for (int i = floor.length - 1; i >= 0; i--) {
            System.out.printf("%2d ", i);
            for (int j = 0; j < floor.length; j++) {
                if (floor[i][j] == 1) {
                    System.out.print("* ");
                } else {
                    System.out.print("  ");
                }
            }
            System.out.println();
        }
        // Print the column indices at the bottom
        System.out.print("   ");
        for (int j = 0; j < floor.length; j++) {
            System.out.print(j + " ");
        }
        System.out.println();
    }

    public String getCurrentState() {
        return "Position: [" + x + ", " + y + "]\n"
               + "Pen: " + (penDown ? "Down" : "Up") + "\n"
               + "Facing: " + facing;
    }

    public List<String> getAndReplayHistory(boolean print) {
        if (print) {
            for (String command : history) {
                System.out.println("Executing: " + command);
            }
        }
        return new ArrayList<>(history);
    }

    public boolean isFloorMarked(int x, int y) { 
        return floor[y][x] == 1; 
    }

    /**
     * Processes a single command string (extracted from main).
     * This allows JUnit tests to call it directly, improving coverage.
     *
     * @param currentRobot The current RobotSimulation instance (can be null if not initialized).
     * @param input The raw command string from user or tests.
     * @return The (possibly updated) RobotSimulation instance.
     */
    public static RobotSimulation processCommand(RobotSimulation currentRobot, String input) {
        if (input == null || input.trim().isEmpty()) {
            System.out.println("No command entered.");
            return currentRobot;
        }

        String[] parts = input.trim().split("\\s+");
        String command = parts[0].toUpperCase();

        try {
            switch (command) {
                case "I":
                    if (parts.length < 2) {
                        System.out.println("Missing size argument for 'I' command.");
                        return currentRobot;
                    }
                    String sizeArg = parts[1];

                    // If the user explicitly types "0", show "grid size not valid error."
//                    if ("0".equals(sizeArg)) {
//                        System.out.println("Grid size is not valid.");
//                        return currentRobot;
//                    }

                    // Reject any string that isn't strictly positive with no leading zeros 
                    // (must start 1-9, followed by zero or more digits).
//                    if (!sizeArg.matches("[1-9]\\d*")) {
//                        System.out.println("Invalid format for size. Please try again.");
//                        return currentRobot;
//                    }

                    // Now it's safe to parse the integer (>=1, no leading zeros).
                    int size = Integer.parseInt(sizeArg);
                    if (size <= 0) {
                    	System.out.println("Grid size is not valid");
                    	return currentRobot;
                    }

                    // Create the new robot simulation
                    currentRobot = new RobotSimulation(size);
                    System.out.println("System initialized with a " + size + "x" + size + " floor.");
                    break;

                case "U":
                    if (currentRobot == null) {
                        System.out.println("Robot not initialized. Use 'I n' first.");
                        break;
                    }
                    currentRobot.penUp();
                    break;

                case "D":
                    if (currentRobot == null) {
                        System.out.println("Robot not initialized. Use 'I n' first.");
                        break;
                    }
                    currentRobot.penDown();
                    break;

                case "R":
                    if (currentRobot == null) {
                        System.out.println("Robot not initialized. Use 'I n' first.");
                        break;
                    }
                    currentRobot.turnRight();
                    break;

                case "L":
                    if (currentRobot == null) {
                        System.out.println("Robot not initialized. Use 'I n' first.");
                        break;
                    }
                    currentRobot.turnLeft();
                    break;

                case "M":
                    if (currentRobot == null) {
                        System.out.println("Robot not initialized. Use 'I n' first.");
                        break;
                    }
                    if (parts.length < 2) {
                        System.out.println("Missing steps argument for 'M' command.");
                        break;
                    }
                    currentRobot.move(Integer.parseInt(parts[1]));
                    break;

                case "P":
                    if (currentRobot == null) {
                        System.out.println("Robot not initialized. Use 'I n' first.");
                        break;
                    }
                    currentRobot.printFloor();
                    break;

                case "C":
                    if (currentRobot == null) {
                        System.out.println("Robot not initialized. Use 'I n' first.");
                        break;
                    }
                    System.out.println(currentRobot.getCurrentState());
                    break;

                case "H":
                    if (currentRobot == null) {
                        System.out.println("Robot not initialized. Use 'I n' first.");
                        break;
                    }
                    currentRobot.getAndReplayHistory(true);
                    break;

                case "Q":
                    System.out.println("Exiting... (test-friendly, no System.exit)");
                    break;

                default:
                    System.out.println("Unknown command");
                    break;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format. Please try again.");
        }

        return currentRobot;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        RobotSimulation robot = null;

        while (true) {
            System.out.println("Enter command:");
            String input = scanner.nextLine();

            // Call the new processCommand method:
            robot = processCommand(robot, input);

            // If user typed "Q" (or "q"), break
            if ("Q".equalsIgnoreCase(input.trim())) {
                break;
            }
        }
        System.out.println("Program ended.");
    }
}