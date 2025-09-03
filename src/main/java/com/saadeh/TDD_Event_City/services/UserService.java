package com.saadeh.TDD_Event_City.services;

import com.saadeh.TDD_Event_City.entities.Role;
import com.saadeh.TDD_Event_City.entities.User;
import com.saadeh.TDD_Event_City.projections.UserDetailsProjection;
import com.saadeh.TDD_Event_City.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<UserDetailsProjection> listUserDetails = repository.searchUserAndRoleByEmail(username);
        if (listUserDetails.size()==0){
            throw new UsernameNotFoundException("User not found.");
        }

        User user = new User();
        user.setEmail(username);
        user.setPassword(listUserDetails.get(0).getPassword());
        for (UserDetailsProjection u: listUserDetails){
            user.addRole(new Role(u.getRoleId(),u.getAuthority()));
        }
        return user;
    }
}
