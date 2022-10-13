### Written Answers: 6/6

### Code Quality: 3/3

### Mechanics: 3/3

### General Feedback
Overall great work! Make sure to take a look at some of the small pieces of feedback
I left for you down below. Great job practicing defensive programming and throwing exceptions
when the paramaters do not meet the precondition! Keep up the good work!

### Specific Feedback
- When selecting a greeting in `RandomHello`, the best style would use the length
of the array to specify the maximum value for the random integer generation:
```
String nextGreeting = greetings[rand.nextInt(greetings.length)];
```
Notice how this benefits us later on if we wanted to change the number of
possible greetings in the array.

- For the size() function in in BallContainer, instead of iterating through each element
in "this", it would've been cleaner to simply call contents.size().

- In getBallsFromSmallest() in Box.java, the "temp" LinkedHashSet that you create is not 
necessary since you could have simply added the elements directly into the "sort" list
that you created.

- Missing documentation for the new fields in `Box.java`.
Make sure to document new additions in the future!

