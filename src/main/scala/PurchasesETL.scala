import java.sql.{Connection, DriverManager}

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.Column

import scala.collection.mutable.ListBuffer
import org.apache.spark.sql.functions._
import scalikejdbc._
import scalikejdbc.config._



object PurchasesETL {

//  val ENVIRONMENT = sys.env("SSM_ENV")


  def main(args: Array[String]) {

    Logger.getLogger("org").setLevel(Level.OFF)
    val session = SparkSession.builder().appName("readcsv").master("local[1]").getOrCreate()
    import session.implicits._



    val dataFrameReader = session.read



    val purchases_input_file = dataFrameReader
      .option("header", "true")
      .option("inferSchema", value = true)
      .csv("in/purchases_input_file.csv")


    System.out.println("=== Print out schema ===")
    purchases_input_file.printSchema()
    purchases_input_file.show()

//    DBs.setupAll()

    DBsWithEnv("development").setupAll()
    var col_names = new ListBuffer[String]()
    DB readOnly { implicit session =>
      sql"""select title from import_configurations ic join import_file_column_options ifco on ifco.import_configuration_id=ic.id
          where ic.shop_id=802
          and ic.subject=1
          and ifco.order>=0
          order by ifco.order""".foreach { rs => col_names +=rs.string("title")}
    }

    col_names.toList
    col_names.foreach(println)

    val id = 215496

    // simple example
    val model: Option[String] = DB readOnly { implicit session =>
      sql"SELECT model from shop_portfolio_shoes where id = ${id}".map(rs => rs.string("model")).single.apply()
    }

    println(model)

    // defined mapper as a function
    val nameOnly = (rs: WrappedResultSet) => rs.string("model")
    val name: Option[String] = DB readOnly { implicit session =>
      sql"SELECT model from shop_portfolio_shoes where id = ${id}".map(nameOnly).single.apply()
    }
    println(model)

    // define a class to map the result
    case class Shoe(id: Int, model: String, material: String)
    val shoe: Option[Shoe] = DB readOnly { implicit session =>
      sql"SELECT id,model,material from shop_portfolio_shoes  where id = ${id}"
        .map(rs => Shoe(rs.int("id"), rs.string("model"), rs.string("material"))).single.apply()
    }

    println(shoe)

//    case class Shoe(id: Int, model: String, material: String)
    // QueryDSL
//    object Shoe extends SQLSyntaxSupport[Shoe] {
//        def apply(e: ResultName[Shoe])(rs: WrappedResultSet): Shoe = new Shoe(id=rs.get(e.id), model = rs.get(e.model), material = rs.get(e.material))
//    }
//    val e = Shoe.syntax("e")
//    val shoe2: Option[Shoe] = DB readOnly { implicit session =>
//      withSQL { select.from(Shoe as e).where.eq(e.id, id) }.map(Shoe(e.resultName)).single.apply()
//    }
//
//    println(shoe2)


    //first result
    val model_first: Option[String] = DB readOnly { implicit session =>
      sql"SELECT model from shop_portfolio_shoes limit 10".map(rs => rs.string("model")).first.apply()
    }
    println("first result:"+model_first)

    //List Results
    val listshoes: List[String] = DB readOnly { implicit session =>
      sql"SELECT model from shop_portfolio_shoes limit 10".map(rs => rs.string("model")).list.apply()
    }
    println("list results:")
    listshoes.foreach(println)

    //for each operation
//    var list_shoes = List[Shoe] = Nil

    val list_shoes: List[Shoe] = DB readOnly { implicit session =>
      sql"SELECT id,model,material from shop_portfolio_shoes limit 10".map(rs => Shoe(rs.int("id"), rs.string("model"), rs.string("material"))).list.apply()
//    val m = Shoe.syntax("s")
//    val name: Option[String] = DB readOnly { implicit session =>
//      withSQL { select(e.result.name).from(Emp as e) }.map(_.string(e.name)).first.apply()
    }

    println("list varios results:")

    println(list_shoes)

    DBs.closeAll()

    //get configured mapped columns from configuration
//    var col_names = getMappingColumns(session, 802)
//
//    var purchases_input_file_2 = purchases_input_file.toDF(col_names: _*).withColumn("size_scale",lit("EU")).show
//



  }


//  def getMappingColumns(session: SparkSession, Shop_id:Int) : List[String] = {
//
////    DBsWithEnv(ENVIRONMENT).setupAll()
//    DBs.setupAll()
//
//    var col_names = new ListBuffer[String]()
//    DB readOnly { implicit session =>
//      sql"""select title from import_configurations ic join import_file_column_options ifco on ifco.import_configuration_id=ic.id
//          where ic.shop_id=$Shop_id
//          and ic.subject=1
//          and ifco.order>=0
//          order by ifco.order""".foreach { rs => col_names +=rs.string("title")}
//    }
//
//
//    DBs.closeAll()
//    col_names.toList
//  }
//

  //  def getVirtualValues(Shop_id:Int) : Option[VirtualColumns] = {
  //  def getVirtualValues(Shop_id:Int) : String = {
  //
  //    case class VirtualColumns(title: String, virtual_value: String)
  //
  //
  //
  //    DBsWithEnv(ENVIRONMENT).setupAll()
  //
  //  val vco: List[VirtualColumns] = DB readOnly { implicit session =>
  //      sql"""select title,virtual_value from import_configurations ic
  //      join import_file_column_options ifco on ifco.import_configuration_id=ic.id
  //      where ic.shop_id=$Shop_id
  //      and ic.subject=1
  //      and ifco.order is null""".map(rs => vco).toMap())
  //    }
  //
  //  vco.foreach(println)
  //  DBs.closeAll()//
  //    "aaaa"
  ////    virtual_columns
  //  }
  //



}