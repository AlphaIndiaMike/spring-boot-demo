
import { Form, Formik } from 'formik';
import { 
  VStack,
  Button,
  useColorModeValue,
} from "@chakra-ui/react"
import * as Yup from 'yup'
import {useAuth} from '../context/AuthContext';
import { errorNotification } from '../../services/notification';
import { useNavigate } from 'react-router-dom';
import LoginTextInput from '../shared/LoginTextInput';
import LoginPasswordInput from '../shared/LoginPasswordInput';

const LoginForm = ({}) => {
    const {user, login} = useAuth();
    const navigate = useNavigate();
    return (
        <>
            <Formik
                validateOnMount={true}
                validationSchema={
                    Yup.object({
                        username: Yup.string()
                            .email("Must be valid e-mail")
                            .required("Email is required"),
                        password: Yup.string()
                            .max(30, "Password cannot be more than 30 chars")
                            .required("Password is required")
                    })
                }
                initialValues={{username: '', password: ''}}
                onSubmit={(values, {setSubmitting}) => {
                    setSubmitting(true);
                    //alert(JSON.stringify(values, null, 0));
                    login(values).then(res => {
                        //TODO: navigate to dashboard 
                        navigate("/dashboard");
                        //console.log("Successfully logged in", res);

                    }).catch(err => {
                        errorNotification(
                            err.code, 
                            err.response.data.message
                        )
                    }).finally(()=>{
                        setSubmitting(false);
                    })
                }}
            >
            {({isValid, isSubmitting}) => {
                return (
                <Form>
                  <VStack
                  boxSize={{ base: 'xs', sm: 'sm', md: 'md' }}
                  h="max-content !important"
                  bg={useColorModeValue('white', 'gray.700')}
                  rounded="lg"
                  boxShadow="lg"
                  p={{ base: 5, sm: 10 }}
                  spacing={10}
                  >
                    <VStack spacing={4} w="100%">
                        <LoginTextInput
                            label={"Email"}
                            name={"username"}
                            type={"email"}
                            placeholder={"username@email.com"}
                            w="100%"
                        />
                        <LoginPasswordInput
                            label={"Password"}
                            name={"password"}
                            type={"password"}
                            placeholder={"Type your password"}
                            w="100%"
                        />
                        <Button
                            type={"submit"}
                            isDisabled={!isValid || isSubmitting}
                            bg="teal.400"
                            color="white"
                            _hover={{
                                bg: 'blue.500'
                            }}
                            rounded="md"
                            w="100%"
                        >
                            Login
                        </Button>
                    </VStack>
                  </VStack>
                </Form>
                )
            }}
  
            </Formik>
        </>
    );
};

export default LoginForm;

