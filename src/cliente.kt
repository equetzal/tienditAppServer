
class cliente (id:Int, nombre:String){
    val idCliente = id
    val nombre = nombre
    val compras = mutableSetOf<Int>()
    var carrito = mutableMapOf<Int,Int>()
}