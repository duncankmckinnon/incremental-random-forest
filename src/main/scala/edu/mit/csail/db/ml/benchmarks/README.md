# Setup

In the examples below, you'll see an argument called `<spark_master>`. This is the URL of the cluster.

If you want to use the embedded Spark, you should use something like `local[4]`.

If you want to deploy a cluster on your local machine, navigate to your Spark installation and run

```
./sbin/start-all.sh
```

This will deploy a Spark master and worker on your machine. Now, navigate your browser to 
[http://localhost:8080](http://localhost:8080) and note the URL at the top. It should look something
like:

```
spark://<host>:7077
```

To deploy the benchmark to the master on the local machine, use the URL above for `<spark_master>`.


# What's Cooking
## Description
This is a Kaggle competition in which competitors are asked to determine the type of cuisine (e.g.
Italian, Greek) based on the ingredients in the dish (e.g. tomatoes, shredded parmesan).

## Data
Download the data from the [Kaggle competition URL](https://www.kaggle.com/c/whats-cooking).

## Preprocessing
Before you can run the benchmark, you need to pre-process the data using the 
`scripts/preprocess_whats_cooking.js` script first (remember to use `npm install` to install 
the dependencies first).

This script will put each JavaScript object on a single line, allow subsampling, and allow selecting
a subset of the cuisine types.

## Benchmark
To run the benchmark, first preprocess the data (if you are doing two class classification, then
remember to pick a subset of the cusines).

Now, run

```
sbt clean && sbt assembly
```

Then, you can run the binary benchmark to train a Logistic Regression model for binary classification
and specify whether to use Wahoo or Spark classes:

```
spark-submit --master <spark_master> --class "edu.mit.csail.db.ml.benchmarks.whatscooking.Binary" target/scala-2.11/ml.jar <path_to_preprocessed_whats_cooking_json> <wahoo OR spark>
```

You can also train a multiclass classifier (a one vs. rest classifier based on Logistic Regression).

```
spark-submit --master <spark_master> --class "edu.mit.csail.db.ml.benchmarks.whatscooking.Multiclass" target/scala-2.11/ml.jar <path_to_preprocessed_whats_cooking_json> <wahoo OR spark>
```

# MNIST
This is a Kaggle competition in which competitors are asked to classify pictures of handwritten digits
(e.g. picture of "1" -> 1).

## Data
Download the data from the [Kaggle competition URL](https://www.kaggle.com/c/digit-recognizer).

## Preprocessing
No preprocessing is required.

## Benchmark
First run

```
sbt clean && sbt assembly
```

Then, run

```
spark-submit --master <spark_master> --class "edu.mit.csail.db.ml.benchmarks.mnist.Classifier" target/scala-2.11/ml.jar <path_to_mnist_csv> <wahoo|spark>
```

# Census
This is a UCI dataset in which we use demographic data to predict whether a person makes more or 
less than $50K per year.

## Data
Download this data from the [UCI Machine Learning Repository](http://archive.ics.uci.edu/ml/datasets/Census+Income).

We use the `adult.data` file.

## Preprocessing
No preprocessing is required.

## Benchmark
First run

```
sbt clean && sbt assembly
```

Then run

```
spark-submit --master <spark_master> --class "edu.mit.csail.db.ml.benchmarks.census.Forest" target/scala-2.11/ml.jar <path_to_adult.data>
```

**NOTE:** This does not support Wahoo classes yet.