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
import InsertCustomerForm from '../shared/InsertCustomerForm';


const AddIcon = () => "+";

const InsertCustomerDrawer = ({ fetchCustomers }) => {
    const { isOpen, onOpen, onClose } = useDisclosure()
    return (
        <>
            <Button leftIcon={<AddIcon/>}
            colorScheme={"teal"}
            onClick={onOpen}>
                Create customer
            </Button>
            <Drawer isOpen={isOpen} onClose={onClose} size={"xl"}>
            <DrawerOverlay />
            <DrawerContent>
                <DrawerCloseButton />
                <DrawerHeader>Insert new customer</DrawerHeader>

                <DrawerBody>
                    <InsertCustomerForm
                        fetchCustomers={fetchCustomers}
                    ></InsertCustomerForm>
                </DrawerBody>

                <DrawerFooter>

                </DrawerFooter>
            </DrawerContent>
            </Drawer>
        </>
    )
}

export default InsertCustomerDrawer;
