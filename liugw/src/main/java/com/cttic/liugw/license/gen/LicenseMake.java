package com.cttic.liugw.license.gen;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.prefs.Preferences;

import javax.security.auth.x500.X500Principal;

import com.cttic.liugw.license.LicenseManagerHolder;

import de.schlichtherle.license.CipherParam;
import de.schlichtherle.license.DefaultCipherParam;
import de.schlichtherle.license.DefaultKeyStoreParam;
import de.schlichtherle.license.DefaultLicenseParam;
import de.schlichtherle.license.KeyStoreParam;
import de.schlichtherle.license.LicenseContent;
import de.schlichtherle.license.LicenseManager;
import de.schlichtherle.license.LicenseParam;

public class LicenseMake {

    private String licPath;
    private String issued;
    private String notBefore;
    private String notAfter;
    private String consumerType;
    private int consumerAmount;
    private String info;

    // 私钥的别名
    private String priAlias;

    // 生成密钥对的密码
    private String privateKeyPwd;

    // 使用keytool生成密钥对时，设置的密钥库的访问密码
    private String keyStorePwd;

    private String subject;

    private String priPath;

    // X500Princal是一个证书文件的固有格式，详见API  [姓氏，组织单位，组织，区域，城市，国家地区]
    private final static X500Principal DEFAULTHOLDERANDISSUER = new X500Principal(
            "CN=liugw, OU=liugw, O=liugw, L=china, ST=beijing, C=china");

    public LicenseMake() {
    }

    public LicenseMake(String confPath) {
        initParam(confPath);
    }

    private void initParam(String confPath) {
        System.out.println("confPath=" + confPath);
        // 获取参数
        Properties properties = new Properties();

        InputStream inputStream = this.getClass().getResourceAsStream(confPath);

        try {
            properties.load(inputStream);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // 获取参数
        priAlias = properties.getProperty("private.key.alias");
        privateKeyPwd = properties.getProperty("private.key.pwd");
        keyStorePwd = properties.getProperty("key.store.pwd");
        subject = properties.getProperty("subject");
        priPath = properties.getProperty("priPath");
        licPath = properties.getProperty("licPath");
        // license content  
        issued = properties.getProperty("issuedTime");
        notBefore = properties.getProperty("notBefore");
        notAfter = properties.getProperty("notAfter");
        consumerType = properties.getProperty("consumerType");
        consumerAmount = Integer.valueOf(properties.getProperty("consumerAmount"));
        info = properties.getProperty("info");
    }

    /*
     * 初始化证书的相关参数
     */
    private LicenseParam initLicenseParams() {
        Class<LicenseMake> clazz = LicenseMake.class;

        Preferences preferences = Preferences.userNodeForPackage(clazz);

        // 设置对证书内容加密的对称密码
        CipherParam cipherParam = new DefaultCipherParam(this.keyStorePwd);

        //参数1,2从哪个Class.getResource()获得密钥库;  
        //参数3密钥库的别名;  
        //参数4密钥库存储密码;  
        //参数5密钥库密码
        KeyStoreParam privateStoreParam = new DefaultKeyStoreParam(clazz, priPath, priAlias, keyStorePwd,
                privateKeyPwd);

        // 返回生成证书时需要的参数
        LicenseParam licenseParam = new DefaultLicenseParam(subject, preferences, privateStoreParam, cipherParam);

        return licenseParam;
    }

    /**
     * 通过外部配置文件构建证书的的相关信息
     * 
     * @return
     * @throws ParseException
     */
    public LicenseContent buildLicenseContent() throws ParseException {
        LicenseContent content = new LicenseContent();
        SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd");

        content.setConsumerAmount(consumerAmount);
        content.setConsumerType(consumerType);
        content.setHolder(DEFAULTHOLDERANDISSUER);
        content.setIssuer(DEFAULTHOLDERANDISSUER);
        content.setIssued(formate.parse(issued));
        content.setNotBefore(formate.parse(notBefore));
        content.setNotAfter(formate.parse(notAfter));
        content.setInfo(info);
        content.setExtra(new Object());

        return content;
    }

    /**
     * 生成证书，在证书发布者端执行 , 可以封装为独立的一个小程序
     * 
     * @throws Exception
     */
    public void create() throws Exception {
        LicenseManager licenseManager = LicenseManagerHolder.getLicenseManager(initLicenseParams());
        LicenseContent content = buildLicenseContent();
        licenseManager.store(content, new File(licPath));
        System.out.println("证书发布成功");
    }

    /**
     * target 本java class目录下会生成 license.lic 文件即提供给使用者的软件授权证书文件
     * 
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        LicenseMake clicense = new LicenseMake("licenseMakeConf.properties");
        clicense.create();

    }
}
