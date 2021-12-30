package UUIDTest;

import org.junit.Test;

import java.util.UUID;

public class UUIDtest {
    @Test
    public  void getUUID(){
        String s = UUID.randomUUID().toString().replaceAll("-", "");
        System.out.println(s);
    }
}
