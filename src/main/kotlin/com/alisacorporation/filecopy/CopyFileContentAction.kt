package com.alisacorporation.filecopy

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.ide.CopyPasteManager
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.vfs.VirtualFile
import java.awt.datatransfer.StringSelection

class CopyFileContentAction : AnAction() {
    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val virtualFiles = e.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY) ?: return

        val content = buildString {
            virtualFiles.forEach { file ->
                processFile(file, this)
            }
        }

        CopyPasteManager.getInstance().setContents(StringSelection(content))
    }

    private fun processFile(file: VirtualFile, builder: StringBuilder) {
        if (file.isDirectory) {
            file.children.forEach { child ->
                processFile(child, builder)
            }
        } else {
            builder.append("\n=== File: ${file.path}\n")
            builder.append(String(file.contentsToByteArray()))
            builder.append("\n")
        }
    }

    override fun update(e: AnActionEvent) {
        if (getActionUpdateThread() == ActionUpdateThread.BGT) {
            val files = e.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY)
            e.presentation.isEnabledAndVisible = !files.isNullOrEmpty()
        }
    }
}