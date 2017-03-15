#!/bin/sh

sed "s/##ARK_USERNAME##/${ARK_USERNAME}/g" /docker-entrypoint-initdb.d/data_to_import.sql.example | \
	mysql --protocol=socket -uroot -p${MYSQL_ROOT_PASSWORD}

for schema in $(mysql --protocol=socket -uroot -p${MYSQL_ROOT_PASSWORD} -s -r -e "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME != \"INFORMATION_SCHEMA\" AND SCHEMA_NAME != \"MYSQL\" AND SCHEMA_NAME != \"PERFORMANCE_SCHEMA\" AND SCHEMA_NAME != \"SYS\";" | tail -n +3); do
	echo "GRANT ALL PRIVILEGES ON ${schema}.* TO '${MYSQL_USER}'@'%';" |
	mysql --protocol=socket -uroot -p${MYSQL_ROOT_PASSWORD}
done
