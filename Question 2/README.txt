Instructions for Windows Platforms

1. Open terminal inside the directory Neural Networks - Question 2

2. Compile with: javac -cp ".;jars/*" main/*.java

3. Create a new dataset with: java -cp ".;jars/*" main/DatasetGenerator - This is not necessary

4. Run java application with: java -cp ".;jars/*" main/Kmeans <dataset_name.txt> <number_of_clusters> <number_of_runs>
                     example: java -cp ".;jars/*" main/Kmeans dataset.txt 9 20


Instructions for Linux Platforms

1. Open terminal inside the directory Neural Networks - Question 2

2. Compile with: javac -cp ".:jars/*" main/*.java

3. Create a new dataset with: java -cp ".:jars/*" main/DatasetGenerator - This is not necessary

4. Run java application with: java -cp ".:jars/*" main/Kmeans <dataset_name.txt> <number_of_clusters> <number_of_runs>
                     example: java -cp ".:jars/*" main/Kmeans dataset.txt 9 20
