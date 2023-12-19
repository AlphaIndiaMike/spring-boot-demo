import { FormLabel, Input, Alert, AlertIcon, Box } from "@chakra-ui/react"
import { useField } from 'formik';


const MyTextInput = ({ label, ...props }) => {
    // useField() returns [formik.getFieldProps(), formik.getFieldMeta()]
    // which we can spread on <input>. We can use field meta to show an error
    // message if the field is invalid and it has been touched (i.e. visited)
    const [field, meta] = useField(props);
    return (
      <Box {...props}>
        <FormLabel htmlFor={props.id || props.name}>{label}</FormLabel>
        <Input className="text-input" {...field} {...props} />
        {meta.touched && meta.error ? (
              <Alert className="error" status={"error"} mt={2}>
                  <AlertIcon/>
                  {meta.error}
              </Alert>
        ) : null}
      </Box>
    );
  };

export default MyTextInput;