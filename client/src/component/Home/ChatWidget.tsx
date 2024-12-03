import React, { useCallback, useEffect, useRef, useState } from 'react';
import { Input, Button, List, Space, Typography, Row, Col, Popover, Avatar } from 'antd';
import { useGetChatRoomQuery, useSendMessageMutation } from '../../redux/api/chat-api';
import { useSelector } from 'react-redux';
import { RootState } from '../../redux/store';
import { useWebSocket } from '../../hook/useWebSocket ';
import { MessageOutlined, SendOutlined, UserOutlined } from '@ant-design/icons';
import "../../sass/scroll.scss"
const { Text } = Typography;

interface Message {
    sender: string;
    content: string;
    timestamp: string;
}

const ChatWidget: React.FC = () => {
    const [newMessage, setNewMessage] = useState<string>('');
    const userInfo = useSelector((state: RootState) => state.user.user);
    const { data: getChatRoom, refetch } = useGetChatRoomQuery(userInfo?.id ?? "", {
        skip: !userInfo,
    });
    const [sendMessage, { isLoading }] = useSendMessageMutation();
    const messages = getChatRoom?.result?.messages ?? [];
    const [visibleMessageId, setVisibleMessageId] = useState<string | null>(null);

    const handleSubscription = useCallback((message: any) => {
        refetch();
    }, [refetch]);
    const chatListRef = useRef<HTMLDivElement | null>(null);

    useEffect(() => {
        if (chatListRef.current) {
            chatListRef.current.scrollTop = chatListRef.current.scrollHeight;
        }
    }, [messages]);

    useWebSocket(getChatRoom?.result?.id ?? "", handleSubscription);

    const handleSendMessage = async () => {
        if (newMessage.trim()) {
            await sendMessage({ chatRoomId: getChatRoom?.result?.id ?? "", content: newMessage });
            setNewMessage('');
        }
    };

    const chatContent = (
        <div className='flex flex-column justify-space-between'>
            <List
                ref={chatListRef}
                dataSource={messages}
                renderItem={(message, index) => (
                    <List.Item key={index} className="border-none"
                        style={{ padding: '4px 8px' }}
                    >
                        <Space className={`flex w-100 ${message.sender.id === userInfo?.id ? 'justify-end' : 'justify-start'}`}>
                            {message.sender.id !== userInfo?.id && (
                                <Avatar size="small" icon={<UserOutlined />} />
                            )}
                            <div
                                style={{
                                    borderRadius: '8px',
                                    boxShadow: '0 2px 4px rgba(0, 0, 0, 0.1)',
                                    wordBreak: 'break-word',
                                }}
                                className={`p-1 ${message.sender.id === userInfo?.id ? 'background-primary text-white' : 'background-card max-w-80p'}`}
                                onClick={() => setVisibleMessageId(visibleMessageId === message.id ? null : message.id)}
                            >
                                <span>{message.content}</span>
                            </div>
                            {visibleMessageId === message.id && (
                                <div className="fs-10 text-color-secondary mt-1">
                                    {new Date(message.timestamp).toLocaleString()}
                                </div>
                            )}
                        </Space>
                    </List.Item>
                )}
                className='scrollbar-thin scrollbar-thumb-gray-400 scrollbar-track-gray-100 hover:scrollbar-thumb-gray-500 scrollbar-rounded-md height-300 overflow-y-scroll mb-2'
            />

            <div className='w-100 flex gap-1'>
                <Input
                    placeholder="Type your message..."
                    value={newMessage}
                    onChange={(e) => setNewMessage(e.target.value)}
                    onPressEnter={handleSendMessage}
                />
                <Button type="primary" onClick={handleSendMessage} loading={isLoading}>
                    <SendOutlined size={16} />
                </Button>
            </div>
        </div>
    );

    return (
        <div style={{ position: 'absolute', bottom: 20, right: 20 }}>
            <Popover
                content={chatContent}
                title={
                    <Space>
                        <MessageOutlined />
                        <Text strong>Chat</Text>
                    </Space>
                }
                trigger="click"
                placement="topRight"
                arrowPointAtCenter
                overlayStyle={{
                    borderRadius: '8px',
                    boxShadow: '0 4px 8px rgba(0, 0, 0, 0.2)',
                    width: '350px',
                }}
            >
                <Button
                    type="primary"
                    icon={<MessageOutlined />}
                    shape="circle"
                    size="large"
                    style={{
                        position: 'fixed',
                        bottom: '20px',
                        right: '20px',
                        borderRadius: '50%',
                        padding: '0',
                        width: '60px',
                        height: '60px',
                        boxShadow: '0 4px 8px rgba(0, 0, 0, 0.1)',
                        zIndex: 1000,
                    }}
                />
            </Popover>
        </div>
    );
};

export default ChatWidget;
