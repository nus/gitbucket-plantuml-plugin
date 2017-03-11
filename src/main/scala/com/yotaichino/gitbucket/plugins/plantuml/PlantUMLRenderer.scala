package com.yotaichino.gitbucket.plugins.plantuml

import gitbucket.core.plugin.Renderer
import gitbucket.core.plugin.RenderRequest
import java.nio.charset.Charset
import play.twirl.api.Html

class PlantUMLRenderer extends Renderer {

  override def render(request: RenderRequest): Html = {
    Html(toHtml(request.fileContent))
  }

  def toHtml(fileContent: String): String = {
    val svg = new String(PlantUMLUtils.generateSVGImage(fileContent), Charset.forName("UTF-8"));

    """<object type="image/svg+xml" data="plantuml.svg">""" + svg + "</object>"
  }
}
