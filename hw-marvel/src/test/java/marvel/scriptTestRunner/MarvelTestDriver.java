/*
 * Copyright (C) 2021 Hal Perkins.  All rights reserved.  Permission is
 * hereby granted to students registered for University of Washington
 * CSE 331 for use solely during Winter Quarter 2021 for purposes of
 * the course.  No other use, copying, distribution, or modification
 * is permitted without prior written consent. Copyrights for
 * third-party components of this work must be honored.  Instructors
 * interested in reusing these course materials should contact the
 * author.
 */

package marvel.scriptTestRunner;

import graph.Graph;
import marvel.MarvelParser;
import marvel.MarvelPaths;

import java.io.*;
import java.util.*;

/**
 * This class implements a testing driver which reads test scripts from
 * files for testing Graph, the Marvel parser, and your BFS algorithm.
 */
public class MarvelTestDriver {

    private final PrintWriter output;
    private final BufferedReader input;
    private final Map<String, Graph<String, String>> graphs = new HashMap<String, Graph<String, String>>();

    // Leave this constructor public
    public MarvelTestDriver(Reader r, Writer w) {
        // TODO: Implement this, reading commands from `r` and writing output to `w`.
        input = new BufferedReader(r);
        output = new PrintWriter(w);
    }

    // Leave this method public
    public void runTests() throws IOException {
        // TODO: Implement this.
        String inputLine;
        while ((inputLine = input.readLine()) != null) {
            if ((inputLine.trim().length() == 0) ||
                    (inputLine.charAt(0) == '#')) {
                // echo blank and comment lines
                output.println(inputLine);
            } else {
                // separate the input line on white space
                StringTokenizer st = new StringTokenizer(inputLine);
                if (st.hasMoreTokens()) {
                    String command = st.nextToken();

                    List<String> arguments = new ArrayList<String>();
                    while (st.hasMoreTokens()) {
                        arguments.add(st.nextToken());
                    }

                    executeCommand(command, arguments);
                }
            }
            output.flush();
        }
    }

    private void executeCommand(String command, List<String> arguments) {
        try {
            switch (command) {
                case "LoadGraph":
                    loadGraph(arguments);
                    break;
                case "FindPath":
                    findPath(arguments);
                    break;
                case "AddNode":
                    addNode(arguments);
                    break;
                case "AddEdge":
                    addEdge(arguments);
                    break;
                case "ListChildren":
                    listChildren(arguments);
                    break;
                case "CreateGraph":
                    createGraph(arguments);
                    break;
                default:
                    output.println("Unrecognized command: " + command);
                    break;
            }
        } catch (Exception e) {
            output.println("Exception: " + e.toString());
        }
    }

    private void loadGraph(List<String> arguments) {
        if (arguments.size() != 2) {
            throw new CommandException("Bad arguments to loadGraph: " + arguments);
        }

        String graphName = arguments.get(0);
        String fileName = arguments.get(1);
        loadGraph(graphName, fileName);
    }

    private void loadGraph(String graphName, String fileName) {
        graphs.put(graphName, MarvelParser.parseData(fileName));
        output.println("loaded graph " + graphName);
    }

    private void findPath(List<String> arguments) {
        if (arguments.size() != 3) {
            throw new CommandException("Bad arguments to findGraph: " + arguments);
        }

        String graphName = arguments.get(0);
        String startHero = arguments.get(1).replace("_", " ");
        String destHero = arguments.get(2).replace("_", " ");
        findPath(graphName, startHero, destHero);
    }

    private void findPath(String graphName, String startHero, String destHero) {
        try {
            List<Graph<String, String>.Edge> path = new MarvelPaths().findPath(startHero, destHero, graphs.get(graphName));
            output.println("path from " + startHero + " to " + destHero + ":");
            if (path != null && path.size() > 0){
                output.print(startHero + " to ");
                for (int i = 0; i < path.size(); i++){
                    output.println(path.get(i).getChildNodeData() + " via " + path.get(i).getLabel());
                    if(i != path.size()-1){
                        output.print(path.get(i).getChildNodeData() + " to ");
                    }
                }
            } else if (!startHero.equals(destHero)){
                output.println("no path found");
            }
        } catch (IllegalArgumentException e){
            if(e.toString().contains(startHero) && e.toString().contains(destHero)) {
                output.println("unknown character " + startHero);
                output.println("unknown character " + destHero);
            } else if (e.toString().contains(startHero)) {
                output.println("unknown character " + startHero);
            } else {
                output.println("unknown character " + destHero);
            }
        }
    }

    private void addNode(List<String> arguments) {
        if (arguments.size() != 2) {
            throw new CommandException("Bad arguments to AddNode: " + arguments);
        }

        String graphName = arguments.get(0);
        String nodeName = arguments.get(1);

        addNode(graphName, nodeName);
    }

    private void addNode(String graphName, String nodeName) {
        // TODO Insert your code here.
        Graph<String, String> current = graphs.get(graphName);
        if (nodeName.equals("null")) {
            current.addNode(null);
        } else {
            if (current.addNode(current.new Node(nodeName))){
                output.println("added node " + nodeName + " to " + graphName);
            } else {
                output.println("failed to add node " + nodeName + " to " + graphName);
            }
        }


    }

    private void addEdge(List<String> arguments) {
        if (arguments.size() != 4) {
            throw new CommandException("Bad arguments to AddEdge: " + arguments);
        }

        String graphName = arguments.get(0);
        String parentName = arguments.get(1);
        String childName = arguments.get(2);
        String edgeLabel = arguments.get(3);

        addEdge(graphName, parentName, childName, edgeLabel);
    }

    private void addEdge(String graphName, String parentName, String childName,
                         String edgeLabel) {
        // TODO Insert your code here.
        Graph<String, String> current = graphs.get(graphName);
        Graph<String, String>.Node parent = current.new Node(parentName);
        if (parent.getData().equals("null")) parent = null;
        Graph<String, String>.Node child = current.new Node(childName);
        if (child.getData().equals("null")) child = null;
        String label = edgeLabel;
        if (edgeLabel.equals("null")) label = null;
        if (current.addEdge(parent, child, label)) {
            output.println("added edge " + edgeLabel + " from " + parentName + " to " + childName + " in " + graphName);
        } else {
            output.println("failed to add edge " + edgeLabel + " from " + parentName + " to " + childName + " in " + graphName);
        }
    }

    private void listChildren(List<String> arguments) {
        if (arguments.size() != 2) {
            throw new CommandException("Bad arguments to ListChildren: " + arguments);
        }

        String graphName = arguments.get(0);
        String parentName = arguments.get(1);
        listChildren(graphName, parentName);
    }

    private void listChildren(String graphName, String parentName) {
        // TODO Insert your code here.
        Graph<String, String> current = graphs.get(graphName);
        Graph<String, String>.Node parent = current.new Node(parentName);
        if (parent.getData().equals("null")) parent = null;
        HashSet<Graph<String, String>.Edge> set = current.ListEdges(parent);
        ArrayList<String[]> list = new ArrayList<>();
        for (Graph<String, String>.Edge e : set){
            String [] arr = new String[2];
            arr[0] = e.getChildNodeData();
            arr[1] = e.getLabel();
            list.add(arr);
        }
        Collections.sort(list, new Comparator<String[]>() {
            public int compare(String[] a, String[] b) {
                if (a[0].compareTo(b[0]) == 0){
                    return a[1].compareTo(b[1]);
                }
                return a[0].compareTo(b[0]);
            }
        });
        output.print("the children of " + parentName + " in " + graphName + " are:");
        for (String[] n : list){
            output.print(" " + n[0] + "(" + n[1] + ")");
        }
        output.println();
    }

    private void createGraph(List<String> arguments) {
        if (arguments.size() != 1) {
            throw new CommandException("Bad arguments to CreateGraph: " + arguments);
        }

        String graphName = arguments.get(0);
        createGraph(graphName);
    }

    private void createGraph(String graphName) {
        // TODO Insert your code here.
        graphs.put(graphName, new Graph<String, String>());
        output.println("created graph " + graphName);
    }

    /**
     * This exception results when the input file cannot be parsed properly
     **/
    static class CommandException extends RuntimeException {

        public CommandException() {
            super();
        }

        public CommandException(String s) {
            super(s);
        }

        public static final long serialVersionUID = 3495;
    }

}
