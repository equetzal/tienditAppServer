import java.lang.Exception

class database {
    var idClienteMax = 0
    var idProductoMax = 0
    var idCompraMax = 0
    var compras = mutableMapOf<Int, compra>()
    var productos = mutableMapOf<Int, producto>()
    var clientes = mutableMapOf<Int, cliente>()

    //@author github.com/equetzal -> Enya
    fun addClient(name:String) : Int{
        val newClient = cliente(idClienteMax, name)
        clientes.put(idClienteMax, newClient)
        return idClienteMax++
    }

    //@author github.com/equetzal -> Enya
    fun isClient(idClient: Int) : Boolean{
        return clientes.containsKey(idClient)
    }

    //@author github.com/equetzal -> Enya
    fun addProduct(name:String, sku:String, imgPath:String, price:Double, amount:Int) : Int{
        val newProduct = producto(idProductoMax, name, sku, imgPath, price, amount)
        productos.put(idProductoMax, newProduct)
        return idProductoMax++
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
    fun newPurchase(idClient:Int, products:Map<Int,Int>) : Int{
        var purchaseDetails = ArrayList<producto_comprado>()
        products.forEach{
            println("Key= ${it.key}")
            if(products.containsKey(it.key) && productos[it.key]!!.cantidadDisponible >= it.value){
                val newPurchasedProduct = producto_comprado(it.key, it.value, productos[it.key]!!.precio)
                purchaseDetails.add(newPurchasedProduct)
            }else
                return -1
        }
        purchaseDetails.forEach {
            productos[it.idProducto]!!.cantidadDisponible -= it.cantidadProducto
        }

        return try {
            val newPurchase = compra(idCompraMax, idClient, purchaseDetails)
            compras[idCompraMax] = newPurchase
            clientes[idClient]!!.compras.add(idCompraMax)
            idCompraMax++
        }catch (e:Exception){
            e.printStackTrace()
            -1
        }

    }

    //@author github.com/equetzal -> Enya
    fun saveCart(idClient:Int, products: Map<Int, Int>) : Boolean{
        return if(clientes.containsKey(idClient)){
            clientes[idClient]!!.carrito = products as MutableMap<Int, Int>
            true
        }else
            false
    }

    fun addToCart(idClient: Int, productId: Int, amount: Int) : Boolean{
        return if(clientes.containsKey(idClient) && productos.containsKey(productId)){
            if(clientes[idClient]!!.carrito.containsKey(productId))
                clientes[idClient]!!.carrito[productId]!!.plus(amount)
            else
                clientes[idClient]!!.carrito.put(productId, amount)
            true
        }else
            false
    }

    fun removeFromCart(idClient: Int, productId: Int, amount: Int) : Boolean{
        return if(clientes.containsKey(idClient) && productos.containsKey(productId)){
            if(clientes[idClient]!!.carrito.containsKey(productId)){
                clientes[idClient]!!.carrito[productId]!!.minus(amount)
                true
            }
            false
        }else
            false
    }

    //@author github.com/equetzal -> Enya
    fun getCart(idClient: Int): Map<Int, Int> {
        var ans = mapOf<Int,Int>()
        if(clientes.containsKey(idClient))
            ans = clientes[idClient]!!.carrito
        return ans
    }

}