class slapd {
	package { ['slapd', 'ldap-utils']:
		ensure => present,
	} ~>
	file { 'default':
		name => '/etc/default/slapd',
		ensure => file,
		owner => 'root',
		group => 'root',
		mode => '0644',
		source => 'puppet:///modules/slapd/slapd.defaults',
		require => Package['slapd'];		
	}->
	file { 'conf':
		name => '/etc/ldap/slapd.conf',
		ensure => file,
		owner => 'openldap',
		group => 'openldap',
		mode => '0644',
		source => 'puppet:///modules/slapd/slapd.conf',
		require => Package['slapd'];
	}->
	file { 'DB_CONFIG':
		name => '/var/lib/ldap/DB_CONFIG',
		ensure => present,
		owner => 'openldap',
		group => 'openldap',
		mode => '0644',
		source => '/usr/share/slapd/DB_CONFIG',
		require => Package['slapd'],
	}->
	
	#these two files set up the LDAP groups
	file { '/home/vagrant/bareBase.ldif':
		owner => 'vagrant',
		group => 'vagrant',
		mode => '0777',
		source => 'puppet:///modules/slapd/bareBase.ldif',
		notify => Exec['bareBase'],
	}->
	file { '/home/vagrant/initGroup.ldif':
		owner => 'vagrant',
		group => 'vagrant',
		mode => '0777',
		source => 'puppet:///modules/slapd/initGroup.ldif',
		notify => Exec['initGroup'],
	}

	#applies the barebase ldif file to ldap if it has changed
	exec { 'bareBase':	
		command => '/usr/bin/ldapadd -x -D "cn=admin,dc=the-ark,dc=org,dc=au" -f /home/vagrant/bareBase.ldif -w password',
		require => [ Service['slapd'] ],
		refreshonly => true,
		onlyif => '/usr/bin/test -z "$(ldapsearch -w password -D \'cn=admin,dc=the-ark,dc=org,dc=au\' -b \'cn=arksuperuser@ark.org.au,ou=arkUsers,dc=the-ark,dc=org,dc=au\' | grep \'dn: cn=arksuperuser@ark.org.au,ou=arkUsers,dc=the-ark,dc=org,dc=au\')"'
	}

	#applies the initGroup ldif file to ldap if it has changed
	exec { 'initGroup':	
		command => '/usr/bin/ldapadd -x -D "cn=admin,dc=the-ark,dc=org,dc=au" -f /home/vagrant/initGroup.ldif -w password',
		require => [ Service['slapd'] ],
		refreshonly => true,
	}

	service { 'slapd':
		ensure => running,
		enable => true,
		hasrestart => true,
		hasstatus => true,
		require => [ File['default'], File['conf'], File['DB_CONFIG'] ],
		subscribe => [ File['default'], File['conf'], File['DB_CONFIG'] ],
	}
}
