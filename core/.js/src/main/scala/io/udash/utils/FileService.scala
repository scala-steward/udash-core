package io.udash.utils

import com.avsystem.commons.misc.AbstractCase

import java.io.IOException
import org.scalajs.dom._
import org.scalajs.dom.raw.Blob

import scala.scalajs.js
import scala.concurrent.{Future, Promise}
import scala.scalajs.js.annotation.JSGlobal
import scala.scalajs.js.typedarray.ArrayBuffer
import scala.util.Try

@js.native
@JSGlobal
private[utils] final class FileReaderSync() extends js.Object {
  def readAsArrayBuffer(blob: Blob): ArrayBuffer = js.native
}

final case class CloseableUrl(value: String) extends AbstractCase with AutoCloseable {
  override def close(): Unit = {
    URL.revokeObjectURL(value)
  }
}

object FileService {

  final val OctetStreamType = "application/octet-stream"

  /**
   *  Converts specified bytes arrays to string that contains URL
   *  that representing the array given in the parameter with specified mime-type.
   *
   *  Keep in mind that returned URL should be closed.
   */
  def createURL(bytesArrays: Seq[Array[Byte]], mimeType: String): CloseableUrl = {
    import js.typedarray._

    val jsBytesArrays = js.Array[js.Any](bytesArrays.map(_.toTypedArray) :_ *)
    val blob = new Blob(jsBytesArrays, BlobPropertyBag(mimeType))
    CloseableUrl(URL.createObjectURL(blob))
  }

  /**
   *  Converts specified bytes arrays to string that contains URL
   *  that representing the array given in the parameter with `application/octet-stream` mime-type.
   *
   *  Keep in mind that returned URL should be closed.
   */
  def createURL(bytesArrays: Seq[Array[Byte]]): CloseableUrl =
    createURL(bytesArrays, OctetStreamType)

  /**
   *  Converts specified bytes array to string that contains URL
   *  that representing the array given in the parameter with specified mime-type.
   *
   *  Keep in mind that returned URL should be closed.
   */
  def createURL(byteArray: Array[Byte], mimeType: String): CloseableUrl =
    createURL(Seq(byteArray), mimeType)

  /**
   *  Converts specified bytes array to string that contains URL
   *  that representing the array given in the parameter with `application/octet-stream` mime-type.
   *
   *  Keep in mind that returned URL should be closed.
   */
  def createURL(byteArray: Array[Byte]): CloseableUrl =
    createURL(Seq(byteArray), OctetStreamType)

  /**
   * Asynchronously convert specified part of file to bytes array.
   */
  def asBytesArray(file: File, start: Double, end: Double): Future[Array[Byte]] = {
    import js.typedarray._

    val fileReader = new FileReader()
    val promise = Promise[Array[Byte]]()

    fileReader.onerror = (e: Event) =>
      promise.failure(new IOException(e.toString))

    fileReader.onabort = (e: Event) =>
      promise.failure(new IOException(e.toString))

    fileReader.onload = (_: UIEvent) =>
      promise.complete(Try(
        new Int8Array(fileReader.result.asInstanceOf[ArrayBuffer]).toArray
      ))

    val slice = file.slice(start, end)
    fileReader.readAsArrayBuffer(slice)

    promise.future
  }

  /**
   * Asynchronously convert specified file to bytes array.
   */
  def asBytesArray(file: File): Future[Array[Byte]] =
    asBytesArray(file, 0, file.size)

  /**
   * Synchronously convert specified part of file to bytes array.
   *
   * Because it is using synchronous I/O this API can be used only inside worker.
   *
   * This method is using FileReaderSync that is part of Working Draft File API.
   * Anyway it is supported for majority of modern browsers
   */
  def asBytesArraySync(file: File, start: Double, end: Double): Array[Byte] = {
    import js.typedarray._

    val fileReaderSync = new FileReaderSync()
    val slice = file.slice(start, end)

    val int8Array = new Int8Array(fileReaderSync.readAsArrayBuffer(slice))

    int8Array.toArray
  }

  /**
   * Synchronously convert file to bytes array.
   *
   * Because it is using synchronous I/O this API can be used only inside worker.
   *
   * This method is using FileReaderSync that is part of Working Draft File API.
   * Anyway it is supported for majority of modern browsers
   */
  def asBytesArraySync(file: File): Array[Byte] =
    asBytesArraySync(file, 0, file.size)
}
