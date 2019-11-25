
case class ShoeSize(size_numeric: String, scale: String, retailed_size_numeric: String, retailed_scale: String,
                    gtin: String, sku: String, shoe_length: String, shoe_height: String, heel_height: String,
                    platform_height: String, stock_level: String)
//case class Shoe(
//                 title: String,
//                 description: String,
//                 brand: String,
//                 shoe_category: String,
//                 color: String,
//                 article_number: String,
//                 material: String,
//                 barcode: String,
//                 sizes: Seq[ShoeSize]
//               ) {
//}

case class Shoe(
                 title: String,
                 description: String,
                 brand: String,
                 sizes: Seq[ShoeSize]
               ) {
}


val inter_transfo =
  (str: String) => {val pattern = """\d+(?:\.\d+)?""".r; pattern.findFirstIn(str).getOrElse(str)
  }

val inter_removeKidsShoes =
  (shoe: Shoe) => {
    val newSizes = shoe.sizes.filter(
      size => (size.size_numeric.toDouble >= 34.0)
    )
    if(newSizes.isEmpty) {
      None
    }
    else {
      Some(shoe.copy(sizes = newSizes))
    }
  }

val remove_flip_flop =
  (shoe: Shoe) => {
    val pattern = """Flip Flop""".r
    val modelMatches = pattern.findFirstMatchIn(shoe.title)
    modelMatches match {
      case Some(s) =>
        None
      case None =>
        Some(shoe)
    }
  }

val size_1 = "38 ½ Mondo"
val size_2 = "39 ½ Mondo"
val size1_transfo = inter_transfo(size_1)
val size2_transfo = inter_transfo(size_2)
println("size1_transfo: "+size1_transfo)
println("size2_transfo: "+size2_transfo)

val ss1 = ShoeSize(size1_transfo,"EU","","","","","","","","","")
val ss2 = ShoeSize(size2_transfo,"EU","","","","","","","","","")

val sizesSeq = Seq(ss1,ss2)

val shoe1 = Shoe("FIREFLY Herren Flip Flops Zehensandale Madera M","TECNICA Skisschuhe MACH1 MV 130","TECNICA", sizesSeq)
val shoe1_transfo  = inter_removeKidsShoes(shoe1)
println(shoe1_transfo)

val shoe1_transfo2  = remove_flip_flop(shoe1)

//val shoe1 = Shoe("Longo Komfort","zapatilla adidas","", "<spec> <name>Stil</name><values> <string>Spor terlik</string></values></spec><spec> <name>Renk</name><values> <string>Somon</string><string>Mint</string><string>Ekru</string><string>Pembe</string></values></spec><spec> <name>Numara</name><values> <string>39 - 40</string><string>35 - 36</string><string>37 - 38</string></values></spec><spec> <name>Durum</name><values> <string>Yeni, Kutusunda</string></values></spec><spec> <name>Marka</name><values> <string>A' La</string></values></spec>",sizes1)
//println(shoe1)


//val shoe1 = Shoe("Spangenschuhe Hestia 37","","Brako", "Schuhe und Strümpfe > Schuhe und Stiefel > Spangenschuhe", "Grau","2295266_37","","",sizesSeq)
//
//val newShoe1=CleanSizes(shoe1)
////val newShoe1=RuleToTestfake(shoe1)



// intersport //
//val removeAllKidsShoes =
//(shoe: Shoe) => {
//  if (size.size_numeric.toDouble < 34)
//    None
//  else
//    Some(shoe)
//}
//
//val 	removeAllrollerskates =
//  (shoe: Shoe) => {
//    val size_numeric = size.size_numeric
//    if(size_numeric.indexOf("Mondo") != -1)
//      None
//    else
//      Some(shoe)
//  }
//
//val 	removeAllSlippers =
//  (shoe: Shoe) => {
//    val model = shoe.model
//    if(model.indexOf("Flip Flops ") != -1)
//      None
//    else
//      Some(shoe)
//  }
//

//val transfo =
// (str: String) =>  { val regex = "\d+(?:\.\d+)?".r; regex.findFirstMatchIn(str) match { case Some(s) => { s } case None => { str }}

//val transfo2 =
//(str: String) => {val pattern = """\d+(?:\.\d+)?""".r; pattern.findFirstIn(str).getOrElse("invalid_size")}

//val CleanSizes =
//  (shoe: Shoe) => {
//    Some(shoe.copy(
//      sizes= shoe.sizes.map(size => size.copy(size_numeric = size.size_numeric.replace("EU ",
//        "").replace("\D+\d+\D+\d","")))))
//  }

val RuleToTestfake =
  (shoe: Shoe) => {
    Some(shoe.copy(title = "aaaaa"))
  }


//val CleanupModel =
//    (shoe: Shoe) => {
//    Some(shoe.copy(
//      title = shoe.title
//        .replace(shoe.brand, "")
//        .replace(shoe.shoe_category, "")
//        .replace(shoe.color, "")
//        .replace(shoe.material, "")
//        .replaceAll("""\bGröße\s.+aus\b""", "")
//        .replaceAll("""\b(Damen|Unisex)""", "")
//        .replace("Größe","")
//        .trim()
//    ))
//  }


//val TransformationFunctionForArticleNumber =
//  (shoe: Shoe) => {
//    Some(shoe.copy(article_number=shoe.article_number.substring(0, shoe.article_number.indexOf('_'))))
//  }

val anwr =
(shoe: Shoe) => {
  val newSizes = shoe.sizes.filter(
    size => !(size.scale == "us" || (size.size_numeric.toDouble <= 32.0) || (size.size_numeric.toDouble > 50.0) )
  )
  if(newSizes.isEmpty) {
    None
  }
  else {
    Some(shoe.copy(sizes = newSizes))
  }
}

val testsize =
(shoe: Shoe) => {
  val sizes = shoe.sizes
  val newSizes = sizes.map(size => if(size.size_numeric.toDouble<=11) size.copy(scale = "US") else size)
  Some(shoe.copy(sizes = newSizes))
}



//val ss1 = ShoeSize("EU 36 / UK 3 / US 6","EU","","","","","","","","","")
//val ss2 = ShoeSize("EU 37 / UK 4 / US 7","EU","","","","","","","","","")
//
//val sizes = List(ss1,ss2)
//val sizes1 = List(ShoeSize("","","","","","","","","","",""))
//val sizesSeq = Seq(ss1,ss2)
//
//val ss1transfo = transfo(ss1)


//val shoe1 = Shoe("Longo Komfort","zapatilla adidas","", "<spec> <name>Stil</name><values> <string>Spor terlik</string></values></spec><spec> <name>Renk</name><values> <string>Somon</string><string>Mint</string><string>Ekru</string><string>Pembe</string></values></spec><spec> <name>Numara</name><values> <string>39 - 40</string><string>35 - 36</string><string>37 - 38</string></values></spec><spec> <name>Durum</name><values> <string>Yeni, Kutusunda</string></values></spec><spec> <name>Marka</name><values> <string>A' La</string></values></spec>",sizes1)
//println(shoe1)


//val shoe1 = Shoe("Spangenschuhe Hestia 37","","Brako", "Schuhe und Strümpfe > Schuhe und Stiefel > Spangenschuhe", "Grau","2295266_37","","",sizesSeq)
//
//val newShoe1=CleanSizes(shoe1)
////val newShoe1=RuleToTestfake(shoe1)

//println(newShoe1)

//val newShoe2=TransformationFunctionForArticleNumber(shoe1)
//
//println(newShoe2)
//
//print(shoe1)
//
//val newShoe3=testsize(shoe1)
//
//println(newShoe3)



//val newShoe2=RuleToTest3(shoe1)
//println(newShoe2)

//newShoe1 match {

//  case Some(s) => {
//    print(s.sizes)
//  }
//}

//println(newShoe1.getClass)
//
//val shoe2 = Shoe("Skechers","zapatilla adidas","", "    <specs>\n        <spec>\n            <name>Desen</name>\n            <values>\n                <string>Desensiz</string>\n            </values>\n        </spec>\n        <spec>\n            <name>Menşei</name>\n            <values>\n                <string>Diğer</string>\n            </values>\n        </spec>\n        <spec>\n            <name>Renk</name>\n            <values>\n                <string>Kahverengi Koyu</string>\n            </values>\n        </spec>\n        <spec>\n            <name>Numara</name>\n            <values>\n                <string>45.5</string>\n                <string>44</string>\n                <string>46</string>\n                <string>42.5</string>\n                <string>43.5</string>\n                <string>42</string>\n                <string>44.5</string>\n            </values>\n        </spec>\n        <spec>\n            <name>Malzeme</name>\n            <values>\n                <string>Gore-Tex</string>\n            </values>\n        </spec>",sizes1)
//println(shoe2)
//val newShoe2=RuleToTest1(shoe2)
//println(newShoe2)
//newShoe2 match {
//  case Some(s) => {
//    print(s.sizes)
//  }
//}
//println(newShoe2)
//
//
//val shoe3 = Shoe("Longo Komfort","zapatilla adidas","","<spec> <name>Stil</name><values> <string>Spor terlik</string></values></spec><spec> <name>Renk</name><values> <string>Somon</string><string>Mint</string><string>Ekru</string><string>Pembe</string></values></spec><spec> <name>Numara</name><values> <string>39 - 40</string><string>35 - 36</string><string>37 - 38</string></values></spec><spec> <name>Durum</name><values> <string>Yeni, Kutusunda</string></values></spec><spec> <name>Marka</name><values> <string>A' La</string></values></spec>",sizes1)
//println(shoe3)
//val newShoe3=RuleToTest1(shoe1)
//newShoe3 match {
//  case Some(s) => {
//    print(s.sizes)
//  }
//}
//println(newShoe3)
//

//
//val sizeStringPattern = """<name>Numara<\/name>\s?<values>\s?((<string>([^<]+)<\/string>)+)\s?<\/values>""".r
//val sizeMatches = sizeStringPattern.findFirstMatchIn(shoe1.internal_material)
//var sizesString = ""
//sizeMatches match {
//  case Some(s) => {
//    sizesString = s.group(1)
//  }
//  case None => {
//    sizesString = ""
//  }
//}
//val sizesPattern = """<string>([^<]+)<\/string>""".r
//val sizesMatches = sizesPattern.findAllIn(sizesString).group(1)
//var lista : List[ShoeSize]= Nil
//
//for (s <- sizesPattern findAllIn sizesString) {
////  println(s)
//  val Decimal = """(-)?(\d+)(\.\d*)?""".r
//  for (s2 <- Decimal findAllIn s) {
//    val shoeSize = ShoeSize(s2,"eu","","")
//    lista=shoeSize::lista
//    }
//  println(lista)
//}
//shoe1.copy(sizes = lista.toSeq)
//println(shoe1)

//while (sizesMatches.hasNext) {
//  val d = sizesMatches.group(1)
//  println(s"$d")
//}
//sizeMatches match {
//  case Some(s) => {
//    sizesString = s.toString()
//  }
//  case None => {
//    sizesString = ""
//  }
//}
//println(sizesString)



//val sizesSeq: List[ShoeSize] = Nil
//
//while (sizesMatches.hasNext) {
//  val d = sizesMatches.next
//  val ss1 = ShoeSize(d.toString(),"eu","","")
//  sizesSeq = sizesSeq: +ss1
//  println(ss1)
//  //if (sizeMatches.group(1).toInt < 32) println(s"$d: An oldie but goodie.")
//}
//println(sizesSeq)
// val colors = colorsMatches.map(colorMatch => colorMatch.group(1)) mkString ", "
//val sizesArray = sizesMatches.map(sizesMatch => sizesMatch.group(1))

//print(sizesArray)
//   sizesMatches.foreach { m =>
//   println("CC=" + m.group(1) + "AC=" + m.group(2) + "Number=" + m.group(3))

