import {
    Box,
    CloseButton,
    Flex,
    useColorModeValue,
    Text,
    Image,
  } from '@chakra-ui/react'
  import{
    FiHome,
    FiUsers,
    FiSettings,
  } from 'react-icons/fi'
  import logo from '../../assets/logo-q.png';
  import NavItem from './NavItem';

const LinkItems = [
    { name: 'Home', icon: FiHome, href: '/' },
    { name: 'Customers', icon: FiUsers, href: '/dashboard/customers' },
    { name: 'Settings', icon: FiSettings, href: '#' },
  ]

const SidebarContent = ({ onClose, ...rest }) => {
    return (
      <Box
        transition="3s ease"
        bg={useColorModeValue('white', 'gray.900')}
        borderRight="1px"
        borderRightColor={useColorModeValue('gray.200', 'gray.700')}
        w={{ base: 'full', md: 60 }}
        pos="fixed"
        h="full"
        {...rest}>
        <Flex h="20" alignItems="center" mx="8"
            mb={75} mt={2} justifyContent="space-between">
            <Flex h="20" flexDirection="column" alignItems="center" mx="8"
                justifyContent="space-between">
            <Text fontSize="2xl" fontFamily="monospace" fontWeight="bold"
                mb={5}>
                Dashboard
            </Text>
            <Image
                borderRadius='full'
                boxSize='75px'
                src={logo}
                alt='q-logo'
                />
            </Flex>
            <CloseButton display={{ base: 'flex', md: 'none' }} onClick={onClose} />
        </Flex>
        {LinkItems.map((link) => (
          <NavItem key={link.name} icon={link.icon} link={link.href}> 
            {link.name}
          </NavItem>
        ))}
      </Box>
    )
  }

  export default SidebarContent