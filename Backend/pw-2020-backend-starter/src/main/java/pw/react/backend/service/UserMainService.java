package pw.react.backend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pw.react.backend.dao.CompanyRepository;
import pw.react.backend.dao.UserRepository;
import pw.react.backend.model.Company;
import pw.react.backend.model.User;

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
        Boolean result = userRepository.findAll().toString().contains("user_key=" + token);
        return result;
    }
}
