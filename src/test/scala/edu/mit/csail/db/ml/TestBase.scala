package org.apache.spark.ml

import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.sql.{SQLContext, DataFrame}
import org.apache.log4j.{Logger, Level}
import java.io.File

/**
 * Object that sets up a generic wahoo config and context for tests
 */
object TestBase {
  // Turn off logging.
  Logger.getLogger("org").setLevel(Level.OFF)
  Logger.getLogger("akka").setLevel(Level.OFF)
  Logger.getLogger("breeze").setLevel(Level.OFF)

  // Make the log directory, if it doesn't already exist.
  private val logDir = "testLog"
  new File(logDir).mkdir()

  // Set up Spark.
  private val conf = new SparkConf()
    .setMaster("local[2]")
    .setAppName("test")
    .set("spark.eventLog.enabled", "true")
    .set("spark.eventLog.dir", logDir)
  val sc = new SparkContext(conf)
  val sqlContext = SQLContext.getOrCreate(sc)

  /**
    * Execute the given function with a new WahooContext. The name of the database used by the
    * WahooContext is given by dbName.
    * @param dbName - Name of the database
    * @param fn - Function to execute with the WahooContext
    */
  def withContext(dbName: String)(fn: WahooContext => Unit): Unit = {
    val wconf = new WahooConfig().setDbName(dbName).setDropFirst(true)
    val wcontext = new WahooContext(sc, wconf)

    fn(wcontext)
    wcontext.dropDb
  }

  /**
    * Execute the given function with a new WahooContext.
    * @param fn - The function to execute.
    */
  def withContext(fn: WahooContext => Unit): Unit = {
    val dbName = "test" + java.util.UUID.randomUUID().toString
    withContext(dbName)(fn)
  }
}