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
    public static int sumFuel = 0;
 
    public static void main(String[] args) {
        try {
            // lê o arquivo
            BufferedReader reader = new BufferedReader(new FileReader("casos-cohen/mapa2000.txt"));
            String line = reader.readLine();
            String[] values = line.split(" ");
            // separa a primeira linha entre linha e coluna e armazena os valores.
            int rows = allRows = Integer.parseInt(values[0]);
            int cols = allColumn = Integer.parseInt(values[1]);

            // cria uma matriz de vértices
            Vertex[][] vertexs = new Vertex[rows][cols];
            
            // para cada linha, pega sua coluna e cria um vértice com sua posição e valor
            for (int i = 0; i < rows; i++) {
                line = reader.readLine();
                for (int j = 0; j < cols; j++) {
                    char value = line.charAt(j);
                    vertexs[i][j] = new Vertex(i, j, value);
                    if (value == '1') initialNumber = new int[]{i, j}; // se o numero for 1, armazena sua coordenada
                }
            }

            // cria uma auxiliar de vertices.
            allVertexs = vertexs;

            reader.close();

            // para cada linha e coluna da matrix, verifica seus adjascentes,
            // se for válido e diferente de *, adiciona como sua aresta.
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

    // método utilizado para voltar ao elemento pai depois de encontrar o elemento que deseja.
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

    // método para resetar todos os visitados da matriz.
    private static void resetVisited(){
        for (int i = 0; i < allRows; i++) {
            for (int j = 0; j < allColumn; j++) {
                allVertexs[i][j].visited = false;
            }
        }
    }

    private static void bfs(Vertex[][] vertex, int[] start) {
        // retira o primeiro elemento da fila.
        Queue<int[]> queue = new ArrayDeque<>();
        queue.offer(start);

        // enquanto a fila não estiver vazia, faça:
        while (!queue.isEmpty()) {
            int[] curr = queue.poll();
            int x = curr[0];
            int y = curr[1];
        
            // marca como visitado
            vertex[x][y].visited = true;

            // se o elemento da matriz for igual ao número que queremos chegar:
            if (Character.getNumericValue(vertex[x][y].value) == number) {
                number++; // incrementa a variavel que serve para acharmos os numeros;
                recursiveParent(vertex[x][y]); // verifica a quantidade de combustivel gasta;
                // se o numero for igual a 2, acabou o programa.
                if (number == 2) {
                    System.out.println("Total de combustível: " + sumFuel); 
                    break;
                }
                vertex[x][y].parent = null; // coloca o elemento atual com o pai == null;
                value = Character.getNumericValue(vertex[x][y].value); // armazena o valor dele
                coordinateLastNumber = new int[]{x, y}; // armazena a coordenada dele
                resetVisited(); // reseta os visitados
                queue.clear(); // reseta a fila

                // caso o numero seja maior que 9, pega-se as coordenadas do primeiro elemento (1)
                // e realiza a bfs para encontrá-lo a partir do último elemento da matriz.
                if (number > 9) {
                    int newX = initialNumber[0];
                    int newY = initialNumber[1];
                    vertex[newX][newY].parent = null; 
                    number = 1;
                    bfs(vertex, coordinateLastNumber);
                    break;
                }
            }

            // para cada aresta desse vértice, adiciona na lista.
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

            // se a fila estiver vazia, significa que não foi possível encontrar o elemento
            // ou seja, deve chamar de novo a bfs a partir do ultimo elemento encontrado.
            if (queue.isEmpty() && number != 10){
                if (number == 2) {
                    if (sumFuel != 0){
                        System.out.println("Total de combustível: " + sumFuel); 
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
    }
}
