case class Raw_Import_Purchases
      (external_id: String,
       email: String,
       size: String,
       size_scale: String,
       quantity: Int
       )


/* Duke and Dexter *
apply regexp to a field.
 */
//val purchase_transfo_size =
//  (str: String) =>
//      {val pattern = """\d+(?:\.\d+)?""".r;
//        if(!str.isEmpty) {
//          pattern.findFirstIn(str).getOrElse(str)
//        }
//        else "";
//  }
//
//val a = purchase_transfo_size("")

/* Kyburn no rules*

/* Goertz *
exclude all size_scale=UK
exclude all size<33
 */
 */
//val exclude__size_scale_uk =
//  (rip: Raw_Import_Purchases) => {
//    val pattern = """UK""".r
//    val scaleMatches = pattern.findFirstMatchIn(rip.size_scale)
//    scaleMatches match {
//      case Some(rip) =>
//        None
//      case None =>
//        Some(rip)
//    }
//  }
//
//val ripTest = Raw_Import_Purchases("a2", "","34","UK",1)
//val ripTestTransfo = exclude__size_scale_uk(ripTest)
//println(ripTestTransfo)

val exclude_size_less_33 =
  (rip: Raw_Import_Purchases) => {
      //val newSizes = rip.size.filter(size => (size.toDouble >= 33.0))
      if(rip.size.toDouble < 33.0)  None
      else Some(rip)
  }

val ripTest = Raw_Import_Purchases("a2", "","28","UK",1)
val ripTestTransfo = exclude_size_less_33(ripTest)
println(ripTestTransfo)

val ripTest2 = Raw_Import_Purchases("a2", "","30","UK",1)
val ripTestTransfo2 = exclude_size_less_33(ripTest2)
println(ripTestTransfo2)