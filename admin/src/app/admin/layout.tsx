import AppSidebar from "@/app/admin/AppSidebar"
import { Card } from "@/components/ui/card"
import { SidebarProvider, SidebarTrigger } from "@/components/ui/sidebar"
import { CircleChevronLeft } from "lucide-react"

export default function Layout({ children }: { children: React.ReactNode }) {
    return (
        <SidebarProvider>
            <AppSidebar />
            <main>
                <SidebarTrigger className="absolute">
                    <CircleChevronLeft />
                </SidebarTrigger>
            </main>
            <div className="p-10 w-full bg-[#eef1f8]">
                <Card className="w-full h-full">
                    {children}
                </Card>
            </div>
            
        </SidebarProvider>
    )
}
