/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * PremiumVPNNagScreen.java
 *
 * Created on 13.07.2011, 13:44:49
 */
package de.shellfire.vpn.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.xnap.commons.i18n.I18n;

import de.shellfire.vpn.Util;
import de.shellfire.vpn.gui.helper.MoveMouseListener;
import de.shellfire.vpn.gui.model.VpnAttributeHeader;
import de.shellfire.vpn.gui.model.VpnComparisonTableModel;
import de.shellfire.vpn.gui.renderer.BoolImageRenderer;
import de.shellfire.vpn.gui.renderer.StarImageRenderer;
import de.shellfire.vpn.i18n.VpnI18N;
import de.shellfire.vpn.webservice.WebService;
import de.shellfire.vpn.webservice.model.VpnEntry;
import net.miginfocom.swing.MigLayout;
import org.slf4j.Logger;

/**
 * 
 * @author bettmenn
 */
 public class PremiumVPNNagScreen extends javax.swing.JDialog {
  private final ShellfireVPNMainForm aparent;
  private MoveMouseListener mml;
private static Logger log = Util.getLogger(PremiumVPNNagScreen.class.getCanonicalName());
  /** Creates new form PremiumVPNNagScreen */
  public PremiumVPNNagScreen(java.awt.Frame parent, boolean modal, ActionListener al) {
    super(parent, modal);
    aparent = (ShellfireVPNMainForm) parent;
    initComponents();

    this.setLocationRelativeTo(null);
    if (al != null)
      jButton1.addActionListener(al);

    this.initComparisonTable();
    this.enableMouseMoveListener();
    this.pack();

  }

  public void disableTimer() {
    this.jLabel1.setText("");
    this.jRemainingTime.setText("");
  }

  /**
   * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method
   * is always regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jHeaderPanel = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jContentPanel = new javax.swing.JPanel();
        jLoginPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jRemainingTime = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton2 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Shellfire VPN 2");
        setUndecorated(true);

        jHeaderPanel.setBackground(new java.awt.Color(18, 172, 229));
        jHeaderPanel.setName("jHeaderPanel");
        jHeaderPanel.setLayout(new MigLayout("", "[grow]", "[][][]"));

        jLabel6.setIcon(ShellfireVPNMainForm.getLogo());
        jLabel6.setAlignmentY(0.0F);
        jLabel6.setName("jLabel6"); // NOI18N
        jHeaderPanel.add(jLabel6, "cell 0 0,grow");

        jContentPanel.setBackground(new java.awt.Color(64, 69, 73));
        jContentPanel.setName("jContentPanel");

        jLoginPanel.setName("jLoginPanel"); // NOI18N

        

        
        jLabel1.setFont(new java.awt.Font("Tahoma", 0, Util.getFontSize()));
        jLabel1.setText(i18n.tr("Remaining waiting time:"));
        jLabel1.setName("jLabel1"); // NOI18N

        jRemainingTime.setFont(new java.awt.Font("Tahoma", 0, Util.getFontSize()));
        jRemainingTime.setText("(remaining time)");
        jRemainingTime.setName("jRemainingTime"); // NOI18N

        jButton1.setFont(new java.awt.Font("Tahoma", 0, Util.getFontSize()));
        jButton1.setText(i18n.tr("Cancel"));
        jButton1.setName("jButton1"); // NOI18N

        jScrollPane1.setBackground(new java.awt.Color(18, 172, 229));
        jScrollPane1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jScrollPane1.setViewportBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jTable1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jTable1.setModel(new VpnComparisonTableModel());
        jTable1.setFillsViewportHeight(true);
        jTable1.setFocusable(false);
        jTable1.setName("jTable1"); // NOI18N
        jTable1.setRowSelectionAllowed(false);
        jTable1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jTable1.setShowHorizontalLines(false);
        jTable1.setShowVerticalLines(false);
        jScrollPane1.setViewportView(jTable1);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screenSize.getWidth() / 1.5);
        int height = (int) (screenSize.getHeight() / 1.5);
        
        jScrollPane1.setPreferredSize(new Dimension(width, height));

        

        jButton2.setFont(new java.awt.Font("Arial", 1, Util.getFontSize()));
        jButton2.setText(i18n.tr("Buy premium now"));
        jButton2.setName("jButton2"); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        getContentPane().setLayout(new MigLayout("", "[]", "[]"));
        jContentPanel.setLayout(new MigLayout("", "[grow,fill]", "[]"));

        jContentPanel.add(jLoginPanel, "cell 0 0,grow");
        jLoginPanel.setLayout(new MigLayout("", "[grow][grow][grow]", "[][][]"));
        jLoginPanel.add(jButton2, "cell 0 1 4 1,grow");
        jLoginPanel.add(jScrollPane1, "cell 0 0 3 1,grow");
        jLoginPanel.add(jLabel1, "cell 0 2,growx,aligny bottom");
        jLoginPanel.add(jRemainingTime, "cell 1 2,alignx left,aligny bottom");
        jLoginPanel.add(jButton1, "cell 2 2,alignx right,aligny top");

        jHeaderPanel.add(jContentPanel, "cell 0 2 3 1,grow");

        jLabel2.setFont(new java.awt.Font("Arial", 1, Util.getFontSize())); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("<html>"+i18n.tr("Upgrade to Shellfire VPN premium now")+"</html>");
        jLabel2.setName("jLabel2"); // NOI18N
        jHeaderPanel.add(jLabel2, "cell 0 1,alignx left,growy");

        getContentPane().add(jHeaderPanel, "cell 0 0,alignx left,aligny top");

        pack();
    }// </editor-fold>//GEN-END:initComponents

  private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton2ActionPerformed
    WebService service = WebService.getInstance();
    Util.openUrl(service.getUrlPremiumInfo());

  }// GEN-LAST:event_jButton2ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JPanel jContentPanel;
    private javax.swing.JPanel jHeaderPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jLoginPanel;
    private javax.swing.JLabel jRemainingTime;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
  private static I18n i18n = VpnI18N.getI18n();

  void setDelay(int i) {
     String text = i + "s";
    this.jRemainingTime.setText(text);
  }

  private void initComparisonTable() {
    JTableHeader header = jTable1.getTableHeader();
    header.setReorderingAllowed(false);
    header.getColumnModel().setColumnMargin(0);
    header.setResizingAllowed(false);
    TableColumnModel columnModel = header.getColumnModel();

    for (int i = 0; i < columnModel.getColumnCount(); i++) {
      TableColumn column = jTable1.getColumnModel().getColumn(i);
      column.setHeaderRenderer(new HeaderRenderer());
    }

    TableColumnModel cm = this.jTable1.getColumnModel();
    cm.getColumn(0).setPreferredWidth((int) (130*Util.getScalingFactor()));

    cm.getColumn(0).setCellRenderer(new RowHeaderRenderer());
    
    for (int i = 1; i < cm.getColumnCount(); i++) {
        cm.getColumn(i).setCellRenderer(new CustomRenderer());  
    }
  }

  void cleanRemove() {
    this.dispose();
    aparent.repaint();
  }

  class HeaderRenderer extends JLabel implements TableCellRenderer {
    public Component getTableCellRendererComponent(JTable table, Object value, boolean hasFocus, boolean isSelected, int row, int col) {
      setText(value.toString());
      setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
      setFont(getFont().deriveFont((float) (Util.getFontSize()*1.1)).deriveFont(Font.BOLD));
      setBackground(new Color(18, 172, 229));
      setForeground(Color.white);
      setOpaque(true);
      setHorizontalAlignment(JLabel.CENTER);
      return this;
    }
  }

  class RowHeaderRenderer extends JLabel implements TableCellRenderer {
    public Component getTableCellRendererComponent(JTable table, Object value, boolean hasFocus, boolean isSelected, int row, int col) {
      setOpaque(true);
      setHorizontalAlignment(JLabel.RIGHT);
      setText(value.toString());
      setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
      

      if (value instanceof VpnAttributeHeader) {
        
        jTable1.setRowHeight(row, 45);
        setFont(getFont().deriveFont((float) (Util.getFontSize()*1.1)).deriveFont(Font.BOLD));
        setBackground(new Color(18, 172, 229));
        setForeground(Color.white);

      }

      else {
        setFont(getFont().deriveFont((float) (Util.getFontSize())).deriveFont(Font.PLAIN));
        jTable1.setRowHeight(row, 40);
        setBackground(Color.white);
        setForeground(Color.black);
      }

      return this;
    }
  }

  class CustomRenderer extends JLabel implements TableCellRenderer {
    public Component getTableCellRendererComponent(JTable table, Object value, boolean hasFocus, boolean isSelected, int row, int col) {
      setOpaque(true);

      setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
      setHorizontalAlignment(JLabel.CENTER);
      
      if (value instanceof VpnEntry) {
        VpnEntry entry = (VpnEntry) value;
        if (entry.isBoolEntry()) {
          return new BoolImageRenderer().getTableCellRendererComponent(table, value, hasFocus, isSelected, row, col);
        } else if (entry.isStarEntry()) {
            return new StarImageRenderer().getTableCellRendererComponent(table, value, hasFocus, isSelected, row, col);
          } else {
          setText(entry.getText());
          log.debug("PremiumVPNNagScreen: Entry Values " + entry.getText());
        }
        
      } else if (value instanceof String) {
        setText((String)value);
        log.debug("PremiumVPNNagScreen: String Values " + (String)value);
      }
      
      

      if (value.equals("")) {
        setBackground(new Color(18, 172, 229));
        setForeground(Color.white);
      } else {
        setBackground(Color.white);
        setForeground(Color.black);
      }

      return this;
    }
  }

  private void enableMouseMoveListener() {

    if (mml == null)
      mml = new MoveMouseListener(this);

    this.addMouseListener(mml);
    this.addMouseMotionListener(mml);

  }
}
