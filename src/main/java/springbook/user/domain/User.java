package springbook.user.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class User {
    String id;
    String name;
    String password;

    public User(String id, String name, String password) {
        setId(id);
        setName(name);
        setPassword(password);
    }
}
