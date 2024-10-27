import React from 'react'
import { Calendar, Home, Inbox, Search, Settings, Heart, ShoppingCart, ShoppingBag } from "lucide-react"
import { Sidebar, SidebarContent, SidebarGroup, SidebarGroupContent, SidebarMenu, SidebarMenuButton, SidebarMenuItem, SidebarHeader } from '@/components/ui/sidebar'

const items = [
    {
        title: "Home",
        url: "/",
        icon: Home,
    },
    {
        title: "Category",
        url: "#",
        icon: ShoppingBag,
    },
    {
        title: "Product",
        url: "#",
        icon: Heart,
    },
    {
        title: "Order",
        url: "#",
        icon: ShoppingCart,
    },
    {
        title: "Settings",
        url: "#",
        icon: Settings,
    },
]

export default function AppSidebar() {
    return (
        <Sidebar>
            <SidebarHeader>

            </SidebarHeader>
            <SidebarContent>
                <SidebarGroup>
                    <SidebarGroupContent>
                        <SidebarMenu>
                            {items.map((item) => (
                                <SidebarMenuItem key={item.title}>
                                    <SidebarMenuButton asChild>
                                        <a href={item.url}>
                                            <item.icon />
                                            <span>{item.title}</span>
                                        </a>
                                    </SidebarMenuButton>
                                </SidebarMenuItem>
                            ))}
                        </SidebarMenu>
                    </SidebarGroupContent>
                </SidebarGroup>
            </SidebarContent>
        </Sidebar>
    )
}
