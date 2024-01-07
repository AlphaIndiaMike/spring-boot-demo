import axios from 'axios';

const getAuthConfig = () => ({
    headers: {
        Authorization: `Bearer ${localStorage.getItem("access_token")}`
    }

})

export const getCustomers = async function() {
    try {
        console.log(`${import.meta.env.VITE_API_BASE_URL}/api/v1/customers`);
        return await axios.get(
            `${import.meta.env.VITE_API_BASE_URL}/api/v1/customers`,
            getAuthConfig());
    }catch(e){
        throw e;
    }
}

export const saveCustomer = async (customer) => {
    try {
        return await axios.post(
            `${import.meta.env.VITE_API_BASE_URL}/api/v1/customers`,
            customer
        )
    } catch (e) {
        throw e;
    }
}

export const updateCustomer = async (id, updateCustomer) => {
    try {
        return await axios.put(
            `${import.meta.env.VITE_API_BASE_URL}/api/v1/customers/${id}`,
            updateCustomer,
            getAuthConfig()
        )
    } catch (e) {
        throw e;
    }
}

export const deleteCustomer = async (id) => {
    try {
        return await axios.delete(
            `${import.meta.env.VITE_API_BASE_URL}/api/v1/customers/${id}`,
            getAuthConfig()
        )
    } catch (e) {
        throw e;
    }
}

export const login = async (usernameAndPassword) => {
    try {
        return await axios.post(
            `${import.meta.env.VITE_API_BASE_URL}/api/v1/auth/login`,
            usernameAndPassword
        )
    } catch (e) {
        throw e;
    }
}

export const uploadCustomerProfilePicture = async (id, formData) => {
    console.log(`${import.meta.env.VITE_API_BASE_URL}/api/v1/customers/${id}/profile-image`);
    try {
        return axios.post(
            `${import.meta.env.VITE_API_BASE_URL}/api/v1/customers/${id}/profile-image`,
            formData,
            {
                ...getAuthConfig(),
                'Content-Type' : 'multipart/form-data'
            }
        );
    } catch (e) {
        throw e;
    }
}

export const customerProfilePicture = async (customerId) => {
    try {
        const response = await axios.get(
            `${import.meta.env.VITE_API_BASE_URL}/api/v1/customers/${customerId}/profile-image`,
            {
                ...getAuthConfig(),
                responseType: 'blob' // Correct way to handle binary data
            }
        );
        return response.data; // This will be a blob
    } catch (e) {
        throw e;
    }
}