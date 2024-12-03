package com.doan.backend.repositories;

import com.doan.backend.entity.ChatRoomUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomUserRepository extends JpaRepository<ChatRoomUser, String> {
    List<ChatRoomUser> findByChatRoomId(String chatRoomId);

    List<ChatRoomUser> findByUserId(String userId);

    Void deleteByChatRoomIdAndUserId(String chatRoomId, String userId);
}
