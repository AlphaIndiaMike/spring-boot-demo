import {
    Container,
    Box,
    Heading,
    Link
  } from '@chakra-ui/react';

import InsertCustomerForm from "../shared/InsertCustomerForm"


const Signup = () => {
    return (
        <Container maxW="5xl" p={{ base: 5, md: 10 }}>
            <Heading as="h3" size="lg" mb="4" fontWeight="bold" textAlign="left">
                Register
            </Heading>
            <Box mb={{ base: '2.5rem', lg: '4rem' }}>
                <InsertCustomerForm></InsertCustomerForm>
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