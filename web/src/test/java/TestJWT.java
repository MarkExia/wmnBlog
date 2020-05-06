import com.wmn.entity.UserInfo;
import com.wmn.utils.JWTToken;
import org.junit.Test;

public class TestJWT {
        @Test
      public  void  main() throws Exception {
            UserInfo u = new UserInfo();
            u.setId(1);
            u.setNickname("admin");
            u.setRealname("wmn");
            String s = JWTToken.createToken(u);
            System.out.println(s);
      }
      @Test
      public void verToken(){
            JWTToken.verifyToken("eyJhbGciOiJIUzUxMiJ9.eyJyZWFsTmFtZSI6IndtbiIsIm5pY2tOYW1lIjoiYWRtaW4iLCJ0eXAiOiJKV1QiLCJpZCI6MSwiYWxnIjoiSFMyNTYiLCJleHAiOjE1Njk0MjY1NjUsImlhdCI6MTU2OTQyNDc2NX0.GZm6XpwIjln10yr4N1PezNSUw5dbMAA-kwvWb2eSOQGaBK4Vt-HxFiywQ4AA5rwcRGsM3IQomcYMhATH-MQRaQ");
      }
}
