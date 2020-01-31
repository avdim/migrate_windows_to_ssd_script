package org.script

import kotlinx.coroutines.*
import java.io.File
import java.util.*

var totalSize: Long = 0L
var maxSizeFile: File? = null
var maxSize: Long = Long.MIN_VALUE
var minSizeFile: File? = null
var minSize: Long = Long.MAX_VALUE
var currentProcess: File? = null
var processedCount: Int = 0
var found: Int = 0
var duplicate: String = ""
var duplications: Int = 0

val diskWriteContext = newSingleThreadContext("write disk")

val moveToDir = File("K:/save_jpg")
val ignoreDirs: List<File> = listOf(
    moveToDir,
    File("C:\\windows"),
    File("D:\\windows")
)
val ignoreSize = 10 * 1024L

fun File.printInfo(): String = "${length() / 1024 / 1000F} Mb, $absolutePath"

suspend fun main(args: Array<String>) {
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
                    maxSizeStr = maxSizeFile?.printInfo().orEmpty(),
                    totalSize = "${totalSize / 1024 / 1000f} Mb",
                    duplicate = "duplicate: $duplications, $duplicate"
                )
            )
        }
    }

    roots.forEach {
        it.process {
            val ext = it.extension.toLowerCase()
            if (ext == "jpg" || ext == "jpeg") {
                val size = it.length()
                if (size > ignoreSize) {
                    found++
                    if (size > maxSize) {
                        maxSizeFile = it
                        maxSize = size
                    }
                    if (size < minSize) {
                        minSizeFile = it
                        minSize = size
                    }
                    totalSize += size
                    if (moveToDir.exists()) {
                        withContext(diskWriteContext) {
                            val destination = moveToDir.resolve(it.longDirsName())
                            if (destination.exists()) {
                                println("duplicate: ${destination}            from  ${it.absolutePath}")
                                duplicate = it.absolutePath
                                duplications++
                            } else {
                                destination.parentFile.mkdirs()
                                it.copyTo(destination)
                            }
                            val rootLabel = destination.parentFile
                            if(!rootLabel.exists()) {
                                rootLabel.createNewFile()
                            }
                        }
                    }
                }
            }
        }
    }

}

fun File.process(depth: Int = 8, lambda: suspend (File) -> Unit) {
    processedCount++
    for (ignore in ignoreDirs) {
        if (relativeToOrNull(ignore) != null) {
            return
        }
    }

    currentProcess = this
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
