package gerenciadorTarefas.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import gerenciadorTarefas.dominio.Usuario;
import gerenciadorTarefas.repositories.UsuarioRepository;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository repositorioUsuario;
	
	@Autowired
	private BCryptPasswordEncoder senhaEncoder;
	
	public Usuario buscarPorEmail(String email) {
		return repositorioUsuario.findByEmail(email);
	}
	
	public void salvarUsuario(Usuario usuario) {
		usuario.setSenha(senhaEncoder.encode(usuario.getSenha()));
		repositorioUsuario.save(usuario);
	}
}
