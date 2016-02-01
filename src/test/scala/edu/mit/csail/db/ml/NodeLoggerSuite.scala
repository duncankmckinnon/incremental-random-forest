package org.apache.spark.ml

import org.apache.spark.mllib.linalg.{Vector, Vectors}
import org.apache.spark.sql.{SQLContext, DataFrame}
import org.scalatest.{FunSuite, BeforeAndAfter}


/**
 * This test will attempt to log messages to the central node server. Make sure you have the node
 * server running on http://localhost:3000
 */
class NodeLoggerSuite extends FunSuite with BeforeAndAfter {
  test("logging events to the central node.js server") {
    TestBase.withContext("wahootest")(false) { (wctx) =>
      wctx.wc.setWebAppUrl("http://localhost:3000")

      val training = TestBase.sqlContext.createDataFrame(Seq(
        (34.0, Vectors.dense(0.0, 1.1, 0.1)),
        (6.0, Vectors.dense(2.0, 1.0, -1.0)),
        (5.0, Vectors.dense(2.0, 1.3, 1.0)),
        (11.0, Vectors.dense(0.0, 1.2, -0.5))
      )).toDF("label", "features")

      // Train a Wahoo Linear regression model.
      val lr = wctx.createLinearRegression
      lr.setMaxIter(10).setRegParam(1.0)

      lr.fit(training)
      // The second training should just read from the cache.
      lr.fit(training) // hack something weird about reusing tuples
    }
  }
}
