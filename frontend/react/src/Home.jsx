import SidebarWithHeader from "./components/shared/SideBar.jsx";
import BasicStatistics from "./components/shared/Dashboard.jsx";
import React from 'react';

const Home = () => {

    return (
        <SidebarWithHeader>
            <BasicStatistics />
        </SidebarWithHeader>
    )
}

export default Home;