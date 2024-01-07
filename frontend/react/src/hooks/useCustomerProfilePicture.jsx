import { useState, useEffect } from 'react';
import { customerProfilePicture } from '../services/client';

export const useCustomerProfilePicture = (customerId, imageUpdateKey) => {
    const [imageBlobUrl, setImageBlobUrl] = useState(null);

    useEffect(() => {
        
        console.log("Hook Arguments - customerId:", customerId, "imageUpdateKey:", imageUpdateKey); // Debugging line
        let isActive = true; // Flag to prevent state update if the component is unmounted

        const fetchImage = async () => {
            if (!customerId) return;

            try {
                const blob = await customerProfilePicture(customerId);
                const url = URL.createObjectURL(blob);


                if (isActive) {
                    setImageBlobUrl(url);
                }
            } catch (e) {
                console.error(e);
                // Handle errors as needed
            }
        };

        fetchImage();

        return () => {
            isActive = false;
            if (imageBlobUrl) {
                URL.revokeObjectURL(imageBlobUrl);
                console.log("Image blob url"); // Debugging line
            }
        };
    }, [customerId, imageUpdateKey]);

    return imageBlobUrl;
};

