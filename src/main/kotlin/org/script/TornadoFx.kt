package org.script

import javafx.beans.property.SimpleStringProperty
import tornadofx.*

val stringProperty = SimpleStringProperty("_____________________________________________________")

class MyView : View() {
    override val root = vbox() {
        label(stringProperty).bind(stringProperty)
    }
}

class MyApp : App(MyView::class)

fun main(args: Array<String>) {
    launchTornadoFx()
}

fun launchTornadoFx() {
    launch<MyApp>()
}