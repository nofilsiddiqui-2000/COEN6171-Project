import org.junit.jupiter.api.BeforeEach;

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
}
