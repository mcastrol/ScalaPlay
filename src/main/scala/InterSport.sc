
case class Raw_Import_Purchases (
                                  external_id: String,
                                  email: String,
                                  size: String,
                                  size_scale:String,
                                  quantity: Int,
                                  order_time: String,
                                  model: String
                                )

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

def getNumeric(s:String) : String = {
  val pattern = """\d+(?:\.\d+)?""".r;
  val newSize = pattern.findFirstIn(s).getOrElse("")
  newSize
}


//exclude empty sizes and sizes < 34 and sizes > 55
//take only sizes: remove not numeric simbols
//  only US sizes (<20)
val clean_sizes =
  (rip: Raw_Import_Purchases) => {
    val new_size_numeric = getNumeric(rip.size)
    val ripNow= if(isNullOrEmpty(new_size_numeric)) None
    else {if(new_size_numeric.toDouble>=34 && new_size_numeric.toDouble<=55) Some(rip.copy(size = new_size_numeric))
         else None
      }
    ripNow
  }


//exclude empty sizes
//take only sizes: remove not numeric simbols
//  only US sizes (<20)
//test1 2
val t1 = Raw_Import_Purchases("00001025305-5637655019", "000020181729","40","",1,"2019-10-15","")
val t1tr =clean_sizes(t1)
println(t1tr)
assert(t1tr.get.size=="40","right size")

//test1 exclude empty sizes
val t2 = Raw_Import_Purchases("00001025305-5637655019", "000020181729","","",1,"2019-10-15","")
val t2tr =clean_sizes(t2)
println(t2tr)
assert(t2tr.getOrElse("None")=="None","exclude empty sizes")


//test3 remove non numeric chars
val t3 = Raw_Import_Purchases("00001025305-5637655019", "000020181729","Waxed Black 15in","24",1,"2019-10-15","")
val t3tr =clean_sizes(t3)
println(t3tr)
assert(t3tr.getOrElse("None")=="None","exclude kids sizes < 33")


//test4 remove non numeric chars
val t4 = Raw_Import_Purchases("00001025305-5637655019", "000020181729","asf60","fs60",1,"2019-10-15","")
val t4tr =clean_sizes(t4)
println(t4tr)
assert(t4tr.getOrElse("None")=="None","exclude > 54")


