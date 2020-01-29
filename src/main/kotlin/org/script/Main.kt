package org.script

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.util.*

var process: String = ""
var processed: Int = 0
var found: Int = 0

suspend fun main(args: Array<String>) {
    println("hello windows")
    val roots: Array<File> = File.listRoots()
    println(roots.map { it.absolutePath })
    showSwing()

    GlobalScope.launch {
        while (true) {
            delay(50)
            setSwingData(
                FormData(
                    processed = processed,
                    process = process,
                    found = found,
                    time = Date().toString()
                )
            )
        }
    }

    roots.forEach {
        GlobalScope.launch {
            it.eachLeaf {
                val ext = it.extension.toLowerCase()
                if (ext == "jpg" || ext == "jpeg") {
                    found++
                }
            }
        }
    }

}

suspend fun File.eachLeaf(depth: Int = 8, lambda: suspend (File) -> Unit) {
    processed++
    GlobalScope.launch {
        process = absolutePath
        if (!isAbsolute) {
            return@launch
        }
        if (depth <= 0) {
            return@launch
        }
        if (!canRead()) {
            return@launch
        }
        if (name.startsWith(".")) {
            return@launch
        }
        if (name.startsWith("$")) {
            return@launch
        }
//        if (visited.contains(absolutePath)) {
//            return@launch
//        }
//        visited.add(absolutePath)

        if (isDirectory) {
            for (file in listFiles().orEmpty()) {
                GlobalScope.launch {
                    file.eachLeaf(depth - 1, lambda)
                }
            }
            return@launch
        }
        if (isFile && !isHidden) {
            lambda(this@eachLeaf)
        }
    }
}
