package com.smartmaint.web.Services;

import com.smartmaint.web.Models.Blogs;
import com.smartmaint.web.Models.User;
import com.smartmaint.web.Repositorises.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserRepo userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("username not found"));
    }

    public User findById(String id){
        return userRepo.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("username not found"));
    }


    public boolean emailExists(String email){
        return userRepo.findByEmail(email).isPresent();
    }

    public String getFirstNameByEmail(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return user.getFirstName();
    }

    public String getLastNameByEmail(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return user.getLastName();
    }

    public User registerUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User user_ = userRepo.save(user);
        return user_;
    }


    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public long countUsers() {
        return userRepo.count();
    }

    public void deleteUser(String id) {
        userRepo.deleteById(id);
    }

    public void enableUser(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        user.setEnabled(true);
        userRepo.save(user);
    }

    public Page<User> getAllUsersPageable(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        return userRepo.findAll(pageable);
    }


//    here we can add the CRUD functions

    public Page<User> getAdminUsersPageable(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        return userRepo.findByRole("ROLE_ADMIN", pageable);
    }

    public Page<User> getSmUsersPageable(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        return userRepo.findByRole("ROLE_PERSONEL", pageable);
    }
    public Page<User> getUsersPageable(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        return userRepo.findByRole("ROLE_USER", pageable);
    }

    public void changePassword(String email, String newPassword) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found change pass"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(user);
    }

}

