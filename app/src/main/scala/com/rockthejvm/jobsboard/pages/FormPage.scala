package com.rockthejvm.jobsboard.pages

import tyrian.*
import tyrian.http.*
import tyrian.Html.*
import cats.effect.IO

import com.rockthejvm.jobsboard.common.*
import com.rockthejvm.jobsboard.core.*
import com.rockthejvm.jobsboard.*

abstract class FormPage(title: String, status: Option[Page.Status]) extends Page {

    // public API
    override def initCmd: Cmd[IO, App.Msg] =
        Cmd.None
    override def view(): Html[App.Msg] =
        renderForm()

    // abstract API
    protected def renderFormContent(): List[Html[App.Msg]] // for every page to override

    // protected API
    protected def renderForm(): Html[App.Msg] = {
       div(`class` := "form-section")(
        // title: Sign Up
        div(`class` := "top-section")(
        h1(title)
       ),
       // form
       form(
            name:= "sign-in",
            `class` := "form",
            onEvent(
                "submit",
                e => {
                    e.preventDefault()
                    App.NoOp
                }
            )
       )(
        renderFormContent()
       ),
       status.map(s => div(s.message)).getOrElse(div()))
  }

    protected def renderInput(
        name: String,
        uid: String,
        kind: String,
        isRequired: Boolean,
        onChange: String => App.Msg
    ) =
        div(`class` := "form-input")(
           label(`for` := name, `class` := "form-label")(
            if(isRequired) span("*") else span(),
            text(name)
           ),
           input(`type` := kind, `class` := "form-control", id := uid, onInput(onChange)) 
        )

    protected def renderAuxLink(location: String, text: String): Html[App.Msg] =
        a(
            href := location,
            `class` := "aux-link",
            onEvent(
                "click",
                e => {
                    e.preventDefault() // native JS - prevent reloading the page
                    Router.ChangeLocation(location)
                }
            )
        )(text)
}
