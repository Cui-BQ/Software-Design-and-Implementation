### Command line application: 3/3

### Design: 2/3

### Documentation & Specification (including JavaDoc): 2/3

### Code quality (code and internal comments including RI/AF when appropriate): 2/3

### Testing (test suite quality & implementation): 2/3

### Mechanics: 3/3

### Command line application extra credit:  -/3

#### Overall Feedback

- Good work overall.

#### More Details

- The `MarvelParser.parseData` method ought to return an object with the least
amount of extra processing done on top of the original data.  This design allows
you to reuse the method in other applications, where the intention is not
necessarily to use the data in a graph that correlates superheroes.
- Your main method for MarvelPaths should have some documentation explaining the functionality of the command line application.
- For next time, if you implement a class that isn't an ADT, you should add an internal comment that explicitly says so where the ADT/RI would normally go.
- Your test suite is lacking in coverage.  Here's a few ideas for interesting test cases:
- Empty data file.
- Loading two graphs.
- Operations that mix graph loading and building.
- Cyclic graphs with path finding.
- Lexicographically least paths.