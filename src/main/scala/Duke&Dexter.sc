//case class Raw_Import_Purchases (
//                                  external_id: String,
//                                  email: String,
//                                  size: String,
//                                  size_scale:String,
//                                  quantity: Int,
//                                  order_time: String,
//                                  model: String
//                                )
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



/* Duke and Dexter *
apply regexp to a field. size. tranform UK 9 into 9
 */

val purchase_transfo_size =
  (str: String) =>
  {val pattern = """\d+(?:\.\d+)?""".r;
    pattern.findFirstIn(str).getOrElse(str)
  }

val a = purchase_transfo_size("DDDD 3")

val CleanSizes =
  (rip: RawImportPurchase) => {
    val pattern = """\d+(?:\.\d+)?""".r;
    val newSize= pattern.findFirstIn(rip.size).getOrElse("")
    Some(rip.copy(size=newSize))
  }

/* CASES "UK 9" - UK - ""*/
//t1: no number
val t1 = RawImportPurchase("a2", "","UK","UK","1","","")
val t1tr = CleanSizes(t1)
println(t1tr)
assert(t1tr.get.size=="","ok")
//t2: no number
val t2 = RawImportPurchase("a2", "","uk 9","UK 9","","","")
val t2tr = CleanSizes(t2)
println(t2tr)

//t3: no value
val t3 = RawImportPurchase("a2", "","","","","","")
val t3tr = CleanSizes(t3)
println(t3tr)

