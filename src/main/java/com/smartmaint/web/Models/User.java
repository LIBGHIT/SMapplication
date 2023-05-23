package com.smartmaint.web.Models;

import com.mongodb.client.gridfs.model.GridFSFile;
import com.smartmaint.web.utilityClasses.Skills;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@Builder
@NoArgsConstructor(force = true)
@Data
@Document(collection = "Users")
public class User implements UserDetails {
    @Id
    private String ID_User;
    @NonNull
    @NotBlank(message = "First name must not be empty")
    @Size(min = 3, message = "First name must be at least 3 characters long")
    private String firstName;
    @NonNull
    @NotBlank(message = "Last name must not be empty")
    @Size(min = 3, message = "Last name must be at least 3 characters long")
    private String lastName;
    @NonNull
    @Indexed(unique = true)
    @NotBlank(message = "Email must not be empty")
    @Email(message = "Please provide a valid email")
    private String email;
    @NonNull
    @NotBlank(message = "Password must not be empty")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;
    private String sector;
    private Skills skills;
    private String Id_CV;
    private Role role;
    private Boolean locked = false;
    private Boolean enabled = false;



    transient private String roleStr;
    transient private String checkPass;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
