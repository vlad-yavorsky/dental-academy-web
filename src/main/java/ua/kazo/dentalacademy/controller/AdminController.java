package ua.kazo.dentalacademy.controller;

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
import ua.kazo.dentalacademy.dto.offering.OfferingCreateDto;
import ua.kazo.dentalacademy.dto.offering.OfferingUpdateDto;
import ua.kazo.dentalacademy.dto.program.ProgramCreateDto;
import ua.kazo.dentalacademy.dto.program.ProgramUpdateDto;
import ua.kazo.dentalacademy.entity.*;
import ua.kazo.dentalacademy.enumerated.FolderCategory;
import ua.kazo.dentalacademy.enumerated.FolderItemType;
import ua.kazo.dentalacademy.enumerated.OfferingType;
import ua.kazo.dentalacademy.mapper.*;
import ua.kazo.dentalacademy.service.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Comparator;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final ProgramService programService;
    private final ProgramMapper programMapper;
    private final OfferingService offeringService;
    private final OfferingMapper offeringMapper;
    private final FolderService folderService;
    private final FolderMapper folderMapper;
    private final UserService userService;
    private final UserMapper userMapper;
    private final EventService eventService;
    private final EventMapper eventMapper;

    /* ---------------------------------------------- PROGRAMS ---------------------------------------------- */

    @GetMapping("/programs")
    public String programs(final ModelMap model) {
        model.addAttribute(ModelMapConstants.PROGRAMS, programMapper.toResponseDto(programService.findAll()));
        return "admin/program/programs";
    }

    /* ---------------------------------------------- ADD PROGRAM ---------------------------------------------- */

    private void validateProgramName(final Program program, final BindingResult bindingResult, final boolean isAdd) {
        if (isAdd) {
            if (programService.existsByName(program.getName())) {
                bindingResult.rejectValue("name", "validation.NameNotUnique");
            }
        } else {
            if (programService.existsByNameAndIdNot(program.getName(), program.getId())) {
                bindingResult.rejectValue("name", "validation.NameNotUnique");
            }
        }
    }

    private String loadProgramAddPage(final ProgramCreateDto programCreateDto, final ModelMap model) {
        model.addAttribute(ModelMapConstants.PROGRAM, programCreateDto);
        return "admin/program/program-add";
    }

    @GetMapping("/program/add")
    public String addProgram(final ModelMap model) {
        return loadProgramAddPage(new ProgramCreateDto(), model);
    }

    @PostMapping("/program/add")
    public String addProgram(@ModelAttribute(ModelMapConstants.PROGRAM) @Valid final ProgramCreateDto programCreateDto,
                             final BindingResult bindingResult, final ModelMap model, final RedirectAttributes redirectAttributes) {
        Program program = programMapper.toEntity(programCreateDto);

        validateProgramName(program, bindingResult, true);
        if (bindingResult.hasErrors()) {
            model.addAttribute(ModelMapConstants.ERRORS, bindingResult.getFieldErrors());
            return loadProgramAddPage(programCreateDto, model);
        }
        redirectAttributes.addFlashAttribute(ModelMapConstants.SUCCESS, "success.program.add");

        Program savedProgram = programService.save(program);
        return "redirect:/admin/program/edit/" + savedProgram.getId();
    }

    /* ---------------------------------------------- EDIT PROGRAM ---------------------------------------------- */

    private String loadProgramEditPage(final ProgramUpdateDto programUpdateDto, final ModelMap model) {
        model.addAttribute(ModelMapConstants.PROGRAM, programUpdateDto);
        model.addAttribute(ModelMapConstants.FOLDERS, folderMapper.toResponseDto(folderService.findAllByProgramId(programUpdateDto.getId())));
        model.addAttribute(ModelMapConstants.OFFERINGS, offeringMapper.toResponseDto(offeringService.findAllByProgramId(programUpdateDto.getId())));
        return "admin/program/program-edit";
    }

    @GetMapping("/program/edit/{id}")
    public String editProgram(@PathVariable Long id, final ModelMap model) {
        return loadProgramEditPage(programMapper.toUpdateDto(programService.findById(id)), model);
    }

    @PostMapping("/program/edit/{id}")
    public String editProgram(@ModelAttribute(ModelMapConstants.PROGRAM) @Valid final ProgramUpdateDto programUpdateDto, final BindingResult bindingResult, final ModelMap model) {
        Program program = programMapper.toEntity(programUpdateDto);

        validateProgramName(program, bindingResult, false);
        if (bindingResult.hasErrors()) {
            model.addAttribute(ModelMapConstants.ERRORS, bindingResult.getFieldErrors());
            return loadProgramEditPage(programUpdateDto, model);
        }
        model.addAttribute(ModelMapConstants.SUCCESS, "success.program.edit");

        Program savedProgram = programService.save(program);
        return loadProgramEditPage(programMapper.toUpdateDto(savedProgram), model);
    }

    /* ---------------------------------------------- FOLDERS/BONUSES ---------------------------------------------- */

    @GetMapping("/bonuses")
    public String bonuses(final ModelMap model) {
        model.addAttribute(ModelMapConstants.BONUSES, folderMapper.toResponseDto(folderService.findAllByCategory(FolderCategory.BONUS)));
        return "admin/folder/bonuses";
    }

    /* ---------------------------------------------- ADD FOLDER ---------------------------------------------- */

    private void validateFolderName(final Folder folder, final BindingResult bindingResult, final boolean isAdd) {
        if (isAdd) {
            if (folderService.existsByNameAndPrograms(folder.getName(), folder.getPrograms())) {
                bindingResult.rejectValue("name", "validation.NameNotUnique");
            }
//            if (folderService.existsByNameAndOfferings(folder.getName(), folder.getOfferings())) {
//                bindingResult.rejectValue("name", "validation.NameNotUnique");
//            }
        } else {
            if (folderService.existsByNameAndProgramsAndIdNot(folder.getName(), folder.getPrograms(), folder.getId())) {
                bindingResult.rejectValue("name", "validation.NameNotUnique");
            }
//            if (folderService.existsByNameAndOfferingsAndIdNot(folder.getName(), folder.getOfferings(), folder.getId())) {
//                bindingResult.rejectValue("name", "validation.NameNotUnique");
//            }
        }
    }

    private String loadFolderAddPage(final FolderCreateDto folderCreateDto, final ModelMap model) {
        model.addAttribute(ModelMapConstants.FOLDER, folderCreateDto);
        model.addAttribute(ModelMapConstants.PROGRAMS, programMapper.toResponseDto(programService.findAll()));
        return "admin/folder/folder-add";
    }

    @GetMapping("/folder/add")
    public String addFolder(@RequestParam(required = false) Long programId, @RequestParam(required = false) FolderCategory category, final ModelMap model) {
        FolderCreateDto folderCreateDto = new FolderCreateDto();
        if (programId != null) {
            folderCreateDto.getPrograms().add(programId);
        }
        if (category != null) {
            folderCreateDto.setCategory(category);
        }
        return loadFolderAddPage(folderCreateDto, model);
    }

    @PostMapping("/folder/add")
    public String addFolder(@ModelAttribute(ModelMapConstants.FOLDER) @Valid final FolderCreateDto folderCreateDto,
                            final BindingResult bindingResult, final ModelMap model, final RedirectAttributes redirectAttributes) {
        Folder folder = folderMapper.toEntity(folderCreateDto);

        validateFolderName(folder, bindingResult, true);
        if (bindingResult.hasErrors()) {
            model.addAttribute(ModelMapConstants.ERRORS, bindingResult.getFieldErrors());
            return loadFolderAddPage(folderCreateDto, model);
        }
        redirectAttributes.addFlashAttribute(ModelMapConstants.SUCCESS, "success.folder.add");

        folder.getItems().forEach(folderItem -> folderItem.setFolder(folder));
        Folder savedFolder = folderService.save(folder);
        return "redirect:/admin/folder/edit/" + savedFolder.getId();
    }

    @PostMapping(value = "/folder/add", params = {"addRow"})
    public String addFolderAddRow(@ModelAttribute(ModelMapConstants.FOLDER) final FolderCreateDto folderCreateDto, final BindingResult bindingResult, ModelMap model) {
        folderCreateDto.getItems().add(new FolderItemCreateDto());
        return loadFolderAddPage(folderCreateDto, model);
    }

    @PostMapping(value = "/folder/add", params = {"removeRow"})
    public String addFolderRemoveRow(@ModelAttribute(ModelMapConstants.FOLDER) final FolderCreateDto folderCreateDto, final BindingResult bindingResult, final HttpServletRequest request, ModelMap model) {
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
        return loadFolderEditPage(folderMapper.toUpdateDto(folderService.findByIdFetchItemsAndPrograms(id)), model);
    }

    @PostMapping("/folder/edit/{id}")
    public String editFolder(@ModelAttribute(ModelMapConstants.FOLDER) @Valid final FolderUpdateDto folderUpdateDto, final BindingResult bindingResult, final ModelMap model) {
        Folder folder = folderMapper.toEntity(folderUpdateDto);

        validateFolderName(folder, bindingResult, false);
        if (bindingResult.hasErrors()) {
            model.addAttribute(ModelMapConstants.ERRORS, bindingResult.getFieldErrors());
            return loadFolderEditPage(folderUpdateDto, model);
        }
        model.addAttribute(ModelMapConstants.SUCCESS, "success.folder.edit");

        Folder savedFolder = folderService.save(folder);
        savedFolder.getItems().sort(Comparator.comparingInt(FolderItem::getOrdering));
        return loadFolderEditPage(folderMapper.toUpdateDto(savedFolder), model);
    }

    @PostMapping(value = "/folder/edit/{id}", params = {"addRow"})
    public String editFolderAddRow(@ModelAttribute(ModelMapConstants.FOLDER) final FolderUpdateDto folderUpdateDto, final BindingResult bindingResult, ModelMap model) {
        FolderItemUpdateDto folderItemUpdateDto = new FolderItemUpdateDto();
        folderItemUpdateDto.setFolderId(folderUpdateDto.getId());
        folderUpdateDto.getItems().add(folderItemUpdateDto);
        return loadFolderEditPage(folderUpdateDto, model);
    }

    @PostMapping(value = "/folder/edit/{id}", params = {"removeRow"})
    public String editFolderRemoveRow(@ModelAttribute(ModelMapConstants.FOLDER) final FolderUpdateDto folderUpdateDto, final BindingResult bindingResult, final HttpServletRequest request, ModelMap model) {
        int rowId = Integer.parseInt(request.getParameter("removeRow"));
        folderUpdateDto.getItems().remove(rowId);
        return loadFolderEditPage(folderUpdateDto, model);
    }

    /* ---------------------------------------------- OFFERINGS ---------------------------------------------- */

    @GetMapping("/offerings")
    public String offerings(final ModelMap model) {
        model.addAttribute(ModelMapConstants.OFFERINGS, offeringMapper.toResponseDto(offeringService.findAll()));
        model.addAttribute(ModelMapConstants.NOW, LocalDateTime.now());
        return "admin/offering/offerings";
    }

    /* ---------------------------------------------- DE/ACTIVATE OFFERINGS ---------------------------------------------- */

    @GetMapping("/offering/activate/{id}")
    public String activateOffering(@PathVariable Long id, final ModelMap model) {
        offeringService.activateOffering(id);
        return offerings(model);
    }

    @GetMapping("/offering/deactivate/{id}")
    public String deactivateOffering(@PathVariable Long id, final ModelMap model) {
        offeringService.deactivateOffering(id);
        return offerings(model);
    }

    /* ---------------------------------------------- ADD OFFERING ---------------------------------------------- */

    private void validateOffering(final Offering offering, final BindingResult bindingResult) {
        if (offering.getActivated() != null && offering.getDeactivated() != null && offering.getActivated().isAfter(offering.getDeactivated())) {
            bindingResult.rejectValue("deactivated", "validation.offering.IncorrectDates");
        }
    }

    private void validateOfferingNameAndType(final Offering offering, final BindingResult bindingResult, final boolean isAdd) {
        if (isAdd) {
            if (offeringService.existsByNameAndType(offering.getName(), offering.getType())) {
                bindingResult.rejectValue("name", "validation.offering.NameAndTypeNotUnique");
            }
        } else {
            if (offeringService.existsByNameAndTypeAndIdNot(offering.getName(), offering.getType(), offering.getId())) {
                bindingResult.rejectValue("name", "validation.offering.NameAndTypeNotUnique");
            }
        }
    }

    private String loadOfferingAddPage(final OfferingCreateDto offeringCreateDto, final ModelMap model) {
        model.addAttribute(ModelMapConstants.OFFERING, offeringCreateDto);
        model.addAttribute(ModelMapConstants.BONUSES, folderMapper.toResponseDto(folderService.findAllByCategory(FolderCategory.BONUS)));
        model.addAttribute(ModelMapConstants.PROGRAMS, programMapper.toResponseDto(programService.findAll()));
        return "admin/offering/offering-add";
    }

    @GetMapping("/offering/add")
    public String addOffering(@RequestParam(required = false) Long programId, final ModelMap model) {
        OfferingCreateDto offeringCreateDto = new OfferingCreateDto();
        if (programId != null) {
            offeringCreateDto.getPrograms().add(programId);
        }
        return loadOfferingAddPage(offeringCreateDto, model);
    }

    @PostMapping("/offering/add")
    public String addOffering(@ModelAttribute(ModelMapConstants.OFFERING) @Valid final OfferingCreateDto offeringCreateDto,
                              final BindingResult bindingResult, final ModelMap model, final RedirectAttributes redirectAttributes) {
        Offering offering = offeringMapper.toEntity(offeringCreateDto);

        validateOffering(offering, bindingResult);
        validateOfferingNameAndType(offering, bindingResult, true);
        if (bindingResult.hasErrors()) {
            model.addAttribute(ModelMapConstants.ERRORS, bindingResult.getFieldErrors());
            return loadOfferingAddPage(offeringCreateDto, model);
        }
        redirectAttributes.addFlashAttribute(ModelMapConstants.SUCCESS, "success.offering.add");

        Offering savedOffering = offeringService.save(offering);
        return "redirect:/admin/offering/edit/" + savedOffering.getId();
    }

    /* ---------------------------------------------- EDIT OFFERING ---------------------------------------------- */

    private String loadOfferingEditPage(final OfferingUpdateDto offeringUpdateDto, final ModelMap model) {
        model.addAttribute(ModelMapConstants.OFFERING, offeringUpdateDto);
        model.addAttribute(ModelMapConstants.BONUSES, folderMapper.toResponseDto(folderService.findAllByCategory(FolderCategory.BONUS)));
        model.addAttribute(ModelMapConstants.PROGRAMS, programMapper.toResponseDto(programService.findAll()));
        return "admin/offering/offering-edit";
    }

    @GetMapping("/offering/edit/{id}")
    public String editOffering(@PathVariable Long id, final ModelMap model) {
        return loadOfferingEditPage(offeringMapper.toUpdateDto(offeringService.findByIdFetchProgramsAndFolders(id)), model);
    }

    @PostMapping("/offering/edit/{id}")
    public String editOffering(@ModelAttribute(ModelMapConstants.OFFERING) @Valid final OfferingUpdateDto offeringUpdateDto, final BindingResult bindingResult, final ModelMap model) {
        Offering offering = offeringMapper.toEntity(offeringUpdateDto);

        validateOffering(offering, bindingResult);
        validateOfferingNameAndType(offering, bindingResult, false);
        if (bindingResult.hasErrors()) {
            model.addAttribute(ModelMapConstants.ERRORS, bindingResult.getFieldErrors());
            return loadOfferingEditPage(offeringUpdateDto, model);
        }
        model.addAttribute(ModelMapConstants.SUCCESS, "success.offering.edit");

        Offering savedOffering = offeringService.save(offering);
        return loadOfferingEditPage(offeringMapper.toUpdateDto(savedOffering), model);
    }

    /* ---------------------------------------------- USERS ---------------------------------------------- */

    @GetMapping("/users")
    public String users(final ModelMap model) {
        model.addAttribute(ModelMapConstants.USERS, userMapper.toResponseDto(userService.findAll()));
        return "admin/user/users";
    }

    /* ---------------------------------------------- EVENTS ---------------------------------------------- */

    @GetMapping("/events")
    public String events(final ModelMap model) {
        model.addAttribute(ModelMapConstants.EVENTS, eventMapper.toResponseDto(eventService.findAll()));
        return "admin/event/events";
    }

}
