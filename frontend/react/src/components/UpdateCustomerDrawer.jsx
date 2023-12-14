import {
    Button,
    Drawer,
    DrawerBody,
    DrawerFooter,
    DrawerHeader,
    DrawerOverlay,
    DrawerContent,
    DrawerCloseButton,
    useDisclosure
  } from '@chakra-ui/react';
import UpdateCustomerForm from './UpdateCustomerForm';


const UpdateCustomerDrawer = ({ fetchCustomers, initialValues, id }) => {
    const { isOpen, onOpen, onClose } = useDisclosure()
    return (
        <>
            <Button
            colorScheme={"blue"}
            _hover={{
            transform: 'translateY(-2px)',
            boxShadow: 'lg'
            }}
            _focus={{
            bg: 'grey.500'
            }}
            onClick={onOpen}>
                Update customer
            </Button>
            <Drawer isOpen={isOpen} onClose={onClose} size={"xl"}>
            <DrawerOverlay />
            <DrawerContent>
                <DrawerCloseButton />
                <DrawerHeader>Update customer</DrawerHeader>

                <DrawerBody>
                    <UpdateCustomerForm
                        fetchCustomers={fetchCustomers}
                        initialValues={initialValues}
                        id = {id}
                    ></UpdateCustomerForm>
                </DrawerBody>

                <DrawerFooter>

                </DrawerFooter>
            </DrawerContent>
            </Drawer>
        </>
    )
}

export default UpdateCustomerDrawer;
