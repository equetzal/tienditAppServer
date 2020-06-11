class producto_comprado(idProducto:Int, cantidadProducto:Int, precioUnitario:Double) {
    var idProducto = 0
    var cantidadProducto = 0
    var precioUnitario = 0.0
    var precioFinalProductos = 0.0

    init {
        precioFinalProductos = precioUnitario*cantidadProducto
    }

}