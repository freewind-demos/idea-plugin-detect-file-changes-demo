package example

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.ProjectComponent
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.openapi.vfs.newvfs.BulkFileListener
import com.intellij.openapi.vfs.newvfs.events.VFileContentChangeEvent
import com.intellij.openapi.vfs.newvfs.events.VFileEvent
import com.intellij.util.messages.MessageBusConnection

class MyProjectComponent : ProjectComponent {
    var conn: MessageBusConnection? = null
    override fun initComponent() {
        conn = ApplicationManager.getApplication().messageBus.connect()
        conn?.subscribe(VirtualFileManager.VFS_CHANGES, object : BulkFileListener {
            override fun before(events: MutableList<out VFileEvent>) {
                println("> before: ${events.joinToString(",")}")
            }

            override fun after(events: MutableList<out VFileEvent>) {
                println("> after: ${events.joinToString(",")}")

                val changeEvents = events.filter { it is VFileContentChangeEvent }
                println("changeEvents: $changeEvents")
            }
        })
    }

    override fun disposeComponent() {
        conn?.disconnect()
    }
}