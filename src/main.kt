
fun main(){
    val servidor = server()

    //servidor.db.addProduct("Cubrebocas", "QUETZ-3285TP", "./files/products/cubrebocas.png", 2300.0, 136)
    servidor.db.newPurchase(0, mapOf(0 to 100, 1 to 5))

    servidor.dumpDatabase()

}