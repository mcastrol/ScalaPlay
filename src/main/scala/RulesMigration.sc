case class Raw_Import_Purchases
      (external_id: String,
       email: String,
       size: String,
       size_scale: String,
       quantity: Int
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
val ripDD = Raw_Import_Purchases("a2", "","","UK",1)
val ripDDTransfo = CleanSizes(ripDD)
println(ripDDTransfo)

/* Kyburn no rules*

/* Goertz *
exclude all size_scale=UK
exclude all size<33
 */
 */
