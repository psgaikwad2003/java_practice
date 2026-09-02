import java.util.*;

public class DijkstraAlgorithmDemo {
    static class Node implements Comparable<Node> {
        int vertex, weight;
        Node(int v, int w) { vertex = v; weight = w; }
        public int compareTo(Node other) { return Integer.compare(this.weight, other.weight); }
    }

    public static void main(String[] args) {
        System.out.println("Dijkstra's Algorithm Implementation");
        int V = 5;
        List<List<Node>> adj = new ArrayList<>();
        for (int i = 0; i < V; i++) adj.add(new ArrayList<>());
        
        adj.get(0).add(new Node(1, 9));
        adj.get(0).add(new Node(2, 6));
        adj.get(0).add(new Node(3, 5));
        adj.get(0).add(new Node(4, 3));
        adj.get(2).add(new Node(1, 2));
        adj.get(2).add(new Node(3, 4));
        
        int[] dist = new int[V];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[0] = 0;
        
        PriorityQueue<Node> pq = new PriorityQueue<>();
        pq.add(new Node(0, 0));
        
        while (!pq.isEmpty()) {
            Node current = pq.poll();
            for (Node neighbor : adj.get(current.vertex)) {
                if (dist[current.vertex] + neighbor.weight < dist[neighbor.vertex]) {
                    dist[neighbor.vertex] = dist[current.vertex] + neighbor.weight;
                    pq.add(new Node(neighbor.vertex, dist[neighbor.vertex]));
                }
            }
        }
        
        for (int i = 0; i < V; i++) {
            System.out.println("Distance from 0 to " + i + " is " + dist[i]);
        }
    }
}
