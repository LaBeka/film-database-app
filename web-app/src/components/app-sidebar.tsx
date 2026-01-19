"use client";

import { useEffect, useState } from "react"
import Link from "next/link"
import { FilmIcon, Home, Search, Settings, UserIcon, LogOut } from "lucide-react"
import { Sidebar, SidebarContent, SidebarGroup, SidebarGroupContent, SidebarGroupLabel, SidebarMenu, SidebarMenuButton, SidebarMenuItem } from "@/components/ui/sidebar"

export function AppSidebar() {
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const [mounted, setMounted] = useState(false);

    useEffect(() => {
        setMounted(true);
        const checkToken = () => setIsLoggedIn(!!localStorage.getItem("token"))
        checkToken();
        window.addEventListener("storage", checkToken)
        return () => window.removeEventListener("storage", checkToken)
    }, []);

    // 3. Important: Prevent rendering the dynamic menu until mounted
    if (!mounted) {
        return null // or a simple skeleton loader
    }

    const handleLogout = () => {
        localStorage.removeItem("token");
        window.dispatchEvent(new Event("storage"));
        setIsLoggedIn(false);
        window.location.href = "/";
    }

    // Define which items are public vs private
    const menuItems = [
        { title: "Home", url: "/", icon: Home, public: true },
        { title: "Users", url: "/users", icon: UserIcon, public: false },
        { title: "Films", url: "/films", icon: FilmIcon, public: false },
        { title: "Reviews", url: "/reviews", icon: Search, public: false },
        { title: "Settings", url: "/settings", icon: Settings, public: false },
    ]

    return (
        <Sidebar>
            <SidebarContent>
                <SidebarGroup>
                    <SidebarGroupLabel>Application</SidebarGroupLabel>
                    <SidebarGroupContent>
                        <SidebarMenu>
                            {menuItems
                                .filter(item => item.public || isLoggedIn)
                                .map((item) => (
                                    <SidebarMenuItem key={item.title}>
                                        <SidebarMenuButton asChild>
                                            <Link href={item.url}>
                                                <item.icon />
                                                <span>{item.title}</span>
                                            </Link>
                                        </SidebarMenuButton>
                                    </SidebarMenuItem>
                                ))}

                            {/* LOGOUT BUTTON IN SIDEBAR */}
                            {isLoggedIn && (
                                <SidebarMenuItem>
                                    <SidebarMenuButton
                                        onClick={handleLogout}
                                        className="text-red-500 hover:text-red-700 hover:bg-red-50"
                                    >
                                        <LogOut />
                                        <span>Logout</span>
                                    </SidebarMenuButton>
                                </SidebarMenuItem>
                            )}
                        </SidebarMenu>
                    </SidebarGroupContent>
                </SidebarGroup>
            </SidebarContent>
        </Sidebar>
    )
}