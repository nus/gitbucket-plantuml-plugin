package com.yotaichino.gitbucket.plugins.plantuml

import java.nio.charset.StandardCharsets.UTF_8
import java.util.Base64
import org.scalatest.FunSpec

class PlantUMLRendererSpec extends FunSpec {
  private val renderer = new PlantUMLRenderer
  private val source = "@startuml\nAlice -> Bob: Hello\n@enduml"
  private val blobPath = "/owner/repo/blob/main/docs/example.puml"

  describe("PlantUML file display") {
    it("provides controls, a context-relative stylesheet and a vector image in the viewport") {
      val html = renderer.renderAt(source, blobPath, "/gitbucket")

      assert(html.contains("""href="/gitbucket/plugin-assets/plantuml_plugin/plantuml-viewer.css""""))
      for (action <- Seq("zoom-in", "zoom-out", "reset", "fit", "fullscreen")) {
        assert(html.contains(s"""data-action="$action""""))
      }
      val embeddedImage = """<div class="plantuml-viewer-viewport"><img src="data:image/svg\+xml;charset=utf-8;base64,([^"]+)"""".r
      val encoded = embeddedImage.findFirstMatchIn(html).getOrElse(fail("SVG missing from viewer viewport")).group(1)
      val svg = new String(Base64.getDecoder.decode(encoded), UTF_8)
      assert(svg.contains("""<svg xmlns="http://www.w3.org/2000/svg""""))
    }

    it("keeps the escaped source and omits viewer assets and controls when conversion fails") {
      val html = renderer.renderAt("""<script>"a & b"</script>""", blobPath, "/gitbucket")

      assert(html == """<pre class="prettyprint linenums blob">&lt;script&gt;&quot;a &amp; b&quot;&lt;/script&gt;</pre>""")
    }

    it("keeps the plain SVG image when rendering outside a PlantUML blob page") {
      val html = renderer.renderAt(source, "/owner/repo/plantuml/main/blob/example.puml", "")

      assert(html.startsWith("""<img src="data:image/svg+xml;charset=utf-8;base64,"""))
      assert(!html.contains("plantuml-viewer"))
    }
  }
}
