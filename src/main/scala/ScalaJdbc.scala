import java.sql.{Connection, DriverManager, PreparedStatement, SQLException}

import scala.collection.mutable.ListBuffer
import org.apache.log4j.{Level, Logger}
import org.apache.parquet.schema.Type.ID
import org.apache.spark.sql.{DataFrame, DataFrameWriter, SaveMode, SparkSession}

import scala.language.postfixOps

/**
  * A Scala JDBC connection example by Alvin Alexander,
  * https://alvinalexander.com
  */
object ScalaJdbc {

  def main(args: Array[String]) {
    // connect to the database named "mysql" on the localhost

    val driver="org.postgresql.Driver"
    val url="jdbc:postgresql://aa1e1cjlcyi36s5.ci7bif7gh2xh.eu-central-1.rds.amazonaws.com:5432/ebdb"
    val username="shoesize"
    val password="SSM2015?"

    // there's probably a better way to do this
    var connection:Connection = null

    try {
      // make the connection
      Class.forName(driver)
      connection = DriverManager.getConnection(url, username, password)

      // create the statement, and run the select query
      val statement = connection.createStatement()
      val resultSet = statement.executeQuery("select id, encrypted_password from users where email='marcela.castro@shoesize.me'")
      while ( resultSet.next() ) {
        val id = resultSet.getString("id")
        val encrypted_password = resultSet.getString("encrypted_password")
        println("id, encrypted_password = " + id + ", " +encrypted_password)
      }
    } catch {
      case e => e.printStackTrace
    }

    // CASE Insert 1 row
    val table_name= "raw_import_purchases"

    val column_label_list_string = "order_number, order_time,external_id,email,ip,size,size_scale,shop_id,status, import_id,user_id,created_at, updated_at"

    val value_list_string="'100401213','2019-07-13 07:17','28128331','null','178.238.175.216','430','EU',802,0,99999,23102879,now(),now()"

    val prepare_statement_add_column = connection.prepareStatement(s"INSERT INTO $table_name ($column_label_list_string) VALUES ($value_list_string) ")
    prepare_statement_add_column.executeUpdate()
    prepare_statement_add_column.close()

    // case 2 several rows
    val ps_2 = connection.prepareStatement("INSERT INTO MCLTEST(ID, NAME, URL) VALUES (?, ?, ?)")

    ps_2.setInt(1, 2)
    ps_2.setString(2, "DOS")
    ps_2.setString(3, "URL2")
    val rs = ps_2.execute()

    println(rs)

    val session = SparkSession.builder().appName("readcsv").master("local[1]").getOrCreate()
    import session.implicits._

    val dataFrameReader = session.read


    val dftest = dataFrameReader
      .option("header", "true")
      .option("inferSchema", value = true)
      .csv("in/mcl_test.csv")

    dftest.show()

    val exp = "name,url"

    val a = dftest.selectExpr(exp.split(","):_*)

    a.show

    dftest.toDF().write
      .mode(SaveMode.Append)
      .format("jdbc")
      .option("url", url)
      .option("dbtable", "mcl_test")
      .option("user", username)
      .option("password", password)
      .save()

    val prop = new java.util.Properties
      prop.setProperty("driver", driver)
      prop.setProperty("user", username)
      prop.setProperty("password", password)

    /*
    //destination database table
    val table = "mcl_test"
    val brandsDF = session.read
      .format("jdbc")
      .option("url", url)
      .option("dbtable", "brands")
      .option("user", username)
      .option("password", password)
      .load()

*/
    /*
    val ps_3 = connection.prepareStatement("INSERT INTO MCLTEST(ID, NAME, URL) VALUES (?, ?, ?)")
    mcltest_file.collect().foreach { row =>
      println(row.mkString(",")
      ps_3.setInt(1, ROW2))
    }
*/

//    var all_columns= new ListBuffer[String]()
//
//    //select * from a table
//    val statement = connection.createStatement()
//    val resultSet = statement.executeQuery(s"select * from $table_name where import_id=99999")
//    //Get metadata of resultSet
//    val resultSet_metadata = resultSet.getMetaData()
//    //Get the number of columns
//    val column_count = resultSet_metadata.getColumnCount()
//    for (i <- 1 to column_count ) {
//      val column_name = resultSet_metadata.getColumnName(i)
//      all_columns += column_name
//    }
//    println("all_columns: "+all_columns)
//
//    val all_columns_list = all_columns.toList
//
//    println("all_values:")
//
//    while ( resultSet.next() ) {
//      for (c <- all_columns_list) {
//        var value = resultSet.getString(c)
//        printf(s"$value, ")
//      }
//      printf("%n")
//    }


    /*
    Logger.getLogger("org").setLevel(Level.ERROR)
    val session = SparkSession.builder().appName("readcsv").master("local[1]").getOrCreate()
    import session.implicits._

    val brandsDF = session.read
      .format("jdbc")
      .option("url", url)
      .option("dbtable", "brands")
      .option("user", username)
      .option("password", password)
      .load()

    val brandsDFtowrite= brandsDF.filter($"name" isin ("adidas","nike","timberland")).show()
*/







    /// batch

//    val value_empty_col="?,?,?,?,?,?,?,?,?,?,?,?,?"
//
//    var rpi_all_columns= new ListBuffer[]()
//
//    val value_list_string = "'100401213','2019-07-13 07:17','28128331','null','178.238.175.216','430','EU',802,0,99999,23102879,now(),now()
//    val SQL = "INSERT INTO $table_name ($column_label_list_string) VALUES ($value_empty_col)"
//    try {
//      val conn = connection
//      val statement = conn.prepareStatement(SQL)
//      try {
//        var count = 0
//        import scala.collection.JavaConversions._
//        for (actor <- list) {
//          statement.setString(1, actor.getFirstName)
//          statement.setString(2, actor.getLastName)
//          statement.addBatch
//          count += 1
//          // execute every 100 rows or less
//          if (count % 100 == 0 || count == list.size) statement.executeBatch
//        }
//      } catch {
//        case ex: SQLException =>
//          System.out.println(ex.getMessage)
//      } finally {
//        if (conn != null) conn.close()
//        if (statement != null) statement.close()
//      }
//    }


    connection.close()
  }

}