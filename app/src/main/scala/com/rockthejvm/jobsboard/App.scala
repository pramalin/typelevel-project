package com.rockthejvm.jobsboard

import tyrian.*
import tyrian.Html.*
import cats.effect.*
import scala.scalajs.js.annotation.*
import org.scalajs.dom.document
import scala.concurrent.duration.*
import tyrian.cmds.Logger


object App {
    sealed trait Msg
    case class Increment(amount: Int) extends Msg

    case class Model(count: Int)
}

@JSExportTopLevel("RockTheJvmApp")
class App extends TyrianApp[App.Msg,        App.Model] {
    //                       ^^ model type, ^^ message type = state

    import App.*
    /*
      We can send messages by
      - trigger a command
      - create a subscription
      - listening for event
    */

    // potentially endless stream of messages
    override def init(flags: Map[String, String]): (Model, Cmd[IO, Msg]) =
        (Model(0), Cmd.None)
    override def subscriptions(model: Model): Sub[IO, Msg] =
        Sub.every[IO](1.second).map(_ => Increment(1))

    // model can change by receiving messages
    // model => message => (new model, ___ /*new command*/)
    // update is triggered whenever we get a new message
    override def update(model: Model): Msg => (Model, Cmd[IO, Msg]) = { case Increment(amount) =>
        (model.copy(count = model.count + amount), Logger.info("Changing the count by " + amount)) // we can also trigger a command when we update the model
    }

    // view triggered whenever model changes
    override def view(model: Model): Html[Msg] =
        div(
            button(onClick(Increment(1)))("Increase"),
            button(onClick(Increment(-1)))("Decrease"),
            div(s"Tyrian running ${model.count}")
        )
}
 