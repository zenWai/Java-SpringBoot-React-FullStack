import {
    Wrap,
    WrapItem,
    Spinner,
    Text
} from '@chakra-ui/react';
import SidebarWithHeader from "./components/shared/SideBar.jsx";
import CardWithImage from "./components/customer/CustomerCard.jsx";
import CreateCustomerDrawer from "./components/customer/CreateCustomerDrawer.jsx";
import {useCustomers} from "./components/customer/UseFetchCustomers.jsx";

const Customer = () => {
    const { customers, loading, err, fetchCustomers } = useCustomers();
    return (
        <SidebarWithHeader>
            <CreateCustomerDrawer
                fetchCustomers={fetchCustomers}
            />
            {loading ? (
                <Spinner
                    thickness='4px'
                    speed='0.65s'
                    emptyColor='gray.200'
                    color='blue.500'
                    size='xl'
                />
            ) : err ? (
                <Text mt={5}>Ooops there was an error</Text>
            ) : customers.length <= 0 ? (
                <Text mt={5}>No customers available</Text>
            ) : (
                <Wrap justify={"center"} spacing={"30px"}>
                    {customers.map((customer, index) => (
                        <WrapItem key={index}>
                            <CardWithImage
                                {...customer}
                                fetchCustomers={fetchCustomers}
                            />
                        </WrapItem>
                    ))}
                </Wrap>
            )}
        </SidebarWithHeader>
    )
}

export default Customer;