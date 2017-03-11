package com.yotaichino.gitbucket.plugins.plantuml

import java.io.ByteArrayOutputStream
import net.sourceforge.plantuml.FileFormat
import net.sourceforge.plantuml.FileFormatOption
import net.sourceforge.plantuml.SourceStringReader

object PlantUMLUtils {

  def generateSVGImage(source: String): Array[Byte] = generateImage(source, FileFormat.SVG)

  private def generateImage(source: String, format: FileFormat): Array[Byte] = {
    val reader = new SourceStringReader(source)
    val os = new ByteArrayOutputStream()

    reader.generateImage(os, new FileFormatOption(format))
    os.close()

    os.toByteArray()
  }
}