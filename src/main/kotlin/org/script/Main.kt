package org.script

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import java.io.File
import java.util.*

var maxSizeFile: File? = null
var maxSize: Long = Long.MIN_VALUE
var minSizeFile: File? = null
var minSize: Long = Long.MAX_VALUE
var currentProcess: File? = null
var processedCount: Int = 0
var found: Int = 0

fun File.printInfo(): String = "${length() / 1024 / 1000F} Mb, $absolutePath"

suspend fun main(args: Array<String>) {
    val moveToDir = File("K:/save_jpg")

    val ignoreDirs: List<File> = listOf(
        moveToDir
    )

    val roots: Array<File> = File.listRoots()
    println(roots.map { it.absolutePath })
    val render = showSwing()

    GlobalScope.launch {
        while (true) {
            delay(50)
            render(
                ViewState(
                    processed = processedCount,
                    process = currentProcess?.absolutePath.orEmpty(),
                    found = found,
                    time = Date().toString(),
                    minSizeStr = minSizeFile?.printInfo().orEmpty(),
                    maxSizeStr = maxSizeFile?.printInfo().orEmpty()
                )
            )
        }
    }

    roots.forEach {
        it.process {
            val ext = it.extension.toLowerCase()
            if (ext == "jpg" || ext == "jpeg") {
                found++
                val size = it.length()
                if (size > maxSize) {
                    maxSizeFile = it
                    maxSize = size
                }
                if (size < minSize) {
                    minSizeFile = it
                    minSize = size
                }
            }
        }
    }

}

fun File.process(depth: Int = 8, lambda: suspend (File) -> Unit) {
    currentProcess = this
    processedCount++
    GlobalScope.launch {
        when {
            depth < 0 -> Unit
            !canRead() -> Unit
            name.startsWith(".") -> Unit
            name.startsWith("$") -> Unit
            false && !isAbsolute -> Unit
            isDirectory -> {
                for (file in listFiles().orEmpty()) {
                    yield()
                    file.process(depth - 1, lambda)
                }
            }
            isHidden -> Unit
            isFile -> {
                yield()
                lambda(this@process)
            }
        }
    }
}
