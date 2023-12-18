//import React from 'react';
//import ReactDOM from 'react-dom';
import { Formik, Form } from 'formik';
import * as Yup from 'yup';
import { Button, Stack } from "@chakra-ui/react"
import { saveCustomer } from '../../services/client';
import { errorNotification, successNotification } from '../../services/notification';
import MyTextInput from './MyTextInput';
import MySelect from './MySelect';
import LoginPasswordInput from './LoginPasswordInput';

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
          password: 'password'
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
            password: Yup.string()
                .min(5, "Minimum 5 characters")
                .required('password required'),
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
                    console.info(res);
                    successNotification(
                      "Customer saved",
                      `${values.name} was successfully saved`
                    )
                }).catch(err => {
                  console.info(err);
                  console.info(err.code);
                    errorNotification(
                      err.code,
                      err.response.data.message
                    )
                }).finally(() => {
                    console.info(values);
                    setSubmitting(false);
                    fetchCustomers && fetchCustomers();
                })
        }}
      >
        {({isValid, isSubmitting }) => (
                <Form >
                    <Stack spacing={"24px"} >
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

                        <LoginPasswordInput
                          label="Password"
                          name="password"
                          type="password"/>

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