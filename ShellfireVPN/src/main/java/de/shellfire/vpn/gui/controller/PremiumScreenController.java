/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.shellfire.vpn.gui.controller;

import de.shellfire.vpn.Util;
import de.shellfire.vpn.gui.LoginForms;
import de.shellfire.vpn.gui.model.VpnComparisonFXHelper;
import de.shellfire.vpn.gui.model.VpnComparisonFXTableModel;
import de.shellfire.vpn.i18n.VpnI18N;
import de.shellfire.vpn.webservice.WebService;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import org.slf4j.Logger;
import org.xnap.commons.i18n.I18n;


/**
 *
 * @author Tcheutchoua Steve
 */
public class PremiumScreenController implements Initializable{

    @FXML
    private Button cancelButton;
    @FXML
    private TableView<VpnComparisonFXTableModel> comparisonTableView;
    @FXML
    private TableColumn<VpnComparisonFXTableModel, String> connectionColumn;
    @FXML
    private TableColumn<VpnComparisonFXTableModel, Object> freeColumn;
    @FXML
    private TableColumn<VpnComparisonFXTableModel, Object> premiumColumn;
    @FXML
    private TableColumn<VpnComparisonFXTableModel, Object> premiumPlusColumn;
    @FXML
    private ImageView shellfireImageView;
    @FXML
    private Label upgradeLabel;
    @FXML
    private ScrollPane navigationPane;
    @FXML
    private Button buyPremiumButton;
    @FXML
    private Label remainingTimeLabel;
    @FXML
    private Label remainingTimeValue;
    
    private static final I18n i18N = VpnI18N.getI18n();
    private static Logger log = Util.getLogger(PremiumScreenController.class.getCanonicalName());
    private LoginForms application;
    private  WebService shellfireService = null ;
    public PremiumScreenController() {
        //initComparisonTable();
    }
    
    void setDelay(int i) {
     String text = i + "s";
    this.remainingTimeValue.setText(text);
  }

    @FXML
    private void handleBuyPremiumNow(ActionEvent event) {
        WebService service = WebService.getInstance();
    Util.openUrl(service.getUrlPremiumInfo());
    }

    @FXML
    private void handleCalcelButton(ActionEvent event) {
        setNormalCursor();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.connectionColumn.setText(i18N.tr("Connection "));
        this.freeColumn.setText(i18N.tr("Free "));
        this.premiumColumn.setText(i18N.tr("Premium "));
        this.premiumPlusColumn.setText(i18N.tr("Premium Plus "));
        this.upgradeLabel.setText(i18N.tr("Upgrage to Shellfire VPN now"));
        this.buyPremiumButton.setText(i18N.tr("Buy premium now"));
        this.remainingTimeLabel.setText(i18N.tr("Remaining waiting time"));
        this.cancelButton.setText(i18N.tr("Cancel"));
        
        connectionColumn.setCellValueFactory(cellData -> cellData.getValue().connectionProperty());
        freeColumn.setCellValueFactory(cellData -> cellData.getValue().freeProperty());
        premiumColumn.setCellValueFactory(cellData -> cellData.getValue().premiumProperty());
        premiumPlusColumn.setCellValueFactory(cellData -> cellData.getValue().premiumPlusProperty());
    }
    
    public void disableTimer() {
    this.remainingTimeLabel.setText("");
    this.remainingTimeValue.setText("");
  }
        private void setNormalCursor() {
        this.application.getStage().getScene().setCursor(Cursor.DEFAULT);
        disableTimer();
    }
    public void initComparisonTable (){
        System.out.println("PremiumScreenController: initComparisonTable intialization");
        VpnComparisonFXHelper comparisonModel = new VpnComparisonFXHelper();
        comparisonModel.setService(shellfireService);
        comparisonModel.initData();
//        comparisonModel.initData();
//        for (int i = 0; i < comparisonModel.getRowCount(); i++) {
//            //System.out.println("PremiumScreenController: initComparisonTable"+comparisonModel.getValueAt(1, i).toString());
//            for(int j = 0; j < comparisonModel.getColumnCount(); i++){
//                log.debug("PremiumScreenController: initComparisonTable " + comparisonModel.getValueAt(i, j));
//            }
//        }
        //return null;
    }
    
    public void setApp(LoginForms applic) {
        this.application = applic;
    }
    
     public void setService(WebService service) {
        this.shellfireService = service;
 }
}
