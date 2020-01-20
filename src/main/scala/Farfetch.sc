case class RawImportPurchase(
                              external_id: String,
                              //                              brand: String,
                              model: String,
                              size_scale: String,
                              //                              size_width: String,
                              size: String,
                              order_number: String,
                              order_time: String,
                              email: String
                              //,
                              //                              ip: String,
                              //                              quantity: Int,
                              //                              price: Double,
                              //                              currency: String
                            ){}

case class RawImportReturn(
                              external_id: String,
                              //                              brand: String,
                              model: String,
                              size_scale: String,
                              //                              size_width: String,
                              size: String,
                              order_number: String,
                              order_time: String,
                              email: String
                              //,
                              //                              ip: String,
                              //                              quantity: Int,
                              //                              price: Double,
                              //                              currency: String
                            ){}


/* Farfetch*
set date format m%-d%-Y%
size_scale BR -> BRA
exclude model=CANCEL
*/

val date_format =
  (rip: RawImportPurchase) => {
    val new_date= rip.order_time.slice(6,10)+"-"+rip.order_time.slice(0,2)+"-"+rip.order_time.slice(3,5)
    Some(rip.copy(order_time = new_date))
  }

val BRA_size_scale =
    (rip: RawImportPurchase) => {
      val sc= if(rip.size_scale.indexOf("BR") != -1) "BRA"  else  rip.size_scale
      Some(rip.copy(size_scale = sc))
    }

val exclude_cancelled_rows =
  (rip: RawImportPurchase) => {
    if(rip.model.indexOf("Cancel") != -1) None else Some(rip)
  }

val unify_size_format =
  (rip: RawImportPurchase) => {
    val size=rip.size.replace(',','.')
    println(size)
    Some(rip.copy(size = size))
  }




val size_scale_BR_IT =
  (rir: RawImportReturn) => {
    val sc= if(rir.size_scale.indexOf("BR") != -1) "BRA"
            else if (rir.size_scale.indexOf("IT/EU") != -1) "IT"
            else rir.size_scale
    Some(rir.copy(size_scale = sc))
  }



/* test 1 date format  ""*/
val t1 = RawImportPurchase("ext-id", "email","EU","10,5","onum","10-15-2019","email")
val t1tr =date_format(t1)
val t1tr2 = BRA_size_scale(t1tr.get)
val t1tr3 =  exclude_cancelled_rows(t1tr2.get)
val t1tr4 = unify_size_format(t1tr3.get)
println(t1tr4)



/* test 2 BR scale  ""*/
val t2 = RawImportPurchase("ext-id", "email","BR","10","onum","10-15-2019","email")
val t2tr =date_format(t2)
val t2tr2 = BRA_size_scale(t2tr.get)
val t2tr3 =  exclude_cancelled_rows(t2tr2.get)
println(t2tr3)

/* test 3 exclude  ""*/
val t3 = RawImportPurchase("ext-id", "Cancel","BR","10","onum","10-15-2019","email")
val t3tr =date_format(t3)
val t3tr2 = BRA_size_scale(t3tr.get)
val t3tr3 =  exclude_cancelled_rows(t3tr2.get)
println(t3tr3)


/* test return size_scale  ""*/
val tr1 = RawImportReturn("ext-id", "email","","10","onum","10-15-2019","email")
val tr1t = size_scale_BR_IT(tr1)
println(tr1t)
assert(tr1t.get.size_scale=="IT","size scale OK")
