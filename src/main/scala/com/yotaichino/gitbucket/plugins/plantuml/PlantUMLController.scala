package com.yotaichino.gitbucket.plugins.plantuml

import scala.util.Using

import gitbucket.core.controller._
import gitbucket.core.service._
import gitbucket.core.util._
import gitbucket.core.util.SyntaxSugars._
import gitbucket.core.util.Directory._
import gitbucket.core.util.FileUtil
import gitbucket.core.util.JGitUtil
import org.eclipse.jgit.api.Git

class PlantUMLController extends PlantUMLControllerBase
with RepositoryService with AccountService with ReferrerAuthenticator

trait PlantUMLControllerBase extends ControllerBase {
  self: RepositoryService with AccountService with ReferrerAuthenticator =>

  get("/:owner/:repository/plantuml/*")(referrersOnly { repository =>
    val (id, path) = repository.splitPath(multiParams("splat").head)
    Using.resource(Git.open(getRepositoryDir(repository.owner, repository.name))){ git =>
      val revCommit = JGitUtil.getRevCommitFromId(git, git.getRepository.resolve(id))

      JGitUtil.getContentFromPath(git, revCommit.getTree, path, true).map { content =>
        FileUtil.getExtension(path) match {
          case "plantuml" | "puml" | "pu" => {
              contentType = "image/svg+xml"
              PlantUMLUtils.generateSVGImage(new String(content))
            }
          case _ => unsupportedMediaType
        }
      } getOrElse NotFound
    }
  })

  private def unsupportedMediaType(): org.scalatra.ActionResult = org.scalatra.UnsupportedMediaType(gitbucket.core.html.error("Unsupported Media Type"))
}
