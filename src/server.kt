import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.*
import java.net.ServerSocket
import java.net.SocketException

class server {
    var db = database()
    val serverSocket = ServerSocket(12345)

    init{
        db = loadDatabase()
    }

    fun start(){
        try{
            println("Esperando Clientes...")
            println("Escuchando peticiones desde ${serverSocket.localSocketAddress}")

            while(true){
                val socket = serverSocket.accept()
                println("Se ha establecido una conexión desde ${socket.inetAddress}:${socket.port}")

                val dataInputStream = DataInputStream(socket.getInputStream())
                val dataOutputStream = DataOutputStream(socket.getOutputStream())
                val bytes = ByteArray(1024)

                var json = dataInputStream.readUTF()
                val request = Gson().fromJson(json, jsonRequest::class.java)
                val response = jsonResponse()
                println("Request -> $json")


                // del 12 al 49 por si mandan mas Json
                if(request.operationId in (0..49)){
                    when(request.operationId){
                        0 -> { //Ping para comprobar comunicacion
                            response.pingOk = true
                        }

                        1 -> { //Agregar Cliente
                            response.clientId = db.addClient(request.clientName)
                        }

                        2 -> { //Loggear Client
                            response.isLoginOk = db.isClient(request.clientId)
                            if(response.isLoginOk!!)
                                response.clientName = db.clientes[request.clientId]!!.nombre
                        }

                        3 -> { //Mandar lista de Productos
                            response.productList = db.getProductsList()
                        }

                        4 -> { //Añadir Producto
                            val mimeType = dataInputStream.readUTF()
                            val file = "./files/products/${request.productId}.$mimeType"
                            val size:Long = dataInputStream.readLong()

                            val outputFile = DataOutputStream(FileOutputStream(file))
                            var received:Long = 0
                            var segment:Int = 0
                            println("Reading file $file \nSize = $size Bytes")
                            while (received < size){
                                segment = dataInputStream.read(bytes)
                                outputFile.write(bytes, 0, segment)
                                outputFile.flush()
                                received += segment
                                println("Received $received of $size Bytes")
                            }
                            println("File received successfully")
                            outputFile.close()

                            response.productId = db.addProduct(request.productName, request.productSku, file, request.productPrice, request.productAmount)
                        }

                        5 -> { //Quitar Producto
                            response.productRemoveSuccessful = db.removeProduct(request.productId)
                        }

                        6 -> { //Actualiza Producto
                            response.productUpdateSuccessful = db.updateProduct(request.productId, request.productName, request.productPrice, request.productAmount)
                        }

                        7 -> { //Realizar Compra
                            response.purchaseId = db.newPurchase(request.clientId, request.cartList)
                        }

                        8 -> { //Añadir al Carrito
                            response.isCartUpdateSuccessful = db.addToCart(request.clientId, request.productId, request.productAmount)
                        }

                        9 -> { //Quitar del Carrito
                            response.isCartUpdateSuccessful = db.removeFromCart(request.clientId, request.productId, request.productAmount)
                        }

                        10 -> { //Reemplazar Carrito
                            response.isCartUpdateSuccessful = db.saveCart(request.clientId, request.cartList)
                        }

                        11 -> { //Obtener Carrito
                            response.cartList = db.getCart(request.clientId)
                        }
                    }
                    json = Gson().toJson(response)
                    dataOutputStream.writeUTF(json)
                    println("Response -> $json")
                }

                // del 50 al 99 para envío de archivos
                if(request.operationId in (50..99)){
                    when(request.operationId){
                        50 -> { //Obtener Imagen de Producto
                            response.isProductImageAvailable = true
                            var path:String? = null
                            var file:File? = null
                            var inputFile:DataInputStream? = null
                            try{
                                path = db.productos[request.productId]?.imgPath
                                file = File(path)
                                inputFile = DataInputStream(FileInputStream(file))
                            }catch (e:java.lang.Exception){
                                response.isProductImageAvailable = false
                            }
                            json = Gson().toJson(response)
                            dataOutputStream.writeUTF(json)
                            println("Response -> $json")

                            if(inputFile != null && file != null){
                                dataOutputStream.writeUTF(file.name)
                                dataOutputStream.flush()
                                dataOutputStream.writeLong(file.length())
                                dataOutputStream.flush()

                                val size:Long = file.length()
                                var sent:Long = 0
                                var segment:Int = 0
                                println("Sending file ${file.name} \nSize = $size Bytes")
                                while(sent < size){
                                    segment = inputFile.read(bytes)
                                    dataOutputStream.write(bytes, 0, segment)
                                    dataOutputStream.flush()
                                    sent += segment
                                    println("Sent $sent Bytes of $size Bytes")
                                }
                                inputFile.close()
                            }
                        }

                        51 -> { //Obtener PDF del recibo
                            response.isPurchasePDFAvailable = true
                            var path:String? = null
                            var file:File? = null
                            var inputFile:DataInputStream? = null
                            try{
                                path = db.compras[request.purchaseId]?.urlPdf
                                file = File(path)
                                inputFile = DataInputStream(FileInputStream(file))
                            }catch (e:java.lang.Exception){
                                response.isPurchasePDFAvailable = false
                            }
                            json = Gson().toJson(response)
                            dataOutputStream.writeUTF(json)
                            println("Response -> $json")

                            if(inputFile != null && file != null){
                                dataOutputStream.writeUTF(file.name)
                                dataOutputStream.flush()
                                dataOutputStream.writeLong(file.length())
                                dataOutputStream.flush()

                                val size:Long = file.length()
                                var sent:Long = 0
                                var segment:Int = 0
                                println("Sending file ${file.name} \nSize = $size Bytes")
                                while(sent < size){
                                    segment = inputFile.read(bytes)
                                    dataOutputStream.write(bytes, 0, segment)
                                    dataOutputStream.flush()
                                    sent += segment
                                    println("Sent $sent Bytes of $size Bytes")
                                }
                                inputFile.close()
                            }
                        }
                    }
                }

                println("Se ha cerrado la conexión de ${socket.inetAddress}:${socket.port}")
                dataOutputStream.close()
                dataInputStream.close()
                socket.close()
            }
        }catch (e:SocketException){
          println("El servidor se ha cerrado")
        } catch (e:Exception){
            e.printStackTrace()
            println("Chin! Ocurrió un error quetzalliano!")
            start()
        }
        return
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