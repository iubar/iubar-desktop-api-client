package it.iubar.desktop.api.models;

import it.iubar.desktop.api.services.JSONPrinter;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ClientTest {

    static Client client;

    @BeforeClass
    public static void before(){
        client = new Client("123325345234134", 2323, "8697623", "151.21.219.26");
        client.setPlatform("");
        client.setOs_name("Windows Vista");
        client.setOs_version("6.3");
        client.setJava_version("");
        client.setTitolari(0);
        client.setDatori(0);
        client.setLavoratori(0);
        client.setIp_local("192.168.32.2");
        client.setHost_name("TWO");
        client.setUser_name("TOMMASO");
        client.setServer_ip("127.0.0.1");
        client.setServer_name("localhost");
        client.setReg_key("");
        client.setAct_key("");
        client.setDb_date("0000-00-00");
        client.setDb_version("00.92");
        client.setInfo_added("2009-08-18 16:37:54");
        client.setInfo_updated("2009-08-18 20:33:47");
    }

    @Test
    public void testToJSon() throws Exception {
        String str = "{\"mac\":\"123325345234134\",\"idapp\":2323,\"version\":\"8697623\",\"platform\":\"\",\"os_name\":\"Windows Vista\",\"os_version\":\"6.3\",\"java_version\":\"\",\"titolari\":0,\"datori\":0,\"lavoratori\":0,\"ip_wan\":\"151.21.219.26\",\"ip_local\":\"192.168.32.2\",\"host_name\":\"TWO\",\"user_name\":\"TOMMASO\",\"server_ip\":\"127.0.0.1\",\"server_name\":\"localhost\",\"reg_key\":\"\",\"act_key\":\"\",\"db_date\":\"0000-00-00\",\"db_version\":\"00.92\",\"info_added\":\"2009-08-18 16:37:54\",\"info_updated\":\"2009-08-18 20:33:47\"}";
        assertEquals(str, JSONPrinter.toJson(client));
    }

}