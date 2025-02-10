# COEN6171-Project
Imagine a robot that walks around a room under the control of a Java program. The robot holds a pen in one of two positions, up or down. While the pen is down, the robot traces out shapes as it moves; while the pen is up, the robot moves about freely without writing anything. The room will be represented by an N by N array called floor that is initialized to zeros. Initially the robot is at position [0, 0] of the floor, its pen is up, and is facing north (as shown in the below figure).

![image](https://github.com/user-attachments/assets/aaea19bb-0009-4e5a-9a3d-4829539a022f)

The robot moves around the floor (i.e. the array) as it receives commands from the user. The set of robot commands your program must process are as follows:

## Command Reference Table

| Command       | Meaning |
|--------------|---------|
| **\[U\|u\]**   | Pen up |
| **\[D\|d\]**   | Pen down |
| **\[R\|r\]**   | Turn right |
| **\[L\|l\]**   | Turn left |
| **\[M s\|m s\]** | Move forward `s` spaces (`s` is a non-negative integer) |
| **\[P\|p\]**   | Print the `N x N` array and display the indices |
| **\[C\|c\]**   | Print the current position of the pen, whether it is up or down, and its facing direction |
| **\[Q\|q\]**   | Stop the program |
| **\[I n\|i n\]** | Initialize the system: The array `floor` is filled with zeros, and the robot is reset to `[0, 0]`, pen up, and facing north. `n` is the array size (integer > 0) |
| **\[H\|h\]**   | Replay all steps in history since the last start of the program |








