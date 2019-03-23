package com.cttic.liugw.rsa;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.HashMap;


import javax.crypto.Cipher;

/**
 * js 加密
 *
 * 账号：<input type="text" id="username" ><br><br>
 * 密码：<input type="password" id="password"><br><br>
 *
 * <input id="publicKeyExponent" value="" type="hidden"> //后台传过来的公钥
 *
 * <input id="publicKeyModulus" value="" type="hidden">//后台传过来的模
 *
 * <script>
 * $(function(){
 * //生成登陆用RSA公钥 密钥
 * $.ajax({
 * url:'${basePath}UserText/loginRSA.do',
 * type:'post',
 * dataType:'json',
 * success:function(data){
 * $("#publicKeyExponent").val(data[0]);
 * $("#publicKeyModulus").val(data[1]);
 * }
 * })
 *
 * });
 *
 * //点击登录按钮，执行的方法
 *
 * function login1(){
 * var username=$("#username").val();
 * var password=$("#password").val();
 * //RSA加密
 *         var publicKeyExponent=$("#publicKeyExponent").val();
 *         var publicKeyModulus=$("#publicKeyModulus").val();
 *         RSAUtils.setMaxDigits(200);
 * var key = new RSAUtils.getKeyPair(publicKeyExponent, "", publicKeyModulus);
 * var userNameEncrypt = RSAUtils.encryptedString(key,username.split("").reverse().join(""));
 * var  userPwdEncrypt= RSAUtils.encryptedString(key,password.split("").reverse().join(""));
 *
 * $.ajax({
 * url:"${basePath}UserText/textLogin.do",
 * data:'post',
 * dataType:'json',
 * data:{"username":userNameEncrypt,"password":userPwdEncrypt},
 * success:function(data){
 * if(data=='success'){
 * alert("登录成功")
 * window.location.href="${basePath}UserText/textList.do";
 *
 * }
 * else if(data=='usernameIsNull'){
 * alert("账号错误")
 * }
 * else if(data=='passwordIsNull'){
 * alert("密码错误")
 * }
 * else{
 * alert("登录失败")
 * }
 * }
 * })
 * }
 * </script>
 *
 *    //java 后台接收，解密
 *
 *     //生成登陆用RSA公钥 密钥
 *    @RequestMapping(value="/loginRSA")
 *    @ResponseBody
 *     public List<String> loginRSA(HttpServletRequest request){
 *     //	HttpServletRequest request = ServletActionContext.getRequest();
 * String publicKeyExponent="";
 * String publicKeyModulus="";
 *  
 * try {
 * HashMap<String, Object> map = RSAUtils.getKeys();
 * //生成公钥和私钥    
 *   RSAPublicKey publicKey = (RSAPublicKey) map.get("public");  
 *   RSAPrivateKey privateKey = (RSAPrivateKey) map.get("private");  
 *   //私钥保存在session中，用于解密  
 *   request.getSession().setAttribute("privateKeyLogin", privateKey);  
 *   //公钥信息保存在页面，用于加密   公钥指数  
 *   publicKeyExponent = publicKey.getPublicExponent().toString(16);
 *   System.out.println("zoule1:"+publicKeyExponent);
 *   //模 
 *   publicKeyModulus = publicKey.getModulus().toString(16);
 *   System.out.println("zoule2:"+publicKeyModulus);
 *  
 * // request.getSession().setAttribute("publicKeyExponent", publicKeyExponent);  
 *   //request.getSession().setAttribute("publicKeyModulus", publicKeyModulus); 
 * } catch (Exception e) {
 * log.error("RSA生成公钥错误",e);
 * }
 *   List<String> list=new ArrayList<String>();
 *   list.add(publicKeyExponent);
 *   list.add(publicKeyModulus);
 * return list;
 *     }
 * //登录验证
 * @RequestMapping(value="/textLogin")
 * @ResponseBody
 * public String textLogin(String username,String password,HttpServletRequest request){
 *
 * if(org.apache.commons.lang.StringUtils.isBlank(username)){
 * System.out.println("the username is null");
 * return "usernameIsNull";
 * }
 * if(org.apache.commons.lang.StringUtils.isBlank(password)){
 * System.out.println("the password is null");
 * return "passwordIsNull";
 * }
 * RSAPrivateKey privateKey = (RSAPrivateKey) request.getSession().getAttribute("privateKeyLogin");
 * try {
 * username = RSAUtils.decryptByPrivateKey(username, privateKey);
 * System.out.println("解密后1"+username);
 * password=RSAUtils.decryptByPrivateKey(password, privateKey);
 * System.out.println("解密后2"+password);
 * } catch (Exception e) {
 * log.error("RSA解密失败", e);
 * }
 * User user=userService.getUser(username);
 * if(user!=null){
 * if(password.equals(user.getPassword())){
 * System.out.println("登陆成功");
 * return "success";
 * }
 * }
 * System.out.println("登录失败");
 * return "fail";
 *
 * }
 */
public class RSAUtils {
/**
   * 生成公钥和私钥
   * @throws NoSuchAlgorithmException
   *
   */
    public static HashMap<String, Object> getKeys() throws NoSuchAlgorithmException{
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        HashMap<String, Object> map = new HashMap<String, Object>();
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA", new org.bouncycastle.jce.provider.BouncyCastleProvider());
        keyPairGen.initialize(1024);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        map.put("public", publicKey);
        map.put("private", privateKey);
        return map;
    }
    /**
       * 使用模和指数生成RSA公钥
       * @param modulus 模
       * @param exponent 指数
       * @return
       */
    public static RSAPublicKey getPublicKey(String modulus, String exponent) {
      Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        try {
            BigInteger b1 = new BigInteger(modulus);
            BigInteger b2 = new BigInteger(exponent);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA", new org.bouncycastle.jce.provider.BouncyCastleProvider());
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(b1, b2);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
       * 使用模和指数生成RSA私钥
       * /None/NoPadding
       * @param modulus  模
       * @param exponent 指数
       * @return
       */
    public static RSAPrivateKey getPrivateKey(String modulus, String exponent) {
        try {
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            BigInteger b1 = new BigInteger(modulus);
            BigInteger b2 = new BigInteger(exponent);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA", new org.bouncycastle.jce.provider.BouncyCastleProvider());
            RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(b1, b2);
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
    * 公钥加密
    *
    * @param data
    * @param publicKey
    * @return
    * @throws Exception
     */
    public static String encryptByPublicKey(String data, RSAPublicKey publicKey) throws Exception {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        Cipher cipher = Cipher.getInstance("RSA", new org.bouncycastle.jce.provider.BouncyCastleProvider());
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        // 模长
        int key_len = publicKey.getModulus().bitLength() / 8;
        // 加密数据长度 <= 模长-11
        String[] datas = splitString(data, key_len - 11);
        String mi = "";
        //如果明文长度大于模长-11则要分组加密
        for (String s : datas) {
          mi += bcd2Str(cipher.doFinal(s.getBytes()));
        }
        return mi;
    }

    /**
    * 私钥解密
    *
    * @param data
    * @param privateKey
    * @return
    * @throws Exception
    */
    public static String decryptByPrivateKey(String data, RSAPrivateKey privateKey) throws Exception {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        Cipher cipher = Cipher.getInstance("RSA", new org.bouncycastle.jce.provider.BouncyCastleProvider());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        //模长
        int key_len = privateKey.getModulus().bitLength() / 8;
        byte[] bytes = data.getBytes();
        byte[] bcd = ASCII_To_BCD(bytes, bytes.length);
        //System.err.println(bcd.length);
        //如果密文长度大于模长则要分组解密
        String ming = "";
        byte[][] arrays = splitArray(bcd, key_len);
        for(byte[] arr : arrays){
          ming += new String(cipher.doFinal(arr));
        }
        return ming;
    }

    /**
    * ASCII码转BCD码
    *
    */
    public static byte[] ASCII_To_BCD(byte[] ascii, int asc_len) {
        byte[] bcd = new byte[asc_len / 2];
        int j = 0;
        for (int i = 0; i < (asc_len + 1) / 2; i++) {
          bcd[i] = asc_to_bcd(ascii[j++]);
          bcd[i] = (byte) (((j >= asc_len) ? 0x00 : asc_to_bcd(ascii[j++])) + (bcd[i] << 4));
        }
        return bcd;
    }

    public static byte asc_to_bcd(byte asc) {
        byte bcd;

        if ((asc >= '0') && (asc <= '9'))
          bcd = (byte) (asc - '0');
        else if ((asc >= 'A') && (asc <= 'F'))
          bcd = (byte) (asc - 'A' + 10);
        else if ((asc >= 'a') && (asc <= 'f'))
          bcd = (byte) (asc - 'a' + 10);
        else
          bcd = (byte) (asc - 48);
        return bcd;
    }

    /**
    * BCD转字符串
    */
    public static String bcd2Str(byte[] bytes) {
        char temp[] = new char[bytes.length * 2], val;

        for (int i = 0; i < bytes.length; i++) {
          val = (char) (((bytes[i] & 0xf0) >> 4) & 0x0f);
          temp[i * 2] = (char) (val > 9 ? val + 'A' - 10 : val + '0');

          val = (char) (bytes[i] & 0x0f);
          temp[i * 2 + 1] = (char) (val > 9 ? val + 'A' - 10 : val + '0');
        }
        return new String(temp);
    }

    /**
    * 拆分字符串
    */
    public static String[] splitString(String string, int len) {
        int x = string.length() / len;
        int y = string.length() % len;
        int z = 0;
        if (y != 0) {
          z = 1;
        }
        String[] strings = new String[x + z];
        String str = "";
        for (int i=0; i<x+z; i++) {
          if (i==x+z-1 && y!=0) {
            str = string.substring(i*len, i*len+y);
          }else{
            str = string.substring(i*len, i*len+len);
          }
          strings[i] = str;
        }
        return strings;
    }

    /**
    *拆分数组
    */
    public static byte[][] splitArray(byte[] data,int len){
        int x = data.length / len;
        int y = data.length % len;
        int z = 0;
        if(y!=0){
          z = 1;
        }
        byte[][] arrays = new byte[x+z][];
        byte[] arr;
        for(int i=0; i<x+z; i++){
          arr = new byte[len];
          if(i==x+z-1 && y!=0){
            System.arraycopy(data, i*len, arr, 0, y);
          }else{
            System.arraycopy(data, i*len, arr, 0, len);
          }
          arrays[i] = arr;
        }
        return arrays;
     }

    public static void main(String[] args) throws Exception{
        HashMap<String, Object> map = getKeys();
        //生成公钥和私钥
        RSAPublicKey publicKey = (RSAPublicKey) map.get("public");
        RSAPrivateKey privateKey = (RSAPrivateKey) map.get("private");

        //模
        String modulus = publicKey.getModulus().toString();
        System.out.println("pubkey modulus="+modulus);
        //公钥指数
        String public_exponent = publicKey.getPublicExponent().toString();
        System.out.println("pubkey exponent="+public_exponent);
        //私钥指数
        String private_exponent = privateKey.getPrivateExponent().toString();
        System.out.println("private exponent="+private_exponent);
        //明文
        String ming = "123456";
        //使用模和指数生成公钥和私钥
        RSAPublicKey pubKey = RSAUtils.getPublicKey(modulus, public_exponent);
        RSAPrivateKey priKey = RSAUtils.getPrivateKey(modulus, private_exponent);
        //加密后的密文
        String mi = RSAUtils.encryptByPublicKey(ming, pubKey);
        System.err.println("mi="+mi);
        //解密后的明文
        String ming2 = RSAUtils.decryptByPrivateKey(mi, priKey);
        System.err.println("ming2="+ming2);

        ////////////////////////////// 测试
        // 密文
        String miWenTestStr="C254A993B20CFB2C931B32F007BA700B9C33EC7579D094D0F456A620E5F458E8A4A95BBE29A0E0061140335D31D1262988218F7D39251913D6B740656AF1F648E4B0FB6621EDE7F8490CB759F597F30CE4CD2A66D89EC2B7DA1EDC777D87677D0F0E0E34ECC25D965A2CC84FF88016B39D6CA110A2C7B85EF7AE5A4DB9BEA609";
        // 私钥指数
        String private_exponentTest ="31607247710414572052111010486566399355919902904076983192679596030735752611323122104556763892307836049851004939205476052544224206912195550768529284321408159476618615582629385057722273483275410389848159239002538600786685123527907370382154408610347594406891270862787166302444473086609184920462160069361742670713";
        // 模数
        String modulusTest = "140818775880179456735499612118157859591361160885417623895217041812802788503622260595944026866769452834744072787267796332806989928511526839613671224104155465540308469931613141183319266223238312609833822262448759682275729766715183818992927745854853590923759495525497716042200061484836563697656414270095041837433";
        // 根据私钥指数和模获取私钥
        RSAPrivateKey priKeyTest = RSAUtils.getPrivateKey(modulusTest, private_exponentTest);

        // 使用私钥对密文解密获得明文
        String mingWenTest = RSAUtils.decryptByPrivateKey(miWenTestStr, priKeyTest);
        System.out.println("解密后的明文：" + mingWenTest);


        String miwen = "LRIvUBaBgQkcxxK4Q0lls1gx33GU5yEYkeND/5A/fOHo7m/PR4lmIUOgQEkJj6jMhnIudNgJN4lgpdPe6Rqo5k/fYlPGET9CiALWafN5SKLp6PNT9LTAI5DUKQkaca6jlGmF3zhtPgsgx1sxgSZi7F8sCWbfXarMPq4gT+P8UqQ=";
        mingWenTest = RSAUtils.decryptByPrivateKey(miWenTestStr, priKeyTest);
        System.out.println("解密后的明文：" + mingWenTest);

    }
}
