package springbook.user.dao;

import lombok.Data;

import java.sql.Connection;
import java.sql.SQLException;

@Data
public class CountingConnectionMaker implements ConnectionMaker {
    int counter = 0;
    ConnectionMaker realConnectionMaker;
    @Override
    public Connection makeNewConnection() throws ClassNotFoundException, SQLException {
        this.counter++;
        return realConnectionMaker.makeNewConnection();
    }
}
