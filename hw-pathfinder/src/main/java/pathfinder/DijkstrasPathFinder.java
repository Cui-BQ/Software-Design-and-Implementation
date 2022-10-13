package pathfinder;

import graph.Graph;
import pathfinder.datastructures.Path;

import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;


/**
 * This class uses Dijkstra's Algorithm to find a minimum-cost path
 * between two given nodes in a graph with all non-negative edge weights.
 */
public class DijkstrasPathFinder {

    /**
     * @param start the start location of the path
     * @param dest the destination location of the path
     * @param graph the graph that will be used to find path
     * @param <N> DataType of location
     * @throws IllegalArgumentException if graph == null, or start or dest doesn't exist in the graph.
     * @return the shortest path between start and dest, return null if no path between them.
     */
    public static <N> Path<N> DijkstraPathFinder (N start, N dest, Graph<N, Double> graph){
        if (graph == null){
            throw new IllegalArgumentException("graph == null");
        }
        if (!graph.contains(graph.new Node(start)) && !graph.contains(graph.new Node(dest))){
            throw new IllegalArgumentException("graph doesn't contain " + start + " and " + dest);
        } else if (!graph.contains(graph.new Node(start))) {
            throw new IllegalArgumentException("graph doesn't contain " + start);
        } else if (!graph.contains(graph.new Node(dest))){
            throw new IllegalArgumentException("graph doesn't contain " + dest);
        }
        PriorityQueue<Path<N>> active = new PriorityQueue<>(Comparator.comparing(Path::getCost));
        Set<N> finished = new HashSet<>();
        active.add(new Path<>(start));

        while(!active.isEmpty()){
            Path<N> minPath = active.remove();
            N minDest = minPath.getEnd();
            if(minDest.equals(dest)){
                return minPath;
            }
            if(finished.contains(minDest)) {
                continue;
            }
            for (Graph<N, Double>.Edge e : graph.ListEdges(graph.new Node(minDest))) {
                if (!finished.contains(e.getChildNodeData())) {
                    Path<N> newPath = minPath.extend(e.getChildNodeData(), e.getLabel());
                    active.add(newPath);
                }
            }
            finished.add(minDest);
        }
        return null;
    }
}
