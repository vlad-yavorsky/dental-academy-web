package ua.kazo.dentalacademy.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ua.kazo.dentalacademy.constants.ModelMapConstants;
import ua.kazo.dentalacademy.dto.folder.FolderCreateDto;
import ua.kazo.dentalacademy.dto.folder.FolderUpdateDto;
import ua.kazo.dentalacademy.dto.folder.item.FolderItemCreateDto;
import ua.kazo.dentalacademy.dto.folder.item.FolderItemUpdateDto;
import ua.kazo.dentalacademy.entity.Folder;
import ua.kazo.dentalacademy.entity.FolderItem;
import ua.kazo.dentalacademy.mapper.FolderMapper;
import ua.kazo.dentalacademy.mapper.ProgramMapper;
import ua.kazo.dentalacademy.service.FolderService;
import ua.kazo.dentalacademy.service.ProgramService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Comparator;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminFolderController {

    private final ProgramService programService;
    private final ProgramMapper programMapper;
    private final FolderService folderService;
    private final FolderMapper folderMapper;

    /* ---------------------------------------------- ADD FOLDER ---------------------------------------------- */

    private void validateFolderName(final Folder folder, final BindingResult bindingResult, final boolean isAdd) {
        if (isAdd) {
            if (folderService.existsByNameAndProgram(folder.getName(), folder.getProgram())) {
                bindingResult.rejectValue("name", "validation.NameNotUnique");
            }
        } else {
            if (folderService.existsByNameAndProgramAndIdNot(folder.getName(), folder.getProgram(), folder.getId())) {
                bindingResult.rejectValue("name", "validation.NameNotUnique");
            }
        }
    }

    private String loadFolderAddPage(final FolderCreateDto folderCreateDto, final ModelMap model) {
        model.addAttribute(ModelMapConstants.FOLDER, folderCreateDto);
        model.addAttribute(ModelMapConstants.PROGRAMS, programMapper.toResponseDto(programService.findAll()));
        return "admin/folder/folder-add";
    }

    @GetMapping("/folder/add")
    public String addFolder(@RequestParam(required = false) Long programId, final ModelMap model) {
        FolderCreateDto folderCreateDto = new FolderCreateDto();
        folderCreateDto.setProgramId(programId);
        return loadFolderAddPage(folderCreateDto, model);
    }

    @PostMapping("/folder/add")
    public String addFolder(@ModelAttribute(ModelMapConstants.FOLDER) @Valid final FolderCreateDto folderCreateDto,
                            final BindingResult bindingResult, final ModelMap model, final RedirectAttributes redirectAttributes) {
        Folder folder = folderMapper.toEntity(folderCreateDto);

        validateFolderName(folder, bindingResult, true);
        if (bindingResult.hasErrors()) {
            model.addAttribute(ModelMapConstants.FIELD_ERRORS, bindingResult.getFieldErrors());
            return loadFolderAddPage(folderCreateDto, model);
        }
        redirectAttributes.addFlashAttribute(ModelMapConstants.SUCCESS, "success.folder.add");

        folder.getItems().forEach(folderItem -> folderItem.setFolder(folder));
        Folder savedFolder = folderService.save(folder);
        return "redirect:/admin/folder/edit/" + savedFolder.getId();
    }

    @PostMapping(value = "/folder/add", params = {"addRow"})
    public String addFolderAddRow(@ModelAttribute(ModelMapConstants.FOLDER) final FolderCreateDto folderCreateDto, ModelMap model) {
        folderCreateDto.getItems().add(new FolderItemCreateDto());
        return loadFolderAddPage(folderCreateDto, model);
    }

    @PostMapping(value = "/folder/add", params = {"removeRow"})
    public String addFolderRemoveRow(@ModelAttribute(ModelMapConstants.FOLDER) final FolderCreateDto folderCreateDto, final HttpServletRequest request, ModelMap model) {
        int rowId = Integer.parseInt(request.getParameter("removeRow"));
        folderCreateDto.getItems().remove(rowId);
        return loadFolderAddPage(folderCreateDto, model);
    }

    /* ---------------------------------------------- EDIT FOLDER ---------------------------------------------- */

    private String loadFolderEditPage(final FolderUpdateDto folderUpdateDto, final ModelMap model) {
        model.addAttribute(ModelMapConstants.FOLDER, folderUpdateDto);
        model.addAttribute(ModelMapConstants.PROGRAMS, programMapper.toResponseDto(programService.findAll()));
        return "admin/folder/folder-edit";
    }

    @GetMapping("/folder/edit/{id}")
    public String editFolder(@PathVariable Long id, final ModelMap model) {
        return loadFolderEditPage(folderMapper.toUpdateDto(folderService.findByIdFetchItems(id)), model);
    }

    @PostMapping("/folder/edit/{id}")
    public String editFolder(@ModelAttribute(ModelMapConstants.FOLDER) @Valid final FolderUpdateDto folderUpdateDto, final BindingResult bindingResult, final ModelMap model) {
        Folder folder = folderMapper.toEntity(folderUpdateDto);

        validateFolderName(folder, bindingResult, false);
        if (bindingResult.hasErrors()) {
            model.addAttribute(ModelMapConstants.FIELD_ERRORS, bindingResult.getFieldErrors());
            return loadFolderEditPage(folderUpdateDto, model);
        }
        model.addAttribute(ModelMapConstants.SUCCESS, "success.folder.edit");

        Folder savedFolder = folderService.save(folder);
        savedFolder.getItems().sort(Comparator.comparingInt(FolderItem::getOrdering));
        return loadFolderEditPage(folderMapper.toUpdateDto(savedFolder), model);
    }

    @PostMapping(value = "/folder/edit/{id}", params = {"addRow"})
    public String editFolderAddRow(@ModelAttribute(ModelMapConstants.FOLDER) final FolderUpdateDto folderUpdateDto, ModelMap model) {
        FolderItemUpdateDto folderItemUpdateDto = new FolderItemUpdateDto();
        folderItemUpdateDto.setFolderId(folderUpdateDto.getId());
        folderUpdateDto.getItems().add(folderItemUpdateDto);
        return loadFolderEditPage(folderUpdateDto, model);
    }

    @PostMapping(value = "/folder/edit/{id}", params = {"removeRow"})
    public String editFolderRemoveRow(@ModelAttribute(ModelMapConstants.FOLDER) final FolderUpdateDto folderUpdateDto, final HttpServletRequest request, ModelMap model) {
        int rowId = Integer.parseInt(request.getParameter("removeRow"));
        folderUpdateDto.getItems().remove(rowId);
        return loadFolderEditPage(folderUpdateDto, model);
    }

}
