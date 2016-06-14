package it.iubar.desktopApi.DBClasses;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ClientTest {

    Client client;

    @Before
    public void before(){
        client = new Client(1, "00-1C-F0-99-84-3E", 13, "00.92.01", "", "Windows Vista", "6.0", "", 0, 0, 0, "151.21.219.26", "192.168.32.2", "ONE", "FABIO", "127.0.0.1", "localhost", "", "", "0000-00-00", "00.92", "2009-08-18 16:37:54", "2009-08-18 20:33:47");
    }

    @Test
    public void testToJSon() throws Exception {
        String str = "{\"idClient\":1,\"mac\":\"00-1C-F0-99-84-3E\",\"idApp\":13,\"version\":\"00.92.01\",\"platform\":\"\",\"osName\":\"Windows Vista\",\"osVersion\":\"6.0\",\"javaVersion\":\"\",\"titolari\":0,\"datori\":0,\"lavoratori\":0,\"ipWan\":\"151.21.219.26\",\"ipLocal\":\"192.168.32.2\",\"hostName\":\"ONE\",\"userName\":\"FABIO\",\"serverIp\":\"127.0.0.1\",\"serverName\":\"localhost\",\"regKey\":\"\",\"actKey\":\"\",\"dbDate\":\"0000-00-00\",\"dbVersion\":\"00.92\",\"infoAdded\":\"2009-08-18 16:37:54\",\"infoUploaded\":\"2009-08-18 20:33:47\"}";
        assertEquals(str, client.toJson());
    }

}