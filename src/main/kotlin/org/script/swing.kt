package org.script

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.swing.JFrame
import javax.swing.JLabel


var listener: (String) -> Unit = {}
fun setSwingText(str: String) {
    listener(str)
}

fun showSwing() {
    val frame = JFrame("title1")
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    val label = JLabel("Hello World")

    listener = { str ->
        runBlocking {
            withContext(Dispatchers.Main) {
                label.text = str
            }
        }
    }

    frame.contentPane.add(label)
    frame.pack()
    frame.isVisible = true
}