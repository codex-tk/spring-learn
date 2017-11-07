package springbook.user.dao;

import lombok.Data;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import springbook.user.domain.User;

import javax.sql.DataSource;
import java.sql.*;

@Data
public class UserDao {

    //JdbcContext jdbcContext;

    JdbcTemplate jdbcTemplate;

    public void add(User user) throws SQLException {
        jdbcTemplate.update(
                "insert into users( id , name , password ) values( ? , ? , ?)"
                , user.getId() , user.getName(),user.getPassword());
        /*
        jdbcContext.executeStatement((Connection c) -> {
            PreparedStatement ps = c.prepareStatement(
                    "insert into users( id , name , password ) values( ? , ? , ?)");
            ps.setString( 1 , user.getId());
            ps.setString( 2 , user.getName());
            ps.setString( 3 , user.getPassword());
            return ps;
        });*/
        /*
        Connection c  = dataSource.getConnection();

        PreparedStatement pstmt = c.prepareStatement(
                "insert into users( id , name , password ) values( ? , ? , ?)");
        pstmt.setString( 1 , user.getId());
        pstmt.setString( 2 , user.getName());
        pstmt.setString( 3 , user.getPassword());

        pstmt.executeUpdate();

        pstmt.close();
        c.close();*/
    }

    public User get( String id ) throws SQLException {
        return jdbcTemplate.queryForObject("select * from users where id = ?"
                , new Object[] { id }
                , ( rs , num ) -> new User( rs.getString("id")
                        , rs.getString("name")
                        , rs.getString("password"))
        );
        /*
        Connection c  = jdbcContext.getDataSource().getConnection();

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
        */
    }

    public void deleteAll() throws SQLException {
        /*
        Connection c = null;
        PreparedStatement ps = null;

        try {
            c = dataSource.getConnection();
            ps = c.prepareStatement("delete from users");
            ps.executeUpdate();
        } catch ( Exception e ) {
            throw e;
        } finally {
            if ( ps != null ) try { ps.close(); } catch (Exception e){}
            if ( c != null ) try { c.close(); } catch (Exception e){}
        }*/
        jdbcTemplate.update("delete from users");
    }

    public int getCount() throws SQLException {
        return jdbcTemplate.query( c -> c.prepareStatement("select count(*) from users")
                , rs-> {
                    rs.next();
                    return rs.getInt(1);
                });
        /*
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            c = jdbcContext.getDataSource().getConnection();
            ps = c.prepareStatement("select count(*) from users");
            rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        } catch ( Exception e ) {
            throw e;
        } finally {
            if ( rs != null ) try { rs.close(); } catch (Exception e){}
            if ( ps != null ) try { ps.close(); } catch (Exception e){}
            if ( c != null ) try { c.close(); } catch (Exception e){}
        }*/
    }


}
