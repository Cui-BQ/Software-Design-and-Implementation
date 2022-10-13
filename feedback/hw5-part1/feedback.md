### Written Answers: 16/20
- for IntQueue2's AF, it should be % not /. Tkae the 1st element from the front. That would be at q.front/q.entries.length, which could very well be 0. Instead, it should be The abstraction function for `IntQueue2` ought to be:
```
AF(this) = [..., entries[(i + front) % entries.length], ...]
           for front <= i < size
```

- For IntQueue2, `entries` is an array of primitive ints. Because of this, it can't actually store null values, so that doesn't need to be in your rep invariant. Additionally, `front` and `size` must be greater than or equal to 0.
- for 3e), it could expose because iterators have a remove method. It's not just because it refers directly to teh data.
- the JUnit tests should still be unit tests, so each one should still focus on a small detail. Additionally, what do you mean by the difference between these? If script tests are testing 0,1,2, then what are the JUnit tests testing?
### Design: 2/3

### Documentation & Specification (including JavaDoc): 3/3

### Testing (test suite quality & implementation): 2/3

### Code quality (code stubs/skeletons only, nothing else): 3/3

### Mechanics: 3/3

#### Overall Feedback

Good start on designing your first ADT. Make sure to read my feedback for various things to think about/potentially change as you move forward with implementation.

#### More Details
- you should clarify in your `Graph` class comment whether the graphs are mutable and what exactly edges are: they are directed, go from a parent to a child, and contain a label. this helps clear it up for the client.
- the convention for Java classes is to use capital letters, so your `graph` should be `Graph`. same thing applies to your nodes and edges.
- what does `addEdge` do if `parentNode == null` or `childNode == null`? This should be specified as well.
- your graph should not be returning a string to list the nodes. if a client wanted to use this, they would need to parse the string to get the actual individual node data. additionally, your graph should not need to sort things in alphabetical order. if a client wants this, they will do this themselves. what seems to be happening is that you are designing your graph ADT exclusively for the test driver. but the graph should be usable for all potential clients. Many other clients will instead just want an array, list, or set of strings to represent the nodes, rather than one long String. The script test driver can then get that list of nodes, and the test driver should be the one to sort them and print them out in the correct order. The graph should not be doing this. The same thing applies for your `ListChildren` method.
- what does `ListChildren` do if `parentNode == null`? this should be specified
- I think your `contains` method spec has a typo. You want to throw an exception if the node **is** null, not !=.
- with your current design, why does the client need access to this edge class? none of your methods take edges as a parameter and none return edges, so what use might it be for the clients?
- you should stay consistent in throwing exceptions if things are null. In your node/edge class, you use spec.requires. This might be confusing for a client if they have come to expect an exception to be thrown in these cases.
- what does "return true the edge's label" mean for `getLabel`? Same thing applies for `getChildNode`.
- your tests should be unit tests that test things of varying complexity. For example, one test might just test adding a single node (and then listing it). Another might test adding multiple nodes and then listing them. You should not just have a single tests to test addNodes. Additionally, you should test some special cases of the graph. For example, test that a cycle is allowed in your graph, or a self-edge. Or maybe test that your test driver actually prints in proper alphabetical order.
- in your junit tests, you should be testing that your IllegalArgumentExceptions are actually being thrown.