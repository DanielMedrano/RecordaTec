package mx.edu.ittepic.judamedranoba.recordatec;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import static android.content.Context.WIFI_SERVICE;

/**
 * Created by twarrios on 07/11/2017.
 */

public class php {
    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    String IP2 = getLocalIpAddress();
    String IP = "http://172.20.4.18";
    String RUTA = IP+"/php";
    String GET_ALUMNO_BY_ID = RUTA + "/obtener_alumnos_por_id.php";
    String GET_TAREAS = RUTA + "/obtener_tareas.php";
    String GET_TAREAS_POR_ID = RUTA + "/obtener_tareas_por_id.php";
    String GET_MATERIAS = RUTA + "/obtener_materias.php";
    String INSERT_TAREA = RUTA + "/insert_tarea.php";
}
