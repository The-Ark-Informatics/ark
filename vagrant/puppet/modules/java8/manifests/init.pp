class java8 {
	# This follows the recipe from http://blog.nocturne.net.nz/devops/2013/08/14/provisioning-oracle-java-with-puppet-apply/
	# adapted with the instructions from http://www.webupd8.org/2014/03/how-to-install-oracle-java-8-in-debian.html

	# define a variable for the webupd8team ppa sources list
	$webupd8src = '/etc/apt/sources.list.d/webupd8team.list'

	exec {'hello':
		unless => "/bin/true",
		command => '/bin/echo hello world',
	}

	# Configure ppa
	file { $webupd8src:
		content => "deb http://ppa.launchpad.net/webupd8team/java/ubuntu trusty main\ndeb-src http://ppa.launchpad.net/webupd8team/java/ubuntu trusty main\n",
	} ->
	# Authorise the webupd8 ppa
	# At the time of writing this key was correct, but check the PPA page on launchpad!
	# https://launchpad.net/~webupd8team/+archive/java
	exec { 'add-webupd8-key':
		unless => "apt-key list | grep -c EEA14886 2>/dev/null",
	       	command => 'apt-key adv --keyserver keyserver.ubuntu.com --recv-keys EEA14886',
		path => ['/usr/bin/','/bin/'],
	} ->
	# update the apt keystore
	exec { 'apt-key-update':
		unless => "update-alternatives --list java|grep java-8-oracle 2>/dev/null",
		command => 'apt-key update',
		path => ['/usr/bin/','/bin/'],
	} ->
	# update apt sources
	exec { 'apt-update':
		unless => "update-alternatives --list java|grep java-8-oracle 2>/dev/null",
		command => 'apt-get update',
		path => ['/usr/bin/','/bin/'],
	} ->
	# set license acceptance with debconf
	exec { 'accept-java-license':
		unless => "update-alternatives --list java|grep java-8-oracle 2>/dev/null",
	       	command => 'echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | sudo /usr/bin/debconf-set-selections',
		path => ['/usr/bin/','/bin/'],
	} ~>
	# finally install the package
	# oracle-java6-installer and oracle-java7-installer also available from the ppa
	package { 'oracle-java8-installer':
		ensure => present,
	} ~>
	file { "/etc/environment":
		source => "puppet:///modules/java8/environment",    	
	}	
}
