package com.doan.backend.repositories;

import com.doan.backend.entity.ChatRoomUser;
import com.doan.backend.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ChatRoomUserRepository extends JpaRepository<ChatRoomUser, String> {
    List<ChatRoomUser> findByChatRoomId(String chatRoomId);

    List<ChatRoomUser> findByUserId(String userId);

    Void deleteByChatRoomIdAndUserId(String chatRoomId, String userId);

    boolean existsByChatRoomIdAndUser_RolesIn(String chatRoomId, Set<RoleEnum> roles);
}
