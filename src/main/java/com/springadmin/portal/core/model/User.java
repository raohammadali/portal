package com.springadmin.portal.core.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.springadmin.portal.core.config.Auditable;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "users")
public class User extends Auditable{
    public User(String firstName, String lastName, String phone, String email,
                String password, List<Role> roles) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.isVerified = false;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 666)
    private String firstName;

    @Column(length = 666)
    private String lastName;

    @Column(length = 15)
    private String phone;

    private String twoFactorCode;
    private LocalDateTime twoFactorExpiry;
    private String email;
    private String password;
    private Boolean isVerified;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name= "user_roles", joinColumns = @JoinColumn(name="user_id"), inverseJoinColumns = @JoinColumn(name = "roles_id"))
    private List<Role> roles = new ArrayList<>();
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name= "user_permissions", joinColumns = @JoinColumn(name="user_id"), inverseJoinColumns = @JoinColumn(name = "permissions_id"))
    private List<Permission> permissions=  new ArrayList<>();

}
