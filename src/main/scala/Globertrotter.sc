case class RawImportPurchase(
                              external_id: String,
                              //                              brand: String,
                              model: String,
                              size_scale: String,
                              //                              size_width: String,
                              size: String,
                              order_number: String,
                              order_time: String,
                              email: String,
                              //,
                              //                              ip: String,
                              quantity: Int
                              //                              price: Double,
                              //                              currency: String
                            ){}
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
  (rip: RawImportPurchase) => {
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
  (rip: RawImportPurchase) => {
    val pattern = """\d+(?:\.\d+)?""".r;
    val size_num= pattern.findFirstIn(rip.size).getOrElse("0").toDouble
    val size_scale_1=if(rip.size_scale.isEmpty & size_num>=30 & size_num<=60) "EU" else rip.size_scale
    Some(rip.copy(size_scale = size_scale_1))
}




/*
36# -> 36.33
*/

//test 1 article-id and email
val t1 = RawImportPurchase("00001025305-5637655019", "model1","","40","onumber","2019-10-15","000020181729",1)
val t1tr =clean_article_id_email_quantity(t1)
println(t1tr)
assert(t1tr.get.external_id=="1025305","clean article id: leading 0 and after - OK")
assert(t1tr.get.email=="20181729","clean email: leading 0 is OK")
assert(t1tr.get.size=="40","keep size same is OK")
assert(t1tr.get.quantity==1,"keep quantity in 1 is OK")
val t1tr2 =set_size_scale_eu(t1tr.get)
assert(t1tr2.get.size_scale=="EU","put size scale in EU is OK")
println(t1tr2)

//test 2: exclude rows with quantity <1
val t2 = RawImportPurchase("00001025305-5637655019", "model2","","10#","onu","10-15-2019","000020181729",0)
val t2tr =clean_article_id_email_quantity(t2)
assert(t2tr.getOrElse("None")=="None","exclude rows with quantity = 0 ok")
println(t2tr)

//test 3 transform # in size for .33
val t3 = RawImportPurchase("00001025305-563019", "model","","35#","on","10-15-2019","000020181729",10)
val t3tr =clean_article_id_email_quantity(t3)
val t3tr2 =set_size_scale_eu(t3tr.get)
println(t3tr2)
assert(t3tr2.get.external_id=="1025305","clean article id: leading 0 and after - OK")
assert(t3tr2.get.email=="20181729","clean email: leading 0 is OK")
assert(t3tr2.get.quantity==10,"keep quantity in 1 is OK")
assert(t3tr2.get.size=="35.33","replace # by .33 is OK")
assert(t3tr2.get.size_scale=="EU","put size scale in EU is OK")
