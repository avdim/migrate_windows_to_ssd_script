package org.script

import javafx.beans.property.SimpleStringProperty
import tornadofx.*

val stringProperty = SimpleStringProperty("TEST STRING")

class MyView : View() {
    override val root = vbox() {
        label(stringProperty).bind(stringProperty)
    }
}

class MyApp : App(MyView::class)

fun launchTornadoFx() {
    launch<MyApp>()
}