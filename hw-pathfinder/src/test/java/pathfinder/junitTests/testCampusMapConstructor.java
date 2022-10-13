package pathfinder.junitTests;

import org.junit.Test;
import pathfinder.CampusMap;
import pathfinder.parser.CampusBuilding;
import pathfinder.parser.CampusPathsParser;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class testCampusMapConstructor {
    private static final CampusMap map = new CampusMap();

    @Test
    public void testBuildingList (){
        List<CampusBuilding> actualBuildings = CampusPathsParser.parseCampusBuildings("campus_buildings.tsv");
        assertEquals(map.buildingNames().size(), actualBuildings.size());
        for (CampusBuilding b : actualBuildings){
            assertTrue(map.buildingNames().get(b.getShortName()).equals(b.getLongName()));
        }
    }


}
