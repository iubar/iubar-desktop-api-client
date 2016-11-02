package it.iubar.desktop.api.models;

import it.iubar.desktop.api.MasterClientTest;
import it.iubar.desktop.api.services.JSONPrinter;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ClientModelTest {

    public static ClientModel factory() {
    	ClientModel client = new ClientModel("123325345234134", MasterClientTest.ID_APP_PAGHEOPEN);
		client.setVersion("8697623");		
        client.setOs_name("Windows Vista");
        client.setOs_version("6.3");
        client.setJava_version("");
        client.setTitolari(0);
        client.setDatori(0);
        client.setLavoratori(0);
        client.setIp_local("192.168.32.2");
        client.setHost_name("JUNIT");
        client.setUser_name("JUNIT");
        client.setServer_ip("127.0.0.1");
        client.setServer_name("localhost");
        client.setReg_key("");
        client.setAct_key("");
        client.setDb_date("0000-00-00");
        client.setDb_version("00.92"); 
        return client;
    }
 

}
