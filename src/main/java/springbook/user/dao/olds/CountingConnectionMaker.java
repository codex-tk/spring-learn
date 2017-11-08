package springbook.user.dao.olds;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;

@Data
@Component
public class CountingConnectionMaker implements ConnectionMaker {
    int counter = 0;
    ConnectionMaker realConnectionMaker;

    @Override
    public Connection makeNewConnection() throws ClassNotFoundException, SQLException {
        this.counter++;
        return realConnectionMaker.makeNewConnection();
    }
}
