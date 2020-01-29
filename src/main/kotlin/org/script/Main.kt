package org.script

import java.io.File

suspend fun main(args: Array<String>) {
    println("hello windows")
    val roots: Array<File> = File.listRoots()
    println(roots.map { it.absolutePath })
}
