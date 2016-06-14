package it.iubar.desktopApi.DBClasses;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ClientTest {

    class BrokenClient extends JSONPrinter {
        private String s;

        BrokenClient(String s){
            this.setS(s);
        }

        public String getS() {
            return s;
        }

        public void setS(String s) {
            this.s = s;
        }
    }

    Client client;
    BrokenClient brokenClient;

    @Before
    public void before(){
        client = new Client(1, "00-1C-F0-99-84-3E", 13, "00.92.01", "", "Windows Vista", "6.0", "", 0, 0, 0, "151.21.219.26", "192.168.32.2", "ONE", "FABIO", "127.0.0.1", "localhost", "", "", "0000-00-00", "00.92", "2009-08-18 16:37:54", "2009-08-18 20:33:47");
        brokenClient = new BrokenClient("s");
    }

    @Test
    public void testToJSon() throws Exception {
        String str = "{\"idclient\":1,\"mac\":\"00-1C-F0-99-84-3E\",\"idapp\":13,\"version\":\"00.92.01\",\"platform\":\"\",\"os_name\":\"Windows Vista\",\"os_version\":\"6.0\",\"java_version\":\"\",\"titolari\":0,\"datori\":0,\"lavoratori\":0,\"ip_wan\":\"151.21.219.26\",\"ip_local\":\"192.168.32.2\",\"host_name\":\"ONE\",\"user_name\":\"FABIO\",\"server_ip\":\"127.0.0.1\",\"server_name\":\"localhost\",\"reg_key\":\"\",\"act_key\":\"\",\"db_date\":\"0000-00-00\",\"db_version\":\"00.92\",\"info_added\":\"2009-08-18 16:37:54\",\"info_uploaded\":\"2009-08-18 20:33:47\"}";
        assertEquals(str, client.toJson());
    }

    @Test(expected = CloneNotSupportedException.class)
    public void testBrokenToJson() throws Exception{
        brokenClient.toJson();
    }

}