package sample;
//--module-path "C:\Users\Vishnu\Downloads\openjfx-18_windows-x64_bin-sdk\javafx-sdk-18\lib" --add-modules javafx.controls,javafx.fxml
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import sample.datamodel.RContact;
import sample.datamodel.RContactData;

import java.util.Optional;


public class RMain extends Application{
    private BorderPane borderPane;
    private RContactData rContactData;
    private TableView<RContact> contactsTable;

    public void initialize() {
        rContactData = new RContactData();
        rContactData.loadContacts();

    }

    @Override
    public void init() throws Exception {
        super.init();
        initialize();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        rContactData.saveContacts();
    }

    @Override
    public void start(Stage primarystage){
        borderPane=new BorderPane();
        MenuBar menuBar=new MenuBar();
        Menu menu=new Menu("CONTACTS");
        MenuItem menuAdd=new MenuItem("ADD");
        menuAdd.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                showAddContactDialog();
            }
        });
        MenuItem menuEdit=new MenuItem("EDIT");
        menuEdit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                showEditContactDialog();
            }
        });
        MenuItem menuDelete=new MenuItem("DELETE");
        menuDelete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                deleteContact();
            }
        });
        menuBar.getMenus().addAll(menu);
        menu.getItems().addAll(menuAdd,menuEdit,menuDelete);

        /////////////////////////////////////////////////////
        contactsTable =new TableView<RContact>();
        contactsTable.setItems(rContactData.getContacts());//putting the line in initilize method[called in init] is giving error
        //maybe because init is called before start is called as tableView is initilized in start init gives error..

        TableColumn<RContact,String> colFirstName=new TableColumn<>("First Name");
        colFirstName.setCellValueFactory(new PropertyValueFactory<RContact,String>("firstName"));//we use SingleStringProperty
        TableColumn<RContact,String> colLastName=new TableColumn<>("Last Name");
        colLastName.setCellValueFactory(new PropertyValueFactory<RContact,String>("lastName"));
        TableColumn<RContact,String> colPhoneNumber=new TableColumn<>("Phone Number");
        colPhoneNumber.setCellValueFactory(new PropertyValueFactory<RContact,String>("phoneNumber"));
        TableColumn<RContact,String> colNotes=new TableColumn<>("Notes");
        colNotes.setCellValueFactory(new PropertyValueFactory<RContact,String>("notes"));
        colNotes.setReorderable(false);
        contactsTable.setEditable(false);
        contactsTable.getColumns().add(colFirstName);
        contactsTable.getColumns().add(colLastName);
        contactsTable.getColumns().add(colPhoneNumber);
        contactsTable.getColumns().add(colNotes);
//        contactsTable.getColumns().addAll(colFirstName,colLastName,colPhoneNumber,colNotes);
//        contactsTable.getItems().add(new RContact("Vishu","kumar","9785855892","LPU"));

        borderPane.setTop(menuBar);
        borderPane.setCenter(contactsTable);

        Scene scene=new Scene(borderPane);
        primarystage.setScene(scene);
        primarystage.show();
    }
    //////////////functions////////////////////////////
    public void showAddContactDialog(){
        Dialog<ButtonType> dialog=new Dialog<>();
        dialog.initOwner(borderPane.getScene().getWindow());
        dialog.setTitle("Add New Contact");

        DialogPane dialogPane=new DialogPane();
        dialogPane.setHeaderText("Fill in the information for the new Contact");
        GridPane gridPane=new GridPane();
        gridPane.setHgap(5);
        gridPane.setVgap(5);
        Label labelFirstName=new Label("First Name");
        TextField textFieldFirstName=new TextField();
        Label labelLastName=new Label("Last Name");
        TextField textFieldLastName=new TextField();
        Label labelPhoneNumber=new Label("Phone Number");
        TextField textFieldPhoneNumber=new TextField();
        Label labelNotes=new Label("Notes");
        TextField textFieldNotes=new TextField();
        gridPane.add(labelFirstName,0,0);
        gridPane.add(textFieldFirstName,1,0);
        gridPane.add(labelLastName,0,1);
        gridPane.add(textFieldLastName,1,1);
        gridPane.add(labelPhoneNumber,0,2);
        gridPane.add(textFieldPhoneNumber,1,2);
        gridPane.add(labelNotes,0,3);
        gridPane.add(textFieldNotes,1,3);
        dialogPane.setContent(gridPane);
        dialog.getDialogPane().setContent(dialogPane);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result=dialog.showAndWait();
        if(result.isPresent() && result.get()==ButtonType.OK){
            String firstName = textFieldFirstName.getText();
            String lastName = textFieldLastName.getText();
            String phoneNumber = textFieldPhoneNumber.getText();
            String notes = textFieldNotes.getText();
            RContact newContact = new RContact(firstName, lastName, phoneNumber, notes);
            rContactData.addContact(newContact);
        }
    }


    public void showEditContactDialog(){
        RContact selectedContact=contactsTable.getSelectionModel().getSelectedItem();
        if(selectedContact==null){
            Alert alert=new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Contact Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select the contact you want to edit");
            alert.showAndWait();
            return;
        }
        Dialog<ButtonType> dialog=new Dialog<>();
        dialog.initOwner(borderPane.getScene().getWindow());
        dialog.setTitle("Edit Contact");

        DialogPane dialogPane=new DialogPane();

        GridPane pane=new GridPane();
        Label labelFirstName=new Label("First Name");
        TextField textFieldFirstname=new TextField();
        Label labelLastName=new Label("Last Name");
        TextField textFieldLastName=new TextField();
        Label labelPhoneNumber=new Label("Phone Number");
        TextField textFieldPhoneNumber=new TextField();
        Label labelNotes=new Label("Note");
        TextField textFieldNotes=new TextField();

        pane.setVgap(5);
        pane.setHgap(5);
        pane.add(labelFirstName,0,0);
        pane.add(textFieldFirstname,1,0);
        pane.add(labelLastName,0,1);
        pane.add(textFieldLastName,1,1);
        pane.add(labelPhoneNumber,0,2);
        pane.add(textFieldPhoneNumber,1,2);
        pane.add(labelNotes,0,3);
        pane.add(textFieldNotes,1,3);
        dialogPane.setContent(pane);
        dialog.getDialogPane().setContent(dialogPane);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        //////////show contact(selected)
        textFieldFirstname.setText(selectedContact.getFirstName());
        textFieldLastName.setText(selectedContact.getLastName());
        textFieldPhoneNumber.setText(selectedContact.getPhoneNumber());
        textFieldNotes.setText(selectedContact.getNotes());

        //////////////////////////////////////////////
        Optional<ButtonType> result=dialog.showAndWait();
        if(result.isPresent() && result.get()==ButtonType.OK){
            if(textFieldFirstname.getText().compareTo(selectedContact.getFirstName())!=0)
            selectedContact.setFirstName(textFieldFirstname.getText());
            if(textFieldLastName.getText().compareTo(selectedContact.getLastName())!=0)
            selectedContact.setLastName(textFieldLastName.getText());
            if(textFieldPhoneNumber.getText().compareTo(selectedContact.getPhoneNumber())!=0)
            selectedContact.setPhoneNumber(textFieldPhoneNumber.getText());
            if(textFieldNotes.getText().compareTo(selectedContact.getNotes())!=0)
            selectedContact.setNotes(textFieldNotes.getText());
        }



    }

    public void deleteContact(){
        RContact selectedContact=contactsTable.getSelectionModel().getSelectedItem();
        if(selectedContact == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Contact Selected");
            alert.setHeaderText(null);
            alert.setContentText("PLease select the contact you want to delete.");
            alert.showAndWait();
            return;
        }
        Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Contact");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete the selected contact :\n"+selectedContact.getFirstName()+" "+selectedContact.getLastName());
        Optional<ButtonType> result=alert.showAndWait();
        if(result.isPresent() && result.get()==ButtonType.OK){
            rContactData.deleteContact(selectedContact);
        }
    }
}
