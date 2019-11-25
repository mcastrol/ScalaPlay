case class Raw_Import_Returns
    (external_id: String,
     email: String,
     size: String,
     size_scale: String,
     quantity: Int,
     reason: String,
     return_reason: Int
    )


/* Caval *
size: 0, other:1,
if reason contains "Too" -> return_reason=0 (size) else =1 (other)
 */

val Return_Reason =
  (rir: Raw_Import_Returns) => {
    val new_return_reason= if(rir.reason.indexOf("Too") != -1) 1  else  0
    Some(rir.copy(return_reason = new_return_reason))
  }

/* test 1 reason=Too Big  ""*/
val t1 = Raw_Import_Returns("ext-id", "email","10","EU",1,"Too Big",-1)
val t1tr = Return_Reason(t1)
println(t1tr)


/* test 2 reason=Too Big  ""*/
val t2 = Raw_Import_Returns("ext-id", "email","10","EU",1,"Shape",-1)
val t2tr = Return_Reason(t2)
println(t2tr)

/* test 3 reason=Too Big  ""*/
val t3 = Raw_Import_Returns("ext-id", "email","10","EU",1,"",-1)
val t3tr = Return_Reason(t3)
println(t3tr)