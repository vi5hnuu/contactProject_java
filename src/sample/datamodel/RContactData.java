package sample.datamodel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.swing.plaf.PanelUI;
import javax.xml.stream.*;
import javax.xml.stream.events.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.PublicKey;
import java.time.LocalDate;
import java.util.Iterator;

public class RContactData {
//    private static final String CONTACTS_FILE = "contacts.xml";
    private static final String CONTACTS_FILE = "D:\\contactProject\\src\\contacts.txt";
    private static final String CONTACT="contact";
    private static final String FIRST_NAME="first_name";
    private static final String LAST_NAME="last_name";
    private static final String PHONE_NUMBER="phone_number";
    private static final String NOTES="notes";

    private ObservableList<RContact> contacts;
    public RContactData(){
        contacts= FXCollections.observableArrayList();
    }

    public ObservableList<RContact> getContacts(){
        return contacts;
    }
    public void addContact(RContact item){
        contacts.add(item);
    }
    public void deleteContact(RContact item){
        contacts.remove(item);
    }

    public void loadContacts(){
        contacts= FXCollections.observableArrayList();
        Path path= Paths.get(CONTACTS_FILE);

        String input;

        try(BufferedReader br= Files.newBufferedReader(path)){
            while ((input=br.readLine())!=null){
                String[] itemPieces=input.split("@.@");
                String firstName=itemPieces[0];
                String lastName=itemPieces[1];
                String phoneNumber=itemPieces[2];
                String notes=itemPieces[3];
                RContact newContact=new RContact(firstName,lastName,phoneNumber,notes);
//                System.out.println(newContact);
                contacts.add(newContact);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public void saveContacts(){
        Path path=Paths.get(CONTACTS_FILE);

        try(BufferedWriter bw=Files.newBufferedWriter(path);){
            Iterator<RContact> iter=contacts.iterator();
            while(iter.hasNext()){
                RContact item=iter.next();
                bw.write(String.format("%s@.@%s@.@%s@.@%s",item.getFirstName(),item.getLastName(),item.getPhoneNumber(),item.getNotes()));
                bw.newLine();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
