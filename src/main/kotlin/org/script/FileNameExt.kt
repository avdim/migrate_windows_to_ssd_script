package org.script

import java.io.File

val File.simpleName: String get() = name.simpleName

val String.simpleName get(): String {
    val longString = replace(Regex("[^A-Z a-z.А-Яа-я0-9]+"), "_")
    if (longString.length < 20) {
        return longString
    } else {
        return longString.take(12) + "_" + longString.takeLast(8)
    }
}

fun File.longDirsName(depth: Int = 2): String {
    val parent = parentFile
    if (depth == 0 || parent == null) {
        return simpleName
    } else {
        return parent.longDirsName(depth - 1) + "/" + simpleName
    }
}

val File.secondRootName: String
    get() {
        var current: File = this
        while (current.parentFile?.parentFile != null) {
            current = current.parentFile
        }
        return current.absolutePath.simpleName
    }

fun File.smartName() = secondRootName + "/" + longDirsName()
