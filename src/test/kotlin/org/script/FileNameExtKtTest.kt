package org.script

import org.junit.Test
import java.io.File

internal class FileNameExtKtTest{

    @Test
    fun testSimpleName() {
        val simpleName = File("123)))ghjkhgfhjkhgfdghjkhgfdgh.jpg").simpleName
        print(simpleName)
    }

    @Test
    fun testRecursiveName() {
        val name = File("C:/A/B/C/123rth.jpg").longDirsName()
        println(name)
    }

}