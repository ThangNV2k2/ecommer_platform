package com.doan.backend.services;

import com.doan.backend.dto.response.ApiResponse;
import com.doan.backend.dto.response.ChatRoomResponse;
import com.doan.backend.dto.response.MessageResponse;
import com.doan.backend.entity.ChatRoom;
import com.doan.backend.entity.ChatRoomUser;
import com.doan.backend.entity.User;
import com.doan.backend.enums.ChatRoomStatus;
import com.doan.backend.enums.RoleEnum;
import com.doan.backend.exception.Unauthorized;
import com.doan.backend.repositories.ChatRoomRepository;
import com.doan.backend.repositories.ChatRoomUserRepository;
import com.doan.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Service
public class ChatRoomService {
    ChatRoomRepository chatRoomRepository;
    ChatRoomUserRepository chatRoomUserRepository;
    MessageService messageService;
    AuthService authService;
    UserRepository userRepository;

    public ApiResponse<ChatRoomResponse> getChatRoom(String customerId) {
        User customer = userRepository.findById(customerId).orElseThrow(() -> new RuntimeException("Customer not found"));
        User user = authService.getUserByToken();
        Set<RoleEnum> roles = user.getRoles();
        if (roles.contains(RoleEnum.ADMIN) || roles.contains(RoleEnum.STAFF) || user.getId().equals(customerId)) {
            Optional<ChatRoom> chatRoomOptional = chatRoomRepository.findByCustomerId(customerId);
            if (chatRoomOptional.isPresent()) {
                ChatRoom chatRoom = chatRoomOptional.get();
                List<MessageResponse> messageResponses = messageService.getMessages(chatRoom.getId());
                ChatRoomResponse chatRoomResponse = new ChatRoomResponse();
                chatRoomResponse.setId(chatRoom.getId());
                chatRoomResponse.setCustomerId(chatRoom.getCustomer().getId());
                chatRoomResponse.setStatus(ChatRoomStatus.OPEN);
                chatRoomResponse.setMessages(messageResponses);
                return ApiResponse.<ChatRoomResponse>builder()
                        .code(200)
                        .message("Chat room retrieved successfully")
                        .result(chatRoomResponse)
                        .build();
            } else {
                ChatRoom chatRoom = new ChatRoom();
                chatRoom.setCustomer(customer);
                chatRoom.setStatus(ChatRoomStatus.OPEN);
                ChatRoom chatRoomSave = chatRoomRepository.save(chatRoom);

                return ApiResponse.<ChatRoomResponse>builder()
                        .code(200)
                        .message("Chat room created successfully")
                        .result(new ChatRoomResponse(chatRoomSave.getId(), chatRoomSave.getCustomer().getId(), chatRoomSave.getStatus(), List.of()))
                        .build();
            }
        } else {
            throw new Unauthorized("Unauthorized");
        }

    }

    public void addUserToChatRoom(ChatRoom chatRoom, User user) {
        ChatRoomUser chatRoomUser = new ChatRoomUser();
        chatRoomUser.setChatRoom(chatRoom);
        chatRoomUser.setUser(user);
        chatRoomUserRepository.save(chatRoomUser);
    }

    public void deleteUserFromChatRoom(String userId, String chatRoomId) {
        chatRoomUserRepository.deleteByChatRoomIdAndUserId(chatRoomId, userId);
    }
}
