package com.yotaichino.gitbucket.plugins.plantuml

import gitbucket.core.controller.ControllerBase
import gitbucket.core.util.AdminAuthenticator
import net.sourceforge.plantuml.version.Version
import play.twirl.api.Html

class PlantUMLAdminController extends PlantUMLAdminControllerBase with AdminAuthenticator

trait PlantUMLAdminControllerBase extends ControllerBase {
  self: AdminAuthenticator =>

  get("/admin/plantuml")(adminOnly {
    val diagnostics = Html(new PlantUMLRenderer().imgEmbedded("@startuml\ntestdot\n@enduml"))
    gitbucket.plantuml.html.status(Version.versionString(), diagnostics)
  })
}
