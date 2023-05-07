import { PieChart, Pie, Cell, ResponsiveContainer, Tooltip, Legend } from 'recharts';
import {Spinner} from '@chakra-ui/react';

const COLORS = ['#0088FE', '#00C49F', '#FFBB28', '#FF8042'];
const RADIAN = Math.PI / 180;

const PieChartWithCustomizedLabel = ({ customers, loading }) => {

    if (loading || !customers) {
        return (<Spinner
                thickness='4px'
                speed='0.65s'
                emptyColor='gray.200'
                color='blue.500'
                size='xl'
        />)
    }

    const { maleCount, femaleCount } = customers.reduce(
        (counts, { gender }) => {
            if (gender === "MALE") {
                counts.maleCount++;
            } else if (gender === "FEMALE") {
                counts.femaleCount++;
            }
            return counts;
        },
        { maleCount: 0, femaleCount: 0 }
    );

    const genderData = [
        { name: "MALE", value: maleCount },
        { name: "FEMALE", value: femaleCount }
    ];

    const renderCustomizedLabel = ({ cx, cy, midAngle, innerRadius, outerRadius, percent }) => {
        const radius = innerRadius + (outerRadius - innerRadius) * 0.5;
        const x = cx + radius * Math.cos(-midAngle * RADIAN);
        const y = cy + radius * Math.sin(-midAngle * RADIAN);
        return (
            <text x={x} y={y} fill="white" textAnchor={x > cx ? 'start' : 'end'} dominantBaseline="central">
                {`${(percent * 100).toFixed(0)}%`}
            </text>
        );
    };

    return (
        <ResponsiveContainer width="100%" height={400}>
            <PieChart>
                <Pie
                    data={genderData}
                    cx="50%"
                    cy="50%"
                    labelLine={false}
                    label={renderCustomizedLabel}
                    outerRadius={80}
                    fill="#8884d8"
                    dataKey="value"
                >
                    {genderData.map((entry, index) => (
                        <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                    ))}
                </Pie>
                <Tooltip />
                <Legend layout="horizontal" verticalAlign="bottom" align="center" />
            </PieChart>
        </ResponsiveContainer>
    );
};

export default PieChartWithCustomizedLabel;