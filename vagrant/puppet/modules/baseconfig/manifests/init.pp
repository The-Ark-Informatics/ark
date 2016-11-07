class baseconfig {
	exec { 'apt-get update':
		command => '/usr/bin/apt-get update';
	}->
	exec { 	'locale-gen':
		command => '/usr/sbin/locale-gen en_US.UTF-8',
	}
}
