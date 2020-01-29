package org.script

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.awt.Component
import javax.swing.BoxLayout
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel


class FormData(
    val processed: Int,
    val process: String,
    val found: Int,
    val time: String
)

var listener: (FormData) -> Unit = {}
fun setSwingData(data: FormData) {
    listener(data)
}


fun showSwing() {
    val frame = JFrame("title1")
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    val panel = JPanel()
    panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)
    frame.contentPane.add(panel)
    operator fun <T : Component> T.unaryPlus(): T {
        panel.add(this)
        return this
    }

    val processLabel = +JLabel("process")
    val processedLabel = +JLabel("processed count")
    val foundLabel = +JLabel("found count")
    val timeLabel = +JLabel("time")


    listener = { data ->
        runBlocking {
            withContext(Dispatchers.Main) {
                with(data) {
                    processLabel.text = data.process
                    processedLabel.text = "processed: $processed"
                    foundLabel.text = "found: $found"
                    timeLabel.text = time
                }
            }
        }
    }

    frame.pack()
    frame.isVisible = true
}