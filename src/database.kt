import java.lang.Exception

class database {
    var idClienteMax = 0
    var idProductoMax = 0
    var idCompraMax = 0
    var compras = mutableMapOf<Int, compra>()
    var productos = mutableMapOf<Int, producto>()
    var clientes = mutableMapOf<Int, cliente>()

    //Ops

    fun addClient(name:String){
        val newClient = cliente(idClienteMax, name)
        clientes.put(idClienteMax, newClient)
        idClienteMax++
    }

    fun addProduct(name:String, sku:String, imgPath:String, price:Double, amount:Int){
        val newProduct = producto(idProductoMax, name, sku, imgPath, price, amount)
        productos.put(idProductoMax, newProduct)
        idProductoMax++
    }

    fun newPurchase(idClient:Int, products:Map<Int,Int>) : Boolean{
        var purchaseDetails = ArrayList<producto_comprado>()
        products.forEach{
            println("Key= ${it.key}")
            if(products.containsKey(it.key) && productos[it.key]!!.cantidadDisponible >= it.value){
                val newPurchasedProduct = producto_comprado(it.key, it.value, productos[it.key]!!.precio)
                purchaseDetails.add(newPurchasedProduct)
            }else{
                return false
            }
        }
        purchaseDetails.forEach {
            productos[it.idProducto]!!.cantidadDisponible -= it.cantidadProducto
        }

        try {
            val newPurchase = compra(idCompraMax, idClient, purchaseDetails)
            compras.put(idCompraMax, newPurchase)
            clientes[idClient]!!.compras.add(idCompraMax)
            idCompraMax++
        }catch (e:Exception){
            e.printStackTrace()
            return false
        }

        return true
    }

}