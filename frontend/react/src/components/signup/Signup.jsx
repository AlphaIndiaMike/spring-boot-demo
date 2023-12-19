import {
    Container,
    Box,
    Heading,
    Link
  } from '@chakra-ui/react';
import { useNavigate } from 'react-router-dom';
import InsertCustomerForm from "../shared/InsertCustomerForm"
import {useAuth} from '../context/AuthContext'



const Signup = () => {
    const { customer, setCustomerFromToken } = useAuth();
    const navigate = useNavigate();

    return (
        <Container maxW="5xl" p={{ base: 5, md: 10 }}>
            <Heading as="h3" size="lg" mb="4" fontWeight="bold" textAlign="left">
                Register
            </Heading>
            <Box mb={{ base: '2.5rem', lg: '4rem' }}>
                <InsertCustomerForm
                    onSuccess={(token) => {
                        localStorage.setItem("access_token", token);
                        setCustomerFromToken();
                        navigate("/dashboard/customers");
                    }}
                />
            </Box>
            <Box align={"center"}>
                <Link color={"blue.500"} href={"/"} >
                    Already registered? Log in.
                </Link>
            </Box>
        </Container>
        
    )
}

export default Signup;  