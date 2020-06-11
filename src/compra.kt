import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfWriter
import java.io.FileOutputStream

class compra(idCompra:Int, idCliente:Int, productosComprados:ArrayList<producto_comprado>){
    var idCompra = idCompra
    var idCliente = idCliente
    var productoComprados = productosComprados
    var urlPdf = ""
    var total = 0.0

    //@author github.com/equetzal -> Enya
    init{
        productosComprados.forEach {
            total += it.precioFinalProductos
        }
        urlPdf = generarPdf()
    }

    fun generarPdf() : String{
        val path = "./files/receipts/"
        val file = "$path$idCompra.pdf"
        val document = Document()
        PdfWriter.getInstance(document, FileOutputStream(file))

        document.open()
        val font: Font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK)
        var chunk: Chunk = Chunk(""+ idCompra, font)
        document.add(chunk)
        chunk = Chunk(""+ idCliente, font)
        document.add(chunk)

        productoComprados.forEach{
            chunk = Chunk("ID producto: "+ it.idProducto, font)
            document.add(chunk)
            chunk = Chunk("Cantidad: "+ it.cantidadProducto, font)
            document.add(chunk)
            chunk = Chunk("Precio unitario"+ it.precioUnitario, font)
            document.add(chunk)
            chunk = Chunk("Subtotal: "+ it.precioFinalProductos, font)
            document.add(chunk)
        }

        document.close()
        return file
    }

}