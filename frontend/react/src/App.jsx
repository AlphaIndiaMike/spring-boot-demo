import { Wrap, WrapItem, Spinner, Text } from '@chakra-ui/react'
import SidebarWithHeader from './components/shared/Sidebar';
import { useEffect, useState } from 'react';
import { getCustomers } from './services/client';
import CardWithImage from './components/customer/CardWithImage';
import InsertCustomerDrawer from './components/customer/InsertCustomerDrawer';
import { errorNotification, successNotification } from './services/notification';

const App = () => {

  const [customers, setCustomers] = useState([]);
  const [loading, setLoading] = useState(false);
  const [err, setError] = useState("");
  const fetchCustomers = () => {
    setLoading(true);
    setTimeout( () => {
      getCustomers().then(res => {
        //console.log(res);
        setCustomers(res.data);
      }).catch(err => {
          setError(err.response.data.message);
          errorNotification(
            err.code,
            err.response.data.message
          )
      }).finally(() => {
        setLoading(false);
      })
    }, 500)
  }

  useEffect(function() {
    fetchCustomers();
  }, [])

  if (loading) {
    return (
    <SidebarWithHeader>
      <Spinner
        thickness='4px'
        speed='0.65s'
        emptyColor='gray.200'
        color='blue.500'
        size='xl'></Spinner>
    </SidebarWithHeader>)
  }

  if ((customers.length <= 0) || (err)){
    return (
      <SidebarWithHeader>
        <InsertCustomerDrawer
          fetchCustomers={fetchCustomers}
        ></InsertCustomerDrawer>
        <Text mt={5}>Empty</Text>
      </SidebarWithHeader>
    )
  }

  return (
    <SidebarWithHeader>
      <InsertCustomerDrawer
        fetchCustomers={fetchCustomers}
      ></InsertCustomerDrawer>
      <Wrap justify={"left"} spacing={"30px"}>
        {customers.map((customer, index) => (
          <WrapItem key={index}>
            <CardWithImage
              {...customer}
              ImageNumber={index}
              fetchCustomers={fetchCustomers}
            ></CardWithImage>
          </WrapItem>
        ))}
      </Wrap>
    </SidebarWithHeader>
  )
}

export default App;