Usage during development: (automated process used otherwise)

mysql -uroot -ppassu < create_databases.sql


mysql -uroot -ppassu -Diotdevicemanager < create_tables_triggers_user.sql

mysql -uroot -ppassu -Diotdevicemanager < procedures_configuration.sql

mysql -uroot -ppassu -Diotdevicemanager < procedures_device_group.sql

mysql -uroot -ppassu -Diotdevicemanager < procedures_device_icon.sql

mysql -uroot -ppassu -Diotdevicemanager < procedures_device.sql

mysql -uroot -ppassu -Diotdevicemanager < procedures_device_type.sql

mysql -uroot -ppassu -Diotdevicemanager < procedures_location.sql


mysql -uroot -ppassu -Dtestiotdevicemanager < create_tables_triggers_user.sql

mysql -uroot -ppassu -Dtestiotdevicemanager < procedures_configuration.sql

mysql -uroot -ppassu -Dtestiotdevicemanager < procedures_device_group.sql

mysql -uroot -ppassu -Dtestiotdevicemanager < procedures_device_icon.sql

mysql -uroot -ppassu -Dtestiotdevicemanager < procedures_device.sql

mysql -uroot -ppassu -Dtestiotdevicemanager < procedures_device_type.sql

mysql -uroot -ppassu -Dtestiotdevicemanager < procedures_location.sql

mysql -uroot -ppassu -Dtestiotdevicemanager < mockdata.sql
