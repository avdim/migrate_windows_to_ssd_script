package org.script

import java.io.File

suspend fun main(args: Array<String>) {
    println("hello windows")
    val roots: Array<File> = File.listRoots()
    println(roots.map { it.absolutePath })

    roots.forEach {
        it.eachLeaf {
            val ext = it.extension.toLowerCase()
            if (ext == "jpg" || ext == "jpeg") {
                println(it.absolutePath)
            }
        }
//        delay(1)
    }
}

suspend fun File.eachLeaf(lambda: suspend (File) -> Unit) {
    if (isHidden) {
        return
    }
    if (isDirectory) {
        for (file in listFiles().orEmpty()) {
            file.eachLeaf(lambda)
        }
        return
    }
    println("process: ${this.absolutePath}")
    lambda(this)
}

