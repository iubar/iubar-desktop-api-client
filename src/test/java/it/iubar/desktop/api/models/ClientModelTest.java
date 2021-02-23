package it.iubar.desktop.api.models;

import it.iubar.desktop.api.MasterClientAbstract;

public class ClientModelTest {

	public static final String MAC = "123325345234134";
	public static final String CF = "ABABABABABABABAB"; // deve essere di 11 o 16 caratteri
	public static final String PIVA = "12345678901";
	
    public static ClientModel factory() {
    	ClientModel client = new ClientModel(ClientModelTest.MAC, MasterClientAbstract.ID_APP_PAGHEOPEN);
		client.setVersion("8697623");		
        client.setOs_name("Windows Vista");
        client.setOs_version("6.3");
        client.setJava_version("");
        client.setTitolari(0);
        client.setCf(ClientModelTest.CF);
        client.setDatori(0);
        client.setLavoratori(0);
        client.setCedolini_ultimi_12_mesi(0);
        client.setIp_local("192.168.32.2");
        client.setHost_name("JUNIT");
        client.setUser_name("JUNIT");
        client.setServer_ip("127.0.0.1");
        client.setServer_name("localhost");
        client.setReg_key("");
        client.setAct_key("");
        client.setDb_date("24/05/2018");
        client.setDb_version("00.92"); 
        return client;
    }
 

}
