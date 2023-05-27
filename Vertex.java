import java.util.ArrayList;

public class Vertex {
    int x, y;
    char value;
    ArrayList<Edge> edges;
    boolean visited;
    Vertex parent;
    
    public Vertex(int x, int y, char value) {
        this.x = x;
        this.y = y;
        this.value = value;
        this.edges = new ArrayList<>();
        visited = false;
        parent = null;
    }
}