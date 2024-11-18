import React, {useState, useRef, useEffect} from 'react';
import {
  TableOutlined,
  FileSearchOutlined,
  PieChartOutlined,
  SettingOutlined,
  BellOutlined,
  SearchOutlined,
  CheckOutlined,
  CloseOutlined
} from '@ant-design/icons';
import type { MenuProps } from 'antd';
import { Typography, Layout, Menu, theme, Avatar,  Badge, Space, Dropdown, Switch  } from 'antd';
const { Title } = Typography;
import { ConfigProvider } from 'antd';
import ruRU from 'antd/locale/ru_RU';
import { TableFree } from './TableFree.tsx';

const { Header, Content, Footer, Sider } = Layout;

type MenuItem = Required<MenuProps>['items'][number];

function getItem(
  label: React.ReactNode,
  key: React.Key,
  icon?: React.ReactNode,
  children?: MenuItem[],
): MenuItem {
  return {
    key,
    icon,
    children,
    label,
  } as MenuItem;
}

const items: MenuItem[] = [
  getItem('Поиск заявок', '1', <FileSearchOutlined />),
  getItem('Заявки на ATI', '2', <TableOutlined />),
  getItem('Настройки', '3', <SettingOutlined />),
  getItem('Статистика', '4', <PieChartOutlined />),
];


const Main: React.FC = () => {
  const [collapsed, setCollapsed] = useState(false);
  const logoRef = useRef<HTMLHeadingElement>(null);
  const [startSearch, setStartSearch] = useState(false)
  const [loading, setLoading] = useState(false)
  const {
    token: { colorBgContainer, borderRadiusLG },
  } = theme.useToken();

  const searchStartStop = async () => {
      if (startSearch) {
        await fetch('/scheduler/start/searchOrders', {method: 'GET'})
            .then(res => res.text())
            .then(resJson => {
              setLoading(false)
              console.log(resJson)
            }).catch(eeee => console.log(eeee))
        await fetch('/scheduler/start/ws', {method: 'GET'})
            .then(res => res.text())
            .then(resJson => {
              console.log(resJson)
            }).catch(eeee => console.log(eeee))

      } else {
        await fetch('/scheduler/stop/searchOrders', {method: 'GET'})
            .then(res => res.text())
            .then(resJson => {
              setLoading(false)
              console.log(resJson)
            }).catch(eeee => console.log(eeee))
        await fetch('/scheduler/stop/ws', {method: 'GET'})
            .then(res => res.text())
            .then(resJson => {
              console.log(resJson)
            }).catch(eeee => console.log(eeee))
      }
  }
  const changeSearch = async () => {
    await fetch('/scheduler/list', {method: 'GET'})
        .then(res => res.text())
        .then(resJson => {
          if (resJson === "Нет запущенных планировщиков") setStartSearch(false)
          else setStartSearch(true)
          console.log(resJson)
        }).catch(eeee => console.log(eeee))
  }
  useEffect(() => {
   changeSearch().then(r => console.log(r));
      const interval = setInterval(() => {
          changeSearch().then(r => console.log(r));
      }, 30000);

      //Clearing the interval
      return () => clearInterval(interval);
  }, []);

  useEffect(() => {
    searchStartStop().then(r => console.log(r));
  }, [startSearch]);
  return (
    <ConfigProvider locale={ruRU}>
    <Layout style={{ minHeight: '100vh' }}>
      <Sider collapsible collapsed={collapsed} onCollapse={(value) => {
          setCollapsed(value)
          if (value && !logoRef.current?.classList.contains("span-logo-hidden")){
            if (logoRef.current?.classList.contains("span-logo-visible")){
              logoRef.current?.classList.remove("span-logo-visible")
            }
            logoRef.current?.classList.add("span-logo-hidden")
          }
          if (!value && !logoRef.current?.classList.contains("span-logo-visible")){
            if (logoRef.current?.classList.contains("span-logo-hidden")){
              logoRef.current?.classList.remove("span-logo-hidden")
            }
            logoRef.current?.classList.add("span-logo-visible")
          }          
          console.log(logoRef.current?.className, value)
     
        }}>
        <div className="demo-logo-vertical" >
          <SearchOutlined style={{fontSize: '28px', marginRight: 10, marginLeft: 10}}/>
          <Title ref={logoRef} className="span-logo" style={{margin: 0, padding:0, color: 'rgba(255, 255, 255, 0.65)', position: 'absolute', left: 55}} level={3}>ТН Поиск</Title>
        </div>
        <Menu theme="dark" defaultSelectedKeys={['1']} mode="inline" items={items} />
      </Sider>
      <Layout>
        <Header style={{ padding: 0, background: colorBgContainer }} >
          <div className='nav-bar'>
            <div className='nav-logo'>
              <div style={{width: 50}}></div>
            </div>
            <ul className='nav-bar-ul'>
              <li>
              <Dropdown menu={{ items }} trigger={['click']}>
                <Space>
                  <Badge count={1}>
                    <Avatar shape="square" icon={<BellOutlined />} />
                  </Badge>
                </Space>
              </Dropdown>
              </li>
              <div className='dd-block'></div>
              <li className='nav-li-user'>
                <Title level={5} style={{width:120, height:32, margin: "8px 0px 0px 0px", padding:0}}>Кудряшова А.</Title>
                <Avatar style={{ backgroundColor: '#f56a00' }} size={36}>КА</Avatar>
              </li>
              <li style={{width:15}}></li>
            </ul>
          </div>
        </Header>
        <Content style={{ margin: '0 16px' }}>
          <div style={{display: 'flex', justifyContent: 'space-between', alignItems: 'center'}}>
            <Title level={2} style={{margin: 16}}>Таблица поиска заявок</Title>
            <Switch
                checkedChildren={<CheckOutlined />}
                unCheckedChildren={<CloseOutlined />}
                checked={startSearch}
                onChange={() => {
                  setStartSearch(!startSearch)
                  setLoading(true)
                }}
                loading={loading}
            />
          </div>
          <div
            style={{
              padding: 24,
              minHeight: 360,
              background: colorBgContainer,
              borderRadius: borderRadiusLG,
            }}
          >
            <TableFree />
          </div>
        </Content>
        <Footer style={{ textAlign: 'center' }}>
          TH Search ©{new Date().getFullYear()} Created by THSearch UED
        </Footer>
      </Layout>
    </Layout>
    </ConfigProvider>
  );
};

export default Main;