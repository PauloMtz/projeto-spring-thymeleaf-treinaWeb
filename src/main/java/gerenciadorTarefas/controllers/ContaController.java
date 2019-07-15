package gerenciadorTarefas.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import gerenciadorTarefas.dominio.Usuario;
import gerenciadorTarefas.services.UsuarioService;

@Controller
public class ContaController {

	@Autowired
	private UsuarioService servicoUsuario;
	
	@GetMapping("/login")
	public String login() {
		return "conta/login";
	}
	
	// direciona para a página de registro
	@GetMapping("/registration")
	public ModelAndView registrar() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("conta/registrar");
		mv.addObject("usuario", new Usuario());
		return mv;
	}
	
	// quando o usuário preenche o formulário e manda salvar
	@PostMapping("/registration")
	public ModelAndView registrar(@Valid Usuario usuario, BindingResult result) {
		ModelAndView mv = new ModelAndView();
		Usuario user = servicoUsuario.buscarPorEmail(usuario.getEmail());
		if(user != null) {
			result.rejectValue("email", "", "Usuário já cadastrado.");
		}
		if(result.hasErrors()) {
			mv.setViewName("conta/registrar");
			mv.addObject("usuario", usuario);
		} else {
			servicoUsuario.salvarUsuario(usuario);
			mv.setViewName("redirect:/login");
		}
		return mv;
	}
}
