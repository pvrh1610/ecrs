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
        userVO.setWhereClause("ROLE_NAME = 'BSL'");
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
        
        ECrsSearchVORowImpl row = (ECrsSearchVORowImpl) searchVO.getCurrentRow();
        
        if (row.getReleaseStatus() != null)
            whereClause +=
                    "RELEASE_STATUS_FLAG = '" + row.getReleaseStatus() +
                    "' AND ";
        if (row.getCompoundType() != null)
                    whereClause +=
                            "CRS_COMPOUND_TYPE ='" + row.getCompoundType() +
                            "' AND ";
        if (row.getCompoundCodeId() != null)
            whereClause +=
                    "COMPOUND_ID =" + row.getCompoundCodeId() +
                    " AND ";
        //if user role is MQM - set state = 2 and 3 , TASL  - SET State = 4 , ML - set state = 5
        if (row.getState() != null)
            whereClause +=
                    "STATE_ID =" + row.getState() + " AND ";
        else if(isInboxDisable){
            if(ModelConstants.USER_IN_ROLE_MQM.equals(userInRole))
                whereClause +=
                        "STATE_ID IN (2,3) AND ";
            if(ModelConstants.USER_IN_ROLE_TASL.equals(userInRole))
                whereClause +=
                        "STATE_ID = 4 AND ";
            if(ModelConstants.USER_IN_ROLE_ML.equals(userInRole))
                whereClause +=
                        "STATE_ID = 5 AND ";
        }
            
        if (row.getGenericName() != null)
            whereClause +=
                    "GENERIC_NAME LIKE '%" + row.getGenericName() +
                    "%' AND ";
        if (row.getTradeName() != null)
            whereClause +=
                    "TRADE_NAME LIKE '%" + row.getTradeName() +
                    "%' AND ";

        if (row.getIndication() != null)
            whereClause +=
                    "INDICATION LIKE '%" + row.getIndication() +
                    "%' AND ";
        if (row.getMarketed() != null)
            whereClause +=
                    "IS_MARKETED_FLAG ='" + row.getMarketed() +
                    "' AND ";
        if (row.getDesignee() != null)
            whereClause += "DESIGNEE LIKE '%" + row.getDesignee() + "%' AND ";
//        else {
//            TODO if BSL set user role 
//            whereClause += "DESIGNEE LIKE '%" + userName + "%' AND ";
//        }

        if (row.getCrsTasl() != null)
            whereClause +=
                    "TASL_NAME ='" + row.getCrsTasl() + "' AND ";
        if (row.getCrsMedicalLead() != null)
            whereClause +=
                    "MEDICAL_LEAD_NAME ='" + row.getCrsMedicalLead() +
                    "' AND ";
        if (row.getCrsBsl() != null)
            whereClause +=
                    "BSL_NAME = '" + row.getCrsBsl() + "' AND ";
        
        if (row.getCrsName() != null)
            whereClause +=
                    "CRS_NAME LIKE '%" + row.getCrsName() +
                    "%' AND ";

        if (row.getCrsId() != null)
            whereClause +=
                    "CRS_ID LIKE '%" + row.getCrsId() +
                    "%' AND ";
        
        if (whereClause.endsWith("AND "))
            whereClause =
                    whereClause.substring(0, whereClause.length() - 4) + "";
        
        ViewObjectImpl crsContentVO = this.getCrsContentVO();
       // crsContentVO.setNestedSelectForFullSql(false);
        //System.out.println("----->"+whereClause);
       // System.out.println("--->>>"+ crsContentVO.getWhereClause());
        crsContentVO.setWhereClause(null);
        crsContentVO.applyViewCriteria(null);
        crsContentVO.executeQuery();
        //Apply whereClause to crsContentVO
        crsContentVO.setWhereClause(whereClause);
       // System.out.println("----->"+crsContentVO.getQuery());
        crsContentVO.executeQuery(); 
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
    
    public void initRiskRelation(Long crsId){
        ViewObject riskVO = this.getCrsRiskVO();
        riskVO.setWhereClause("CRS_ID = "+crsId);
        riskVO.executeQuery();
    }
}
