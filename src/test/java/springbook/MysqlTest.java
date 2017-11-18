package springbook;

import org.junit.Test;

import java.sql.DriverManager;

public class MysqlTest {
    @Test
    public void open(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            DriverManager.getConnection("jdbc:mysql://192.168.1.10:3306/spring?user=spring&password=spring" )
                    .createStatement()
                    .execute(
                    "CREATE TABLE if not exists users( " +
                            "id varchar(256) PRIMARY KEY," +
                            "name text," +
                            "password text," +
                            "level int(11) DEFAULT NULL," +
                            "login int(11) DEFAULT NULL," +
                            "recommend int(11) DEFAULT NULL )"
            );
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
