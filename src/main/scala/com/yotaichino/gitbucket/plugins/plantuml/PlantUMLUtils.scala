package com.yotaichino.gitbucket.plugins.plantuml

import java.io.ByteArrayOutputStream
import java.io.IOException
import java.lang.NullPointerException
import net.sourceforge.plantuml.FileFormat
import net.sourceforge.plantuml.FileFormatOption
import net.sourceforge.plantuml.SourceStringReader

object PlantUMLUtils {

  def generateSVGImage(source: String): Array[Byte] = generateImage(source, FileFormat.SVG)

  private def generateImage(source: String, format: FileFormat): Array[Byte] = {
    val reader = new SourceStringReader(source)
    val os = new ByteArrayOutputStream()

    try {
      reader.outputImage(os, new FileFormatOption(format)).getDescription()
    } catch {
      case _: IOException => return null
      case _: NullPointerException => return null
    } finally {
      os.close()
    }

    os.toByteArray()
  }
}