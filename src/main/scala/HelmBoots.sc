
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
                              email: String,
                              //,
                              //                              ip: String,
                              quantity: Int
                              //                              price: Double,
                              //                              currency: String
                            ){}
//
//def isNullOrEmpty[T](s: Seq[T]) = s match {
//  case null => true
//  case Seq() => true
//  case _ => false
//}
//
//def getInitNumeric(s:String) : String = {
//  val pattern = """^\d+(?:\.\d+)?""".r;
//  val newSize = pattern.findFirstIn(s).getOrElse("")
//  newSize
//}
//
//def getNumeric(s:String) : String = {
//  val pattern = """\d+(?:\.\d+)?""".r;
//  val newSize = pattern.findFirstIn(s).getOrElse("")
//  newSize
//}


//exclude empty sizes
//take only sizes: remove not numeric simbols
//  only US sizes (<20)
val clean_sizes =
  (rip: RawImportPurchase) => {
    def isNullOrEmpty[T](s: Seq[T]) = s match {
      case null => true
      case Seq() => true
      case _ => false
    }
    def getInitNumeric(s:String) : String = {
      val pattern = """^\d+(?:\.\d+)?""".r;
      val newSize = pattern.findFirstIn(s).getOrElse("")
      newSize
    }
    val new_size_numeric = getInitNumeric(rip.size)
    val ripNow= if(isNullOrEmpty(new_size_numeric)) None
    else {if(new_size_numeric.toDouble<=20) Some(rip.copy(size = new_size_numeric))
         else None
      }
    ripNow
  }


//exclude empty sizes
//take only sizes: remove not numeric simbols
//  only US sizes (<20)
//test1 2
val t1 = RawImportPurchase("00001025305-5637655019", "model","","40","Onumber","2019-10-15","000020181729",1)
val t1tr =clean_sizes(t1)
println(t1tr)
assert(t1tr.getOrElse("None")=="None","exclude sizes <40")

//test1 exclude empty sizes
val t2 = RawImportPurchase("00001025305-5637655019", "model2","","","onumber","2019-10-15","000020181729",1)
val t2tr =clean_sizes(t2)
println(t2tr)
assert(t2tr.getOrElse("None")=="None","exclude empty sizes")


//test3 remove non numeric chars
val t3 = RawImportPurchase("00001025305-5637655019","Waxed Black15in","","xf","onumber","2019-10-15","000020181729",1)
val t3tr =clean_sizes(t3)
println(t3tr)
assert(t3tr.getOrElse("None")=="None","exclude non numeric sizes")


//good size
val t4 = RawImportPurchase("00001025305-5637655019","Waxed Black15in","","8","onumber","2019-10-15","000020181729",1)
val t4tr =clean_sizes(t4)
println(t4tr)
assert(t4tr.get.size=="8","good US size")
