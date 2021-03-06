package es.rul3s.raul.wifisecurityauditor;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WpsCallback;
import android.net.wifi.WpsInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class ManualConnectActivity extends Activity {
    public static WifiManager wifimgr;
    public Context context;
    private TextView tvLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_connect);

        wifimgr = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        tvLog = (TextView) findViewById(R.id.manual_tvLog);
        context = this;

        checkWifi();
    }

    private void checkWifi(){
        if(!wifimgr.isWifiEnabled()){
            Toast.makeText(this,"Wifi disabled, enabling...", Toast.LENGTH_SHORT).show();
            wifimgr.setWifiEnabled(true);
        }
    }

    public void btConnectAdp(View view){
        Toast.makeText(this,"Connect by FIXED", Toast.LENGTH_SHORT).show();
        String networkSSID = "ADP Informatica";
        //String networkBSSID = "64:70:02:6d:c9:c6";
        String networkPass = "academia";

        WifiConfiguration wifiConf = new WifiConfiguration();
        wifiConf.SSID = "\"" +networkSSID +"\"";
        //wifiConf.BSSID = "\"" +networkBSSID +"\"";
        wifiConf.preSharedKey = String.format("\"%s\"",networkPass);
        tvLog.append("SSID: " +wifiConf.SSID +"\n");
        tvLog.append("PASS: " +wifiConf.SSID +"\n");

        int netId = wifimgr.addNetwork(wifiConf);
        tvLog.append("NETID: " +netId +"\n");
        wifimgr.disconnect();
        boolean enableNet = wifimgr.enableNetwork(netId, true);
        tvLog.append("ENABLENETWORK: " +enableNet +"\n");
        wifimgr.reconnect();
        Toast.makeText(this,"Punto3", Toast.LENGTH_SHORT).show();
    }

    public void btConnectPass(View view){
        Toast.makeText(this,"Connect by PASS", Toast.LENGTH_SHORT).show();
        String networkSSID = "";
        String networkBSSID = "";
        String networkPass = "";

        WifiConfiguration wifiConf = new WifiConfiguration();
        wifiConf.SSID = "\"" +networkSSID +"\"";
        wifiConf.BSSID = "\"" +networkBSSID +"\"";
        wifiConf.preSharedKey = "\"" +networkPass +"\"";

        int netId = wifimgr.addNetwork(wifiConf);
        wifimgr.disconnect();
        wifimgr.enableNetwork(netId, true);
        wifimgr.reconnect();
    }

    private final WpsCallback mWpsCallback = new WpsCallback() {
        @Override
        public void onStarted(String pin) {
            Toast.makeText(context,"Trying pin " +pin,Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onSucceeded() {
            //mWpsComplete = true;
            Toast.makeText(context,"Connection Succeed",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFailed(int reason) {
            //mWpsComplete = true;
            String errorMessage;
            Toast.makeText(context,"REASON: " +reason,Toast.LENGTH_SHORT).show();
            switch (reason) {

                case WifiManager.WPS_OVERLAP_ERROR:
                    Toast.makeText(context,"Failed, WPS_OVERLAP_ERROR",Toast.LENGTH_SHORT).show();
                    //errorMessage = getString(R.string.wifi_wps_failed_overlap);
                    break;
                case WifiManager.WPS_WEP_PROHIBITED:
                    Toast.makeText(context,"Failed, WPS_WEP_PROHIBITED",Toast.LENGTH_SHORT).show();
                    //errorMessage = getString(R.string.wifi_wps_failed_wep);
                    break;
                case WifiManager.WPS_TKIP_ONLY_PROHIBITED:
                    Toast.makeText(context,"Failed, WPS_TKIP_ONLY_PROHIBITED",Toast.LENGTH_SHORT).show();
                    //errorMessage = getString(R.string.wifi_wps_failed_tkip);
                    break;
                case WifiManager.WPS_AUTH_FAILURE:
                    Toast.makeText(context,"Failed, AUTH_FAILURE",Toast.LENGTH_SHORT).show();
                    //mWifiManager.cancelWps(null);
                    //startWps();
                    return;
                case WifiManager.WPS_TIMED_OUT:
                    Toast.makeText(context,"Failed, WPS_TIMED_OUT",Toast.LENGTH_SHORT).show();
                    //startWps();
                    return;
                default:
                    Toast.makeText(context,"Failed, wifi_wps_failed_generic",Toast.LENGTH_SHORT).show();
                    //errorMessage = getString(R.string.wifi_wps_failed_generic);
                    break;
            }
            //displayFragment(createErrorFragment(errorMessage), true);

        }
    };

    public void btConnectWps(View view){
        Toast.makeText(this,"Connect by WPS", Toast.LENGTH_SHORT).show();

        WpsInfo wpsConfig = new WpsInfo();
        wpsConfig.BSSID = "b2:46:fc:67:39:38";
        wpsConfig.pin = "67648566";
        wifimgr.startWps(wpsConfig, mWpsCallback);
    }
}
