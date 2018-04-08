/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.shellfire.vpn.gui.model;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.shellfire.vpn.Util;

/**
 *
 * @author bettmenn
 */
public class ContentPane {
    private final String file;
    private final JLabel button;
    private final String folder = "/buttons/";
    private final String activeExtension = "-active.png";
    private final String hoverExtension = "-hover.png";
    private final String idleExtension = "-idle.png";
    private final ImageIcon iconActive;
    private final ImageIcon iconHover;
    private final ImageIcon iconIdle;
    private final JPanel panel;
    private final JLabel header;
    private final JLabel footer;
    static public final Color colorIdle = new Color(170, 178, 189);
    static public final Color colorActive = new Color(57, 170, 214);
    //static public final Color colorDarkGrey = new Color(49, 57, 64);
    static public final Color colorDarkGrey = new Color(77, 77, 77);
    static public final Color colorLightGray = new Color(225, 225, 225);
    static public final Color colorVeryLightGray = new Color(244, 244, 244);

    public ContentPane(String file, JLabel jButton, JPanel jPanel, JLabel jHeader, JLabel jFooter) {
        this.file = file;
        this.button = jButton;
        this.panel = jPanel;
        this.header = jHeader;
        this.footer = jFooter;
        
        this.iconActive = Util.getImageIcon(this.getFileActive());
        this.iconHover = Util.getImageIcon(this.getFileHovered());
        this.iconIdle = Util.getImageIcon(this.getFileIdle());
    }



    public JLabel getButton() {
        return button;
    }

    public String getFile() {
        return file;
    }
    
    private String getFileActive() {
        return this.folder + this.getFile() + this.activeExtension;
    }
    
    private String getFileHovered() {
        return this.folder + this.getFile() + this.hoverExtension;
    }    
    
    private String getFileIdle() {
        return this.folder + this.getFile() + this.idleExtension;
    }
    
    void setActiveState() {
        this.button.setIcon(this.iconActive);
        this.header.setForeground(colorActive);
        this.footer.setForeground(colorActive);
    }

    void setHoveredState() {
        this.button.setIcon(this.iconHover);
        this.header.setForeground(colorIdle);
        this.footer.setForeground(colorIdle);
    }

    void setIdleState() {
        this.button.setIcon(this.iconIdle);
        this.header.setForeground(colorIdle);
        this.footer.setForeground(colorIdle);
    }

    void setVisible(boolean visible) {
        this.panel.setVisible(visible);
    }

    
}
