import java.net.Socket

class Popo(tipo:Int){
    var tipo = tipo

    fun hola(cantidad:Int):Boolean{
        var i = 0
        while(i < cantidad){
            println("i = $i")
            i++
        }
        return true
    }

}

fun main(){
    val popo = Popo(20)
    var pipi = 10

    println("Hola Mundo ${popo.tipo}")


    popo.hola(10)

}