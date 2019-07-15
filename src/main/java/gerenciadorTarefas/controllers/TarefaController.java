package gerenciadorTarefas.controllers;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import gerenciadorTarefas.dominio.Tarefa;
import gerenciadorTarefas.dominio.Usuario;
import gerenciadorTarefas.repositories.TarefaRepository;
import gerenciadorTarefas.services.UsuarioService;

@Controller
@RequestMapping("/tarefas")
public class TarefaController {

	@Autowired
	private TarefaRepository repositorioTarefa;
	
	@Autowired
	private UsuarioService servicoUsuario;
	
	@GetMapping("/listar")
	public ModelAndView listar(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("tarefas/listar"); // pasta/arquivo.html
		String emailUsuario = request.getUserPrincipal().getName();
		mv.addObject("tarefas", repositorioTarefa.carregarTarefasPorUsuario(emailUsuario)); // objeto tarefas, findAll() vem do JpaRepository 
		return mv;
	}
	
	// direciona para a página inserir
	@GetMapping("/inserir")
	public ModelAndView inserir() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("tarefas/inserir");
		mv.addObject("tarefa", new Tarefa());
		return mv;
	}
	
	// insere os dados
	@PostMapping("/inserir")
	public ModelAndView inserir(@Valid Tarefa tarefa, BindingResult result, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();
		if(tarefa.getDataExpiracao() == null) { // valida a data
			result.rejectValue("dataExpiracao", "tarefa.dataExpiracaoInvalida", "Data de expiração nula.");
		} else {
			if(tarefa.getDataExpiracao().before(new Date())) { // valida a data
				result.rejectValue("dataExpiracao", "tarefa.dataExpiracaoInvalida", "Data de expiração inválida.");
			}
		}
		if(result.hasErrors()) { // verifica se tem erro nos campos de inserção
			mv.setViewName("tarefas/inserir"); // se tiver erro, volta para inserir
			mv.addObject(tarefa); // traz os campos preenchidos
		} else {
			String emailUsuario = request.getUserPrincipal().getName();
			Usuario usuarioLogado = servicoUsuario.buscarPorEmail(emailUsuario);
			tarefa.setUsuario(usuarioLogado);
			repositorioTarefa.save(tarefa);
			mv.setViewName("redirect:/tarefas/listar");
		}
		return mv;
	}
	
	// direciona para a página de alteração
	@GetMapping("/alterar/{id}")
	public ModelAndView alterar(@PathVariable("id") Long id) {
		ModelAndView mv = new ModelAndView();
		Tarefa tarefa = repositorioTarefa.getOne(id);
		mv.addObject("tarefa", tarefa);
		return mv;
	}
	
	// altera os dados
	@PostMapping("/alterar")
	public ModelAndView alterar(@Valid Tarefa tarefa, BindingResult result) {
		ModelAndView mv = new ModelAndView();
		if(tarefa.getDataExpiracao() == null) { // valida a data
			result.rejectValue("dataExpiracao", "tarefa.dataExpiracaoInvalida", "Data de expiração nula.");
		} else {
			if(tarefa.getDataExpiracao().before(new Date())) { // valida a data
				result.rejectValue("dataExpiracao", "tarefa.dataExpiracaoInvalida", "Data de expiração inválida.");
			}
		}
		if(result.hasErrors()) { // verifica se tem erro nos campos de inserção
			mv.setViewName("tarefas/alterar"); // se tiver erro, volta para alterar
			mv.addObject(tarefa); // traz os campos preenchidos
		} else {
			mv.setViewName("redirect:/tarefas/listar");
			repositorioTarefa.save(tarefa);
		}
		return mv;
	}
	
	@GetMapping("/excluir/{id}")
	public String excluir(@PathVariable("id") Long id) {
		repositorioTarefa.deleteById(id);
		return "redirect:/tarefas/listar";
	}
	
	@GetMapping("/concluir/{id}")
	public String concluir(@PathVariable("id") Long id) {
		Tarefa tarefa = repositorioTarefa.getOne(id);
		tarefa.setConcluida(true);
		repositorioTarefa.save(tarefa);
		return "redirect:/tarefas/listar";
	}
}
