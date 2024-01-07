import { Wrap } from '@chakra-ui/react'
import SidebarWithHeader from './components/shared/Sidebar';

const Home = () => {
  return (
    <SidebarWithHeader>
      <Wrap justify={"left"} spacing={"30px"}>
        It's working, just press something from the left menu.
      </Wrap>
    </SidebarWithHeader>
  )
}

export default Home;