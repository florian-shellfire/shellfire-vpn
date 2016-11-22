/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SettingsDialog.java
 *
 * Created on 15.08.2011, 08:41:17
 */
package de.shellfire.vpn.gui;

import java.rmi.RemoteException;
import java.util.LinkedList;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;

import org.xnap.commons.i18n.I18n;

import de.shellfire.vpn.Util;
import de.shellfire.vpn.VpnProperties;
import de.shellfire.vpn.client.Client;
import de.shellfire.vpn.i18n.Language;
import de.shellfire.vpn.i18n.VpnI18N;
import de.shellfire.vpn.webservice.Vpn;
import de.shellfire.vpn.webservice.WebService;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author bettmenn
 */
public class SettingsDialog extends javax.swing.JDialog {

    private Language currentLanguage;
    private static I18n i18n = VpnI18N.getI18n();
    
    
    /** Creates new form SettingsDialog */
    public SettingsDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        
        initComponents();
        initValues();

        this.setLocationRelativeTo(null);
        this.pack();
        this.setVisible(true);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSettingsPanel = new javax.swing.JPanel();
        jRememberLoginData = new javax.swing.JCheckBox();
        jAutoLogin = new javax.swing.JCheckBox();
        jSaveVpnSelection = new javax.swing.JCheckBox();
        jLanguageComboBox = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jSaveSettings = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jAutoStart = new javax.swing.JCheckBox();
        jAutoConnect = new javax.swing.JCheckBox();
        jLaunchStatusWebsiteOnConnect = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(i18n.tr("Shellfire VPN Einstellungen"));

        jSettingsPanel.setName("jSettingsPanel");

        jRememberLoginData.setText(i18n.tr("Logindaten speichern"));
        jRememberLoginData.setName("jRememberLoginData"); // NOI18N
        jRememberLoginData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRememberLoginDataActionPerformed(evt);
            }
        });
        jSettingsPanel.setLayout(new MigLayout("", "[][][]", "[][][][][][][][]"));
        jSettingsPanel.add(jRememberLoginData, "cell 0 0 2 1,growx,aligny top");

        jAutoLogin.setText(i18n.tr("Automatisch einloggen"));
        jAutoLogin.setName("jAutoLogin"); // NOI18N
        jAutoLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jAutoLoginActionPerformed(evt);
            }
        });
        jSettingsPanel.add(jAutoLogin, "cell 0 1 2 1,growx,aligny bottom");

        jSaveVpnSelection.setText(i18n.tr("VPN Auswahl merken"));
        jSaveVpnSelection.setName("jSaveVpnSelection"); // NOI18N
        jSaveVpnSelection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jSaveVpnSelectionActionPerformed(evt);
            }
        });
        jSettingsPanel.add(jSaveVpnSelection, "cell 0 2 2 1,growx,aligny top");

        jLanguageComboBox.setName("jLanguageComboBox"); // NOI18N
        jLanguageComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jLanguageComboBoxActionPerformed(evt);
            }
        });
        jSettingsPanel.add(jLanguageComboBox, "cell 1 6,growx,aligny top");

        jLabel2.setText(i18n.tr("Sprache") + ":");
        jLabel2.setName("jLabel2"); // NOI18N
        jSettingsPanel.add(jLabel2, "cell 0 6,alignx right,aligny bottom");

        jSaveSettings.setText(i18n.tr("Einstellungen Speichern"));
        jSaveSettings.setName("jSaveSettings"); // NOI18N
        jSaveSettings.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jSaveSettingsActionPerformed(evt);
            }
        });
        jSettingsPanel.add(jSaveSettings, "cell 0 7,alignx left,aligny top");

        jButton1.setText(i18n.tr("Abbrechen"));
        jButton1.setName("jButton1"); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().setLayout(new MigLayout("", "", ""));
        jSettingsPanel.add(jButton1, "cell 1 7,growx,aligny top");

        jAutoStart.setText(i18n.tr("Beim Hochfahren starten"));
        jAutoStart.setName("jAutoStart"); // NOI18N
        jSettingsPanel.add(jAutoStart, "cell 0 3 2 1,growx,aligny top");

        jAutoConnect.setText(i18n.tr("Automatisch verbinden"));
        jAutoConnect.setName("jAutoConnect"); // NOI18N
        jSettingsPanel.add(jAutoConnect, "cell 0 4 2 1,growx,aligny top");

        jLaunchStatusWebsiteOnConnect.setText(i18n.tr("Zeige Status-Seite nach Verbindungsaufbau"));
        jLaunchStatusWebsiteOnConnect.setName("jLaunchStatusWebsiteOnConnect"); // NOI18N
        jSettingsPanel.add(jLaunchStatusWebsiteOnConnect, "cell 0 5 2 1,growx,aligny top");
        getContentPane().add(jSettingsPanel, "cell 0 0,grow");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jRememberLoginDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRememberLoginDataActionPerformed
        if (jRememberLoginData.isEnabled() && !jRememberLoginData.isSelected()) {
          if (jAutoLogin.isEnabled()) {
            this.jAutoLogin.setSelected(false);
          }
          if (jSaveVpnSelection.isEnabled()) {
            this.jSaveVpnSelection.setSelected(false);
          }
        }
}//GEN-LAST:event_jRememberLoginDataActionPerformed

    private void jAutoLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jAutoLoginActionPerformed
        if (jAutoLogin.isEnabled() && jAutoLogin.isSelected() && jRememberLoginData.isEnabled()) {
            this.jRememberLoginData.setSelected(true);
        }
}//GEN-LAST:event_jAutoLoginActionPerformed

    private void jLanguageComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jLanguageComboBoxActionPerformed
        JComboBox cb = (JComboBox) evt.getSource();
        Language language = (Language) cb.getSelectedItem();
        this.currentLanguage = language;
    }//GEN-LAST:event_jLanguageComboBoxActionPerformed

    private void jSaveSettingsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jSaveSettingsActionPerformed
      saveAndClose();
        
}//GEN-LAST:event_jSaveSettingsActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        close();
}//GEN-LAST:event_jButton1ActionPerformed

    private void jSaveVpnSelectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jSaveVpnSelectionActionPerformed
      if (jSaveVpnSelection.isEnabled() && jSaveVpnSelection.isSelected() && jRememberLoginData.isEnabled()) {
        this.jRememberLoginData.setSelected(true);
      }

    }//GEN-LAST:event_jSaveVpnSelectionActionPerformed


    private void saveAndClose() {
        close();
        save();
    }

    private void save() {
      VpnProperties props = VpnProperties.getInstance();

        if (this.jAutoLogin.isEnabled()) {
          props.setBoolean(LoginForm.REG_AUTOLOGIN, this.jAutoLogin.isSelected());
        }

        if (this.jRememberLoginData.isEnabled() && this.jRememberLoginData.isSelected() == false) {
          props.remove(LoginForm.REG_USER);
          props.remove(LoginForm.REG_PASS);
        }

        if (this.jSaveVpnSelection.isEnabled() && !this.jSaveVpnSelection.isSelected()) {
          props.remove(VpnSelectDialog.REG_REMEMBERSELECTION);
        }
        
        props.setBoolean(LoginForm.REG_AUTOCONNECT, this.jAutoConnect.isSelected());
        props.setBoolean(LoginForm.REG_SHOWSTATUSURL, this.jLaunchStatusWebsiteOnConnect.isSelected());
        
        if (jSaveVpnSelection.isSelected()) {
          WebService service = WebService.getInstance();
          Vpn vpn = service.getVpn();
          props.setInt(VpnSelectDialog.REG_REMEMBERSELECTION, vpn.getVpnId());
        }
        
        
        if (this.jAutoStart.isSelected()) {
          Client.addVpnToAutoStart();
        } else {
          Client.removeVpnFromAutoStart();
        }
        

        Language oldLanguage = VpnI18N.getLanguage();
        
        if (!currentLanguage.equals(oldLanguage)) {
          VpnI18N.setLanguage(currentLanguage);  
          int res = JOptionPane.showConfirmDialog(null, i18n.tr("Die Änderung der Sprache wird erst nach einem Neustart von Shellfire VPN aktiv. Jetzt neu starten?"), i18n.tr("Geänderte Sprache erst nach Neustart wirksam"), JOptionPane.YES_NO_OPTION);
          if (res == JOptionPane.YES_OPTION) {
            this.dispose();
            LoginForm.restart();
          }
          
        }
        
    }

    private void close() {
        this.setVisible(false);
    }    

    private void initValues() {
      VpnProperties props = VpnProperties.getInstance();
        
        if (props.getProperty(LoginForm.REG_USER, null) != null) {
            this.jRememberLoginData.setSelected(true);
        } else {
            this.jRememberLoginData.setEnabled(false); // can only be enabled from login dialog
        }
        
        if (props.getBoolean(LoginForm.REG_AUTOLOGIN, false)) {
          this.jAutoLogin.setSelected(true);
        } else if (props.getProperty(LoginForm.REG_USER, null) == null){
            this.jAutoLogin.setEnabled(false); // disable if login data not remembered, because then it makes no sense
        }

        if (props.getInt(VpnSelectDialog.REG_REMEMBERSELECTION, 0) != 0) {
            this.jSaveVpnSelection.setSelected(true);
        } else if (props.getProperty(LoginForm.REG_USER, null) == null){
            this.jSaveVpnSelection.setEnabled(false); // disable if login data not remembered, because then it makes no sense
        }
        
        boolean autoConnect = props.getBoolean(LoginForm.REG_AUTOCONNECT, false);
        jAutoConnect.setSelected(autoConnect);
        
        boolean autoStart = Client.vpnAutoStartEnabled();
        jAutoStart.setSelected(autoStart);  

        boolean showStatusUrlOnConnect = props.getBoolean(LoginForm.REG_SHOWSTATUSURL, false);
        jLaunchStatusWebsiteOnConnect.setSelected(showStatusUrlOnConnect);

        initLanguages();
    }
    
    private void initLanguages() {
        LinkedList<Language> languages = VpnI18N.getAvailableTranslations();

        for (Language language : languages) {
            jLanguageComboBox.addItem(language);
        }

        currentLanguage = VpnI18N.getLanguage();
        jLanguageComboBox.setSelectedItem(currentLanguage);

    }    
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox jAutoConnect;
    private javax.swing.JCheckBox jAutoLogin;
    private javax.swing.JCheckBox jAutoStart;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JComboBox jLanguageComboBox;
    private javax.swing.JCheckBox jLaunchStatusWebsiteOnConnect;
    private javax.swing.JCheckBox jRememberLoginData;
    private javax.swing.JButton jSaveSettings;
    private javax.swing.JCheckBox jSaveVpnSelection;
    private javax.swing.JPanel jSettingsPanel;
    // End of variables declaration//GEN-END:variables
}
