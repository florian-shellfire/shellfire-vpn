/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.shellfire.vpn.gui.model;

import java.awt.image.BufferedImage;

import org.jdesktop.swingx.mapviewer.DefaultWaypoint;

import de.shellfire.vpn.types.LocatableIcon;

/**
 *
 * @author bettmenn
 */
public class IconWaypoint extends DefaultWaypoint {
    private final BufferedImage icon;

    public BufferedImage getIcon() {
        return icon;
    }
    
    public IconWaypoint(LocatableIcon locatableIcon) {
        super(locatableIcon.getGeoPosition());
        this.icon = locatableIcon.getIcon();
    }
    
}
