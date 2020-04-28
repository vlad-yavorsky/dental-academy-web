package ua.kazo.dentalacademy.controller.client;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import ua.kazo.dentalacademy.constants.AppConfig;
import ua.kazo.dentalacademy.constants.ModelMapConstants;
import ua.kazo.dentalacademy.entity.Program;
import ua.kazo.dentalacademy.entity.PurchaseData;
import ua.kazo.dentalacademy.mapper.OfferingMapper;
import ua.kazo.dentalacademy.mapper.ProgramMapper;
import ua.kazo.dentalacademy.service.OfferingService;
import ua.kazo.dentalacademy.service.ProgramService;
import ua.kazo.dentalacademy.service.PurchaseDataService;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ShopController {

    private final ProgramService programService;
    private final ProgramMapper programMapper;
    private final OfferingMapper offeringMapper;
    private final OfferingService offeringService;
    private final PurchaseDataService purchaseDataService;

    /* ---------------------------------------------- SHOP ---------------------------------------------- */

    @GetMapping("/shop")
    public String shop(final ModelMap model, @RequestParam(required = false) final String search,
                       @RequestParam(defaultValue = "0") final int page,
                       @RequestParam(defaultValue = AppConfig.Constants.DEFAULT_PAGE_SIZE_VALUE) final int size) {
        Page<Program> pageResult = programService.findAllByNotDeactivatedOfferings(search, PageRequest.of(page, size));
        model.addAttribute(ModelMapConstants.PROGRAMS, programMapper.toResponseDto(pageResult));
        model.addAttribute(ModelMapConstants.SEARCH, search);
        model.addAttribute("pageResult", pageResult);
        return "client/shop/shop";
    }

    /* ---------------------------------------------- SHOP ITEM ---------------------------------------------- */

    @GetMapping("/shop/program/{programId}")
    public String shopItem(final ModelMap model, @PathVariable final Long programId, final Principal principal) {
        List<Long> offeringIds = offeringService.findAllIdsByProgramId(programId);
        List<PurchaseData> purchasesByUser = purchaseDataService.findAllByIdOfferingIdInAndUserEmail(offeringIds, principal.getName());
        model.addAttribute(ModelMapConstants.PROGRAM, programMapper.toResponseDto(programService.findById(programId)));
        model.addAttribute(ModelMapConstants.OFFERINGS, offeringMapper.toShopItemResponseDto(offeringService.findAllByIdsAndNotDeactivatedFetchProgramsAndFolders(offeringIds), purchasesByUser));
        model.addAttribute(ModelMapConstants.IS_PURCHASED, purchaseDataService.isProgramPurchasedAndNotExpired(programId, principal.getName()));
        model.addAttribute(ModelMapConstants.NOW, LocalDateTime.now());
        return "client/shop/shop-item";
    }

    /* ---------------------------------------------- BUY OFFERING ---------------------------------------------- */

    @GetMapping("/shop/program/{programId}/buy/{offeringId}")
    public RedirectView buyOffering(@PathVariable final Long programId, @PathVariable final Long offeringId, final Principal principal, final RedirectAttributes redirectAttributes) {
        offeringService.buy(offeringId, principal.getName());
        redirectAttributes.addFlashAttribute(ModelMapConstants.SUCCESS, "success.program.purchase");
        return new RedirectView("/shop/program/" + programId);
    }

}
