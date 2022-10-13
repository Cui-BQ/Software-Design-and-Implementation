### Design: 3/3

### Documentation & Specification (including JavaDoc): 2/3

### Code quality (code and internal comments including RI/AF when appropriate): 3/3

### Testing (test suite quality & implementation): 3/3

### Mechanics: 3/3

#### Overall Feedback

Good work overall, be sure to address some of the documentation comments below.

#### More Details

- Should document generic types (ex: in your Graph class)

- You should explicitly document when a class is not an ADT

- Remember to remove TODO comments

- The graph building routine should be factored out into its own method.
  Otherwise, you cannot test the graph building on arbitrary data files or some
  mock parser.

- Your Dijkstra's implementation should have internal comments. Dijkstra's is
  not a trivial algorithm, so it's important to include documentation to improve
  readability and clarity for anyone who reads through the method

- Your CampusMap is an ADT but is lacking things becoming of one (RI, AF,
  checkRep)

- All fields should be documented (ex: your fields in CampusMap are lacking
  documentation)

- You don't test errors you throw (ex: your Dijkstra's implementation throws
  errors in many different conditions)

- As mentioned above, you should test graph building directly (not just the
  campusmap constructor - the graph building itself is important to test
  independentl)

- Your tests are ok otherwise, though it would be nice to see cyclic tests

- Script tests should ideally have top-level comments describing what they are
  doing
