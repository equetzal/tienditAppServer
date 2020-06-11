class producto_comprado(idProducto:Int, cantidadProducto:Int, precioUnitario:Double) {
    var idProducto = idProducto
    var cantidadProducto = cantidadProducto
    var precioUnitario = precioUnitario
    var precioFinalProductos = 0.0

    //@author github.com/equetzal -> Enya
    init {
        precioFinalProductos = precioUnitario*cantidadProducto
    }

}