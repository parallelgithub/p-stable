import scala.io.Source
import scala.util.Random
import scala.math


object LSH {


	val parameterW = 4.0

	//from http://picomath.org/scala/Erf.scala.html
	//It is correct, can be better...
	def ERFC(x: Double): Double =  {
		val a1: Double =  0.254829592;
		val a2: Double = -0.284496736;
		val a3: Double =  1.421413741;
		val a4: Double = -1.453152027;
		val a5: Double =  1.061405429;
		val p: Double  =  0.3275911;
		// Save the sign of x
		val sign = if (x < 0) -1 else 1
		val absx =  math.abs(x)

		// A&S formula 7.1.26, rational approximation of error function
		val t = 1.0/(1.0 + p*absx);
		val y = 1.0 - (((((a5*t + a4)*t) + a3)*t + a2)*t + a1)*t*math.exp(-x*x);
		sign*y
	}

	//from E2LSH code in SelfTuring.cpp
	//w: bin width
	//c: | vector_x - vector_y | , i.e. distance of 2 point
	def computeFunctionP(w: Double, c: Double):Double = {
		  val x = w / c;
			val M_SQRT2 = math.sqrt(2.0)
			val M_2_SQRTPI = 2.0 / math.sqrt(math.Pi)
			1 - ERFC(x / M_SQRT2) - M_2_SQRTPI / M_SQRT2 / x * (1 - math.exp(-(x*x) / 2));
	}

	def computeLfromKP(k: Int, successProbability: Double):Int = {
		  math.ceil(math.log(1 - successProbability) / math.log(1 - math.pow(computeFunctionP(parameterW, 1.0), k))).toInt;
	}

	def computeK(p2: Double,n: Int): Int = {
		//println(math.log(n))
		//println("p2 = " + p2)
		(math.log(n)/math.log(1.0/p2)).toInt
	}


	def main(args: Array[String]){
		//val baseVector = Source.fromFile("../dataset/sift/siftsmall/test_base").getLines.toList.map{_.split(" ").toIndexedSeq}
		val queryVectors = Source.fromFile("../dataset/sift/siftsmall/query.input").getLines.toList.map{_.split(" ").toIndexedSeq}
		val learnVectors = Source.fromFile("../dataset/sift/siftsmall/learn.input").getLines.toList.map{_.split(" ").map{_.toDouble}.toIndexedSeq}

		val numberOfVectors = learnVectors.length
		val dimention = learnVectors(1).length

		//from the original paper
		val parameterK = computeK(computeFunctionP(parameterW, 1.0), numberOfVectors)
		println("k = " + parameterK)

		//The value will be minus because radius is less than bin width
		//println("p(R=1.0) = computeFunctionP(4.0,1.0) = " + computeFunctionP(4.0,1.0))

		var i = 5.0
		for (a <- 1 to 10){
			println("p(R=" + i + ") = " + computeFunctionP(4.0,i));
			i = i+1.0
		}

		//learnVectors: 25000 * 128 List

		
		var a = 0
		/*
		for (vector <- learnVectors){

			val vectorA = IndexedSeq.fill(dimention)(Random.nextGaussian())
			
			val parameterB = Random.nextDouble() * parameterW

			val dot = (vector zip vectorA).map{case (a,b) => a * b}.foldLeft(0.0)(_+_)
			val hashValue = math.floor((dot + parameterB)/parameterW).toInt
			if(a<20){
			//println("a = " + vectorA)
			//println(hashValue)
			a += 1
			}
		}
		*/

	}
}
