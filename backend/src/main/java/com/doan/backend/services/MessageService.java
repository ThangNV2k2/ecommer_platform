package com.doan.backend.services;

import com.doan.backend.dto.request.MessageRequest;
import com.doan.backend.dto.response.ApiResponse;
import com.doan.backend.dto.response.MessageResponse;
import com.doan.backend.entity.Message;
import com.doan.backend.entity.User;
import com.doan.backend.mapper.MessageMapper;
import com.doan.backend.repositories.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Service
public class MessageService {
    MessageRepository messageRepository;
    MessageMapper messageMapper;
    AuthService authService;
    SimpMessagingTemplate messagingTemplate;

    public ApiResponse<Void> sendMessage(MessageRequest messageRequest) {
        User user = authService.getUserByToken();

        Message message = messageMapper.toMessage(messageRequest);
        message.setSender(user);

        MessageResponse messageResponse = messageMapper.toMessageResponse(messageRepository.save(message));
        messagingTemplate.convertAndSend("/topic/chatRoom/" + message.getChatRoom().getId(), messageResponse);

        return ApiResponse.<Void>builder().build();
    }

    public List<MessageResponse> getMessages(String chatRoomId) {
        return messageRepository.findByChatRoomIdOrderByTimestampAsc(chatRoomId).stream().map(messageMapper::toMessageResponse).toList();
    }
}
