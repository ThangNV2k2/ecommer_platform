import {Menu} from "antd";
import {CategoryResponse} from "../../types/category";
import {MinusOutlined} from "@ant-design/icons";
import {useState} from "react";

interface CategoryProps {
    categories: CategoryResponse[];
    onCategorySelect: (categoryId: string) => void;
}

const Category: React.FC<CategoryProps> = ({ categories, onCategorySelect }) => {
    const [collapsed, setCollapsed] = useState(false);

    const handleToggle = () => {
        setCollapsed(!collapsed);
    };

    return (
        <Menu
            mode="inline"
            defaultSelectedKeys={['1']}
            className="h-100"
            onClick={(e) => onCategorySelect(e.key)}
        >
            <Menu.SubMenu
                key="sub1"
                title={
                    <span className="fs-18 fw-600 text-color">
                        Categories
                    </span>
                }
                className="background-card"
            >
                <Menu.Divider />
                {!collapsed &&
                    categories.map((category) => (
                        <Menu.Item key={category.id}>
                            <span className="fs-14 fw-600 text-primary">{category.name}</span>
                        </Menu.Item>
                    ))}
            </Menu.SubMenu>
        </Menu>
    );
};

export default Category;