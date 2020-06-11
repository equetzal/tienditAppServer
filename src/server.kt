import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.File
import java.io.FileOutputStream
import java.net.ServerSocket

class server {
    var db = database()

    init{
        db = loadDatabase()
    }

    fun start(){
        try{
            val serverSocket = ServerSocket(12345)
            println("Esperando Clientes...")
            println("Escuchando peticiones desde ${serverSocket.localSocketAddress}")

            while(true){
                val socket = serverSocket.accept()
                println("Se ha establecido una conexión desde ${socket.inetAddress}:${socket.port}")

                val dataInputStream = DataInputStream(socket.getInputStream())
                val dataOutputStream = DataOutputStream(socket.getOutputStream())
                val bytes = ByteArray(1024)

                val request = Gson().fromJson(dataInputStream.readUTF(), jsonRequets::class.java)

                when(request.operationId){
                    1 -> { //Agregar Cliente
                        dataOutputStream.writeInt(db.addClient(request.clientName))
                    }

                    2 -> { //Loggear Client
                        dataOutputStream.writeBoolean(db.isClient(request.clientId))
                    }

                    3 -> { //Mandar lista de Productos

                    }
                }

                dataOutputStream.close()
                dataInputStream.close()
                socket.close()

                /*
                val file = "C:/equetzal/" + dataInputStream.readUTF()
                val size = dataInputStream.readLong()
                val dataOutputStream = DataOutputStream(FileOutputStream(file))

                println("Writting File on $file")

                var received:Long = 0
                var n:Int
                var percentage:Int
                while(received < size){
                    n = dataInputStream.read(bytes)
                    dataOutputStream.write(bytes, 0, n)
                    dataOutputStream.flush()
                    received += n
                    percentage = (received*100/size).toInt()
                    println("Leido $percentage%")
                }
                println("Archivo Leido Completamente")

                dataOutputStream.close()
                dataInputStream.close()
                socket.close()

                deserializarGustoQuetzalliano(file)*/
            }
        }catch (e:Exception){
            e.printStackTrace()
            println("Chin! Ocurrió un error quetzalliano!")
        }
    }

    private fun deserializarGustoQuetzalliano(file:String){
        val jsonString = File(file).readText(Charsets.UTF_8)
        println("Json File:")
        println(jsonString)
        var gustoQuetzalliano = Gson().fromJson(jsonString, GustoQuetzalliano::class.java)

        println("Mi gusto Quetzalliano ha llegado!!, Esto es lo que contiene:")
        println("Nombre del Gusto: ${gustoQuetzalliano?.nombreDelGusto}")
        println("Tipo de Gusto: ${gustoQuetzalliano?.tipoDeGusto}")
        println("Nivel de Felicidad: ${gustoQuetzalliano?.nivelDeFelicidad}")
        println("Nivel de Musicalidad: ${gustoQuetzalliano?.nivelDeMusicalidad}")
        println("Es un gusto de Kaskade?: ${gustoQuetzalliano?.esKaskade}")
        println("Mensaje recibido: ${gustoQuetzalliano?.mensaje}")
    }

    //@author github.com/equetzal -> Enya
    fun loadDatabase():database{
        val path = "./db.json"
        val jsonDB = File(path).readText(Charsets.UTF_8)

        return Gson().fromJson(jsonDB, database::class.java)
    }

    //@author github.com/equetzal -> Enya
    fun dumpDatabase(){
        val path = "./db.json"
        val output = DataOutputStream(FileOutputStream(path))
        val gson = GsonBuilder().setPrettyPrinting().create()
        val jsonDB = gson.toJson(db)

        output.writeBytes(jsonDB)
        output.close()
    }

}