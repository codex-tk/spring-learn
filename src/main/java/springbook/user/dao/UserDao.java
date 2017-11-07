package springbook.user.dao;

import lombok.Data;
import org.springframework.dao.EmptyResultDataAccessException;
import springbook.user.domain.User;

import javax.sql.DataSource;
import java.sql.*;

@Data
public class UserDao {

    DataSource dataSource;

    public void add(User user) throws SQLException {
        Connection c  = dataSource.getConnection();

        PreparedStatement pstmt = c.prepareStatement(
                "insert into users( id , name , password ) values( ? , ? , ?)");
        pstmt.setString( 1 , user.getId());
        pstmt.setString( 2 , user.getName());
        pstmt.setString( 3 , user.getPassword());

        pstmt.executeUpdate();

        pstmt.close();
        c.close();
    }

    public User get( String id ) throws SQLException {
        Connection c  = dataSource.getConnection();

        PreparedStatement pstmt = c.prepareStatement("select * from users where id = ?");
        pstmt.setString(1,id );
        ResultSet rs = pstmt.executeQuery();
        User user = null;
        if ( rs.next() ){
            user = new User();
            user.setId( rs.getString("id"));
            user.setName( rs.getString("name"));
            user.setPassword(rs.getString("password"));
        }
        rs.close();
        pstmt.close();
        c.close();
        if (user == null) throw new EmptyResultDataAccessException(1);
        return user;
    }

    public void deleteAll() throws SQLException {
        Connection c  = dataSource.getConnection();
        PreparedStatement pstmt = c.prepareStatement("delete from users");
        pstmt.executeUpdate();
        pstmt.close();
        c.close();
    }

    public int getCount() throws SQLException {
        Connection c = dataSource.getConnection();
        PreparedStatement ps = c.prepareStatement("select count(*) from users");
        ResultSet rs = ps.executeQuery();
        rs.next();
        int ans = rs.getInt(1);
        rs.close();
        ps.close();
        c.close();
        return ans;
    }
}
