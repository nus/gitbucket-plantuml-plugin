package com.yotaichino.gitbucket.plugins.plantuml

import gitbucket.core.plugin.Renderer
import gitbucket.core.plugin.RenderRequest
import gitbucket.core.util.StringUtil
import java.nio.charset.Charset
import play.twirl.api.Html

class PlantUMLRenderer extends Renderer {

  override def render(request: RenderRequest): Html = {
    val owner = request.repository.owner
    val name = request.repository.name
    val branch = request.branch
    val filePath = request.filePath.mkString("/")
    Html(toHtml(owner, name, branch, filePath))
  }

  def toHtml(owner: String, name: String, branch: String, filePath: String): String = {
    val pumlPath = StringUtil.escapeHtml(s"/$owner/$name/plantuml/$branch/$filePath")
    s"""<img src="$pumlPath">"""
  }
}
