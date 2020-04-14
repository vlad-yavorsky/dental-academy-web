package ua.kazo.dentalacademy.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import ua.kazo.dentalacademy.constants.ModelMapConstants;
import ua.kazo.dentalacademy.entity.Folder;
import ua.kazo.dentalacademy.enumerated.ExceptionCode;
import ua.kazo.dentalacademy.enumerated.FolderCategory;
import ua.kazo.dentalacademy.exception.ApplicationException;
import ua.kazo.dentalacademy.mapper.FolderItemMapper;
import ua.kazo.dentalacademy.mapper.FolderMapper;
import ua.kazo.dentalacademy.mapper.OfferingMapper;
import ua.kazo.dentalacademy.mapper.ProgramMapper;
import ua.kazo.dentalacademy.security.Permission;
import ua.kazo.dentalacademy.security.TargetType;
import ua.kazo.dentalacademy.service.FolderItemService;
import ua.kazo.dentalacademy.service.FolderService;
import ua.kazo.dentalacademy.service.OfferingService;
import ua.kazo.dentalacademy.service.ProgramService;

import java.security.Principal;
import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class ProgramController {

    private final ProgramService programService;
    private final ProgramMapper programMapper;
    private final FolderService folderService;
    private final FolderMapper folderMapper;
    private final FolderItemService folderItemService;
    private final FolderItemMapper folderItemMapper;
    private final OfferingMapper offeringMapper;
    private final OfferingService offeringService;

    /* ---------------------------------------------- MY PROGRAMS ---------------------------------------------- */

    @GetMapping("/")
    public String myPrograms(final ModelMap model, final Principal principal) {
        model.addAttribute(ModelMapConstants.PROGRAMS, programMapper.toResponseDto(programService.findAllPurchasedPrograms(principal.getName())));
        return "index";
    }

    /* ---------------------------------------------- FOLDERS ---------------------------------------------- */

    private void addFolderCategoriesExistence(final Long programId, final ModelMap model, final FolderCategory category) {
        model.addAttribute(ModelMapConstants.IS_MODULES_EXIST, FolderCategory.MODULE == category || programService.existsByIdAndFolderCategory(programId, FolderCategory.MODULE));
        model.addAttribute(ModelMapConstants.IS_QA_EXIST, FolderCategory.QA == category || programService.existsByIdAndFolderCategory(programId, FolderCategory.QA));
    }

    @GetMapping("/program/{programId}")
    public RedirectView program(final @PathVariable Long programId) {
        boolean isModulesExist = programService.existsByIdAndFolderCategory(programId, FolderCategory.MODULE);
        boolean isQaExist = programService.existsByIdAndFolderCategory(programId, FolderCategory.QA);
        if (isModulesExist) {
            return new RedirectView("/program/" + programId + "/modules");
        } else if (isQaExist) {
            return new RedirectView("/program/" + programId + "/qa");
        }
        throw new ApplicationException(ExceptionCode.PROGRAM_NOT_FOUND, programId);
    }

    private String loadProgramFolders(final Long programId, final FolderCategory category, final ModelMap model) {
        model.addAttribute(ModelMapConstants.PROGRAM, programMapper.toFoldersResponseDto(programService.findByIdAndFolderCategoryFetchFolders(programId, category)));
        addFolderCategoriesExistence(programId, model, category);
        return "client/program/program-folders";
    }

    @PreAuthorize("hasPermission(#programId, '" + TargetType.PROGRAM + "', '" + Permission.READ + "')")
    @GetMapping("/program/{programId}/{category}")
    public String programFolderCategory(final ModelMap model, final @PathVariable Long programId, final @PathVariable String category) {
        FolderCategory folderCategory;
        switch (category) {
            case "modules":
                folderCategory = FolderCategory.MODULE;
                break;
            case "qa":
                folderCategory = FolderCategory.QA;
                break;
            default:
                throw new ApplicationException(ExceptionCode.FOLDER_CATEGORY_DOES_NOT_EXIST, category);
        }
        return loadProgramFolders(programId, folderCategory, model);
    }

    /* ---------------------------------------------- FOLDER ITEMS ---------------------------------------------- */

    @PreAuthorize("hasPermission(#folderId, '" + TargetType.FOLDER + "', '" + Permission.READ + "')")
    @GetMapping({"/program/{programId}/folder/{folderId}", "/program/{programId}/folder/{folderId}/item/{itemId}"})
    public String folderItems(final ModelMap model, final @PathVariable Long programId, final @PathVariable Long folderId, final @PathVariable(required = false) Long itemId) {
        Folder folder = folderService.findByIdFetchItems(folderId);
        model.addAttribute(ModelMapConstants.PROGRAM, programMapper.toResponseDto(programService.findById(programId)));
        model.addAttribute(ModelMapConstants.FOLDER, folderMapper.toItemsResponseDto(folder));
        model.addAttribute(ModelMapConstants.SELECTED_ITEM, folderItemMapper.toResponseDto(itemId == null ? folder.getItems().get(0) : folderItemService.findById(itemId)));
        addFolderCategoriesExistence(programId, model, folder.getCategory());
        return "client/program/program-folder-items";
    }

    /* ---------------------------------------------- SHOP ---------------------------------------------- */

    @GetMapping("/shop")
    public String shop(final ModelMap model, final Principal principal) {
        model.addAttribute(ModelMapConstants.PROGRAMS, programMapper.toShopResponseDto(programService.findAllWithOfferings(), principal.getName()));
        return "client/shop/shop";
    }

    /* ---------------------------------------------- BUY PROGRAM ---------------------------------------------- */

    @GetMapping("/buy/program/{programId}")
    public String buyProgram(final ModelMap model, @PathVariable final Long programId) {
        model.addAttribute(ModelMapConstants.PROGRAM, programMapper.toResponseDto(programService.findById(programId)));
        model.addAttribute(ModelMapConstants.OFFERINGS, offeringMapper.toFullResponseDto(offeringService.findAllByOfferingIdsIfActiveFetchProgramsAndFolders(programId)));
        model.addAttribute(ModelMapConstants.NOW, LocalDateTime.now());
        return "client/shop/shop-item";
    }

    @GetMapping("/buy/offering/{offeringId}")
    public RedirectView buyProgramProcess(@PathVariable final Long offeringId, final Principal principal, final RedirectAttributes redirectAttributes) {
        offeringService.buy(offeringId, principal.getName());
        redirectAttributes.addFlashAttribute(ModelMapConstants.SUCCESS, "program.buy.success");
        return new RedirectView("/");
    }

    /* ---------------------------------------------- MY BONUSES ---------------------------------------------- */

    @GetMapping("/bonuses")
    public String myBonuses(final ModelMap model, final Principal principal) {
        model.addAttribute(ModelMapConstants.BONUSES, folderMapper.toResponseDto(folderService.findAllByUserEmail(principal.getName())));
        return "client/bonus/bonuses";
    }

    /* ---------------------------------------------- BONUS ITEMS ---------------------------------------------- */

    @PreAuthorize("hasPermission(#folderId, '" + TargetType.FOLDER + "', '" + Permission.READ + "')")
    @GetMapping({"/bonus/{folderId}", "/bonus/{folderId}/item/{itemId}"})
    public String myBonuses(final @PathVariable Long folderId, final @PathVariable(required = false) Long itemId, final ModelMap model) {
        Folder folder = folderService.findByIdFetchItems(folderId);
        model.addAttribute(ModelMapConstants.SELECTED_ITEM, folderItemMapper.toResponseDto(itemId == null ? folder.getItems().get(0) : folderItemService.findById(itemId)));
        model.addAttribute(ModelMapConstants.BONUS, folderMapper.toItemsResponseDto(folder));
        return "client/bonus/bonus-items";
    }

}
