# PersoniFiler
A technique using the filesystem to compare the collaborative behaviors of people with the groups that they actually belong to
Research website: https://sites.google.com/site/clusteringpeople/home

![picture alt](http://i.imgur.com/F4kuIap.png "Graph of cluster vs. ground truth")

This project was started to analyze corporate placements of employees. 
Did the group placements of staff members accurately reflect which other staff members they have been working with?
Was it a good thing if a person collaborated on projects with people from other groups moreso than with his own group?

PersoniFiler analyzes the shared file systems, retrieves the owners of every single file, clusters the results,
and compares them to the ground truth (the corporate chart, the actual group placements).

The general approach is:
* Traverse the file system and obtain the owners of each file
* Generate a "feature matrix", a matrix similar to an adjacency matrix representing the degree to which each person is connected with another person
* Using each row of the feature matrix as vectors, cluster the vectors and place each file owner into groups
* Obtain the ground truth groups of the file owners
* Develop/Use an evaluation metric to compare the results of PersoniFiler's clustering with the ground truth groups
* Use a graph to visually analyze the differences and the effectiveness of the evaluation metric
