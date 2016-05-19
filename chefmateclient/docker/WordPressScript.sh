# Create VM
(echo "createVM" 
	&& echo "wordpress" # instance name
	&& echo "scriptedTag" # instance tag
	&& echo "eu-central-1" # region
	&& echo "ami-87564feb" # image-id
	&& echo "ubuntu" # username
	&& echo "t2.micro" # flavor
	&& echo "sg-79ae5d11" # security group
	&& echo "shutdown" #s shutdown client
) | java -jar chefmateclient-1.0-jar-with-dependencies.jar -h $1 -p $2

# Init chef repo
(echo "initChefRepo" 
	&& echo "ubuntu" # username
	&& echo "TODO HOST??" # host
	&& echo "chefmateserver_key.pem" # private key name
	&& echo "10000" # timeout
	&& echo "shutdown" #s shutdown client
) | java -jar chefmateclient-1.0-jar-with-dependencies.jar -h $1 -p $2

# Deploy WordPress with default settings
(echo "deployWPApp" 
	&& echo "ubuntu" # username
	&& echo "TODO HOST??" # host
	&& echo "chefmateserver_key.pem" # private key name
	&& echo "10000" # ssh timeout
	&& echo "" # WP server name
	&& echo "" # WP port
	&& echo "" # WP database naem
	&& echo "" # WP database host (default: localhost)
	&& echo "" # WP database port
	&& echo "" # WP database username
	&& echo "" # WP database password
	&& echo "" # WP database root password
	&& echo "" # Additional WP configs (PHP key values)
	&& echo "shutdown" #s shutdown client
) | java -jar chefmateclient-1.0-jar-with-dependencies.jar -h $1 -p $2