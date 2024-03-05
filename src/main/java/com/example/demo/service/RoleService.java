//package com.example.demo.service;
//
//import com.example.demo.entity.User;
//import com.example.demo.repository.RoleRepository;
//import com.example.demo.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.Optional;
//
//@Service
//@RequiredArgsConstructor
//public class RoleService implements UserDetailsService {
//    private final UserRepository userRepository;
//    private final RoleRepository roleRepository;
//    private final PasswordEncoder passwordEncoder;
//
////    public Optional<User> findByUsername(String name) {
////        userRepository.findByUsername(name);
////    }
//
////    @Override
////    @Transactional
////    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
////        User user = findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(
////                String.format("User '%s' not found", username)
////        ));
////        return new org.springframework.security.core.userdetails.User(
////                user.getName(),
////                user.getPassword(),
////                user.getRoles().stream()
////                        .map(role -> new SimpleGrantedAuthority(role.getName()))
////                        .toList());
////    }
//
//    public void createNewUser(User user) {
//        user.setRoles(List.of(roleRepository.findByName("ROLE_USER").get()));
//        userRepository.save(user);
//    }
//}
