create table tarefas (
	id_tarefa bigint not null auto_increment primary key,
	titulo varchar(65),
	descricao varchar(255),
	data_expiracao datetime
);