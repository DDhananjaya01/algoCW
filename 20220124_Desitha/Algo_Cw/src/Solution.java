
import java.io.*;
import java.nio.file.Files;
import java.util.*;

public class Solution {
    // Static variables to represent the maze, possible movements, and start/end positions
    static char[][] maze;
    static int[] dR = {0, 0, 1, -1};
    static int[] dC = {1, -1, 0, 0};
    static int startR, startC, endR, endC;

    // Main method to read files from a directory and process each file
    public static void main(String[] args) throws IOException {
        // Directory path containing maze files
        String directoryPath = "C:\\Users\\USER\\Desktop\\20220124_Desitha\\20220124_Desitha\\benchmark_series";
        File directory = new File(directoryPath);
        File[] files = directory.listFiles((dir, name) -> name.endsWith(".txt")); // Filter to only get .txt files

        // Process each file in the directory
        if (files != null) {
            for (File file : files) {
                List<String> lines = Files.readAllLines(file.toPath());
                processFile(lines, file.getName());
            }
        } else {
            System.out.println("DIRECTORY NOT FOUND: " + directoryPath);
        }
    }

    // Method to process each file containing maze data
    private static void processFile(List<String> lines, String fileName) {
        // Initialize the maze array and find start and end positions
        maze = new char[lines.size()][lines.get(0).length()];
        for (int i = 0; i < lines.size(); i++) {
            maze[i] = lines.get(i).toCharArray();
            for (int j = 0; j < maze[i].length; j++) {
                if (maze[i][j] == 'S') {
                    startR = i;
                    startC = j;
                } else if (maze[i][j] == 'F') {
                    endR = i;
                    endC = j;
                }
            }
        }

        // Print start and end positions of the maze
        System.out.println("START: (" + (startR + 1) + ", " + (startC + 1) + ")");
        System.out.println("END: (" + (endR + 1) + ", " + (endC + 1) + ")");

        // Run BFS algorithm to find the shortest path and print the result
        Node endNode = bfs();
        if (endNode != null) {
            System.out.println("SHORTEST PATH LENGTH: " + endNode.distance);
            printPath(endNode, fileName);
        } else {
            System.out.println("NO PATH FOUND");
            System.out.println();
        }
    }

    // BFS algorithm to find the shortest path in the maze
    static Node bfs() {
        System.out.println("RUNNING IN BFS ALGORITHM...");
        Node[][] nodes = new Node[maze.length][maze[0].length];
        boolean[][] visited = new boolean[maze.length][maze[0].length];
        Queue<Node> queue = new LinkedList<>();
        Node start = new Node(startR, startC, 0, null, "");
        nodes[startR][startC] = start;
        queue.offer(start);

        // Traverse the maze using BFS
        while (!queue.isEmpty()) {
            Node curr = queue.poll();
            if (visited[curr.row][curr.column]) continue;
            visited[curr.row][curr.column] = true;

            // Explore adjacent cells and enqueue valid ones
            for (int i = 0; i < 4; i++) {
                int nx = curr.row + dR[i];
                int ny = curr.column + dC[i];
                String direction = "";
                if (i == 0) direction = "RIGHT";
                else if (i == 1) direction = "LEFT";
                else if (i == 2) direction = "DOWN";
                else if (i == 3) direction = "UP";

                // Check if the next cell is within bounds and not a wall
                if (nx >= 0 && nx < maze.length && ny >= 0 && ny < maze[0].length && maze[nx][ny] != '0') {
                    // Enqueue unvisited cells
                    if (nodes[nx][ny] == null || !visited[nx][ny]) {
                        Node next = new Node(nx, ny, curr.distance + 1, curr, direction);
                        nodes[nx][ny] = next;
                        queue.offer(next);
                    }
                }
            }

            // If reached the end position, return the node
            if (curr.row == endR && curr.column == endC) {
                return curr;
            }
        }
        return null;
    }

    // Method to print the path found in the maze
    static void printPath(Node end, String fileName) {
        System.out.println("\nPATH FOR " + fileName + ":");
        List<String> steps = new ArrayList<>();
        Node curr = end;
        while (curr != null) {
            // Add each step to the list
            if (curr.prev != null) {
                steps.add(". MOVE " + curr.move + " TO (" + (curr.row + 1) + "," + (curr.column + 1) + ")");
            } else {
                steps.add(". START AT (" + (curr.row + 1) + "," + (curr.column + 1) + ")");
            }
            curr = curr.prev;
        }

        // Reverse the list and print the path
        Collections.reverse(steps);
        for (int i = 0; i < steps.size(); i++) {
            System.out.println((i + 1) + steps.get(i));
        }
        System.out.println((steps.size() + 1) + ". DONE!");
        System.out.println();
    }

    // Node class representing a cell in the maze
    static class Node {
        int row, column;
        int distance;
        Node prev;
        String move;

        // Constructor to initialize node attributes
        Node(int x, int y, int distance, Node prev, String move) {
            this.row = x;
            this.column = y;
            this.distance = distance;
            this.prev = prev;
            this.move = move;
        }
    }
}
