package com.aisyahpn0033.qrcodegenerator

// Import class Bitmap dari Android
import android.graphics.Bitmap

// Import untuk mengubah warna dari Compose ke ARGB (yang dimengerti oleh Android Bitmap)
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

// Import library ZXing untuk membuat QR Code
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix

// Import ekstensi untuk membuat dan mengatur pixel di Bitmap
import androidx.core.graphics.createBitmap
import androidx.core.graphics.set

// Object singleton bernama QRCodeGenerator
object QRCodeGenerator {

    // Fungsi untuk membuat QR Code dari sebuah string
    fun generateQRCode(content: String, size: Int = 512): Bitmap? {
        return try {
            // Meng-encode string menjadi QR Code dengan ukuran (size x size)
            val bitMatrix: BitMatrix = MultiFormatWriter().encode(
                content,                  // Teks yang akan diubah ke QR
                BarcodeFormat.QR_CODE,   // Format barcode (dalam hal ini QR Code)
                size,                    // Lebar QR Code
                size                     // Tinggi QR Code
            )

            // Membuat objek Bitmap kosong untuk menggambar QR Code
            val bitmap = createBitmap(size, size)

            // Iterasi semua pixel di Bitmap untuk mengatur warnanya berdasarkan data QR
            for (x in 0 until size) {
                for (y in 0 until size) {
                    bitmap[x, y] =
                        if (bitMatrix[x, y]) Color.Black.toArgb()  // Jika true, warnai hitam
                        else Color.White.toArgb()                  // Jika false, warnai putih
                }
            }

            // Mengembalikan QR Code dalam bentuk Bitmap
            bitmap
        } catch (e: Exception) {
            // Jika gagal generate QR Code, tampilkan error dan kembalikan null
            e.printStackTrace()
            null
        }
    }
}
