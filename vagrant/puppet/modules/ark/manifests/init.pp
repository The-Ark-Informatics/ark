class ark {
	package { ['maven']:
		ensure => present,
	}

	file { 'applicationContext':
		name => '/home/vagrant/ark/ark-container/src/main/resources/applicationContext.xml',
		source => 'puppet:///modules/ark/applicationContext.xml',
		owner => 'vagrant',
		group => 'vagrant',
		mode => '0644',
	}->
	file { 'applicationProperties':
		name => '/home/vagrant/ark/ark-container/src/main/resources/application.properties',
		source => 'puppet:///modules/ark/application.properties',
		owner => 'vagrant',
		group => 'vagrant',
		mode => '0644',
	}->
	file { 'ark-datastore':
		ensure => directory,
		name => '/opt/ark-datastore',
		owner => 'tomcat7',
		group => 'tomcat7',
	}->
	exec { 'initMvn':
		cwd => '/home/vagrant/ark/',
		command => '/usr/bin/mvn initialize',
		timeout => 0,
		require => Package['maven'],
	}->
	exec { 'buildArk':
		cwd => '/home/vagrant/ark/',
		command => '/usr/bin/mvn clean package -T1C',
		timeout => 0,
		require => [ Package['maven'], Exec['initMvn']],
	}->
#	file { 'ark.war':
#		name => '/var/lib/tomcat7/webapps/ark.war',
#		source => '/home/vagrant/ark/ark-container/target/ark.war',
#		owner => 'tomcat7',
#		group => 'tomcat7',
#		notify => [ Service['tomcat7'] ],
#	}->
	exec { 'ark.war.mv':
		cwd => '/home/vagrant/ark/',
		command => '/bin/mv /home/vagrant/ark/ark-container/target/ark.war /var/lib/tomcat7/webapps/ark.war',
		timeout => 0,
	}
	
	file { 'useraccount':
		name => '/home/vagrant/ark/ark-user-account/src/main/resources/applicationContext.xml',
		source => 'puppet:///modules/ark/user-account',
		owner => 'vagrant',
		group => 'vagrant',
		mode => '0644',
		notify => Exec['makeUserJar'],
		ensure => present,
	}
	file { 'insertDone':
		name => '/home/vagrant/.insertSuperUserDone',
		ensure => present,
	}

	exec { 'makeUserJar':
		cwd => '/home/vagrant/ark/ark-user-account',
		command => '/usr/bin/mvn assembly:assembly',
		timeout => 0,
		refreshonly => true,
		require => [ Package['maven'] ],
		before => [ File['insertDone'] ],
		onlyif => '/usr/bin/test -z "$(ldapsearch -w password -D \'cn=admin,dc=the-ark,dc=org,dc=au\' -b \'cn=arksuperuser@ark.org.au,ou=arkUsers,dc=the-ark,dc=org,dc=au\' | grep \'dn: cn=arksuperuser@ark.org.au,ou=arkUsers,dc=the-ark,dc=org,dc=au\')"'
	}->
	exec { 'insertUser':
		cwd => '/home/vagrant/ark/ark-user-account',
		command => '/usr/bin/java -jar target/ark-user-account-1.0.0-jar-with-dependencies.jar arksuperuser@ark.org.au password Super User',
		timeout => 0,
		onlyif => '/usr/bin/test -z "$(ldapsearch -w password -D \'cn=admin,dc=the-ark,dc=org,dc=au\' -b \'cn=arksuperuser@ark.org.au,ou=arkUsers,dc=the-ark,dc=org,dc=au\' | grep \'dn: cn=arksuperuser@ark.org.au,ou=arkUsers,dc=the-ark,dc=org,dc=au\')"'
	}

	### Madeline
	package { [ 'build-essential', 'libxml2', 'libxml2-dev', 'libcurl3', 'libcurl4-gnutls-dev', 'zlib1g', 'zlib1g-dev', 'libbz2-1.0', 'libbz2-dev', 'libssl-dev' ]:
		ensure => present,
	} ~>
	file { 'MadelineMakefile':
		name => '/home/vagrant/ark/ark-common/src/main/native/madeline/Makefile',
		source => 'puppet:///modules/ark/Makefile',
		owner => 'vagrant',
		group => 'vagrant',
		mode => '0644',
	} ->	
	exec { 'makeMadeline':
		command => '/usr/bin/make distclean; /usr/bin/make',
		cwd => '/home/vagrant/ark/ark-common/src/main/native/madeline',
	} ->
	file { '/usr/share/tomcat7/lib/libmadeline.so':
		source => '/home/vagrant/ark/ark-common/src/main/native/madeline/libmadeline.so',
		owner => 'root',
		group => 'root',
		mode => '0644',
		require => Package['tomcat7'],		
	}	

}
