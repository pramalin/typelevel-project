package com.rockthejvm.jobsboard.pages

import tyrian.*
import cats.effect.IO

object Page {
    trait Msg

    object Urls {
        val LOGIN            = "/login"
        val SIGNUP           = "/signup"
        val FORGOT_PASSWORD  = "/forgotpassword"
        val RECOVER_PASSWORD = "/recoverpassword"
        val JOBS             = "/jobs"
        val EMPTY            = ""
        val HOME             = "/"
    }

    def get(location: String): Page =
        location match {
            case Urls.LOGIN            => LoginPage()
            case Urls.SIGNUP           => SignupPage()
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
    def initCmd: Cmd[IO, Page.Msg]
    // update
    def update(msg: Page.Msg): (Page, Cmd[IO, Page.Msg])
    // render
    def view(): Html[Page.Msg]
}
