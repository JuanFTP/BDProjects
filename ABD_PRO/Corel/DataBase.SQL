create database Control;
	use Control;
	-- Tabla para los administradores y usuarios del sistema
	create table manejadores(
		login varchar(10) primary key unique,
		password varchar(10),
		nombreM varchar(20),
		apellidoM varchar(40),
		telefonoM varchar(10) unique,
		direccionM varchar(100),
		permisos int(4),
		loginMC varchar(10),
		foreign key(login)
		references manejadores(login)
		);
	-- Tabla de usuario del servicio
	create table usuario(
		idUS int primary key,
		nombre varchar(20),
		apellidos varchar(40),
		telefono varchar(10) unique,
		direccion varchar(100),
		);