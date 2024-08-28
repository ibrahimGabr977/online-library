package com.igb.web.online_library.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.igb.web.online_library.model.Patron;
import com.igb.web.online_library.repositories.PatronRepository;

import jakarta.transaction.Transactional;


@SuppressWarnings({ "null", "unchecked" })
@Service
public class PatronService {



    private static final String PATRONS = "patrons";
    private static final String ALL_PATRONS = "allPatrons";
    private final PatronRepository patronRepository;
    private final CacheManager cacheManager;




    public PatronService(PatronRepository patronRepository, CacheManager cacheManager) {
        this.patronRepository = patronRepository;
        this.cacheManager = cacheManager;
    }



  

    @Cacheable(value = PATRONS, key = ALL_PATRONS)
    public List<Patron> findAllPatrons() {
        return (List<Patron>) patronRepository.findAll();
    }

    public Patron findPatronBy(Long id) {
        return patronRepository.findById(id).orElse(null);

    }




    public Patron addNew(Patron patron) {
        List<Patron> allPatrons = allPatronsCache() == null ? new ArrayList<>() : allPatronsCache();
        allPatrons.add(patron);
        cacheManager.getCache(PATRONS).put(ALL_PATRONS, allPatrons);

        return patronRepository.save(patron);
    }




    @Transactional
    public Patron updatePatronWith(Long id, Patron updatedPatron) {
        List<Patron> allPatronsCache = allPatronsCache();

        Patron existingPatron = patronRepository.findById(id).orElseThrow(() -> new RuntimeException("Patron not found"));

        updatePatronFields(existingPatron, updatedPatron);

        updatePatronIfFoundOnCache(existingPatron, allPatronsCache);

        return patronRepository.save(existingPatron);
    }




    private void updatePatronFields(Patron patron, Patron updatedPatron) {
        patron.setName(updatedPatron.getName());
        patron.setContactInformation(updatedPatron.getContactInformation());
    }



    private void updatePatronIfFoundOnCache(Patron patron, List<Patron> allPatronsCache) {
        if (allPatronsCache != null) {
            for (int i = 0; i < allPatronsCache.size(); i++) {
                if (allPatronsCache.get(i).getId().equals(patron.getId())) {
                    allPatronsCache.set(i, patron);
                    cacheManager.getCache(PATRONS).put(ALL_PATRONS, allPatronsCache);
                    break;
                }
            }
        }

    }




    @Transactional
    public void deletePatronBy(Long id) {

        if (!patronRepository.existsById(id))
            throw new RuntimeException("Patron not found");

        patronRepository.deleteById(id);

        List<Patron> allPatrons = allPatronsCache();

        if (allPatrons != null) {
            allPatrons.removeIf(patron -> patron.getId().equals(id));

            cacheManager.getCache(PATRONS).put(ALL_PATRONS, allPatrons);
        }

    }


    private List<Patron> allPatronsCache() {
        return cacheManager.getCache(PATRONS).get(ALL_PATRONS, List.class);
    }


}