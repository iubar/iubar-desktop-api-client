package it.iubar.desktop.api;

import org.junit.*;

import java.io.*;
import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MasterClientTest {

    static File falseIni;
    static File trueIni;
    static File editedIni;

    @BeforeClass
    public static void genFiles() throws IOException {

        trueIni = File.createTempFile("trueIni", ".ini");
        BufferedWriter bwTrue = new BufferedWriter(new FileWriter(trueIni));
        bwTrue.write("is_auth = true\n" +
                "host = localhost\n" +
                ";path = /yo/");
        bwTrue.close();

        falseIni = File.createTempFile("falseIni", ".ini");
        BufferedWriter bwfalse = new BufferedWriter(new FileWriter(falseIni));
        bwfalse.write("is_auth = 0");
        bwfalse.close();


        editedIni = File.createTempFile("editedIni", ".ini");
        BufferedWriter bwEdited = new BufferedWriter(new FileWriter(editedIni));
        bwEdited.write(";is = true");
        bwEdited.close();
    }

    @Test
    public void masterClientTrue() throws IOException, NoSuchFieldException, IllegalAccessException {
        MasterClient masterClient = new MasterClient(trueIni.getAbsolutePath());
        Field fieldAuth = MasterClient.class.getDeclaredField("isAuth");
        fieldAuth.setAccessible(true);
        Field fieldUrl = MasterClient.class.getDeclaredField("url");
        fieldUrl.setAccessible(true);
        assertTrue((boolean) fieldAuth.get(masterClient));
        assertEquals("http://localhost:80/", fieldUrl.get(masterClient));
    }

    @Test
    public void masterClientFalse() throws IOException, NoSuchFieldException, IllegalAccessException {
        MasterClient masterClient = new MasterClient(falseIni.getAbsolutePath());
        Field field = MasterClient.class.getDeclaredField("isAuth");
        field.setAccessible(true);
        assertFalse((boolean) field.get(masterClient));
    }

    @Test
    public void masterClientEdit() throws IOException, NoSuchFieldException, IllegalAccessException {
        MasterClient masterClient = new MasterClient(editedIni.getAbsolutePath(), "is");
        Field field = MasterClient.class.getDeclaredField("isAuth");
        field.setAccessible(true);
        assertFalse((boolean) field.get(masterClient));
    }

    @AfterClass
    public static void delFiles(){
        falseIni.delete();
        trueIni.delete();
        editedIni.delete();
    }
}