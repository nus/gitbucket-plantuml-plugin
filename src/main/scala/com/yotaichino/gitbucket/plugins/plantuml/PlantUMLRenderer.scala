package com.yotaichino.gitbucket.plugins.plantuml

import gitbucket.core.plugin.Renderer
import gitbucket.core.plugin.RenderRequest
import java.io.ByteArrayOutputStream
import java.nio.charset.Charset
import net.sourceforge.plantuml.FileFormat
import net.sourceforge.plantuml.FileFormatOption
import net.sourceforge.plantuml.SourceStringReader
import play.twirl.api.Html

class PlantUMLRenderer extends Renderer {

  override def render(request: RenderRequest): Html = {
    Html(toHtml(request.fileContent))
  }

  def toHtml(fileContent: String): String = {
    val reader = new SourceStringReader(fileContent);
    val os = new ByteArrayOutputStream();

    val desc = reader.generateImage(os, new FileFormatOption(FileFormat.SVG));
    os.close();
    val svg = new String(os.toByteArray(), Charset.forName("UTF-8"));

    """<object type="image/svg+xml" data="plantuml.svg">""" + svg + "</object>"
  }
}
