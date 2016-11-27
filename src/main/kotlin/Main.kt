import components.FileChooserDemo
import javax.swing.SwingUtilities
import javax.swing.UIManager

/**
 * Created by work on 11/25/16.
 */

class Main {
    companion object {
        @JvmStatic fun main(args: Array<String>) {

            args.forEach { println(it) }

            //Schedule a job for the event dispatch thread:
            //creating and showing this application's GUI.
            SwingUtilities.invokeLater {
                //Turn off metal's use of bold fonts
                UIManager.put("swing.boldMetal", java.lang.Boolean.FALSE)
                FileChooserDemo.createAndShowGUI()
            }
        }
    }
}