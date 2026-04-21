package com.rockthejvm.jobsboard.pages

import tyrian.*
import tyrian.Html.*
import cats.effect.IO

final case class HomePage() extends Page {
    override def initCmd: Cmd[IO, Page.Msg] = Cmd.None
    override def update(msg: Page.Msg): (Page, Cmd[IO, Page.Msg]) = (this, Cmd.None)
    override def view(): Html[Page.Msg] = Html.div(
        Html.h1("Home Page")
    )
}
