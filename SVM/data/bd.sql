create database ventas;
	use ventas;

	create table cliente (
			id int primary key auto_increment,
			nombre varchar(50),
			apellidoP varchar(50),
			apellidoM varchar(50),
			monto double
		);

	create table producto (
			codigo int primary key auto_increment,
			nombre varchar(50),
			descripcion varchar(50),
			precio double,
			existencia int
		);

	create table credito (
			folio int primary key auto_increment,
			idCliente int,
			foreign key(idCliente) references cliente(id),
			descripcion varchar(200),
			cP int,
			monto double,
			fecha date
		);

	create table abono (
			folio int primary key auto_increment,
			fCredito int,
			foreign key (fCredito) references credito(folio),
			monto double,
			fecha date
		);