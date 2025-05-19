package com.college.yi.bookmaneger.service;

import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.college.yi.bookmaneger.entity.UserEntity;
import com.college.yi.bookmaneger.repository.UserMapper;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserMapper userMapper;

    public CustomUserDetailsService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userMapper.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("ユーザーが見つかりません:" + username);
        }

        // 権限をROLE_XXXに形成
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRole());

        // Spring Security の UserDetails 実装
        // org.springframework.security.core.userdetails.User を返却
        return User.builder().username(user.getUsername()).password(user.getPassword()) // すでにハッシュ化済みと仮定
                .authorities(Collections.singleton(authority)).accountExpired(false).accountLocked(false)
                .credentialsExpired(false).disabled(!Boolean.TRUE.equals(user.getEnabled())).build();
    }

}
