case class Raw_Import_Purchases (
                                  external_id: String,
                                  email: String,
                                  size: String,
                                  size_scale:String,
                                  quantity: Int,
                                  order_time: String
                                )

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
  (rip: Raw_Import_Purchases) => {
    val pattern = """\d+(?:\.\d+)?""".r;
    val newSize= pattern.findFirstIn(rip.size).getOrElse("")
    Some(rip.copy(size=newSize))
  }

/* CASES "UK 9" - UK - ""*/
//t1: no number
val t1 = Raw_Import_Purchases("a2", "","","UK",1)
val t1tr = CleanSizes(t1)
println(t1tr)
//t2: no number
val t2 = Raw_Import_Purchases("a2", "","","UK 9",1)
val t2tr = CleanSizes(t2)
println(t2tr)

//t3: no value
val t3 = Raw_Import_Purchases("a2", "","","",1)
val t3tr = CleanSizes(t3)
println(t3tr)

