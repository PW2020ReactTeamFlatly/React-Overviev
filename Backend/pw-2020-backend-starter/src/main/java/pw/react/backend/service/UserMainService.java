package pw.react.backend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pw.react.backend.dao.CompanyRepository;
import pw.react.backend.dao.UserRepository;
import pw.react.backend.model.Company;
import pw.react.backend.model.User;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;

@Service
class UserMainService implements UserService {
    private final Logger logger = LoggerFactory.getLogger(CompanyMainService.class);

    private UserRepository userRepository;

    UserMainService() { /*Needed only for initializing spy in unit tests*/}

    @Autowired
    UserMainService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean containsToken(String token) {
        Collection<User> users = userRepository.findAll();
        for (Iterator<User> iterator = users.iterator(); iterator.hasNext();) {

            if(Objects.equals(token, iterator.next().getUser_key()))
                return true;

        }
        return false;
    }
}
