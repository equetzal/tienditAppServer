
class compra(idCompra:Int, idCliente:Int, productosComprados:ArrayList<producto_comprado>){
    var idCompra = idCompra
    var idCliente = idCliente
    var productoComprados = productosComprados
    var urlPdf = ""
    var total = 0.0

    //@author github.com/equetzal -> Enya
    init{
        productosComprados.forEach {
            total += it.precioFinalProductos
        }
        urlPdf = generarPdf()
    }

    fun generarPdf() : String{
        return ""
    }

}