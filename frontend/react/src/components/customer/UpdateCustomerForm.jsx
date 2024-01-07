//import React from 'react';
//import ReactDOM from 'react-dom';
import { Formik, Form, useField } from 'formik';
import * as Yup from 'yup';
import { FormLabel, Input, Alert, AlertIcon, Select, Box, Button,
            Stack, VStack, Image } from "@chakra-ui/react"
import { updateCustomer, 
  uploadCustomerProfilePicture } from '../../services/client';
import { errorNotification, successNotification } from '../../services/notification';
import React, {useCallback, useMemo, useState} from 'react'
import {useDropzone} from 'react-dropzone'
import { useCustomerProfilePicture } from '../../hooks/useCustomerProfilePicture'

const baseStyle = {
  flex: 1,
  display: 'flex',
  flexDirection: 'column',
  alignItems: 'center',
  padding: '20px',
  borderWidth: 2,
  borderRadius: 2,
  borderColor: '#eeeeee',
  borderStyle: 'dashed',
  backgroundColor: '#fafafa',
  color: '#bdbdbd',
  outline: 'none',
  transition: 'border .24s ease-in-out'
};

const focusedStyle = {
  borderColor: '#2196f3'
};

const acceptStyle = {
  borderColor: '#00e676'
};

const rejectStyle = {
  borderColor: '#ff1744'
};

function StyledDropzone({customerId, onUploadComplete, ...props}) {
  const onDrop = useCallback(acceptedFiles => {
    const formData = new FormData();
    formData.append("file", acceptedFiles[0])
    uploadCustomerProfilePicture(
      customerId,
      formData
    ).then(() => {
      successNotification("Success", "Profile picture uploaded");
      if(onUploadComplete && typeof onUploadComplete === 'function') {
        onUploadComplete();
      }
    }).catch((e) => {
      errorNotification("Error", "Profile picture failed to upload. Reason: " + e.message);
    }) 
  }, [customerId, onUploadComplete]);

  const {
    getRootProps,
    getInputProps,
    isFocused,
    isDragAccept,
    isDragReject
  } = useDropzone({onDrop});

  const style = useMemo(() => ({
    ...baseStyle,
    ...(isFocused ? focusedStyle : {}),
    ...(isDragAccept ? acceptStyle : {}),
    ...(isDragReject ? rejectStyle : {})
  }), [
    isFocused,
    isDragAccept,
    isDragReject
  ]);

  return (
    <div className="container">
      <div {...getRootProps({style})}>
        <input {...getInputProps()} />
        <p>Drag 'n' drop, or click to select the profile picture</p>
      </div>
    </div>
  );
}

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
const UpdateCustomerForm = ({ fetchCustomers, initialValues, id }) => {
  const [imageUpdateKey, setImageUpdateKey] = useState(0);
  const imageBlobUrl = useCustomerProfilePicture(id, imageUpdateKey);

  const handleImageUpload = () => {
    // Increment the key to trigger a re-fetch
    setImageUpdateKey(k => k + 1);
    // Dispatch a custom event after successful image upload
    window.dispatchEvent(new CustomEvent('avatarUpdated'));
  };

  return (
    <>
      <div style={{height:40 + 'px'}}></div>
      <VStack spacing={'5'} mb={'5'}>
        <Image 
          key={imageUpdateKey}
          borderRadius={'full'}
          boxSize={'150px'}
          objectFit={'cover'}
          src={imageBlobUrl}/>
      </VStack>
      <StyledDropzone customerId={id} onUploadComplete={handleImageUpload}/>
      <div style={{height:40 + 'px'}}></div>
      <Formik
        initialValues={initialValues}
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
        })}
        onSubmit={(values, { setSubmitting }) => {
            setSubmitting(true);
            updateCustomer(id, values)
                .then(res => {
                    successNotification(
                      "Customer updated",
                      `${values.name} was successfully updated`
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
        {({isValid, isSubmitting, dirty}) => (
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

                        <Button
                        //dirty tells us if the values have changed
                            isDisabled={ !(isValid && dirty) || isSubmitting }
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

export default UpdateCustomerForm