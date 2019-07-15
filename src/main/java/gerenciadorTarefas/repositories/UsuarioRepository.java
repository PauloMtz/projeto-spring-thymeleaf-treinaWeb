package gerenciadorTarefas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import gerenciadorTarefas.dominio.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	Usuario findByEmail(String email);
	// o próprio Spring executa o select por email com esse método
}
