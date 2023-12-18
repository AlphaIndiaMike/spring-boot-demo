
import {
  Tag,
  Heading,
  Avatar,
  Box,
  Center,
  Image,
  Flex,
  Text,
  Stack,
  Button,
  useColorModeValue,
  useDisclosure
} from '@chakra-ui/react'

import React from 'react'
import DeleteDialog from '../shared/DeleteDialog';
import { deleteCustomer } from '../../services/client';
import { successNotification } from '../../services/notification';
import UpdateCustomerDrawer from './UpdateCustomerDrawer';

export default function CardWithImage({id, name, email, age, gender, ImageNumber, fetchCustomers}) {
  const randomUserGender = gender === "MALE" ? "men" : "women";  
  const { isOpen, onOpen, onClose } = useDisclosure()
  const cancelRef = React.useRef()
  return (
    <Center py={6}>
      <Box
        maxW={'400px'}
        minW={'300px'}
        m={2}
        w={'full'}
        bg={useColorModeValue('white', 'gray.800')}
        boxShadow={'lg'}
        rounded={'md'}
        overflow={'hidden'}>
        <Image
          h={'120px'}
          w={'full'}
          src={
            'https://images.unsplash.com/photo-1612865547334-09cb8cb455da?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=634&q=80'
          }
          objectFit="cover"
          alt="#"
        />
        <Flex justify={'center'} mt={-12}>
          <Avatar
            size={'xl'}
            src={
              `https://randomuser.me/api/portraits/${randomUserGender}/${ImageNumber}.jpg`
            }
            css={{
              border: '2px solid white',
            }}
          />
        </Flex>

        <Box p={6}>
          <Stack spacing={2} align={'center'} mb={5}>
            <Tag borderRadius="full">{id}</Tag>
            <Heading fontSize={'2xl'} fontWeight={500} fontFamily={'body'}>
              {name}
            </Heading>
            <Text color={'gray.500'}>{email}</Text>
            <Text color={'gray.500'}>Age {age}</Text>
          </Stack>
        </Box>
        <Stack direction={'row'} justify={'center'} margin={2} p={4}>
          <Stack>
            <UpdateCustomerDrawer 
              fetchCustomers={fetchCustomers}
              initialValues={{ name, email, age}}
              id = {id}
            />
          </Stack>
          <Stack>
            <Button 
              colorScheme={'red'}
              _hover={{
                transform: 'translateY(-2px)',
                boxShadow: 'lg'
              }}
              _focus={{
                bg: 'grey.500'
              }}
              onClick={onOpen}
              >Delete</Button>
              <DeleteDialog
                title = "Delete customer"
                text = "Are you sure you want to delete customer?"
                isOpen = {isOpen}
                cancelRef = {cancelRef} 
                onClose = {onClose}
                onAction = {() => {
                  deleteCustomer(id).then(
                    res => {
                      console.log(res);
                      successNotification(
                        'Customer deleted',
                        `${name} was successfully deleted!`
                      )
                    }).catch(err => {
                        errorNotification(
                          err.code,
                          err.response.data.message
                        )
                    }).finally(() => {
                      fetchCustomers();
                      onClose();
                    })
                }}
              ></DeleteDialog>
            </Stack>
        </Stack>
      </Box>
    </Center>
  )
}