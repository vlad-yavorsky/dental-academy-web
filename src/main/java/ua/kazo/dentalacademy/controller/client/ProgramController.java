package ua.kazo.dentalacademy.controller.client;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import ua.kazo.dentalacademy.constants.AppConfig;
import ua.kazo.dentalacademy.constants.ModelMapConstants;
import ua.kazo.dentalacademy.dto.folder.item.FolderItemResponseDto;
import ua.kazo.dentalacademy.entity.Program;
import ua.kazo.dentalacademy.mapper.FolderItemMapper;
import ua.kazo.dentalacademy.mapper.ProgramMapper;
import ua.kazo.dentalacademy.security.Permission;
import ua.kazo.dentalacademy.security.TargetType;
import ua.kazo.dentalacademy.service.FolderItemService;
import ua.kazo.dentalacademy.service.ProgramService;
import ua.kazo.dentalacademy.service.ViewedFolderItemService;
import ua.kazo.dentalacademy.util.AuthUtils;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ProgramController {

    private final ProgramService programService;
    private final ProgramMapper programMapper;
    private final FolderItemService folderItemService;
    private final ViewedFolderItemService viewedFolderItemService;
    private final FolderItemMapper folderItemMapper;

    @GetMapping("/")
    public String index() {
        return "redirect:/shop";
    }

    /* ---------------------------------------------- MY PROGRAMS ---------------------------------------------- */

    @GetMapping("/programs")
    public String myPrograms(final ModelMap model, final Principal principal, @RequestParam(required = false) final String search,
                             @RequestParam(defaultValue = "0") final int page,
                             @RequestParam(defaultValue = AppConfig.Constants.DEFAULT_PAGE_SIZE_VALUE) final int size) {
        Page<Program> pageResult = programService.findAllProgramsByNotExpiredPurchase(principal.getName(), search, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id")));
        model.addAttribute(ModelMapConstants.PROGRAMS, programMapper.toResponseDto(pageResult));
        model.addAttribute(ModelMapConstants.SEARCH, search);
        model.addAttribute(ModelMapConstants.PAGE_RESULT, pageResult);
        return "client/program/programs";
    }

    /* ---------------------------------------------- PROGRAM ---------------------------------------------- */

    private void loadProgramFolders(final Long programId, final ModelMap model, final FolderItemResponseDto selectedItem, final Long userId) {
        model.addAttribute(ModelMapConstants.PROGRAM, programMapper.toViewedFoldersItemsResponseDto(programService.findByIdFetchFoldersAndItemsAndViewedFolderItems(programId), userId));
        model.addAttribute(ModelMapConstants.SELECTED_ITEM, selectedItem);
    }

    @PreAuthorize("hasPermission(#programId, '" + TargetType.PROGRAM + "', '" + Permission.READ + "')")
    @GetMapping("/program/{programId}")
    public String program(final @PathVariable Long programId, final ModelMap model, final Principal principal) {
        Long userId = AuthUtils.getUser(principal).getId();
        loadProgramFolders(programId, model, null, userId);
        return "client/program/program";
    }

    @PreAuthorize("hasPermission(#programId, '" + TargetType.PROGRAM + "', '" + Permission.READ + "')")
    @GetMapping("/program/{programId}/item/{itemId}")
    public String programItem(final @PathVariable Long programId, @PathVariable(required = false) Long itemId,
                          final ModelMap model, final Principal principal) {
        Long userId = AuthUtils.getUser(principal).getId();
        viewedFolderItemService.setFolderItemIsViewedByUser(userId, itemId);
        loadProgramFolders(programId, model, folderItemMapper.toResponseDto(folderItemService.findById(itemId)), userId);
        return "client/program/program-item";
    }

    /* ---------------------------------------------- MY BONUSES ---------------------------------------------- */

    @GetMapping("/bonuses")
    public String myBonuses(final ModelMap model, final Principal principal, @RequestParam(required = false) final String search,
                            @RequestParam(defaultValue = "0") final int page,
                            @RequestParam(defaultValue = AppConfig.Constants.DEFAULT_PAGE_SIZE_VALUE) final int size) {
        Page<Program> pageResult = programService.findAllBonusesByNotExpiredPurchaseAndName(principal.getName(), search, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id")));
        model.addAttribute(ModelMapConstants.BONUSES, programMapper.toResponseDto(pageResult));
        model.addAttribute(ModelMapConstants.SEARCH, search);
        model.addAttribute(ModelMapConstants.PAGE_RESULT, pageResult);
        return "client/bonus/bonuses";
    }

    /* ---------------------------------------------- BONUS ---------------------------------------------- */

    private void loadBonusFolders(final Long programId, final ModelMap model, final FolderItemResponseDto selectedItem, final Long userId) {
        model.addAttribute(ModelMapConstants.BONUS, programMapper.toViewedFoldersItemsResponseDto(programService.findByIdFetchFoldersAndItemsAndViewedFolderItems(programId), userId));
        model.addAttribute(ModelMapConstants.SELECTED_ITEM, selectedItem);
    }

    @PreAuthorize("hasPermission(#bonusId, '" + TargetType.BONUS + "', '" + Permission.READ + "')")
    @GetMapping("/bonus/{bonusId}")
    public String bonus(final @PathVariable Long bonusId, final ModelMap model, final Principal principal) {
        Long userId = AuthUtils.getUser(principal).getId();
        loadBonusFolders(bonusId, model, null, userId);
        return "client/bonus/bonus";
    }

    @PreAuthorize("hasPermission(#bonusId, '" + TargetType.BONUS + "', '" + Permission.READ + "')")
    @GetMapping("/bonus/{bonusId}/item/{itemId}")
    public String bonusItem(final @PathVariable Long bonusId, @PathVariable(required = false) Long itemId,
                            final ModelMap model, final Principal principal) {
        Long userId = AuthUtils.getUser(principal).getId();
        viewedFolderItemService.setFolderItemIsViewedByUser(userId, itemId);
        loadBonusFolders(bonusId, model, folderItemMapper.toResponseDto(folderItemService.findById(itemId)), userId);
        return "client/bonus/bonus-item";
    }

    /* ---------------------------------------------- RESET PROGRESS ---------------------------------------------- */

    @PreAuthorize("hasPermission(#programId, '" + TargetType.PROGRAM + "', '" + Permission.READ + "')")
    @GetMapping({"/program/{programId}/reset"})
    public RedirectView resetProgramProgress(final @PathVariable Long programId, final RedirectAttributes redirectAttributes, final Principal principal) {
        Long userId = AuthUtils.getUser(principal).getId();
        programService.resetProgramProgress(userId, programId);
        redirectAttributes.addFlashAttribute(ModelMapConstants.SUCCESS, "success.progress.reset");
        return new RedirectView("/program/" + programId);
    }

    @PreAuthorize("hasPermission(#bonusId, '" + TargetType.BONUS + "', '" + Permission.READ + "')")
    @GetMapping({"/bonus/{bonusId}/reset"})
    public RedirectView resetFolderProgress(final @PathVariable Long bonusId, final RedirectAttributes redirectAttributes, final Principal principal) {
        Long userId = AuthUtils.getUser(principal).getId();
        programService.resetProgramProgress(userId, bonusId);
        redirectAttributes.addFlashAttribute(ModelMapConstants.SUCCESS, "success.progress.reset");
        return new RedirectView("/bonus/" + bonusId);
    }

    /* ---------------------------------------------- PROGRAM/BONUS CONTENTS ---------------------------------------------- */

    @GetMapping({"/program/{programId}/preview"})
    public String programPreview(final @PathVariable Long programId, final ModelMap model) {
        model.addAttribute(ModelMapConstants.PROGRAM, programMapper.toFoldersItemsResponseDto(programService.findByIdFetchFoldersAndItemsAndViewedFolderItems(programId)));
        return "client/program/preview";
    }

    @GetMapping({"/bonus/{bonusId}/preview"})
    public String bonusPreview(final @PathVariable Long bonusId, final ModelMap model) {
        model.addAttribute(ModelMapConstants.BONUS, programMapper.toFoldersItemsResponseDto(programService.findByIdFetchFoldersAndItemsAndViewedFolderItems(bonusId)));
        return "client/bonus/preview";
    }

}
