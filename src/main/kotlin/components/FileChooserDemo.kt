/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package components

import java.io.*
import java.awt.*
import java.awt.event.*
import javax.swing.*
import javax.swing.SwingUtilities
import javax.swing.filechooser.*
import java.util.stream.Stream
import java.util.stream.Collectors
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes
import java.util.regex.Pattern
import java.nio.file.SimpleFileVisitor
import java.nio.file.FileVisitResult
import java.nio.file.FileVisitor
import java.nio.file.Files
import java.nio.file.Paths
import java.io.BufferedWriter
import java.io.FileWriter
import java.io.PrintWriter
import java.io.File.separator
import java.io.File




/*
 * FileChooserDemo.java uses these files:
 *   images/Open16.gif
 *   images/Save16.gif
 */
class FileChooserDemo : JPanel(BorderLayout()), ActionListener {
    internal var openButton: JButton
    internal var saveButton: JButton
    internal var log: JTextArea
    internal var fc: JFileChooser

    init {

        //Create the log first, because the action listeners
        //need to refer to it.
        log = JTextArea(5, 20)
        log.margin = Insets(5, 5, 5, 5)
        log.isEditable = false
        val logScrollPane = JScrollPane(log)

        //Create a file chooser
        fc = JFileChooser()

        //Uncomment one of the following lines to try a different
        //file selection mode.  The first allows just directories
        //to be selected (and, at least in the Java look and feel,
        //shown).  The second allows both files and directories
        //to be selected.  If you leave these lines commented out,
        //then the default mode (FILES_ONLY) will be used.
        //
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        //Create the open button.  We use the image from the JLF
        //Graphics Repository (but we extracted it from the jar).
        openButton = JButton("Open a File...",
                createImageIcon("/images/Open16.gif"))
        openButton.addActionListener(this)

        //Create the save button.  We use the image from the JLF
        //Graphics Repository (but we extracted it from the jar).
        saveButton = JButton("Save a File...",
                createImageIcon("/images/Save16.gif"))
        saveButton.addActionListener(this)

        //For layout purposes, put the buttons in a separate panel
        val buttonPanel = JPanel() //use FlowLayout
        buttonPanel.add(openButton)
        buttonPanel.add(saveButton)

        //Add the buttons and the log to this panel.
        add(buttonPanel, BorderLayout.PAGE_START)
        add(logScrollPane, BorderLayout.CENTER)
    }

    override fun actionPerformed(e: ActionEvent) {

        //Handle open button action.
        if (e.source === openButton) {
            val returnVal = fc.showOpenDialog(this@FileChooserDemo)

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                //todo: This is where a real application would open the file.


                val dir = fc.selectedFile
                log.append("Opening: " + dir.name + "." + newline)

                if (dir.isDirectory) {

                    val ROOT = dir.absolutePath
                    val fileProcessor = ProcessFile(log)
                    Files.walkFileTree(Paths.get(ROOT), fileProcessor)

                }

            } else {
                log.append("Open command cancelled by user." + newline)
            }
            log.caretPosition = log.document.length

            //Handle save button action.
        } else if (e.source === saveButton) {
            val returnVal = fc.showSaveDialog(this@FileChooserDemo)
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                val file = fc.selectedFile
                //todo: This is where a real application would save the file.

                log.append("Saving: " + file.name + "." + newline)
            } else {
                log.append("Save command cancelled by user." + newline)
            }
            log.caretPosition = log.document.length
        }
    }

    private class ProcessFile(internal var log: JTextArea) : SimpleFileVisitor<Path>() {

        @Throws(IOException::class)
        override fun visitFile(
                aFile: Path, aAttrs: BasicFileAttributes
        ): FileVisitResult {

            if (aFile.toString().toLowerCase().endsWith("txt")) {

                val lines = Files.readAllLines(aFile, StandardCharsets.UTF_8)

                val sensitiveNames = Sanitizer.BuildSensitiveStrings(lines)

                log.append("Processing file:" + aFile + newline)

                sensitiveNames.forEach {
                    log.append(it + newline)
                }

                val results = Sanitizer.RemoveSensitiveStrings(lines, sensitiveNames)

                val absolutePath = aFile.toAbsolutePath().toString()
                val filePath = absolutePath.substring(0, absolutePath.lastIndexOf(File.separator))

                val writer = BufferedWriter(FileWriter(filePath + File.separator + "~" + aFile.fileName.toString() + "sanitized.txt"))

                val printWriter = PrintWriter(writer)
                results.forEach {
                    printWriter.println(it)
                }
// do stuff
                writer.close()
                printWriter.close()


            }

            return FileVisitResult.CONTINUE
        }

        @Throws(IOException::class)
        override fun preVisitDirectory(
                aDir: Path, aAttrs: BasicFileAttributes
        ): FileVisitResult {
            log.append("Processing directory:" + aDir + newline)

            return FileVisitResult.CONTINUE
        }
    }

    companion object {
        private val newline = "\n"

        /** Returns an ImageIcon, or null if the path was invalid.  */
        protected fun createImageIcon(path: String): ImageIcon? {
            val imgURL = FileChooserDemo::class.java.getResource(path)
            if (imgURL != null) {
                return ImageIcon(imgURL)
            } else {
                System.err.println("Couldn't find file: " + path)
                return null
            }
        }

        /**
         * Create the GUI and show it.  For thread safety,
         * this method should be invoked from the
         * event dispatch thread.
         */
        fun createAndShowGUI() {
            //Create and set up the window.
            val frame = JFrame("FileChooserDemo")
            frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE

            //Add content to the window.
            frame.add(FileChooserDemo())

            //Display the window.
            frame.pack()
            frame.isVisible = true
        }
    }
}