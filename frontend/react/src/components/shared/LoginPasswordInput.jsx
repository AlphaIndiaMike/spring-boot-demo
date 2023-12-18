import { useState } from 'react';
import { useField } from 'formik';
import { FormLabel,
  Input,
  Alert,
  AlertIcon,
  Button,
  InputGroup,
  InputRightElement,
  useColorModeValue,
  FormControl
} from "@chakra-ui/react"

const LoginPasswordInput = ({ label, ...props }) => {
    const [show, setShow] = useState(false);
    const handleClick = () => setShow(!show);
    const [field, meta] = useField(props);
    return (
      <FormControl {...props}>
        <FormLabel htmlFor={props.id || props.name}>{label}</FormLabel>
        <InputGroup size="md">
            <Input rounded="md" className="text-input" {...field} {...props} type={show ? 'text' : 'password'} />
            <InputRightElement width="4.5rem">
            <Button
                h="1.75rem"
                size="sm"
                rounded="md"
                bg={useColorModeValue('gray.300', 'gray.700')}
                _hover={{
                bg: useColorModeValue('gray.400', 'gray.800')
                }}
                onClick={handleClick}
            >
                {show ? 'Hide' : 'Show'}
            </Button>
            </InputRightElement>
        </InputGroup>
        {meta.touched && meta.error ? (
              <Alert className="error" status={"error"} mt={2}>
                  <AlertIcon/>
                  {meta.error}
              </Alert>
        ) : null}
      </FormControl>
    );
  };

export default LoginPasswordInput;