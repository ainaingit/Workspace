package com.example.workspace.controller;

import com.example.workspace.entity.Admin;
import com.example.workspace.entity.Client;
import com.example.workspace.entity.Reservation;
import com.example.workspace.entity.Workspace;
import com.example.workspace.model.DashboardModel;
import com.example.workspace.repository.AdminRepository;
import com.example.workspace.repository.ClientRepository;
import com.example.workspace.repository.ReservationRepository;
import com.example.workspace.repository.WorkspaceRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
public class LoginController {
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private WorkspaceRepository workspaceRepository;

    @Autowired
    ReservationRepository reservationRepository;



    @GetMapping("/")
    public String index() { return "index"; }

    @GetMapping("/admin/login")
    public String adminLogin() { return "adminLogin"; }

    @GetMapping("/client/login")
    public String clientLogin() { return "clientLogin"; }

    @GetMapping("/client-dashboard")
    public String clientDashboard(@RequestParam(value = "date", required = false) String dateStr, Model model) {
        List<Workspace> listeWorkspace = workspaceRepository.findAll();
        List<Reservation> reservations = new ArrayList<>();

        if (dateStr != null && !dateStr.isEmpty()) {
            LocalDate date = LocalDate.parse(dateStr);
            reservations = reservationRepository.findByDate(date);
        }

        // Créer un DashboardModel avec les nouvelles réservations
        DashboardModel dashboardModel = new DashboardModel(listeWorkspace, reservations);

        // Ajouter l'objet dashboardModel à l'attribut du modèle
        model.addAttribute("dashboardModel", dashboardModel);

        // Retourner la vue client-dashboard pour afficher le tableau mis à jour
        return "client-dashboard";
    }



    @GetMapping("/admin-dashboard")
    public String adminDashboard(Model model) {
        // Vous pouvez ajouter des attributs au modèle ici, si nécessaire
        return "admin-dashboard"; // Assurez-vous que ce fichier JSP ou template existe
    }

    @PostMapping("/login_admin")
    public String loginAdmin(@RequestParam String username, @RequestParam String password, HttpSession session, Model model) {
        try {
            Admin admin = adminRepository.findByNom(username);
            if (admin != null) {
                session.setAttribute("adminId", admin.getId());
                return "redirect:/admin-dashboard";
            }
            model.addAttribute("error", "Nom d'utilisateur ou mot de passe incorrect.");
        } catch (Exception e) {
            model.addAttribute("error", "Une erreur inattendue s'est produite.");
            e.printStackTrace();
        }
        return "/admin/login";
    }


    @PostMapping("/login_client")
    public String loginClient(@RequestParam("numero") String numero,
                              HttpSession session,
                              Model model) {
        System.out.println("Données reçues : " + numero);
        try {
            // Recherche le client via le numéro
            Client client = clientRepository.findByNumber(numero);

            if (client != null) {
                // Si le client est trouvé, créer une session et rediriger vers le dashboard du client


                session.setAttribute("clientId", client.getId());
                return "redirect:/client-dashboard"; // Redirection vers le dashboard du client
            } else {
                // Si le client n'est pas trouvé, ajouter un message d'erreur
                model.addAttribute("error", "Numéro incorrect.");
                return "loginclient"; // Retourner à la page de login avec un message d'erreur
            }
        } catch (Exception e) {
            e.printStackTrace();
            // En cas d'exception, afficher un message d'erreur générique
            model.addAttribute("error", "Une erreur inattendue s'est produite.");
            return "loginclient"; // Retourner à la page de login avec un message d'erreur générique
        }
    }

}
