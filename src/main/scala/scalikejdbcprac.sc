import java.sql.{Connection, DriverManager}

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.{Column, DataFrame, SparkSession}

import scala.collection.mutable.ListBuffer
import org.apache.spark.sql.functions._
import scalikejdbc._
import scalikejdbc.config._


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
//val nameOnly = (rs: WrappedResultSet) => rs.string("model")
//
//val name: Option[String] = DB readOnly { implicit session =>
//  sql"SELECT model from shop_portfolio_shoes where id = ${id}".map(nameOnly).single.apply()
//}
//println(name)

// define a class to map the result
case class Shoe(id: Int, model: String, material: String)
val shoe: Option[Shoe] = DB readOnly { implicit session =>
  sql"SELECT id,model,material from shop_portfolio_shoes  where id = ${id}"
    .map(rs => Shoe(rs.int("id"), rs.string("model"), rs.string("material"))).single.apply()
}

println(shoe)

case class Brands(id: Int, name: String, created_at: String, updated_at: String, size_chart_policy_id: Int, main_image: String, url: String)
/// QueryDSL
object Brands extends SQLSyntaxSupport[Brands] {
  def apply(b: ResultName[Brands])(rs: WrappedResultSet): Brands= new Brands(id=rs.get(b.id), name = rs.get(b.name),
    created_at = rs.get(b.created_at), updated_at=rs.get(b.updated_at), size_chart_policy_id=rs.get(b.size_chart_policy_id),
    main_image=rs.get(b.main_image),url= rs.get(b.url))
}
val b = Brands.syntax("b")
val brand: Option[Brands] = DB readOnly { implicit session =>
  withSQL { select.from(Brands as b).where.eq(b.id, id) }.map(Brands(b.resultName)).single.apply()
}

println(brand)


DBs.closeAll()