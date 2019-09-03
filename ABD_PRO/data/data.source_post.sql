create database agua;
	create table manejador(
		login varchar(10) unique primary key,
		password varchar(15),
		nombreM varchar(20),
		apellidos varchar(50),
		direccion varchar(50),
		permisos int(4),
		fecha date
		);

	/*Admin*/
	insert into manejador values('admin', 'admin1234', 'Juan', 'FTP', 'Calle s/n', 1111, curdate());
	insert into manejador values('adSO12', 'adrielSO12', 'Adriel', 'Zaragoza Monta_o', 'Gomez Pedraza', 1111, curdate());		
	/*User N*/
	insert into manejador values('lector', 'soylector', 'Gad', 'Dante Fiol', 'Benito Juarez', 1000, curdate());

	select * from manejador;