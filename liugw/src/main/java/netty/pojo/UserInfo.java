package netty.pojo;

import java.io.Serializable;
import java.nio.ByteBuffer;

public class UserInfo implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String userName;
    private int userId;

    public final String getUserName() {
        return userName;
    }
    public final void setUserName(String userName) {
        this.userName = userName;
    }
    public final int getUserId() {
        return userId;
    }
    public final void setUserId(int userId) {
        this.userId = userId;
    }
    
    public UserInfo buildUserName(String userName) {
        this.userName = userName;
        return this;
    }
    
    public UserInfo buildUserID(int userID) {
        this.userId = userID;
        return this;
    }
    
    public byte[] codeC() {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        byte[] value = this.userName.getBytes();
        buffer.putInt(value.length);
        buffer.put(value);
        buffer.putInt(this.userId);
        buffer.flip();
        value = null;
        byte[] resultBytes = new byte[buffer.remaining()];
        buffer.get(resultBytes);
        return resultBytes;
    }
    
    public byte[] codeC2(ByteBuffer buffer) {
        buffer.clear();
        byte[] value = this.userName.getBytes();
        buffer.putInt(value.length);
        buffer.put(value);
        buffer.putInt(this.userId);
        buffer.flip();
        value = null;
        byte[] resultBytes = new byte[buffer.remaining()];
        buffer.get(resultBytes);
        return resultBytes;
    }
}
