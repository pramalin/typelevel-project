package com.rockthejvm.jobsboard

import tyrian.*
import tyrian.Html.*
import cats.effect.*
import scala.scalajs.js.annotation.*
import org.scalajs.dom.window
import scala.concurrent.duration.*

import core.*
object App {
    type Msg = Router.Msg

    case class Model(router: Router)
}

@JSExportTopLevel("RockTheJvmApp")
class App extends TyrianApp[App.Msg, App.Model] {
    import App.*

    override def init(flags: Map[String, String]): (Model, Cmd[IO, Msg]) = {
        val (router, cmd) = Router.startAt(window.location.pathname)
        (Model(router), cmd)
    }
    
    override def subscriptions(model: Model): Sub[IO, Msg] =
        Sub.make( // listener for browser history changes
            "uriChange",
             model.router.history.state.discrete
                .map(_.get)
                .map(newLocation => Router.ChangeLocation(newLocation, true))
        )

    // model can change by receiving messages
    // model => message => (new model, ___ /*new command*/)
    // update is triggered whenever we get a new message
    override def update(model: Model): Msg => (Model, Cmd[IO, Msg]) = {
         case msg: Router.Msg =>
            val (newRouter, cmd) = model.router.update(msg)
            (model.copy(router = newRouter), cmd)
    }

    // view triggered whenever model changes
    override def view(model: Model): Html[Msg] =
        div(
            renderNavLink("Jobs", "/jobs"),
            renderNavLink("Login", "/login"),
            renderNavLink("Sign up", "/signup"),
            div(s"Your are now at ${model.router.location}")
        )


    private def renderNavLink(text: String, location: String) =
        a(
            href := location,
            `class` := "nav-link",
            onEvent(
                "click",
                e => {
                    e.preventDefault() // native JS - prevent reloading the page
                    Router.ChangeLocation(location)
                }
            )
        )(text)
}
 