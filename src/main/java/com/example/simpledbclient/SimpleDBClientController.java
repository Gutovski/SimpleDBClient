package com.example.simpledbclient;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import java.sql.*;

public class SimpleDBClientController {
    // Connection variables => Adjust to your database environment
    String url = "jdbc:mysql://localhost:3306/java_ii";
    String username = "root";
    String password = "";

    // The sql script for loading the sample database java_ii into your mysql server
    // is in the directory 'database' and is called 'mysqlsampledatabase_java_ii.sql'

    @FXML   private TextField txtQuery;
    @FXML   private Button btnGo;
    @FXML   private TableView tbvResult;

    @FXML
    protected void on_btnGoClick(MouseEvent event) {
        // Open connection, send the query to the database, and present the results in the tableview
        Connection conn = null;
        try {
            // Open connection
            conn = DriverManager.getConnection(url, username, password);

            // Send the query to the database and store the results in a ResultSet
            ResultSet rs = null;
            Statement stmt = conn.createStatement();
            ObservableList<Object> data = FXCollections.observableArrayList();
            rs = stmt.executeQuery(txtQuery.getText());

            // Clean the table view before inserting the new ResultSet data
            tbvResult.getItems().clear();
            tbvResult.getColumns().clear();

            // Insert columns in the tableview
            for(int i=0 ; i<rs.getMetaData().getColumnCount(); i++){
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i+1));
                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });

                tbvResult.getColumns().add(col);
            }

            // Get the data from the ResultSet...
            while (rs != null && rs.next()) {
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for(int i=1 ; i<=rs.getMetaData().getColumnCount(); i++){
                    //Iterate Column
                    row.add(rs.getString(i)==null?"":rs.getString(i));
                }
                data.add(row);
            }

            // ...and now put the data in the tableview
            tbvResult.setItems(data);
        }
        catch (SQLException e) {
            e.printStackTrace();

            // Let the user know the error that happened
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("An error occurred!");
            alert.setContentText(e.getLocalizedMessage());
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                }
            });
        }
        finally {
            if (conn != null) {
                // Close connection
                try {
                    conn.close();
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}