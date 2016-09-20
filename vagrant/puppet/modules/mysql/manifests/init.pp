class mysql {
	package { ['mysql-client', 'mysql-server']:
		ensure => present,
		before => File['/etc/mysql/my.cnf'],
	}

	file { '/etc/mysql/my.cnf':
		ensure => file,
		owner => 'root',
		group => 'root',
		mode  => '0644',
		source => 'puppet:///modules/mysql/my.cnf';
	}

	exec { 'mysqladmin':
		unless => "/usr/bin/mysqladmin -uroot -ppassword status",
		command => '/usr/bin/mysqladmin -u root password password',
		require => [ Package['mysql-client'], Service['mysql'] ],
	} ->
	exec { 'mysqladmin127':
		command => '/bin/echo "SELECT @password := password from mysql.user where host=\"localhost\" and user=\"root\"; update mysql.user set password=@password where host=\"127.0.0.1\" and user=\"root\";" | /usr/bin/mysql -u root -ppassword; sudo service mysql restart',
		require => [ Package['mysql-client'], Service['mysql'] ],
	} ->
	file { '/tmp/base.sql':
		owner => 'root',
		group => 'root',
		mode  => '0777',
		source => 'puppet:///modules/mysql/to_import.sql'
	} ->
	exec { 'apply_patch':
		command => '/usr/bin/mysql -u root -ppassword < /tmp/base.sql',
		onlyif => '/usr/bin/test -z $(echo "show databases;" | mysql -uroot -ppassword | grep "study")',
	}

	service { 'mysql':
		ensure => running,
		require => [ Package['mysql-client'], Package['mysql-server'] ],
	}
}
