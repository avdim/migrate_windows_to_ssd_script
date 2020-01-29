package org.script

import kotlinx.coroutines.*
import java.io.File
import java.util.*
import java.util.concurrent.ConcurrentHashMap

var process: String = ""
val visited: MutableSet<String> = Collections.newSetFromMap(ConcurrentHashMap())

suspend fun main(args: Array<String>) {
    println("hello windows")
    val roots: Array<File> = File.listRoots()
    println(roots.map { it.absolutePath })
    showSwing()

    GlobalScope.launch {
        while (true) {
            delay(50)
            setSwingText(process)
            stringProperty.set(process)
        }
    }

    GlobalScope.launch {
        roots.forEach {
            it.eachLeaf {
                val ext = it.extension.toLowerCase()
                if (ext == "jpg" || ext == "jpeg") {
                    println(it.absolutePath)
                }
            }
        }
    }
}

suspend fun File.eachLeaf(depth: Int = 5, lambda: suspend (File) -> Unit) {
    GlobalScope.launch {
        process = absolutePath
        if(!isAbsolute) {
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
        if (visited.contains(absolutePath)) {
            return@launch
        }

        visited.add(absolutePath)

        if (isDirectory) {
            for (file in listFiles().orEmpty()) {
                GlobalScope.launch {
                    if (!visited.contains(file.absolutePath)) {
                        file.eachLeaf(depth - 1, lambda)
                    }
                }
            }
            return@launch
        }
        if (isFile && !isHidden) {
            lambda(this@eachLeaf)
        }
    }
}
