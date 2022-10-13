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

package marvel;



import com.opencsv.bean.CsvToBeanBuilder;
import graph.Graph;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

/**
 * Parser utility to load the Marvel Comics dataset.
 */
public class MarvelParser {


    /**
     * Reads the Marvel Universe dataset. Each line of the input file contains a character name and a
     * comic book the character appeared in, separated by a tab character.
     * Then return characters and their relations by using a graph.
     *
     * @param filename the file that will be read
     * @spec.requires filename is a valid file in the resources/data folder.
     * @return a Graph that contains all heroes as nodes, and their relations as edges.
     */
    // TODO: Replace 'void' with the type you want the parser to produce
    public static Graph<String, String> parseData(String filename) {
        // You can use this code as an example for getting a file from the resources folder
        // in a project like this. If you access TSV files elsewhere in your code, you'll need
        // to use similar code. If you use this code elsewhere, don't forget:
        //   - Replace 'MarvelParser' in `MarvelParser.class' with the name of the class you write this in
        //   - If the class is in src/main, it'll get resources from src/main/resources
        //   - If the class is in src/test, it'll get resources from src/test/resources
        //   - The "/" at the beginning of the path is important
        // Note: Most students won't re-write this code anywhere, this explanation is just for completeness.
        InputStream stream = MarvelParser.class.getResourceAsStream("/data/" + filename);
        if (stream == null) {
            // stream is null if the file doesn't exist.
            // You'll probably want to handle this case so you don't try to call
            // getPath and have a null pointer exception.
            // Technically, you'd be allowed to just have the NPE because of
            // the @spec.requires, but it's good to program defensively. :)
            throw new IllegalArgumentException("provided an invalid file name");
        }
        Reader reader = new BufferedReader(new InputStreamReader(stream));

        // TODO: Complete this method
        // Hint: You might want to create a new bean class to use with the OpenCSV Parser.
        Iterator<Model> tsvIter =
                new CsvToBeanBuilder<Model>(reader) // set input
                        .withType(Model.class) // set entry type
                        .withSeparator('\t') // , for CSV
                        .withIgnoreLeadingWhiteSpace(true)
                        .build() // returns a CsvToBean<Model>
                        .iterator();

        Graph<String, String> graph = new Graph<String, String>();
        HashMap<String, HashSet<String>> res = new HashMap<String, HashSet<String>>();
        while (tsvIter.hasNext()){
            Model current = tsvIter.next();
            if(!res.containsKey(current.getBook())){ // res doesn't contain this book
                res.put(current.getBook(), new HashSet<String>());
            }
            // Now, res must contain this book, add the hero associate with the book.
            res.get(current.getBook()).add(current.getHero());
            // Add the hero to the graph.
            graph.addNode(graph.new Node(current.getHero()));
        }

        // Insert edges
        res.forEach((key,value)->{
            for(String parentHero : value){
                for(String childHero : value){
                    if(!parentHero.equals(childHero)){
                        graph.addEdge(graph.new Node(parentHero), graph.new Node(childHero), key);
                    }
                }
            }
        });

        return graph;
    }
}
