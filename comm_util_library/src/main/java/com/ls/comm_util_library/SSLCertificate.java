package com.ls.comm_util_library;

import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Arrays;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * https自定义证书验证
 */
public class SSLCertificate {
    private SSLSocketFactory mSSLSocketFactory;
    private X509TrustManager mTrustManager;
    private HostnameVerifier mHostnameVerifier;

    /**
     * 初始化单向认证
     * @param domain 需要验证的域名，不需要验证可传null或""
     * @param aliasPrefix 别名前缀（随便起）
     * @param certificates 证书集合
     * @throws CertificateException
     * @throws NoSuchAlgorithmException
     * @throws KeyStoreException
     * @throws IOException
     * @throws KeyManagementException
     */
    public void initSingleCertificate(String domain,String aliasPrefix,InputStream... certificates)
            throws CertificateException,
                NoSuchAlgorithmException,
                KeyStoreException,
                IOException,
                KeyManagementException {
        initCertificate(domain,null,generateTrustManager(aliasPrefix, certificates));
    }

    /**
     * 初始化双向认证
     * @param domain 需要验证的域名，不需要验证可传null或""
     * @param p12 客户端证书 .p12
     * @param password p12密码
     * @param aliasPrefix 别名前缀（随便起）
     * @param certificates 证书集合
     * @throws CertificateException
     * @throws NoSuchAlgorithmException
     * @throws KeyStoreException
     * @throws IOException
     * @throws UnrecoverableKeyException
     * @throws KeyManagementException
     */
    public void initDoubleCertificate(String domain,InputStream p12,String password,String aliasPrefix,InputStream... certificates)
            throws CertificateException,
                NoSuchAlgorithmException,
                KeyStoreException,
                IOException,
                UnrecoverableKeyException,
                KeyManagementException {
    	initCertificate(domain,generateKeyManagers(p12, password),generateTrustManager(aliasPrefix, certificates));
    }

    public SSLSocketFactory getSslSocketFactory() {
        return mSSLSocketFactory;
    }

    public X509TrustManager getTrustManager() {
        return mTrustManager;
    }
    
    public HostnameVerifier getHostnameVerifier(){
    	return mHostnameVerifier;
    }
    
    private void initCertificate(String domainName,KeyManager[] keyManagers,X509TrustManager trustManager)
    		throws KeyManagementException, NoSuchAlgorithmException{
    	this.mTrustManager = trustManager;
    	this.mSSLSocketFactory = generateSSlSocketFactory(keyManagers,trustManager);
    	this.mHostnameVerifier = generateHostnameVerifier(domainName);
    }
    
    private HostnameVerifier generateHostnameVerifier(final String domain){
    	return new HostnameVerifier() {
			
			@Override
			public boolean verify(String hostname, SSLSession session) {
				if(!TextUtils.isEmpty(domain)){
				    return domain.contains(hostname);
                }
				return true;
			}
		};
    }

    private X509TrustManager generateTrustManager(String aliasPrefix,InputStream... certificates) throws CertificateException, KeyStoreException, IOException, NoSuchAlgorithmException {
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        //去掉系统默认证书
        keyStore.load(null);
        int index = 0;
        String certificateAlias;
        for (InputStream certificate: certificates){
            certificateAlias = aliasPrefix + index++;
            keyStore.setCertificateEntry(certificateAlias,certificateFactory.generateCertificate(certificate));
            if(certificate != null){
                try{
                    certificate.close();
                }
                catch (IOException ex){
                    Log.e("SSLCertificate","close " + certificateAlias + " fail!!!");
                    ex.printStackTrace();
                }
            }
        }
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);
        //信任管理器
        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
            throw new IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers));
        }
        return (X509TrustManager) trustManagers[0];
    }

    private KeyManager[] generateKeyManagers(InputStream p12,String password) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, UnrecoverableKeyException {
        // 服务器端需要验证的客户端证书
        KeyStore clientKeyStore = KeyStore.getInstance("PKCS12");
        clientKeyStore.load(p12, password.toCharArray());
        //密钥管理器（用于管理服务端要验证的客户端的证书）
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(clientKeyStore, password.toCharArray());
        return keyManagerFactory.getKeyManagers();
    }

    private SSLSocketFactory generateSSlSocketFactory(KeyManager[] keyManagers,TrustManager trustManager) throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagers, new TrustManager[]{trustManager}, new SecureRandom());
        return sslContext.getSocketFactory();
    }
}
