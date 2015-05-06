package com.novartis.ecrs.model.am.common;

import java.util.List;

import oracle.jbo.ApplicationModule;
// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Sun May 03 13:34:24 IST 2015
// ---------------------------------------------------------------------
public interface ECRSAppModule extends ApplicationModule {
    boolean activateCrs(Long pCRSId, String pReasonForChange);

    void copyCurrentRiskRelation(Long srcRiskId, Long destCrsId);

    void copyRoutineDefinition(Long crsId);

    void deleteCrs();

    List fetchDatabases();

    List fetchDesignees();

    void filterCRSContent(String userInRole, String userName, boolean isInboxDisable);

    void initRiskRelation(Long crsId, String status);

    String modifyCrs(Long pCRSId, String pReasonForChange);


    boolean refreshRepository(Long crsId);


    boolean findByCrsFromStg(Long pCrsId);

    String reactivateCrs(Long pCRSId, String pReasonForChange);

    String retireCrs(Long pCRSId, String pReasonForChange);
}
