package it.iubar.desktop.api;

import it.iubar.desktop.api.exceptions.ClientException;
import it.iubar.desktop.api.models.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.Assert.*;

public class MasterClientTest {

    private static File falseIni;
    private static File trueIni;
    private static File testIni;
    private static File brokenIni;

    private static Client client;
    private static Datore datore;
    private static Titolare titolare;
    private static Ccnl ccnl;
    private static Doc doc;

    @BeforeClass
    public static void genFiles() throws IOException {

        trueIni = File.createTempFile("trueIni", ".ini");
        BufferedWriter bwTrue = new BufferedWriter(new FileWriter(trueIni));
        bwTrue.write("is_auth = true\n" +
                "host = localhost\n" +
                "path = /yo");
        bwTrue.close();

        falseIni = File.createTempFile("falseIni", ".ini");
        BufferedWriter bwFalse = new BufferedWriter(new FileWriter(falseIni));
        bwFalse.write("host = localhost\n" +
                "is_auth = 0");
        bwFalse.close();

        testIni = File.createTempFile("testIni", ".ini");
        BufferedWriter bwTest = new BufferedWriter(new FileWriter(testIni));
        bwTest.write("host = httpbin.org\n" +
                "path = /post\n" +
                "is_unique = true");
        bwTest.close();

        brokenIni = File.createTempFile("brokenIni", ".ini");
        BufferedWriter bwBroken = new BufferedWriter(new FileWriter(brokenIni));
        bwBroken.write("path = /post");
        bwBroken.close();

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

        datore = new Datore(2, 4);
        datore.setCf("AIHDUAWHDOU");
        datore.setPiva("obaojwdboawdb");
        datore.setDenom("AWDBAWJDBJLA");
        datore.setSub("ADWADAWD");
        datore.setPara("EBOLA");
        datore.setEmail("awdnaowdj@gmail.com");
        datore.setTel("0129300823");

        titolare = new Titolare(123, 0);
        titolare.setIdtipo(0);
        titolare.setCf("123123123");
        titolare.setPiva("qweasdzxc");
        titolare.setDenom("Studio Eccol");
        titolare.setCognome("Tommaso");
        titolare.setIndirizzo("Via Sant'Antonio");
        titolare.setEmail("");
        titolare.setTel("4674567567");
        titolare.setDatori(123);
        titolare.setLavoratori(123);
        titolare.setInfo_added("12307799987934");
        titolare.setInfo_updated("1273559797681729539");
        titolare.setChiave_pubblica("");

        ccnl = new Ccnl(123);

        doc = new Doc(1, 12055, 2 , 2020);
    }

    @Test
    public void masterClientTrue() throws Exception {
        MasterClient masterClient = new MasterClient(trueIni.getAbsolutePath());
        Field fieldAuth = MasterClient.class.getDeclaredField("isAuth");
        fieldAuth.setAccessible(true);
        Field fieldUrl = MasterClient.class.getDeclaredField("url");
        fieldUrl.setAccessible(true);
        assertTrue((boolean) fieldAuth.get(masterClient));
        assertEquals("http://localhost:80/yo", fieldUrl.get(masterClient));
    }

    @Test
    public void masterClientFalse() throws Exception {
        MasterClient masterClient = new MasterClient(falseIni.getAbsolutePath());
        Field field = MasterClient.class.getDeclaredField("isAuth");
        field.setAccessible(true);
        assertFalse((boolean) field.get(masterClient));
    }

    @Test (expected = RuntimeException.class)
    public void masterClientFail() throws Exception {
        new MasterClient(brokenIni.getAbsolutePath());
    }

    @Test
    public void testNormalizeHost() throws Exception{
        MasterClient masterClient = new MasterClient(testIni.getAbsolutePath());
        Method method = MasterClient.class.getDeclaredMethod("normalizeHost", String.class);
        method.setAccessible(true);
        assertEquals("localhost", method.invoke(masterClient, "http://localhost:80"));
        assertEquals("localhost", method.invoke(masterClient, "https://localhost"));
    }

    @Test
    public void testNormalizePort() throws Exception {
        MasterClient masterClient = new MasterClient(testIni.getAbsolutePath());
        Method method = MasterClient.class.getDeclaredMethod("normalizePort", String.class);
        method.setAccessible(true);
        assertEquals("8080", method.invoke(masterClient, ":8080"));
    }

    @Test
    public void testNormalizePath() throws Exception {
        MasterClient masterClient = new MasterClient(testIni.getAbsolutePath());
        Method method = MasterClient.class.getDeclaredMethod("normalizePath", String.class);
        method.setAccessible(true);
        assertEquals("/yo", method.invoke(masterClient, "yo"));
    }

    @Ignore("Ignoring test until the service is online")
    @Test
    public void sendTestLocal() throws Exception {
        MasterClient masterClient = new MasterClient("src/main/resources/config.ini");
        try {
            masterClient.send(client);
        } catch (ClientException e) {
            fail();
        }
    }

    @Test
    public void sendTest() throws Exception {
        MasterClient masterClient = new MasterClient(testIni.getAbsolutePath());
        try {
            masterClient.send(client);
            masterClient.send(titolare);
            masterClient.send(datore);
            masterClient.send(ccnl);
            masterClient.send(doc);
        } catch (ClientException e) {
            fail();
        }
    }

    @Ignore("Ignoring test until the service is online")
    @Test
    public void checkMacTest() throws Exception {
        MasterClient masterClient = new MasterClient("src/main/resources/config.ini");
        ListMac listMac = masterClient.checkMac("1C-4B-D6-C8-8B-31");
        assertEquals(true,listMac.isGreyList());
        assertEquals(2,listMac.getCodeGreyList());
        assertEquals("Non ha pagato", listMac.getDescGreyList());
        assertEquals(false, listMac.isBlackList());
        listMac = masterClient.checkMac("84-2B-2B-91-78-96");
        assertEquals(false, listMac.isGreyList());
        assertEquals(0, listMac.getCodeGreyList());
        assertEquals("", listMac.getDescGreyList());
        assertEquals(true, listMac.isBlackList());
    }

    @Ignore("Ignoring test until the service is online")
    @Test (expected = RuntimeException.class)
    public void checkWrongMac() throws Exception {
        MasterClient masterClient = new MasterClient("src/main/resources/config.ini");
        ListMac listMac = masterClient.checkMac("123456789");
    }

    @Test (expected = RuntimeException.class)
    public void sendTestNull() throws Exception{
        MasterClient masterClient = new MasterClient(testIni.getAbsolutePath());
        masterClient.send(null);
    }

    @AfterClass
    public static void delFiles(){
        falseIni.delete();
        trueIni.delete();
        testIni.delete();
        brokenIni.delete();
    }
}