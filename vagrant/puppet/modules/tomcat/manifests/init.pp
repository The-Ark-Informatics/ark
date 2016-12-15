class tomcat {
	package { ['tomcat7', 'authbind']:
		ensure => present,
	}

	service { 'tomcat7':
		ensure => running,
		enable => true,
		hasrestart => true,
		hasstatus => true,
		require => [ File['tomcat_default'], File['tomcat_init'] ],
		subscribe => [ File['tomcat_default'], File['tomcat_init'] ],
	}

	file {'tomcat_default':
		name => '/etc/default/tomcat7',
		ensure => file,
		owner => 'root',
		group => 'root',
		mode => '0644',
		source => 'puppet:///modules/tomcat/tomcat7.default',
		require => Package['tomcat7'];		
	}
	
	file { 'tomcat_init':
		name => '/etc/init.d/tomcat7',
		ensure => 'file',
		owner => 'root',
		group => 'root',
		mode => '0755',
		source => 'puppet:///modules/tomcat/init.d',
		require => Package['tomcat7']
	}
}
