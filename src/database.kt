import java.lang.Exception

class database {
    var idClienteMax = 0
    var idProductoMax = 0
    var idCompraMax = 0
    var compras = mutableMapOf<Int, compra>()
    var productos = mutableMapOf<Int, producto>()
    var clientes = mutableMapOf<Int, cliente>()

    //@author github.com/equetzal -> Enya
    fun addClient(name:String){
        val newClient = cliente(idClienteMax, name)
        clientes.put(idClienteMax, newClient)
        idClienteMax++
    }

    //@author github.com/equetzal -> Enya
    fun addProduct(name:String, sku:String, imgPath:String, price:Double, amount:Int){
        val newProduct = producto(idProductoMax, name, sku, imgPath, price, amount)
        productos.put(idProductoMax, newProduct)
        idProductoMax++
    }

    //@author github.com/equetzal -> Enya
    fun removeProduct(idProduct:Int) : Boolean{
        return if(productos.containsKey(idProduct)){
            productos.remove(idProduct)
            true
        }else{
            false
        }
    }

    //@author github.com/equetzal -> Enya
    fun updateProduct(idProduct:Int, name:String, price:Double, amount:Int) : Boolean{
        return if(productos.containsKey(idProduct)){
            productos[idProduct]!!.nombre = name
            productos[idProduct]!!.precio = price
            productos[idProduct]!!.cantidadDisponible = amount
            true
        }else
            false
    }

    //@author github.com/equetzal -> Enya
    fun getProductsList() : ArrayList<producto>{
        val productsList = ArrayList<producto>()
        productos.forEach{
            productsList.add(it.value)
        }
        return productsList
    }

    //@author github.com/equetzal -> Enya
    fun newPurchase(idClient:Int, products:Map<Int,Int>) : Boolean{
        var purchaseDetails = ArrayList<producto_comprado>()
        products.forEach{
            println("Key= ${it.key}")
            if(products.containsKey(it.key) && productos[it.key]!!.cantidadDisponible >= it.value){
                val newPurchasedProduct = producto_comprado(it.key, it.value, productos[it.key]!!.precio)
                purchaseDetails.add(newPurchasedProduct)
            }else
                return false
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

    //@author github.com/equetzal -> Enya
    fun saveCart(idClient:Int, products: Map<Int, Int>) : Boolean{
        return if(clientes.containsKey(idClient)){
            clientes[idClient]!!.carrito = products as MutableMap<Int, Int>
            true
        }else
            false
    }

    fun getCart(idClient: Int): Map<Int, Int> {
        var ans = mapOf<Int,Int>()
        if(clientes.containsKey(idClient))
            ans = clientes[idClient]!!.carrito
        return ans
    }

}