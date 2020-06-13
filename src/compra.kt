import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import java.io.FileOutputStream
import java.util.*


class compra(idCompra:Int, idCliente:Int, productosComprados:ArrayList<producto_comprado>){
    var idCompra = idCompra
    var idCliente = idCliente
    var productoComprados = productosComprados
    var urlPdf = ""
    var total = 0.0
    private val catFont: Font = Font(Font.FontFamily.TIMES_ROMAN, 18f,
            Font.BOLD)
    private val subFont: Font = Font(Font.FontFamily.TIMES_ROMAN, 16f,
            Font.NORMAL)
    private val smallBold: Font = Font(Font.FontFamily.TIMES_ROMAN, 12f,
            Font.BOLD)
    private  val normFont: Font = Font(Font.FontFamily.TIMES_ROMAN, 14f, Font.NORMAL)
    //@author github.com/equetzal -> Enya
    init{
        productosComprados.forEach {
            total += it.precioFinalProductos
        }
        urlPdf = generarPdf()
    }

    private fun addEmptyLine(paragraph: Paragraph, number: Int) {
        for (i in 0 until number) {
            paragraph.add(Paragraph(" "))
        }
    }

    @Throws(DocumentException::class)
    private fun addTitlePage(document: Document) {
        val preface = Paragraph()
        // We add one empty line
        addEmptyLine(preface, 1)
        // Lets write a big header
        preface.add(Paragraph("Recibo de compra: $idCompra", catFont))
        addEmptyLine(preface, 1)
        // Will create: Report generated by: _name, _date
        preface.add(Paragraph(
                "Cliente número: $idCliente" + ",   Fecha: " + Date(),  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                smallBold))
        addEmptyLine(preface, 2)
        document.add(preface)
    }

    @Throws(BadElementException::class)
    private fun createTable(document: Document,producto: producto_comprado) {
        val table = PdfPTable(2)

        // t.setBorderColor(BaseColor.GRAY);
        // t.setPadding(4);
        // t.setSpacing(4);
        // t.setBorderWidth(1);
        var c1 = PdfPCell(Phrase("Detalles"))
        c1.horizontalAlignment = Element.ALIGN_CENTER
        table.addCell(c1)
        c1 = PdfPCell(Phrase(""))
        c1.horizontalAlignment = Element.ALIGN_CENTER
        table.addCell(c1)
        table.headerRows = 1
        table.addCell("ID producto")
        table.addCell(producto.idProducto.toString())
        table.addCell("Cantidad")
        table.addCell(producto.cantidadProducto.toString())
        table.addCell("Precio unitario")
        table.addCell(producto.precioUnitario.toString())
        table.addCell("Subtotal")
        table.addCell(producto.precioFinalProductos.toString())
        table.addCell("2.3")
        document.add(table)
    }

    fun generarPdf() : String{
        val path = "./files/receipts/"
        val file = "$path$idCompra.pdf"
        val document = Document()
        PdfWriter.getInstance(document, FileOutputStream(file))

        document.open()
        addTitlePage(document)
        var paragraph = Paragraph()
        addEmptyLine(paragraph,2)
        productoComprados.forEach{
            createTable(document,it)
            document.add(paragraph)
        }
        paragraph.add(Paragraph("Total: $total", smallBold))
        document.add(paragraph)
        document.close()
        return file
    }


}