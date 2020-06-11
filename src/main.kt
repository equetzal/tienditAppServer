import com.google.gson.Gson
import com.google.gson.GsonBuilder

fun main(){
    val servidor = server()

    //servidor.db.addProduct("Cubrebocas", "QUETZ-3285TP", "./files/products/cubrebocas.png", 2300.0, 136)
    //servidor.db.newPurchase(0, mapOf(0 to 100, 1 to 5))
    //servidor.db.removeProduct(1)
    //servidor.db.updateProduct(3, "Tamal de Mole", 15.5, 67)

    servidor.db.saveCart(0, mapOf(3 to 10, 2 to 1, 0 to 1))

    val ans = servidor.db.getCart(0)
    val json = (GsonBuilder().setPrettyPrinting().create()).toJson(ans)
    println("Product List= $json")

    servidor.dumpDatabase()

}