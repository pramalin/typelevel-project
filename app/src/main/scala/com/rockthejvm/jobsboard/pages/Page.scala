package com.rockthejvm.jobsboard.pages

import tyrian.*
import cats.effect.IO

import com.rockthejvm.jobsboard.*

object Page {
    trait Msg

    enum StatusKind {
        case SUCCESS, ERROR, LOADING
    }

    final case class Status(message: String, kind: StatusKind)
    object Urls {
        val LOGIN            = "/login"
        val SIGNUP           = "/signup"
        val FORGOT_PASSWORD  = "/forgotpassword"
        val RECOVER_PASSWORD = "/recoverpassword"
        val JOBS             = "/jobs"
        val EMPTY            = ""
        val HOME             = "/"
        val HASH             = "#"
    }

    def get(location: String): Page =
        location match {
            case Urls.LOGIN            => LoginPage()
            case Urls.SIGNUP           => SignUpPage()
            case Urls.FORGOT_PASSWORD  => ForgotPasswordPage()
            case Urls.RECOVER_PASSWORD => RecoverPasswordPage()
            case Urls.EMPTY | Urls.HOME | Urls.JOBS => JobListPage()
            case s"/jobs/$id"           => JobPage(id)
            case _                      => NotFoundPage()
        }
}

abstract class Page {
    // API

    // send a command upon instantiating
    def initCmd: Cmd[IO, App.Msg]
    // update
    def update(msg: App.Msg): (Page, Cmd[IO, App.Msg])
    // render
    def view(): Html[App.Msg]
}
