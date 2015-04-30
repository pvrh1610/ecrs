package com.novartis.ecrs.model.am.common;

import java.util.List;

import oracle.jbo.ApplicationModule;
// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Sat Apr 11 22:15:42 IST 2015
// ---------------------------------------------------------------------
public interface ECRSAppModule extends ApplicationModule {
    List fetchDesignees();

    void filterCRSContent(String userInRole, String userName,
                          boolean isInboxDisable);


    void copyRoutineDefinition(Long crsId);

    List fetchDatabases();

    void deleteCrs();

    void copyCurrentRiskRelation(Long srcRiskId, Long destCrsId);

    void initRiskRelation(Long crsId, String status);
}
