/* Goertz *
exclude all size_scale=UK
exclude all size<33
 */
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



val exclude_size_scale_uk =
  (rip: RawImportPurchase) => {
    val pattern = """UK""".r
    val scaleMatches = pattern.findFirstMatchIn(rip.size_scale)
    scaleMatches match {
      case Some(rip) =>
        None
      case None =>
        Some(rip)
    }
  }

val ripT1 = RawImportPurchase("a2", "","UK","34","1","","")
val ripT1Transfo = exclude_size_scale_uk(ripT1)
println(ripT1Transfo)

val exclude_size_less_33 =
  (rip: RawImportPurchase) => {
    val pattern = """\d+(?:\.\d+)?""".r;
    val newSize= pattern.findFirstIn(rip.size).getOrElse("0")
    if(newSize.toDouble < 33.0)  None
    else Some(rip)
  }

val date_format =
  (rip: RawImportPurchase) => {
    val new_date= rip.order_time.slice(0,19).replace('T', ' ')
    Some(rip.copy(order_time = new_date))
  }


val ripTest = RawImportPurchase("a2", "","UK","28","1","2020-01-06T18:40:38Z0","")
//val ripTestTransfo = exclude_size_less_33(ripTest)
val ripTestTransfo = date_format(ripTest)
println(ripTestTransfo)


val ripTest2 = RawImportPurchase("a2", "","UK","40","1","","")
val ripTestTransfo2 = exclude_size_less_33(ripTest2)
println(ripTestTransfo2)

val rT3 = RawImportPurchase("a2", "","M","UK","1","","")
val ripT3Tr1 = exclude_size_less_33(rT3)
println(ripT3Tr1)



