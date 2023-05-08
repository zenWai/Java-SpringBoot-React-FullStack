import SidebarWithHeader from "./components/shared/SideBar.jsx";
import BasicStatisticsDashboard from "./components/shared/Dashboard.jsx";
import React from 'react';

const Home = () => {

    return (
        <SidebarWithHeader>
            <BasicStatisticsDashboard />
        </SidebarWithHeader>
    )
}
export default Home;