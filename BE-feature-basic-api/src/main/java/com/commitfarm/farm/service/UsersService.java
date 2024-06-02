package com.commitfarm.farm.service;

import com.commitfarm.farm.domain.Users;
import com.commitfarm.farm.dto.user.CreateUserReq;
import com.commitfarm.farm.dto.user.LoginRes;
import com.commitfarm.farm.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsersService {

    @Autowired
    private UsersRepository usersRepository;

    public LoginRes login(String email, String password) {
        Users user = usersRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호를 잘못 입력했습니다"));

        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("비밀번호를 잘못 입력했습니다");
        }

        return new LoginRes(user.getUsername(), user.isAdmin(), user.getEmail());
    }

    public void createUser(CreateUserReq createUserReq) {
        if (usersRepository.findByEmail(createUserReq.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다: " + createUserReq.getEmail());
        }

        Users user = new Users();
        user.setUsername(createUserReq.getUsername());
        user.setPassword(createUserReq.getPassword());
        user.setEmail(createUserReq.getEmail());
        user.setAdmin(createUserReq.isAdmin());

        usersRepository.save(user);
    }

}
