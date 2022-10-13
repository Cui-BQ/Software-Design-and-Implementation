package marvel.junitTests;

import marvel.MarvelParser;
import marvel.MarvelPaths;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import graph.*;

import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.*;

/**
 * This class contains a junitTest case that can be used to test the implementation of the
 * MarvelParser and MarvePaths classes.
 */
public class MarvelPathTest {

    @Rule
    public Timeout globalTimeout = Timeout.seconds(10); // 10 seconds max per method tested

    @Test
    public void MarvelParserTest () {
        Graph<String, String> myTest = MarvelParser.parseData("myTest.tsv");
        assertTrue(myTest.contains(myTest.new Node("A")));
        assertTrue(myTest.contains(myTest.new Node("B")));
        assertTrue(myTest.contains(myTest.new Node("C")));
        assertTrue(myTest.contains(myTest.new Node("D")));
        assertTrue(myTest.contains(myTest.new Node("E")));
        assertTrue(myTest.contains(myTest.new Node("F")));
        assertTrue(myTest.contains(myTest.new Node("G")));
        assertTrue(myTest.contains(myTest.new Node("X")));

        Graph<String, String> staffSuperheroes = MarvelParser.parseData("staffSuperheroes.tsv");
        HashSet<Graph<String, String>.Edge> shouldBe = new HashSet<>();
        shouldBe.add(staffSuperheroes.new Edge("CSE331",
                staffSuperheroes.new Node("Ernst-the-Bicycling-Wizard")));
        shouldBe.add(staffSuperheroes.new Edge("CSE331",
                staffSuperheroes.new Node("Notkin-of-the-Superhuman-Beard")));
        shouldBe.add(staffSuperheroes.new Edge("CSE331",
                staffSuperheroes.new Node("Grossman-the-Youngest-of-them-all")));
        assertTrue(staffSuperheroes.ListEdges(staffSuperheroes.new Node("Perkins-the-Magical-Singing-Instructor")).containsAll(shouldBe));
    }

    @Test
    public void MarvelPathsTest () {
        MarvelPaths mp = new MarvelPaths();
        // Start and end are the same, so no intermediate paths.
        assertTrue(mp.findPath("Ernst-the-Bicycling-Wizard", "Ernst-the-Bicycling-Wizard",
                MarvelParser.parseData("staffSuperheroes.tsv")).isEmpty());
    }

}
