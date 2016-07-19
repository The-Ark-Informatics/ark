#!/bin/sh

sed "s/##ARK_USERNAME##/${ARK_USERNAME}/g" /docker-entrypoint-initdb.d/data_to_import.sql.example |
	mysql --protocol=socket -uroot -p${MYSQL_ROOT_PASSWORD}
