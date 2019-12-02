case class Raw_Import_Purchases (
                                  external_id: String,
                                  email: String,
                                  size: String,
                                  size_scale:String,
                                  quantity: Int,
                                  order_time: String,
                                  model: String
                                )


/* Danner if size_scale is null size_scale="us_m"*/


val Size_Scale_Default_Value =
  (rip: Raw_Import_Purchases) => {
    val sc = if(rip.size_scale.isEmpty) "us_m" else rip.size_scale
    Some(rip.copy(size_scale=sc))
  }

/* CASES size_scale empty */
//t1: no number
val t1 = Raw_Import_Purchases("extid","email","10","",1,"","")
val t1tr = Size_Scale_Default_Value(t1)
println(t1tr)

//t2: size_scale us_m
val t2 = Raw_Import_Purchases("extid","email","10","us_m",1,"","")
val t2tr = Size_Scale_Default_Value(t2)
println(t2tr)

//t3: no us_w
val t3 = Raw_Import_Purchases("extid","email","10","us_w",1,"","")
val t3tr = Size_Scale_Default_Value(t3)
println(t3tr)

