package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.AssetInstanceService;
import ar.edu.itba.paw.interfaces.ImageService;
import ar.edu.itba.paw.models.assetExistanceContext.interfaces.AssetInstance;
import ar.edu.itba.paw.models.viewsContext.implementations.SearchQueryImpl;
import ar.edu.itba.paw.models.viewsContext.interfaces.Page;
import ar.edu.itba.paw.models.viewsContext.interfaces.SearchQuery;
import ar.edu.itba.paw.webapp.form.SearchFilterSortForm;
import ar.edu.itba.paw.webapp.form.SnackbarControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
public class IndexViewController {
    private final ViewResolver viewResolver;
    private final AssetInstanceService assetInstanceService;
    private final ImageService imageService;
    @Autowired
    public IndexViewController(@Qualifier("viewResolver")final ViewResolver vr, AssetInstanceService assetInstanceService,ImageService imageService){
        this.viewResolver = vr;
        this.assetInstanceService = assetInstanceService;
        this.imageService = imageService;
    }

    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("path", "home");
    }
    @RequestMapping( "/")
    public ModelAndView indexView(){
        final ModelAndView mav = new ModelAndView("/views/index");

        return mav;
    }

    @RequestMapping(value = "/discovery", method = RequestMethod.GET)
    public ModelAndView discoveryView(
            @Valid @ModelAttribute("searchFilterSortForm") SearchFilterSortForm searchFilterSortForm,
            final BindingResult errors
            ){

        if(errors.hasErrors())
            return new ModelAndView("/views/discoveryView");

        int pageNum = searchFilterSortForm.getCurrentPage();

        Page page = assetInstanceService.getAllAssetsInstances(
                pageNum,
                new SearchQueryImpl( ( searchFilterSortForm.getAuthors() != null ) ? searchFilterSortForm.getAuthors() : new ArrayList<>(),
                        ( searchFilterSortForm.getLanguages() != null ) ? searchFilterSortForm.getLanguages() :  new ArrayList<>(),
                        ( searchFilterSortForm.getPhysicalConditions() != null ) ? searchFilterSortForm.getPhysicalConditions() : new ArrayList<>()
                )
        );

        final ModelAndView mav = new ModelAndView("/views/discoveryView");
        mav.addObject("books", page.getBooks());
        mav.addObject("nextPage", page.getCurrentPage() != page.getTotalPages());
        mav.addObject("previousPage", page.getCurrentPage() != 1);
        mav.addObject("currentPage", page.getCurrentPage());
        mav.addObject("totalPages", page.getTotalPages());
        mav.addObject("page", page.getCurrentPage());

        List<String> authors = page.getAuthors();
        mav.addObject("authors", authors != null ? authors : new ArrayList<>());

        List<String> languages = page.getLanguages();
        mav.addObject("languages", languages != null ? languages : new ArrayList<>());

        List<String> physicalConditions = page.getPhysicalConditions();
        mav.addObject("physicalConditions", physicalConditions != null ? physicalConditions : new ArrayList<>());

        return mav;
    }


    @RequestMapping( "/assetView")
    public ModelAndView assetView(){
        //El objeto ModelAndView nos deja detener el modelo y la view
        final ModelAndView mav = new ModelAndView("/views/assetView");
        return mav;
    }

    @RequestMapping( "/getImage/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable String id, HttpServletRequest request){
        byte[] array = imageService.getPhoto(Integer.parseInt(id));

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-type", MediaType.IMAGE_JPEG_VALUE);
        headers.set("Content-Disposition","inline; filename=\"whaterver.jpg\"");

        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(array);
    }
}
