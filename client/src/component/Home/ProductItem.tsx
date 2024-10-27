import {ProductResponse} from "../../types/product";
import {Card, Divider, Image, Rate, Typography} from "antd";
import '../../sass/product.scss'

const { Text, Title } = Typography;

const ProductItem = ({ product }: { product: ProductResponse }) => {

    return (
        <Card
            hoverable
            cover={<Image alt={product.name} src={product.mainImage}/>}
            style={{width: 240}}
            className="relative"
            classNames={{
                body: "p-0-important"
            }}

        >
            <div className="product-discount flex justify-center align-center fs-12">
                -80%
            </div>
            <div className="flex flex-column px-2">
                <Title level={5} className="m-0">{product.name}</Title>
                <Text type="secondary">VERGENCY</Text>

                <Rate disabled defaultValue={product?.rating ?? 5} className="fs-12 color-start" />
            </div>

            <Divider/>

            <div className="flex justify-space-between align-center px-2">
                <Text delete className="fs-16 text-secondary">
                    đ{product.price}
                </Text>
                <br/>
                <Text className="fs-16">
                    đ{product.price}
                </Text>
            </div>
        </Card>
    )
}

export default ProductItem;