import java.util.*;

public class RobotSimulation {
    private int[][] floor;
    private int x, y;
    private boolean penDown;
    private enum Direction { NORTH, SOUTH, EAST, WEST }
    private Direction facing;
    private List<String> history;

    public RobotSimulation(int n) {
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
            case EAST: facing = Direction.SOUTH; break;
            case SOUTH: facing = Direction.WEST; break;
            case WEST: facing = Direction.NORTH; break;
        }
        history.add("R");
    }

    public void turnLeft() {
        switch (facing) {
            case NORTH: facing = Direction.WEST; break;
            case WEST: facing = Direction.SOUTH; break;
            case SOUTH: facing = Direction.EAST; break;
            case EAST: facing = Direction.NORTH; break;
        }
        history.add("L");
    }

    public void move(int steps) {
        for (int i = 0; i < steps; i++) {
            int newX = x, newY = y;
            switch (facing) {
                case NORTH: newY++; break;
                case SOUTH: newY--; break;
                case EAST: newX++; break;
                case WEST: newX--; break;
            }
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
        // Print grid with row numbers
        for (int i = floor.length - 1; i >= 0; i--) {
            System.out.printf("%2d ", i); // Row numbers
            for (int j = 0; j < floor.length; j++) {
                if (floor[i][j] == 1) {
                    System.out.print("* "); // Print '*' for 1
                } else {
                    System.out.print("  "); // Print nothing for 0
                }
            }
            System.out.println(); // Move to the next line
        }
        // Print top row numbers
        System.out.print("   "); // Offset for row numbers
        for (int j = 0; j < floor.length; j++) {
            System.out.print(j + " "); // Print each column number
        }
        System.out.println();
    }

    public void printCurrentState() {
        System.out.println("Position: [" + x + ", " + y + "]");
        System.out.println("Pen: " + (penDown ? "Down" : "Up"));
        System.out.println("Facing: " + facing);
    }

    public void replayHistory() {
        for (String command : history) {
            System.out.println("Executing: " + command);
        }
    }
    
    public List<String> getHistory() {
        return new ArrayList<>(history); // Return a copy to avoid external modification
    }
    
    public boolean isFloorMarked(int x, int y) { 
        return floor[y][x] == 1; 
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        RobotSimulation robot = null; // Declare robot outside the loop to initialize later
        
        while (true) {
            System.out.println("Enter command:");
            String input = scanner.nextLine();
            
            // Handle cases where the user inputs a blank line
            if (input.isEmpty()) {
                continue;
            }

            String[] parts = input.split(" ");
            String command = parts[0].toUpperCase();
            
            try {
                switch (command) {
                    case "I": 
                        int size = Integer.parseInt(parts[1]); // Parse size for the floor
                        robot = new RobotSimulation(size); // Initialize robot with the given size
                        System.out.println("System initialized with a " + size + "x" + size + " floor.");
                        break;
                    case "U": 
                        robot.penUp(); 
                        break;
                    case "D": 
                        robot.penDown(); 
                        break;
                    case "R": 
                        robot.turnRight(); 
                        break;
                    case "L": 
                        robot.turnLeft(); 
                        break;
                    case "M": 
                        robot.move(Integer.parseInt(parts[1])); 
                        break;
                    case "P": 
                        robot.printFloor(); 
                        break;
                    case "C": 
                        robot.printCurrentState(); 
                        break;
                    case "Q": 
                        System.out.println("Exiting...");
                        System.exit(0);
                    case "H": 
                        robot.replayHistory(); 
                        break;
                    default: 
                        System.out.println("Unknown command"); 
                        break;
                }
            } catch (Exception e) {
                System.out.println("Invalid command. Please try again.");
            }
        }
    }
}
