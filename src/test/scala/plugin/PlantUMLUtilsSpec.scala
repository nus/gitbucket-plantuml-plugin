import org.scalatest._
import com.yotaichino.gitbucket.plugins.plantuml.PlantUMLUtils

class PlantUMLUtilsSpec extends FunSpec {
  describe("generateSVGImage") {
    it("should return a SVG image.") {
      val content = new String(PlantUMLUtils.generateSVGImage("@startuml\n@enduml"), "utf-8")
      assert(content.contains("""<svg xmlns="http://www.w3.org/2000/svg""""))
    }
    it("should render chart diagrams introduced after PlantUML 1.2022.5") {
      val source =
        """@startchart
          |h-axis [Q1, Q2, Q3, Q4]
          |v-axis "Revenue" 0 --> 100
          |bar "Sales" [45, 62, 58, 70] #3498db
          |legend right
          |@endchart""".stripMargin
      val content = new String(PlantUMLUtils.generateSVGImage(source), "utf-8")
      assert(content.contains("<svg"))
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
