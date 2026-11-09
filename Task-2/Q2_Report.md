## Conclusion

The experiment compared ArrayList, LinkedList, and ArrayDeque for different playlist operations using three playlist sizes: 1,000, 10,000, and 100,000 elements. The observed results indicate that ArrayList provides efficient indexed access because it uses a dynamic array, while LinkedList performs better for insertion and removal at the beginning of the list because it updates node references without shifting existing elements.

As the playlist size increased, the performance difference between ArrayList and LinkedList became more significant. At 100,000 elements, ArrayList took 229.373 ms for front insertion, whereas LinkedList took only 2.039 ms. Similarly, for 1,000 indexed accesses, ArrayList took 0.350 ms compared with 67.597 ms for LinkedList.

ArrayDeque was also evaluated for operations at both ends. It is an appropriate choice for an Up Next queue because it supports efficient insertion and removal at both ends without requiring indexed access.

Therefore, ArrayList is recommended for the main music playlist, while ArrayDeque is recommended for the Up Next queue. LinkedList can be considered when frequent end operations or operations at already-known nodes are required. The experiment demonstrates that collection selection should depend on the application's operation patterns rather than simply choosing one data structure for every situation.
