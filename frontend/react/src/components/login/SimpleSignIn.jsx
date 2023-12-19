//import { useState } from 'react';
import {
  Container,
  Stack,
  Heading,
  Center,
  Link,
  Box
} from '@chakra-ui/react';
import LoginForm from './LoginForm';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { useEffect } from 'react';

const SimpleSignIn = () => {
  const {customer} = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    if (customer) {
      navigate("/dashboard");
    }
  })

  return (
    <Container maxW="7xl" p={{ base: 5, md: 10 }}>
      <Center>
        <Stack spacing={4}>
          <Stack align="center">
            <Heading fontSize="2xl">Sign in to your account</Heading>
          </Stack>
                <LoginForm/>
                <Box align={"center"}>
                  <Link color={"blue.500"} href={"/signup"} >
                    Don't have an account? Signup now.
                  </Link>
                </Box>
        </Stack>
      </Center>
    </Container>
  );
};

export default SimpleSignIn;