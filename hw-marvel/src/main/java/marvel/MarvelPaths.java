package marvel;

import graph.Graph;

import java.util.*;

public class MarvelPaths {


    public static void main (String[] args){
        Scanner sc = new Scanner(System.in);
        System.out.println("This's MarvePaths finder, please enter the file name. (E for exit)");
        String filename = sc.nextLine();
        if (filename.equals("E")) System.exit(0);
        System.out.println("The file name you entered is: " + filename + ", now trying to create graph......");
        Graph<String, String> graph;
        while(true){
            try {
                graph = MarvelParser.parseData(filename);
                break;
            } catch (Exception e) {
                System.out.println("Unable to create graph with this file: " + filename);
                System.out.println("Please enter a correct filename. (E for exit)");
                filename = sc.nextLine();
                if (filename.equals("E")) System.exit(0);
            }
        }
        MarvelPaths mp = new MarvelPaths();
        while(true){
            System.out.println("Please enter the start hero: ");
            String startHero = sc.nextLine();
            System.out.println("Please enter the Destination hero: ");
            String destHero = sc.nextLine();
            System.out.println("Searching the shortest path form " + startHero + " to " + destHero + "......");
            try {
                List<Graph<String, String>.Edge> path = mp.findPath(startHero, destHero, graph);
                System.out.println("path from " + startHero + " to " + destHero + ":");
                if (path != null && path.size() > 0){
                    System.out.print(startHero + " to ");
                    for (int i = 0; i < path.size(); i++){
                        System.out.println(path.get(i).getChildNodeData() + " via " + path.get(i).getLabel());
                        if(i != path.size()-1){
                            System.out.print(path.get(i).getChildNodeData() + " to ");
                        }
                    }
                } else if (!startHero.equals(destHero)){
                    System.out.println("no path found between " + startHero + " and " + destHero);
                }
            } catch (Exception e) {
                if (e.toString().contains(startHero)) {
                    System.out.println(startHero + " didn't exist in the graph");
                }
                if (e.toString().contains(destHero)) {
                    System.out.println(destHero + " didn't exist in the graph");
                }
            }
            System.out.println("Search another pair of heroes in this graph? (Y for yes, Anything else for exit)");
            String yes = sc.nextLine();
            if (!yes.equals("Y")) {
                System.exit(0);
            }
        }

    }


    /**
     * @param startHero the start of the searching path.
     * @param destHero the ending of the searching path.
     * @param graph finding path in this graph
     * @throws IllegalArgumentException if graph == null, or startHero or destHero isn't appearing in the passing graph.
     * @return a list of edges that contains the shortest path from start to dest.
     *         Otherwise return null (No path between startHero and destHero).
     */
    public List<Graph<String, String>.Edge> findPath(String startHero, String destHero, Graph<String, String> graph){
        if (graph == null){
            throw new IllegalArgumentException("graph == null");
        }
        if (!graph.contains(graph.new Node(startHero)) && !graph.contains(graph.new Node(destHero))){
            throw new IllegalArgumentException("graph doesn't contain " + startHero + " and " + destHero);
        } else if (!graph.contains(graph.new Node(startHero))) {
            throw new IllegalArgumentException("graph doesn't contain " + startHero);
        } else if (!graph.contains(graph.new Node(destHero))){
            throw new IllegalArgumentException("graph doesn't contain " + destHero);
        }
        Queue<String> queue = new LinkedList<>();
        HashMap<String, LinkedList<Graph<String, String>.Edge>> map = new HashMap<>();
        queue.add(startHero);
        map.put(startHero, new LinkedList<>());
        while (!queue.isEmpty()){
            Graph<String, String>.Node current = graph.new Node(queue.poll());
            if (current.getData().equals(destHero)){
                return map.get(destHero);
            }
            HashSet<Graph<String, String>.Edge> set = graph.ListEdges(current);
            ArrayList<String[]> edges = new ArrayList<>();
            for (Graph<String, String>.Edge e : set){
                String [] arr = new String[2];
                arr[0] = e.getChildNodeData();
                arr[1] = e.getLabel();
                edges.add(arr);
            }
            Collections.sort(edges, new Comparator<String[]>() {
                public int compare(String[] a, String[] b) {
                    if (a[0].compareTo(b[0]) == 0){
                        return a[1].compareTo(b[1]);
                    }
                    return a[0].compareTo(b[0]);
                }
            });
            for (String[] e : edges){
                if (!map.containsKey(e[0])){
                    LinkedList<Graph<String, String>.Edge> path = new LinkedList<>(map.get(current.getData()));
                    path.add(graph.new Edge(e[1], graph.new Node(e[0])));
                    map.put(e[0], path);
                    queue.add(e[0]);
                }
            }
        }
        return null;
    }
}
