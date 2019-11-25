case class Raw_Import_Purchases2 (
                                  external_id: String,
                                  email: String,
                                  size: String,
                                  size_scale:String,
                                  quantity: Int,
                                  order_time: String,
                                  model: String
                                )


/* Farfetch*
set date format m%-d%-Y%
size_scale BR -> BRA
exclude model=CANCEL
*/

val date_format =
  (rip: Raw_Import_Purchases2) => {
    val new_date= rip.order_time.slice(6,10)+"-"+rip.order_time.slice(0,2)+"-"+rip.order_time.slice(3,5)
    Some(rip.copy(order_time = new_date))
  }

val BRA_size_scale =
    (rip: Raw_Import_Purchases2) => {
      val sc= if(rip.size_scale.indexOf("BR") != -1) "BRA"  else  rip.size_scale
      Some(rip.copy(size_scale = sc))
    }

val exclude_cancelled_rows =
  (rip: Raw_Import_Purchases2) => {
    if(rip.model.indexOf("Cancel") != -1) None else Some(rip)
  }



/* test 1 date format  ""*/
val t1 = Raw_Import_Purchases2("ext-id", "email","10","EU",1,"10-15-2019","modelito")
val t1tr =date_format(t1)
val t1tr2 = BRA_size_scale(t1tr.get)
val t1tr3 =  exclude_cancelled_rows(t1tr2.get)
println(t1tr3)

println(t1tr)

/* test 2 BR scale  ""*/
val t2 = Raw_Import_Purchases2("ext-id", "email","10","BR",1,"10-15-2019","modelito")
val t2tr =date_format(t2)
val t2tr2 = BRA_size_scale(t2tr.get)
val t2tr3 =  exclude_cancelled_rows(t2tr2.get)
println(t2tr3)

/* test 3 exclude  ""*/
val t3 = Raw_Import_Purchases2("ext-id", "email","10","BR",1,"10-15-2019","Cancel")
val t3tr =date_format(t3)
val t3tr2 = BRA_size_scale(t3tr.get)
val t3tr3 =  exclude_cancelled_rows(t3tr2.get)
println(t3tr3)