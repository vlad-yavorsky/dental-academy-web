package ua.kazo.dentalacademy.controller.client;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;
import ua.kazo.dentalacademy.constants.AppConfig;
import ua.kazo.dentalacademy.constants.ModelMapConstants;
import ua.kazo.dentalacademy.entity.Folder;
import ua.kazo.dentalacademy.entity.Program;
import ua.kazo.dentalacademy.enumerated.ExceptionCode;
import ua.kazo.dentalacademy.enumerated.FolderCategory;
import ua.kazo.dentalacademy.exception.ApplicationException;
import ua.kazo.dentalacademy.mapper.FolderItemMapper;
import ua.kazo.dentalacademy.mapper.FolderMapper;
import ua.kazo.dentalacademy.mapper.ProgramMapper;
import ua.kazo.dentalacademy.security.Permission;
import ua.kazo.dentalacademy.security.TargetType;
import ua.kazo.dentalacademy.service.FolderItemService;
import ua.kazo.dentalacademy.service.FolderService;
import ua.kazo.dentalacademy.service.ProgramService;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ProgramController {

    private final ProgramService programService;
    private final ProgramMapper programMapper;
    private final FolderService folderService;
    private final FolderMapper folderMapper;
    private final FolderItemService folderItemService;
    private final FolderItemMapper folderItemMapper;
    private final MessageSource messageSource;

    /* ---------------------------------------------- MY PROGRAMS ---------------------------------------------- */

    @GetMapping("/")
    public String myPrograms(final ModelMap model, final Principal principal, @RequestParam(required = false) final String search,
                             @RequestParam(defaultValue = "0") final int page,
                             @RequestParam(defaultValue = AppConfig.Constants.DEFAULT_PAGE_SIZE_VALUE) final int size) {
        Page<Program> pageResult = programService.findAllByNotExpiredPurchase(principal.getName(), search, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id")));
        model.addAttribute(ModelMapConstants.PROGRAMS, programMapper.toResponseDto(pageResult));
        model.addAttribute(ModelMapConstants.SEARCH, search);
        model.addAttribute(ModelMapConstants.PAGE_RESULT, pageResult);
        return "index";
    }

    /* ---------------------------------------------- FOLDERS ---------------------------------------------- */

    private void addFolderCategoriesExistence(final Long programId, final ModelMap model, final FolderCategory category) {
        model.addAttribute(ModelMapConstants.IS_MODULES_EXIST, FolderCategory.MODULE == category || programService.existsByIdAndFolderCategory(programId, FolderCategory.MODULE));
        model.addAttribute(ModelMapConstants.IS_QA_EXIST, FolderCategory.QA == category || programService.existsByIdAndFolderCategory(programId, FolderCategory.QA));
    }

    @GetMapping("/program/{programId}")
    public RedirectView program(final @PathVariable Long programId) {
        if (programService.existsByIdAndFolderCategory(programId, FolderCategory.MODULE)) {
            return new RedirectView("/program/" + programId + "/modules");
        }
        if (programService.existsByIdAndFolderCategory(programId, FolderCategory.QA)) {
            return new RedirectView("/program/" + programId + "/qa");
        }
        throw new ApplicationException(messageSource, ExceptionCode.PROGRAM_NOT_FOUND, programId);
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
                throw new ApplicationException(messageSource, ExceptionCode.FOLDER_CATEGORY_NOT_EXIST, category);
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
        return "client/program/program-folder-item";
    }

    /* ---------------------------------------------- MY BONUSES ---------------------------------------------- */

    @GetMapping("/bonuses")
    public String myBonuses(final ModelMap model, final Principal principal, @RequestParam(required = false) final String search,
                            @RequestParam(defaultValue = "0") final int page,
                            @RequestParam(defaultValue = AppConfig.Constants.DEFAULT_PAGE_SIZE_VALUE) final int size) {
        Page<Folder> pageResult = folderService.findAllByUserEmail(principal.getName(), search, PageRequest.of(page, size));
        model.addAttribute(ModelMapConstants.BONUSES, folderMapper.toResponseDto(pageResult));
        model.addAttribute(ModelMapConstants.SEARCH, search);
        model.addAttribute(ModelMapConstants.PAGE_RESULT, pageResult);
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