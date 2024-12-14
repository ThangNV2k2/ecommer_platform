import { FC, useState, useEffect, useRef, useCallback } from 'react';
import { MessageResponse } from '@/types/message';
import { Input } from '@/components/ui/input';
import { Button } from '@/components/ui/button';
import { useWebSocket } from '@/hooks/use-websocket';
import { useGetChatRoomQuery, useSendMessageMutation } from '@/redux/api/chat-api';
import { Loader2, Send } from 'lucide-react';
import "@/css/scroll.css";
import { User } from '@/types/user-info';
import { Avatar, AvatarFallback } from '@/components/ui/avatar';

interface ChatWindowProps {
    selectedUser: User;
}

const ChatWindow: FC<ChatWindowProps> = ({ selectedUser }) => {
    const [message, setMessage] = useState('');
    const [messages, setMessages] = useState<MessageResponse[]>([]);
    const messageEndRef = useRef<HTMLDivElement>(null);
    const [visibleMessageId, setVisibleMessageId] = useState<string | null>(null);

    const { data: chatRoomData } = useGetChatRoomQuery(selectedUser.id, {
        skip: !selectedUser.id,
    });
    console.log(selectedUser);
    const [sendMessage, {isLoading: isSending}] = useSendMessageMutation();

    const handleSendMessage = () => {
        if (message && selectedUser.id) {
            sendMessage({
                chatRoomId: chatRoomData?.result?.id ?? "",
                content: message,
            }).unwrap()
                .then(() => setMessage(''))
                .catch((error) => console.error("Error sending message:", error));
        }
    };

    const handleSubscription = useCallback((message: MessageResponse) => {
        setMessages((prevMessages) => [...prevMessages, message]);
    }, [setMessages]);

    useWebSocket(chatRoomData?.result?.id ?? "", handleSubscription);

    useEffect(() => {
        if (messageEndRef.current) {
            messageEndRef.current.scrollIntoView({ behavior: 'smooth' });
        }
    }, [messages]);

    useEffect(() => {
        if (chatRoomData?.result?.messages) {
            setMessages(chatRoomData.result.messages);
        }
    }, [chatRoomData]);

    return (
        <div className="flex flex-col flex-1 p-4 rounded-lg bg-white h-full">
            <div className="flex-1 max-h-[768px] overflow-y-auto scrollbar-thin scrollbar-thumb-gray-400 scrollbar-track-gray-100 hover:scrollbar-thumb-gray-500 scrollbar-rounded-md mb-4 space-y-2 px-2">
                {messages.map((msg) => (
                    <div key={msg.id} className={`flex flex-col ${msg.sender.id === selectedUser.id ? 'items-start' : 'items-end'}`}>
                        <div className="flex items-center gap-2">
                            {msg.sender.id === selectedUser.id && (
                                <Avatar className="w-10 h-10 inline-block">
                                    <AvatarFallback>{selectedUser?.name.slice(0, 2)?.toUpperCase() ?? 'CN'}</AvatarFallback>
                                </Avatar>
                            )}
                            <Button onClick={() => setVisibleMessageId(visibleMessageId === msg.id ? null : msg.id)}
                                className={`px-4 rounded-lg max-w-xs h-auto text-white ${msg.sender.id === selectedUser.id ? 'bg-orange-300' : 'bg-primary'}`}
                            >
                                {msg.content}
                            </Button>
                        </div>
                        {visibleMessageId === msg.id && (
                            <div className="block text-xs text-gray-500 mt-1">
                                {new Date(msg.timestamp).toLocaleString()}
                            </div>
                        )}
                    </div>
                ))}
                <div ref={messageEndRef}></div>
            </div>

            <div className="flex items-center space-x-4 mt-auto">
                <Input
                    value={message}
                    onChange={(e) => setMessage(e.target.value)}
                    placeholder="Type a message..."
                    className="flex-1 p-3 rounded-lg"
                    onKeyDown={(e) => {
                        if (e.key === 'Enter' && !e.shiftKey) {
                            e.preventDefault();
                            handleSendMessage();
                        }
                    }}
                />
                <Button onClick={handleSendMessage} disabled={isSending} className="p-3 rounded-lg">
                    {isSending ? <Loader2 className="animate-spin" /> : <Send size={20} />}
                </Button>
            </div>
        </div>

    );
};

export default ChatWindow;
