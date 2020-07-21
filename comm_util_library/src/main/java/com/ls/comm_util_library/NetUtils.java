/************************************************************
  *  * EaseMob CONFIDENTIAL
  * __________________
  * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
  *
  * NOTICE: All information contained herein is, and remains
  * the property of EaseMob Technologies.
  * Dissemination of this information or reproduction of this material
  * is strictly forbidden unless prior written permission is obtained
  * from EaseMob Technologies.
  */
package com.ls.comm_util_library;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;

@SuppressLint("MissingPermission")
public class NetUtils {
    public enum Types {
        NONE, WIFI, MOBILE, ETHERNET, OTHERS
    }

	private static final String TAG = "net";
	/**
	 * Check the network connection is available
	 *
	 * @param context
	 * @return
	 */
	public static boolean hasNetwork(Context context) {
		if (context != null) {
			ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
			if (networkInfo != null) {
				return networkInfo.isAvailable();
			}
			return false;
		}
		return false;
	}

	public static Types getNetworkTypes(Context context) {
	    Types types = Types.NONE;
        try {
            ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Network network = connManager.getActiveNetwork();
                NetworkCapabilities capabilities = connManager.getNetworkCapabilities(network);
                if (capabilities != null) {
                    // mobile
                    boolean cellular = capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR);
                    boolean wifi = capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI);
                    boolean bluetooth = capabilities.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH);
                    boolean ethernet = capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET);
                    boolean vpn = capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN);
                    if (cellular) {
                        types = Types.MOBILE;
                    } else if (wifi) {
                        types = Types.WIFI;
                    } else if (ethernet) {
                        types = Types.ETHERNET;
                    } else if (bluetooth || vpn) {
                        types = Types.OTHERS;
                    }
                }
                LogUtils.INSTANCE.i(TAG, "Network info: " + types);
            } else {
                NetworkInfo activeNetworkInfo = connManager.getActiveNetworkInfo();
                LogUtils.INSTANCE.i(TAG, "Active Network info: " + (activeNetworkInfo == null ? "null" : activeNetworkInfo.toString()));
                if (activeNetworkInfo != null && activeNetworkInfo.isAvailable() && activeNetworkInfo.isConnected()) {
                    int type = activeNetworkInfo.getType();
                    if (type == ConnectivityManager.TYPE_WIFI) {
                        types = Types.WIFI;
                    } else if (type == ConnectivityManager.TYPE_MOBILE) {
                        types = Types.MOBILE;
                    } else if (type == ConnectivityManager.TYPE_ETHERNET) {
                        types = Types.ETHERNET;
                    } else if (type == ConnectivityManager.TYPE_BLUETOOTH
                            || type == ConnectivityManager.TYPE_VPN) {
                        types = Types.OTHERS;
                    }
                }
            }
        } catch (Exception e) {
            LogUtils.INSTANCE.e(TAG, e.getMessage());
        }

        return types;
    }

    @TargetApi(13)
	public static boolean hasDataConnection(Context context) {
        return getNetworkTypes(context) != Types.NONE;
    }

	/**
	 * check the current network connection is wifi connection or not
	 * @param context
	 * @return
     * @deprecated  use {@link NetUtils#isWifiConnected(Context)}  instead
     */
    @Deprecated
    public static boolean isWifiConnection(Context context) {
        return isWifiConnected(context);
    }

    /**
     * check the current network connection is wifi connection or not
     * @param context
     * @return
     */
    public static boolean isWifiConnected(Context context)
    {
        try {
            ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Network network = connManager.getActiveNetwork();
                NetworkCapabilities capabilities = connManager.getNetworkCapabilities(network);

                if (capabilities == null) return false;

                return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI);
            } else {
                NetworkInfo info = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                return (info != null && info.isAvailable() && info.isConnected());
            }
        } catch (Exception e) {
            LogUtils.INSTANCE.e(TAG, e.getMessage());
            return false;
        }
	}

	/**
	 * check the current network connection is mobile connection or not
	 * @param context
	 * @return
     * @deprecated  use {@link NetUtils#isMobileConnected(Context)} instead
	 */
    @Deprecated
    public static boolean isMobileConnection(Context context)
    {
        return isMobileConnected(context);
    }

    /**
     * check the current network connection is mobile connection or not
     * @param context
     * @return
     */
    public static boolean isMobileConnected(Context context)
    {
        try {
            ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Network network = connManager.getActiveNetwork();
                NetworkCapabilities capabilities = connManager.getNetworkCapabilities(network);
                return capabilities != null && capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR);
            } else {
                NetworkInfo info = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                return (info != null && info.isAvailable() && info.isConnected());
            }
        } catch (Exception e) {
            LogUtils.INSTANCE.e(TAG, e.getMessage());
            return false;
        }
	}

	/**
	 * check the current network connection is mobile connection or not
	 * @param context
	 * @return
     * @deprecated  use {@link NetUtils#isEthernetConnected(Context)} instead
	 */
    @Deprecated
    public static boolean isEthernetConnection(Context context) {
        return isEthernetConnected(context);
    }

    /**
     * check the current network connection is mobile connection or not
     * @param context
     * @return
     */
    public static boolean isEthernetConnected(Context context)
    {
        try {
            ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Network network = connManager.getActiveNetwork();
                NetworkCapabilities capabilities = connManager.getNetworkCapabilities(network);
                return capabilities != null && capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR);
            } else {
                NetworkInfo info = connManager.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);
                return (info != null && info.isAvailable() && info.isConnected());
            }
        } catch (Exception e) {
            LogUtils.INSTANCE.e(TAG, e.getMessage());
            return false;
        }
	}

	public static boolean isOthersConnected(Context context) {
        try {
            ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Network network = connManager.getActiveNetwork();
                NetworkCapabilities capabilities = connManager.getNetworkCapabilities(network);

                if (capabilities == null) return false;

                boolean bluetooth = capabilities.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH);
                boolean vpn = capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN);
                return bluetooth || vpn;
            } else {
                NetworkInfo bluetoothInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_BLUETOOTH);
                NetworkInfo vpnInfo = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    vpnInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_VPN);
                }
                return (bluetoothInfo != null && bluetoothInfo.isAvailable() && bluetoothInfo.isConnected())
                        || (vpnInfo != null && vpnInfo.isAvailable() && vpnInfo.isConnected());
            }
        } catch (Exception e) {
            LogUtils.INSTANCE.e(TAG, e.getMessage());
            return false;
        }
    }

    /**
     * get wifi SSID
     */
    public static String getWiFiSSID(Context context) {
        WifiManager wifi_service = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        String SSID = "";
        try {
            WifiInfo wifiInfo = wifi_service.getConnectionInfo();
            SSID = wifiInfo.getSSID();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return SSID;
    }

	/**
	 * for wifi connection, we use 100kbytes for write buffer
	 * for 3/4G connection, we use 20kbytes
	 * for grps, we use 1k
	 * @return
	 */
	public static int getUploadBufSize(Context context) {
	    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo info = cm.getActiveNetworkInfo();
	    if (info != null && info.getType() == ConnectivityManager.TYPE_WIFI) {
	        return MAX_SPEED_UPLOAD_BUF_SIZE;
	    }
        if (info != null && info.getType() == ConnectivityManager.TYPE_ETHERNET) {
            return MAX_SPEED_UPLOAD_BUF_SIZE;
        }
	    if (info != null && isConnectionFast(info.getType(), info.getSubtype())) {
	        return HIGH_SPEED_UPLOAD_BUF_SIZE;
	    }
	    return LOW_SPEED_UPLOAD_BUF_SIZE;
	}

	public static int getDownloadBufSize(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info != null && info.getType() == ConnectivityManager.TYPE_WIFI) {
            return MAX_SPEED_DOWNLOAD_BUF_SIZE;
        }
        if (Build.VERSION.SDK_INT >= 13) {
            if (info != null && info.getType() == ConnectivityManager.TYPE_ETHERNET) {
                return MAX_SPEED_DOWNLOAD_BUF_SIZE;
            }
        }
        if (info != null && isConnectionFast(info.getType(), info.getSubtype())) {
            return HIGH_SPEED_DOWNLOAD_BUF_SIZE;
        }
        return LOW_SPEED_DOWNLOAD_BUF_SIZE;
    }

	private static boolean isConnectionFast(int type, int subType) {
	    if(type==ConnectivityManager.TYPE_WIFI){
            return true;
        } else if (Build.VERSION.SDK_INT >= 13 && type == ConnectivityManager.TYPE_ETHERNET) {
            return true;
        }else if(type==ConnectivityManager.TYPE_MOBILE){
            switch(subType){
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                return false; // ~ 50-100 kbps
            case TelephonyManager.NETWORK_TYPE_CDMA:
                return false; // ~ 14-64 kbps
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return false; // ~ 50-100 kbps
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                return true; // ~ 400-1000 kbps
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                return true; // ~ 600-1400 kbps
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return false; // ~ 100 kbps
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return true; // ~ 2-14 Mbps
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return true; // ~ 700-1700 kbps
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return true; // ~ 1-23 Mbps
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return true; // ~ 400-7000 kbps
            }

            if(android.os.Build.VERSION.SDK_INT >= 11) {
                if (subType == TelephonyManager.NETWORK_TYPE_EHRPD || //1-2Mbps
                        subType == TelephonyManager.NETWORK_TYPE_LTE) { //10+ Mbps
                    return true;
                }
            }
            if (android.os.Build.VERSION.SDK_INT >= 9) {
                if (subType == TelephonyManager.NETWORK_TYPE_EVDO_B) { // 5Mbps
                    return true;
                }
            }

            if (android.os.Build.VERSION.SDK_INT >= 8) {
                if (subType == TelephonyManager.NETWORK_TYPE_IDEN) { // 25 kbps
                    return false;
                }
            }
        }
        return false;
	}

	public static String getNetworkType(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable()) {
            int networkType = networkInfo.getType();

            if (android.os.Build.VERSION.SDK_INT >= 13 && networkType == ConnectivityManager.TYPE_ETHERNET) {
                return "ETHERNET";
            } else if (networkType == ConnectivityManager.TYPE_WIFI) {
                return "WIFI";
            }else {
                TelephonyManager telephonyManager = (TelephonyManager) context
                        .getSystemService(Context.TELEPHONY_SERVICE);
                switch (telephonyManager.getNetworkType()) {
                case TelephonyManager.NETWORK_TYPE_GPRS:
                case TelephonyManager.NETWORK_TYPE_EDGE:
                case TelephonyManager.NETWORK_TYPE_CDMA:
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                case TelephonyManager.NETWORK_TYPE_IDEN:
                    return "2G";
                case TelephonyManager.NETWORK_TYPE_UMTS:
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                case TelephonyManager.NETWORK_TYPE_HSPA:
                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                case TelephonyManager.NETWORK_TYPE_EHRPD:
                case TelephonyManager.NETWORK_TYPE_HSPAP:
                    return "3G";
                case TelephonyManager.NETWORK_TYPE_LTE:
                    return "4G";
                default:
                    return "unkonw network";
                }
            }


        } else {

            return "no network";
        }
    }

	private static final int LOW_SPEED_UPLOAD_BUF_SIZE = 1024;
	private static final int HIGH_SPEED_UPLOAD_BUF_SIZE = 1024*10;
	private static final int MAX_SPEED_UPLOAD_BUF_SIZE = 1024*100;

	private static final int LOW_SPEED_DOWNLOAD_BUF_SIZE = 2024;
    private static final int HIGH_SPEED_DOWNLOAD_BUF_SIZE = 1024*30;
    private static final int MAX_SPEED_DOWNLOAD_BUF_SIZE = 1024*100;

}
