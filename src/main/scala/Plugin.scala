import com.yotaichino.gitbucket.plugins.plantuml.PlantUMLRenderer
import com.yotaichino.gitbucket.plugins.plantuml.PlantUMLController
import javax.servlet.ServletContext
import java.util.regex.Pattern
import gitbucket.core.plugin.PluginRegistry
import gitbucket.core.service.SystemSettingsService.SystemSettings
import io.github.gitbucket.solidbase.model.Version
import scala.io.Source
import scala.util.Using

class Plugin extends gitbucket.core.plugin.Plugin {
  override val pluginId: String = "plantuml_plugin"
  override val pluginName: String = "PlantUML Plugin"
  override val description: String = "Renders PlantUML diagrams!"
  override val versions: List[Version] = List(
    new Version("1.0.0"),
    new Version("1.1.0"),
    new Version("1.1.1"),
    new Version("1.1.2"),
    new Version("1.2.0"),
    new Version("1.2.1"),
    new Version("1.3.0"),
    new Version("1.4.0"),
    new Version("1.5.0"),
    new Version("1.6.0"),
    new Version("1.6.1"),
    new Version("1.6.2"),
    new Version("1.7.0")
  )

  override val assetsMappings: Seq[(String, String)] = Seq("/plantuml_plugin/" -> "/assets/")

  override def javaScripts(
    registry: PluginRegistry,
    context: ServletContext,
    settings: SystemSettings
  ): Seq[(String, String)] = {
    val script = Using.resource(Source.fromResource("plantuml-viewer.js", getClass.getClassLoader))(_.mkString)
    // GitBucket matches these patterns against the request URI, including the servlet context.
    Seq((Pattern.quote(context.getContextPath) + PlantUMLRenderer.blobPath) -> script)
  }

  override def initialize(registry: PluginRegistry, context: ServletContext, settings: SystemSettings): Unit = {
    val renderer = new PlantUMLRenderer()

    registry.addRenderer("plantuml", renderer)
    registry.addRenderer("puml", renderer)
    registry.addRenderer("pu", renderer)

    registry.addController("/*", new PlantUMLController())
  }
}
