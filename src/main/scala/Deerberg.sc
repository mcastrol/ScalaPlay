case class Raw_Import_Purchases (
                                  external_id: String,
                                  email: String,
                                  size: String,
                                  size_scale:String,
                                  quantity: Int,
                                  order_time: String
                                )


/* Deerberg	"if size<20 then size_scale=UK
if quantity=0 qurantity=1""*
 */


val Set_Size_Scale_UK =
  (rip: Raw_Import_Purchases) => {
    val pattern = """\d+(?:\.\d+)?""".r;
    val newSize= pattern.findFirstIn(rip.size).getOrElse("")
    val sc = if(newSize.toDouble<=20) "UK" else rip.size_scale;
    Some(rip.copy(size_scale=sc))
  }

val Set_Default_Quantity =
  (rip: Raw_Import_Purchases) => {
    val newQ = if(rip.quantity.isNaN | rip.quantity==0) 1 else rip.quantity;
    Some(rip.copy(quantity=newQ))
  }



/* CASES size_scale empty */
//t1: no number
val t1 = Raw_Import_Purchases("extid","email","6/8","",0)
val t1tra = Set_Size_Scale_UK(t1)
val t1trb = Set_Default_Quantity(t1tra.get)
println(t1trb)

//t2: size_scale us_m
val t2 = Raw_Import_Purchases("extid","email","4.5","us_m",5)
val t2tr = Set_Size_Scale_UK(t2)
val t2trb = Set_Default_Quantity(t2tr.get)
println(t2trb)

//t3: no us_w
val t3 = Raw_Import_Purchases("extid","email","4.5","us_m",0)
val t3tr = Set_Size_Scale_UK(t3)
val t3trb = Set_Default_Quantity(t3tr.get)
println(t3trb)
