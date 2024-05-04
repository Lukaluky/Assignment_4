import java.util.HashMap;
import java.util.Map;

// Define basic components of a maze
class Maze {
    private Map<Integer, Room> rooms = new HashMap<>();

    public void addRoom(Room r) {
        rooms.put(r.getRoomNo(), r);
    }

    public Room roomNo(int r) {
        return rooms.get(r);
    }
}

enum Direction {
    NORTH, EAST, SOUTH, WEST
}

class Room {
    private Map<Direction, Wall> sides = new HashMap<>();
    private int roomNo;

    public Room(int roomNo) {
        this.roomNo = roomNo;
    }

    public Wall getSide(Direction direction) {
        return sides.get(direction);
    }

    public void setSide(Direction direction, Wall wall) {
        sides.put(direction, wall);
    }

    public int getRoomNo() {
        return roomNo;
    }
}

class Wall {
    public void enter() {
        System.out.println("You just hit a wall.");
    }
}

class DoorWall extends Wall {
    private Room r1;
    private Room r2;
    private boolean isOpen;

    public DoorWall(Room r1, Room r2) {
        this.r1 = r1;
        this.r2 = r2;
        this.isOpen = false;
    }

    @Override
    public void enter() {
        if (isOpen) {
            System.out.println("You pass through the door.");
        } else {
            System.out.println("The door is closed.");
        }
    }

    public void open() {
        isOpen = true;
    }

    public void close() {
        isOpen = false;
    }
}

// Define the builder interface
interface MazeBuilder {
    void buildMaze();

    void buildRoom(int roomNo);

    void buildDoor(int roomFrom, int roomTo);

    Maze getMaze();
}

// Implement the builder interface
class StandardMazeBuilder implements MazeBuilder {
    private Maze currentMaze;

    public void buildMaze() {
        currentMaze = new Maze();
    }

    public void buildRoom(int roomNo) {
        Room room = new Room(roomNo);
        currentMaze.addRoom(room);
        room.setSide(Direction.NORTH, new Wall());
        room.setSide(Direction.EAST, new Wall());
        room.setSide(Direction.SOUTH, new Wall());
        room.setSide(Direction.WEST, new Wall());
    }

    public void buildDoor(int roomFrom, int roomTo) {
        Room r1 = currentMaze.roomNo(roomFrom);
        Room r2 = currentMaze.roomNo(roomTo);
        DoorWall d = new DoorWall(r1, r2);
        r1.setSide(Direction.SOUTH, d);
        r2.setSide(Direction.NORTH, d);
        d.open();
    }

    public Maze getMaze() {
        return currentMaze;
    }
}

// Director class that uses the builder to construct the maze
class MazeGameDirector {
    private MazeBuilder builder;

    public MazeGameDirector(MazeBuilder builder) {
        this.builder = builder;
    }

    public Maze construct() {
        builder.buildMaze();
        builder.buildRoom(1);
        builder.buildRoom(2);
        builder.buildDoor(1, 2);
        return builder.getMaze();
    }
}

// Main class to run the maze game
public class MazeGame {
    public static void main(String[] argv) {
        MazeBuilder builder = new StandardMazeBuilder();
        MazeGameDirector director = new MazeGameDirector(builder);
        Maze maze = director.construct();
        Room startRoom = maze.roomNo(1);
        startRoom.getSide(Direction.SOUTH).enter();  // Simulate entering the door
    }
}
