package gerenciadorTarefas.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import gerenciadorTarefas.repositories.TarefaRepository;

@Controller
public class HomeController {

	@Autowired
	private TarefaRepository tarefaRepository;

	@GetMapping("/")
	public ModelAndView home() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("home/home");
		mv.addObject("tarefas", tarefaRepository.carregarTodasNaoConcluidas());
		mv.addObject("mensagem", "Mensagem do controller home.");
		return mv;
	}
}
