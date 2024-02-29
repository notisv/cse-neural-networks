Instructions for Windows Platforms

1. Open terminal inside the directory Neural Networks - Question 1

2. Compile with: javac -cp ".;jars/*" main/*.java datasetgenerator/*.java neurons/*.java transferfunctions/*.java

3. Create a new dataset with: java -cp ".;jars/*" main/DatasetGenerator - This is not necessary

4. Run java application with:
         java -cp ".;jars/*" main/MLPDriver <hidden layers> <h1 neurons> <h2 neurons> <transfer function> <learning rate> <batch size>
example: java -cp ".;jars/*" main/MLPDriver 2 4 3 tanh 0.05 1

         java -cp ".;jars/*" main/MLPDriver <hidden layers> <h1 neurons> <h2 neurons> <h3 neurons> <transfer function> <learning rate> <batch size>
example: java -cp ".;jars/*" main/MLPDriver 3 4 3 2 tanh 0.05 1

Instructions for Linux Platforms

1. Open terminal inside the directory Neural Networks - Question 1

2. Compile with: javac -cp ".:jars/*" main/*.java datasetgenerator/*.java neurons/*.java transferfunctions/*.java

3. Create a new dataset with: java -cp ".:jars/*" main/DatasetGenerator - This is not necessary

4. Run java application with:
         java -cp ".:jars/*" main/MLPDriver <hidden layers> <h1 neurons> <h2 neurons> <transfer function> <learning rate> <batch size>
example: java -cp ".:jars/*" main/MLPDriver 2 4 3 tanh 0.05 1

         java -cp ".:jars/*" main/MLPDriver <hidden layers> <h1 neurons> <h2 neurons> <h3 neurons> <transfer function> <learning rate> <batch size>
example: java -cp ".:jars/*" main/MLPDriver 3 4 3 2 tanh 0.05 1

Instructions for Eclipse IDE:

Run Configurations -> Arguments -> Program Arguments:

<hidden layers> <h1 neurons> <h2 neurons> <transfer function> <learning rate> <batch size>
example: 2 4 3 tanh 0.05 1

<hidden layers> <h1 neurons> <h2 neurons> <h3neurons> <transfer function> <learning rate> <batch size>
example: 3 4 3 2 tanh 0.05 1

Keep the learning rate at 0.05 (Google defaults it at 0.03)