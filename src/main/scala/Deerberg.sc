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
                              //                              ip: String,
                              quantity: Int
                              //                              price: Double,
                              //                              currency: String
                            ){}

/* Deerberg	"if size<20 then size_scale=UK
if quantity=0 qurantity=1""*
 */


val Set_Size_Scale_UK =
  (rip: RawImportPurchase) => {
    val pattern = """\d+(?:\.\d+)?""".r;
    val newSize= pattern.findFirstIn(rip.size).getOrElse("")
    val sc = if(newSize.toDouble<=20) "UK" else rip.size_scale;
    Some(rip.copy(size_scale=sc))
  }

val Set_Default_Quantity =
  (rip: RawImportPurchase) => {
    val newQ = if(rip.quantity.isNaN | rip.quantity==0) 1 else rip.quantity;
    Some(rip.copy(quantity=newQ))
  }



/* CASES size_scale empty */
//t1: no number
val t1 = RawImportPurchase("extid","model","","6/8","onumber","otime","email",1)
val t1tra = Set_Size_Scale_UK(t1)
val t1trb = Set_Default_Quantity(t1tra.get)
println(t1trb)

//t2: size_scale us_m
val t2 = RawImportPurchase("extid","email","us_m","4.5","onumber","otime","email",1)
val t2tr = Set_Size_Scale_UK(t2)
val t2trb = Set_Default_Quantity(t2tr.get)
println(t2trb)

//t3: no us_w
val t3 = RawImportPurchase("extid","email","us_m","4.5","onumber","otime","email",1)
val t3tr = Set_Size_Scale_UK(t3)
val t3trb = Set_Default_Quantity(t3tr.get)
println(t3trb)
