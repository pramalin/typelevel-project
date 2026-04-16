package com.rockthejvm.jobsboard

import scala.scalajs.js.annotation.*
import org.scalajs.dom.document

@JSExportTopLevel("RockTheJvmApp")
class App {
    @JSExport
    def doSomething(documentId: String) =
        document.getElementById(documentId).innerHTML = "Scala Rocks the JVM!"
    //in JS: document.getElementById[...].innerHTML - "This is my HTML"
}
 