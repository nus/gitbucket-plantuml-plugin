package com.yotaichino.gitbucket.plugins.plantuml

import gitbucket.core.plugin.Renderer
import gitbucket.core.plugin.RenderRequest
import gitbucket.core.util.StringUtil
import java.nio.charset.Charset
import java.util.Base64
import play.twirl.api.Html

class PlantUMLRenderer extends Renderer {

  override def render(request: RenderRequest): Html = {
    val owner = request.repository.owner
    val name = request.repository.name
    val preview = s"/$owner/$name/_preview".equals(request.context.currentPath)

    if (preview) {
      Html(imgEmbedded(request.fileContent))
    } else {
      val branch = request.branch
      val filePath = request.filePath.mkString("/")
      Html(imgLink(owner, name, branch, filePath))
    }
  }

  def imgEmbedded(content: String): String = {
    val raw = PlantUMLUtils.generateSVGImage(content)
    val src = Base64.getEncoder.encodeToString(raw)
    s"""<img src="data:image/svg+xml;base64,$src">"""
  }

  def imgLink(owner: String, name: String, branch: String, filePath: String): String = {
    val pumlPath = StringUtil.escapeHtml(s"/$owner/$name/plantuml/$branch/$filePath")
    s"""<img src="$pumlPath">"""
  }
}
