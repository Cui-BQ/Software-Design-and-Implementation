### Written Answers: 20/26

#### Polynomial Division

- The loop in polynomial division needs to terminate when p becomes 0.  If you
don't have this condition in the loop, then an infinite loop can occur when the
degree of the divisor is 0, since the degree of p = 0 is also 0.

- It appears you are using polynomial subtraction. Note, we don't have
a method to do this. Can you maybe use addition in someway to achieve this?

#### Representation Invariants

- In part 2c, the changes in the following methods are required: the constructor,
and `checkRep`.

#### `checkRep`

- Immutability is a property of the specification, and `checkRep` does not assume
the specification was correctly implemented.  So, in general, regardless of
whether or not they are immutable, ADTs need calls to `checkRep` at the
beginning and end of all public methods.

### Code Quality: 3/3

### Mechanics: 3/3

### General Feedback
- Great Job!

- Please remove TODO after implementing the methods.

### Specific Feedback
- Your RatPoly.div() appears to be more complicated than it should be. Try storing
values to enhance readability.