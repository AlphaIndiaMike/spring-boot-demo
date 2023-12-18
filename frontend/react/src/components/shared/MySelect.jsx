import { FormLabel, Alert, AlertIcon, Select, Box } from "@chakra-ui/react"
import { useField } from 'formik';

const MySelect = ({ label, ...props }) => {
    const [field, meta] = useField(props);
    return (
      <Box>
        <FormLabel htmlFor={props.id || props.name}>{label}</FormLabel>
        <Select {...field} {...props} />
        {meta.touched && meta.error ? (
              <Alert className="error" status={"error"} mt={2}>
                  <AlertIcon/>
                  {meta.error}
              </Alert>
        ) : null}
      </Box>
    );
  };


export default MySelect;