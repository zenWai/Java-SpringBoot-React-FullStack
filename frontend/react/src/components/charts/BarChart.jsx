import {BarChart, Bar, XAxis, YAxis, Tooltip, Legend, CartesianGrid, ResponsiveContainer} from 'recharts';
import React, {useState, useEffect} from 'react';
import {Spinner} from "@chakra-ui/react";


const renderBar = (props, data, dataKey, color) => {
    let { x, y, width, value } = props;
    const radius = 10; // radius of the circle

    const currentAgeGroup = data.find((group) => group.ageGroup === props.ageGroup);
    const shouldRenderCircle = currentAgeGroup && currentAgeGroup[dataKey] > 0 && !isNaN(currentAgeGroup[dataKey]);
    const circleValue = shouldRenderCircle ? currentAgeGroup[dataKey] : null;
    const formattedCircleValue = isNaN(circleValue) ? '0' : Math.round(circleValue).toFixed(0);
    value = shouldRenderCircle ? [formattedCircleValue] : [];

    return (
        <g>
            <rect {...props} />
            {shouldRenderCircle && (
                <circle cx={x + width / 2} cy={y - radius * 1.3} r={radius * 1.5} fill={color}>
                    <title>{circleValue}</title>
                </circle>
            )}
            {shouldRenderCircle && (
                <text x={x + width / 2} y={y - radius} fill="#fff" textAnchor="middle" dominantBaseline="middle">
                    {value[0]}
                </text>
            )}
        </g>
    );
}
export const BarChartWithMinHeight = ({ customers, loading }) => {

    const [ageGroupData, setAgeGroupData] = useState(null);
    const ageGroups = ['0-14', '15-29', '30-44', '45-59', '60-74', '75-89', '90-104'];
    const getAgeGroup = (age) => {
        const index = Math.floor(age / 15);
        return ageGroups[index];
    };

    useEffect(() => {
        const processCustomersData = async () => {
            const initialAgeGroupData = ageGroups.reduce((acc, group) => ({
                ...acc,
                [group]: {
                    maleCount: 0,
                    femaleCount: 0,
                    maleWithPicCount: 0,
                    femaleWithPicCount: 0,
                }
            }), {});

            for (let customer of customers) {
                const ageGroup = getAgeGroup(customer.age);
                const hasPic = customer.profileImageId ? true : false;

                if (customer.gender === 'MALE') {
                    initialAgeGroupData[ageGroup].maleCount += 1;
                    if (hasPic) {
                        initialAgeGroupData[ageGroup].maleWithPicCount += 1;
                    }
                } else if (customer.gender === 'FEMALE') {
                    initialAgeGroupData[ageGroup].femaleCount += 1;
                    if (hasPic) {
                        initialAgeGroupData[ageGroup].femaleWithPicCount += 1;
                    }
                }
            }

            const finalAgeGroupData = Object.entries(initialAgeGroupData).map(([group, data]) => {
                const totalGroupCount = data.maleCount + data.femaleCount;
                const totalGroupWithPicCount = data.maleWithPicCount + data.femaleWithPicCount;

                return {
                    ageGroup: group,
                    malesWithPic: ((data.maleWithPicCount / data.maleCount) * 100).toFixed(2),
                    femalesWithPic: ((data.femaleWithPicCount / data.femaleCount) * 100).toFixed(2),
                    totalWithPic: ((totalGroupWithPicCount / totalGroupCount) * 100).toFixed(2),
                };
            });

            setAgeGroupData(finalAgeGroupData);
        };

        if (customers.length > 0) {
            processCustomersData();
        }
    }, [customers]);

    if (loading || !ageGroupData) {
        return (<Spinner
            thickness='4px'
            speed='0.65s'
            emptyColor='gray.200'
            color='blue.500'
            size='xl'
        />)
    }

    return (
        <ResponsiveContainer width="100%" height={300}>
            <BarChart data={ageGroupData} margin={{ top: 30, right: 30, left: 20, bottom: 5 }} >
                <CartesianGrid strokeDasharray="3 3"/>
                <XAxis dataKey="ageGroup"/>
                <YAxis domain={[0, 100]} tickCount={5} tick={[0, 25, 50, 75, 100]} />
                <Tooltip/>
                <Legend/>
                <Bar
                    dataKey="femalesWithPic"
                    fill="#82ca9d"
                    shape={(props) =>
                        renderBar(props, ageGroupData, 'femalesWithPic', '#82ca9d')
                    }
                />
                <Bar
                    dataKey="malesWithPic"
                    fill="#8884d8"
                    shape={(props) =>
                        renderBar(props, ageGroupData, 'malesWithPic', '#8884d8')
                    }
                />
            </BarChart>
        </ResponsiveContainer>
    );
}