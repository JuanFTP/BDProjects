create database agua;
	use agua;
	create table manejador(
		login varchar(10) unique primary key,
		password varchar(20),
		nombreM varchar(30),
		apellidos varchar(50),
		direccion varchar(100),
		permisos varchar(20),
		fecha date
		);
	
	create table usuario(
		id int primary key auto_increment,
		nombre varchar(30),
		apellidos varchar(50),
		direccion varchar(100),
		telefono varchar(10) unique,
		login varchar(10),
		foreign key(login) references manejador(login),
		fecha date
		);
	
	create table contrato(
		folio int(10) primary key auto_increment,
		fecha date,
		descripcion varchar(200),
		tipoContrato varchar(50),
		estado varchar(50),
		id int,
		foreign key(id) references usuario(id),
		login varchar(10),
		foreign key(login) references manejador(login),
		fechaM date
		);
	
	create table pago(
		folio int(10) primary key auto_increment,
		descripcion varchar(100),
		importe int(10),
		fechaP date,
		folioC int,
		foreign key(folioC) references contrato(folio),
		id int,
		foreign key(id) references usuario(id),
		login varchar(10),
		foreign key(login) references manejador(login)
		);
	
	create table ingreso(
		id int primary key not null auto_increment,
		fecha date,
		descripcion varchar(200),
		importe double,
		login varchar(10),
		foreign key(login) references manejador(login)
		);
	
	create table egreso(
		id int primary key not null auto_increment,
		fecha date,
		descripcion varchar(200),
		importe double,
		login varchar(10),
		foreign key(login) references manejador(login)
		);