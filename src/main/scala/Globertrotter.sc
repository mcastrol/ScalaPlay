case class Raw_Import_Purchases (
                                  external_id: String,
                                  email: String,
                                  size: String,
                                  size_scale:String,
                                  quantity: Int,
                                  order_time: String
                                )

/*
ojo ver tratamiento de size no numéricos.
remove leading 0
remove leading 0 from email
clean article-id
exclude rows with quantity <1

set size_scale EU for not size_scale and for sizes between 30 & 60
replace # by .33

*/

//remove leading 0
//remove leading 0 from email
//clean article-id
//replace "#" in size by .33
//exclude rows with quantity <1
val clean_article_id_email_quantity =
  (rip: Raw_Import_Purchases) => {
    val external_id_1=rip.external_id.replaceFirst("^0+", "");
    val regexp = "-\\d+".r
    val external_id_2=regexp.replaceFirstIn(external_id_1,"")

    val regexp_size = "#".r
    val size_1=regexp_size.replaceFirstIn(rip.size,".33")

    val email_1 = rip.email.replaceFirst("^0+", "");


    if(rip.quantity<1) None
    else Some(rip.copy(external_id = external_id_2,email=email_1,size=size_1))
  }

//set size_scale EU for not size_scale and for sizes between 30 & 60
val set_size_scale_eu =
  (rip: Raw_Import_Purchases) => {
    val pattern = """\d+(?:\.\d+)?""".r;
    val size_num= pattern.findFirstIn(rip.size).getOrElse("0").toDouble
    val size_scale_1=if(rip.size_scale.isEmpty & size_num>=30 & size_num<=60) "EU" else rip.size_scale
    Some(rip.copy(size_scale = size_scale_1))
}


val set_33 =
  (rip: Raw_Import_Purchases) => {
    val pattern = """\d+(?:\.\d+)?""".r;
    val size_num= pattern.findFirstIn(rip.size).getOrElse("0").toDouble
    val size_scale_1=if(rip.size_scale.isEmpty & size_num>=30 & size_num<=60) "EU" else rip.size_scale
    Some(rip.copy(size_scale = size_scale_1))
  }

/*
36# -> 36.33
*/

//test 1 article-id and email
val t1 = Raw_Import_Purchases("00001025305-5637655019", "000020181729","40","",1,"10-15-2019")
val t1tr =clean_article_id_email_quantity(t1)
val t1tr2 =set_size_scale_eu(t1tr.get)
println(t1tr2)


val t2 = Raw_Import_Purchases("00001025305-5637655019", "000020181729","10#","",0,"10-15-2019")
val t2tr =clean_article_id_email_quantity(t2)
println(t2tr)

val t3 = Raw_Import_Purchases("00001025305-563019", "000020181729","35#","",10,"10-15-2019")
val t3tr =clean_article_id_email_quantity(t3)
println(t3tr)