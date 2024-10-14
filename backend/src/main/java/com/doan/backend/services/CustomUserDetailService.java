package com.doan.backend.services;

import com.doan.backend.entity.User;
import com.doan.backend.enums.RoleEnum;
import com.doan.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Tìm kiếm người dùng trong cơ sở dữ liệu bằng email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // Trả về một đối tượng UserDetails chứa thông tin người dùng
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),  // Password đã mã hóa (hash)
                user.getIsActive(),    // Trạng thái người dùng có đang hoạt động hay không
                true,                // accountNonExpired
                true,                // credentialsNonExpired
                true,                // accountNonLocked
                getAuthorities(user.getRoles())  // Chuyển đổi các vai trò thành quyền (authorities)
        );
    }

    // Chuyển đổi danh sách vai trò của người dùng thành danh sách các quyền
    private Collection<? extends GrantedAuthority> getAuthorities(Set<RoleEnum> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toList());
    }
}
