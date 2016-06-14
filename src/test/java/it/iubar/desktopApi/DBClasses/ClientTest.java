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
        client = new Client(2323, "123325345234134", 2323, "8697623", "", "Windows Vista", "6.3", "", 0, 0, 0, "151.21.219.26", "192.168.32.2", "TWO", "TOMMASO", "127.0.0.1", "localhost", "", "", "0000-00-00", "00.92", "2009-08-18 16:37:54", "2009-08-18 20:33:47");
        brokenClient = new BrokenClient("s");
    }

    @Test
    public void testToJSon() throws Exception {
        String str = "{\n" +
                "  \"idclient\" : 2323,\n" +
                "  \"mac\" : \"123325345234134\",\n" +
                "  \"idapp\" : 2323,\n" +
                "  \"version\" : \"8697623\",\n" +
                "  \"platform\" : \"\",\n" +
                "  \"os_name\" : \"Windows Vista\",\n" +
                "  \"os_version\" : \"6.3\",\n" +
                "  \"java_version\" : \"\",\n" +
                "  \"titolari\" : 0,\n" +
                "  \"datori\" : 0,\n" +
                "  \"lavoratori\" : 0,\n" +
                "  \"ip_wan\" : \"151.21.219.26\",\n" +
                "  \"ip_local\" : \"192.168.32.2\",\n" +
                "  \"host_name\" : \"TWO\",\n" +
                "  \"user_name\" : \"TOMMASO\",\n" +
                "  \"server_ip\" : \"127.0.0.1\",\n" +
                "  \"server_name\" : \"localhost\",\n" +
                "  \"reg_key\" : \"\",\n" +
                "  \"act_key\" : \"\",\n" +
                "  \"db_date\" : \"0000-00-00\",\n" +
                "  \"db_version\" : \"00.92\",\n" +
                "  \"info_added\" : \"2009-08-18 16:37:54\",\n" +
                "  \"info_uploaded\" : \"2009-08-18 20:33:47\"\n" +
                "}";
        assertEquals(str, client.toJson());
    }

    @Test(expected = CloneNotSupportedException.class)
    public void testBrokenToJson() throws Exception{
        brokenClient.toJson();
    }

}