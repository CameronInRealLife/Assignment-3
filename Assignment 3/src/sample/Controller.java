package sample;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private TextField itemTitleTF;
    @FXML
    private TextField isbnTF;
    @FXML
    private TextField authorTF;
    @FXML
    private TextField callNoTF;
    @FXML
    private TextField quantityTF;
    @FXML
    private Button createItemTableBTN;
    @FXML
    private Button insertItemBTN;
    @FXML
    private ComboBox itemTypeCB;
    @FXML
    private Button itemsViewBTN;
    @FXML
    private ListView itemsListView;

    //Users
    @FXML
    private ComboBox userTypeCB;
    @FXML
    private TextField userNameTF;
    @FXML
    private ListView usersListView;
    @FXML
    private ComboBox itemSelectionCB;

    private ObservableList itemsList;
    @FXML
    private Button itemCheckoutBTN;
    @FXML
    private Button userViewBTN;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        final String DB_URL = "jdbc:derby://localhost:1527/LibraryDB;";
        itemTypeCB.getItems().addAll("Book","Media");
        itemsList = FXCollections.observableArrayList();
        itemsListView.setItems(itemsList);

        createItemTableBTN.setOnAction(ActionEvent->{
            try{
                Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement();
                stmt.execute("CREATE TABLE items (" +
                        "ISBN INT NOT NULL PRIMARY KEY, " +
                        "Item_Type VARCHAR(50), " +
                        "Item_Title VARCHAR(50), " +
                        "Author VARCHAR(50), " +
                        "Call_No VARCHAR(20), " +
                        "Quantity INT )");
                stmt.close();
                System.out.println("Success!..Table created");
                conn.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        insertItemBTN.setOnAction(ActionEvent->{
            try {
                Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement stmt = null;
                String itemTitle = itemTitleTF.getText();
                String itemType = (String) itemTypeCB.getValue();
                int isbn = Integer.parseInt(isbnTF.getText());
                String author = authorTF.getText();
                String callNo = callNoTF.getText();
                int quantity = Integer.parseInt(quantityTF.getText());
                stmt = conn.prepareStatement("INSERT INTO items ( ISBN, Item_Type, Item_Title, Author, Call_No, Quantity ) VALUES (?,?,?,?,?,?)");
                stmt.setInt(1, isbn);
                stmt.setString(2, itemType);
                stmt.setString(3,itemTitle);
                stmt.setString(4, author);
                stmt.setString(5, callNo);
                stmt.setInt(6, quantity);
                stmt.executeUpdate();
                System.out.println("record inserted into table");
            }catch (SQLException e) {
                e.printStackTrace();
            }
            itemTitleTF.clear();
            itemTypeCB.valueProperty().setValue(null);
            isbnTF.clear();
            authorTF.clear();
            callNoTF.clear();
            quantityTF.clear();
        });

        itemsViewBTN.setOnAction(ActionEvent->{
            ResultSet resultSet = null;
            try {
                Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement stmt = conn.prepareStatement("SELECT * from items");
                resultSet = stmt.executeQuery();
                itemsListView.getItems().clear();
                while (resultSet.next()){
                    int isbn = Integer.parseInt(resultSet.getString("ISBN"));
                    String itemType = resultSet.getString("Item_Type");
                    String itemTitle = resultSet.getString("Item_Title");
                    String author = resultSet.getString("Author");
                    String callNo = resultSet.getString("Call_No");
                    int quantity = Integer.parseInt(resultSet.getString("Quantity"));
                    itemsList.addAll(isbn + "\t" + itemType + "\t" + itemTitle + "\t" + author + "\t" + callNo + "\t" + quantity);
                }
                resultSet.close();
                conn.close();
            }catch (Exception ex){
                var msg = ex.getMessage();
                System.out.println(msg);
            }
        });
    }

}
