package de.shellfire.vpn.webservice;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.xnap.commons.i18n.I18n;

import de.shellfire.vpn.Util;
import de.shellfire.vpn.VpnProperties;
import de.shellfire.vpn.gui.CanContinueAfterBackEndAvailableFX;
import de.shellfire.vpn.gui.LoginForms;
import de.shellfire.vpn.gui.controller.LoginController;
import de.shellfire.vpn.gui.controller.ProgressDialogController;
import de.shellfire.vpn.i18n.VpnI18N;
import java.io.IOException;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;


public class EndpointManager {

    private static final String DELIM = ";";
    private final static String PROPERTY_ENDPOINTS = "webserviceEndPoints";
    private final static String PROPERTY_PREFERRED_ENDPOINT = "preferredWebserviceEndPoint";
    private final static String DEFAULT_PROPERTIES = "www.shellfire.de:443;www.shellfire.net:443;www.shellfire.fr:443;213.239.207.251:380;213.239.207.252:380;176.57.129.88:380;192.71.249.26:380;46.246.93.202:380;37.235.48.187:380;174.34.178.139:380;176.9.16.216:380;176.9.16.215:380;94.76.223.69:380;94.76.223.68:380;192.95.24.110:380;94.23.27.103:380;176.57.141.68:380;176.57.141.83:380;37.235.49.49:380;158.255.208.212:380;151.236.23.76:380;151.236.18.125:380;37.235.52.74:380;37.235.55.134:380;46.108.39.238:380;213.183.56.14:380;162.252.172.111:380;176.57.141.162:380;185.4.134.183:380;104.152.44.66:380;176.57.141.209:380;176.57.141.93:443;174.34.178.141:443;";
    private static I18n i18n = VpnI18N.getI18n();

    private static Logger log = Util.getLogger(EndpointManager.class.getCanonicalName());
    private static EndpointManager instance;
    private List<String> endPointList;
    private ProgressDialogController initDialogFX;
    private String preferredEndPoint;
    private boolean currentlyUsingDefaultList = false;
    private VpnProperties vpnProperties;

    private EndpointManager() {
        log.debug("EndpointManager: In the constructors");
        loadFromProperties();
    }

    private void loadFromProperties() {
        vpnProperties = VpnProperties.getInstance();

        String endPointListCsv = vpnProperties.getProperty(PROPERTY_ENDPOINTS);

        if (endPointListCsv == null || endPointListCsv.length() < 5) {
            log.debug("No Endpoints in properties file, using hard coded default list (if possible will be replaced by new list later)");
            endPointListCsv = getDefaultListCsv();
            currentlyUsingDefaultList = true;
        }

        setEndPointListFromCsv(endPointListCsv);
        setPreferredEndPoint(getPreferredEndPointFromProperties());
        
        // Check if JavaFX application is running. True if Platform variable is set or not null
        try {
            if(Platform.isImplicitExit()? true: true){
                log.debug("Dialog binding has been set");
            }
        } catch (Exception e) {
            log.error("Not Javafx Application running " + e);
        }
    }

    public String getPreferredEndPointFromProperties() {
        String preferredEndPoint = vpnProperties.getProperty(PROPERTY_PREFERRED_ENDPOINT);
        if (preferredEndPoint == null) {
            log.warn("No preferred endPoint set yet, returning default endpoint");
            return getDefaultEndPoint();
        }
        log.debug("getPreferredEndPointFromProperties() - returning: " + preferredEndPoint);
        return preferredEndPoint;
    }

    private void setPreferredEndPoint(String preferredEndPoint) {
        log.debug("setPreferredEndPoint(" + preferredEndPoint + ")");
        this.preferredEndPoint = preferredEndPoint;
        this.vpnProperties.setProperty(PROPERTY_PREFERRED_ENDPOINT, preferredEndPoint);
    }

    public static EndpointManager getInstance() {
        if (instance == null) {
            instance = new EndpointManager();
        }

        return instance;
    }

    public class FindEndpointTaskFX extends Task<String> {
        /*
         * Main task. Executed in background thread of javaFX app.
         */
        private CanContinueAfterBackEndAvailableFX continueFormFX;
        private boolean initDialogOriginFX;
        
        public FindEndpointTaskFX(CanContinueAfterBackEndAvailableFX form) {
            log.debug("FindEndpointTaskFX: Constructor of Endpoint task");
            this.continueFormFX = form;

            if (null == initDialogFX) {
                try {
                    log.debug("\nFindEndpointTaskFX: In Dialog is null \n");
                    initDialogFX = ProgressDialogController.getInstance("Update Check", null, LoginForms.getStage(), false);
                    initDialogOriginFX = true;
                    initDialogFX.getDialogStage().show();
                    log.debug("Endpoint task is still null;");
                } catch (IOException ex) {
                    ex.printStackTrace(System.out);
                }
            }
        }

        @Override
        protected String call() {
            log.debug("EndpointManager: start of call method");
            //Platform.runLater(()->dialogTextProperty.set(i18n.tr("Searching for backend connection...")));
            Platform.runLater(()->initDialogFX.setDialogText(i18n.tr("Searching for backend connection...")));
            log.debug("Find Endpoint task method, init dialog has " + initDialogFX.toString());
            boolean result = false;

            result = testPreferredEndpoint();

            if (result) {
                log.debug("Preferred Endpoint works, using it and skipping other tests");
            } else {
                log.debug("Preferred Endpoint not working or not set yet, trying other endPoints");
                result = testEndPointList(endPointList);

                if (result) {
                    log.debug("Found a working endPoint in the list, using it");
                } else {
                    log.debug("Did not find an endPoint that works in the current list.");
                    if (!currentlyUsingDefaultList) {
                        log.debug("We're not currently using the default list, so trying the default list");
                        String defaultList = getDefaultListCsv();
                        setEndPointListFromCsv(defaultList);
                        result = testEndPointList(endPointList);

                        if (result) {
                            log.debug("one of the default endPoints worked, using it");
                        } else {
                            log.debug("Still not found a working endPoint. know nothing else to do, giving up :-(");
                        }
                    }
                }
            }
            return preferredEndPoint;
        }
        
        /*
         * Executed in event dispatch thread
         */

        private boolean testPreferredEndpoint() {
            log.debug("testPreferredEndpoint() - start");
            boolean result = false;
            if (preferredEndPoint == null) {
                log.debug("No preferred endPoint set yet, not testing ");
            } else {
                log.debug("fx testing preferred endPoint {}", preferredEndPoint);
                Platform.runLater(() -> initDialogFX.setDialogText(i18n.tr("Testing endpoint that worked before...")));
                log.debug("testPreferredEndpoint - Tested endpoint that worked befores");

                result = testEndpoint(preferredEndPoint);
            }

            log.debug("testPreferredEndpoint() - finished, returning {}", result);
            return result;
        }

        public CanContinueAfterBackEndAvailableFX getContinueFormFX() {
            return continueFormFX;
        }

        public boolean isInitDialogOriginFX() {
            return initDialogOriginFX;
        }

        private boolean testEndPointList(List<String> endPointList) {
            log.debug("testEndPointList() - start");
            boolean result = false;

            for (int i = 0; i < endPointList.size() && result == false; i++) {
                Platform.setImplicitExit(false);
                String msg = String.format(" %s / %s", (i + 1), endPointList.size());
                Platform.runLater(() -> {initDialogFX.setDialogText(i18n.tr("Searching for backend connection...") + msg);});
                String endPoint = endPointList.get(i);
                result = testEndpoint(endPoint);
            }

            log.debug("testEndPointList() - finished, returning {}", result);
            return result;
        }

        @Override
        protected void failed() {
            log.debug("testEndpointlist did not worked so failed");
        }
        
        @Override
        protected void succeeded() {
            log.debug("testEndpointlist worked and succeeded");
            String result = null ;
            if (isInitDialogOriginFX()) {
                log.debug("end task is successfully set");
                initDialogFX.getDialogStage().hide();
            }

            result = String.valueOf(getValue());
      
            if (result == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(i18n.tr("Could not connect to the Shellfire backend - Shellfire VPN is shutting down"));
            alert.showAndWait();
            Platform.exit();
            System.exit(0);
            }
            if (isInitDialogOriginFX()) {
                initDialogFX.getDialogStage().hide();
            }   
            this.continueFormFX.continueAfterBackEndAvailabledFX();
        }
        
}

    public void ensureShellfireBackendAvailableFx(CanContinueAfterBackEndAvailableFX form) {
        log.debug("ensureShellfireBackendAvailableFx starting...");
        initDialogFX = LoginController.initProgressDialog;
        log.debug("ensureShellfireBackendAvailableFx continuation...");
        FindEndpointTaskFX taskE = new FindEndpointTaskFX(form);
        Thread t = new Thread(taskE);
        log.debug("Starting Endpoint task");
        t.start();
    }

    private boolean testEndpoint(String endPoint) {
        log.debug("testEndpoint({}) - start", endPoint);

        boolean result = false;
        WebServiceBroker broker = WebServiceBroker.getInstance();
        broker.setEndPoint(endPoint);

        try {
            String url = broker.getUrlSuccesfulConnect();
            if (url != null && url.length() > 10) {
                log.debug("successfully retrieved an answer from endPoint as a test. setting as preferred endPoint");
                setPreferredEndPoint(endPoint);
                result = true;
            } else {
                log.debug("got a response from the backend, but did not receive serverlist successfully");
            }
        } catch (Exception e) {
            log.error("Could not connect to endPoint", e);
        }

        log.debug("testEndpoint(String) - finsihed, returning {}", result);
        return result;
    }

    private String getDefaultListCsv() {
        return DEFAULT_PROPERTIES;
    }

    public void setEndPointList(List<String> endPointList) {
        if (endPointList == null) {
            log.warn("setEndPointList received empty list of endpoints - not updating!");
        } else {
            this.endPointList = endPointList;
            String[] endPointArray = new String[endPointList.size()];
            endPointList.toArray(endPointArray);

            String endPointListCsv = Arrays.toString(endPointArray).replace(", ", DELIM).replaceAll("[\\[\\]]", "");
            this.vpnProperties.setProperty(PROPERTY_ENDPOINTS, endPointListCsv);
        }

    }

    private void setEndPointListFromCsv(String endPointListCsv) {
        setEndPointList(Arrays.asList(endPointListCsv.split(DELIM)));
    }

    public String getDefaultEndPoint() {
        return this.endPointList.get(0);
    }

}