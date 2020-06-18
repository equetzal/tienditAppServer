import com.google.gson.Gson
import com.google.gson.GsonBuilder

fun main() {
    val servidor = server()

    //servidor.db.newPurchase(0, mapOf(0 to 1, 1 to 1))

    var serverThread = Thread{
        run {
            println("Starting Server")
            servidor.start()
            return@run
        }
        return@Thread
    }

    serverThread.start()

    var line:String?
    while(true){
        line = readLine()!!
        if(line == "exit"){
            servidor.serverSocket.close()
            servidor.dumpDatabase()
            serverThread.stop()
            println("Cerrando Servidor.\nExportando base de datos.")
            break
        }else{
            println("Comando no reconocido: $line")
        }
    }

}

