import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Queue;

public class Bfs {
    private static int[] initialNumber;
    public static int fuel = 0;
    public static Vertex[][] allVertexs;
    public static int allRows;
    public static int allColumn;
    public static int value;
    public static int[] coordinateLastNumber;
    public static int number = 2;
    public static int armazenaValue = 1;
    public static int sumFuel = 0;
 
    public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("casos-cohen/mapa2000.txt"));
            String line = reader.readLine();
            String[] values = line.split(" ");
            int rows = allRows = Integer.parseInt(values[0]);
            int cols = allColumn = Integer.parseInt(values[1]);

            Vertex[][] vertexs = new Vertex[rows][cols];
            
            for (int i = 0; i < rows; i++) {
                line = reader.readLine();
                for (int j = 0; j < cols; j++) {
                    char value = line.charAt(j);
                    vertexs[i][j] = new Vertex(i, j, value);
                    if (value == '1') initialNumber = new int[]{i, j}; 
                }
            }

            allVertexs = vertexs;

            reader.close();

            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    Vertex current = vertexs[i][j];
                    char currentValue = current.value;
                    
                    if (currentValue == '*') {
                        continue; // não adiciona arestas para obstáculos
                    }
                    // adiciona uma aresta para a linha de cima
                    if (i > 0 && vertexs[i-1][j].value != '*') {
                        current.edges.add(new Edge(current, vertexs[i-1][j]));
                    }
                    // adiciona uma aresta para a coluna da esquerda
                    if (j > 0 && vertexs[i][j-1].value != '*') {
                        current.edges.add(new Edge(current, vertexs[i][j-1]));
                    }
                    // adiciona uma aresta para a linha de baixo
                    if (i < rows-1 && vertexs[i+1][j].value != '*') {
                        current.edges.add(new Edge(current, vertexs[i+1][j]));
                    }
                    // adiciona uma aresta para a coluna da direita
                    if (j < cols-1 && vertexs[i][j+1].value != '*') {
                        current.edges.add(new Edge(current, vertexs[i][j+1]));
                    }
                }
            }

            bfs(vertexs, initialNumber);
    
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void recursiveParent(Vertex vertex) {
        if (vertex.parent == null){
            System.out.println("Viajando de " + Character.getNumericValue(vertex.value) + " para " + (number - 1) + " foi gasto um total de combustível: " + fuel);
            sumFuel += fuel;
            fuel = 0;
        }
        else {
            Vertex aux = vertex.parent;
            fuel++;
            recursiveParent(aux);
        }
    }

    private static void resetVisited(){
        for (int i = 0; i < allRows; i++) {
            for (int j = 0; j < allColumn; j++) {
                allVertexs[i][j].visited = false;
            }
        }

    }

    private static void bfs(Vertex[][] vertex, int[] start) {
        Queue<int[]> queue = new ArrayDeque<>();
        queue.offer(start);

        while (!queue.isEmpty()) {
            int[] curr = queue.poll();
            int x = curr[0];
            int y = curr[1];
        
            vertex[x][y].visited = true;

            if (Character.getNumericValue(vertex[x][y].value) == number) {
                number++;
                recursiveParent(vertex[x][y]);
                if (number == 2) {
                    System.out.println("Total de combustível: " + sumFuel + " :: dentro do while if"); 
                    break;
                }
                vertex[x][y].parent = null;
                value = Character.getNumericValue(vertex[x][y].value);
                coordinateLastNumber = new int[]{x, y};
                resetVisited();
                queue.clear();

                if (number > 9) {
                    int newX = initialNumber[0];
                    int newY = initialNumber[1];
                    vertex[newX][newY].parent = null; 
                    number = 1;
                    bfs(vertex, coordinateLastNumber);
                    break;
                }
            }

            for (Edge edge : vertex[x][y].edges) {
                Vertex edges = edge.to;
        
                if (!edges.visited) {
                    edges.visited = true;
                    if (edges.parent == null && Character.getNumericValue(edges.value) == value){
                        edges.parent = null;
                    } else {
                        edges.parent = vertex[x][y];
                    }
                    queue.add(new int[]{edges.x, edges.y});
                }
            }

            if (queue.isEmpty() && number != 10){
                if (number == 2) {
                    if (sumFuel != 0){
                        System.out.println("Total de combustível: " + sumFuel + " :: dentro da lista vazia"); 
                        break;
                    }
                    else {
                        System.out.println("Viajando de " + (number - 1) + " para " + number + " não foi encontrado nenhum caminho!");
                        number++;
                        resetVisited();
                        bfs(allVertexs, initialNumber);
                        break;
                    }
                }
                System.out.println("Viajando de " + (number - 1) + " para " + number + " não foi encontrado nenhum caminho!");
                number++;
                if (number == 10) number = 1;
                resetVisited();
                bfs(allVertexs, coordinateLastNumber);
                break;
            }

        }
        // if (number - 1 == 1) break;
    }
}

// VERIFICAR SE O PRIMEIRO NÃO EXISTE
// VERIFICAR SE O ULTIMO NAO EXISTE.
