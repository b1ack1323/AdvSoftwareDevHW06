package SQL_Manager;

import com.mysql.jdbc.ResultSet;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * Created by tanner on 10/23/16.
 */
public class TableViewDataset {
    String columnName[];


    public TableViewDataset(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        int count = metaData.getColumnCount(); //number of column
        String columnName[] = new String[count];

        for (int i = 1; i <= count; i++)
        {
            columnName[i-1] = metaData.getColumnLabel(i);
        }
    }

}
