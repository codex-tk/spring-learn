package springbook;

import org.junit.Test;

import java.sql.DriverManager;

public class Sqlit3Test {
    @Test
    public void open(){
        try {
            Class.forName("org.sqlite.JDBC");
            DriverManager.getConnection("jdbc:sqlite:./sample.db").createStatement().execute(
                    "create table if not exists users( id TEXT primary key , name TEXT , password TEXT )"
            );
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}