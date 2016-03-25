package de.shellfire.vpn.webservice;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.security.KeyStore;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.shellfire.vpn.Util;
import de.shellfire.vpn.exception.VpnException;
import de.shellfire.vpn.updater.Updater;
import de.shellfire.vpn.webservice.model.GetActivationStatusRequest;
import de.shellfire.vpn.webservice.model.GetAllVpnDetailsRequest;
import de.shellfire.vpn.webservice.model.GetCertificatesForOpenVpnRequest;
import de.shellfire.vpn.webservice.model.GetComparisonTableDataRequest;
import de.shellfire.vpn.webservice.model.GetLatestInstallerRequest;
import de.shellfire.vpn.webservice.model.GetLatestVersionRequest;
import de.shellfire.vpn.webservice.model.GetLocalIpAddressRequest;
import de.shellfire.vpn.webservice.model.GetLocalLocationRequest;
import de.shellfire.vpn.webservice.model.GetParametersForOpenVpnRequest;
import de.shellfire.vpn.webservice.model.GetServerListRequest;
import de.shellfire.vpn.webservice.model.GetTrayMessagesRequest;
import de.shellfire.vpn.webservice.model.GetUrlHelpRequest;
import de.shellfire.vpn.webservice.model.GetUrlPasswordLostRequest;
import de.shellfire.vpn.webservice.model.GetUrlPremiumInfoRequest;
import de.shellfire.vpn.webservice.model.GetUrlSuccesfulConnectRequest;
import de.shellfire.vpn.webservice.model.RegisterRequest;
import de.shellfire.vpn.webservice.model.SendLogToShellfireRequest;
import de.shellfire.vpn.webservice.model.SetProtocolToRequest;
import de.shellfire.vpn.webservice.model.SetServerToRequest;
import de.shellfire.vpn.webservice.model.WsLoginRequest;

@SuppressWarnings("rawtypes")
class JsonHttpRequest<RequestType, ResponseType> {

  public JsonHttpRequest() {
    setupKeyStore();
  }
  
  private static Logger log = Util.getLogger(JsonHttpRequest.class.getCanonicalName());
  private String function;
  RequestConfig defaultRequestConfig = RequestConfig.custom()
      .setSocketTimeout(3000)
      .setConnectTimeout(3000)
      .setConnectionRequestTimeout(3000)
      .build();

  
  //Gson gson = new GsonBuilder().setPrettyPrinting().create();
  Gson gson = new GsonBuilder().create();
  private WebServiceBroker broker = WebServiceBroker.getInstance();
  private CloseableHttpClient httpClient;

  static final Map<Class, String> functionMap;

  static {
    HashMap<Class, String> tempMap = new HashMap<Class, String>();
    tempMap.put(WsLoginRequest.class, "login");
    tempMap.put(GetAllVpnDetailsRequest.class, "getAllVpnDetails");
    tempMap.put(GetServerListRequest.class, "getServerList");
    tempMap.put(SetServerToRequest.class, "setServerTo");
    tempMap.put(SetProtocolToRequest.class, "setProtocol");
    tempMap.put(GetParametersForOpenVpnRequest.class, "getOpenVpnParams");
    tempMap.put(GetCertificatesForOpenVpnRequest.class, "getCertificates");
    tempMap.put(GetLocalIpAddressRequest.class, "getLocalIpAddress");
    tempMap.put(GetLocalLocationRequest.class, "getLocalLocation");
    tempMap.put(RegisterRequest.class, "register");
    tempMap.put(GetActivationStatusRequest.class, "getActivationStatus");
    tempMap.put(GetComparisonTableDataRequest.class, "getComparisonTable");
    tempMap.put(GetTrayMessagesRequest.class, "getTrayMessages");
    tempMap.put(GetLatestVersionRequest.class, "getLatestVersion");
    tempMap.put(GetLatestInstallerRequest.class, "getLatestInstaller");
    tempMap.put(GetUrlSuccesfulConnectRequest.class, "getUrlSuccesfulConnect");
    tempMap.put(GetUrlHelpRequest.class, "getUrlHelp");
    tempMap.put(GetUrlPremiumInfoRequest.class, "getUrlPremiumInfo");
    tempMap.put(GetUrlPasswordLostRequest.class, "getUrlPasswordLost");
    tempMap.put(SendLogToShellfireRequest.class, "sendLog");
    functionMap = Collections.unmodifiableMap(tempMap);
  }

  private void setupKeyStore() { 
    try
    {
      TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
      KeyStore ks = KeyStore.getInstance("JKS");
      FileInputStream fis = new FileInputStream("shellfire.keystore");
      ks.load(fis, "blubber".toCharArray());
      fis.close();

      tmf.init(ks);

      SSLContext sslContext = SSLContext.getInstance("TLS");
      sslContext.init(null, tmf.getTrustManagers(), null);
      
      HostnameVerifier verifier = new HostnameVerifier(){
        public boolean verify(String hostname, SSLSession session) {
          return true;
        }
      };
      
      SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
          sslContext,
          new String[] { "TLSv1" },
          null,
          verifier);
      
      httpClient = HttpClients.custom()
          .setSSLHostnameVerifier(verifier)
          .setSSLSocketFactory(sslsf)
          .setDefaultRequestConfig(defaultRequestConfig)
          .build();
      
    } catch (Exception e) {
      log.error("Error occured while setting up keystore", e);
      System.exit(0);
    }
  }

  public Response<ResponseType> call(RequestType payload, Type clazz) throws ClientProtocolException, IOException, VpnException {
    log.debug("call() - start");
    this.function = functionMap.get(payload.getClass());
    if (function == null) {
      throw new VpnException("Unknown request (not contained in functionMap): " + payload.getClass().getCanonicalName());
    }
    log.debug("function: {}", function);

    HttpPost request = createRequest();

    log.debug("gson.toJson(payload)");
    String params = gson.toJson(payload);

    // TODO: anonymize the password
    String logParams = "";
    if (params != null) {
      logParams = params.substring(0, Math.min(400, params.length()));
    }
    log.debug("Body of http post: {}", logParams);

    StringEntity body = null;
    body = new StringEntity(params);
    request.setEntity(body);

    log.debug("executing http request");
    HttpResponse result = httpClient.execute(request);
    log.debug("response received");
    
    log.debug(result.getStatusLine().toString());
    String jsonResult = EntityUtils.toString(result.getEntity(), "UTF-8");
    request.releaseConnection();
    
    // TODO: REMOVE after testing
    /*
    JsonParser jp = new JsonParser();
    JsonElement je = jp.parse(jsonResult);
    jsonResult = gson.toJson(je);
    */
    
    log.debug("jsonResult of response: {}", jsonResult);

    log.debug("gson.fromJson(...)");
    
    Response<ResponseType>  resp = gson.fromJson(jsonResult, clazz);

    if (resp == null) {
      resp = new Response<ResponseType>();
      resp.setMessage("Null response received");
      resp.setStatus(Response.STATUS_ERROR);
    }

    log.debug("returning response");
    return resp;
  }

  private HttpPost createRequest() {
    log.debug("createRequest() - start");
    HttpPost request = new HttpPost(getUrl());

    request.addHeader("content-type", "application/json");

    if (WebServiceBroker.isLoggedIn()) {
      request.addHeader("x-authorization-token", WebServiceBroker.getSessionToken());
    }

    long version = Updater.getInstalledVersion();
    request.addHeader("x-shellfirevpn-client-version", new Long(version).toString());

    request.addHeader("x-shellfirevpn-client-arch", Util.getArchitecture());

    String os = "";
    if (Util.isWindows()) {
      os = "win";
    } else {
      os = "osx";
    }
    request.addHeader("x-shellfirevpn-client-os", os);
    
    log.debug("createRequest() - finish");
    return request;
  }

  private String getUrl() {
    String url = broker .getEndPoint() + function;
    log.debug("getUrl() - returning {}", url);
    return url;
  }

  public Response<ResponseType> call(Type theType) throws ClientProtocolException, IOException, VpnException {
    return call(null, theType);
  }

}