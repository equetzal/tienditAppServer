
class cliente (id:Int, nombre:String){
    val idCliente = id
    val nombre = nombre
    val compras = mutableSetOf<Int>()
    val carrito = mutableMapOf<Int,Int>()
}