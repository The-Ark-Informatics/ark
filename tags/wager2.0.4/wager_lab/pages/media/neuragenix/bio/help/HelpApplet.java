/*
 * HelpApplet.java
 *
 * Created on 11 March 2003, 11:03
 */

package media.neuragenix.bio.help;

/**
 *
 * @author  hh
 */
import java.awt.*;
import javax.help.*;
import java.net.URL;
import javax.swing.*;

public class HelpApplet extends JApplet {
    final String HELPSET = "media/neuragenix/bio/help/help/myhelpset.hs";
    final String HOME_ID = "BioGenix Online Help";
    
    private JHelp helpViewer = null;
    
    public void init() {
        
        // create JHelp object
        try {
            /*
            UIManager.LookAndFeelInfo[] looks = UIManager.getInstalledLookAndFeels();
            UIManager.setLookAndFeel(looks[1].getClassName());
            SwingUtilities.updateComponentTreeUI(this);
            */
            
            // Get the classloader of this class.
	    ClassLoader cl = HelpApplet.class.getClassLoader();
	    URL url = HelpSet.findHelpSet(cl, HELPSET);
            
            // Create a new JHelp object with a new HelpSet.
            helpViewer = new JHelp(new HelpSet(cl, url));

            // Set the initial entry point in the table of contents.
            helpViewer.setCurrentID(HOME_ID);
            
        } 
        catch (Exception e) {
            e.printStackTrace();
            System.err.println("API Help Set not found");
        }
        
        // adding JHelp object into applet
        getContentPane().add(helpViewer);
    }
    
    public void stop() {
        helpViewer = null;
    }
}