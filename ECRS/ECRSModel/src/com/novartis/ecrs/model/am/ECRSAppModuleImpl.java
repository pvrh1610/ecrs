package com.novartis.ecrs.model.am;


import com.novartis.ecrs.model.am.common.ECRSAppModule;
import com.novartis.ecrs.model.constants.ModelConstants;
import com.novartis.ecrs.model.view.ECrsSearchVORowImpl;
import com.novartis.ecrs.model.view.trans.CompoundTransientVOImpl;
import com.novartis.ecrs.model.view.trans.RiskPurposeTransientVOImpl;
import com.novartis.ecrs.model.view.trans.RolesTransientVOImpl;
import com.novartis.ecrs.model.view.trans.StateTransientVOImpl;
import com.novartis.ecrs.model.view.trans.UserRolesTransientVOImpl;

import java.util.ArrayList;
import java.util.List;

import oracle.jbo.Row;
import oracle.jbo.RowSetIterator;
import oracle.jbo.ViewObject;
import oracle.jbo.server.ApplicationModuleImpl;
import oracle.jbo.server.ViewLinkImpl;
import oracle.jbo.server.ViewObjectImpl;


// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Sat Apr 11 22:09:07 IST 2015
// ---    Custom code may be added to this class.
// ---    Warning: Do not modify method signatures of generated methods.
// ---------------------------------------------------------------------
public class ECRSAppModuleImpl extends ApplicationModuleImpl implements ECRSAppModule {
    /**
     * This is the default constructor (do not remove).
     */
    public ECRSAppModuleImpl() {
    }

    /**
     * Container's getter for CrsContentVO.
     * @return CrsContentVO
     */
    public ViewObjectImpl getCrsContentVO() {
        return (ViewObjectImpl)findViewObject("CrsContentVO");
    }


    /**
     * Container's getter for CrsUserRolesVO.
     * @return CrsUserRolesVO
     */
    public ViewObjectImpl getCrsUserRolesVO() {
        return (ViewObjectImpl)findViewObject("CrsUserRolesVO");
    }
    
    public List fetchDesignees(){
        List<String> designeeList = new ArrayList<String>();
        ViewObject userVO = this.getCrsUserRolesVO();
        userVO.setWhereClause("ROLE_NAME = '"+ModelConstants.ROLE_BSL+"'");
        userVO.executeQuery();
        if(userVO.getEstimatedRowCount() > 0){
            RowSetIterator rs = userVO.createRowSetIterator(null);
            while(rs.hasNext()){
                Row row = rs.next();
                designeeList.add((String)row.getAttribute("UserName"));
            }
            rs.closeRowSetIterator();
        }
        return designeeList;
    }
    
    public List fetchDatabases(){
        List<String> databaseList = new ArrayList<String>();
        ViewObject databaseVO = this.getCrsDatabasesLOVVO();
        databaseVO.executeQuery();
        if(databaseVO.getEstimatedRowCount() > 0){
            RowSetIterator rs = databaseVO.createRowSetIterator(null);
            while(rs.hasNext()){
                Row row = rs.next();
                databaseList.add((String)row.getAttribute("DatabaseName"));
            }
            rs.closeRowSetIterator();
        }
        return databaseList;
    }

    /**
     * Container's getter for CompoundTransientVO.
     * @return CompoundTransientVO
     */
    public CompoundTransientVOImpl getCompoundTransientVO() {
        return (CompoundTransientVOImpl)findViewObject("CompoundTransientVO");
    }

    /**
     * Container's getter for CrsCompoundVO.
     * @return CrsCompoundVO
     */
    public ViewObjectImpl getCrsCompoundVO() {
        return (ViewObjectImpl)findViewObject("CrsCompoundVO");
    }

    /**
     * Container's getter for RiskPurposeTransientVO.
     * @return RiskPurposeTransientVO
     */
    public RiskPurposeTransientVOImpl getRiskPurposeTransientVO() {
        return (RiskPurposeTransientVOImpl)findViewObject("RiskPurposeTransientVO");
    }

    /**
     * Container's getter for CrsRiskPurposesVO.
     * @return CrsRiskPurposesVO
     */
    public ViewObjectImpl getCrsRiskPurposesVO() {
        return (ViewObjectImpl)findViewObject("CrsRiskPurposesVO");
    }

    /**
     * Container's getter for RolesTransientVO.
     * @return RolesTransientVO
     */
    public RolesTransientVOImpl getRolesTransientVO() {
        return (RolesTransientVOImpl)findViewObject("RolesTransientVO");
    }

    /**
     * Container's getter for CrsRolesVO.
     * @return CrsRolesVO
     */
    public ViewObjectImpl getCrsRolesVO() {
        return (ViewObjectImpl)findViewObject("CrsRolesVO");
    }

    /**
     * Container's getter for ECrsSearchVO1.
     * @return ECrsSearchVO1
     */
    public ViewObjectImpl getECrsSearchVO() {
        return (ViewObjectImpl)findViewObject("ECrsSearchVO");
    }
    
    public void filterCRSContent(String userInRole,String userName,boolean isInboxDisable){
        String whereClause = "";
        ViewObjectImpl searchVO = this.getECrsSearchVO();
        
        //IF INBOX SELECTED - PENDING ONES IN USER'S QUEUE
        ECrsSearchVORowImpl row = null;
        if (isInboxDisable) {
            whereClause +=
                    "RELEASE_STATUS_FLAG = '" + ModelConstants.STATUS_PENDING +
            "' AND ";
            
            //if user role is MQM - set state = 2 and 3 , TASL - SET State = 4 , ML - set state = 5
            // set appropriate column names
            if (ModelConstants.ROLE_MQM.equals(userInRole))
                whereClause += "STATE_ID IN (2,3)";
            else if (ModelConstants.ROLE_TASL.equals(userInRole))
                whereClause += "STATE_ID = 4 AND TASL_NAME ='"+userName+"'";
            else if (ModelConstants.ROLE_ML.equals(userInRole))
                whereClause += "STATE_ID = 5 AND MEDICAL_LEAD_NAME ='"+userName+"'";          
            else if (ModelConstants.ROLE_BSL.equals(userInRole))
                whereClause += "STATE_ID NOT IN (2,4,5) AND ( BSL_NAME ='"+userName+"' OR DESIGNEE LIKE '%"+ userName+"%')";     

        // INBOX NOT SELECTED
        } else {

            row = (ECrsSearchVORowImpl)searchVO.getCurrentRow();

            if (row.getReleaseStatus() != null)
                whereClause += "RELEASE_STATUS_FLAG = '" + row.getReleaseStatus() + "' AND ";
            if (row.getCompoundType() != null)
                whereClause += "CRS_COMPOUND_TYPE ='" + row.getCompoundType() + "' AND ";
            if (row.getCompoundCodeId() != null)
                whereClause += "COMPOUND_ID =" + row.getCompoundCodeId() + " AND ";
            if (row.getState() != null)
                whereClause += "STATE_ID =" + row.getState() + " AND ";     
            if (row.getGenericName() != null)
                whereClause += "GENERIC_NAME LIKE '%" + row.getGenericName() + "%' AND ";
            if (row.getTradeName() != null)
                whereClause += "TRADE_NAME LIKE '%" + row.getTradeName() + "%' AND ";
            if (row.getIndication() != null)
                whereClause += "INDICATION LIKE '%" + row.getIndication() + "%' AND ";
            if (row.getMarketed() != null)
                whereClause += "IS_MARKETED_FLAG ='" + row.getMarketed() + "' AND ";
            if (row.getDesignee() != null)
                whereClause += "DESIGNEE LIKE '%" + row.getDesignee() + "%' AND ";
            if (row.getCrsTasl() != null)
                whereClause += "TASL_NAME ='" + row.getCrsTasl() + "' AND ";
            if (row.getCrsMedicalLead() != null)
                whereClause += "MEDICAL_LEAD_NAME ='" + row.getCrsMedicalLead() + "' AND ";            
            if (row.getCrsBsl() != null)
                whereClause += "BSL_NAME = '" + row.getCrsBsl() + "' AND ";
            if (row.getCrsName() != null)
                whereClause += "CRS_NAME LIKE '%" + row.getCrsName() + "%' AND ";
            if (row.getCrsId() != null)
                whereClause += "CRS_ID LIKE '%" + row.getCrsId() + "%' AND ";
            
            // DATA SECUTIRY - BSL/MQM/TASL/ML can view only their corresponding records
            if (ModelConstants.ROLE_MQM.equals(userInRole))
                whereClause += "((RELEASE_STATUS_FLAG = '" + ModelConstants.STATUS_PENDING + "' AND STATE_ID IN (2,3)) OR RELEASE_STATUS_FLAG = '" + ModelConstants.STATUS_CURRENT + "')";
            if (ModelConstants.ROLE_TASL.equals(userInRole))
                whereClause += "((RELEASE_STATUS_FLAG = '" + ModelConstants.STATUS_PENDING + "' AND STATE_ID = 4 AND TASL_NAME ='"+userName+"') OR RELEASE_STATUS_FLAG = '" + ModelConstants.STATUS_CURRENT + "')";
            if (ModelConstants.ROLE_ML.equals(userInRole))
                whereClause += "((RELEASE_STATUS_FLAG = '" + ModelConstants.STATUS_PENDING + "' AND STATE_ID = 5 AND MEDICAL_LEAD_NAME ='"+userName+"') OR RELEASE_STATUS_FLAG = '" + ModelConstants.STATUS_CURRENT + "')";        
            if (ModelConstants.ROLE_BSL.equals(userInRole))
                whereClause += "((RELEASE_STATUS_FLAG = '" + ModelConstants.STATUS_PENDING + "' AND STATE_ID NOT IN (2,4,5) AND ( BSL_NAME ='"+userName+"' OR DESIGNEE LIKE '%"+ userName+"%')) OR RELEASE_STATUS_FLAG = '" + ModelConstants.STATUS_CURRENT + "')";        
        }
        
        if (whereClause.endsWith("AND "))
            whereClause =
                    whereClause.substring(0, whereClause.length() - 4) + "";
        
        ViewObjectImpl crsContentVO = null;
        if (ModelConstants.STATUS_PENDING.equals(row.getReleaseStatus())) {
            crsContentVO = this.getCrsContentVO();
        } else {
            crsContentVO = this.getCrsContentBaseVO();
        }
        crsContentVO.setWhereClause(whereClause);
        crsContentVO.executeQuery();
        crsContentVO.setWhereClause(null);
        crsContentVO.applyViewCriteria(null);
        
        
       // crsContentVO.setNestedSelectForFullSql(false);
        //System.out.println("----->"+whereClause);
       // System.out.println("--->>>"+ crsContentVO.getWhereClause());
//        crsContentVO.setWhereClause(null);
//        crsContentVO.applyViewCriteria(null);
//        crsContentVO.executeQuery();
        //Apply whereClause to crsContentVO
       // System.out.println("----->"+crsContentVO.getQuery());
        
    }

    /**
     * Container's getter for CrsStateVO.
     * @return CrsStateVO
     */
    public ViewObjectImpl getCrsStateVO() {
        return (ViewObjectImpl)findViewObject("CrsStateVO");
    }

    /**
     * Container's getter for StateTransientVO.
     * @return StateTransientVO
     */
    public StateTransientVOImpl getStateTransientVO() {
        return (StateTransientVOImpl)findViewObject("StateTransientVO");
    }

    /**
     * Container's getter for UserRolesTransientVO.
     * @return UserRolesTransientVO
     */
    public UserRolesTransientVOImpl getUserRolesTransientVO() {
        return (UserRolesTransientVOImpl)findViewObject("UserRolesTransientVO");
    }

    /**
     * Container's getter for CrsRiskVO.
     * @return CrsRiskVO
     */
    public ViewObjectImpl getCrsRiskVO() {
        return (ViewObjectImpl)findViewObject("CrsRiskVO");
    }


    /**
     * Container's getter for CrsRiskRelationVO.
     * @return CrsRiskRelationVO
     */
    public ViewObjectImpl getCrsRiskRelationVO() {
        return (ViewObjectImpl)findViewObject("CrsRiskRelationVO");
    }

    /**
     * Container's getter for CrsRiskDefinitionsVO.
     * @return CrsRiskDefinitionsVO
     */
    public ViewObjectImpl getCrsRiskDefinitionsVO() {
        return (ViewObjectImpl)findViewObject("CrsRiskDefinitionsVO");
    }

    /**
     * Container's getter for CrsRiskRelationToRiskDefintionLink1.
     * @return CrsRiskRelationToRiskDefintionLink1
     */
    public ViewLinkImpl getCrsRiskRelationToRiskDefintionLink1() {
        return (ViewLinkImpl)findViewLink("CrsRiskRelationToRiskDefintionLink1");
    }
    
    /**
     *
     * @param crsId -- ID of the CRS that is being created or updated
     * 
     * Info : Metdod to show the risk definitions (if any) for the selected CRS on load of risk definitions page.
     */
    public void initRiskRelation(Long crsId, String status){
        if(status != null && "P".equals(status)){
            ViewObject riskVO = this.getCrsRiskVO();
            riskVO.setWhereClause("CRS_ID = "+crsId);
            riskVO.executeQuery();
            ViewObject riskBaseVO = this.getCrsRiskBaseVO();
            riskBaseVO.executeEmptyRowSet();
        }
        else{
            ViewObject riskBaseVO = this.getCrsRiskBaseVO();
            riskBaseVO.setWhereClause("CRS_ID = "+crsId);
            riskBaseVO.executeQuery();
            ViewObject riskVO = this.getCrsRiskVO();
            riskVO.executeEmptyRowSet();
        }
    }

    /**
     * Container's getter for CRSCurrentAndPendingCRSReport1.
     * @return CRSCurrentAndPendingCRSReport1
     */
    public ViewObjectImpl getCRSCurrentPendingCRSReport() {
        return (ViewObjectImpl)findViewObject("CRSCurrentPendingCRSReport");
    }

    /**
     * Container's getter for MedDRAComponentsReport1.
     * @return MedDRAComponentsReport1
     */
    public ViewObjectImpl getMedDRAComponentsReport() {
        return (ViewObjectImpl)findViewObject("MedDRAComponentsReport");
    }

    /**
     * Container's getter for MedDRAStandardReport1.
     * @return MedDRAStandardReport1
     */
    public ViewObjectImpl getMedDRAStandardReport() {
        return (ViewObjectImpl)findViewObject("MedDRAStandardReport");
    }

    /**
     * Container's getter for NumberOfCompoundCrsReport1.
     * @return NumberOfCompoundCrsReport1
     */
    public ViewObjectImpl getNumberOfCompoundCrsReport() {
        return (ViewObjectImpl)findViewObject("NumberOfCompoundCrsReport");
    }

    /**
     * Container's getter for RiskDefinitionsSafetyTopicReport1.
     * @return RiskDefinitionsSafetyTopicReport1
     */
    public ViewObjectImpl getRiskDefSafetyTopicReport() {
        return (ViewObjectImpl)findViewObject("RiskDefSafetyTopicReport");
    }

    /**
     * Container's getter for TotalNumOfADRsReport1.
     * @return TotalNumOfADRsReport1
     */
    public ViewObjectImpl getTotalNumOfADRsReport() {
        return (ViewObjectImpl)findViewObject("TotalNumOfADRsReport");
    }

    /**
     * Container's getter for TotalNumOfSafetyTopicsReport1.
     * @return TotalNumOfSafetyTopicsReport1
     */
    public ViewObjectImpl getTotalNumOfSafetyTopicsReport() {
        return (ViewObjectImpl)findViewObject("TotalNumOfSafetyTopicsReport");
    }
    
    /**
     *
     * @param crsId -- ID of the CRS to which the routine crs needs to be copied
     * 
     * Info : Method to copy the ROUTINE CRS risk definitons to the newly created CRS.
     */
    public void copyRoutineDefinition(Long crsId){
        ViewObject relationVO = this.getCrsRiskRelationVO();
        ViewObject definitionVO = this.getCrsRiskDefinitionsVO();
        ViewObjectImpl crsContentVO = this.getFetchCrsContentVO();
        crsContentVO.setWhereClause("CRS_NAME = 'ROUTINE'");
        crsContentVO.executeQuery(); 
        crsContentVO.setWhereClause(null);
        crsContentVO.applyViewCriteria(null);
        if(crsContentVO.getEstimatedRowCount() > 0){
            Row crsRow = crsContentVO.first();
            Long srcCrsId = (Long)crsRow.getAttribute("CrsId");
            ViewObject crsRiskVO = this.getCrsRiskVO();
            crsRiskVO.setWhereClause("CRS_ID = "+srcCrsId);
            crsRiskVO.setSortBy("CrsRiskId asc");
            crsRiskVO.executeQuery();
            Long crsRiskId = null;
            Long newCrsRiskId = null;
            if(crsRiskVO.getEstimatedRowCount() > 0){
                RowSetIterator rs = crsRiskVO.createRowSetIterator(null);
                while(rs.hasNext()){
                    Row row = rs.next();
                    if(crsRiskId == null || !crsRiskId.equals((Long)row.getAttribute("CrsRiskId"))){
                        crsRiskId = (Long)row.getAttribute("CrsRiskId");
                        Row relationRow = relationVO.createRow();
                        relationRow.setAttribute("CrsId", crsId);
                        relationRow.setAttribute("DataDomain", row.getAttribute("DataDomain"));
                        relationRow.setAttribute("DatabaseList", row.getAttribute("DatabaseList"));                        
                        String riskPurposeList = (String)row.getAttribute("RiskPurposeList");
                        if(riskPurposeList != null && riskPurposeList.endsWith(",")){
                            riskPurposeList = riskPurposeList.substring(0,riskPurposeList.length()-1);
                        }
                        relationRow.setAttribute("RiskPurposeList", riskPurposeList);
                        relationRow.setAttribute("SafetyTopicOfInterest", row.getAttribute("SafetyTopicOfInterest"));
                        relationRow.setAttribute("SocDictContentEntryTs", row.getAttribute("SocDictContentEntryTs"));
                        relationRow.setAttribute("SocDictContentId", row.getAttribute("SocDictContentId"));
                        relationRow.setAttribute("SocTerm", row.getAttribute("SocTerm"));
                        relationVO.insertRow(relationRow);
                        Row definitionRow = definitionVO.createRow();
                        newCrsRiskId = (Long)relationRow.getAttribute("CrsRiskId");
                        definitionRow.setAttribute("CrsRiskId", newCrsRiskId);
                        definitionRow.setAttribute("MeddraQualifier", row.getAttribute("MeddraQualifier"));
                        definitionRow.setAttribute("MeddraCode", row.getAttribute("MeddraCode"));
                        definitionRow.setAttribute("MeddraLevel", row.getAttribute("MeddraLevel"));
                        definitionRow.setAttribute("MeddraTerm", row.getAttribute("MeddraTerm"));
                        definitionRow.setAttribute("MeddraVersion", row.getAttribute("MeddraVersion"));
                        definitionRow.setAttribute("MeddraVersionDate", row.getAttribute("MeddraVersionDate"));
                        definitionRow.setAttribute("SearchCriteriaDetails", row.getAttribute("SearchCriteriaDetails"));
                        definitionRow.setAttribute("MeddraDict", row.getAttribute("MeddraDict"));
                        definitionRow.setAttribute("MeddraExtension", row.getAttribute("MeddraExtension"));
//                        definitionRow.setAttribute("TmsDictContentEntryTs", row.getAttribute("TmsDictContentEntryTs"));
//                        definitionRow.setAttribute("TmsDictContentId", row.getAttribute("TmsDictContentId"));
//                        definitionRow.setAttribute("TmsEndTs", row.getAttribute("TmsEndTs"));
//                        definitionRow.setAttribute("TmsUpdateFlag", row.getAttribute("TmsUpdateFlag"));
//                        definitionRow.setAttribute("TmsUpdateFlagDt", row.getAttribute("TmsUpdateFlagDt"));
                        definitionVO.insertRow(definitionRow);
                    } else { 
                        Row definitionRow = definitionVO.createRow();
                        definitionRow.setAttribute("CrsRiskId", newCrsRiskId);
                        definitionRow.setAttribute("MeddraQualifier", row.getAttribute("MeddraQualifier"));
                        definitionRow.setAttribute("MeddraCode", row.getAttribute("MeddraCode"));
                        definitionRow.setAttribute("MeddraLevel", row.getAttribute("MeddraLevel"));
                        definitionRow.setAttribute("MeddraTerm", row.getAttribute("MeddraTerm"));
                        definitionRow.setAttribute("MeddraVersion", row.getAttribute("MeddraVersion"));
                        definitionRow.setAttribute("MeddraVersionDate", row.getAttribute("MeddraVersionDate"));
                        definitionRow.setAttribute("SearchCriteriaDetails", row.getAttribute("SearchCriteriaDetails"));
                        definitionRow.setAttribute("MeddraDict", row.getAttribute("MeddraDict"));
                        definitionRow.setAttribute("MeddraExtension", row.getAttribute("MeddraExtension"));
//                        definitionRow.setAttribute("TmsDictContentEntryTs", row.getAttribute("TmsDictContentEntryTs"));
//                        definitionRow.setAttribute("TmsDictContentId", row.getAttribute("TmsDictContentId"));
//                        definitionRow.setAttribute("TmsEndTs", row.getAttribute("TmsEndTs"));
//                        definitionRow.setAttribute("TmsUpdateFlag", row.getAttribute("TmsUpdateFlag"));
//                        definitionRow.setAttribute("TmsUpdateFlagDt", row.getAttribute("TmsUpdateFlagDt"));
                        definitionVO.insertRow(definitionRow); 
                    }
                }
            }
//            this.getDBTransaction().commit();
        }
    }

    /**
     * Container's getter for CRSCountPerMeddraReportVO.
     * @return CRSCountPerMeddraReportVO
     */
    public ViewObjectImpl getCRSCountPerMeddraReportVO() {
        return (ViewObjectImpl)findViewObject("CRSCountPerMeddraReportVO");
    }

    /**
     * Container's getter for CrsDatabasesLOVVO.
     * @return CrsDatabasesLOVVO
     */
    public ViewObjectImpl getCrsDatabasesLOVVO() {
        return (ViewObjectImpl)findViewObject("CrsDatabasesLOVVO");
    }

    /**
     * Container's getter for HierarchySearchVO.
     * @return HierarchySearchVO
     */
    public ViewObjectImpl getHierarchySearchVO() {
        return (ViewObjectImpl)findViewObject("HierarchySearchVO");
    }
    
    /**
     * 
     * Info : Method to delete the current CRS, which internally deletes risk relations and risk definitions attached to it.
     */
    public void deleteCrs(){
        ViewObject crsContentVO = this.getCrsContentVO();
        ViewObject relationVO = this.getCrsRiskRelationVO();
        ViewObject definitionVO = this.getCrsRiskDefinitionsVO();
        Row crsRow = crsContentVO.getCurrentRow();
        if(crsRow != null){
            Long crsId = (Long)crsRow.getAttribute("CrsId");
            relationVO.setWhereClause("CRS_ID = "+crsId);
            relationVO.executeQuery();
            if(relationVO.getEstimatedRowCount() > 0){
                RowSetIterator relationRs = relationVO.createRowSetIterator(null);
                while(relationRs.hasNext()){
                    Row relationRow = relationRs.next();
                    relationVO.setCurrentRow(relationRow);
//                    Long riskId = (Long)relationRow.getAttribute("CrsRiskId");
//                    definitionVO.setWhereClause("CRS_RISK_ID = "+riskId);
//                    definitionVO.executeQuery();
                    if(definitionVO.getEstimatedRowCount() > 0){
                        RowSetIterator definitionRs = definitionVO.createRowSetIterator(null);
                        while(definitionRs.hasNext()){
                            Row definitionRow = definitionRs.next();
                            definitionRow.remove();
                        }
                    }
                    relationRow.remove();
                }
            }
            crsRow.remove();
            this.getDBTransaction().commit();
        }
    }

    /**
     * Container's getter for FetchCrsContentVO.
     * @return FetchCrsContentVO
     */
    public ViewObjectImpl getFetchCrsContentVO() {
        return (ViewObjectImpl)findViewObject("FetchCrsContentVO");
    }

    /**
     * Container's getter for HierarchyChildVO.
     * @return HierarchyChildVO
     */
    public ViewObjectImpl getHierarchyChildVO() {
        return (ViewObjectImpl)findViewObject("HierarchyChildVO");
    }


    /**
     * Container's getter for CopyCrsRiskVO.
     * @return CopyCrsRiskVO
     */
    public ViewObjectImpl getCopyCrsRiskVO() {
        return (ViewObjectImpl)findViewObject("CopyCrsRiskVO");
    }


    /**
     *
     * @param srcRiskId
     * @param destCrsId
     */
    public void copyCurrentRiskRelation(Long srcRiskId, Long destCrsId) {
        ViewObject relationVO = this.getCrsRiskRelationVO();
        ViewObject definitionVO = this.getCrsRiskDefinitionsVO();
        ViewObject crsRiskVO = this.getFetchCrsRiskVO();
        crsRiskVO.setWhereClause("CRS_RISK_ID = " + srcRiskId);
        crsRiskVO.executeQuery();
        Long crsRiskId = null;
        Long newCrsRiskId = null;
        if (crsRiskVO.getEstimatedRowCount() > 0) {
            RowSetIterator rs = crsRiskVO.createRowSetIterator(null);
            while (rs.hasNext()) {
                Row row = rs.next();
                if (crsRiskId == null || !crsRiskId.equals((Long)row.getAttribute("CrsRiskId"))) {
                    crsRiskId = (Long)row.getAttribute("CrsRiskId");
                    Row relationRow = relationVO.createRow();
                    relationRow.setAttribute("CrsId", destCrsId);
                    relationRow.setAttribute("DataDomain", row.getAttribute("DataDomain"));
                    relationRow.setAttribute("DatabaseList", row.getAttribute("DatabaseList"));
                    String riskPurposeList = (String)row.getAttribute("RiskPurposeList");
                    if (riskPurposeList != null && riskPurposeList.endsWith(",")) {
                        riskPurposeList = riskPurposeList.substring(0, riskPurposeList.length() - 1);
                    }
                    relationRow.setAttribute("RiskPurposeList", riskPurposeList);
                    relationRow.setAttribute("SafetyTopicOfInterest", row.getAttribute("SafetyTopicOfInterest"));
                    relationRow.setAttribute("SocDictContentEntryTs", row.getAttribute("SocDictContentEntryTs"));
                    relationRow.setAttribute("SocDictContentId", row.getAttribute("SocDictContentId"));
                    relationRow.setAttribute("SocTerm", row.getAttribute("SocTerm"));
                    relationVO.insertRow(relationRow);
                    Row definitionRow = definitionVO.createRow();
                    newCrsRiskId = (Long)relationRow.getAttribute("CrsRiskId");
                    definitionRow.setAttribute("CrsRiskId", newCrsRiskId);
                    definitionRow.setAttribute("MeddraQualifier", row.getAttribute("MeddraQualifier"));
                    definitionRow.setAttribute("MeddraCode", row.getAttribute("MeddraCode"));
                    definitionRow.setAttribute("MeddraLevel", row.getAttribute("MeddraLevel"));
                    definitionRow.setAttribute("MeddraTerm", row.getAttribute("MeddraTerm"));
                    definitionRow.setAttribute("MeddraVersion", row.getAttribute("MeddraVersion"));
                    definitionRow.setAttribute("MeddraVersionDate", row.getAttribute("MeddraVersionDate"));
                    definitionRow.setAttribute("SearchCriteriaDetails", row.getAttribute("SearchCriteriaDetails"));
                    definitionRow.setAttribute("MeddraDict", row.getAttribute("MeddraDict"));
                    definitionRow.setAttribute("MeddraExtension", row.getAttribute("MeddraExtension"));
//                    definitionRow.setAttribute("TmsDictContentEntryTs", row.getAttribute("TmsDictContentEntryTs"));
//                    definitionRow.setAttribute("TmsDictContentId", row.getAttribute("TmsDictContentId"));
//                    definitionRow.setAttribute("TmsEndTs", row.getAttribute("TmsEndTs"));
//                    definitionRow.setAttribute("TmsUpdateFlag", row.getAttribute("TmsUpdateFlag"));
//                    definitionRow.setAttribute("TmsUpdateFlagDt", row.getAttribute("TmsUpdateFlagDt"));
                    definitionVO.insertRow(definitionRow);
                } else {
                    Row definitionRow = definitionVO.createRow();
                    definitionRow.setAttribute("CrsRiskId", newCrsRiskId);
                    definitionRow.setAttribute("MeddraQualifier", row.getAttribute("MeddraQualifier"));
                    definitionRow.setAttribute("MeddraCode", row.getAttribute("MeddraCode"));
                    definitionRow.setAttribute("MeddraLevel", row.getAttribute("MeddraLevel"));
                    definitionRow.setAttribute("MeddraTerm", row.getAttribute("MeddraTerm"));
                    definitionRow.setAttribute("MeddraVersion", row.getAttribute("MeddraVersion"));
                    definitionRow.setAttribute("MeddraVersionDate", row.getAttribute("MeddraVersionDate"));
                    definitionRow.setAttribute("SearchCriteriaDetails", row.getAttribute("SearchCriteriaDetails"));
                    definitionRow.setAttribute("MeddraDict", row.getAttribute("MeddraDict"));
                    definitionRow.setAttribute("MeddraExtension", row.getAttribute("MeddraExtension"));
//                    definitionRow.setAttribute("TmsDictContentEntryTs", row.getAttribute("TmsDictContentEntryTs"));
//                    definitionRow.setAttribute("TmsDictContentId", row.getAttribute("TmsDictContentId"));
//                    definitionRow.setAttribute("TmsEndTs", row.getAttribute("TmsEndTs"));
//                    definitionRow.setAttribute("TmsUpdateFlag", row.getAttribute("TmsUpdateFlag"));
//                    definitionRow.setAttribute("TmsUpdateFlagDt", row.getAttribute("TmsUpdateFlagDt"));
                    definitionVO.insertRow(definitionRow);
                }
            }
        }
    }

    /**
     * Container's getter for FetchCrsRiskVO.
     * @return FetchCrsRiskVO
     */
    public ViewObjectImpl getFetchCrsRiskVO() {
        return (ViewObjectImpl)findViewObject("FetchCrsRiskVO");
    }

    /**
     * Container's getter for HierarchyChildDetailVO1.
     * @return HierarchyChildDetailVO1
     */
    public ViewObjectImpl getHierarchyChildDetailVO1() {
        return (ViewObjectImpl)findViewObject("HierarchyChildDetailVO1");
    }

    /**
     * Container's getter for HierarchyChildSelfLink1.
     * @return HierarchyChildSelfLink1
     */
    public ViewLinkImpl getHierarchyChildSelfLink1() {
        return (ViewLinkImpl)findViewLink("HierarchyChildSelfLink1");
    }

    /**
     * Container's getter for RetiredNmqCmqprevUsedReport1.
     * @return RetiredNmqCmqprevUsedReport1
     */
    public ViewObjectImpl getRetiredNmqCmqPrevUsedReport() {
        return (ViewObjectImpl)findViewObject("RetiredNmqCmqPrevUsedReport");
    }

    /**
     * Container's getter for CrsRiskBaseVO.
     * @return CrsRiskBaseVO
     */
    public ViewObjectImpl getCrsRiskBaseVO() {
        return (ViewObjectImpl)findViewObject("CrsRiskBaseVO");
    }

    /**
     * Container's getter for CrsContentBaseVO1.
     * @return CrsContentBaseVO1
     */
    public ViewObjectImpl getCrsContentBaseVO() {
        return (ViewObjectImpl)findViewObject("CrsContentBaseVO");
    }
}
