case class Shoe(
                 title: String,
                 description: String,
//                 brand: String,
//                 shoe_category: String,
//                 color: String,
//                 article_number: String,
//                 material: String,
                 manufacturer_id: String
//                 sizes: Seq[ShoeSize]
               ) {
}

def isNullOrEmpty[T](s: Seq[T]) = s match {
  case null => true
  case Seq() => true
  case _ => false
}

val s = Shoe("FIREFLY Herren Flip Flops Zehensandale Madera M","TECNICA Skisschuhe MACH1 MV 130",null)


val len= s.manufacturer_id match {
  case null => 0
  case _ => s.manufacturer_id.length()
}

println(len)

val len2 = if (isNullOrEmpty(s.manufacturer_id)) 0 else s.manufacturer_id.length
println(len2)


