import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.io.ByteArrayOutputStream;

public class TestRobotSimulation {
    
    private RobotSimulation robot;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        robot = new RobotSimulation(10); // 10x10 floor
    }
    
    @Test
    void testInitializeWithZeroSize() {
        // Capture console output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        RobotSimulation currentRobot = null;
        // Pass the "I 0" command to processCommand.
        // By design, the code should print "Grid size not valid error." and not create a robot.
        currentRobot = RobotSimulation.processCommand(currentRobot, "I 0");

        // Convert captured output to string for inspection
        String consoleOutput = outContent.toString();

        // Assert that the console output contains the expected message
        assertTrue(consoleOutput.contains("Grid size is not valid"),
                   "Expected 'Grid size not valid error' message, but got:\n" + consoleOutput);

        // Clean up: restore original System.out
        System.setOut(originalOut);
    }

    @Test
    void testInitializeSystem() {
        assertEquals(0, robot.getX());
        assertEquals(0, robot.getY());
        assertFalse(robot.isPenDown());
        assertEquals("NORTH", robot.getFacing());
    }

    @Test
    void testPenUpAndDown() {
        robot.penDown();
        assertTrue(robot.isPenDown());

        robot.penUp();
        assertFalse(robot.isPenDown());
    }

    @Test
    void testMoveWithoutPen() {
        robot.move(4);
        assertEquals(0, robot.getX());
        assertEquals(4, robot.getY());
        assertFalse(robot.isFloorMarked(0, 1)); // Pen is up, floor should remain 0
    }

    @Test
    void testMoveWithPen() {
        robot.penDown();
        robot.move(4);
        assertEquals(0, robot.getX());
        assertEquals(4, robot.getY());
        assertTrue(robot.isFloorMarked(0, 1));
        assertTrue(robot.isFloorMarked(0, 2));
        assertTrue(robot.isFloorMarked(0, 3));
        assertTrue(robot.isFloorMarked(0, 4));
    }
    
    @Test
    void testMoveBeyondBoundary() {
        robot.move(15); // Move more than grid size (10X10)
        assertEquals(0, robot.getX());
        assertEquals(9, robot.getY());
    }

    @Test
    void testTurnRight() {
        robot.turnRight();
        assertEquals("EAST", robot.getFacing());
        robot.turnRight();
        assertEquals("SOUTH", robot.getFacing());
        robot.turnRight();
        assertEquals("WEST", robot.getFacing());
        robot.turnRight();
        assertEquals("NORTH", robot.getFacing());
    }
    
    @Test
    void testTurnLeft() {
    	robot.turnLeft();
    	assertEquals("WEST", robot.getFacing());
    	robot.turnLeft();
    	assertEquals("SOUTH", robot.getFacing());
    	robot.turnLeft();
    	assertEquals("EAST", robot.getFacing());
    	robot.turnLeft();
    	assertEquals("NORTH", robot.getFacing());
    }

    @Test
    void testTurnRightAndMove() {
        robot.move(4); // Move north
        robot.turnRight();
        assertEquals("EAST", robot.getFacing());

        robot.move(3); // Move east
        assertEquals(3, robot.getX());
        assertEquals(4, robot.getY());
    }

    @Test
    void testPrintFloor() {
        robot.penDown();
        robot.move(4);
        robot.turnRight();
        robot.move(3);
        robot.printFloor(); // Should not throw an exception
    }
    
    @Test
    void testPrintCurrentStatePenUp() {
    	robot.move(2);
    	String expectedState = "Position: [0, 2]\nPen: Up\nFacing: NORTH";
    	assertEquals(expectedState, robot.getCurrentState());
    }
    
    @Test
    void testPrintCurrentStatePenDown() {
        robot.penDown();
        robot.turnRight();
        String expectedState = "Position: [0, 0]\nPen: Down\nFacing: EAST";
        assertEquals(expectedState, robot.getCurrentState());
    }
    
    @Test
    void testGetAndReplayHistory() {
    	robot.penDown();
    	robot.move(2);
    	robot.turnRight();
    	List<String> history = robot.getAndReplayHistory(true);
    	assertEquals(3, history.size()); // Check if three commands were recorded
        assertEquals("D", history.get(0));
        assertEquals("M 2", history.get(1));
        assertEquals("R", history.get(2));
    }
    
    @Test
    void testMainMethodCoverage() {
        // 1) Provide a fake input sequence
        String userInput = String.join("\n",
            "I 10",
            "U",
            "D",
            "M 4",
            "P",
            "C",
            "XYZ",    // unknown command
            "Q"       // triggers exit
        ) + "\n"; // final newline

        // 2) Redirect System.in
        ByteArrayInputStream in = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(in);

        // 3) Capture System.out
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(out));

        // 4) Call main
        RobotSimulation.main(new String[]{});

        // 5) Restore System.in and System.out
        System.setIn(System.in);
        System.setOut(originalOut);

        // Optionally: check console output
        String consoleOutput = out.toString();
        assertTrue(consoleOutput.contains("System initialized with a 10x10 floor."));
        assertTrue(consoleOutput.contains("Exiting..."));
    }
    
    @Test
    void testProcessCommandAllScenarios() {
        RobotSimulation robot = null;

        // Null or empty input
        robot = RobotSimulation.processCommand(robot, null);
        robot = RobotSimulation.processCommand(robot, "");

        // Robot not initialized yet (should print "Robot not initialized. Use 'I n' first.")
        robot = RobotSimulation.processCommand(robot, "U");
        robot = RobotSimulation.processCommand(robot, "D");
        robot = RobotSimulation.processCommand(robot, "R");
        robot = RobotSimulation.processCommand(robot, "L");
        robot = RobotSimulation.processCommand(robot, "M");
        robot = RobotSimulation.processCommand(robot, "P");
        robot = RobotSimulation.processCommand(robot, "C");
        robot = RobotSimulation.processCommand(robot, "H");

        // Unknown command
        robot = RobotSimulation.processCommand(robot, "XYZ");

        // Missing argument for I
        robot = RobotSimulation.processCommand(robot, "I");

        // Invalid argument for I
        robot = RobotSimulation.processCommand(robot, "I abc");

        // Now initialize properly
        robot = RobotSimulation.processCommand(robot, "I 5");

        // Valid commands
        robot = RobotSimulation.processCommand(robot, "U");
        robot = RobotSimulation.processCommand(robot, "D");
        robot = RobotSimulation.processCommand(robot, "R");
        robot = RobotSimulation.processCommand(robot, "L");
        robot = RobotSimulation.processCommand(robot, "M 3");
        robot = RobotSimulation.processCommand(robot, "P");
        robot = RobotSimulation.processCommand(robot, "C");
        robot = RobotSimulation.processCommand(robot, "H");

        // Missing argument for M
        robot = RobotSimulation.processCommand(robot, "M");

        // Invalid argument for M
        robot = RobotSimulation.processCommand(robot, "M abc");

        // Quit command
        robot = RobotSimulation.processCommand(robot, "Q");
    }
    
    @Test
    void testMoveEast() {
        robot.turnRight(); // Facing EAST
        robot.move(3);
        assertEquals(3, robot.getX()); // Ensure movement to the right
        assertEquals(0, robot.getY()); // Y should remain the same
    }

    @Test
    void testMoveSouth() {
        robot.turnRight(); // Facing EAST
        robot.turnRight(); // Facing SOUTH
        robot.move(2);
        assertEquals(0, robot.getX()); // X remains same
        assertEquals(0, robot.getY()); // Y should not go negative (boundary check)
    }

    @Test
    void testMoveWest() {
        robot.turnLeft(); // Facing WEST
        robot.move(2);
        assertEquals(0, robot.getX()); // Should not move left beyond boundary
    }



    
}
