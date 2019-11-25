import java.io.ByteArrayInputStream
import java.sql.{Connection, DriverManager, PreparedStatement}

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.{Column, DataFrame, SparkSession}

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


//    val dataFrameReader = session.read
//
//    val purchases_input_file = dataFrameReader
//      .option("header", "true")
//      .option("inferSchema", value = true)
//      .csv("in/purchases_input_file.csv")
//
//    System.out.println("=== Print out schema ===")
//    purchases_input_file.printSchema()
//    purchases_input_file.show()
    //scalikejdbc: configure build.sbt & resources/application.conf
    DBsWithEnv("development").setupAll()

    //reading title and building an array of colNames
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
    //val ids = Seq(71,72,73,74,75,76,77,78,79,80)

    // simple example
    //execute a select of
    val model: Option[String] = DB readOnly { implicit session =>
      sql"SELECT model from shop_portfolio_shoes where id=${id}".map(rs => rs.string("model")).single.apply()
    }

    println("printing several models")

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

//    case class Brands(id: Int, name: String, created_at: String, updated_at: String, size_chart_policy_id: Int, main_image: String, url: String)
//    /// QueryDSL
//    object Brands extends SQLSyntaxSupport[Brands] {
//        def apply(b: ResultName[Brands])(rs: WrappedResultSet): Brands= new Brands(id=rs.get(b.id), name = rs.get(b.name),
//          created_at = rs.get(b.created_at), updated_at=rs.get(b.updated_at), size_chart_policy_id=rs.get(b.size_chart_policy_id),
//          main_image=rs.get(b.main_image),url= rs.get(b.url))
//    }
//    val b = Brands.syntax("b")
//    val brand: Option[Brands] = DB readOnly { implicit session =>
//      withSQL { select.from(Brands as b).where.eq(b.id, id) }.map(Brands(b.resultName)).single.apply()
//    }
//
//    println(brand)


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
    }
    println("list varios results:")

    println(list_shoes)


    //indicating fetch size
    val list_shoes_fetch: List[Shoe] = DB readOnly { implicit session =>
      sql"SELECT id,model,material from shop_portfolio_shoes limit 1000"
        .fetchSize(100)
        .map(rs => Shoe(rs.int("id"), rs.string("model"), rs.string("material"))).list.apply()
    }
    println("list varios results (using fetch):")

    println(list_shoes_fetch)

    ).execute.apply()

    val bytes = scala.Array[Byte](1, 2, 3, 4, 5, 6, 7)
    val in = new ByteArrayInputStream(bytes)
    val bytesBinder = ParameterBinder(
      value = in,
      binder = (stmt: PreparedStatement, idx: Int) => stmt.setBinaryStream(idx, in, bytes.length)
    )

    sql"insert into blob_example (data) values (${bytesBinder})").update.apply()


    DBs.closeAll()

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