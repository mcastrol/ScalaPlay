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


/* Danner if size_scale is null size_scale="us_m"*/


val Size_Scale_Default_Value =
  (rip: RawImportPurchase) => {
    val sc = if(rip.size_scale.isEmpty) "us_m" else rip.size_scale
    Some(rip.copy(size_scale=sc))
  }

/* CASES size_scale empty */
//t1: no number
val t1 = RawImportPurchase("extid","email","","10","","1","")
val t1tr = Size_Scale_Default_Value(t1)
println(t1tr)

//t2: size_scale us_m
val t2 = RawImportPurchase("extid","email","us_m","10","","1","")
val t2tr = Size_Scale_Default_Value(t2)
println(t2tr)

//t3: no us_w
val t3 = RawImportPurchase("extid","email","us_w","10","","1","")
val t3tr = Size_Scale_Default_Value(t3)
println(t3tr)

