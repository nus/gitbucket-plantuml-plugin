package com.yotaichino.gitbucket.plugins.plantuml

import gitbucket.core.plugin.Renderer
import gitbucket.core.plugin.RenderRequest
import gitbucket.core.util.StringUtil
import java.nio.charset.Charset
import java.util.Base64
import play.twirl.api.Html

class PlantUMLRenderer extends Renderer {

  override def render(request: RenderRequest): Html = {
    Html(imgEmbedded(request.fileContent))
  }

  def imgEmbedded(content: String): String = {
    val raw = PlantUMLUtils.generateSVGImage(content)
    val src = Base64.getEncoder.encodeToString(raw)
    s"""<img src="data:image/svg+xml;base64,$src">"""
  }
}
