import org.scalatest._
import com.yotaichino.gitbucket.plugins.plantuml.PlantUMLUtils

class PlantUMLUtilsSpec extends FunSpec {
  describe("generateSVGImage") {
    it("should return a SVG image.") {
      val content = new String(PlantUMLUtils.generateSVGImage("@startuml\n@enduml"), "utf-8")
      println(content)
      assert(content.startsWith("""<svg xmlns="http://www.w3.org/2000/svg" """))
    }
    it("should return null when empty string") {
      assert(PlantUMLUtils.generateSVGImage("") == null)
    }
    it("should return null when string with invisible characters") {
      assert(PlantUMLUtils.generateSVGImage("   ") == null)
      assert(PlantUMLUtils.generateSVGImage("\n\n") == null)
      assert(PlantUMLUtils.generateSVGImage(" \r\n  ") == null)
      assert(PlantUMLUtils.generateSVGImage(" \r  ") == null)
    }
  }
}
