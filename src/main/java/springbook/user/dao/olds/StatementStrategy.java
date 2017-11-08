package springbook.user.dao.olds;
import java.sql.*;

public interface StatementStrategy {
    PreparedStatement makePreparedStatement( Connection c ) throws SQLException;
}
