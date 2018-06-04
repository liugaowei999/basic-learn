package com.cttic.liugw.license;

import de.schlichtherle.license.LicenseManager;
import de.schlichtherle.license.LicenseParam;

/**
 * 单例模式下的证书管理器
 * 
 * @author liugaowei
 *
 */
public class LicenseManagerHolder {
    private static LicenseManager licenseManager;

    // 私有的构造函数
    private LicenseManagerHolder() {
    }

    // 不会出现高并发操作， 直接使用同步方法
    public static synchronized LicenseManager getLicenseManager(LicenseParam param) {
        if (licenseManager == null) {
            licenseManager = new LicenseManager(param);
        }
        return licenseManager;
    }

}
