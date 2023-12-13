import { Wrap, WrapItem, Spinner, Text } from '@chakra-ui/react'
import SidebarWithHeader from './components/shared/Sidebar';
import { useEffect, useState } from 'react';
import { getCustomers } from './services/client';
import CardWithImage from './components/CardWithImage';

const App = () => {

  const [customers, setCustomers] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(function() {
    setLoading(true);
    setTimeout( () => {
      getCustomers().then(res => {
        //console.log(res);
        setCustomers(res.data);
      }).catch(err => {
        console.log(err);
      }).finally(() => {
        setLoading(false);
      })
    }, 500)
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

  if (customers.length <= 0){
    return (
      <SidebarWithHeader>
        <Text>Empty</Text>
      </SidebarWithHeader>
    )
  }

  return (
    <SidebarWithHeader>
      <Wrap justify={"left"} spacing={"30px"}>
        {customers.map((customer, index) => (
          <WrapItem key={index}>
            <CardWithImage
              {...customer}
              ImageNumber={index}
            ></CardWithImage>
          </WrapItem>
        ))}
      </Wrap>
    </SidebarWithHeader>
  )
}

export default App;