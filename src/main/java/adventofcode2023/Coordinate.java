package adventofcode2023;

import java.util.ArrayList;
import java.util.Objects;

public class Coordinate {
    @Override
    public int hashCode() {
        return Objects.hash(line, column);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Coordinate other = (Coordinate) obj;
        return line == other.line && column == other.column;
    }

    public Coordinate(int line, int column) {
        this.line = line;
        this.column = column;
    }

    final int line;
    final int column;

    /**
     * Return line, column tuples that are adjacent and on the grid.
     * 
     * @param <T>
     *            the type of object in grid
     * @param grid
     *            the grid
     * @return list of valid adjacent positions
     */
    <T extends Object> ArrayList<Coordinate> adjacentCoordinates(ArrayList<ArrayList<T>> grid) {
        var out = new ArrayList<Coordinate>();
        if (line > 0) {
            if (column > 0)
                out.add(new Coordinate(line - 1, column - 1));
            out.add(new Coordinate(line - 1, column));
            if (column < grid.get(line).size() - 1)
                out.add(new Coordinate(line - 1, column + 1));
        }
        if (column > 0)
            out.add(new Coordinate(line, column - 1));
        if (column < grid.get(line).size() - 1)
            out.add(new Coordinate(line, column + 1));
        if (line < grid.size() - 1) {
            if (column > 0)
                out.add(new Coordinate(line + 1, column - 1));
            out.add(new Coordinate(line + 1, column));
            if (column < grid.get(line).size() - 1)
                out.add(new Coordinate(line + 1, column + 1));
        }
        return out;
    }

    @Override
    public String toString() {
        return "Coordinate [line=" + line + ", column=" + column + "]";
    }
}