package SQL_Manager;

import com.mysql.jdbc.ResultSet;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.sql.SQLException;
import java.util.HashMap;

public class Controller {
    private ObservableList<ObservableList> data;
    Database db = new Database();
    HashMap<Integer, String> referenceIndex = new HashMap<>();

    @FXML
    private TableView resultTable;

    @FXML
    private TextArea queryField;

    @FXML
    private TextField searchField;

    @FXML
    private TextField updateQueryBtn;

    @FXML
    protected void updateQuery() throws SQLException {
        db.updateQuery(updateQueryBtn.getText(), referenceIndex.get(resultTable.getSelectionModel().getSelectedIndex()));
        selectTemplate();
        runQuery();
    }
    @FXML
    protected void runQuery() throws SQLException {
        System.out.println("run");

        if(queryField.getText().toLowerCase().contains("insert"))
        {
            try {
                db.insertQuery(queryField.getText());
            }catch(NullPointerException e){
                System.err.println("Set Database");
            }
        }
        else{
        try {
            buildData((ResultSet) db.query(queryField.getText()));
        }catch(NullPointerException e){
            System.err.println("Set Database");
        }}
    }
    @FXML
    protected void runQuery(String q) throws SQLException {
            System.out.println("run");
            q="SELECT * FROM "+db.getSelectedTable()+" WHERE id="+q;
            try {
                buildData((ResultSet) db.query(q));
            }catch(NullPointerException e){
                System.err.println("Set Database");
            }
    }

    @FXML
    protected void selectTemplate() throws SQLException {
        if(db.getSelectedTable()!=null)
        queryField.setText("SELECT * FROM "+db.getSelectedTable()+";");
        else System.err.println("Select Table");

        runQuery();
    }
    @FXML
    protected void insertTemplate(){
        String build = "INSERT INTO "+db.getSelectedTable()+"(";
        for (int i = 1; i < db.getColumnName().length; i++) {
            build+=db.getColumnName()[i];
            if(i<db.getColumnName().length-1)
            {
                build+=", ";
            }
        }
        build+=") VALUES ();";
        if(db.getSelectedTable()!=null)
            queryField.setText(build);
        else System.err.println("Select Table");
    }
    @FXML
    protected void chooseTable() throws SQLException {
        db.getTables();
        db.chooseTable(new CstPopup(db.getTables()).getResponse());
        selectTemplate();
        runQuery();
    }
    @FXML
    protected void chooseDB() throws SQLException {
        db.chooseDB(new CstPopup(db.getSchemas()).getResponse());
    }

    @FXML
    public void searchFieldQuery() throws SQLException {
        runQuery(searchField.getText());

    }

    @FXML
    public void buildData(ResultSet rs){
        data = FXCollections.observableArrayList();
        resultTable.getColumns().clear();
        try{
            int cnt = rs.getMetaData().getColumnCount();
            String[] cn=new String[cnt];
            for(int i=0 ; i<cnt; i++){
                cn[i]=rs.getMetaData().getColumnName(i+1);
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i+1));
                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });
                db.setColumnName(cn);
                resultTable.getColumns().addAll(col);
                System.out.println("Column ["+i+"] ");
            }
            int count = 0;
            while(rs.next()){
                ObservableList<String> row = FXCollections.observableArrayList();
                for(int i=1 ; i<=rs.getMetaData().getColumnCount(); i++){
                    row.add(rs.getString(i));
                    System.out.println(rs.getString(i));
                }
                referenceIndex.put(count++,rs.getString(1));
                //System.out.println("Row [1] added "+row );
                data.add(row);
            }
            System.out.println(referenceIndex.size());
            for (int i = 0; i < referenceIndex.size(); i++) {
                System.out.println(referenceIndex.get(i));
            }
            resultTable.setItems(data);
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Error on Building Data");
        }
    }




}
