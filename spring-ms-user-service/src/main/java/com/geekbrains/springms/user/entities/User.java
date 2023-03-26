package com.geekbrains.springms.user.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;


//@Setter
//@Getter
@NoArgsConstructor
//@AllArgsConstructor

@Entity
@Table(schema = "spring_shop", name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Getter
    private Long id;

    @Column(name = "username")
    @Getter
    private String username;

    @Column(name = "password")
    @Getter
    private String password;

    @ManyToMany
    @JoinTable(
            schema = "spring_shop",
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @Getter
    private List<Role> roles;

    @OneToOne(mappedBy = "user", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @Getter
    private UserDetails userDetails;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @Getter
    private List<UserBilling> userBillings;

    //mutable object
    public Builder newBuilder() {
        return this.new Builder();
    }

    public class Builder {
        private Builder() {}

        public Builder setId(Long id) {
            User.this.id = id;
            return this;
        }

        public Builder setUsername(String username) {
            User.this.username = username;
            return this;
        }

        public Builder setPassword(String password) {
            User.this.password = password;
            return this;
        }

        public Builder setRoles(List<Role> roles) {
            User.this.roles = roles;
            return this;
        }

        public Builder setUserDetails(UserDetails userDetails) {
            User.this.userDetails = userDetails;
            return this;
        }

        public Builder setUserBillings(List<UserBilling> userBillings) {
            User.this.userBillings = userBillings;
            return this;
        }

        public User build() {
            return User.this;
        }
    }




}
