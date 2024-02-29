## About
The project was developed for the [Neural Networks](https://www.cse.uoi.gr/course/computational-intelligence/?lang=en) course @ [cse.uoi.gr](https://www.cs.uoi.gr/)

Question 1 is about classifying 4000 points using mini-batch gradient descent algorithms on a 2-layer MLP network.

Question 2 is about clustering 1200 points using the k-means algorithm.

## Question 1 Instructions
### Instructions for Windows Platforms
1. Open terminal inside the directory Neural Networks - Question 1

2. Compile with: ```javac -cp ".;jars/*" main/*.java datasetgenerator/*.java neurons/*.java transferfunctions/*.java```

3. (optional) Create a new dataset with: ```java -cp ".;jars/*" main/DatasetGenerator```

4. Run java application with:
```
java -cp ".;jars/*" main/MLPDriver <hidden layers> <h1 neurons> <h2 neurons> <transfer function> <learning rate> <batch size>
example: java -cp ".;jars/*" main/MLPDriver 2 4 3 tanh 0.05 1

java -cp ".;jars/*" main/MLPDriver <hidden layers> <h1 neurons> <h2 neurons> <h3 neurons> <transfer function> <learning rate> <batch size>
example: java -cp ".;jars/*" main/MLPDriver 3 4 3 2 tanh 0.05 1
```
### Instructions for Linux Platforms
1. Open terminal inside the directory Neural Networks - Question 1

2. Compile with: ```javac -cp ".:jars/*" main/*.java datasetgenerator/*.java neurons/*.java transferfunctions/*.java```

3. (optional) Create a new dataset with: ```java -cp ".:jars/*" main/DatasetGenerator```

4. Run java application with:
```
java -cp ".:jars/*" main/MLPDriver <hidden layers> <h1 neurons> <h2 neurons> <transfer function> <learning rate> <batch size>
example: java -cp ".:jars/*" main/MLPDriver 2 4 3 tanh 0.05 1

java -cp ".:jars/*" main/MLPDriver <hidden layers> <h1 neurons> <h2 neurons> <h3 neurons> <transfer function> <learning rate> <batch size>
example: java -cp ".:jars/*" main/MLPDriver 3 4 3 2 tanh 0.05 1
```
### Instructions for Eclipse IDE:
Run Configurations -> Arguments -> Program Arguments:
```
<hidden layers> <h1 neurons> <h2 neurons> <transfer function> <learning rate> <batch size>
example: 2 4 3 tanh 0.05 1

<hidden layers> <h1 neurons> <h2 neurons> <h3neurons> <transfer function> <learning rate> <batch size>
example: 3 4 3 2 tanh 0.05 1
```
Keep the learning rate at 0.05 (Google defaults it at 0.03)

## Question 2 Instructions
### Instructions for Windows Platforms

1. Open terminal inside the directory Neural Networks - Question 2

2. Compile with: ```javac -cp ".;jars/*" main/*.java```

3. (optional) Create a new dataset with: ```java -cp ".;jars/*" main/DatasetGenerator```

4. Run java application with:
```
java -cp ".;jars/*" main/Kmeans <dataset_name.txt> <number_of_clusters> <number_of_runs>
example: java -cp ".;jars/*" main/Kmeans dataset.txt 9 20
```

### Instructions for Linux Platforms

1. Open terminal inside the directory Neural Networks - Question 2

2. Compile with: ```javac -cp ".:jars/*" main/*.java```

3. (optional) Create a new dataset with: ```java -cp ".:jars/*" main/DatasetGenerator```

4. Run java application with:
```
java -cp ".:jars/*" main/Kmeans <dataset_name.txt> <number_of_clusters> <number_of_runs>
example: java -cp ".:jars/*" main/Kmeans dataset.txt 9 20
```
