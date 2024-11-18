import React, { useEffect, useRef, useState } from 'react';
import {TableProps,  InputRef, TableColumnType, AutoComplete} from 'antd';
import type { AutoCompleteProps } from 'antd';
import type { FilterDropdownProps } from 'antd/es/table/interface';
import ky from 'ky';
import dayjs from 'dayjs';
import { CheckOutlined, CloseOutlined, InfoCircleOutlined, PlusOutlined, SearchOutlined, EditOutlined, SaveOutlined, CloseCircleOutlined} from '@ant-design/icons';
import {DatePicker, Checkbox, Form, Input, InputNumber, Popconfirm, Table, Button, Tooltip,  Space } from 'antd';
import Highlighter from 'react-highlight-words';

type TableRowSelection<T extends object = object> = TableProps<T>['rowSelection'];

interface Cardtransport {
    id: number;
    placeofloading: string;
    placeofloadingfias: string;
    placeofdelivery: string;
    onplaceofdelivery: string;
    shipmentstart: number;
    onnum: string;
    chk_apply: boolean;
    chk_search: boolean;
    send_ati: boolean;
    continuingsearch: boolean;
}

interface AutoCompleteList{
    label: string;
    value: string;
}


const originData: Cardtransport[] = [];


interface EditableCellProps extends React.HTMLAttributes<HTMLElement> {
    editing: boolean;
    dataIndex: string;
    title: any;
    inputType: 'number' | 'text' | 'date' | 'boolean';
    record: Cardtransport;
    index: number;
}

const EditableCell: React.FC<React.PropsWithChildren<EditableCellProps>> =
    ({
         editing,
         dataIndex,
         title,
         inputType,
         record,
         index,
         children,
         ...restProps
    }) => {
        const [options, setOptions] = useState<AutoCompleteProps['options']>([]);

        const rq = async function (val: string) {
            const jsonValues: AutoCompleteList[] = await ky.get("/api/ctc/fias/" + encodeURIComponent(val)).json()
            setOptions(() => {
                if (val.length < 3) {
                  return [];
                }
                return jsonValues.map(m => ({
                    label: m.label,
                    value: m.label,
                    fias: m.value
                }));
              });
            
        }
        const handleSearch = (value: string) => {
            rq(value);
          };
        const onSelect = (data: string) => {
            const fis = options?.filter(f => f.value === data).map(m =>m.fias)
            if (fis !== null && fis !== undefined && fis.length > 0) {
                record.placeofloadingfias = fis[0];
            }
            
          };
        
    const inputNode = inputType === 'number' ? 
        <InputNumber /> : dataIndex !== "placeofloading" ?
        <Input onChange={(event) => {
            if (dataIndex !== "placeofloading") return
        }}/> : 
            <AutoComplete
                popupMatchSelectWidth={500}
                options={options}
                onSelect={onSelect}
                onSearch={handleSearch}
      />;

    return (
        <td {...restProps}>
            {editing ? (
                inputType === 'boolean' ?
                <Form.Item name={dataIndex} style={{ margin: 0 }} valuePropName='checked' >
                    {<Checkbox defaultChecked={record.chk_apply}/> }
                </Form.Item> :
                    inputType === 'date' ?
                    <Form.Item name={dataIndex} style={{ margin: 0 }} >
                        {<DatePicker format='DD.MM.YYYY HH:mm:ss' showTime={{ defaultValue: dayjs('00:00:00', 'HH:mm:ss') }}/> }
                    </Form.Item> :
                    <Form.Item
                        name={dataIndex}
                        style={{ margin: 0, fontSize: '8px' }}
                        rules={[
                            {
                                required: dataIndex === "placeofloading",
                                message: 'Введите значения',
                            },
                        ]}
               
                    >
                        {inputNode }
                    </Form.Item> 
            ) : (
                children
            )}
        </td>
    );
};




const TableFree: React.FC = () => {
    const [form] = Form.useForm();
    const [data, setData] = useState(originData);
    const [editingKey, setEditingKey] = useState('');

    const getData = async () => {
        const list: Cardtransport[] = await ky.get("/api/ctc/list").json()
        setData(list)
    }
    
    useEffect(() => {
        getData();
        const interval = setInterval(() => {
            getData();
        }, 30000);

        //Clearing the interval
        return () => clearInterval(interval);

    },[])
    

    const isEditing = (record: Cardtransport) => String(record.id) === editingKey;

    const edit = (record: Partial<Cardtransport> & { id: React.Key }) => {
        
        const {id, placeofloading, placeofloadingfias, placeofdelivery, onplaceofdelivery, shipmentstart, onnum, chk_apply, chk_search, send_ati, continuingsearch } = record;
        
        form.setFieldsValue({ id, placeofloading: placeofloading, placeofloadingfias: placeofloadingfias, placeofdelivery: placeofdelivery, onplaceofdelivery: onplaceofdelivery, shipmentstart: dayjs(shipmentstart), onnum: onnum, chk_apply: chk_apply, chk_search: chk_search, send_ati: send_ati, continuingsearch: continuingsearch });
        setEditingKey(String(record.id));

        console.log(editingKey, record)
    };
    const cancelPag = () => {
        setEditingKey('');
    };
    const cancel = (record: Cardtransport) => {
        setEditingKey('');
        // setData(data.filter(f => f.id !== record.id))
        setChangeNewCard(false)
    };
    const addCard = async(card: Cardtransport) => {
        card.shipmentstart = new Date(card.shipmentstart).getTime()
        if (changeNewCard) {
            delete card.id;
            setChangeNewCard(false)
        }
        try{
            console.log('add', card)
            await fetch('/api/ctc/add', {
                method: 'POST',
                body: JSON.stringify(card),
                headers: {
                  'content-type': 'application/json'
                },
                mode: 'cors'
              });

        } catch (eeee){
            console.log(eeee)
        }
        
        
    }
    const deleteCard = async(id: React.Key[]) => {
        console.log(JSON.stringify(id))

        try{
            await fetch('/api/ctc/delete', {
                method: 'POST',
                body: JSON.stringify(id),
                headers: {
                    'content-type': 'application/json'
                },
                mode: 'cors'
            });

        } catch (eeee){
            console.log(eeee)
        }


    }

    const [changeNewCard, setChangeNewCard] = useState(false);
    const save = async (id: React.Key) => {
        try {
            
            const row = (await form.validateFields()) as Cardtransport;
            
            const newData = [...data];
            const index = newData.findIndex((item) => id === item.id);
            if (index > -1) {
                const item = newData[index];
                newData.splice(index, 1, {
                    ...item,
                    ...row,
                });
                setData(newData);
                setEditingKey('');
            } else {
                newData.push(row);
                setData(newData);
                setEditingKey('');
            }
            await addCard(newData[index])

        } catch (errInfo) {
            console.log('Valkeyate Failed:', errInfo);
        }
    };
    const add = () => {
        let i = Math.max(...data.map(m => m.id))
        i = data.length > 0 ? i + 1 : 1
        setChangeNewCard(true)
        const newData: Cardtransport = {
            id: i,
            placeofloading: "",
            placeofloadingfias: "",
            placeofdelivery: "",
            onplaceofdelivery: "",
            shipmentstart: new Date().getTime(),
            onnum: "",
            chk_apply: false,
            chk_search: false,
            send_ati: false,
            continuingsearch: false

        }
        setData([newData, ...data])
        edit(newData)           
      };

    const [checkedSend_ati, setCheckedSend_ati] = useState(false)  
    const [checkedChk_apply, setCheckedChk_apply] = useState(false)  
    const [checkedChk_search, setCheckedChk_search] = useState(false)  
    const [checkedContinuingsearch, setCheckedContinuingsearch] = useState(false)  
    
    const changeSend_ati = () => {
        setData(prevDate => prevDate.map(m => {
            m.send_ati = !checkedSend_ati
            return m;
        }))
        setCheckedSend_ati(!checkedSend_ati)
    } 
    const changeChk_apply = () => {
        setData(prevDate => prevDate.map(m => {
            m.chk_apply = !checkedChk_apply
            return m;
        }))
        setCheckedChk_apply(!checkedChk_apply)
    } 
    const changeChk_search = () => {
        setData(prevDate => prevDate.map(m => {
            m.chk_search = !checkedChk_search
            return m;
        }))
        setCheckedChk_search(!checkedChk_search)
    } 
    const changeContinuingsearch = () => {
        setData(prevDate => prevDate.map(m => {
            m.continuingsearch = !checkedContinuingsearch
            return m;
        }))
        setCheckedContinuingsearch(!checkedContinuingsearch)
    } 

    type DataIndex = keyof Cardtransport
    const [searchText, setSearchText] = useState('');
    const [searchedColumn, setSearchedColumn] = useState('');
    const searchInput = useRef<InputRef>(null);
  
    const handleSearch = (
      selectedKeys: string[],
      confirm: FilterDropdownProps['confirm'],
      dataIndex: DataIndex,
    ) => {
      confirm();
      setSearchText(selectedKeys[0]);
      setSearchedColumn(dataIndex);
    };
  
    const handleReset = (clearFilters: () => void) => {
      clearFilters();
      setSearchText('');
    };

    const getColumnSearchProps = (dataIndex: DataIndex): TableColumnType<Cardtransport> => ({
        filterDropdown: ({ setSelectedKeys, selectedKeys, confirm, clearFilters, close }) => (
          <div style={{ padding: 8 }} onKeyDown={(e) => e.stopPropagation()}>
            <Input
              ref={searchInput}
              placeholder={'Поиск'}
              value={selectedKeys[0]}
              onChange={(e) => setSelectedKeys(e.target.value ? [e.target.value] : [])}
              onPressEnter={() => handleSearch(selectedKeys as string[], confirm, dataIndex)}
              style={{ marginBottom: 8, display: 'block' }}
            />
            <Space>
              <Button
                type="primary"
                onClick={() => handleSearch(selectedKeys as string[], confirm, dataIndex)}
                icon={<SearchOutlined />}
                size="small"
                style={{ width: 90 }}
              >
                Поиск
              </Button>
              <Button
                size="small"
                onClick={() => {
                clearFilters && handleReset(clearFilters)
                  confirm({ closeDropdown: false });
                  setSearchText((selectedKeys as string[])[0]);
                  setSearchedColumn(dataIndex);
                }}
              >
                Сброс
              </Button>
              <Button
                type="link"
                size="small"
                onClick={() => {
                  close();
                }}
              >
                Закрыть
              </Button>
            </Space>
          </div>
        ),
        filterIcon: (filtered: boolean) => (
          <SearchOutlined style={{ color: filtered ? '#1677ff' : undefined }} />
        ),
        onFilter: (value, record) =>
          record[dataIndex]
            .toString()
            .toLowerCase()
            .includes((value as string).toLowerCase()),
        onFilterDropdownOpenChange: (visible) => {
          if (visible) {
            setTimeout(() => searchInput.current?.select(), 100);
          }
        },
        render: (text) =>
          searchedColumn === dataIndex ? (
            <Highlighter
              highlightStyle={{ backgroundColor: '#ffc069', padding: 0 }}
              searchWords={[searchText]}
              autoEscape
              textToHighlight={text ? text.toString() : ''}
            />
          ) : (
            text
          ),
      });
    const columns = [
        {
            title: 'Номер',
            dataIndex: 'id',
            width: '50px',
            editable: false,
            defaultSortOrder: 'ascend',
            sorter: (a, b) => a.id - b.id,
        },
        {
            title: 'Место погрузки',
            dataIndex: 'placeofloading',
            width: '150px',
            editable: true,
            defaultSortOrder: 'ascend',
            ...getColumnSearchProps('placeofloading'),
            sorter: (a, b) => a.placeofloading - b.placeofloading,
        },
        {
            title: 'ФИАС',
            dataIndex: 'placeofloadingfias',
            width: '100px',
            editable: false,
        },
        {
            title: 'Место доставки',
            dataIndex: 'placeofdelivery',
            width: '150px',
            editable: true,
            ...getColumnSearchProps('placeofdelivery'),
        },
        {
            title: 'Искл. место д-ки',
            dataIndex: 'onplaceofdelivery',
            width: '130px',
            editable: true,
            ...getColumnSearchProps('onplaceofdelivery'),
        },
        {
            title: 'Отгрузка',
            dataIndex: 'shipmentstart',
            render: (value) => { 
                const date = new Date(value)
                return date.toLocaleDateString() + " " + date.toLocaleTimeString()
            }, 
            width: '150px',
            editable: true,
            defaultSortOrder: 'ascend',
            sorter: (a, b) => a.shipmentstart - b.shipmentstart,
        },
        {
            title: 'Искл. внеш.№',
            dataIndex: 'onnum',
            width: '120px',
            editable: true,
            ...getColumnSearchProps('onnum'),
        },

        {
            title: <div style={{display: 'flex', alignItems: 'center', justifyContent: 'center'}}>
                <Popconfirm title={`Вы действительно хотите ${!checkedChk_apply ? 'брать' : 'не брать'} все заявки?`} onConfirm={changeChk_apply}>
                    <Checkbox checked={checkedChk_apply} style={{fontSize: '12px'}}>Взять</Checkbox> 
                </Popconfirm>
                <Tooltip placement='bottom' title='Взять заявку' >
                    <InfoCircleOutlined />
                </Tooltip>
            </div>,
            dataIndex: 'chk_apply',
            render: (_: any, record: Cardtransport) => ( record.chk_apply ? <CheckOutlined /> : <CloseOutlined />),
            width: '122px',
            align: 'center',
            editable: true,
        },

        {
            title: <div style={{display: 'flex', alignItems: 'center', justifyContent: 'center'}}>
                <Popconfirm title={`Вы действительно хотите ${!checkedChk_search ? 'поставить в поиск' : 'убрать из поиска'} все заявки?`} onConfirm={changeChk_search}>
                    <Checkbox checked={checkedChk_search} style={{fontSize: '12px'}}>В поиск</Checkbox> 
                </Popconfirm>
                <Tooltip placement='bottom' title='Поставить заявку в поиск' >
                    <InfoCircleOutlined />
                </Tooltip>
            </div>,
            dataIndex: 'chk_search',
            render: (_: any, record: Cardtransport) => ( record.chk_search ? <CheckOutlined /> : <CloseOutlined />),
            width: '130px',
            align: 'center',
            editable: true,
        },
        {
            title: <div style={{display: 'flex', alignItems: 'center', justifyContent: 'center'}}>
                <Popconfirm title={`Вы действительно хотите ${!checkedSend_ati ? 'оправлять на ATI' : 'не отправлять на ATI'} все заявки?`} onConfirm={changeSend_ati}>
                    <Checkbox checked={checkedSend_ati} style={{fontSize: '12px'}}>На ATI</Checkbox> 
                </Popconfirm>
                <Tooltip placement='bottom' title='Отправить на ATI' >
                    <InfoCircleOutlined />
                </Tooltip>
            </div>,
            dataIndex: 'send_ati',
            render: (_: any, record: Cardtransport) => ( record.send_ati ? <CheckOutlined /> : <CloseOutlined />),
            width: '122px',
            align: 'center',
            editable: true,
        },
        {
            title: <div style={{display: 'flex', alignItems: 'center', justifyContent: 'center'}}>
                <Popconfirm title={`Вы действительно хотите ${!checkedContinuingsearch ? 'искать непрерывно все заявки' : 'искать заявки по одному разу'}?`} onConfirm={changeContinuingsearch}>
                    <Checkbox checked={checkedContinuingsearch} style={{fontSize: '12px'}}>	&infin; поиск</Checkbox> 
                </Popconfirm>
                <Tooltip placement='bottom' title='Непрерывный поиск' >
                    <InfoCircleOutlined />
                </Tooltip>
            </div>,
            dataIndex: 'continuingsearch',
            render: (_: any, record: Cardtransport) => ( record.continuingsearch ? <CheckOutlined /> : <CloseOutlined />),
            width: '125px',
            align: 'center',
            editable: true,
        },
        {
            title: 'Действия',
            dataIndex: 'operation',
            width: '100px',
            render: (_: any, record: Cardtransport) => {
                const editable = isEditing(record);
                return editable ? (
                    <span>

                        
            <SaveOutlined onClick={() => save(record.id)} style={{fontSize: "20px", textAlign: "center"}} />

            <Popconfirm title="Вы хотите отменить изменения?" onConfirm={() => cancel(record)}>
                <CloseCircleOutlined style={{fontSize: "20px", textAlign: "center", marginLeft: 10}} />
            </Popconfirm>
          </span>
                ) : (

                     <EditOutlined style={{fontSize: "20px", textAlign: "center"}} disabled={editingKey !== ''} onClick={() => edit(record)}/>

                );
            },
        },
    ];

    const mergedColumns: TableProps<Cardtransport>['columns'] = columns.map((col) => {
        if (!col.editable) {
            return col;
        }
        return {
            ...col,
            onCell: (record: Cardtransport) => ({
                record,
                inputType: col.dataIndex === 'id' ? 'number' : col.dataIndex === 'shipmentstart' ? 'date' : ['chk_apply', 'chk_search', 'send_ati', 'continuingsearch'].includes(col.dataIndex) ? 'boolean' : 'text',
                dataIndex: col.dataIndex,
                title: col.title,
                editing: isEditing(record),
            }),
        };
    });
    const [selectedRowKeys, setSelectedRowKeys] = useState<React.Key[]>([]);
    const onSelectChange = (newSelectedRowKeys: React.Key[]) => {
        console.log('selectedRowKeys changed: ', newSelectedRowKeys);
        setSelectedRowKeys(newSelectedRowKeys);
      };

  const rowSelection: TableRowSelection<Cardtransport> = {
    selectedRowKeys,
    onChange: onSelectChange,
    selections: [
      Table.SELECTION_ALL,
      Table.SELECTION_INVERT,
      {
        key: 'copy',
        text: 'Копировать выделенное',
        onSelect: (changeableRowKeys) => {
            console.log(selectedRowKeys,changeableRowKeys )
            let count = 1; 

            setData([...data, ...data.filter(f => selectedRowKeys.includes(f.id)).map(m => {
                m.id = data.length + count;
                count++;
                return m;
            })])
        },
      },
      {
        key: 'delete',
        text: 'Удалить выделенное',
        onSelect: (changeableRowKeys) => {
            console.log("delete", selectedRowKeys, changeableRowKeys)
            deleteCard(selectedRowKeys)
            setData(data.filter(f => !selectedRowKeys.includes(f.id)))
        },
      },
    ],
  };
    return (
        <div style={{minHeight: '540px'}}>
            <Button onClick={() => add()} style={{width: '100%', marginBottom: 5}}> <PlusOutlined /> Добавить запись</Button>
            <Form form={form} component={false}>
                <Table
                    components={{
                        body: {
                            cell: EditableCell,
                        },
                    }}
                    rowKey="id"
                    scroll={{ x: 1480 }}
                    bordered
                    dataSource={data}
                    columns={mergedColumns}
                    rowClassName="editable-row"
                    rowSelection={{
                        type: 'checkbox',
                        ...rowSelection
                    }}

                    size="small"
                    pagination={{
                        onChange:  cancelPag,
                    }}
                />
            </Form>
        </div>
    );
};
export { TableFree };