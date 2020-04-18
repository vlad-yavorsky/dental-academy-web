package ua.kazo.dentalacademy.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ua.kazo.dentalacademy.constants.ModelMapConstants;
import ua.kazo.dentalacademy.dto.offering.OfferingCreateDto;
import ua.kazo.dentalacademy.dto.offering.OfferingUpdateDto;
import ua.kazo.dentalacademy.entity.Offering;
import ua.kazo.dentalacademy.enumerated.FolderCategory;
import ua.kazo.dentalacademy.mapper.FolderMapper;
import ua.kazo.dentalacademy.mapper.OfferingMapper;
import ua.kazo.dentalacademy.mapper.ProgramMapper;
import ua.kazo.dentalacademy.service.FolderService;
import ua.kazo.dentalacademy.service.OfferingService;
import ua.kazo.dentalacademy.service.ProgramService;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminOfferingController {

    private final ProgramService programService;
    private final ProgramMapper programMapper;
    private final OfferingService offeringService;
    private final OfferingMapper offeringMapper;
    private final FolderService folderService;
    private final FolderMapper folderMapper;

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
        model.addAttribute(ModelMapConstants.PROGRAMS, programMapper.toResponseDto(programService.findAllWithFolders()));
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
        model.addAttribute(ModelMapConstants.PROGRAMS, programMapper.toResponseDto(programService.findAllWithFolders()));
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

}
