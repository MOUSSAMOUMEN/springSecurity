package ma.enset.projecthospital.web;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import ma.enset.projecthospital.entities.Patient;
import ma.enset.projecthospital.repositories.PatientRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor
public class PatientController {

    private PatientRepository patientRepository;

    @GetMapping("/index")
    public String index(Model model, @RequestParam(name="p",defaultValue = "0") int p,
                        @RequestParam(name="s",defaultValue ="4") int s,
                        @RequestParam(name="keyword",defaultValue ="") String kw
    ){

        Page<Patient> patientPage=patientRepository.findByNomContains(kw,PageRequest.of(p,s));
        model.addAttribute("ListPatients",patientPage.getContent());
        model.addAttribute("pages",new int[patientPage.getTotalPages()]);
        model.addAttribute("currentPage",p);
        model.addAttribute("keyword",kw);
        return "patients";
    }

    @GetMapping("/delete")
    public String delete(Long id,String keyword,int page){
        patientRepository.deleteById(id);
        return "redirect:/index?page="+page+"&keyword="+keyword;
    }


  @GetMapping("/formPatients")
    public String formPatient(Model model){

        model.addAttribute("patient",new Patient());
         return "formPatients";
    }

    @PostMapping(path="/save")
    public String save(Model model, @Valid Patient patient, BindingResult bindingResult,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "") String keyword){
        if(bindingResult.hasErrors()) return "formPatients";
        patientRepository.save(patient);
        return "redirect:/index?page="+page+"&keyword="+keyword;

    }

    @GetMapping("/editPatient")
    public String editPatient(Model model,Long id,String keyword,int page){

        Patient patient=patientRepository.findById(id).orElse(null);
        if(patient==null) new RuntimeException("Patient Introuvable");
        model.addAttribute("patient",patient);
        model.addAttribute("paage",page);
        model.addAttribute("keyword",keyword);
        return "editPatient";
    }
}
