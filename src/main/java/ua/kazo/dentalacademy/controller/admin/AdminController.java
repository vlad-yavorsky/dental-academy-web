package ua.kazo.dentalacademy.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.kazo.dentalacademy.enumerated.FolderCategory;
import ua.kazo.dentalacademy.service.StatisticsService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final StatisticsService statisticsService;

    @GetMapping
    public String adminPanel(final ModelMap model) {
        model.addAttribute("programs_count", statisticsService.getProgramsCount());
        model.addAttribute("folders_count", statisticsService.getFoldersCount());
        model.addAttribute("modules_count", statisticsService.getFoldersCountByCategory(FolderCategory.MODULE));
        model.addAttribute("qa_count", statisticsService.getFoldersCountByCategory(FolderCategory.QA));
        model.addAttribute("bonuses_count", statisticsService.getFoldersCountByCategory(FolderCategory.BONUS));
        model.addAttribute("offerings_count", statisticsService.getOfferingsCount());
        model.addAttribute("orders_count", statisticsService.getOrdersCount());
        model.addAttribute("users_count", statisticsService.getUsersCount());
        model.addAttribute("events_count", statisticsService.getEventsCount());
        model.addAttribute("articles_count", statisticsService.getArticlesCount());
        return "admin/panel";
    }

}
