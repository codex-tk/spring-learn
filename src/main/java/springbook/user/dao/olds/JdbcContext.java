package springbook.user.dao.olds;


import lombok.Data;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Data
public class JdbcContext {
    private DataSource dataSource;

    public void executSql( String sql ) throws SQLException {
        executeStatement( c -> c.prepareStatement(sql));
    }

    public void executeStatement( StatementStrategy strategy ) throws SQLException {
        Connection c = null;
        PreparedStatement ps = null;

        try {
            c = dataSource.getConnection();
            ps = strategy.makePreparedStatement(c);
            ps.executeUpdate();
        } catch ( Exception e ) {
            throw e;
        } finally {
            if ( ps != null ) try { ps.close(); } catch (Exception e){}
            if ( c != null ) try { c.close(); } catch (Exception e){}
        }
    }
}
