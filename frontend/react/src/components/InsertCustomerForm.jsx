//import React from 'react';
//import ReactDOM from 'react-dom';
import { Formik, Form, useField } from 'formik';
import * as Yup from 'yup';
import { FormLabel, Input, Alert, AlertIcon, Select, Box, Button,
            Stack } from "@chakra-ui/react"
import { saveCustomer } from '../services/client';
import { errorNotification, successNotification } from '../services/notification';

const MyTextInput = ({ label, ...props }) => {
  // useField() returns [formik.getFieldProps(), formik.getFieldMeta()]
  // which we can spread on <input>. We can use field meta to show an error
  // message if the field is invalid and it has been touched (i.e. visited)
  const [field, meta] = useField(props);
  return (
    <Box>
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

// And now we can use these
const InsertCustomerForm = ({ fetchCustomers }) => {
  return (
    <>
      <Formik
        initialValues={{
          name: '',
          email: '',
          age: '',
          gender: '', // added for our select
        }}
        validationSchema={Yup.object({
            name: Yup.string()
                .min(3, "Minimum 3 characters")
                .max(100, 'Maximum 100 characters or less')
                .required('Required'),
            email: Yup.string()
                .email('Invalid email address')
                .required('Required'),
            age: Yup.number()
                .min(0, 'Minimum age is 0')
                .max(1000, 'Are you a vampire?')
                .required('Required'),
            gender: Yup.string()
                .oneOf(
                ['MALE', 'FEMALE'],
                'Invalid Gender'
                )
                .required('Required'),
        })}
        onSubmit={(values, { setSubmitting }) => {
            setSubmitting(true);
            saveCustomer(values)
                .then(res => {
                    successNotification(
                      "Customer saved",
                      `${customer.name} was successfully saved`
                    )
                }).catch(err => {
                    errorNotification(
                      err.code,
                      err.response.data.message
                    )
                }).finally(() => {
                    setSubmitting(false);
                    fetchCustomers();
                })
        }}
      >
        {({isValid, isSubmitting}) => (
                <Form>
                    <Stack spacing={"24px"}>
                        <MyTextInput
                            label="Name"
                            name="name"
                            type="text"
                            placeholder="John Townson"
                        />

                        <MyTextInput
                            label="E-mail"
                            name="email"
                            type="email"
                            placeholder="john.t@gmail.com"
                        />

                        <MyTextInput
                            label="Age"
                            name="age"
                            type="number"
                            placeholder="18"
                        />

                        <MySelect label="Gender" name="gender">
                            <option value="">Select a gender</option>
                            <option value="MALE">Male</option>
                            <option value="FEMALE">Female</option>
                        </MySelect>
                        <Button
                            isDisabled={ !isValid || isSubmitting }
                                type="submit"
                                colorScheme={"teal"} 
                                mt={2}
                                float={'right'}
                                >
                                    Submit
                        </Button>
                    </Stack>
                </Form>
        )}
        
      </Formik>
    </>
  );
};

export default InsertCustomerForm