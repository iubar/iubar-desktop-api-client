package it.iubar.desktop.api.models;

import it.iubar.desktop.api.MasterClientAbstract;

public class ClientModelTest {

	public static final String MAC = "123325345234134";
	public static final String CF = "ABABABABABABABAB"; // deve essere di 11 o 16 caratteri
	public static final String PIVA = "12345678901";
	
    public static ClientModel factory() {
    	ClientModel client = new ClientModel(ClientModelTest.MAC, MasterClientAbstract.ID_APP_PAGHEOPEN);
        client.setTitolari(0);
        client.setCf(ClientModelTest.CF);
        client.setDatori(0);
        client.setLavoratori(0);
        client.setCedolini_ultimi_12_mesi(0);
        return client;
    }
 

}
