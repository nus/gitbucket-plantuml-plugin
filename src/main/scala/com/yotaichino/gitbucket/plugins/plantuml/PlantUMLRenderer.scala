package com.yotaichino.gitbucket.plugins.plantuml

import gitbucket.core.plugin.Renderer
import gitbucket.core.plugin.RenderRequest
import gitbucket.core.util.StringUtil
import java.util.Base64
import play.twirl.api.Html

object PlantUMLRenderer {
  val blobPath: String = "/[^/]+/[^/]+/blob/[^/]+/.*(?:\\.|%2[eE])(?:plantuml|puml|pu)"
}

class PlantUMLRenderer extends Renderer {

  override def render(request: RenderRequest): Html = {
    val context = request.context
    Html(renderAt(request.fileContent, context.currentPath, context.path))
  }

  private[plantuml] def renderAt(content: String, currentPath: String, contextPath: String): String =
    if (currentPath.matches(PlantUMLRenderer.blobPath)) renderViewer(content, contextPath)
    else imgEmbedded(content)

  def imgEmbedded(content: String): String = renderImage(content)(src => s"""<img src="$src">""")

  private def renderViewer(content: String, contextPath: String): String = {
    val stylesheet = StringUtil.escapeHtml(s"$contextPath/plugin-assets/plantuml_plugin/plantuml-viewer.css")
    renderImage(content) { src =>
      s"""<link rel="stylesheet" href="$stylesheet">
         |<div class="plantuml-viewer" role="group" aria-label="PlantUML diagram viewer">
         |  <div class="plantuml-viewer-toolbar" role="group" aria-label="Diagram controls" hidden>
         |    <button type="button" class="btn btn-default btn-sm" data-action="zoom-out" aria-label="Zoom out">-</button>
         |    <button type="button" class="btn btn-default btn-sm" data-action="reset" title="Reset zoom to 100%">100%</button>
         |    <button type="button" class="btn btn-default btn-sm" data-action="zoom-in" aria-label="Zoom in">+</button>
         |    <button type="button" class="btn btn-default btn-sm" data-action="fit">Fit to view</button>
         |    <button type="button" class="btn btn-default btn-sm plantuml-viewer-fullscreen" data-action="fullscreen" aria-pressed="false">Full screen</button>
         |  </div>
         |  <div class="plantuml-viewer-viewport"><img src="$src" alt="PlantUML diagram"></div>
         |  <p class="plantuml-viewer-status" role="status" hidden></p>
         |</div>""".stripMargin
    }
  }

  private def renderImage(content: String)(wrap: String => String): String = {
    val raw = PlantUMLUtils.generateSVGImage(content)
    raw match {
      case null => {
        val c = StringUtil.escapeHtml(content)
        s"""<pre class="prettyprint linenums blob">$c</pre>"""
      }
      case _ => {
        val src = Base64.getEncoder.encodeToString(raw)
        wrap(s"data:image/svg+xml;charset=utf-8;base64,$src")
      }
    }
  }
}
