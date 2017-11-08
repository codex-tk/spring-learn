package springbook.user.service;

import lombok.Data;
import springbook.user.dao.UserDao;

@Data
public class UserService {
    UserDao userDao;
}
