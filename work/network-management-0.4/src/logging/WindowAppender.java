package logging;

// package com.apress.logging.log4j.appender;

import org.apache.log4j.*;
import org.apache.log4j.spi.*;
import javax.swing.JTextArea;

/** This is a custom Appender object publishing the logging
 * information to a Java window. The logging Java window is a
 * singleton instance across all the instance of this Appender
 * object.
 */
public class WindowAppender extends AppenderSkeleton{
    
    private static JTextArea area = null;
    
    /** Holds value of property title. */
    private static String title;
    
    /** Holds value of property height. */
    private static int height;
    
    /** Holds value of property width. */
    private static int width;
    
    /** Creates a new instance of WindowAppender */
    public WindowAppender(JTextArea logArea, String patternLayout, String p) {
        setThreshold(Level.toLevel(p));
        setLayout(new PatternLayout(patternLayout));
        area = logArea;   
    }
    
    
    /** This method is overridden from the super class and
     * prints the logging message to the logging window.
     * NOTE: If there is no layout specified for this Appender,
     * no message will be displayed. The logging information is
     * formatted according to the layout and the conversion pattern
     * specified.
     * @param loggingEvent encapsulates the logging information.
     */
    protected void append(LoggingEvent loggingEvent) {
        //simply extract the message and display it
        
        area.append(this.layout.format(loggingEvent));
    }
    
    public void close() {
    }

    
    /** This method is overridden from the super class and always
     * returns true to indicate that a Layout is required for
     * this appender. If not specified in the conf. file, then it
     * will not print any message in the log window.
     */
    public boolean requiresLayout() {
        return true;
    }
    
    /** Getter for property title.
     * @return Value of property title.
     */
    public String getTitle() {
        return this.title;
    }
    
    /** Setter for property title.
     * @param title New value of property title.
     */
    public void setTitle(String title) {
        this.title = title;
    }
    
    /** Getter for property height.
     * @return Value of property height.
     */
    public int getHeight() {
        return this.height;
    }
    
    /** Setter for property height.
     * @param height New value of property height.
     */
    public void setHeight(int height) {
        this.height = height;
    }
    
    /** Getter for property width.
     * @return Value of property width.
     */
    public int getWidth() {
        return this.width;
    }
    
    /** Setter for property width.
     * @param width New value of property width.
     */
    public void setWidth(int width) {
        this.width = width;
    }
    
}
