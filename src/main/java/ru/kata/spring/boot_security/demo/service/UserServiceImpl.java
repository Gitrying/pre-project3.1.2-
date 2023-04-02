package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements  UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public User userById(long id){
        return userRepository.findById(id).orElse(null);
    }

    public List<User> usersAll(){
        return userRepository.findAll();
    }

    @Transactional
    public User userAdd(User user){
        return userRepository.save(user);
    }

    @Transactional
    public void userDelete(long id){userRepository.deleteById(id);}

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = findByUserEmail(email);
        if(user == null){
            throw new UsernameNotFoundException (String.format("User not found ", email));
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

    @Override
    public Set<Role> getRoles(String[] roles){
        Set<Role> roleSet = new HashSet<>();
        for (String role : roles) {
            roleSet.add(roleRepository.findRoleModelByName(role));
        }
        return roleSet;
    }

    public User findByUserEmail(String email) {
        return userRepository.findUserByEmail(email);
    }
}