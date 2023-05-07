import { useEffect, useState } from "react";
import { getCustomers } from "../../services/client.js";
import { errorNotification } from "../../services/notification.js";

export const useCustomers = () => {
    const [customers, setCustomers] = useState([]);
    const [loading, setLoading] = useState(false);
    const [err, setError] = useState("");

    const fetchCustomers = async () => {
        setLoading(true);
        try {
            const res = await getCustomers();
            const sortedCustomers = res.data.sort((a, b) => a.id - b.id);
            setCustomers(sortedCustomers);
        } catch (err) {
            setError(err.response.data.message);
            errorNotification(err.code, err.response.data.message);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        console.log("useEffect called in useCustomers");
        fetchCustomers();
    }, []);

    return { customers, loading, err, fetchCustomers };
};