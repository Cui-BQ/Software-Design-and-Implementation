### Written Answers: 10/10

### Design: 3/3

### Documentation & Specification (including JavaDoc): 3/3

### Code quality (code and internal comments including RI/AF): 3/3

### Testing (test suite quality & implementation): 2/3

### Mechanics: 3/3

#### Overall Feedback

- Good job overall.

#### More Details

- Do not declare variables with types like `HashSet`.  Instead, just use the
more general type of `Set`.  This will allow you to later switch out the more
specific implementation in the variable assignment rather than having to mess
with the variable type as well.

- Prefer importing `Node` instead of directly using `Graph.Node`.

- In your junit tests, you should be testing that your IllegalArgumentExceptions are actually being thrown.

- Lack some edge cases such as cyclical graphs, ordering, etc.
