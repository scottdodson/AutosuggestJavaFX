package com.nonstick.autosuggestjavafx;

import java.util.*;

public class DirectedGraph {
    // initial structure to suggest 'next' word
    HashMap<String, Node>  nodes;
    int vertices;
    private static class Node {
        private final String label;
        private HashMap<String, Integer> edges;

        public Node(String label) {
            this.label = label;
            this.edges = new HashMap<>();
        }

        public void addEdge(String destination) {
            if (!edges.containsKey(destination)) {
                edges.put(destination, Integer.MAX_VALUE);
            } else {
                edges.put(destination, edges.get(destination) - 1);
            }
        }
    }

    public DirectedGraph() {
        nodes = new HashMap<>();
        vertices = 0;
    }

    public void addVertex(String word) {
        if (!nodes.containsKey(word)) {
            Node newNode = new Node(word);
            nodes.put(word, newNode);
            vertices++;
        }
    }

    public void addEdge(String source, String destination) {
        Node sourceNode;
        if(nodes.containsKey(source)) {
            sourceNode = nodes.get(source);
            sourceNode.addEdge(destination);
            if(!nodes.containsKey(destination)) {
                addVertex(destination);
            }
        } else {
            addVertex(source);
            nodes.get(source).addEdge(destination);
            if(!nodes.containsKey(destination)) {
                addVertex(destination);
            }
        }
    }

    public boolean isEdge(String source, String destination) {
        return (nodes.containsKey(source) && nodes.get(source).edges.containsKey(destination));
    }

    public int getEdgeWeight(String source, String destination) {
        if(nodes.containsKey(source) && nodes.get(source).edges.containsKey(destination)) {
            return nodes.get(source).edges.get(destination);
        }
        return -1;
    }

    public int size() {
        return vertices;
    }

    public boolean isEmpty() {
        return vertices == 0;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (HashMap.Entry<String, Node> entry : nodes.entrySet()) {
            sb.append(entry.getKey() + ": " + entry.getValue().edges.toString() + "\n");
        }
        return sb.toString();
    }

    public ArrayList<String> bfs(String source)  {
        ArrayDeque<String> queue = new ArrayDeque<>();
        ArrayList<String> answer = new ArrayList<>();
        Set<String> visitedSet = new HashSet<>();
        queue.add(source);
        if (!visitedSet.contains(source)) {
            visitedSet.add(source);
        } else {
            System.out.println("tried adding to set but already visited");
        }

        while (!queue.isEmpty()) {
            String rolling = queue.poll();
            answer.add(rolling);
            for (String dest : nodes.get(rolling).edges.keySet()) {
                if (!visitedSet.contains(dest)) {
                    queue.add(dest);
                    visitedSet.add(rolling);
                }
            }
        }
        return answer;
    }

    public static void main(String[] args) {
        DirectedGraph dg = new DirectedGraph();
        dg.addEdge("foo", "bar");
        dg.addEdge("foo", "rob");
        dg.addEdge("bar", "fizz");
        dg.addEdge("rob", "fizz");
        System.out.println(dg.size());
        System.out.println(dg.isEmpty());
        System.out.println(dg.isEdge("cat", "is"));
        System.out.println(dg.getEdgeWeight("cat", "is"));
        dg.addEdge("cat", "is");
        System.out.println(dg.getEdgeWeight("cat", "is"));
        System.out.println(dg.toString());
        //System.out.println(dg.bfs("foo"));
    }
}
