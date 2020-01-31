package org.script

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.awt.Component
import javax.swing.BoxLayout
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel

class ViewState(
    val processed: Int,
    val process: String,
    val found: Int,
    val time: String,
    val minSizeStr:String,
    val maxSizeStr:String,
    val totalSize:String
)

fun showSwing(): suspend (ViewState) -> Unit {
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
    val minSizeLabel = +JLabel()
    val maxSizeLabel = +JLabel()
    val totalSizeLabel = +JLabel()

    frame.pack()
    frame.isVisible = true

    return { state: ViewState ->
        withContext(Dispatchers.Main) {
            with(state) {
                processLabel.text = state.process
                processedLabel.text = "processed: $processed"
                foundLabel.text = "found: $found"
                timeLabel.text = time
                minSizeLabel.text = state.minSizeStr
                maxSizeLabel.text = state.maxSizeStr
                totalSizeLabel.text = state.totalSize
            }
            frame.pack()
        }
    }

}