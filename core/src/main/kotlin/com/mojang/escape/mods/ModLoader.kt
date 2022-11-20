package com.mojang.escape.mods

import java.io.File
import java.net.URL
import java.net.URLClassLoader
import java.util.zip.ZipFile

class ModLoader {
    fun getModList(): List<IMod> {
        val mods = mutableListOf<IMod>()
        val modsFolder = File("mods")
        if (!modsFolder.exists()) {
            modsFolder.mkdirs()
        }
        
        val modsFolderFiles = modsFolder.listFiles { _, name ->
            name.endsWith(".jar")
        }
        if (modsFolderFiles != null) {
            for (file in modsFolderFiles) {
                if (file.isFile) {
                    loadJarClasses(file, mods)
                }
            }
        }
        
        return mods
    }
    
    private fun loadJarClasses(file: File, mods: MutableList<IMod>) {
        val zip = ZipFile(file)
        val classLoader = URLClassLoader(arrayOf(URL("jar:file:" + file.absolutePath + "!/")))
        
        val entries = zip.entries()
        
        for (entry in entries) {
            if (entry.toString().endsWith(".class")) {
                var className = entry.toString()
                className = className.replace('\\', '.')
                className = className.replace('/', '.')
                className = className.substring(0, className.length - 6) // Remove .class extension
                loadClass(classLoader, className, mods)
            }
        }
    }
    
    private fun loadClass(classLoader: ClassLoader, className: String, mods: MutableList<IMod>) {
        val clazz = classLoader.loadClass(className)
        if (IMod::class.java.isAssignableFrom(clazz) && !clazz.isAssignableFrom(IMod::class.java)) {
            mods.add(clazz.newInstance() as IMod)
        }
    }
}