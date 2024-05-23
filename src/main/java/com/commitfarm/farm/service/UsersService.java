package com.commitfarm.farm.service;

import com.commitfarm.farm.domain.Users;
import com.commitfarm.farm.dto.user.CreateUserReq;
import com.commitfarm.farm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsersService {

    @Autowired
    private UserRepository userRepository;

    public void login(String email, String password) throws Exception {
        Users user = userRepository.findByEmail(email);
        if (user == null || !user.getPassword().equals(password)) {
            throw new Exception("아이디 또는 비밀번호를 잘못 입력했습니다");
        }
    }

    public void createUser(CreateUserReq createUserReq) {
        Users user = new Users();
        user.setUsername(createUserReq.getUsername());
        user.setPassword(createUserReq.getPassword());
        user.setEmail(createUserReq.getEmail());
        user.setUserType(createUserReq.getUserType());
        user.setAdmin(false);

        userRepository.save(user);
    }

    public Users findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }



}
