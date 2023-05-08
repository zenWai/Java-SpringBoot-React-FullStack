import {
    Box,
    chakra,
    SimpleGrid,
    Text,
    Image,
    Stack,
} from '@chakra-ui/react';
import PieChartWithCustomizedLabel from '../charts/PieChartWithCustomizedLabel.jsx';
import { BarChartWithMinHeight } from '../charts/BarChart.jsx';
import {useCustomers} from "../customer/UseFetchCustomers.jsx";

function Dashboard({ title, stat, imageSrc }) {
    return (
        <Box
            p={6}
            borderWidth={1}
            borderRadius="lg"
            boxShadow="lg"
            textAlign="center"
            bg="white"
        >
            <Stack
                direction={{ base: 'column', sm: 'row' }}
                alignItems="center"
                justifyContent="center"
            >
                <Image
                    mt={{ base: 0, sm: 0 }}
                    mb={{ base: 4, sm: 0 }}
                    src={imageSrc}
                    alt={title}
                    boxSize={{ base: '60px', sm: '80px' }}
                />
                <Box>
                    <Text fontSize="2xl" fontWeight="bold" mb={4}>
                        {title}
                    </Text>
                    <Text fontSize="4xl">{stat}</Text>
                </Box>
            </Stack>
        </Box>
    );
}

export default function BasicStatisticsDashboard() {
    const { customers, loading } = useCustomers();
    if (loading || !customers) {
        return <Text>Loading...</Text>;
    }

    const { length: totalCustomers } = customers;
    const sumOfAges = customers.reduce((total, customer) => {
        return total + customer.age;
    }, 0);

    const averageAge = (sumOfAges / totalCustomers).toFixed(0);

    const numCustomersWithPic = customers.filter((customer) => {
        const { id } = customer;
        const profileImageId = customer.profileImageId;
        return profileImageId ? true : false;
    }).length;
    const percentCustomersWithPic = ((numCustomersWithPic / totalCustomers) * 100).toFixed(0);

    return (
        <Box
            maxW="100%"
            mx="auto"
            pt={1}
            px={{ base: 1, sm: 1, md: 1 }}
        >
            <chakra.h1
                textAlign={'center'}
                fontSize={'4xl'}
                py={10}
                fontWeight={'bold'}
            >
                Customer Overview
            </chakra.h1>
            <SimpleGrid columns={{ base: 1, sm: 2, md: 3 }} spacing={{ base: 5, lg: 8 }}>
                <Dashboard
                    title="Total Customers"
                    stat={totalCustomers}
                    imageSrc="https://st4.depositphotos.com/1842549/21262/i/450/depositphotos_212625700-stock-photo-clients-icon-internet-button-white.jpg"
                    bg="white"
                />
                <Dashboard
                    title="Average Customers age"
                    stat={`${averageAge}y`}
                    imageSrc="https://cdn-icons-png.flaticon.com/512/31/31370.png"
                    bg="white"
                />
                <Dashboard
                    title="Total Customers with Profile Picture"
                    stat={`${percentCustomersWithPic}%`}
                    imageSrc="https://cdn-icons-png.flaticon.com/128/4795/4795910.png"
                    bg="white"
                />
            </SimpleGrid>
            <SimpleGrid columns={{ base: 1, md: 2 }} spacing={{ base: 5, lg: 8 }} mt={10}>
                <Box
                    p={6}
                    borderWidth={1}
                    borderRadius="lg"
                    boxShadow="lg"
                    bg="white"
                    height={400}
                >
                    <Text fontSize="2xl" fontWeight="bold" mb={4}>
                        Gender Representation
                    </Text>
                    <PieChartWithCustomizedLabel customers={customers}/>
                </Box>
                <Box
                    p={6}
                    borderWidth={1}
                    borderRadius="lg"
                    boxShadow="lg"
                    bg="white"
                    height={400}
                >
                    <Text fontSize="2xl" fontWeight="bold" mb={4}>
                        Age Group Distribution
                    </Text>
                    <BarChartWithMinHeight customers={customers} />
                </Box>
            </SimpleGrid>
        </Box>
    );
}