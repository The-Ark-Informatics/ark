#create new stages, and set order
stage { 'pre': }

stage { 'post': }

Stage['pre'] -> Stage['main'] -> Stage['post']

#set which stages baseconfig, java8 and mount need to run in
class { 'baseconfig':
	stage => 'pre'
}

class { 'java8':
	stage => 'pre'
}

# set defaults for file ownership/permissions
File {
	owner => 'root',
	group => 'root',
	mode  => '0644',
}

include baseconfig, java8, mysql, slapd, tomcat, ark
