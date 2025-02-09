import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class TestRobotSimulation {
	private RobotSimulation robot;
	
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
    void testTurnRight() {
        robot.turnRight();
        assertEquals("EAST", robot.getFacing());
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
    void testHistoryReplay() {
        robot.penDown();
        robot.move(4);
        robot.turnRight();
        robot.move(3);
        robot.penUp();

        List<String> history = robot.getHistory();
        assertEquals(List.of("D", "M 4", "R", "M 3", "U"), history);
    }
}
