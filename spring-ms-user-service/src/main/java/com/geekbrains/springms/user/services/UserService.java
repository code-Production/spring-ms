package com.geekbrains.springms.user.services;

import com.geekbrains.springms.api.UserDto;
import com.geekbrains.springms.api.UserRegisterRequest;
import com.geekbrains.springms.user.entities.Role;
import com.geekbrains.springms.user.entities.User;
import com.geekbrains.springms.user.entities.UserBilling;
import com.geekbrains.springms.user.entities.UserDetails;
import com.geekbrains.springms.user.mappers.UserMapper;
import com.geekbrains.springms.user.repositories.RoleRepository;
import com.geekbrains.springms.user.repositories.UserBillingRepository;
import com.geekbrains.springms.user.repositories.UserDetailsRepository;
import com.geekbrains.springms.user.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private UserRepository userRepository;

    private RoleRepository roleRepository;

    private UserDetailsRepository userDetailsRepository;

    private UserBillingRepository userBillingRepository;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Autowired
    public void setBCryptPasswordEncoder(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Autowired
    public void setUserDetailsRepository(UserDetailsRepository userDetailsRepository) {
        this.userDetailsRepository = userDetailsRepository;
    }

    @Autowired
    public void setUserBillingRepository(UserBillingRepository userBillingRepository) {
        this.userBillingRepository = userBillingRepository;
    }


    @Override
    @Transactional
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Such user was not found"));

        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(r -> new SimpleGrantedAuthority(r.getName()))
                .map(ga -> (GrantedAuthority) (ga))
                .toList();
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }


    @Transactional
    public void registerUser(UserRegisterRequest registerRequest, boolean specialAuthority) throws Exception {

        List<Role> roles;

        if (specialAuthority) {
            if (registerRequest.getRoles() != null && !registerRequest.getRoles().isEmpty()){
                roles = registerRequest.getRoles().stream()
                        .map((s) -> roleRepository.findRoleByName(s)
                                .orElseThrow(() -> new ResponseStatusException(
                                        HttpStatus.BAD_REQUEST,
                                        String.format("No such role can be found as '%s'.", s)
                                ))
                        )
                        .toList();
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No roles are presented in request.");
            }
        } else {
            roles = List.of(roleRepository.findRoleByName("ROLE_USER")
                    .orElseThrow(() ->
                            new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Role 'ROLE_USER' cannot be found.")));
        }

        userRepository.findUserByUsername(registerRequest.getUsername())
                .ifPresent(user -> {
                    throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            String.format("User '%s' is already registered.", registerRequest.getUsername())
                    );
                });

        UserDetails userDetails = new UserDetails();
        User user = new User(
                null,
                registerRequest.getUsername(),
                bCryptPasswordEncoder.encode(registerRequest.getPassword()),
                roles,
                userDetails,
                null
        );
        userDetails.setEmail(registerRequest.getEmail());
        userDetails.setUser(user);
        userRepository.save(user);
//        userDetailsRepository.save(userDetails); //cascade.all
    }


    @Transactional
    public User updateUserInfo(UserDto userDto, boolean specialAuthority) {
        User oldUser = userRepository.findUserByUsername(userDto.getUsername())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("User '%s' cannot be found to update.", userDto.getUsername())
                ));

        User newUser = UserMapper.MAPPER.toEntity(userDto); //get what we can

        newUser.setId(oldUser.getId()); // safe setting
        newUser.setPassword(oldUser.getPassword());
        //admin can set roles to users
        if (specialAuthority) {
            List<Role> roles = userDto.getRoles().stream()
                    .map(s -> roleRepository.findRoleByName(s)
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            String.format("No such role can be found as '%s'.", s)
                    )))
                    .toList();
            newUser.setRoles(roles);
        } else {
            newUser.setRoles(oldUser.getRoles());// mapper roles -> string
        }

        //if they are somehow missed in JSON, to return populated object anyway
        if (newUser.getUserDetails() == null && oldUser.getUserDetails() != null) {
            newUser.setUserDetails(oldUser.getUserDetails());
        }
        if (newUser.getUserBillings() == null && oldUser.getUserBillings() != null) {
            newUser.setUserBillings(oldUser.getUserBillings());
        }

        //restore link to db
        newUser.getUserDetails().setId(oldUser.getUserDetails().getId());
        newUser.getUserDetails().setUser(newUser); //not present in Dto

        if (newUser.getUserBillings() != null && !newUser.getUserBillings().isEmpty()) {
            for (UserBilling newUserBilling : newUser.getUserBillings()) {

                boolean foundById = false;
                boolean idOwner = false;
                boolean foundByCardNumber = false;
                boolean cardNumberOwner = false;

                if (newUserBilling.getId() != null) {
                    Optional<UserBilling> byId = userBillingRepository.findById(newUserBilling.getId());
                    if (byId.isPresent()) {
                        foundById = true;
                        if (byId.get().getUser().getId().equals(newUser.getId())) {
                            idOwner = true;
                        }
                    }
                }

                Optional<UserBilling> byCardNumber = userBillingRepository.findByCardNumber(newUserBilling.getCardNumber());
                if (byCardNumber.isPresent()) {
                    foundByCardNumber = true;
                    if (byCardNumber.get().getUser().getId().equals(newUser.getId())) {
                        cardNumberOwner = true;
                    }
                }

                if (foundByCardNumber && !cardNumberOwner) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot add not unique card number.");
                }
                if (foundByCardNumber && cardNumberOwner) {
                    newUserBilling.setId(byCardNumber.get().getId()); //repair id just in case //safe get()
                }
                if (foundById && !idOwner) {
                    newUserBilling.setId(null);
                }
                if (!foundByCardNumber && !foundById) {
                    newUserBilling.setId(null);
                }

                newUserBilling.setUser(newUser); //not present in Dto
            }
        }
        return userRepository.save(newUser);
    }

    @Transactional
    public User getUserInfoByName(String username) {
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("User '%s' cannot be found.", username)
                ));
    }

    @Transactional
    public void deleteUserBilling(Long id, String username) {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("User '%s' cannot be found for deletion billing.", username)
                ));
        if (user.getUserBillings() != null && !user.getUserBillings().isEmpty()) {
            for (UserBilling userBilling : user.getUserBillings()) {
                if (userBilling.getId().equals(id)) {
                    user.getUserBillings().remove(userBilling);
                    userBillingRepository.deleteById(id);
//                    userRepository.save(user);
                    return;
                }
            }
        }
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, String.format("No such billing with id '%s' can be found for user '%s'", username, id)
        );
    }

    @Transactional
    public void changePassword(String usernameAuthorized,
                               String usernameToModify,
                               String oldPassword,
                               String newPassword,
                               boolean specialAuthority
    )
    {
        User user = userRepository.findUserByUsername(usernameToModify)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        String.format("No such user '%s' was found.", usernameToModify)
                ));
        //admin can give no password to change users passwords but not his own password
        if ((specialAuthority && !usernameAuthorized.equals(usernameToModify)) ||
                        (bCryptPasswordEncoder.matches(oldPassword, user.getPassword()))
        )
        {
            newPassword = bCryptPasswordEncoder.encode(newPassword);
            user.setPassword(newPassword);
//            userRepository.save(user);
            return;
        }
        throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                String.format("Unauthorized try to change a password for user %s", usernameToModify)
        );
    }
}
